
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkHoursManager {
    private static final String CLOCK_IN_QUERY = "INSERT INTO work_hours (username, clock_in) VALUES (?, ?)";
    private static final String CLOCK_OUT_QUERY = "UPDATE work_hours SET clock_out = ?, total_hours = TIMESTAMPDIFF(HOUR, clock_in, ?) WHERE username = ? AND clock_out IS NULL";

    public static void clockIn(String username) {
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(CLOCK_IN_QUERY)) {
            stmt.setString(1, username);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            System.out.println("Clock In Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clockOut(String username) {
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(CLOCK_OUT_QUERY)) {
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(1, now);
            stmt.setTimestamp(2, now);
            stmt.setString(3, username);
            stmt.executeUpdate();
            System.out.println("Clock Out Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
