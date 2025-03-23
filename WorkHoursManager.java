import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WorkHoursManager class for tracking employee work hours
 * This class handles logging, retrieving, updating, and deleting work hours entries
 */
public class WorkHoursManager {
    
    /**
     * Creates the workhours table if it doesn't exist
     */
    public static void createWorkHoursTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS workhours (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(200) NOT NULL, " +
                "work_date DATE NOT NULL, " +
                "hours DOUBLE NOT NULL, " +
                "project VARCHAR(200) NOT NULL, " +
                "FOREIGN KEY (username) REFERENCES users(username)" +
                ")";
        
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Work hours table created or already exists");
            
        } catch (SQLException e) {
            System.out.println("Error creating work hours table: " + e.getMessage());
        }
    }
    
    /**
     * Logs work hours for an employee
     * 
     * @param username The employee's username
     * @param date The date of work in format yyyy-MM-dd
     * @param hours Number of hours worked
     * @param projectName The project name
     * @return true if successful, false otherwise
     */
    public static boolean logWorkHours(String username, String date, double hours, String projectName) {
        String query = "INSERT INTO workhours (username, work_date, hours, project) VALUES (?, ?, ?, ?)";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            // Convert string date to SQL date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(date);
            
            // Set parameters
            stmt.setString(1, username);
            stmt.setDate(2, new java.sql.Date(parsedDate.getTime()));
            stmt.setDouble(3, hours);
            stmt.setString(4, projectName);
            
            // Execute the query
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Error logging work hours: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Updates existing work hours entry
     * 
     * @param entryId The ID of the entry to update
     * @param hours The new hours value
     * @return true if successful, false otherwise
     */
    public static boolean updateWorkHours(int entryId, double hours) {
        String query = "UPDATE workhours SET hours = ? WHERE id = ?";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setDouble(1, hours);
            stmt.setInt(2, entryId);
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating work hours: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a work hours entry
     * 
     * @param entryId The ID of the entry to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteWorkHoursEntry(int entryId) {
        String query = "DELETE FROM workhours WHERE id = ?";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setInt(1, entryId);
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting work hours entry: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all work hours entries for a specific employee
     * 
     * @param username The employee's username
     * @return List of work hours entries as maps
     */
    public static List<Map<String, Object>> getWorkHoursByEmployee(String username) {
        List<Map<String, Object>> workHoursList = new ArrayList<>();
        String query = "SELECT * FROM workhours WHERE username = ? ORDER BY work_date DESC";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("id", rs.getInt("id"));
                    entry.put("username", rs.getString("username"));
                    entry.put("date", rs.getDate("work_date"));
                    entry.put("hours", rs.getDouble("hours"));
                    entry.put("project", rs.getString("project"));
                    
                    workHoursList.add(entry);
                }
            }
            
            return workHoursList;
        } catch (SQLException e) {
            System.out.println("Error retrieving work hours: " + e.getMessage());
            return workHoursList;
        }
    }
    
    /**
     * Gets a summary of work hours by project for a specific employee
     * 
     * @param username The employee's username
     * @return Map with project names as keys and total hours as values
     */
    public static Map<String, Double> getWorkHoursSummary(String username) {
        Map<String, Double> summary = new HashMap<>();
        String query = "SELECT project, SUM(hours) as total_hours FROM workhours WHERE username = ? GROUP BY project";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String project = rs.getString("project");
                    double totalHours = rs.getDouble("total_hours");
                    summary.put(project, totalHours);
                }
            }
            
            // Get total hours across all projects
            String totalQuery = "SELECT SUM(hours) as grand_total FROM workhours WHERE username = ?";
            try (PreparedStatement totalStmt = con.prepareStatement(totalQuery)) {
                totalStmt.setString(1, username);
                
                try (ResultSet rs = totalStmt.executeQuery()) {
                    if (rs.next()) {
                        summary.put("TOTAL", rs.getDouble("grand_total"));
                    }
                }
            }
            
            return summary;
        } catch (SQLException e) {
            System.out.println("Error retrieving work hours summary: " + e.getMessage());
            return summary;
        }
    }
    
    /**
     * Gets work hours for all employees (for HR Manager)
     * 
     * @return List of all employees' work hours entries as maps
     */
    public static List<Map<String, Object>> getAllEmployeesWorkHours() {
        List<Map<String, Object>> allWorkHours = new ArrayList<>();
        String query = "SELECT w.*, u.position FROM workhours w JOIN users u ON w.username = u.username ORDER BY w.username, w.work_date DESC";
        
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("id", rs.getInt("id"));
                entry.put("username", rs.getString("username"));
                entry.put("date", rs.getDate("work_date"));
                entry.put("hours", rs.getDouble("hours"));
                entry.put("project", rs.getString("project"));
                entry.put("position", rs.getString("position"));
                
                allWorkHours.add(entry);
            }
            
            return allWorkHours;
        } catch (SQLException e) {
            System.out.println("Error retrieving all employees work hours: " + e.getMessage());
            return allWorkHours;
        }
    }
}
