package edu.aitu.oop3;

import edu.aitu.oop3.db.Database;
import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.PostgresDatabase;
import edu.aitu.oop3.repositories.GuestRepository;
import edu.aitu.oop3.repositories.PaymentRepository;
import edu.aitu.oop3.repositories.PostgresReservationRepository;
import edu.aitu.oop3.repositories.ReservationRepository;
import edu.aitu.oop3.services.PaymentService;
import edu.aitu.oop3.services.ReservationService;
import edu.aitu.oop3.services.RoomAvailabilityService;
import edu.aitu.oop3.services.exceptions.InvalidDateRangeException;
import edu.aitu.oop3.services.exceptions.PaymentDeclinedException;
import edu.aitu.oop3.services.exceptions.RoomNotAvailableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


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

        //creating repos
        ReservationRepository reservationRepo = new PostgresReservationRepository(db); //repo reserv
        PaymentRepository paymentRepo =new PaymentRepository(db); //repo payment
        GuestRepository guestRepository  = new GuestRepository(db); //repo guest
        //setting room availability service
        RoomAvailabilityService availabilityService = new RoomAvailabilityService(reservationRepo);
        //setting payment service
        PaymentService paymentService = new PaymentService(paymentRepo, reservationRepo);
        //Reservation
        ReservationService reservationService = new ReservationService(reservationRepo,paymentService,availabilityService,guestRepository);

        //2) user story:
        //check availability before any reservation
        LocalDate check_in = LocalDate.of(2026, 1, 20);
        LocalDate check_out = LocalDate.of(2026, 1, 31);
        System.out.println("DEBUG checkIn = " + check_in);
        System.out.println("DEBUG checkOut = " + check_out);

        int room_id = 3;
        boolean available = availabilityService.isRoomAvailable(room_id,check_in,check_out);
        System.out.println("Room"+ room_id+ " is available? " + available);

        //3) user story:
        //create guest first
        int guest_id = guestRepository.createGuest("User44","Testing4");
        try {
            //create reservation
            int reservation_id = reservationService.createReservation(guest_id,room_id,check_in,check_out);
            System.out.println("Reservation's ID: " + reservation_id + " is created.");
            //payment (success)
            int payment_id = paymentRepo.createPayment(reservation_id, 100.0,"Card");
            System.out.println("Payment success. Payment ID: " + payment_id);
            //payment decline (test)
            try {
                paymentService.pay(reservation_id, 0.0, "card"); // should fail
            } catch (PaymentDeclinedException e) {
                System.out.println("Payment declined test OK: " + e.getMessage());
            }
            //test reservation overlapping
            //int reservation2_id = reservationService.createReservation(guest_id,room_id,check_in,check_out);
            //must be commented so that we can cancel any run!!!

            //4) user story: cancellation
            reservationService.cancelReservation(reservation_id);
            System.out.println("Reservation's ID: " + reservation_id +" is cancelled.");

        } catch (InvalidDateRangeException | RoomNotAvailableException | PaymentDeclinedException e) {
            System.out.println("Business error: "+ e.getMessage());
        }


    }
}
