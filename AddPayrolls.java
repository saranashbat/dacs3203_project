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

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("Payroll Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

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

        VBox calculateSection = new VBox(10);
        calculateSection.setAlignment(Pos.CENTER);

        Button calculateButton = new Button("Calculate");
        calculateButton.setMinWidth(125);

        styleButton(calculateButton);

        Label totalPayLabel = new Label("Total Pay: ");
        totalPayLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        totalPayLabel.setTextFill(Color.DARKSLATEGRAY);

        calculateSection.setStyle("-fx-border-color: #d6e0f0; -fx-border-width: 1px; -fx-background-color: white; -fx-padding: 15; -fx-border-radius: 10;");

        calculateSection.getChildren().addAll(calculateButton, totalPayLabel);

        calculateButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                if (salary < 0 || bonus < 0 || deductions < 0) {
                    showAlert("Error", "Values cannot be negative.");
                    return;
                }

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                double totalPay = payroll.getTotalPay();

                totalPayLabel.setText("Total Pay: " + totalPay);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid input. Please enter valid numbers.");
            }
        });


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addPayrollButton = new Button("Add Payroll");
        styleButton(addPayrollButton);
        addPayrollButton.setMinWidth(125);

        addPayrollButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();

                if (username.isEmpty()) {
                    showAlert("Error", "Username cannot be empty.");
                    return;
                }

                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                if (salary < 0 || bonus < 0 || deductions < 0) {
                    showAlert("Error", "Values cannot be negative.");
                    return;
                }

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                hrManager.addPayroll(payroll);

                showAlert("Success", "Payroll added successfully.");
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid input. Please enter valid numbers.");
            } catch (SQLException ex) {
                showAlert("Error", ex.getMessage());
            }
        });



        Button updatePayrollButton = new Button("Update Payroll");
        styleButton(updatePayrollButton);
        updatePayrollButton.setMinWidth(125);

        updatePayrollButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();

                if (username.isEmpty()) {
                    showAlert("Error", "Username cannot be empty.");
                    return;
                }

                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deductions = Double.parseDouble(deductionsField.getText());

                if (salary < 0 || bonus < 0 || deductions < 0) {
                    showAlert("Error", "Values cannot be negative.");
                    return;
                }

                Payroll payroll = new Payroll(username, salary, bonus, deductions);
                hrManager.updatePayroll(payroll);

                showAlert("Success", "Payroll updated successfully.");

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid input. Please enter valid numbers.");
            } catch (SQLException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        buttonBox.getChildren().addAll(addPayrollButton, updatePayrollButton);

        Button viewPayrollsButton = new Button("View All Payrolls");
        styleButton(viewPayrollsButton);
        viewPayrollsButton.setMinWidth(125);

        viewPayrollsButton.setOnAction(e -> {
            ViewPayrolls viewPayrolls = new ViewPayrolls(hrManager);
            viewPayrolls.initializeComponents();
        });

        Button backButton = new Button("Back");
        styleBackButton(backButton);
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(
                titleLabel, inputFields, calculateSection, buttonBox, viewPayrollsButton, backButton
        );

        Scene scene = new Scene(layout, 400, 600);
        stage.setTitle("Payroll Management");
        stage.setScene(scene);
        stage.show();
    }


    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #90c6e6; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-pref-height: 40px; -fx-background-radius: 20px;"));
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c0c0c0; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
    }

    private void styleTextField(TextField textField) {
        textField.setMaxWidth(200);
        textField.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10px; -fx-border-color: #c4d1e0;");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.equals("Error")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
