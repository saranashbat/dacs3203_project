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

public class AddPayrolls {
    private HRManager hrManager;
    private Stage stage;

    public AddPayrolls(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;"); // Light background

        // Title
        Label titleLabel = new Label("Payroll Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        // User Input Fields
        VBox inputFields = new VBox(10);
        inputFields.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleTextField(usernameField);

        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        styleTextField(salaryField);

        TextField bonusField = new TextField();
        bonusField.setPromptText("Bonus");
        styleTextField(bonusField);

        TextField deductionsField = new TextField();
        deductionsField.setPromptText("Deductions");
        styleTextField(deductionsField);

        inputFields.getChildren().addAll(usernameField, salaryField, bonusField, deductionsField);

        // Calculate and Total Pay (grouped in a box)
        VBox calculateSection = new VBox(10);
        calculateSection.setAlignment(Pos.CENTER);

        Button calculateButton = new Button("Calculate");
        calculateButton.setMinWidth(125);  // Set minimum width smaller

        styleButton(calculateButton);

        Label totalPayLabel = new Label("Total Pay: ");
        totalPayLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        totalPayLabel.setTextFill(Color.DARKSLATEGRAY);

        // Creating a bordered section for Total Pay and Calculate button
        calculateSection.setStyle("-fx-border-color: #d6e0f0; -fx-border-width: 1px; -fx-background-color: white; -fx-padding: 15; -fx-border-radius: 10;");

        calculateSection.getChildren().addAll(calculateButton, totalPayLabel);

        // Calculate Button action
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

        // Add Payroll and Update Payroll Buttons in an HBox (Horizontal Layout)
        HBox buttonBox = new HBox(10); // Set a spacing of 10 between buttons
        buttonBox.setAlignment(Pos.CENTER);

        Button addPayrollButton = new Button("Add Payroll");
        styleButton(addPayrollButton);
        addPayrollButton.setMinWidth(125);  // Set minimum width smaller

        addPayrollButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();

                // Check if the username field is empty
                if (username.isEmpty()) {
                    showAlert("Error", "Username cannot be empty.");
                    return; // Prevent further action if username is empty
                }

                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                hrManager.addPayroll(payroll); // Assuming hrManager has an addPayroll method

                // Success alert
                showAlert("Success", "Payroll added successfully.");
                totalPayLabel.setText("Payroll added successfully.");

            } catch (NumberFormatException ex) {
                // Show error if there's a number format exception
                showAlert("Error", "Invalid input. Please enter valid numbers.");
                totalPayLabel.setText("Invalid input. Please enter valid numbers.");
            } catch (SQLException ex) {
                // Show error if there's an SQL exception
                showAlert("Error", ex.getMessage());
                totalPayLabel.setText("Database error. Could not add payroll.");
            }
        });


        Button updatePayrollButton = new Button("Update Payroll");
        styleButton(updatePayrollButton);
        updatePayrollButton.setMinWidth(125);  // Set minimum width smaller

        updatePayrollButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();

                // Check if the username field is empty
                if (username.isEmpty()) {
                    showAlert("Error", "Username cannot be empty.");
                    return; // Prevent further action if username is empty
                }

                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                hrManager.updatePayroll(payroll); // Adding payroll to the HRManager

                // Success alert
                showAlert("Success", "Payroll updated successfully.");
                totalPayLabel.setText("Payroll updated successfully.");

            } catch (NumberFormatException ex) {
                // Show error if there's a number format exception
                showAlert("Error", "Invalid input. Please enter valid numbers.");
                totalPayLabel.setText("Invalid input. Please enter valid numbers.");
            } catch (SQLException ex) {
                // Show error if there's an SQL exception
                showAlert("Error", ex.getMessage());
                totalPayLabel.setText("Database error. Could not update payroll.");
            }
        });

        buttonBox.getChildren().addAll(addPayrollButton, updatePayrollButton); // Add both buttons to the HBox

        Button viewPayrollsButton = new Button("View All Payrolls");
        styleButton(viewPayrollsButton); // Using styleButton method for consistency
        viewPayrollsButton.setMinWidth(125);  // Set minimum width smaller

        viewPayrollsButton.setOnAction(e -> {
            ViewPayrolls viewPayrolls = new ViewPayrolls(hrManager);
            viewPayrolls.initializeComponents();
        });

        // Back Button (styled differently: smaller and different color)
        Button backButton = new Button("Back");
        styleBackButton(backButton); // Custom styling for the back button
        backButton.setOnAction(e -> stage.close());

        // Adding all elements to layout
        layout.getChildren().addAll(
                titleLabel, inputFields, calculateSection, buttonBox, viewPayrollsButton, backButton
        );

        // Scene setup
        Scene scene = new Scene(layout, 400, 600);
        stage.setTitle("Payroll Management");
        stage.setScene(scene);
        stage.show();
    }


    // Method to style buttons with consistent theme
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #90c6e6; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;"));
    }

    // Custom method to style the Back button differently (smaller and different color)
    private void styleBackButton(Button button) {
        button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c0c0c0; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
    }

    // Method to style text fields with consistent theme
    private void styleTextField(TextField textField) {
        textField.setMaxWidth(200);
        textField.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10px; -fx-border-color: #c4d1e0;");
    }

    // Show alert method
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION type for success
        if (title.equals("Error")) {
            alert.setAlertType(Alert.AlertType.ERROR); // Set to ERROR type for error messages
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
