import edu.aitu.oop3.db.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

public class PostgresReservationRepository implements ReservationRepository{
    private final Database db;
    public PostgresReservationRepository(Database db) {
        this.db = db;
    }

    @Override
    public long create(Reservation reservation) {
        String sql = "INSERT INTO RESERVATION (guest_id, room_id, check_in, check_out, status) VALUES (?,?,?,?,?)";

        try(Connection connection = db.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1,guest_id);
            stmt.setInt(2, room_id);
            stmt.setDate(3, Date.valueOf(check_in));
            stmt.setDate(4,Date.valueOf(check_out));
            stmt.setString(5,status);
        }



    }

    @Override
    public Reservation findById(long id) {
        return null;
    }

    @Override
    public Llist<Reservation> findAll() {
        return null;
    }

    @Override
    public void cancel(long id) {

    }

    @Override
    public boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut) {
        return false;
    }
}
