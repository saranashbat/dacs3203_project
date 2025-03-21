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

        // Main layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f4f4f4;");

        // Title
        Label titleLabel = new Label("View All Leave Requests");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close());

        // Container for leave requests
        VBox requestContainer = new VBox(10);
        requestContainer.setPadding(new Insets(10));

        // ScrollPane for requests
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

        // Username field to approve
        Label usernameLabel = new Label("Enter Username to Approve:");
        TextField usernameField = new TextField();
        usernameField.setMaxWidth(200);

        Button approveButton = new Button("Approve Leave Request");
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

        // Layout arrangement
        layout.getChildren().addAll(titleLabel, scrollPane, usernameLabel, usernameField, approveButton, backButton);

        Scene scene = new Scene(layout, 600, 500);
        stage.setTitle("View Leave Requests");
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