package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import types.ChangedNode;
import types.ChangedRelationContainer;
import types.ChangedWay;
import types.MyOSM;
import types.State;
import br.zuq.osm.parser.model.Member;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Relation;
import br.zuq.osm.parser.model.Way;

/**
 * The RelationChecker contains the algorithm for checking an .myosm file.
 * 
 * @author sebastian
 * 
 */
public final class RelationChecker {
	private static List<ChangedRelationContainer> changedRelations;
	private static ChangedRelationContainer cRel;
	private static HashSet<Long> checkedNodes;
	private static HashMap<Long, Member> newRelations;
	private static HashMap<Long, Member> oldRelations;
	private static final Log log = LogFactory.getLog(RelationChecker.class);

	private RelationChecker() {
	}

	/**
	 * Triggers the checking of a document.
	 * 
	 * @param ori
	 *            The original file.
	 * @param upd
	 *            The new file.
	 * @return A list of ChangedRelationContainer. Each container contains a
	 *         whole relation with its ChangedNodes, ChangedWays and
	 *         ChangedRelationMembers. {@code null} if the docs could not be
	 *         compared.
	 */
	public static List<ChangedRelationContainer> checkDocument(MyOSM ori,
			MyOSM upd) {
		Long start = System.currentTimeMillis();
		changedRelations = new LinkedList<ChangedRelationContainer>();
		checkedNodes = new HashSet<Long>();

		if (ori != null && upd != null) {
			checkRelation(ori, upd);
			Long end = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("Checking time of " + ori.getQuery() + ": "
						+ (end - start) + " ms");
			}
		} else {
			log.info("The most recent version of the document could not be parsed!");
			return null;
		}
		return changedRelations;
	}

	/**
	 * Compares the two documents: original and updated.
	 * 
	 * @param ori
	 *            The original document.
	 * @param upd
	 *            The updated doc.
	 */
	private static void checkRelation(MyOSM ori, MyOSM upd) {
		Relation oriRel = null;
		Relation updRel = null;
		HashMap<Long, OSMNode> oriNodes = ori.getNodes();
		HashMap<Long, OSMNode> updNodes = upd.getNodes();
		HashMap<Long, Way> oriWays = ori.getWays();
		HashMap<Long, Way> updWays = upd.getWays();

		// für hinzugefügte und editierte knoten und wege
		for (Entry<Long, Relation> rel : upd.getRelations().entrySet()) {

			updRel = rel.getValue();
			newRelations = new HashMap<Long, Member>();
			oldRelations = new HashMap<Long, Member>();
			cRel = new ChangedRelationContainer(updRel);
			for (Member m : updRel.members) {
				if (m.type.equals("node")) {
					nodeCompare(m, oriNodes, updNodes);
				} else if (m.type.equals("way")) {
					checkAddedOrEditedWay(m, oriWays, updWays);
				} else if (m.type.equals("relation")) {
					newRelations.put(Long.parseLong(m.ref), m);
				}
			}
			// Auf gelöschte Member durchsuchen
			oriRel = ori.getRelations().get(Long.parseLong(updRel.id));
			if (oriRel != null) {
				for (Member m : oriRel.members) {
					if (m.type.equals("node")) {
						nodeDeleted(m, oriNodes, updNodes);
					} else if (m.type.equals("way")) {
						isDeletedWay(m, oriWays, updWays);
					} else if (m.type.equals("relation")) {
						oldRelations.put(Long.parseLong(m.ref), m);
					}
				}
				cRel.addChangedTags(ChangeFileCreator.getChangedTags(oriRel,
						updRel));
			} else {
				cRel.setState(State.CREATED);
				changedRelations.add(cRel);
			}

			checkMemberRelations();

			if (cRel.isEditedRelation()) {
				cRel.setState(State.EDITED);
				changedRelations.add(cRel);
			}
		}
		for (Entry<Long, Relation> rel : ori.getRelations().entrySet()) {
			oriRel = rel.getValue();
			updRel = upd.getRelations().get(Long.parseLong(oriRel.id));
			if (updRel == null) {
				cRel = new ChangedRelationContainer(oriRel);
				cRel.setState(State.REMOVED);
				changedRelations.add(cRel);
			}
		}

	}

	/**
	 * Checks if members that are relations are added or deleted.
	 */
	private static void checkMemberRelations() {
		// gelöschte Memberrelationen
		for (Entry<Long, Member> memRel : newRelations.entrySet()) {
			Member m = oldRelations.get(memRel.getKey());
			if (m == null) {
				// member relation entfernt
				cRel.addMemberRelation(memRel.getValue(), State.DELETED);
			}
		}
		for (Entry<Long, Member> memRel : oldRelations.entrySet()) {
			Member m = newRelations.get(memRel.getKey());
			if (m == null) {
				// member relation hinzugefügt
				cRel.addMemberRelation(memRel.getValue(), State.ADDED);
			}
		}
	}

	/**
	 * Checks if a node has been edited or added.
	 * 
	 * @param m
	 *            The member of the relation.
	 * @param oriNodes
	 *            The set of original nodes.
	 * @param updNodes
	 *            The set of updated nodes.
	 */
	private static void nodeCompare(Member m, HashMap<Long, OSMNode> oriNodes,
			HashMap<Long, OSMNode> updNodes) {
		Long nodeID = Long.parseLong(m.ref);
		OSMNode oriNode = oriNodes.get(nodeID);
		OSMNode updNode = updNodes.get(nodeID);
		if (oriNode != null) {
			checkIfEditedNode(oriNode, updNode, null);
		} else if (oriNode == null && updNode == null) {
			if (log.isDebugEnabled()) {
				log.debug("No complete document");
			}
		} else {
			ChangedNode tmp = ChangeFileCreator.createChNode(null, updNode,
					State.CREATED);
			cRel.addNode(tmp);
		}
	}

	/**
	 * Checks if a node has been deleted.
	 * 
	 * @param m
	 *            The node as member.
	 * @param oriNodes
	 *            The set of original nodes.
	 * @param updNodes
	 *            The set of updated nodes.
	 */
	private static void nodeDeleted(Member m, HashMap<Long, OSMNode> oriNodes,
			HashMap<Long, OSMNode> updNodes) {
		Long nodeID = Long.parseLong(m.ref);
		OSMNode oriNode = oriNodes.get(nodeID);
		OSMNode updNode = updNodes.get(nodeID);
		if (oriNode == null && updNode == null) {
			log.error("No complete document.");
		} else if (updNode == null) {
			ChangedNode tmp = ChangeFileCreator.createChNode(oriNode, null,
					State.DELETED);
			tmp.setUpdChangeset(cRel.getChangeset());
			cRel.addNode(tmp);
		}
	}

	/**
	 * Compares two nodes.
	 * 
	 * @param ori
	 *            The original node.
	 * @param upd
	 *            The updated node.
	 * @param cWay
	 *            The way which refers to the node or null if the node was a
	 *            member of the relation.
	 */
	private static void checkIfEditedNode(OSMNode ori, OSMNode upd,
			ChangedWay cWay) {
		boolean equal = (ori.id.equals(upd.id))
				&& (ori.version.equals(upd.version));
		if (!equal) {
			ChangedNode tmp = ChangeFileCreator.createChNode(ori, upd,
					State.EDITED);
			// keine doppelten Knoten im Container
			if (checkedNodes.add(Long.parseLong(ori.id))) {
				cRel.addNode(tmp);
			}
			if (cWay != null) {
				cWay.addChangedNode(tmp);
			}
		}
	}

	/**
	 * Checks if a way was deleted.
	 * 
	 * @param m
	 *            The way as member.
	 * @param oriWays
	 *            The set of original ways.
	 * @param updWays
	 *            The set of updated ways.
	 */
	private static void isDeletedWay(Member m, HashMap<Long, Way> oriWays,
			HashMap<Long, Way> updWays) {
		Long wayID = Long.parseLong(m.ref);
		Way oriWay = oriWays.get(wayID);
		Way updWay = updWays.get(wayID);
		if (oriWay == null && updWay == null) {
			if (log.isDebugEnabled()) {
				log.debug("No complete document");
			}
		} else if (updWay == null) {
			ChangedWay tmp = new ChangedWay(oriWay, State.DELETED);
			// setze channgeset in dem der weg gelöscht wurde
			tmp.setChangeset(cRel.getChangeset());
			cRel.addWay(tmp);
		}

	}

	/**
	 * Checks if a way has been edited or added.
	 * 
	 * @param m
	 *            The way as member.
	 * @param oriWays
	 *            The set of original ways.
	 * @param updWays
	 *            The set of updated ways.
	 */
	private static void checkAddedOrEditedWay(Member m,
			HashMap<Long, Way> oriWays, HashMap<Long, Way> updWays) {

		Long wayID = Long.parseLong(m.ref);
		Way oriWay = oriWays.get(wayID);
		Way updWay = updWays.get(wayID);
		if (oriWay != null) {
			// Weg wurde möglicherweise geändert
			compareWays(oriWay, updWay);
		} else if (oriWay == null && updWay == null) {
			log.error("No complete document.");
		} else {
			// Weg wurde hinzugefügt
			ChangedWay tmp = new ChangedWay(updWay, State.ADDED);
			cRel.addWay(tmp);
		}
	}

	/**
	 * Compares to ways.
	 * 
	 * @param ori
	 *            The original way.
	 * @param upd
	 *            The updated way.
	 */
	private static void compareWays(Way ori, Way upd) {
		boolean found = false;
		ChangedWay cWay = new ChangedWay(upd, State.EDITED);

		// neue oder editierte Knoten?
		for (OSMNode updNode : upd.nodes) {
			found = false;
			for (OSMNode oriNode : ori.nodes) {

				if (updNode.id.equals(oriNode.id)) {
					// editierten knoten im changedWay speichern
					checkIfEditedNode(oriNode, updNode, cWay);
					found = true;
				}
			}
			if (!found) {
				// Knoten zum Weg hinzugefügt
				ChangedNode edNode = ChangeFileCreator.createChNode(null,
						updNode, State.ADDED);
				// if (checkedNodes.add(Long.parseLong(ori.id))) {
				// cRel.addNode(edNode);
				// }
				cWay.addChangedNode(edNode);
			}
		}

		if (!ori.version.equals(upd.version)) {
			// gelöschte Knoten?
			for (OSMNode oriNode : ori.nodes) {
				found = upd.nodes.contains(oriNode);
				if (!found) {
					ChangedNode edNode = ChangeFileCreator.createChNode(
							oriNode, null, State.DELETED);
					// if (checkedNodes.add(Long.parseLong(ori.id))) {
					// cRel.addNode(edNode);
					// }
					cWay.addChangedNode(edNode);
				}

			}
			// andere Tags? hinzugefügt oder editiert?
			cWay.addChangedTags(ChangeFileCreator.getChangedTags(ori, upd));
		}
		if (cWay.containsChangedNodes()) {
			cRel.addWay(cWay);
		}
	}
}