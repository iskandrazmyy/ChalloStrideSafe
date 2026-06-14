/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author iskandar
 */
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CoachDashboard {

    private Stage primaryStage;
    private Scene dashboardScene;

    public CoachDashboard(Stage stage) {
        this.primaryStage = stage;
    }

    // =====================================================
    // TOP NAVIGATION BAR
    // =====================================================
    private HBox createTopHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 25, 10, 25));
        header.setStyle(
            "-fx-background-color: #0c3253;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10,0,0,3);"
        );

        // Smart dynamic image locator for your Logo
        File logoFile = new File("logo.png");
        if (!logoFile.exists()) logoFile = new File("logo.jpeg");
        if (!logoFile.exists()) logoFile = new File("logo.jpg");

        if (logoFile.exists()) {
            ImageView logoView = new ImageView(new Image(logoFile.toURI().toString()));
            logoView.setFitHeight(45);
            logoView.setPreserveRatio(true);
            header.getChildren().add(logoView);
        } else {
            Label fallbackLogo = new Label("🏃 Run With Challo");
            fallbackLogo.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
            header.getChildren().add(fallbackLogo);
        }

        HBox navMenu = new HBox(25);
        navMenu.setAlignment(Pos.CENTER);

        Button dashboardBtn = new Button("Dashboard");
        Button scheduleBtn = new Button("Schedule");
        Button coachBtn = new Button("Coach");

        String navStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;";

        Button[] navButtons = {dashboardBtn, scheduleBtn, coachBtn};
        for(Button b : navButtons){
            b.setStyle(navStyle);
            b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:#144a78; -fx-text-fill:white; -fx-font-size:14px; -fx-font-weight:bold; -fx-background-radius:8;"));
            b.setOnMouseExited(e -> b.setStyle(navStyle));
        }

        dashboardBtn.setOnAction(e -> primaryStage.setScene(getDashboardScene()));
        navMenu.getChildren().addAll(dashboardBtn, scheduleBtn, coachBtn);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Label username = new Label("Hi Coach");
        username.setStyle("-fx-text-fill:white; -fx-font-size:14px; -fx-font-weight:bold;");

        // Smart dynamic image locator for your Profile Icon
        StackPane avatarContainer = new StackPane();
        File avatarFile = new File("profile_icon.png");
        if (!avatarFile.exists()) avatarFile = new File("profile_icon.jpeg");
        if (!avatarFile.exists()) avatarFile = new File("profile_icon.jpg");

        if (avatarFile.exists()) {
            ImageView avatarView = new ImageView(new Image(avatarFile.toURI().toString()));
            avatarView.setFitWidth(40);
            avatarView.setFitHeight(40);
            avatarView.setPreserveRatio(true);
            
            Circle clip = new Circle(20, 20, 20);
            avatarView.setClip(clip);
            avatarContainer.getChildren().add(avatarView);
        } else {
            Circle avatarCircle = new Circle(20, Color.WHITE);
            Label avatarLetter = new Label("C");
            avatarLetter.setStyle("-fx-font-size:16px; -fx-font-weight:bold; -fx-text-fill:#0c3253;");
            avatarContainer.getChildren().addAll(avatarCircle, avatarLetter);
        }

        ContextMenu profileMenu = new ContextMenu();
        MenuItem profileItem = new MenuItem("Profile");
        MenuItem logoutItem = new MenuItem("Logout");
        profileMenu.getItems().addAll(profileItem, logoutItem);

        avatarContainer.setOnMouseClicked(e -> profileMenu.show(avatarContainer, e.getScreenX(), e.getScreenY()));
        logoutItem.setOnAction(e -> System.out.println("Logout Clicked"));

        HBox userBox = new HBox(10, username, avatarContainer);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(leftSpacer, navMenu, rightSpacer, userBox);
        return header;
    }

    // =====================================================
    // DASHBOARD SCENE
    // =====================================================
    public Scene getDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0B192C;");
        root.setTop(createTopHeader());

        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color:#0B192C;");

        Label title = new Label("COACH DASHBOARD");
        title.setStyle("-fx-font-size:28px; -fx-font-weight:bold; -fx-text-fill:white;");

        Label subtitle = new Label("Monitor athlete performance and schedules");
        subtitle.setStyle("-fx-text-fill:#B8C1CC; -fx-font-size:14px;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search Athlete...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-radius:10; -fx-padding:10;");

        List<Athlete> allAthletes = loadAthletesFromFile();
        int totalAthletes = allAthletes.size();
        int highRiskCount = 0;
        int stableCount = 0;

        TableView<Athlete> normalTable = buildTable();
        TableView<Athlete> riskyTable = buildTable();
        TableView<Athlete> underTable = buildTable();

        for (Athlete a : allAthletes) {
            if (a.getStatus().contains("Stable") || a.getStatus().contains("Consistent")) {
                normalTable.getItems().add(a);
                stableCount++;
            } else if (a.getStatus().contains("High Risk")) {
                riskyTable.getItems().add(a);
                highRiskCount++;
            } else {
                underTable.getItems().add(a);
            }
        }

        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
            createStatCard("🏃 Total Athletes", String.valueOf(totalAthletes), "#3498DB"),
            createStatCard("⚠ High Risk", String.valueOf(highRiskCount), "#E74C3C"),
            createStatCard("✅ Stable", String.valueOf(stableCount), "#2ECC71"),
            createStatCard("📅 Sessions Today", totalAthletes > 0 ? "6" : "0", "#F39C12")
        );

        VBox summaryPanel = new VBox(10);
        summaryPanel.setPadding(new Insets(20));
        summaryPanel.setStyle("-fx-background-color:#13243B; -fx-background-radius:10;");

        Label summaryTitle = new Label("Dashboard Summary");
        summaryTitle.setStyle("-fx-text-fill:white; -fx-font-size:18px; -fx-font-weight:bold;");

        Label summaryText = new Label(
            "You currently have " + totalAthletes + " active athletes monitoring your system. "
            + "There are " + highRiskCount + " athlete(s) requiring critical attention, while "
            + stableCount + " athlete(s) are performing within expected targets."
        );
        summaryText.setWrapText(true);
        summaryText.setStyle("-fx-text-fill:#D0D7E2; -fx-font-size:13px;");
        summaryPanel.getChildren().addAll(summaryTitle, summaryText);

        VBox normalSection = buildSection("Normal Performance", "#d4edda", "#155724");
        normalSection.getChildren().add(normalTable);

        VBox riskySection = buildSection("Risky (Critical Attention) ⚠", "#fff3cd", "#856404");
        riskySection.getChildren().add(riskyTable);

        VBox underSection = buildSection("Underperformance / Inactive", "#e2e3e5", "#383d41");
        underSection.getChildren().add(underTable);

        content.getChildren().addAll(title, subtitle, searchField, statsBox, summaryPanel, normalSection, riskySection, underSection);        

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        
        // FIX: Clean, safe background styling that does not break the layout engine
        scrollPane.setStyle("-fx-background: #0B192C; -fx-background-color: transparent; -fx-viewport-background: transparent; -fx-border-color: transparent;");

        root.setCenter(scrollPane);
        dashboardScene = new Scene(root, 1100, 800, Color.web("#0B192C"));
        return dashboardScene;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(18));
        card.setMinWidth(220);
        card.setStyle("-fx-background-color:white; -fx-background-radius:12; -fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.15),8,0,0,3);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#6c757d;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size:30px; -fx-font-weight:bold; -fx-text-fill:" + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox buildSection(String title, String bgColor, String textColor) {
        VBox box = new VBox();
        Label header = new Label(title);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-background-color:" + bgColor + "; -fx-text-fill:" + textColor + "; -fx-font-weight:bold; -fx-padding:12; -fx-font-size:14px;");
        box.getChildren().add(header);
        return box;
    }

    private TableView<Athlete> buildTable() {
        TableView<Athlete> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(40);
        table.prefHeightProperty().bind(
            javafx.beans.binding.Bindings.size(table.getItems())
            .multiply(table.getFixedCellSize())
            .add(35)
        );

        table.setStyle("-fx-background-color:white; -fx-border-color:#E0E0E0;");

        TableColumn<Athlete, String> idCol = new TableColumn<>("Athlete ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(140);

        TableColumn<Athlete, String> nameCol = new TableColumn<>("Athlete Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        TableColumn<Athlete, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(120);

        TableColumn<Athlete, String> statusCol = new TableColumn<>("Status / Action");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(250);
        
        statusCol.setCellFactory(col -> new TableCell<Athlete, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if(empty || status == null){
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox box = new HBox(8);
                    box.setAlignment(Pos.CENTER_LEFT);

                    Circle indicator = new Circle(5);
                    Label text = new Label(status);

                    if(status.contains("High Risk")){
                        indicator.setFill(Color.RED);
                        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.8), indicator);
                        pulse.setByX(0.4); pulse.setByY(0.4);
                        pulse.setCycleCount(Animation.INDEFINITE);
                        pulse.setAutoReverse(true);
                        pulse.play();
                    } else if(status.contains("Stable") || status.contains("Consistent")){
                        indicator.setFill(Color.GREEN);
                    } else {
                        indicator.setFill(Color.ORANGE);
                    }

                    Button actionBtn = new Button("View");
                    actionBtn.setStyle("-fx-background-color: #0c3253; -fx-text-fill: white; -fx-font-size: 11px; -fx-cursor: hand;");
                    actionBtn.setOnAction(event -> {
                        Athlete clicked = getTableView().getItems().get(getIndex());
                        primaryStage.setScene(getScheduleScene(clicked));
                    });

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    box.getChildren().addAll(indicator, text, spacer, actionBtn);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, nameCol, ageCol, statusCol);

        table.setRowFactory(tv -> {
            TableRow<Athlete> row = new TableRow<>();
            row.setOnMouseEntered(e -> {
                if(!row.isEmpty()) row.setStyle("-fx-background-color:#f0f8ff; -fx-cursor:hand;");
            });
            row.setOnMouseExited(e -> {
                if(!row.isEmpty()) row.setStyle("");
            });
            row.setOnMouseClicked(e -> {
                if(e.getClickCount() == 2 && !row.isEmpty()){
                    Athlete clicked = row.getItem();
                    primaryStage.setScene(getScheduleScene(clicked));
                }
            });
            return row;
        });

        return table;
    }

    // =====================================================
    // SCHEDULE PAGE
    // =====================================================
    public Scene getScheduleScene(Athlete athlete) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color:#0B192C;");
        layout.setTop(createTopHeader());

        VBox centerContainer = new VBox(25);
        centerContainer.setPadding(new Insets(30));
        
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:white; -fx-font-size:14px; -fx-font-weight:bold; -fx-cursor:hand;");
        backBtn.setOnAction(e -> primaryStage.setScene(dashboardScene));

        Label header = new Label("Athlete Schedule & Availability");
        header.setStyle("-fx-text-fill:white; -fx-font-size:26px; -fx-font-weight:bold;");

        HBox profileCard = new HBox(20);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setPadding(new Insets(20));
        profileCard.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-border-radius:10;");

        Circle avatarCircle = new Circle(30, Color.web("#0c3253"));
        Label initials = new Label(athlete.getName().isEmpty() ? "A" : athlete.getName().substring(0,1));
        initials.setStyle("-fx-text-fill:white; -fx-font-size:22px; -fx-font-weight:bold;");

        StackPane avatarPane = new StackPane(avatarCircle, initials);

        VBox details = new VBox(5);
        Label nameLabel = new Label(athlete.getName());
        nameLabel.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");

        Label infoLabel = new Label("ID: " + athlete.getId() + " | Age: " + athlete.getAge() + " | Status: " + athlete.getStatus());
        infoLabel.setStyle("-fx-text-fill:#6c757d;");

        details.getChildren().addAll(nameLabel, infoLabel);
        profileCard.getChildren().addAll(avatarPane, details);

        HBox contentSplit = new HBox(40);
        VBox calendarSide = new VBox(15);
        calendarSide.setMinWidth(550);

        Label calendarTitle = new Label("Current Month Schedule");
        calendarTitle.setStyle("-fx-text-fill:white; -fx-font-size:18px; -fx-font-weight:bold;");

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(5); calendarGrid.setVgap(5);
        calendarGrid.setStyle("-fx-background-color:white; -fx-padding:15; -fx-background-radius:10;");

        for(int i = 0; i < 7; i++){
            ColumnConstraints cc = new ColumnConstraints(70);
            cc.setMinWidth(70);
            calendarGrid.getColumnConstraints().add(cc);
        }

        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for(int i = 0; i < 7; i++){
            Label dayHeader = new Label(days[i]);
            dayHeader.setStyle("-fx-font-weight:bold; -fx-text-fill:#6c757d; -fx-padding:5;");
            dayHeader.setAlignment(Pos.CENTER);
            calendarGrid.add(dayHeader, i, 0);
        }

        Label selectedDateLabel = new Label("Please select a date from the calendar");
        selectedDateLabel.setStyle("-fx-text-fill:#B8C1CC; -fx-font-style:italic;");

        VBox[] dayCells = new VBox[32];
        int[] selectedDay = {-1};
        int dayCount = 1;

        for(int row = 1; row <= 5; row++){
            for(int col = 0; col < 7; col++){
                if(dayCount > 31) break;

                VBox dayCell = new VBox(5);
                dayCell.setPrefSize(70, 70);
                dayCell.setStyle("-fx-border-color:#E0E0E0; -fx-background-color:#FCFCFC; -fx-padding:5; -fx-cursor:hand;");
                dayCells[dayCount] = dayCell;

                Label dayNum = new Label(String.valueOf(dayCount));
                dayNum.setStyle("-fx-font-weight:bold;");
                dayCell.getChildren().add(dayNum);

                if(dayCount == 12 || dayCount == 18 || dayCount == 24){
                    Label badge = new Label("Training");
                    badge.setStyle("-fx-background-color:#CCE5FF; -fx-text-fill:#004085; -fx-font-size:10px; -fx-padding:2 4; -fx-background-radius:3;");
                    dayCell.getChildren().add(badge);
                }

                final int currentDay = dayCount;
                dayCell.setOnMouseClicked(e -> {
                    selectedDay[0] = currentDay;
                    selectedDateLabel.setText("Scheduling session for Day " + currentDay);
                    selectedDateLabel.setStyle("-fx-text-fill:#28A745; -fx-font-weight:bold;");
                });

                dayCell.setOnMouseEntered(e -> dayCell.setStyle("-fx-border-color:#0c3253; -fx-background-color:#F0F8FF; -fx-padding:5; -fx-cursor:hand;"));
                dayCell.setOnMouseExited(e -> dayCell.setStyle("-fx-border-color:#E0E0E0; -fx-background-color:#FCFCFC; -fx-padding:5;"));

                calendarGrid.add(dayCell, col, row);
                dayCount++;
            }
        }

        calendarSide.getChildren().addAll(calendarTitle, calendarGrid);

        VBox formSide = new VBox(15);
        formSide.setMinWidth(350);
        formSide.setPadding(new Insets(25));
        formSide.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label formTitle = new Label("Add New Session");
        formTitle.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
        
        ComboBox<String> timeBox = new ComboBox<>();
        timeBox.getItems().addAll("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM", "03:00 PM");
        timeBox.setPromptText("Select Start Time");
        timeBox.setPrefWidth(Double.MAX_VALUE);

        ComboBox<String> durationBox = new ComboBox<>();
        durationBox.getItems().addAll("30 Minutes", "1 Hour", "1.5 Hours", "2 Hours");
        durationBox.setPromptText("Select Duration");
        durationBox.setPrefWidth(Double.MAX_VALUE);

        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);

        Button saveBtn = new Button("Save Schedule");
        saveBtn.setStyle("-fx-background-color:#28A745; -fx-text-fill:white; -fx-font-weight:bold; -fx-padding:10 20; -fx-background-radius:5; -fx-cursor:hand;");

        saveBtn.setOnAction(e -> {
            if(selectedDay[0] == -1){
                selectedDateLabel.setText("Please select a date first.");
                selectedDateLabel.setStyle("-fx-text-fill:#DC3545; -fx-font-weight:bold;");
                return;
            }
            if(timeBox.getValue() == null || durationBox.getValue() == null){
                selectedDateLabel.setText("Please select both Time and Duration.");
                selectedDateLabel.setStyle("-fx-text-fill:#DC3545; -fx-font-weight:bold;");
                return;
            }

            Label sessionBadge = new Label(timeBox.getValue());
            sessionBadge.setStyle("-fx-background-color:#D4EDDA; -fx-text-fill:#155724; -fx-font-size:10px; -fx-padding:2 4; -fx-background-radius:3;");

            dayCells[selectedDay[0]].getChildren().add(sessionBadge);

            selectedDateLabel.setText("Session saved successfully on Day " + selectedDay[0]);
            selectedDateLabel.setStyle("-fx-text-fill:#28A745; -fx-font-weight:bold;");

            selectedDay[0] = -1;
            timeBox.setValue(null); durationBox.setValue(null); notesArea.clear();
        });

        formSide.getChildren().addAll(formTitle, selectedDateLabel, new Label("Start Time"), timeBox, new Label("Duration"), durationBox, new Label("Session Notes"), notesArea, saveBtn);
        contentSplit.getChildren().addAll(calendarSide, formSide);
        centerContainer.getChildren().addAll(backBtn, header, profileCard, contentSplit);

        ScrollPane scrollPane = new ScrollPane(centerContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #0B192C; -fx-background-color: transparent; -fx-viewport-background: transparent; -fx-border-color: transparent;");

        layout.setCenter(scrollPane);
        return new Scene(layout, 1100, 800, Color.web("#0B192C"));
    }

    // =====================================================
    // FILE I/O METHODS
    // =====================================================
    private List<Athlete> loadAthletesFromFile() {
        List<Athlete> athletes = new ArrayList<>();
        try {
            File file = new File("athletes.txt");
            boolean needsDummyData = false;

            // Check if file exists, if not create it
            if (!file.exists()) {
                file.createNewFile();
                needsDummyData = true;
            } else {
                // Read existing data
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) continue; // Skip empty lines safely
                    
                    String[] data = line.split(",");
                    if (data.length == 4) {
                        athletes.add(new Athlete(data[0].trim(), data[1].trim(), Integer.parseInt(data[2].trim()), data[3].trim()));
                    }
                }
                scanner.close();
                
                // If the file exists but has no data inside it
                if (athletes.isEmpty()) {
                    needsDummyData = true;
                }
            }

            // AUTO-GENERATE dummy data for testing if no athletes were found
            if (needsDummyData) {
                athletes.add(new Athlete("ID-001", "Usain Bolt", 24, "Stable"));
                athletes.add(new Athlete("ID-002", "Eliud Kipchoge", 28, "High Risk"));
                athletes.add(new Athlete("ID-003", "Mo Farah", 22, "Underperforming"));
                athletes.add(new Athlete("ID-004", "Florence Griffith", 25, "Consistent"));
                
                // Save these dummy athletes to the text file so they stay there
                for (Athlete dummy : athletes) {
                    saveAthleteToFile(dummy);
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return athletes;
    }

    public void saveAthleteToFile(Athlete newAthlete) {
        try {
            FileWriter fileWriter = new FileWriter("athletes.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(newAthlete.getId() + "," + newAthlete.getName() + "," + newAthlete.getAge() + "," + newAthlete.getStatus());
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    // =====================================================
    // ATHLETE MODEL (FIXED: CHANGED TO PUBLIC FOR TABLEVIEW)
    // =====================================================
    public static class Athlete {
        private final String id;
        private final String name;
        private final int age;
        private final String status;

        public Athlete(String id, String name, int age, String status) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.status = status;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getStatus() { return status; }
    }
}