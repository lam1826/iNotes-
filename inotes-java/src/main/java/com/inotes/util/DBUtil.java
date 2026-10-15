package com.inotes.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {
    private static final String URL =
            System.getenv().getOrDefault("INOTES_DB_URL",
                    "jdbc:mysql://localhost:3306/inotes_db?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8");
    private static final String USER = System.getenv().getOrDefault("INOTES_DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("INOTES_DB_PASS", "");

    private DBUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
