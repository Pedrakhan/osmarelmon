package test;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import parser.Parser;
import rss.RSSFeedManager;
import types.ChangedRelationContainer;
import types.MyOSM;
import util.RelationChecker;

/**
 * Test for relation checking. Always checks to prepared documents.
 * 
 * @author sebastian
 * 
 */
public class CheckTest {
	private static final Log log = LogFactory.getLog(CheckTest.class);

	/**
	 * Tests to documents stored my directory.
	 */
	@Test
	public void test() {
		String oriFile = "/home/sebastian/Dropbox/Bachelorarbeit/OSMKarten/ÖPNVSR";
		String updFile = "/home/sebastian/Dropbox/Bachelorarbeit/OSMKarten/ÖPNVSR_new";
		for (int i = 0; i < 1; i++) {
			MyOSM upd = null;
			MyOSM ori = null;
			try {
				ori = Parser.parseOSMFile(oriFile, "ÖPNVSR");
				upd = Parser.parseOSMFile(updFile, "ÖPNVSR_new");
			} catch (Exception e) {
				System.err.println("Parsing failed!");
			}
			// compare the two versions
			List<ChangedRelationContainer> con = RelationChecker.checkDocument(
					ori, upd);
			StringBuilder content = new StringBuilder();
			int changes = 0;
			for (ChangedRelationContainer c : con) {
				content.append(c.print());
				changes += c.getChangeSize();
			}
			System.out.println(changes);
			String description = content.toString();
			System.out.println(description);
			RSSFeedManager.generateRSSEntry("ÖPNVSR", "Changes at ÖPNVSR",
					description);
		}
	}
}
