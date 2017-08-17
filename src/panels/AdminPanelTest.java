package src.panels;

import junit.framework.TestCase;
import src.database.Database;
import src.share.Queue;

public class AdminPanelTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/** test start and stop. */
	public void testSetStatus() {
		AdminPanel ad = new AdminPanel(new Queue[1]);
		ad.setStatus(0, 7, "start");
		assertEquals("start", Database.getStatus(0));
		ad.setStatus(0, 1, "stop");
	}

}
