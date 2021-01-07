package org.example.controller;

import com.digitalpersona.uareu.UareUException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.NamedStyle;

import java.awt.*;
import java.io.IOException;


public class MyController  {
    private final NamedStyle namedStyle =new NamedStyle ();

    @FXML
    private Label labelHead;
    @FXML
    private Text statusText;
    @FXML
    private VBox vboxHead;
    @FXML
    private Text statusTextPendataan;
    @FXML
    private VBox vboxBody;
    @FXML
    private StackPane stackPaneAbsen;
    @FXML
    private VBox parentXml;


    private Stage primaryStage;

    public MyController (Stage parentStage) {
        this.primaryStage = new Stage();
        try {


            // Create a Font
            FXMLLoader root = new FXMLLoader(getClass().getClassLoader().getResource("layout/layout_absen.fxml"));

            root.setController(this);

            Scene scene = new Scene(root.load());
            scene.getStylesheets().add(getClass().getClassLoader()
                    .getResource("style/font.css").toExternalForm());
            scene.getStylesheets().add(getClass().getClassLoader().getResource("style/decoration.css").toString());

            /*
            OPEN : SET CLASS EVERY NODE
             */
            this.vboxHead.getStyleClass().add(namedStyle.HEADER);
            this.statusText.setFont(Font.font("Arial", FontWeight.BOLD,14));
            this.statusText.getStyleClass().add(namedStyle.HEADER_TEXT);

            this.labelHead.setUnderline(true);
            this.labelHead.setFont(Font.font("Arial", FontWeight.BOLD,18));

            this.vboxBody.getStyleClass().add(namedStyle.VBOX_BODY_ABSEN);
            System.out.println(vboxBody.getWidth());
            vboxBody.setPrefWidth(parentXml.getPrefWidth());
            vboxBody.setPrefHeight(1800);




            /*
            CLOSE : SET CLASS EVERY NODE
             */

            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.initOwner(parentStage);
            primaryStage.setTitle("Absen");
            primaryStage.setScene(scene);

            primaryStage.setOnHiding(
                    event -> {
                        if (App.isOpen){
                            try {

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

    public void updateLattestPersonAfterTappedFingerPrint(String text){
        this.statusText.setText(text);
    }

    public void updateStatusAfterTappedFingerPrint(String text){
        this.statusTextPendataan.setText(text);
    }

    public void closeStageCauseException (){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.close();
            }
        });

    }
}
