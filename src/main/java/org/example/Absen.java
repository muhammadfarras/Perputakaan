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
    private Reader myReader;
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
        this.myReader = App.reader;
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
//                System.out.println(record.personName);
                Fmd fmd = UareUGlobal.GetImporter().ImportFmd(record.fmdBinary, Fmd.Format.DP_REG_FEATURES, Fmd.Format.DP_REG_FEATURES);
                myListOfFmds.add(fmd);
            }
            myFmdArray = new Fmd[myListOfFmds.size()];
            myListOfFmds.toArray(myFmdArray);
//            System.out.println("Banyaknya data array: "+myFmdArray.length);
        }
        catch (Exception e){
            warningPopUp.informationCostume("Kesalahan","Tidak dapat terhubung ke internet");
            // Close stage
            this.myController.closeStageCauseException();
        }


        try {
            if (null != myReader){
                //        open reader if reader not open
                System.out.println("Reader is close then open");
                if (!App.isOpen){
                    myReader.Open(Reader.Priority.COOPERATIVE);
                    App.isOpen = true;
                }
                else {
                    System.out.println("Reader opene then is close then open");
                    myReader.CancelCapture();
                    myReader.Close();
                    App.isOpen = false;

                    myReader.Open(Reader.Priority.EXCLUSIVE);
                    App.isOpen = true;
                }
            }
            else
            {
                // isi jika null
                try {
                    System.out.println("Create reader");
                    App.reader = new GetReader().reader;
                    this.myReader = App.reader;

                    myReader.Open(Reader.Priority.EXCLUSIVE);
                    App.isOpen = true;
                    System.out.println("Reader not null anymore "+ (null != myReader));
                }
                catch (UareUException e){
                    warningPopUp.informationCostume("Kesalahan","Reader tidak terbaca, tutup jendela lalu masuk kembali");
                }
            }
            caputreAbsen();
        }
        catch (UareUException e){
//            e.printStackTrace();
        }




    }

    public static void main(String[] args) {
        launch();
    }

    private void caputreAbsen () {
        try {
        if (myReader.GetStatus().status == Reader.ReaderStatus.READY){

                myReader.CaptureAsync(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1,this);
            }
        }
        catch (UareUException e){
            System.out.println("Error then close the stage");
//            e.printStackTrace();
        }
    }


//    hasil dari capture absen
    @Override
    @FXML
    public void CaptureResultEvent(Reader.CaptureResult captureResult) {

        if (myFmdArray.length != 0){
            Engine engine = UareUGlobal.GetEngine();

            //            System.out.println(engine.CreateFmd(captureResult.image, Fmd.Format.DP_VER_FEATURES));
            try {
                myFmd = engine.CreateFmd(captureResult.image, Fmd.Format.DP_VER_FEATURES);
            } catch (Exception e) {
                System.out.println("Close stage");
                myController.closeStageCauseException();
            }

            //lets identify
            //Sets target false match rate
            int targetFalseMatchRate = Engine.PROBABILITY_ONE/1000000;

            Engine.Candidate [] candidates = new Engine.Candidate[0];

            try {
                candidates = engine.Identify(myFmd,0,myFmdArray,targetFalseMatchRate,1);
            } catch (UareUException e) {
//                e.printStackTrace();
            }

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
        }
        else {
            myController.updateLattestPersonAfterTappedFingerPrint("Belum ada data yang tersedia didalam databse");
        }

    }
}
