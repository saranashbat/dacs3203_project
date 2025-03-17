import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeScreen {
    private Employee employee;
    private Stage stage;

    public EmployeeScreen(Employee employee) {
        this.employee = employee;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox employeeLayout = new VBox(10);
        employeeLayout.getChildren().addAll(
                new Label("Welcome " + employee.getUsername()),
                new Label("Position: " + employee.getPosition()),
                new Label("Salary: " + employee.getSalary())
        );

        Scene employeeScene = new Scene(employeeLayout, 400, 300);

        stage.setFullScreen(true);
        stage.setTitle("Employee Screen");
        stage.setScene(employeeScene);
        stage.show();
    }
}
