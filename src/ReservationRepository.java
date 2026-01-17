import java.time.LocalDate;
//JOB:Talk to the database only.
public interface ReservationRepository {
    long create(Reservation reservation); //save something in the db
    Reservation findById(long id); //get one row by its ID
    Llist<Reservation> findAll(); // get all rows
    void cancel(long id); //delete a reservation
    boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut);
}
