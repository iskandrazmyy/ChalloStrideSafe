import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create the dashboard and pass the window (primaryStage) to it
        CoachDashboard dashboard = new CoachDashboard(primaryStage);
        
        primaryStage.setScene(dashboard.getDashboardScene());
        primaryStage.setTitle("StrideSafe - Coach Dashboard");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}