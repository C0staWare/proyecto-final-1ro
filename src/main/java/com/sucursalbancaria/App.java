package com.sucursalbancaria;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class App extends Application {

    private static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load((getClass().getResource("/FXML/MainView.fxml")));

        scene = new Scene(root);

        stage.setScene(scene);

        stage.show();

        stage.setResizable(true);

        stage.setMaximized(true);

        scene.getStylesheets().add(getClass().getResource("/Styles/MainStyle.css").toExternalForm());


    }

    public static void main(String[] args) {

        launch();
    }

}