package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.Database;

import java.sql.*;

public class PaymentRepository {
    private final Database db;//only filed

    //constructor
    public PaymentRepository(Database db) {
        this.db = db;
    }
    //method
    public int createPayment(int reservation_id, double amount, String payment_method) {
        String sql = """
                INSERT INTO payments(reservation_id,amount, payment_method)
                VALUES (?, ?, ?)
                """;

        try(Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //set the values
            stmt.setInt(1, reservation_id);
            stmt.setDouble(2, amount);
            stmt.setString(3, payment_method);

            stmt.executeUpdate(); //actual inserting
                //
            try (ResultSet rs = stmt.getGeneratedKeys()) {//from stmt returned value of id
                rs.next();
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating payment", e);
        }
    }

}
