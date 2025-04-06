
public class Payroll {
    private String username;
    private double salary;
    private double bonus;
    private double deductions;
    private double totalPay;

    public Payroll(String username, double salary, double bonus, double deductions) {
        this.username = username;
        this.salary = salary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.totalPay = salary + bonus - deductions;
    }

    public String getUsername() { return username; }
    public double getSalary() { return salary; }
    public double getBonus() { return bonus; }
    public double getDeductions() { return deductions; }
    public double getTotalPay() { return totalPay; }
}