import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

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
        try {
            List<Employee> employees = hrManager.getAllEmployees();
            for (Employee employee : employees) {
                employeeComboBox.getItems().add(employee.getUsername());
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
                displayWorkHoursForEmployee(selectedEmployee, layout);
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

    private void displayWorkHoursForEmployee(String employee, VBox layout) {
        VBox workHoursLayout = new VBox(10);
        workHoursLayout.setPadding(new Insets(10));
        workHoursLayout.setStyle("-fx-background-color: #f0f0f0;");

        double totalHours = 0.0;

        try {
            List<WorkLog> workLogs = hrManager.getWorkLogsByUsername(employee);

            if (workLogs.isEmpty()) {
                Text noDataText = new Text("No work hours found for this employee.");
                noDataText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                noDataText.setFill(Color.GRAY);
                workHoursLayout.getChildren().add(noDataText);
            } else {
                for (WorkLog workLog : workLogs) {
                    String projectName = workLog.getProjectName();
                    String date = workLog.getWorkDate().toString();
                    double hoursWorked = workLog.getHoursWorked();
                    String description = workLog.getDescription();

                    totalHours += hoursWorked;

                    VBox workHourEntry = new VBox(5);
                    workHourEntry.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-padding: 10; -fx-background-insets: 0; -fx-border-color: lightgray;");
                    workHourEntry.setMaxWidth(350);

                    Text workHourInfo = new Text(String.format("Project: %s\nDate: %s\nHours Worked: %.2f\nDescription: %s",
                            projectName, date, hoursWorked, description));
                    workHourInfo.setFont(Font.font("Arial", 13));
                    workHourEntry.getChildren().add(workHourInfo);

                    workHoursLayout.getChildren().add(workHourEntry);
                }

                Text totalHoursText = new Text("\nTotal Hours Worked: " + totalHours);
                totalHoursText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                totalHoursText.setFill(Color.DARKSLATEBLUE);
                workHoursLayout.getChildren().add(totalHoursText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Text errorText = new Text("Error fetching work hours: " + e.getMessage());
            errorText.setFill(Color.RED);
            errorText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            workHoursLayout.getChildren().add(errorText);
        }

        ScrollPane scrollPane = new ScrollPane(workHoursLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        layout.getChildren().set(4, scrollPane);
    }
}
