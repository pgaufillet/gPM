package org.topcased.dbutils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This script replaces invalid characters form the attached file name table with underscores.
 * See DM SUMO 1190
 * 
 * @author Olivier Juin, 2014
 */
public class RemoveInvalidCharacters {

	private static final String DATASOURCE_PATH = "dataSource.properties";

	public static void main(String[] args) {

		// Invalid chars: square, arobace, quote, backquote, dollar 
		char[] lInvalidChars = new char[] { 178, '@', '\'', '`', '$' };
		
		// Open database
		Connection lConn = openDB(DATASOURCE_PATH);

		try {
			Statement stmt = lConn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, name FROM attached_field_value");
			PreparedStatement ps = lConn.prepareStatement("UPDATE attached_field_value SET name=? WHERE id=?");
			int number = 0;
			int total = 0;
			while (rs.next()) {
				String id = rs.getString(1);
				String name = rs.getString(2);
				if (id == null || name == null) {
					System.out.println("Invalid entry. ID:" + id + ", NAME:" + name);
					continue;
				}
				String previousName = name;
				boolean replaced = false;
				for (char c : lInvalidChars) {
					if (name.indexOf(c) != -1) {
						name = name.replace(c, '_');
						replaced = true;
					}
				}
				if (replaced) {
					ps.setString(1, name);
					ps.setString(2, id);
					ps.addBatch();
					System.out.println(previousName + " -> " + name);
					number++;
				}
				total++;
			}
			if (number > 0) {
				System.out.println("Commiting changes...");
				ps.executeBatch();
			}
			System.out.println(number + " out of " + total + " entries replaced.");
		} catch (SQLException ex) {
			System.err.println("Query failed with: " + ex.getMessage()); 
		} finally {
			try {
				lConn.close();
			} catch (SQLException ex) {
				// Do nothing
			}
		}
	}

	private static Connection openDB(String pPropertiesPath) {
		Properties dbProp = new Properties();
		try {	          
			InputStream stream =
					Thread.currentThread().getContextClassLoader().getResourceAsStream(pPropertiesPath);
			dbProp.load(stream);
			stream.close(); 		
		} catch (Exception e) {
			System.err.println("Cannot read database configuration file '" + pPropertiesPath + "'.");
			System.exit(-1);
		}
		try {
			// Load the JDBC driver
			Class.forName(dbProp.getProperty("jdbc.driverClassName")); 
			// Establish the connection to the database 
			return DriverManager.getConnection(
					dbProp.getProperty("jdbc.url"),
					dbProp.getProperty("jdbc.username"),
					dbProp.getProperty("jdbc.password"));
		} catch (ClassNotFoundException ex) {
			System.err.println("Driver " + dbProp.getProperty("jdbc.driverClassName") + " could not be loaded."); 
			System.exit(-1);
		} catch (SQLException ex) {
			System.err.println("Could not connect to database: " + ex.getMessage()); 
			System.err.println("Maybe you should add the driver jar to the project ?");
			System.exit(-1);
		}
		return null; // Never reached
	} 
}
