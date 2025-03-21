import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeaveManager {

    private static final String REQUEST_LEAVE_QUERY =
            "INSERT INTO leave_requests (username, start_date, end_date, status) VALUES (?, ?, ?, 'pending')";
    private static final String APPROVE_LEAVE_QUERY =
            "UPDATE leave_requests SET status = 'approved' WHERE username = ? AND status = 'pending'";

    // Method to request leave
    public static void requestLeave(String username, String startDate, String endDate) {
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(REQUEST_LEAVE_QUERY)) {

            stmt.setString(1, username);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Leave Request Submitted Successfully!");
            } else {
                System.out.println("Failed to Submit Leave Request.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to approve leave
    public static void approveLeave(String username) {
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(APPROVE_LEAVE_QUERY)) {

            stmt.setString(1, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Leave Approved Successfully!");
            } else {
                System.out.println("No Pending Leave Requests Found for " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
