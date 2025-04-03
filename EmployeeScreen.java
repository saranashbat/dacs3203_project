import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EmployeeScreen {
    private Employee employee;
    private Stage stage;

    public EmployeeScreen(Employee employee) {
        this.employee = employee;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox employeeLayout = new VBox(20);
        employeeLayout.setPadding(new Insets(20));
        employeeLayout.setAlignment(Pos.CENTER);
        employeeLayout.setStyle("-fx-background-color: #f4f4f4;"); // Light gray background

        Label titleLabel = new Label("Employee Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        Label welcomeLabel = new Label("Welcome, " + employee.getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.DARKGRAY);

        Label positionLabel = new Label("Position: " + employee.getPosition());
        positionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        positionLabel.setTextFill(Color.DARKGRAY);

        Label salaryLabel = new Label("Salary: $" + employee.getSalary());
        salaryLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        salaryLabel.setTextFill(Color.DARKGRAY);

        Button leaveRequestButton = new Button("Submit Leave Request");
        leaveRequestButton.setOnAction(e -> {
            SubmitLeaveRequest submitLeaveRequest = new SubmitLeaveRequest(employee);
            submitLeaveRequest.initializeComponents();
        });

        employeeLayout.getChildren().addAll(titleLabel, welcomeLabel, positionLabel, salaryLabel, leaveRequestButton);

        Scene employeeScene = new Scene(employeeLayout, 600, 500);

        stage.setTitle("Employee Screen");
        stage.setScene(employeeScene);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}