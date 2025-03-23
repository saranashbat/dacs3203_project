import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SalaryCalculator class for calculating employee salaries and bonuses
 * This class handles salary calculations, bonuses, and salary adjustments
 */
public class SalaryCalculator {
    
    // Constants for bonus calculations
    private static final double PERFORMANCE_BONUS_RATE = 0.05; // 5% bonus for high performance
    private static final double OVERTIME_BONUS_RATE = 0.25; // 25% extra for overtime hours
    private static final double STANDARD_WORK_HOURS = 40.0; // Standard weekly work hours
    
    /**
     * Creates the salary_adjustments table if it doesn't exist
     */
    public static void createSalaryAdjustmentsTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS salary_adjustments (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(200) NOT NULL, " +
                "adjustment_date DATE NOT NULL, " +
                "previous_salary DOUBLE NOT NULL, " +
                "new_salary DOUBLE NOT NULL, " +
                "reason VARCHAR(500) NOT NULL, " +
                "FOREIGN KEY (username) REFERENCES users(username)" +
                ")";
        
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("Salary adjustments table created or already exists");
            
        } catch (SQLException e) {
            System.out.println("Error creating salary adjustments table: " + e.getMessage());
        }
    }
    
    /**
     * Calculates an employee's salary with bonuses and deductions
     * 
     * @param username The employee's username
     * @return Map containing salary details (baseSalary, performanceBonus, overtimeBonus, deductions, totalSalary)
     */
    public static Map<String, Double> calculateSalary(String username) {
        Map<String, Double> salaryDetails = new HashMap<>();
        
        try (Connection con = DBUtils.establishConnection()) {
            // Get base salary from users table
            double baseSalary = getBaseSalary(con, username);
            salaryDetails.put("baseSalary", baseSalary);
            
            // Calculate performance bonus based on work hours
            double performanceBonus = calculatePerformanceBonus(con, username, baseSalary);
            salaryDetails.put("performanceBonus", performanceBonus);
            
            // Calculate overtime bonus
            double overtimeBonus = calculateOvertimeBonus(con, username, baseSalary);
            salaryDetails.put("overtimeBonus", overtimeBonus);
            
            // Calculate deductions (e.g., for unpaid leaves)
            double deductions = calculateDeductions(con, username, baseSalary);
            salaryDetails.put("deductions", deductions);
            
            // Calculate total salary
            double totalSalary = baseSalary + performanceBonus + overtimeBonus - deductions;
            salaryDetails.put("totalSalary", totalSalary);
            
        } catch (SQLException e) {
            System.out.println("Error calculating salary: " + e.getMessage());
        }
        
        return salaryDetails;
    }
    
    /**
     * Gets the base salary for an employee
     * 
     * @param con Database connection
     * @param username The employee's username
     * @return The base salary
     * @throws SQLException If a database error occurs
     */
    private static double getBaseSalary(Connection con, String username) throws SQLException {
        String query = "SELECT salary FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("salary");
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Calculates performance bonus based on work hours
     * 
     * @param con Database connection
     * @param username The employee's username
     * @param baseSalary The employee's base salary
     * @return The performance bonus amount
     * @throws SQLException If a database error occurs
     */
    private static double calculatePerformanceBonus(Connection con, String username, double baseSalary) throws SQLException {
        // Get total work hours for the current month
        String query = "SELECT SUM(hours) as total_hours FROM workhours " +
                       "WHERE username = ? AND MONTH(work_date) = MONTH(CURRENT_DATE()) " +
                       "AND YEAR(work_date) = YEAR(CURRENT_DATE())";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double totalHours = rs.getDouble("total_hours");
                    
                    // If employee worked more than expected hours, give performance bonus
                    if (totalHours > STANDARD_WORK_HOURS * 4) { // Assuming 4 weeks in a month
                        return baseSalary * PERFORMANCE_BONUS_RATE;
                    }
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Calculates overtime bonus
     * 
     * @param con Database connection
     * @param username The employee's username
     * @param baseSalary The employee's base salary
     * @return The overtime bonus amount
     * @throws SQLException If a database error occurs
     */
    private static double calculateOvertimeBonus(Connection con, String username, double baseSalary) throws SQLException {
        // Calculate hourly rate based on monthly salary (assuming 160 work hours per month)
        double hourlyRate = baseSalary / 160.0;
        
        // Get overtime hours (hours exceeding standard work hours per week)
        String query = "SELECT work_date, hours FROM workhours " +
                       "WHERE username = ? AND MONTH(work_date) = MONTH(CURRENT_DATE()) " +
                       "AND YEAR(work_date) = YEAR(CURRENT_DATE()) " +
                       "ORDER BY work_date";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                // Group hours by week and calculate overtime
                Map<Integer, Double> weeklyHours = new HashMap<>();
                
                while (rs.next()) {
                    Date workDate = rs.getDate("work_date");
                    double hours = rs.getDouble("hours");
                    
                    // Calculate week number within the month
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(workDate);
                    int weekOfMonth = cal.get(java.util.Calendar.WEEK_OF_MONTH);
                    
                    // Add hours to the corresponding week
                    weeklyHours.put(weekOfMonth, weeklyHours.getOrDefault(weekOfMonth, 0.0) + hours);
                }
                
                // Calculate overtime for each week
                double totalOvertimeHours = 0.0;
                for (double weekHours : weeklyHours.values()) {
                    if (weekHours > STANDARD_WORK_HOURS) {
                        totalOvertimeHours += (weekHours - STANDARD_WORK_HOURS);
                    }
                }
                
                // Calculate overtime bonus
                return totalOvertimeHours * hourlyRate * OVERTIME_BONUS_RATE;
            }
        }
    }
    
    /**
     * Calculates deductions for unpaid leaves
     * 
     * @param con Database connection
     * @param username The employee's username
     * @param baseSalary The employee's base salary
     * @return The deduction amount
     * @throws SQLException If a database error occurs
     */
    private static double calculateDeductions(Connection con, String username, double baseSalary) throws SQLException {
        // Calculate daily rate based on monthly salary (assuming 22 work days per month)
        double dailyRate = baseSalary / 22.0;
        
        // Get unpaid leave days
        String query = "SELECT COUNT(*) as unpaid_days FROM leaverequests " +
                       "WHERE username = ? AND status = 'approved' AND " +
                       "MONTH(startdate) = MONTH(CURRENT_DATE()) AND " +
                       "YEAR(startdate) = YEAR(CURRENT_DATE()) AND " +
                       "startdate <= enddate";
        
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int unpaidDays = rs.getInt("unpaid_days");
                    return unpaidDays * dailyRate;
                }
            }
        }
        
        return 0.0;
    }
    
    /**
     * Updates an employee's salary
     * 
     * @param username The employee's username
     * @param newSalary The new salary amount
     * @return true if successful, false otherwise
     */
    public static boolean updateSalary(String username, double newSalary) {
        String query = "UPDATE users SET salary = ? WHERE username = ?";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setDouble(1, newSalary);
            stmt.setString(2, username);
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating salary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Records a salary adjustment
     * 
     * @param username The employee's username
     * @param previousSalary The previous salary amount
     * @param newSalary The new salary amount
     * @param reason The reason for the adjustment
     * @return true if successful, false otherwise
     */
    public static boolean recordSalaryAdjustment(String username, double previousSalary, double newSalary, String reason) {
        String query = "INSERT INTO salary_adjustments (username, adjustment_date, previous_salary, new_salary, reason) " +
                       "VALUES (?, CURRENT_DATE(), ?, ?, ?)";
        
        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setDouble(2, previousSalary);
            stmt.setDouble(3, newSalary);
            stmt.setString(4, reason);
            
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error recording salary adjustment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets salary information for all employees (for HR Manager)
     * 
     * @return List of all employees' salary details as maps
     */
    public static List<Map<String, Object>> getAllEmployeesSalaries() {
        List<Map<String, Object>> salaryList = new ArrayList<>();
        String query = "SELECT username, position, salary FROM users WHERE role = 'employee'";
        
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String username = rs.getString("username");
                
                Map<String, Object> employeeData = new HashMap<>();
                employeeData.put("username", username);
                employeeData.put("position", rs.getString("position"));
                employeeData.put("baseSalary", rs.getDouble("salary"));
                
                // Calculate other salary components
                Map<String, Double> salaryDetails = calculateSalary(username);
                employeeData.putAll(salaryDetails);
                
                salaryList.add(employeeData);
            }
            
            return salaryList;
        } catch (SQLException e) {
            System.out.println("Error retrieving employee salaries: " + e.getMessage());
            return salaryList;
        }
    }
}
