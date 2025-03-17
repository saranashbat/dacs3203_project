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
        hrManagerLayout.setPadding(new Insets(20));
        hrManagerLayout.setAlignment(Pos.CENTER);
        hrManagerLayout.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("HR Manager Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        Label welcomeLabel = new Label("Welcome, " + hrManager.getUsername());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.DARKGRAY);

        Label roleLabel = new Label("Role: " + hrManager.getRole());
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        roleLabel.setTextFill(Color.DARKGRAY);

        Button manageEmployeeButton = createManageEmployeeButton();
        manageEmployeeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 200px;");

        hrManagerLayout.getChildren().addAll(titleLabel, welcomeLabel, roleLabel, manageEmployeeButton);

        Scene hrManagerScene = new Scene(hrManagerLayout, 600, 400);

        stage.setTitle("HR Manager Screen");
        stage.setScene(hrManagerScene);
        stage.show();
    }

    private Button createManageEmployeeButton() {
        Button manageEmployeeButton = new Button("Manage Employees");
        manageEmployeeButton.setOnAction(event -> {
            System.out.println("Managing employees...");
        });
        return manageEmployeeButton;
    }
}