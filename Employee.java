import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.sql.*;


public class Employee extends User {
    private String position;
    private double salary;
    private String project;

    public Employee(String username, String password, String role, String position, double salary, String project) {
        super(username, password, role);
        this.position = position;
        this.salary = salary;
        this.project = project;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }



    public String getProject() {
        return project;
    }



    public void requestLeave(String startDateStr, String endDateStr) throws ParseException, SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            Connection con = DBUtils.establishConnection();
            String pendingQuery = "SELECT COUNT(*) FROM leaverequests WHERE username = ? AND status = 'pending'";
            try (PreparedStatement stmt = con.prepareStatement(pendingQuery)) {
                stmt.setString(1, this.getUsername());
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("You already have a pending leave request.");
                }
            }

            String overlapQuery = "SELECT COUNT(*) FROM leaverequests WHERE username = ? " +
                    "AND ((startdate <= ? AND enddate >= ?) OR (startdate <= ? AND enddate >= ?))";
            try (PreparedStatement stmt = con.prepareStatement(overlapQuery)) {
                stmt.setString(1, this.getUsername());
                stmt.setDate(2, new java.sql.Date(startDate.getTime()));
                stmt.setDate(3, new java.sql.Date(startDate.getTime()));
                stmt.setDate(4, new java.sql.Date(endDate.getTime()));
                stmt.setDate(5, new java.sql.Date(endDate.getTime()));
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("The leave request overlaps with an existing leave request.");
                }
            }

            String query = "INSERT INTO leaverequests (username, startdate, enddate, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, this.getUsername());
                stmt.setDate(2, new java.sql.Date(startDate.getTime()));
                stmt.setDate(3, new java.sql.Date(endDate.getTime()));
                stmt.setString(4, "pending");

                int rs = stmt.executeUpdate();
                if (rs > 0) {
                    System.out.println("Leave Request Submitted!");
                }
            } catch (SQLException e) {
                throw e;
            }
        } catch (ParseException e) {
            throw e;
        }
    }


    public void addWorkHours(String projectName, LocalDate workDate, double hoursWorked, String description) throws SQLException {
        try (Connection con = DBUtils.establishConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM workhours WHERE username = ? AND date = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, this.getUsername());
                checkStmt.setString(2, workDate.toString());

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Work hours for this date already exist.");
                }
            }

            String query = "INSERT INTO workhours (username, name, date, hours, description) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, this.getUsername());
                stmt.setString(2, projectName);
                stmt.setString(3, workDate.toString());
                stmt.setDouble(4, hoursWorked);
                stmt.setString(5, description);
                stmt.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage(), ex);
        }
    }

}
