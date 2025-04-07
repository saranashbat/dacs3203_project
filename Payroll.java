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
        this.totalPay = calculateTotalPay(salary, bonus, deductions);
    }

    public static double safeAdd(double left, double right) throws ArithmeticException {
        if (right > 0 && left > Double.MAX_VALUE - right) {
            throw new ArithmeticException("Double Overflow");
        } else if (right < 0 && left < Double.MIN_VALUE - right) {
            throw new ArithmeticException("Double Overflow");
        }
        return left + right;
    }

    public static double safeSubtract(double left, double right) throws ArithmeticException {
        if (right > 0 && left < Double.MIN_VALUE + right) {
            throw new ArithmeticException("Double Overflow");
        } else if (right < 0 && left > Double.MAX_VALUE + right) {
            throw new ArithmeticException("Double Overflow");
        }
        return left - right;
    }

    private double calculateTotalPay(double salary, double bonus, double deductions) {
        try {
            double total = safeAdd(salary, bonus);
            total = safeSubtract(total, deductions);
            return Math.round(total * 100.0) / 100.0;

        } catch (ArithmeticException ex) {
            System.err.println("Error in calculation: " + ex.getMessage());
            return 0.0;
        }
    }

    public String getUsername() {
        return username;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public double getTotalPay() {
        return totalPay;
    }
}
