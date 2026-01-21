package edu.aitu.oop3;

import edu.aitu.oop3.db.Database;
import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.PostgresDatabase;
import edu.aitu.oop3.repositories.PostgresReservationRepository;
import edu.aitu.oop3.repositories.ReservationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        //0) quick connection check of database
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            System.out.println("Error while connecting to database:");
            e.printStackTrace();
            return;
        }

        //1) create database
        Database db = new PostgresDatabase();

        //creating repo
        ReservationRepository reservationRepo = new PostgresReservationRepository(db);



    }
}
