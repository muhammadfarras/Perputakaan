package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AbsenThread extends Application{

    private Stage parentStage;

    public AbsenThread (){
        this.parentStage = App.parentStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a Font
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout/layout_absen.fxml"));

        Scene scene = new Scene(root,900,825);
        scene.getStylesheets().add(getClass().getClassLoader()
                .getResource("style/font.css").toExternalForm());

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.initOwner(parentStage);
        primaryStage.setTitle("Absen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
