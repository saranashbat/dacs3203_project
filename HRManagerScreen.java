import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HRManagerScreen {
    private HRManager hrManager;
    private Stage stage;

    public HRManagerScreen(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox hrManagerLayout = new VBox(10);
        hrManagerLayout.getChildren().addAll(
                new Label("Welcome, HR Manager " + hrManager.getUsername()),
                new Label("Role: " + hrManager.getRole()),
                new Label("Manage Employee Data:"),
                createManageEmployeeButton()
        );

        Scene hrManagerScene = new Scene(hrManagerLayout, 400, 300);

        stage.setFullScreen(true);
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
