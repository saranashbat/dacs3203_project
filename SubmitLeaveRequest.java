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

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("Submit Leave Request");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label startDateLabel = new Label("Start Date:");
        startDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        startDateLabel.setTextFill(Color.DARKSLATEGRAY);
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date:");
        endDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        endDateLabel.setTextFill(Color.DARKSLATEGRAY);
        DatePicker endDatePicker = new DatePicker();

        Button submitButton = new Button("Submit Leave Request");
        submitButton.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        submitButton.setMaxWidth(175);
        submitButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalDate today = LocalDate.now();

            if (startDate != null && endDate != null) {
                if (startDate.isBefore(today) || endDate.isBefore(today)) {
                    showAlert("Error", "Dates cannot be in the past.");
                } else if (!startDate.isAfter(endDate)) {
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
                    showAlert("Error", "Start date cannot be after end date.");
                }
            } else {
                showAlert("Error", "Please select valid start and end dates.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #90c6e6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        backButton.setMaxWidth(175);
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, startDateLabel, startDatePicker, endDateLabel, endDatePicker, submitButton, backButton);

        Scene scene = new Scene(layout, 400, 500);
        stage.setTitle("Submit Leave Request");
        stage.setScene(scene);
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
