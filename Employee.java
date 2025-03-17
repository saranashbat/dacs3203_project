public class Employee extends User {
    private String position;
    private double salary;

    public Employee(String username, String password, String role, String position, double salary) {
        super(username, password, role);
        this.position = position;
        this.salary = salary;
    }



    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }
}
