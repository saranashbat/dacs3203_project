import java.sql.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserLogin {
    private Scene loginScene;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Stage stage;

    public UserLogin(Stage primaryStage) {
        this.stage = primaryStage;
    }

    public void initializeComponents() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setStyle("-fx-background-color: #f4f4f4;"); // Light gray background

        Label titleLabel = new Label("Employee Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        Label usernameLabel = new Label("Username:");
        usernameField.setMaxWidth(200);
        usernameField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc;");

        Label passwordLabel = new Label("Password:");
        passwordField.setMaxWidth(200);
        passwordField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc;");

        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        loginButton.setMaxWidth(200);
        loginButton.setOnAction(event -> authenticate());

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-weight: bold;");
        signUpButton.setMaxWidth(200);
        /*signUpButton.setOnAction(event -> {
            UserRegistration userRegistration = new UserRegistration(stage);
            userRegistration.initializeComponents();
        });*/

        loginLayout.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField, loginButton, signUpButton);

        loginScene = new Scene(loginLayout, 400, 350); // Adjusted size for better proportions

        stage.setTitle("User Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Connection con = DBUtils.establishConnection();
        String hashedpass = null;
        byte[] salt = null;
        String saltQuery = "SELECT salt FROM users WHERE username=?;";

        try {
            PreparedStatement statement = con.prepareStatement(saltQuery);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String base64Salt = rs.getString("salt");
                salt = Base64.getDecoder().decode(base64Salt);
            } else {
                showAlert("Authentication Failed", "Invalid username or password.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
            return;
        }

        try {
            hashedpass = generateHash(password, "SHA-256", salt);
        } catch (NoSuchAlgorithmException e) {
            showAlert("Error", "Hashing algorithm not found.");
            return;
        }

        String query = "SELECT * FROM users WHERE username=? AND hashedpass=?;";

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, hashedpass);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                if (role.equals("hrmanager")) {
                    HRManager user = new HRManager(username, password, role);
                    HRManagerScreen hrManagerScreen = new HRManagerScreen(user);
                    hrManagerScreen.initializeComponents();
                } else if (role.equals("employee")) {
                    Employee user = new Employee(username, password, role, rs.getString("position"), rs.getDouble("salary"));
                    EmployeeScreen employeeScreen = new EmployeeScreen(user);
                    employeeScreen.initializeComponents();
                }
            } else {
                showAlert("Authentication Failed", "Invalid username or password.");
            }
            DBUtils.closeConnection(con, statement);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    private String generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}