package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("24");
        stage.setScene(scene);
        stage.show();
        System.out.println(stage.getX()+","+stage.getY());
    }

    public static void main(String[] args) {
        launch();
    }
}