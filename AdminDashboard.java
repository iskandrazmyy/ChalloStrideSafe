package com.mycompany.mavenproject3;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    private String adminName;
    private String userRole = "System Administrator";
    private int totalAthletes = 45;
    private int totalCoaches = 12;
    private String systemStatus = "ONLINE";

    // 1. Constructor to receive a name (if called from elsewhere)
    public AdminDashboard(String adminName) {
        this.adminName = adminName;
    }
    
    // 2. Default constructor so it can run completely standalone
    public AdminDashboard() {
        this.adminName = "Iskandar";
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Challo StrideSafe - Admin Hub");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F4F9;");

        // ================= NAVIGATION BAR START =================
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 25, 10, 25));
        header.setStyle("-fx-background-color: #0c3253;");

        // LEFT - LOGO (FIXED SIZE)
        try {
            Image logoImg = new Image("file:///C:/Users/muham/OneDrive/Documents/TestJava/src/JavaFx/logo.png");
            ImageView logo = new ImageView(logoImg);
            logo.setFitWidth(67);
            logo.setFitHeight(67);
            logo.setPreserveRatio(true);
            header.getChildren().add(logo);
        } catch (Exception e) {
            Label logoFallback = new Label("Logo");
            logoFallback.setTextFill(Color.WHITE);
            header.getChildren().add(logoFallback);
        }

        // ================= MIDDLE NAV =================
        HBox navMenu = new HBox(25);
        navMenu.setAlignment(Pos.CENTER);

        Button dashboardBtn = new Button("Dashboard");
        Button usersBtn = new Button("Manage Users");
        Button logsBtn = new Button("System Logs");
        Button settingsBtn = new Button("Settings");
        Button manageBtn = new Button("Manage");
        Button coachBtn = new Button("Coach");

        String navStyle =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;";

        for (Button b : new Button[]{
                dashboardBtn, usersBtn, logsBtn, settingsBtn, manageBtn, coachBtn}) {
            b.setStyle(navStyle);
            b.setOnMouseEntered(e ->
                    b.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px;"));
            b.setOnMouseExited(e ->
                    b.setStyle(navStyle));
        }

        dashboardBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        dashboardBtn.setOnMouseExited(e -> dashboardBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"));

        // --- NAVIGATION ROUTING LOGIC ---
        usersBtn.setOnAction(e -> showAlert("Feature Unlinked", "This feature is currently running in standalone mode."));
        logsBtn.setOnAction(e -> showAlert("Feature Unlinked", "This feature is currently running in standalone mode."));
        settingsBtn.setOnAction(e -> showAlert("Feature Unlinked", "This feature is currently running in standalone mode."));
        manageBtn.setOnAction(e -> showAlert("Feature Unlinked", "This feature is currently running in standalone mode."));

        coachBtn.setOnAction(e -> {
            CoachDashboard coachDashboard = new CoachDashboard(primaryStage);
            primaryStage.setScene(coachDashboard.getDashboardScene());
            primaryStage.setTitle("Challo StrideSafe - Coach Dashboard");
        });

        navMenu.getChildren().addAll(
                dashboardBtn, usersBtn, logsBtn, settingsBtn, manageBtn, coachBtn
        );

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // USER SECTION
        Label username = new Label("Hi, " + adminName);
        username.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Image profileImg = null;
        try {
            profileImg = new Image("file:///C:/Users/muham/OneDrive/Documents/TestJava/src/JavaFx/profile.png");
        } catch (Exception e) {
            // Ignore if image not found
        }
        
        ImageView profile = new ImageView(profileImg);
        profile.setFitWidth(43);   
        profile.setFitHeight(43);  
        profile.setPreserveRatio(true);

        Circle clip = new Circle(21.5, 21.5, 21.5);
        profile.setClip(clip);

        ContextMenu profileMenu = new ContextMenu();
        MenuItem logout = new MenuItem("Logout");
        
        // STANDALONE LOGOUT LOGIC: Just closes the window
        logout.setOnAction(e -> {
            System.out.println("User logged out. Closing application.");
            primaryStage.close(); 
        });
        
        profileMenu.getItems().add(logout);
        profile.setOnMouseClicked(e -> profileMenu.show(profile, e.getScreenX(), e.getScreenY()));

        HBox userBox = new HBox(10, username, profile);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(leftSpacer, navMenu, rightSpacer, userBox);
        
        root.setTop(header);
        // ================= NAVIGATION BAR END =================

        // ================= CENTER CONTENT =================
        VBox centerLayout = new VBox(20);
        centerLayout.setPadding(new Insets(40));
        centerLayout.setAlignment(Pos.TOP_CENTER);

        // Personalized Welcome
        VBox welcomeBox = new VBox(5);
        welcomeBox.setAlignment(Pos.CENTER);
        Label welcomeText = new Label("Welcome back, " + adminName + "!");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeText.setTextFill(Color.web("#0c3253"));
        
        Label roleText = new Label("Assigned Role: " + userRole);
        roleText.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        roleText.setTextFill(Color.web("#666666")); 
        
        welcomeBox.getChildren().addAll(welcomeText, roleText);

        // High-Level Metric Cards
        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox athleteCard = createMetricCard("Registered Athletes", String.valueOf(totalAthletes), "#4CAF50"); 
        VBox coachCard = createMetricCard("Registered Coaches", String.valueOf(totalCoaches), "#2196F3"); 
        VBox statusCard = createMetricCard("System Status", systemStatus, "#4CAF50"); 
        
        cardsContainer.getChildren().addAll(athleteCard, coachCard, statusCard);

        // Central Hub Navigation Buttons
        HBox actionButtonsBox = new HBox(20);
        actionButtonsBox.setAlignment(Pos.CENTER);
        actionButtonsBox.setPadding(new Insets(20, 0, 0, 0));

        Button btnManageUsers = new Button("Manage All Users");
        Button btnSystemLogs = new Button("View System Logs");
        Button btnDatabase = new Button("Database Backup");

        for (Button btn : new Button[]{btnManageUsers, btnSystemLogs, btnDatabase}) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            btn.setStyle("-fx-background-color: #0c3253; -fx-text-fill: white; -fx-padding: 15 40 15 40; -fx-background-radius: 30; -fx-cursor: hand;");
            btn.setOnAction(e -> showAlert("Feature Unlinked", "This feature is currently running in standalone mode."));
        }

        actionButtonsBox.getChildren().addAll(btnManageUsers, btnSystemLogs, btnDatabase);

        // Recent Activity Table (Audit Trail)
        VBox tableSection = new VBox(10);
        tableSection.setAlignment(Pos.CENTER);
        tableSection.setPadding(new Insets(20, 0, 0, 0));
        
        Label tableTitle = new Label("Recent System Activity");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        tableTitle.setTextFill(Color.web("#0c3253"));

        TableView<String[]> auditTable = new TableView<>();
        auditTable.setPrefHeight(200);
        auditTable.setPrefWidth(850);
        auditTable.setMaxWidth(850);
        auditTable.setStyle("-fx-border-color: #0c3253; -fx-border-radius: 5;");

        // Use the universally compatible resize policy
        auditTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<String[], String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        timeCol.setPrefWidth(150);

        TableColumn<String[], String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        userCol.setPrefWidth(150);

        TableColumn<String[], String> actionCol = new TableColumn<>("Action Performed");
        actionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        actionCol.setPrefWidth(545);

        auditTable.getColumns().addAll(timeCol, userCol, actionCol);

        // Dummy Data
        auditTable.getItems().add(new String[]{"2026-06-11 10:15:22", "Athlete (Ahmad)", "Successfully logged a new 10km run."});
        auditTable.getItems().add(new String[]{"2026-06-11 09:42:01", "Coach (Hakim)", "Updated schedule for Athlete (Ali)."});
        auditTable.getItems().add(new String[]{"2026-06-11 08:30:45", "System Admin", "Performed weekly database backup."});

        tableSection.getChildren().addAll(tableTitle, auditTable);

        centerLayout.getChildren().addAll(welcomeBox, cardsContainer, actionButtonsBox, tableSection);
        root.setCenter(centerLayout);

        Scene scene = new Scene(root, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMetricCard(String titleText, String metricValue, String metricColor) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: #0c3253; -fx-background-radius: 15; -fx-padding: 25;");
        card.setPrefSize(270, 150);
        card.setAlignment(Pos.CENTER);
        
        Label title = new Label(titleText);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        Label metric = new Label(metricValue);
        metric.setTextFill(Color.web(metricColor)); 
        metric.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        
        card.getChildren().addAll(title, metric);
        return card;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}