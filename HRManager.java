import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HRManager extends User {

    public HRManager(String username, String password, String role) {
        super(username, password, role);
    }

    public void approveLeaveRequest(String username) throws SQLException {
        String query = "UPDATE leaverequests SET status = 'approved' WHERE username = ? AND status = 'pending'";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);

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

    public List<LeaveRequest> getAllLeaveRequests() throws SQLException {
        List<LeaveRequest> leaveRequests = new ArrayList<>();

        String query = "SELECT * FROM leaverequests";

        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("username");
                String startDate = rs.getString("startdate");
                String endDate = rs.getString("enddate");
                String status = rs.getString("status");

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

    public Payroll getPayrollByUsername(String username) throws SQLException {
        Payroll payroll = null;
        String query = "SELECT * FROM payrolls WHERE username = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double salary = rs.getDouble("salary");
                    double bonus = rs.getDouble("bonus");
                    double deductions = rs.getDouble("deductions");
                    double totalPay = rs.getDouble("totalPay");

                    payroll = new Payroll(username, salary, bonus, deductions);
                } else {
                    System.out.println("No payroll found for username: " + username);
                }
            }

        } catch (SQLException e) {
            throw e;
        }

        return payroll;
    }
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        Connection con = DBUtils.establishConnection();

        String query = "SELECT username, hashedpass, role, position, salary, project, vacationdays FROM users WHERE role != 'hrmanager'";
        PreparedStatement stmt = con.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("hashedpass");
            String role = rs.getString("role");
            String position = rs.getString("position");
            double salary = rs.getDouble("salary");
            String project = rs.getString("project");
            int vacationDays = rs.getInt("vacationdays");

            Employee employee = new Employee(username, password, role, position, salary, project, vacationDays);
            employees.add(employee);
        }



        return employees;
    }


    public void updatePayroll(Payroll payroll) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM payrolls WHERE username = ?";
        String updateQuery = "UPDATE payrolls SET salary = ?, bonus = ?, deductions = ?, totalPay = ? WHERE username = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {

            checkStmt.setString(1, payroll.getUsername());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("No payroll record found for username: " + payroll.getUsername());
            }

            try (PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, payroll.getSalary());
                updateStmt.setDouble(2, payroll.getBonus());
                updateStmt.setDouble(3, payroll.getDeductions());
                updateStmt.setDouble(4, payroll.getTotalPay());
                updateStmt.setString(5, payroll.getUsername());

                updateStmt.executeUpdate();
                System.out.println("Payroll record updated for " + payroll.getUsername());
            }
        }
    }


    public void assignProject(String username, String projectName) throws SQLException {
        String query = "UPDATE users SET project = ? WHERE username = ?";

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, projectName);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project '" + projectName + "' assigned to user: " + username);
            } else {
                throw new SQLException("No matching user found with username: " + username);
            }
        }
    }

}


