import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;

public class TrackWorkHours {
    private Employee employee;
    private Stage stage;

    public TrackWorkHours(Employee employee) {
        this.employee = employee;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Track Work Hours");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);
        titleLabel.setAlignment(Pos.CENTER);

        String assignedProject = null;
        String currentUser = employee.getUsername();

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT project FROM users WHERE username = ?")) {
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                assignedProject = rs.getString("project");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (assignedProject == null) {
            new Alert(Alert.AlertType.ERROR, "No project assigned to this user.").showAndWait();
            return;
        }
        final String finalProject = assignedProject;

        HBox projectRow = new HBox(10);
        projectRow.setAlignment(Pos.CENTER);

        Label projectLabel = new Label("Assigned Project:");
        projectLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        projectLabel.setTextFill(Color.DARKSLATEGRAY);

        Label projectNameLabel = new Label(assignedProject);
        projectNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        projectNameLabel.setTextFill(Color.DARKSLATEGRAY);

        projectRow.getChildren().addAll(projectLabel, projectNameLabel);

        HBox dateRow = new HBox(10);
        dateRow.setAlignment(Pos.CENTER);

        Label dateLabel = new Label("Select Date:");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        dateLabel.setTextFill(Color.DARKSLATEGRAY);

        DatePicker workDatePicker = new DatePicker();

        dateRow.getChildren().addAll(dateLabel, workDatePicker);

        HBox hoursRow = new HBox(10);
        hoursRow.setAlignment(Pos.CENTER);

        Label hoursWorkedLabel = new Label("Hours Worked:");
        hoursWorkedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        hoursWorkedLabel.setTextFill(Color.DARKSLATEGRAY);

        TextField hoursWorkedField = new TextField();
        hoursWorkedField.setPromptText("Enter hours worked");

        hoursRow.getChildren().addAll(hoursWorkedLabel, hoursWorkedField);

        HBox descriptionRow = new HBox(10);
        descriptionRow.setAlignment(Pos.CENTER);

        Label descriptionLabel = new Label("Work Description:");
        descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        descriptionLabel.setTextFill(Color.DARKSLATEGRAY);

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Describe the work done");

        descriptionRow.getChildren().addAll(descriptionLabel, descriptionField);

        Button submitButton = new Button("Submit Work Hours");
        submitButton.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        submitButton.setMaxWidth(175);
        submitButton.setOnAction(e -> {
            String workDate = workDatePicker.getValue().toString();
            double hoursWorked;
            try {
                hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                if (hoursWorked < 0) {
                    throw new NumberFormatException("Hours must be positive.");
                }
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid hours. Please enter a valid number.").showAndWait();
                return;
            }

            String description = descriptionField.getText();

            try (Connection con = DBUtils.establishConnection()) {
                String query = "INSERT INTO workhours (username, name, date, hours, description) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, currentUser);
                stmt.setString(2, finalProject);
                stmt.setString(3, workDate);
                stmt.setDouble(4, hoursWorked);
                stmt.setString(5, description);
                stmt.executeUpdate();
                new Alert(Alert.AlertType.INFORMATION, "Work hours successfully recorded.").showAndWait();
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Error saving work hours: " + ex.getMessage()).showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #90c6e6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        backButton.setMaxWidth(175);
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(
                titleLabel,
                projectRow,
                dateRow,
                hoursRow,
                descriptionRow,
                submitButton,
                backButton
        );

        Scene scene = new Scene(layout, 400, 500);
        stage.setTitle("Track Work Hours");
        stage.setScene(scene);
        stage.show();
    }
}
