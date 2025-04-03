import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignProject {
    private HRManager hrManager;
    private Stage stage;

    public AssignProject(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f4f4f4;");

        Label titleLabel = new Label("Assign Project to Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // Fetch users excluding "HRManager"
        List<String> usernames = new ArrayList<>();
        try (Connection con = DBUtils.establishConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username != 'HRManager'")) {
            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a dropdown for users
        ComboBox<String> userComboBox = new ComboBox<>();
        userComboBox.getItems().addAll(usernames);

        // Fetch projects from the "projects" table
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

        // Create a dropdown for projects
        ComboBox<String> projectComboBox = new ComboBox<>();
        projectComboBox.getItems().addAll(projects);

        Button assignButton = new Button("Assign Project");
        assignButton.setOnAction(e -> {
            String selectedUser = userComboBox.getValue();
            String selectedProject = projectComboBox.getValue();

            if (selectedUser != null && selectedProject != null) {
                try {
                    hrManager.assignProject(selectedUser, selectedProject);
                    new Alert(Alert.AlertType.INFORMATION, "Project assigned successfully.").show();
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "Error assigning project: " + ex.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select both a user and a project.").show();
            }
        });

        // Display all users with their current projects
        VBox employeeContainer = new VBox(10);
        employeeContainer.setPadding(new Insets(10));

        Button loadEmployeesButton = new Button("Load Employees");
        loadEmployeesButton.setOnAction(e -> {
            employeeContainer.getChildren().clear();
            try (Connection con = DBUtils.establishConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT username, project FROM users WHERE username != 'HRManager'")) {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String project = rs.getString("project");
                    Label employeeLabel = new Label("Username: " + username + "\nProject: " + project);
                    employeeLabel.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-color: lightgray;");
                    employeeContainer.getChildren().add(employeeLabel);
                }
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Error fetching employees: " + ex.getMessage()).show();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, userComboBox, projectComboBox, assignButton, loadEmployeesButton, employeeContainer, backButton);

        Scene scene = new Scene(layout, 400, 600);
        stage.setTitle("Assign Project");
        stage.setScene(scene);
        stage.show();
    }
}
