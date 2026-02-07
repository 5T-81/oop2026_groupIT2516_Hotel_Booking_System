package edu.aitu.oop3;

import edu.aitu.oop3.db.Database;
import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.PostgresDatabase;
import edu.aitu.oop3.entities.Guests;
import edu.aitu.oop3.entities.Reservations;
import edu.aitu.oop3.entities.Rooms;
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
import java.util.List;
import java.util.function.Predicate;


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
        PaymentRepository paymentRepo = new PaymentRepository(db); //repo payment
        GuestRepository guestRepository = new GuestRepository(db); //repo guest
        //setting room availability service
        RoomAvailabilityService availabilityService = new RoomAvailabilityService(reservationRepo);
        //setting payment service
        PaymentService paymentService = new PaymentService(paymentRepo, reservationRepo);
        //Reservation
        ReservationService reservationService = new ReservationService(reservationRepo, paymentService, availabilityService, guestRepository);

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
            boolean available = availabilityService.isRoomAvailable(room_id, check_in, check_out);
            System.out.println("Room " + room_id + " is available? " + available);
            //create guest first
            int guest_id = guestRepository.createGuest("User01", "Testing");
            //create reservation
            int reservation_id = reservationService.createReservation(guest_id, room_id, check_in, check_out);
            System.out.println("Reservation's ID: " + reservation_id + " is created.");
            //payment (success)
            int payment_id = paymentRepo.createPayment(reservation_id, 100.0, "Card");
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
            System.out.println("Reservation's ID: " + reservation_id + " is cancelled.");

        } catch (InvalidDateRangeException | RoomNotAvailableException | PaymentDeclinedException e) {
            System.out.println("Business error: " + e.getMessage());
        }

// Milestone 2 STARTS HERE
// ====================================================================================
        // using lambda: search for available room:
        RoomSearchService searchService = new RoomSearchService();

        List<Rooms> rooms = List.of(
                //FACTORY usage: create a list of rooms with type(suite, studio, standard)
                RoomFactory.createRoom(RoomType.STANDARD, 1, "AVAILABLE"),
                RoomFactory.createRoom(RoomType.SUITE, 2, "AVAILABLE"),
                RoomFactory.createRoom(RoomType.STUDIO, 3, "MAINTENANCE"),
                RoomFactory.createRoom(RoomType.STANDARD, 4, "AVAILABLE"),
                RoomFactory.createRoom(RoomType.STUDIO, 5, "BOOKED")
        );
        //using lambda to search of an empty room
        List<Rooms> availableRooms = searchService.filterRooms(
                rooms,
                room -> availabilityService.isRoomAvailable(
                        room.getRoom_id(),
                        check_in,
                        check_out
                )
        );
        //print available rooms list
        System.out.println("Available room IDs:");
        for (Rooms r : availableRooms) {
            System.out.println(" - Room ID: " + r.getRoom_id());
        }
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for these dates.");
            return;
        }

        Rooms chosenRoom = availableRooms.getFirst(); // first available room
        int chosen_room_id = chosenRoom.getRoom_id();// id
        RoomType chosenType = chosenRoom.getType(); //room type
        //print the choice
        System.out.println(
                "Chosen room: " +
                        chosenType +
                        " | ID = " + chosen_room_id
        );

        //SINGLETON usage: calculate price for suite
        double price = PricingPolicy.getInstance().calculatePrice(chosenType);
        System.out.println("Singleton price quote for " + chosenType + " = " + price);

        //BUILDER usage: build a reservation details object
        //create guest first
        int guest_id = guestRepository.createGuest("UserMILESTONE2", "Testing");
        ReservationDetails details = new ReservationDetails.Builder()
                .guest_id(guest_id)
                .room_id(chosen_room_id)
                .check_in(check_in)
                .check_out(check_out)
                .paymentMethod("Card")
                .amount(price)
                .build();
        System.out.println("ReservationDetails created using Builder");

        //create reservation in DB
        int reservation_id = reservationService.createReservation(
                guest_id,
                chosen_room_id,
                check_in,
                check_out
        );
        System.out.println("Reservation's ID: " + reservation_id + " is created.");
        //pay the price(we already calculated above)
        int payment_id = paymentRepo.createPayment(reservation_id, price, "Card");
        System.out.println("Payment success. Payment ID: " + payment_id);


        // D) GENERICS usage
        Repository<Reservations, Integer> genericReservationRepo =
                (Repository<Reservations, Integer>) reservationRepo;
        // call findbyID
        System.out.println("Generic Repository<Reservations, Integer> initialized");
        Reservations found = genericReservationRepo.findById(reservation_id);
        System.out.println("Generic findById returned: " + (found == null ? "null" : found.getReservation_id()));


        //cancel the reservation
        reservationService.cancelReservation(reservation_id);
        System.out.println("Reservation's ID: " + reservation_id + " is cancelled.");



// =======================
// Milestone 2 DEMO END
// ==================================================================

        System.out.println("Finished:)");
    }
}
