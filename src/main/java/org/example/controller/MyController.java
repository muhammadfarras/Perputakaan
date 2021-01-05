package org.example.controller;

import com.digitalpersona.uareu.UareUException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MyController  {
    @FXML
    private Text statusText;

    private Stage primaryStage;

    public MyController (Stage parentStage) {
        this.primaryStage = new Stage();
        try {


            // Create a Font
            FXMLLoader root = new FXMLLoader(getClass().getClassLoader().getResource("layout/layout_absen.fxml"));

            root.setController(this);

            Scene scene = new Scene(root.load(),900,825);
            scene.getStylesheets().add(getClass().getClassLoader()
                    .getResource("style/font.css").toExternalForm());

            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.initOwner(parentStage);
            primaryStage.setTitle("Absen");
            primaryStage.setScene(scene);

            primaryStage.setOnHiding(
                    event -> {
                        if (App.isOpen){
                            try {
                                App.reader.CancelCapture();
                                App.reader.Close();
                                App.isOpen = false;

                                System.out.println(getClass().getClasses().toString()+" : ditutup stagenya dan diclose readernya");
                            } catch (UareUException e) {
                                e.printStackTrace();
                            }


                        }
                    }
            );
        }
        catch (LoadException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void show (){
        primaryStage.show();
    }

    public void updateStatusAfterTappedFingerPrint(String text){
        this.statusText.setText(text);
    }
}
