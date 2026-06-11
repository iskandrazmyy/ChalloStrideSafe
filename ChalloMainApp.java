package challoapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChalloMainApp extends Application {

    private static Stage primaryStage;
    private static String currentUser;
    private static String currentUserType;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Challo App");
        primaryStage.setFullScreen(false); // Can be changed based on preference
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        
        // Initialize static instance for LoginTest
        LoginTest.init(this);

        showUserTypeSelection();
        
        primaryStage.show();
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showUserTypeSelection() {
        primaryStage.setScene(LoginTest.getUserTypeSelectionScene());
    }

    public void showLoginScreen() {
        primaryStage.setScene(LoginTest.getLoginScene());
    }

    public void showMainPage(String username, String userType) {
        currentUser = username;
        currentUserType = userType;
        primaryStage.setScene(LoginTest.getMainPageScene(username, userType));
    }
    
    public void showDashboard() {
        primaryStage.setScene(LoginTest.getDashboardScene(currentUser, currentUserType));
    }
    
    public void showRunLog() {
        primaryStage.setScene(RunLogDashboard.getScene(this));
    }

    public void showSchedule() {
        primaryStage.setScene(ScheduleTest.getScene(this, currentUser));
    }

    public void showBookingSlot() {
        primaryStage.setScene(BookingSlotUI.getScene(this));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
