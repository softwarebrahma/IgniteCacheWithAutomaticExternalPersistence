package com.brocade.dcm.server.service;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestIgniteJDBCSupport {
	
	static {
		try {
			Class.forName("org.apache.ignite.IgniteJdbcDriver");
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public static void testConnection() {
		try (Connection conn = DriverManager.getConnection("jdbc:ignite:cfg://file:///ignite-jdbc.xml");) {
			
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
}
