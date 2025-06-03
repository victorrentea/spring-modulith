package victor.training.modulith;

import java.io.File;
import java.sql.SQLException;

// support class: start an in-memory H2 database server that the app can connect to
public class StartDatabase {
	public static void main(String[] args) throws SQLException {
		System.out.println("Started DB...");
		System.out.println("Connecting to 'jdbc:h2:tcp://localhost:9092/~/test' will auto-create a database file 'test.mv.db' in user home (~)...");

		deletePreviousDB();

		// Allow auto-creating new databases on blockDisk at first connection
		org.h2.tools.Server.createTcpServer("-ifNotExists").start();
	}

	private static void deletePreviousDB() {
		File dbFile = new File(System.getProperty("user.home"), "test.mv.db");
		System.out.println("Db file: " +dbFile.getAbsolutePath());

		if (dbFile.isFile()) {
			boolean r = dbFile.delete();
			if (!r) {
				System.err.println("Could not delete previous db content!");
			} else {
				System.out.println("Previous DB contents wiped out");
			}
		}
	}
}
