package com.orion.stocks.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String JDBC_DRIVE = "org.gjt.mm.mysql.Driver";
//    private static final String HOST_URL = "jdbc:mysql://localhost:3306/stocks";
    private static final String HOST_URL = "jdbc:mysql://localhost:3306/stocks_dev"; // development schema

    private static final String USERNAME = "rafael_ramores";
    private static final String PASSWORD = "1qazxsw@";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;

        Class.forName(JDBC_DRIVE);
        conn = DriverManager.getConnection(HOST_URL, USERNAME, PASSWORD);

        return conn;
    }

}
