import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ViewPayrolls {
    private HRManager hrManager;
    private Stage stage;

    public ViewPayrolls(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("View Payrolls by Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        ComboBox<String> employeeComboBox = new ComboBox<>();
        employeeComboBox.setPromptText("Select Employee");
        styleComboBox(employeeComboBox);

        try {
            List<Employee> employees = hrManager.getAllEmployees();
            for (Employee employee : employees) {
                employeeComboBox.getItems().add(employee.getUsername());

            }
        } catch (SQLException ex) {
            showError("Error fetching employees: " + ex.getMessage());
        }

        Label payrollDetailsLabel = new Label("Select an employee to view payroll details.");
        payrollDetailsLabel.setWrapText(true);
        payrollDetailsLabel.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: lightgray;");

        employeeComboBox.setOnAction(e -> {
            String selectedEmployee = employeeComboBox.getValue();
            if (selectedEmployee != null) {
                try {
                    Payroll payroll = hrManager.getPayrollByUsername(selectedEmployee);
                    String payrollInfo = "Username: " + payroll.getUsername() +
                            "\nSalary: " + payroll.getSalary() +
                            "\nBonus: " + payroll.getBonus() +
                            "\nDeductions: " + payroll.getDeductions() +
                            "\nTotal Pay: " + payroll.getTotalPay();
                    payrollDetailsLabel.setText(payrollInfo);
                } catch (SQLException ex) {
                    showError("Error fetching payroll: " + ex.getMessage());
                }
            }
        });

        Button backButton = new Button("Back");
        styleBackButton(backButton);
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, employeeComboBox, payrollDetailsLabel, backButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("View Payrolls");
        stage.setScene(scene);
        stage.show();
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #c4d1e0;");
        comboBox.setMaxWidth(200);
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c0c0c0; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
