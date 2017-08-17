package src.database;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DatabaseTest extends TestCase {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new TestSuite(DatabaseTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/** Test the admin */
	public void testIsAdmin() {
		assertTrue(Database.isAdmin("root", "123456"));
	}

	/** Test the updataStstus method. */
	public void testAddControlRecords() {
		Database.updateStatus(0, "up", 5);
		System.out.println("status:" + Database.getStatus(0));
		assertEquals("up", Database.getStatus(0));
		Database.updateStatus(0, "stop", 1);
	}
}
