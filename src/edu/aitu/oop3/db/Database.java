package edu.aitu.oop3.db;

import java.sql.SQLException;
import java.sql.Connection;

public interface Database {
    Connection getConnection() throws SQLException;
}
