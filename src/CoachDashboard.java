import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CoachDashboard {

    private Stage primaryStage;
    private Scene dashboardScene;

    public CoachDashboard(Stage stage) {
        this.primaryStage = stage;
    }
    
    public Scene getDashboardScene() {
        BorderPane root = new BorderPane();

        VBox sidebar = new VBox(30);
        sidebar.setStyle("-fx-background-color: #0B192C; -fx-padding: 20;");
        sidebar.setPrefWidth(200);

        Label brand = new Label("StrideSafe");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label teamNav = new Label("👥 Team");
        teamNav.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        Label reportsNav = new Label("📊 Reports");
        reportsNav.setStyle("-fx-text-fill: #a8b2c1; -fx-font-size: 14px;");
        Label alertsNav = new Label("🔔 Alerts");
        alertsNav.setStyle("-fx-text-fill: #a8b2c1; -fx-font-size: 14px;");

        sidebar.getChildren().addAll(brand, teamNav, reportsNav, alertsNav);

        VBox content = new VBox(20);
        content.setPadding(new Insets(20, 30, 20, 30));
        content.setStyle("-fx-background-color: #f4f7f6;");

        Label title = new Label("COACH DASHBOARD");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0B192C;");

        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
            createStatCard("Total Athletes", "4", "#0B192C"),
            createStatCard("High Risk", "1", "#dc3545"),
            createStatCard("Stable/Consistent", "2", "#28a745")
        );

        VBox normalSection = buildSection("Normal Performance", "#d4edda", "#155724");
        TableView<Athlete> normalTable = buildTable();
        normalTable.getItems().addAll(
            new Athlete("A31", "Sarah Chen", 21, "Stable"),
            new Athlete("A46", "Mike Johnson", 22, "Consistent")
        );
        normalSection.getChildren().add(normalTable);

        VBox riskySection = buildSection("Risky (Critical Attention) ⚠", "#fff3cd", "#856404");
        TableView<Athlete> riskyTable = buildTable();
        riskyTable.getItems().add(new Athlete("A52", "Ben Adams", 19, "High Risk"));
        riskySection.getChildren().add(riskyTable);

        VBox underSection = buildSection("Underperformance / Inactive", "#e2e3e5", "#383d41");
        TableView<Athlete> underTable = buildTable();
        underTable.getItems().add(new Athlete("A28", "Maria Garcia", 23, "Below Threshold"));
        underSection.getChildren().add(underTable);

        content.getChildren().addAll(title, statsBox, normalSection, riskySection, underSection);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #ffffff;");

        root.setLeft(sidebar);
        root.setCenter(scrollPane);

        dashboardScene = new Scene(root, 1000, 750);
        return dashboardScene;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setMinWidth(200);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-font-weight: bold;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox buildSection(String title, String bgColor, String textColor) {
        VBox box = new VBox();
        box.setSpacing(0);
        Label header = new Label(title);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 10; -fx-font-weight: bold;");
        box.getChildren().add(header);
        return box;
    }

    private TableView<Athlete> buildTable() {
        TableView<Athlete> table = new TableView<>();
        table.setPrefHeight(130);
        table.setStyle("-fx-border-color: #e0e0e0;");

        TableColumn<Athlete, String> idCol = new TableColumn<>("Ath ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(120);

        TableColumn<Athlete, String> nameCol = new TableColumn<>("Ath Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        TableColumn<Athlete, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(120);

        TableColumn<Athlete, String> statusCol = new TableColumn<>("Status / Action");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(200);

        statusCol.setCellFactory(col -> new TableCell<Athlete, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox box = new HBox(8);
                    box.setAlignment(Pos.CENTER_LEFT);
                    Circle indicator = new Circle(5);
                    Label text = new Label(status);

                    if (status.contains("High Risk")) {
                        indicator.setFill(Color.RED);
                        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.8), indicator);
                        pulse.setByX(0.4);
                        pulse.setByY(0.4);
                        pulse.setCycleCount(Animation.INDEFINITE);
                        pulse.setAutoReverse(true);
                        pulse.play();
                    } else if (status.contains("Stable") || status.contains("Consistent")) {
                        indicator.setFill(Color.GREEN);
                    } else {
                        indicator.setFill(Color.ORANGE);
                    }
                    box.getChildren().addAll(indicator, text);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().add(idCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(ageCol);
        table.getColumns().add(statusCol);

        table.setRowFactory(tv -> {
            TableRow<Athlete> row = new TableRow<>();
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) row.setStyle("-fx-background-color: #f0f8ff; -fx-cursor: hand;");
            });
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) row.setStyle("");
            });
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Athlete clicked = row.getItem();
                    primaryStage.setScene(getScheduleScene(clicked));
                }
            });
            return row;
        });

        return table;
    }
    //jangan lupa to make strict time
    public Scene getScheduleScene(Athlete athlete) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f4f7f6;");

        VBox topBox = new VBox(10);
        topBox.setPadding(new Insets(20));
        topBox.setStyle("-fx-background-color: #0B192C;");
        
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        backBtn.setOnAction(e -> primaryStage.setScene(dashboardScene));
        
        Label header = new Label("Athlete Schedule & Availability");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        topBox.getChildren().addAll(backBtn, header);
        layout.setTop(topBox);

        VBox centerContainer = new VBox(25);
        centerContainer.setPadding(new Insets(30));

        HBox profileCard = new HBox(20);
        profileCard.setAlignment(Pos.CENTER_LEFT);
        profileCard.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");
        
        Circle avatar = new Circle(30, Color.web("#0B192C"));
        Label initials = new Label(athlete.getName().substring(0, 1));
        initials.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        StackPane avatarPane = new StackPane(avatar, initials);

        VBox profileDetails = new VBox(5);
        Label nameLabel = new Label(athlete.getName());
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label infoLabel = new Label("ID: " + athlete.getId() + "  |  Age: " + athlete.getAge() + "  |  Status: " + athlete.getStatus());
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d;");
        profileDetails.getChildren().addAll(nameLabel, infoLabel);
        
        profileCard.getChildren().addAll(avatarPane, profileDetails);

        HBox contentSplit = new HBox(40);

        VBox calSide = new VBox(15);
        calSide.setMinWidth(550); 
        
        Label calLabel = new Label("Current Month Schedule");
        calLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        
        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");
        
        for (int i = 0; i < 7; i++) {
            ColumnConstraints colConst = new ColumnConstraints(70);
            colConst.setMinWidth(70);
            calendarGrid.getColumnConstraints().add(colConst);
        }

        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            Label dayHeader = new Label(daysOfWeek[i]);
            dayHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #6c757d; -fx-padding: 5;");
            dayHeader.setAlignment(Pos.CENTER);
            calendarGrid.add(dayHeader, i, 0);
        }

        Label selectedDateLabel = new Label("Please select a date from the calendar");
        selectedDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-font-style: italic;");

        VBox[] dayCells = new VBox[32];
        int[] currentSelectedDay = {-1}; 

        int dayCount = 1;
        for (int row = 1; row <= 5; row++) {
            for (int col = 0; col < 7; col++) {
                if (dayCount > 31) break;
                
                VBox dayCell = new VBox(5);
                dayCell.setPrefSize(70, 70);
                dayCell.setStyle("-fx-border-color: #e0e0e0; -fx-background-color: #fcfcfc; -fx-padding: 5; -fx-cursor: hand;");
                dayCells[dayCount] = dayCell;
                
                Label dayNum = new Label(String.valueOf(dayCount));
                dayNum.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                dayCell.getChildren().add(dayNum);

                if (dayCount == 12 || dayCount == 18) {
                    Label badge = new Label("Training");
                    badge.setStyle("-fx-background-color: #cce5ff; -fx-text-fill: #004085; -fx-font-size: 10px; -fx-padding: 2 4; -fx-background-radius: 3;");
                    dayCell.getChildren().add(badge);
                }

                final int currentDay = dayCount;
                dayCell.setOnMouseClicked(e -> {
                    currentSelectedDay[0] = currentDay;
                    selectedDateLabel.setText("Scheduling session for: Day " + currentDay);
                    selectedDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
                });

                dayCell.setOnMouseEntered(e -> dayCell.setStyle("-fx-border-color: #0B192C; -fx-background-color: #f0f8ff; -fx-padding: 5; -fx-cursor: hand;"));
                dayCell.setOnMouseExited(e -> dayCell.setStyle("-fx-border-color: #e0e0e0; -fx-background-color: #fcfcfc; -fx-padding: 5;"));

                calendarGrid.add(dayCell, col, row);
                dayCount++;
            }
        }
        calSide.getChildren().addAll(calLabel, calendarGrid);

        VBox formSide = new VBox(15);
        formSide.setStyle("-fx-background-color: white; -fx-padding: 25; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10;");
        formSide.setMinWidth(350);
        
        Label formLabel = new Label("Add New Session");
        formLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        ComboBox<String> timeBox = new ComboBox<>();
        timeBox.getItems().addAll("08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "01:00 PM", "02:00 PM", "03:00 PM");
        timeBox.setPromptText("Select Start Time");
        timeBox.setPrefWidth(Double.MAX_VALUE);

        ComboBox<String> durationBox = new ComboBox<>();
        durationBox.getItems().addAll("30 Minutes", "1 Hour", "1.5 Hours", "2 Hours");
        durationBox.setPromptText("Select Duration");
        durationBox.setPrefWidth(Double.MAX_VALUE);

        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(3);
        notesArea.setWrapText(true);

        Button saveBtn = new Button("Save Schedule");
        saveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
        
        saveBtn.setOnAction(e -> {
            if (currentSelectedDay[0] == -1) {
                selectedDateLabel.setText("Error: Please click a date on the calendar first.");
                selectedDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #dc3545; -fx-font-weight: bold;");
                return;
            }
            if (timeBox.getValue() == null || durationBox.getValue() == null) {
                selectedDateLabel.setText("Error: Please select both a Time and Duration.");
                selectedDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #dc3545; -fx-font-weight: bold;");
                return;
            }

            Label newSessionBadge = new Label(timeBox.getValue());
            newSessionBadge.setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-font-size: 10px; -fx-padding: 2 4; -fx-background-radius: 3;");
            
            dayCells[currentSelectedDay[0]].getChildren().add(newSessionBadge);

            selectedDateLabel.setText("Success! Session saved on Day " + currentSelectedDay[0]);
            selectedDateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
            
            currentSelectedDay[0] = -1;
            timeBox.setValue(null);
            durationBox.setValue(null);
            notesArea.clear();
        });

        formSide.getChildren().addAll(
            formLabel, 
            selectedDateLabel,
            new Label("Start Time:"), timeBox, 
            new Label("Duration:"), durationBox, 
            new Label("Notes:"), notesArea, 
            saveBtn
        );

        contentSplit.getChildren().addAll(calSide, formSide);
        centerContainer.getChildren().addAll(profileCard, contentSplit);
        
        ScrollPane centerScroll = new ScrollPane(centerContainer);
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background-color: transparent;");
        
        layout.setCenter(centerScroll);

        return new Scene(layout, 1050, 750);
    }
}