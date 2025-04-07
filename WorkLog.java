import java.time.LocalDate;

public class WorkLog {
    private String username;
    private String projectName;
    private LocalDate workDate;
    private double hoursWorked;
    private String description;

    public WorkLog(String username, String projectName, LocalDate workDate, double hoursWorked, String description) {
        this.username = username;
        this.projectName = projectName;
        this.workDate = workDate;
        this.hoursWorked = hoursWorked;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getProjectName() {
        return projectName;
    }


    public LocalDate getWorkDate() {
        return workDate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }


    public String getDescription() {
        return description;
    }




}
