package edu.aitu.oop3;

import edu.aitu.oop3.db.Database;
import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.PostgresDatabase;
import edu.aitu.oop3.entities.Guests;
import edu.aitu.oop3.milestone2.Singleton.PricingPolicy;
import edu.aitu.oop3.milestone2.builder.ReservationDetails;
import edu.aitu.oop3.milestone2.factory.RoomFactory;
import edu.aitu.oop3.milestone2.factory.RoomType;
import edu.aitu.oop3.milestone2.generics.Repository;
import edu.aitu.oop3.milestone2.lambda.RoomSearchService;
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


public class    Main {
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
        //enter date
        LocalDate check_in = LocalDate.of(2026, 2, 10);
        LocalDate check_out = LocalDate.of(2026, 2, 20);
        System.out.println("DEBUG checkIn = " + check_in);
        System.out.println("DEBUG checkOut = " + check_out);
        //room for reservation
        int room_id = 5;

        try {
            //check availability before any reservation
             boolean available = availabilityService.isRoomAvailable(room_id,check_in,check_out);
            System.out.println("Room "+ room_id+ " is available? " + available);
            //create guest first
            int guest_id = guestRepository.createGuest("User44","Testing4");
            //create reservation
            int reservation_id = reservationService.createReservation(guest_id,room_id,check_in,check_out);
            System.out.println("Reservation's ID: " + reservation_id + " is created.");
            //payment (success)
            int payment_id = paymentRepo.createPayment(reservation_id, 100.0,"Card");
            System.out.println("Payment success. Payment ID: " + payment_id);
            //payment decline (test)
            try {
                paymentService.pay(reservation_id, 0, "card"); // should fail
            } catch (PaymentDeclinedException e) {
                System.out.println("Payment declined test: " + e.getMessage());
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
        // =======================
// Milestone 2 DEMO START
// =======================

// A) FACTORY usage: create a room object by type
        var roomObj = RoomFactory.createRoom(RoomType.SUITE, room_id, "AVAILABLE");
        System.out.println("Factory created room class: " + roomObj.getClass().getSimpleName());

// B) SINGLETON usage: calculate price (example)
        double quotedPrice = PricingPolicy.getInstance().calculatePrice(RoomType.SUITE);
        System.out.println("Singleton price quote for SUITE = " + quotedPrice);

// C) BUILDER usage: build a reservation details object
        ReservationDetails details = new ReservationDetails.Builder()
                .guest_id(999) // demo value, or use real guest_id after creating guest
                .room_id(room_id)
                .check_in(check_in)
                .check_out(check_out)
                .paymentMethod("Card")
                .amount(quotedPrice)
                .build();
        System.out.println("ReservationDetails created using Builder");

// D) GENERICS usage: treat a repo as Repository<Guest, Integer>
       // Repository<PostgresReservationRepository, Integer> genericGuestRepo =
            //    (Repository<edu.aitu.oop3.entities.Reservations, Integer>) reservationRepo;

// just call it once to prove it compiles/runs (or comment out if you don't want DB hit)
// edu.aitu.oop3.entities.Guest g = genericGuestRepo.findById(1);

// E) LAMBDA usage: filtering rooms (demo list)
// If you don't have a list of rooms from DB yet, just demonstrate with a list you create.
        RoomSearchService searchService = new RoomSearchService();
        var rooms = java.util.List.of(
                RoomFactory.createRoom(RoomType.STANDARD, 1, "available"),
                RoomFactory.createRoom(RoomType.SUITE, 2, "maintenance"),
                RoomFactory.createRoom(RoomType.DORM, 3, "available")
        );

        var availableRooms = searchService.filterRooms(rooms,
                r -> "available".equals(r.getStatus())   // <-- lambda
        );

        System.out.println("Lambda filtered available rooms count = " + availableRooms.size());

// =======================
// Milestone 2 DEMO END
// =======================


        System.out.println("Finished:)");
    }
}
