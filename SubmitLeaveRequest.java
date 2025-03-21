// Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SubmitLeaveRequest {
    private Employee employee;
    private Stage stage;

    public SubmitLeaveRequest(Employee employee) {
        this.employee = employee;
    }

    public void initializeComponents() {
        stage = new Stage();

        // Layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f4f4f4;");

        // Title
        Label titleLabel = new Label("Submit Leave Request");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Start Date Picker
        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();

        // End Date Picker
        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        // Submit Button
        Button submitButton = new Button("Submit Leave Request");
        submitButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate != null && endDate != null && !startDate.isAfter(endDate)) {
                String startDateStr = startDate.toString();
                String endDateStr = endDate.toString();

                try {
                    employee.requestLeave(startDateStr, endDateStr);
                    showAlert("Success", "Leave request submitted successfully!");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to submit leave request: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                showAlert("Error", "Please select valid start and end dates.");
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close());

        // Add components to layout
        layout.getChildren().addAll(titleLabel, startDateLabel, startDatePicker, endDateLabel, endDatePicker, submitButton, backButton);

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Submit Leave Request");
        stage.setScene(scene);
        stage.show();
    }

    // Show alert method
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
