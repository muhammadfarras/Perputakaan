package org.example;

import com.digitalpersona.uareu.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controller.MyController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Formatter;

public class Absen extends Application implements Reader.CaptureCallback {

    private Stage parentStage;
    private Reader reader;
    private MyController myController;
    private Fmd myFmd;


    private List<MysqlDB.Record> myListOfRecord = new ArrayList<>();
    private List<Fmd> myListOfFmds = new ArrayList<>();
    private Fmd [] myFmdArray = null;

    MysqlDB mysqlDB = new MysqlDB();

    @FXML
    private Text textStatus;

    public Absen(){
        this.parentStage = App.parentStage;
        this.reader = App.reader;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        load fxml
        myController = new MyController(App.parentStage);
        myController.show();

        // Get All Data
        this.myListOfRecord = mysqlDB.getAllFPData();
        //lets create array
        for (MysqlDB.Record record : this.myListOfRecord){
            System.out.println(record.personName);
            Fmd fmd = UareUGlobal.GetImporter().ImportFmd(record.fmdBinary, Fmd.Format.DP_REG_FEATURES, Fmd.Format.DP_REG_FEATURES);
            myListOfFmds.add(fmd);
        }
        myFmdArray = new Fmd[myListOfFmds.size()];
        myListOfFmds.toArray(myFmdArray);
        System.out.println("Banyaknya data array: "+myFmdArray.length);



//        open reader if reader not open
        if (!App.isOpen){
            reader.Open(Reader.Priority.COOPERATIVE);
            App.isOpen = true;
            caputreAbsen();
        }
        else {
            reader.CancelCapture();
            reader.Close();
            App.isOpen = false;

            reader.Open(Reader.Priority.EXCLUSIVE);
            caputreAbsen();
        }


    }

    public static void main(String[] args) {
        launch();
    }

    private void caputreAbsen () throws UareUException {

        if (reader.GetStatus().status == Reader.ReaderStatus.READY){
            reader.CaptureAsync(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1,this);
        }
    }


//    hasil dari capture absen
    @Override
    @FXML
    public void CaptureResultEvent(Reader.CaptureResult captureResult) {

        if (myFmdArray.length != 0){
            Engine engine = UareUGlobal.GetEngine();
            try {

//            System.out.println(engine.CreateFmd(captureResult.image, Fmd.Format.DP_VER_FEATURES));
                myFmd = engine.CreateFmd(captureResult.image, Fmd.Format.DP_VER_FEATURES);
                //lets identify
                //Sets target false match rate
                int targetFalseMatchRate = Engine.PROBABILITY_ONE/1000000;

                Engine.Candidate [] candidates = engine.Identify(myFmd,0,myFmdArray,targetFalseMatchRate,1);

                if (candidates.length ==1){
                    myController.updateStatusAfterTappedFingerPrint(myListOfRecord.get(candidates[0].fmd_index).personName);
                }
                else {
                    myController.updateStatusAfterTappedFingerPrint("Person not found");
                }




                caputreAbsen();
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
        else {
            myController.updateStatusAfterTappedFingerPrint("Belum ada data yang tersedia didalam databse");
        }

    }
}
