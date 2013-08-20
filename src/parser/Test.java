package parser;

import java.util.HashMap;

import types.MyOSM;
import br.zuq.osm.parser.model.OSMNode;

/**
 * 
 * @author sebastian
 * 
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		String url = "/home/sebastian/Dropbox/Bachelorarbeit/OSMKarten/pa1.osm";
		System.out.println(url);
		MyOSM osm = null;
		try {
			osm = Parser.parseOSMFile(url, "parsertest");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Long, OSMNode> nodeMap = osm.getNodes();
		System.out.println(nodeMap);
		OSMNode node = nodeMap.get(new Long(2106022100));
		System.out.println(node);
	}
}