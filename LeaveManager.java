public class LeaveManager {
    private static final String REQUEST_LEAVE_QUERY = "INSERT INTO leave_requests (username, start_date, end_date) VALUES (?, ?, ?)";

    public static void requestLeave(String username, String startDate, String endDate) {
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(REQUEST_LEAVE_QUERY)) {
            stmt.setString(1, username);
            stmt.setString(2, startDate);
            stmt.setString(3, endDate);
            stmt.executeUpdate();
            System.out.println("Leave Request Submitted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
public static void approveLeave(String username) {
    String query = "UPDATE leave_requests SET status='approved' WHERE username=?";
    try (Connection con = DBUtils.establishConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.executeUpdate();
        System.out.println("Leave Approved!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
