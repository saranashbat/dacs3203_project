import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HRManager extends User {

    public HRManager(String username, String password, String role) {
        super(username, password, role);
    }

    // Approve leave request method
    public void approveLeaveRequest(String username) throws SQLException {
        String query = "UPDATE leaverequests SET status = 'approved' WHERE username = ? AND status = 'pending'";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set the username parameter in the query
            stmt.setString(1, username);

            // Execute the query to update the status
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Leave Request for " + username + " has been approved.");
            } else {
                throw new SQLException("No pending leave request found for username: " + username);
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    // Method to get all leave requests from the database
    public List<LeaveRequest> getAllLeaveRequests() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();

        String query = "SELECT * FROM leaverequests";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate over the result set and create LeaveRequest objects
            while (rs.next()) {
                String username = rs.getString("username");
                String startDate = rs.getString("startdate");
                String endDate = rs.getString("enddate");
                String status = rs.getString("status");

                // Create LeaveRequest object and add it to the list
                LeaveRequest leaveRequest = new LeaveRequest(username, startDate, endDate, status);
                leaveRequests.add(leaveRequest);
            }

        } catch (SQLException e) {
            throw e;
        }

        return leaveRequests;
    }

    public void addPayroll(Payroll payroll) throws SQLException {
        String query = "INSERT INTO payrolls (username, salary, bonus, deductions, totalpay) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, payroll.getUsername());
            stmt.setDouble(2, payroll.getSalary());
            stmt.setDouble(3, payroll.getBonus());
            stmt.setDouble(4, payroll.getDeductions());
            stmt.setDouble(5, payroll.getTotalPay());

            stmt.executeUpdate();
            System.out.println("Payroll record added for " + payroll.getUsername());
        }
    }

    // Retrieve all payroll records
    public List<Payroll> getAllPayrolls() throws SQLException {
        List<Payroll> payrolls = new ArrayList<>();
        String query = "SELECT * FROM payrolls";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                double salary = rs.getDouble("salary");
                double bonus = rs.getDouble("bonus");
                double deductions = rs.getDouble("deductions");
                double totalPay = rs.getDouble("totalPay");

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                payrolls.add(payroll);
            }
        }
        return payrolls;
    }

    // Update payroll record
    public void updatePayroll(Payroll payroll) throws SQLException {
        String query = "UPDATE payrolls SET salary = ?, bonus = ?, deductions = ?, totalPay = ? WHERE username = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setDouble(1, payroll.getSalary());
            stmt.setDouble(2, payroll.getBonus());
            stmt.setDouble(3, payroll.getDeductions());
            stmt.setDouble(4, payroll.getTotalPay());
            stmt.setString(5, payroll.getUsername());

            stmt.executeUpdate();
            System.out.println("Payroll record updated for " + payroll.getUsername());
        }
    }
}
