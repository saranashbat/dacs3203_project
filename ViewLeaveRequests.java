import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.util.List;

public class ViewLeaveRequests {

    private Stage stage;
    private HRManager hrManager;

    public ViewLeaveRequests(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("View All Leave Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Button backButton = new Button("Back");
        styleBackButton(backButton);
        backButton.setOnAction(e -> stage.close());

        VBox requestContainer = new VBox(10);
        requestContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(requestContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        try {
            List<LeaveRequest> leaveRequests = hrManager.getAllLeaveRequests();
            for (LeaveRequest leaveRequest : leaveRequests) {
                Label leaveRequestLabel = new Label(
                        "Username: " + leaveRequest.getUsername() +
                                "\nStart Date: " + leaveRequest.getStartDate() +
                                "\nEnd Date: " + leaveRequest.getEndDate() +
                                "\nStatus: " + leaveRequest.getStatus()
                );
                leaveRequestLabel.setWrapText(true);
                leaveRequestLabel.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: lightgray;");
                requestContainer.getChildren().add(leaveRequestLabel);
            }
        } catch (SQLException ex) {
            showAlert("Error", "An error occurred while getting all leave requests: " + ex.getMessage());
            ex.printStackTrace();
        }

        Label usernameLabel = new Label("Enter Username to Approve:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        usernameLabel.setTextFill(Color.DARKSLATEGRAY);

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(200);
        usernameField.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10px; -fx-border-color: #c4d1e0;");

        Button approveButton = new Button("Approve Leave Request");
        styleButton(approveButton);
        approveButton.setOnAction(e -> {
            String usernameToApprove = usernameField.getText().trim();

            if (!usernameToApprove.isEmpty()) {
                try {
                    hrManager.approveLeaveRequest(usernameToApprove);
                    showAlert("Success", "Leave request approved.");
                } catch (SQLException ex) {
                    showAlert("Error", "An error occurred while approving the leave request: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                showAlert("Error", "Please enter a username.");
            }
        });

        layout.getChildren().addAll(titleLabel, scrollPane, usernameLabel, usernameField, approveButton, backButton);

        Scene scene = new Scene(layout, 600, 500);
        stage.setTitle("View Leave Requests");
        stage.setScene(scene);
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

    private void styleBackButton(Button button) {
        button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c0c0c0; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;"));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
