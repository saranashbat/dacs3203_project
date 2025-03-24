import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ViewPayrolls {
    private HRManager hrManager;
    private Stage stage;

    public ViewPayrolls(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("Payroll Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        TextField bonusField = new TextField();
        bonusField.setPromptText("Bonus");

        TextField deductionsField = new TextField();
        deductionsField.setPromptText("Deductions");

        usernameField.setMaxWidth(200);
        salaryField.setMaxWidth(200);
        bonusField.setMaxWidth(200);
        deductionsField.setMaxWidth(200);

        Label totalPayLabel = new Label("Total Pay: ");

        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                double totalPay = payroll.getTotalPay();

                totalPayLabel.setText("Total Pay: " + totalPay);
            } catch (NumberFormatException ex) {
                totalPayLabel.setText("Invalid input. Please enter valid numbers.");
            }
        });

        Button addPayrollButton = new Button("Add Payroll");
        addPayrollButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                hrManager.addPayroll(payroll);
                totalPayLabel.setText("Payroll added successfully.");

            } catch (NumberFormatException | SQLException ex) {
                totalPayLabel.setText("Invalid input. Please enter valid numbers.");
            }
        });

        VBox requestContainer = new VBox(10);

        Button viewPayrollsButton = new Button("View All Payrolls");
        viewPayrollsButton.setOnAction(e -> {
            requestContainer.getChildren().clear();
            try {
                for (Payroll payroll : hrManager.getAllPayrolls()) {
                    Label payrollLabel = new Label(
                            "Username: " + payroll.getUsername() +
                                    "\nSalary: " + payroll.getSalary() +
                                    "\nBonus: " + payroll.getBonus() +
                                    "\nDeductions: " + payroll.getDeductions() +
                                    "\nTotal Pay: " + payroll.getTotalPay()
                    );
                    payrollLabel.setWrapText(true);
                    payrollLabel.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: lightgray;");
                    requestContainer.getChildren().add(payrollLabel);
                }
            } catch (SQLException ex) {
                Label errorLabel = new Label("Error fetching payrolls: " + ex.getMessage());
                requestContainer.getChildren().add(errorLabel);
            }
        });
        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, usernameField, salaryField, bonusField, deductionsField,
                calculateButton, addPayrollButton, viewPayrollsButton, totalPayLabel, requestContainer,backButton);

        Scene scene = new Scene(layout, 400, 600);
        stage.setTitle("Payroll Management");
        stage.setScene(scene);
        stage.show();
    }
}
