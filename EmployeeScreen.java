import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
        employeeLayout.setPadding(new Insets(30));
        employeeLayout.setAlignment(Pos.CENTER);
        employeeLayout.setStyle("-fx-background-color: #f9f9f9;");

        VBox infoSection = new VBox(10);
        infoSection.setPadding(new Insets(20));
        infoSection.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-border-color: #d1d1d1; -fx-border-width: 1;");
        infoSection.setMaxWidth(300);

        Label titleLabel = new Label("Employee Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label welcomeLabel = new Label("Welcome, " + employee.getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.DARKGRAY);
        welcomeLabel.setAlignment(Pos.CENTER);

        HBox positionBox = new HBox(10);
        Label positionLabel = new Label("Position: ");
        positionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        positionLabel.setTextFill(Color.DARKSLATEGRAY);
        Label positionValueLabel = new Label(employee.getPosition());
        positionValueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        positionValueLabel.setTextFill(Color.DARKGRAY);
        positionBox.getChildren().addAll(positionLabel, positionValueLabel);

        HBox salaryBox = new HBox(10);
        Label salaryLabel = new Label("Salary: ");
        salaryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        salaryLabel.setTextFill(Color.DARKSLATEGRAY);
        Label salaryValueLabel = new Label("$" + employee.getSalary());
        salaryValueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        salaryValueLabel.setTextFill(Color.DARKGRAY);
        salaryBox.getChildren().addAll(salaryLabel, salaryValueLabel);

        HBox projectBox = new HBox(10);
        Label projectLabel = new Label("Project: ");
        projectLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        projectLabel.setTextFill(Color.DARKSLATEGRAY);
        Label projectValueLabel = new Label(employee.getProject());
        projectValueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        projectValueLabel.setTextFill(Color.DARKGRAY);
        projectBox.getChildren().addAll(projectLabel, projectValueLabel);

        infoSection.getChildren().addAll(welcomeLabel, positionBox, salaryBox, projectBox);

        Button leaveRequestButton = new Button("Submit Leave Request");
        styleButton(leaveRequestButton);
        leaveRequestButton.setOnAction(e -> {
            SubmitLeaveRequest submitLeaveRequest = new SubmitLeaveRequest(employee);
            submitLeaveRequest.initializeComponents();
        });

        Button workHoursButton = new Button("Add Work Hours");
        styleButton(workHoursButton);
        workHoursButton.setOnAction(e -> {
            TrackWorkHours trackWorkHours = new TrackWorkHours(employee);
            trackWorkHours.initializeComponents();
        });

        Button logoutButton = new Button("Logout");
        styleLogoutButton(logoutButton);

        logoutButton.setOnAction(event -> {
                    stage.close();

                    UserLogin loginScreen = new UserLogin(stage);
                    loginScreen.initializeComponents();
                    styleLogoutButton(logoutButton);
                });


        employeeLayout.getChildren().addAll(titleLabel, welcomeLabel, infoSection, leaveRequestButton, workHoursButton, logoutButton);

        Scene employeeScene = new Scene(employeeLayout, 600, 500);

        stage.setTitle("Employee Screen");
        stage.setScene(employeeScene);
        stage.show();
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 200px; -fx-pref-height: 40px; -fx-background-radius: 20px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #90c6e6; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 200px; -fx-pref-height: 40px; -fx-background-radius: 20px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 200px; -fx-pref-height: 40px; -fx-background-radius: 20px;"));
    }

    private void styleLogoutButton(Button button) {
        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 100px; -fx-pref-height: 40px; -fx-background-radius: 20px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 100px; -fx-pref-height: 40px; -fx-background-radius: 20px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 100px; -fx-pref-height: 40px; -fx-background-radius: 20px;"));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
