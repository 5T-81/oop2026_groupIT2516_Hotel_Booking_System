package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.Database;

import java.sql.*;
import java.time.LocalDate;

public class GuestRepository {
    private final Database db;//only one field
    //constructor
    public GuestRepository(Database db) {
        this.db = db;
    }
    //method
    public int createGuest(String first_name, String last_name){
        String sql = "INSERT INTO guests (first_name, last_name) VALUES (?, ?)";
        try (Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, first_name);
            stmt.setString(2, last_name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()){
                rs.next();
                return rs.getInt(1);

            }
        }catch (SQLException e){
            throw new RuntimeException("DB error while creating guest");
        }
    }
}
