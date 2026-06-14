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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RunLogDashboard extends Application {
    private int runCounter = 5; // Starts at 5 because we load 5 dummy runs initially
    private XYChart.Series<Number, Number> distanceSeries;
    private PieChart.Data easyData;
    private PieChart.Data tempoData;
    private PieChart.Data longData;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Challo StrideSafe - Run Dashboard");

        // 1. MAIN LAYOUT
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F4F4F9;"); 

        // ================= NAVIGATION BAR START =================
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 25, 10, 25));
        header.setStyle("-fx-background-color: #0c3253;");

        // LEFT - LOGO (FIXED SIZE)
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

        String navStyle =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-cursor: hand;";

        for (Button b : new Button[]{dashboardBtn, runLogBtn, scheduleBtn, bookingBtn}) {
            b.setStyle(navStyle);

            b.setOnMouseEntered(e ->
                    b.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px;"));

            b.setOnMouseExited(e ->
                    b.setStyle(navStyle));
        }

        // Highlight the current page (RunLog)
        runLogBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        runLogBtn.setOnMouseExited(e -> runLogBtn.setStyle("-fx-background-color: #144a78; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;"));

        // --- NAVIGATION ROUTING LOGIC ---
        dashboardBtn.setOnAction(e -> {
            try {
                new AthleteDashboard().start(new Stage());
                ((Stage) dashboardBtn.getScene().getWindow()).close();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        scheduleBtn.setOnAction(e -> {
            try {
                new ScheduleTest().start(new Stage());
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
        Label username = new Label("Hi, Ahmad"); // Updated to match AthleteDashboard
        username.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Image profileImg = new Image("file:///C:/Users/muham/OneDrive/Documents/TestJava/src/JavaFx/profile.png");
        ImageView profile = new ImageView(profileImg);

        profile.setFitWidth(43);   
        profile.setFitHeight(43);  
        profile.setPreserveRatio(true);

        Circle clip = new Circle(21.5, 21.5, 21.5);
        profile.setClip(clip);

        ContextMenu profileMenu = new ContextMenu();
        MenuItem logout = new MenuItem("Logout");

        logout.setOnAction(e -> System.out.println("Logout clicked"));

        profileMenu.getItems().add(logout);

        profile.setOnMouseClicked(e ->
                profileMenu.show(profile, e.getScreenX(), e.getScreenY())
        );

        HBox userBox = new HBox(10, username, profile);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(
                logo,
                leftSpacer,
                navMenu,
                rightSpacer,
                userBox
        );
        root.setTop(header);
        // ================= NAVIGATION BAR END =================

        // 3. CENTER CONTENT
        HBox centerContent = new HBox(30);
        centerContent.setPadding(new Insets(30));
        centerContent.setAlignment(Pos.CENTER);

        //  INPUT FORM 
        VBox inputForm = new VBox(10); 
        inputForm.setStyle("-fx-background-color: #0c3253; -fx-background-radius: 15; -fx-padding: 20;");
        inputForm.setPrefWidth(350);

        Label distanceLbl = createWhiteLabel("Distance (KM)");
        TextField distanceInput = new TextField();
        distanceInput.setPromptText("e.g. 10.5");
        
        Label timeLbl = createWhiteLabel("Moving Time (HH:MM:SS or MM:SS)");
        TextField timeInput = new TextField();
        timeInput.setPromptText("e.g. 55:30");
        
        Label paceLbl = createWhiteLabel("Pace (/km)");
        TextField paceInput = new TextField();
        paceInput.setEditable(false);
        paceInput.setStyle("-fx-background-color: #E0E0E0;");

        // Dropdown for Run Type
        Label typeLbl = createWhiteLabel("Run Type");
        ComboBox<String> typeInput = new ComboBox<>();
        typeInput.getItems().addAll("Easy Run", "Tempo", "Long Run");
        typeInput.setPromptText("Select Type...");
        typeInput.setPrefWidth(Double.MAX_VALUE);

        
        distanceInput.textProperty().addListener((obs, oldVal, newVal) -> updatePace(distanceInput, timeInput, paceInput));
        timeInput.textProperty().addListener((obs, oldVal, newVal) -> updatePace(distanceInput, timeInput, paceInput));

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button btnAdd = new Button("Add");
        Button btnReset = new Button("Reset");
        Button btnDelete = new Button("Delete");
        
        String btnStyle = "-fx-background-color: white; -fx-text-fill: #0c3253; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;";
        btnAdd.setStyle(btnStyle);
        btnReset.setStyle(btnStyle);
        btnDelete.setStyle(btnStyle);
        
        // RIGHT SIDE: CHARTS
        VBox chartsBox = new VBox(20);
        chartsBox.setPrefWidth(450);

        // Top Chart
        NumberAxis xAxis = new NumberAxis(); 
        xAxis.setLabel("Run Session");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Kilometers");
        AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Distance Progression");
        
        distanceSeries = new XYChart.Series<>();
        distanceSeries.setName("Distance Logged");
        distanceSeries.getData().add(new XYChart.Data<>(1, 30));
        distanceSeries.getData().add(new XYChart.Data<>(2, 45));
        distanceSeries.getData().add(new XYChart.Data<>(3, 85));
        distanceSeries.getData().add(new XYChart.Data<>(4, 80));
        distanceSeries.getData().add(new XYChart.Data<>(5, 120));
        areaChart.getData().add(distanceSeries);
        areaChart.setPrefHeight(250);

        // Bottom Chart: Pie Chart 
        PieChart pieChart = new PieChart();
        easyData = new PieChart.Data("Easy Run", 5);
        tempoData = new PieChart.Data("Tempo", 2);
        longData = new PieChart.Data("Long Run", 3);
        
        pieChart.getData().addAll(easyData, tempoData, longData);
        pieChart.setTitle("Exertion Breakdown");
        pieChart.setPrefHeight(250);

        chartsBox.getChildren().addAll(areaChart, pieChart);

        btnAdd.setOnAction(e -> {
            try {
                // Grab data from the text fields
                double dist = Double.parseDouble(distanceInput.getText());
                String runType = typeInput.getValue();

                // Stop if they didn't select a run type
                if (runType == null) {
                    showAlert(Alert.AlertType.WARNING, "Missing Info", "Please select a Run Type!");
                    return;
                }

                // Update the Area Chart (Distance)
                runCounter++;
                distanceSeries.getData().add(new XYChart.Data<>(runCounter, dist));

                // Update the Pie Chart (Run Type)
                if (runType.equals("Easy Run")) {
                    easyData.setPieValue(easyData.getPieValue() + 1);
                } else if (runType.equals("Tempo")) {
                    tempoData.setPieValue(tempoData.getPieValue() + 1);
                } else if (runType.equals("Long Run")) {
                    longData.setPieValue(longData.getPieValue() + 1);
                }

                // Clear the form after adding
                distanceInput.clear();
                timeInput.clear();
                paceInput.clear();
                typeInput.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for distance.");
            }
        });

        // 2. RESET BUTTON
        btnReset.setOnAction(e -> {
            distanceInput.clear();
            timeInput.clear();
            paceInput.clear();
            typeInput.getSelectionModel().clearSelection();
        });
        buttonBox.getChildren().addAll(btnAdd, btnReset, btnDelete);

        inputForm.getChildren().addAll(
                distanceLbl, distanceInput, 
                timeLbl, timeInput, 
                paceLbl, paceInput, 
                typeLbl, typeInput,
                new Region(), buttonBox
        );

        centerContent.getChildren().addAll(inputForm, chartsBox);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1100, 700); // Widened slightly to match navigation bar size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Label createWhiteLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        return label;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updatePace(TextField distField, TextField timeField, TextField paceField) {
        try {
            double distance = Double.parseDouble(distField.getText());
            String timeStr = timeField.getText();
            
            if (distance > 0 && timeStr.contains(":")) {
                String[] timeParts = timeStr.split(":");
                double totalMinutes = 0;
                
                if (timeParts.length == 3) {
                    totalMinutes = (Integer.parseInt(timeParts[0]) * 60) + Integer.parseInt(timeParts[1]) + (Integer.parseInt(timeParts[2]) / 60.0);
                } else if (timeParts.length == 2) {
                    totalMinutes = Integer.parseInt(timeParts[0]) + (Integer.parseInt(timeParts[1]) / 60.0);
                } else return;
                
                double paceDecimal = totalMinutes / distance;
                int paceMinutes = (int) paceDecimal;
                int paceSeconds = (int) Math.round((paceDecimal - paceMinutes) * 60);
                
                if (paceSeconds == 60) {
                    paceMinutes++;
                    paceSeconds = 0;
                }
                paceField.setText(String.format("%d:%02d", paceMinutes, paceSeconds));
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            paceField.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}