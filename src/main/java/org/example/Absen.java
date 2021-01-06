package org.example;

import com.digitalpersona.uareu.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.controller.MyController;
import org.example.warning.WarningPopUp;

import java.util.ArrayList;
import java.util.List;


public class Absen extends Application implements Reader.CaptureCallback {

    private Stage parentStage;
    private Reader reader;
    private MyController myController;
    private Fmd myFmd;
    private final WarningPopUp warningPopUp = new WarningPopUp();

    private SoundNotif soundNotifSukses = new SoundNotif("sound/berhasil.mp3");
    private SoundNotif soundNotifGagal = new SoundNotif("sound/gagal.mp3");


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
    public void start(Stage primaryStage) {

//        load fxml
        myController = new MyController(App.parentStage);
        myController.show();

        try {
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
        }
        catch (Exception e){
            warningPopUp.informationCostume("Kesalahan","Tidak dapat terhubung ke internet");
            // Close stage
            this.myController.closeStageCauseException();
        }



        try {

            if (null != reader){
                //        open reader if reader not open
                if (!App.isOpen){
                    reader.Open(Reader.Priority.COOPERATIVE);
                    App.isOpen = true;
                }
                else {
                    reader.CancelCapture();
                    reader.Close();
                    App.isOpen = false;

                    reader.Open(Reader.Priority.EXCLUSIVE);
                }
                caputreAbsen();
            }

        }
        catch (UareUException e){

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
                    myController.updateLattestPersonAfterTappedFingerPrint(myListOfRecord.get(candidates[0].fmd_index).personName);
                    myController.updateStatusAfterTappedFingerPrint("Selamat Datang "+myListOfRecord.get(candidates[0].fmd_index).personName);
                    soundNotifSukses.turnOn();
                }
                else {
                    myController.updateStatusAfterTappedFingerPrint("Person not found");
                    soundNotifGagal.turnOn();
                }




                caputreAbsen();
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
        else {
            myController.updateLattestPersonAfterTappedFingerPrint("Belum ada data yang tersedia didalam databse");
        }

    }
}
