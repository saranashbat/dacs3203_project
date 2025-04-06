import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HRManagerScreen {
    private HRManager hrManager;
    private Stage stage;

    public HRManagerScreen(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox hrManagerLayout = new VBox(20);
        hrManagerLayout.setPadding(new Insets(40));
        hrManagerLayout.setAlignment(Pos.CENTER);
        hrManagerLayout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("HR Manager Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label welcomeLabel = new Label("Welcome, " + hrManager.getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.DARKGRAY);

        Button viewLeaveRequestsButton = createViewLeaveRequestsButton();
        styleButton(viewLeaveRequestsButton);

        Button viewPayrollsButton = createViewPayrollsButton();
        styleButton(viewPayrollsButton);

        Button assignProjectButton = createAssignProjectButton();
        styleButton(assignProjectButton);

        Button viewHoursButton = createViewHoursButton();
        styleButton(viewHoursButton);

        Button logoutButton = createLogoutButton();
        styleLogoutButton(logoutButton);

        hrManagerLayout.getChildren().addAll(
                titleLabel, welcomeLabel, viewLeaveRequestsButton, viewPayrollsButton,
                assignProjectButton, viewHoursButton, logoutButton
        );

        Scene hrManagerScene = new Scene(hrManagerLayout, 600, 500);
        stage.setTitle("HR Manager Screen");
        stage.setScene(hrManagerScene);
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

    private Button createViewLeaveRequestsButton() {
        Button viewLeaveRequestsButton = new Button("View Leave Requests");
        viewLeaveRequestsButton.setOnAction(event -> {
            ViewLeaveRequests viewLeaveRequests = new ViewLeaveRequests(hrManager);
            viewLeaveRequests.initializeComponents();
        });
        return viewLeaveRequestsButton;
    }

    private Button createViewPayrollsButton() {
        Button addPayrollsButton = new Button("View Payrolls");
        addPayrollsButton.setOnAction(event -> {
            AddPayrolls addPayrolls = new AddPayrolls(hrManager);
            addPayrolls.initializeComponents();
        });
        return addPayrollsButton;
    }

    private Button createAssignProjectButton() {
        Button assignProjectButton = new Button("Assign Projects");
        assignProjectButton.setOnAction(event -> {
            AssignProject assignProject = new AssignProject(hrManager);
            assignProject.initializeComponents();
        });
        return assignProjectButton;
    }

    private Button createViewHoursButton() {
        Button viewHoursButton = new Button("View Work Hours");
        viewHoursButton.setOnAction(event -> {
            ViewWorkHours viewWorkHours = new ViewWorkHours(hrManager);
            viewWorkHours.initializeComponents();
        });
        return viewHoursButton;
    }

    private Button createLogoutButton() {
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            stage.close();

            UserLogin loginScreen = new UserLogin(stage);
            loginScreen.initializeComponents();
        });
        return logoutButton;
    }
}
