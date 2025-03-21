import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee extends User {
    private String position;
    private double salary;
    private int vacationDays;
    private String project;

    public Employee(String username, String password, String role, String position, double salary, String project, int vacationDays) {
        super(username, password, role);
        this.position = position;
        this.salary = salary;
        this.vacationDays = vacationDays;
        this.project = project;
    }

    // Getters
    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    public int getVacationDays() {
        return vacationDays;
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

            String query = "INSERT INTO leaverequests (username, startdate, enddate, status) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement stmt = con.prepareStatement(query);

                stmt.setString(1, this.getUsername());
                stmt.setDate(2, new java.sql.Date(startDate.getTime()));
                stmt.setDate(3, new java.sql.Date(endDate.getTime()));
                stmt.setString(4, "Pending");

                int rs = stmt.executeUpdate();
                if (rs > 0) {
                    System.out.println("Leave Request Submitted!");
                }
                DBUtils.closeConnection(con, stmt);

            } catch (SQLException e) {
                throw e;
            }
        } catch (ParseException e) {
            throw e;
        }
    }
}
