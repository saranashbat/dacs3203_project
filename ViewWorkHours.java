import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ViewWorkHours {
    private HRManager hrManager;
    private Stage stage;

    public ViewWorkHours(HRManager hrManager) {
        this.hrManager = hrManager;
        this.stage = new Stage();
    }

    public void initializeComponents() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Text titleLabel = new Text("View Employee Work Hours");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setFill(Color.DARKSLATEGRAY);

        Text employeeLabel = new Text("Select Employee:");
        employeeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        employeeLabel.setFill(Color.DARKSLATEGRAY);

        ComboBox<String> employeeComboBox = new ComboBox<>();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username != 'hrmanager'")) {

            while (rs.next()) {
                employeeComboBox.getItems().add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        employeeComboBox.setPromptText("Select Employee");
        employeeComboBox.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: lightgray; -fx-font-size: 14px;");

        Button showWorkHoursButton = new Button("Show Work Hours");
        showWorkHoursButton.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 200px; -fx-pref-height: 40px; -fx-background-radius: 20px;");
        showWorkHoursButton.setOnAction(e -> {
            String selectedEmployee = employeeComboBox.getValue();
            if (selectedEmployee != null) {
                displayWorkHoursForEmployee(selectedEmployee, layout, employeeComboBox, showWorkHoursButton);
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select an employee.").show();
            }
        });

        VBox workHoursLayout = new VBox(10);
        workHoursLayout.setPadding(new Insets(10));
        workHoursLayout.setStyle("-fx-background-color: #f0f0f0;");

        layout.getChildren().addAll(titleLabel, employeeLabel, employeeComboBox, showWorkHoursButton, workHoursLayout);

        Scene scene = new Scene(layout, 400, 600);
        stage.setTitle("View Employee Work Hours");
        stage.setScene(scene);
        stage.show();
    }

    private void displayWorkHoursForEmployee(String employee, VBox layout, ComboBox<String> employeeComboBox, Button showWorkHoursButton) {
        VBox workHoursLayout = (VBox) layout.getChildren().get(4);

        workHoursLayout.getChildren().clear();

        Text loadingText = new Text("Loading work hours for " + employee + "...");
        loadingText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        loadingText.setFill(Color.DARKSLATEGRAY);
        workHoursLayout.getChildren().add(loadingText);

        String query = "SELECT name, date, hours, description FROM workhours WHERE username = ?";

        double totalHours = 0.0;

        try (Connection con = DBUtils.establishConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, employee);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasData = false;

                while (rs.next()) {
                    String projectName = rs.getString("name");
                    String date = rs.getString("date");
                    double hoursWorked = rs.getDouble("hours");
                    String description = rs.getString("description");

                    totalHours += hoursWorked;

                    VBox workHourEntry = new VBox(5);
                    workHourEntry.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-padding: 10; -fx-background-insets: 0; -fx-border-color: lightgray;");
                    workHourEntry.setMaxWidth(350);

                    Text workHourInfo = new Text(String.format("Project: %s\nDate: %s\nHours Worked: %.2f\nDescription: %s",
                            projectName, date, hoursWorked, description));
                    workHourEntry.getChildren().add(workHourInfo);
                    workHoursLayout.getChildren().add(workHourEntry);

                    hasData = true;
                }

                if (!hasData) {
                    Text noDataText = new Text("No work hours found for this employee.");
                    workHoursLayout.getChildren().add(noDataText);
                }

                Text totalHoursText = new Text("\nTotal Hours Worked: " + totalHours);
                workHoursLayout.getChildren().add(totalHoursText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Text errorText = new Text("Error fetching work hours: " + e.getMessage());
            workHoursLayout.getChildren().add(errorText);
        }
    }

}
