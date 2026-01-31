package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.Database;
import edu.aitu.oop3.entities.Guests;
import edu.aitu.oop3.entities.Reservations;
import edu.aitu.oop3.milestone2.generics.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostgresReservationRepository implements ReservationRepository, Repository<Reservations, Integer> {
    private final Database db;
    //constructor
    public PostgresReservationRepository(Database db) {
        this.db = db;
    }

    @Override
    public int create(Reservations reservation) {
        String sql = "INSERT INTO Reservations (guest_id, room_id, check_in, check_out) VALUES (?,?,?,?)";

        try(Connection connection = db.getConnection();
            //“Hey, when you run this INSERT, please remember the ID that the database generates.”
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //if sql string has placeholders ?, use setX()
            stmt.setInt(1,reservation.getGuest_id());//comes from the obj -->create(obj)
            stmt.setInt(2, reservation.getRoom_id());
            stmt.setDate(3, Date.valueOf(reservation.getCheck_in()));
            stmt.setDate(4,Date.valueOf(reservation.getCheck_out()));

            int rows = stmt.executeUpdate(); //actual insert to the DB (returns: 0 - nothing inserted or 1 - one row inserted)
            if (rows == 0) {
                throw new RuntimeException("Creating reservation failed, no rows affected.");
            }
            try (ResultSet rs =stmt.getGeneratedKeys()) { //This returns a ResultSet containing:the auto-generated values ,usually the primary key (reservation_id)
                if (rs.next()) { //Cursor starts before first row, next() moves to the first row
                    //this is the returned value from create method
                    return rs.getInt(1); //column 1 of the generated keys result which is in this case the primary key
                } else {
                    throw new RuntimeException("Creating reservation failed, no ID obtained.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating reservation", e);
        }
    }




    @Override
    public Reservations findReservationById(int id) {
        String sql = "SELECT * FROM Reservations WHERE reservation_id = ?";

        try(Connection connection = db.getConnection();  PreparedStatement stmt = connection.prepareStatement(sql)){


            stmt.setInt(1, id);// fill the placeholder in sql

            //without ResultSet SQL never execute, and no data is fetched
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()) { //if row exists with this id

                    //create obj to store the extracted data
                    Reservations res = new Reservations( rs.getInt("reservation_id"),
                            rs.getInt("guest_id"),
                            rs.getInt("room_id"),
                            rs.getDate("check_in").toLocalDate(),
                            rs.getDate("check_out").toLocalDate());
                    //finally return the extracted data
                    return res;

                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while finding reservation",e);
        }
    }

    @Override
    public List<Reservations> findAll() {

        String sql = "SELECT * FROM Reservations";

        try(Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){

            List<Reservations> list = new ArrayList<>();
            while(rs.next()) {
                Reservations res = new Reservations(
                        rs.getInt("reservation_id"),
                        rs.getInt("guest_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate()
                );
                list.add(res);
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while finding reservations",e);
        }
    }
    @Override
    public Reservations findById(Integer id) {
        return findReservationById(id);
    }

    @Override
    public void cancel(int id) {
        String sql =  """
             UPDATE Reservations
             SET status = 'cancelled'
             WHERE reservation_id = ?
        """;

        try(Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(sql);) {

            stmt.setInt(1, id); //place id in the placeholder
            int rows = stmt.executeUpdate(); //update the database(deleting)

            if (rows == 0) {//no row was updated/deleted, so id doesn't exist
                throw new RuntimeException("No reservation found with id " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while canceling reservation",e);
        }
    }

    @Override
    public boolean isRoomAvailable(int roomId, LocalDate checkIn, LocalDate checkOut) {

        if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        String sql = """
                SELECT COUNT(*) FROM Reservations r
                JOIN Rooms ro ON r.room_id = ro.room_id
                WHERE r.room_id =?
                AND ro.status = 'available'
                AND r.status <> 'cancelled'
                AND r.check_in < ? AND r.check_out > ?
                """;

        try(Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)){
            //set placeholders
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOut));// r.check_in < checkOut(from the parameters)
            stmt.setDate(3, Date.valueOf(checkIn));// r.check_out > checkIn

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                int count = rs.getInt(1);

                return count == 0; //if count = 0 is true, otherwise = false
            }

        }catch (SQLException e) {
            throw new RuntimeException("DB error while checking availability",e);
        }
    }
}
