package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginTest extends Application {

    private Stage primaryStage;
    private String selectedUserType = "";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Challo Running App");
        primaryStage.setFullScreen(true);

        showUserTypeSelection();
        primaryStage.show();
    }

    private void showUserTypeSelection() {
        VBox selectionBox = new VBox(20);
        selectionBox.setAlignment(Pos.CENTER);
        selectionBox.setPadding(new Insets(40));
        setBackgroundImage(selectionBox, "-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Select User Type");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button btnAthlete = createStyledButton("Athlete", "#4CAF50");
        Button btnCoach = createStyledButton("Coach", "#2196F3");
        Button btnAdmin = createStyledButton("Admin", "#FF9800");

        selectionBox.getChildren().addAll(titleLabel, btnAthlete, btnCoach, btnAdmin);

        Scene selectionScene = new Scene(selectionBox, 400, 350);
        primaryStage.setScene(selectionScene);

        btnAthlete.setOnAction(e -> {
            selectedUserType = "Athlete";
            showLoginPage();
        });

        btnCoach.setOnAction(e -> {
            selectedUserType = "Coach";
            showLoginPage();
        });

        btnAdmin.setOnAction(e -> {
            selectedUserType = "Admin";
            showLoginPage();
        });
    }

    private void showLoginPage() {
        GridPane loginGrid = new GridPane();
        loginGrid.setHgap(15);
        loginGrid.setVgap(20);
        loginGrid.setPadding(new Insets(40));
        loginGrid.setAlignment(Pos.CENTER);
        setBackgroundImage(loginGrid, "-fx-background-color: white;");

        Label titleLbl = new Label(selectedUserType + " Login");
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        GridPane.setHalignment(titleLbl, javafx.geometry.HPos.CENTER);
        GridPane.setColumnSpan(titleLbl, 2);

        Label lblUser = new Label("Username:");
        TextField txtUser = new TextField();
        txtUser.setPromptText("Enter username");
        txtUser.setPrefWidth(250);

        Label lblPass = new Label("Password:");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Enter password");
        txtPass.setPrefWidth(250);

        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(120);
        btnLogin.setStyle(
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        Button btnBack = new Button("Back");
        btnBack.setPrefWidth(120);
        btnBack.setStyle("-fx-background-color: #9e9e9e; -fx-text-fill: white; -fx-background-radius: 5;");

        btnLogin.setOnAction(e -> {
            String username = txtUser.getText();
            String password = txtPass.getText();

            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter both username and password.");
                alert.showAndWait();
            } else {
                showMainPage(username);
            }
        });

        btnBack.setOnAction(e -> {
            showUserTypeSelection();
        });

        loginGrid.add(titleLbl, 0, 0, 2, 1);
        loginGrid.add(lblUser, 0, 1);
        loginGrid.add(txtUser, 1, 1);
        loginGrid.add(lblPass, 0, 2);
        loginGrid.add(txtPass, 1, 2);
        loginGrid.add(btnLogin, 1, 3);
        loginGrid.add(btnBack, 0, 3);

        Scene loginScene = new Scene(loginGrid, 450, 320);
        primaryStage.setScene(loginScene);

        txtUser.requestFocus();
    }

    private void showMainPage(String username) {
        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(40));
        setBackgroundImage(mainBox, "-fx-background-color: #e3f2fd;");

        Label welcomeLbl = new Label("Welcome, " + username + "!");
        welcomeLbl.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label typeLbl = new Label("User Type: " + selectedUserType);
        typeLbl.setFont(Font.font("Arial", 16));

        Button btnDashboard = new Button("Go to Dashboard");
        btnDashboard.setPrefWidth(200);
        btnDashboard.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14;");

        Button btnLogout = new Button("Logout");
        btnLogout.setPrefWidth(200);
        btnLogout.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14;");

        btnDashboard.setOnAction(e -> {
            showUserDashboard(username);
        });

        btnLogout.setOnAction(e -> {
            showUserTypeSelection();
        });

        mainBox.getChildren().addAll(welcomeLbl, typeLbl, btnDashboard, btnLogout);

        Scene mainScene = new Scene(mainBox, 450, 350);
        primaryStage.setScene(mainScene);
    }

    private void showUserDashboard(String username) {
        VBox dashboardBox = new VBox(20);
        dashboardBox.setAlignment(Pos.CENTER);
        dashboardBox.setPadding(new Insets(40));

        String bgColor = "";
        String dashboardTitle = "";

        switch (selectedUserType) {
            case "Athlete":
                bgColor = "#e8f5e9";
                dashboardTitle = "Athlete Dashboard";
                break;
            case "Coach":
                bgColor = "#e3f2fd";
                dashboardTitle = "Coach Dashboard";
                break;
            case "Admin":
                bgColor = "#fff3e0";
                dashboardTitle = "Admin Dashboard";
                break;
        }

        setBackgroundImage(dashboardBox, "-fx-background-color: " + bgColor + ";");

        Label titleLbl = new Label(dashboardTitle);
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label userLbl = new Label("Logged in as: " + username);
        userLbl.setFont(Font.font("Arial", 14));

        Label contentLbl = new Label(getDashboardContent(selectedUserType));
        contentLbl.setFont(Font.font("Arial", 13));
        contentLbl.setWrapText(true);

        Button btnBack = new Button("Back to Main");
        btnBack.setPrefWidth(150);
        btnBack.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

        btnBack.setOnAction(e -> {
            showMainPage(username);
        });

        dashboardBox.getChildren().addAll(titleLbl, userLbl, contentLbl, btnBack);

        Scene dashboardScene = new Scene(dashboardBox, 500, 400);
        primaryStage.setScene(dashboardScene);
    }

    private String getDashboardContent(String userType) {
        switch (userType) {
            case "Athlete":
                return "• View your training schedule\n• Track your performance\n• Update your profile";
            case "Coach":
                return "• Manage athlete profiles\n• Create training programs\n• Monitor athlete progress\n• Schedule training sessions";
            case "Admin":
                return "• Manage all users\n• System settings\n• Generate reports\n• Manage competitions";
            default:
                return "Dashboard content";
        }
    }

    private void setBackgroundImage(Region region, String fallbackStyle) {
        java.net.URL imageUrl = getClass().getResource("/background.png");
        if (imageUrl != null) {
            region.setStyle("-fx-background-image: url('" + imageUrl.toExternalForm() + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center center;");
        } else {
            region.setStyle(fallbackStyle);
        }
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(250);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-color: " + color
                + "; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));

        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}