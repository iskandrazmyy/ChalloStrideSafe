/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author iskandar
 */

import javafx.application.Application;
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

public class AthleteDashboard extends Application {

    private String athleteName = "Ahmad";

    public AthleteDashboard() {
    }

    public AthleteDashboard(String athleteName) {
        if (athleteName != null && !athleteName.isEmpty()) {
            this.athleteName = athleteName;
        }
    }
    private double weightKg = 68.5;
    private String targetPace = "5:30 /km";
    private double fatigueScore = 32.5;
    private String riskStatus = "SAFE";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Challo StrideSafe - Athlete Home");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F4F9;");

        // ================= NAVIGATION BAR START =================
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 25, 10, 25));
        header.setStyle("-fx-background-color: #0c3253;");

        // LEFT - LOGO
        Image logoImg = new Image("file:///C:/Users/muham/OneDrive/Documents/TestJava/src/JavaFx/logo.png");
        ImageView logo = new ImageView(logoImg);
        logo.setFitWidth(67);  
        logo.setFitHeight(67);  
        logo.setPreserveRatio(true);

        // ================= MIDDLE NAV =================
        HBox navMenu = new HBox(25);
        navMenu.setAlignment(Pos.CENTER);

        Button dashboardBtn = new Button("Dashboard");
        Button runLogBtn = new Button("RunLog");
        Button scheduleBtn = new Button("Schedule");
        Button bookingBtn = new Button("Booking");

        String navStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;";

        for (Button b : new Button[]{dashboardBtn, runLogBtn, scheduleBtn, bookingBtn}) {
            b.setStyle(navStyle);
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px;"));
            b.setOnMouseExited(e -> b.setStyle(navStyle));
        }
        
        // Highlight the current page
        dashboardBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        dashboardBtn.setOnMouseExited(e -> dashboardBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"));

        // --- NAVIGATION ROUTING LOGIC ---
        runLogBtn.setOnAction(e -> {
            try {
                new RunLogDashboard().start(new Stage());
                ((Stage) runLogBtn.getScene().getWindow()).close();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        scheduleBtn.setOnAction(e -> {
            try {
                new ScheduleTest(athleteName).start(new Stage());
                ((Stage) scheduleBtn.getScene().getWindow()).close();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        bookingBtn.setOnAction(e -> {
            try {
                new BookingSlotUI().start(new Stage());
                ((Stage) bookingBtn.getScene().getWindow()).close();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        navMenu.getChildren().addAll(dashboardBtn, runLogBtn, scheduleBtn, bookingBtn);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // ================= RIGHT USER SECTION =================
        Label username = new Label("Hi, " + athleteName);
        username.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Image profileImg = new Image("file:///C:/Users/muham/OneDrive/Documents/TestJava/src/JavaFx/profile.png");
        ImageView profile = new ImageView(profileImg);
        profile.setFitWidth(43);   
        profile.setFitHeight(43);  
        profile.setPreserveRatio(true);

        Circle clip = new Circle(21.5, 21.5, 21.5);
        profile.setClip(clip);

        ContextMenu profileMenu = new ContextMenu();
        MenuItem editProfile = new MenuItem("Edit Profile");
        MenuItem logout = new MenuItem("Logout");

        // --- LOGOUT LOGIC (Connects back to LoginTest) ---
        logout.setOnAction(e -> {
            try {
                new LoginTest().start(new Stage());
                ((Stage) header.getScene().getWindow()).close();
            } catch (Exception ex) { 
                ex.printStackTrace(); 
            }
        });

        profileMenu.getItems().addAll(editProfile, logout);

        profile.setOnMouseClicked(e ->
                profileMenu.show(profile, e.getScreenX(), e.getScreenY())
        );

        HBox userBox = new HBox(10, username, profile);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(logo, leftSpacer, navMenu, rightSpacer, userBox);
        root.setTop(header);
        // ================= NAVIGATION BAR END =================

        // ================= CENTER CONTENT =================
        VBox centerLayout = new VBox(30);
        centerLayout.setPadding(new Insets(40));
        centerLayout.setAlignment(Pos.TOP_CENTER);

        Label welcomeText = new Label("Welcome back, " + athleteName + "!");
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeText.setTextFill(Color.web("#0c3253"));

        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);

        // Card 1 – Profile
        VBox profileCard = createInfoCard("Athlete Profile");
        profileCard.getChildren().addAll(
            createCardText("Weight: " + weightKg + " kg"),
            createCardText("Target Pace: " + targetPace),
            new Region()
        );
        Button btnEditProfile = new Button("Edit Profile");
        styleButton(btnEditProfile);
        btnEditProfile.setOnAction(e -> showAlert("Edit Profile", "Opening profile settings..."));
        profileCard.getChildren().add(btnEditProfile);

        // Card 2 – Injury Monitor
        VBox statusCard = createInfoCard("Injury Risk Monitor");
        Label scoreLbl  = createCardText("Current Fatigue Score: " + fatigueScore);
        Label statusLbl = new Label("Status: " + riskStatus);
        statusLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        if      (riskStatus.equals("SAFE"))    statusLbl.setTextFill(Color.web("#4CAF50"));
        else if (riskStatus.equals("WARNING")) statusLbl.setTextFill(Color.web("#FFC107"));
        else                                   statusLbl.setTextFill(Color.web("#F44336"));
        statusCard.getChildren().addAll(scoreLbl, statusLbl);

        cardsContainer.getChildren().addAll(profileCard, statusCard);

        // Action buttons
        HBox actionButtonsBox = new HBox(20);
        actionButtonsBox.setAlignment(Pos.CENTER);

        Button btnOpenLogs = new Button("🏃 Go to Run Logs & Charts");
        styleNavButton(btnOpenLogs);
        btnOpenLogs.setOnAction(e -> runLogBtn.fire()); 

        Button btnBookSlot = new Button("📅 Book Training");
        styleNavButton(btnBookSlot);
        btnBookSlot.setOnAction(e -> bookingBtn.fire()); 

        Button btnViewSchedule = new Button("🗓️ View Schedule");
        styleNavButton(btnViewSchedule);
        btnViewSchedule.setOnAction(e -> scheduleBtn.fire()); 

        actionButtonsBox.getChildren().addAll(btnOpenLogs, btnBookSlot, btnViewSchedule);
        centerLayout.getChildren().addAll(welcomeText, cardsContainer, actionButtonsBox);
        root.setCenter(centerLayout);

        Scene scene = new Scene(root, 1100, 700); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private VBox createInfoCard(String titleText) {
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: #0c3253; -fx-background-radius: 15; -fx-padding: 25;");
        card.setPrefSize(300, 200);
        card.setAlignment(Pos.TOP_CENTER);
        Label title = new Label(titleText);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        card.getChildren().addAll(title, new Separator());
        return card;
    }

    private Label createCardText(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        return label;
    }

    private void styleButton(Button btn) {
        btn.setStyle("-fx-background-color: white; -fx-text-fill: #1A237E; " +
                     "-fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
    }

    private void styleNavButton(Button btn) {
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        btn.setStyle("-fx-background-color: #0c3253; -fx-text-fill: white; " +
                     "-fx-padding: 15 40 15 40; -fx-background-radius: 30; -fx-cursor: hand;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}
