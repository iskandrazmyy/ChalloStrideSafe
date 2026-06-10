/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.mavenproject1;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author amirul
 */
public class ScheduleTest extends Application {

    private final String currentAthleteName = "Amirul";

    private final List<Schedule> scheduleList = new ArrayList<>();

    private final GridPane calendarGrid = new GridPane();

    private final Label monthLabel = new Label();

    private YearMonth currentMonth = YearMonth.of(2026, 6);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        addSampleSchedules();

        // ===== Top-left title =====
        Label titleLabel = new Label("Athlete Schedule");

        titleLabel.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
        );

        // ===== Top-right logged-in user =====
        Label nameLabel = new Label(
                "Logged in as: " + currentAthleteName
        );

        nameLabel.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
        );

        // Places title on left and username on right
        BorderPane header = new BorderPane();

        header.setLeft(titleLabel);
        header.setRight(nameLabel);

        BorderPane.setAlignment(
                titleLabel,
                Pos.CENTER_LEFT
        );

        BorderPane.setAlignment(
                nameLabel,
                Pos.CENTER_RIGHT
        );

        // ===== Month navigation =====
        Button previousButton = new Button("<");
        Button nextButton = new Button(">");

        previousButton.setStyle(
                createNavigationButtonStyle()
        );

        nextButton.setStyle(
                createNavigationButtonStyle()
        );

        monthLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
        );

        HBox navigationBox = new HBox(
                20,
                previousButton,
                monthLabel,
                nextButton
        );

        navigationBox.setAlignment(Pos.CENTER);

        VBox topSection = new VBox(
                20,
                header,
                navigationBox
        );

        topSection.setPadding(new Insets(20));

        // Previous month button
        previousButton.setOnAction(e -> {
            currentMonth = currentMonth.minusMonths(1);
            createCalendar();
        });

        // Next month button
        nextButton.setOnAction(e -> {
            currentMonth = currentMonth.plusMonths(1);
            createCalendar();
        });

        // ===== Calendar layout =====
        calendarGrid.setHgap(1);
        calendarGrid.setVgap(1);
        calendarGrid.setPadding(new Insets(10));

        calendarGrid.setStyle(
                "-fx-background-color: #D8E0E8;"
        );

        createCalendar();

        ScrollPane scrollPane = new ScrollPane(calendarGrid);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // ===== Main layout =====
        BorderPane root = new BorderPane();

        root.setTop(topSection);
        root.setCenter(scrollPane);

        root.setStyle(
                "-fx-background-color: #0B3556;"
        );

        Scene scene = new Scene(root, 1200, 720);

        stage.setTitle("Athlete Schedule");
        stage.setScene(scene);
        stage.show();
    }

    private void createCalendar() {

        calendarGrid.getChildren().clear();

        DateTimeFormatter monthFormatter =
                DateTimeFormatter.ofPattern("MMMM yyyy");

        monthLabel.setText(
                currentMonth.format(monthFormatter)
        );

        String[] dayNames = {
            "Mon", "Tue", "Wed", "Thu",
            "Fri", "Sat", "Sun"
        };

        // Create day headings
        for (int column = 0; column < 7; column++) {

            Label dayLabel = new Label(
                    dayNames[column]
            );

            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setPrefSize(165, 55);

            dayLabel.setStyle(
                    "-fx-background-color: #E9EFF5;"
                    + "-fx-font-size: 16px;"
                    + "-fx-font-weight: bold;"
                    + "-fx-text-fill: #263445;"
            );

            GridPane.setHgrow(
                    dayLabel,
                    Priority.ALWAYS
            );

            calendarGrid.add(
                    dayLabel,
                    column,
                    0
            );
        }

        LocalDate firstDate =
                currentMonth.atDay(1);

        int firstColumn =
                firstDate.getDayOfWeek().getValue() - 1;

        int totalDays =
                currentMonth.lengthOfMonth();

        // Create 42 calendar boxes
        for (int position = 0; position < 42; position++) {

            int column = position % 7;
            int row = (position / 7) + 1;

            if (position >= firstColumn
                    && position < firstColumn + totalDays) {

                int dayNumber =
                        position - firstColumn + 1;

                LocalDate date =
                        currentMonth.atDay(dayNumber);

                VBox dateCell =
                        createDateCell(date);

                calendarGrid.add(
                        dateCell,
                        column,
                        row
                );

            } else {

                VBox emptyCell = new VBox();

                emptyCell.setPrefSize(165, 105);

                emptyCell.setStyle(
                        "-fx-background-color: #F5F7FA;"
                        + "-fx-border-color: #D8E0E8;"
                        + "-fx-border-width: 0.5;"
                );

                calendarGrid.add(
                        emptyCell,
                        column,
                        row
                );
            }
        }
    }

    private VBox createDateCell(LocalDate date) {

        VBox dateCell = new VBox(6);

        dateCell.setPadding(new Insets(8));
        dateCell.setPrefSize(165, 105);

        dateCell.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #D8E0E8;"
                + "-fx-border-width: 0.5;"
        );

        Label dateNumber = new Label(
                String.valueOf(date.getDayOfMonth())
        );

        dateNumber.setStyle(
                "-fx-font-size: 16px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #263445;"
        );

        dateCell.getChildren().add(dateNumber);

        List<Schedule> schedulesForDate =
                getSchedulesForDate(date);

        for (Schedule schedule : schedulesForDate) {

            Label trainingLabel = new Label(
                    "Scheduled Training\n"
                    + formatTime(schedule.getTime())
            );

            trainingLabel.setWrapText(true);
            trainingLabel.setMaxWidth(Double.MAX_VALUE);

            trainingLabel.setStyle(
                    "-fx-background-color: #EFF6FF;"
                    + "-fx-border-color: #B8D2ED;"
                    + "-fx-border-radius: 4;"
                    + "-fx-background-radius: 4;"
                    + "-fx-padding: 5;"
                    + "-fx-font-size: 11px;"
                    + "-fx-font-weight: bold;"
                    + "-fx-text-fill: #0098B8;"
                    + "-fx-cursor: hand;"
            );

            // Click training to view details
            trainingLabel.setOnMouseClicked(e -> {
                showScheduleDetails(schedule);
            });

            dateCell.getChildren().add(
                    trainingLabel
            );
        }

        return dateCell;
    }

    private List<Schedule> getSchedulesForDate(
            LocalDate selectedDate) {

        List<Schedule> results =
                new ArrayList<>();

        for (Schedule schedule : scheduleList) {

            boolean sameAthlete =
                    schedule.getAthleteName()
                            .equalsIgnoreCase(
                                    currentAthleteName
                            );

            boolean sameDate =
                    schedule.getDate()
                            .equals(selectedDate);

            if (sameAthlete && sameDate) {
                results.add(schedule);
            }
        }

        return results;
    }

    private void showScheduleDetails(
            Schedule schedule) {

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy"
                );

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern(
                        "h:mm a"
                );

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle("Training Details");

        alert.setHeaderText(
                "Scheduled Training"
        );

        alert.setContentText(
                "Date: "
                + schedule.getDate()
                        .format(dateFormatter)
                + "\n"
                + "Time: "
                + schedule.getTime()
                        .format(timeFormatter)
                + "\n"
                + "Coach: "
                + schedule.getCoach()
        );

        alert.showAndWait();
    }

    private String formatTime(LocalTime time) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "h:mm a"
                );

        return time.format(formatter);
    }

    private void addSampleSchedules() {

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 2),
                LocalTime.of(8, 0),
                "Coach Ahmad"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 4),
                LocalTime.of(16, 0),
                "Coach Hakim"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 9),
                LocalTime.of(7, 30),
                "Coach Sarah"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(10, 0),
                "Coach Ahmad"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 12),
                LocalTime.of(14, 0),
                "Coach Hakim"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 15),
                LocalTime.of(8, 30),
                "Coach Sarah"
        ));

        scheduleList.add(new Schedule(
                "Amirul",
                LocalDate.of(2026, 6, 18),
                LocalTime.of(16, 30),
                "Coach Ahmad"
        ));

        // Another athlete's schedule.
        // This will not appear for Amirul.
        scheduleList.add(new Schedule(
                "Ali",
                LocalDate.of(2026, 6, 10),
                LocalTime.of(12, 0),
                "Coach Hakim"
        ));
    }

    private String createNavigationButtonStyle() {

        return "-fx-background-color: white;"
                + "-fx-text-fill: #0B3556;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-border-color: #90A4B8;"
                + "-fx-border-radius: 4;"
                + "-fx-background-radius: 4;";
    }
}