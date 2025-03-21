import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LeaveRequest {
    private String username;
    private String startDate;  // Changed to String
    private String endDate;    // Changed to String
    private String status;

    public LeaveRequest(String username, String startDate, String endDate, String status) {
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to calculate duration in days
    public int duration() throws ParseException {
        // SimpleDateFormat to parse the date from String to Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        long differenceInMillis = end.getTime() - start.getTime();
        return (int) TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);
    }
}
