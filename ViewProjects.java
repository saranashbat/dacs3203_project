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
import java.util.ArrayList;
import java.util.List;

public class ViewProjects {
    private HRManager hrManager;
    private Stage stage;

    public ViewProjects(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("View Employees Assigned to a Project");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label projectLabel = new Label("Select Project:");
        projectLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        projectLabel.setTextFill(Color.DARKSLATEGRAY);

        List<String> projects = new ArrayList<>();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM projects")) {
            while (rs.next()) {
                projects.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ComboBox<String> projectComboBox = new ComboBox<>();
        projectComboBox.getItems().addAll(projects);
        projectComboBox.setPromptText("Select Project");
        projectComboBox.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: lightgray; -fx-font-size: 14px;");

        VBox employeesLayout = new VBox(10);
        employeesLayout.setPadding(new Insets(10));
        employeesLayout.setStyle("-fx-background-color: #f0f0f0;");

        Button showEmployeesButton = new Button("Show Employees");
        showEmployeesButton.setStyle("-fx-background-color: #7fa9d8; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-min-width: 200px; -fx-pref-height: 40px; -fx-background-radius: 20px;");
        showEmployeesButton.setOnAction(e -> {
            String selectedProject = projectComboBox.getValue();

            if (selectedProject != null) {
                List<String> employeeDetails = new ArrayList<>();
                try (Connection con = DBUtils.establishConnection();
                     PreparedStatement stmt = con.prepareStatement("SELECT u.username, u.position FROM users u JOIN projects p ON u.project = p.name WHERE p.name = ?")) {
                    stmt.setString(1, selectedProject);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            String employee = "Username: " + rs.getString("username") + " | Position: " + rs.getString("position");
                            employeeDetails.add(employee);
                        }
                    }
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "Error fetching employees: " + ex.getMessage()).show();
                }

                employeesLayout.getChildren().clear();

                if (employeeDetails.isEmpty()) {
                    Label noEmployeesLabel = new Label("No employees assigned to this project.");
                    noEmployeesLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                    noEmployeesLabel.setTextFill(Color.DARKSLATEGRAY);
                    employeesLayout.getChildren().add(noEmployeesLabel);
                } else {
                    for (String employee : employeeDetails) {
                        HBox employeeRow = new HBox(10);
                        employeeRow.setAlignment(Pos.CENTER_LEFT);

                        Label employeeLabel = new Label(employee);
                        employeeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                        employeeLabel.setTextFill(Color.DARKSLATEGRAY);
                        employeeRow.getChildren().add(employeeLabel);

                        employeesLayout.getChildren().add(employeeRow);
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a project.").show();
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: #333333; -fx-font-weight: normal; "
                + "-fx-min-width: 120px; -fx-pref-height: 30px; -fx-background-radius: 15px;");
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, projectLabel, projectComboBox, showEmployeesButton, employeesLayout, backButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("View Employees Assigned to Project");
        stage.setScene(scene);
        stage.show();
    }
}
