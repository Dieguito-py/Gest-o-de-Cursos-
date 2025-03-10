package senac.senacfx.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import senac.senacfx.controller.MainViewController;

import java.io.IOException;

public class Main extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane scrollPane = loader.load();

            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            scrollPane.setPrefSize(1050, 700);
            mainScene = new Scene(scrollPane);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Gestão de Cursos");
            primaryStage.show();
            primaryStage.setResizable(false);
            MainViewController controller = loader.getController();
            controller.loadInitialView();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static Scene getMainScene(){
        return mainScene;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

