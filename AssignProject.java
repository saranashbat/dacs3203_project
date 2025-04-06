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

public class AssignProject {
    private HRManager hrManager;
    private Stage stage;

    public AssignProject(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public void initializeComponents() {
        stage = new Stage();

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("Assign Project to Employee");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label employeeLabel = new Label("Select Employee:");
        employeeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        employeeLabel.setTextFill(Color.DARKSLATEGRAY);

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

        ComboBox<String> userComboBox = new ComboBox<>();
        userComboBox.getItems().addAll(usernames);
        userComboBox.setPromptText("Select Employee"); // Placeholder text
        userComboBox.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: lightgray; -fx-font-size: 14px;");

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

        Button assignButton = new Button("Assign Project");
        styleButton(assignButton);
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

        Button viewProjectsButton = new Button("View All Projects");
        styleButton(viewProjectsButton);
        viewProjectsButton.setOnAction(e -> {
            ViewProjects viewProjects = new ViewProjects(hrManager);
            viewProjects.initializeComponents();
        });

        Button backButton = new Button("Back");
        styleBackButton(backButton);
        backButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(titleLabel, employeeLabel, userComboBox, projectLabel, projectComboBox, assignButton, viewProjectsButton, backButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("Assign Project");
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
}
