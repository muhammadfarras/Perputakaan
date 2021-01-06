package org.example;

import com.digitalpersona.uareu.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.updategui.UpdateGui;
import org.example.warning.WarningPopUp;

import java.sql.SQLException;

public class EnrollmenThread extends Thread implements Engine.EnrollmentCallback{



    static Reader myReader;
    private String valueInformationText;
    private UpdateGui textGui = new UpdateGui(App.INFORMATION_TEXT);
    private SoundNotif soundNotif = new SoundNotif("sound/true.mp3");
    private SoundNotif soundNotifSukses = new SoundNotif("sound/berhasil.mp3");
    private SoundNotif soundNotifGagal = new SoundNotif("sound/gagal.mp3");
    private ImagePanel imagePanel = new ImagePanel();

    Reader.CaptureResult cr;
    Fmd finalFmd;
//    private static boolean isOpen = false;

    public EnrollmenThread ()  {
        try {
            this.myReader = new GetReader().reader;
        }
        catch (UareUException e){
            e.printStackTrace();
        }

    }
    @Override
    public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
        Engine.PreEnrollmentFmd prefmd = null;

        while (prefmd == null){
            try {
                // Jika di unplug
                if (myReader == null ){
                    break;
                }

                // check reader
                if (myReader.GetStatus().status == Reader.ReaderStatus.BUSY){

                    textGui.appendText("\nMohon tunggu");

                    Thread.sleep(1000);
                }

                if (myReader.GetStatus().status == Reader.ReaderStatus.READY) {
                    // capture asyntask

                    cr = myReader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1);
                }
//                System.out.println(cr.image.getData());
                if (cr.quality == Reader.CaptureQuality.CANCELED){
//                    System.out.println(getClass().toString()+" : "+cr.quality);
                    break;
                }
                else if (null != cr.image && Reader.CaptureQuality.GOOD == cr.quality ){

                    // Get Enggine
                    Engine engine = UareUGlobal.GetEngine();

                    try {

                        // show image

//                        Extarct fitur


                        imagePanel.saveImage(cr.image);
                        imagePanel.showImage(App.IMAGE_VIEW_FINGER);

                        Fmd fmd =engine.CreateFmd(cr.image, Fmd.Format.DP_PRE_REG_FEATURES);
//                        System.out.println(getClass().toString()+" : "+"The fmd "+ fmd);


                        // return prefmd
                        prefmd = new Engine.PreEnrollmentFmd();
                        prefmd.fmd = fmd;
                        prefmd.view_index = 0;

//                        informasi ke GUI
                        textGui.appendText("\n- Masukan jari yang sama.");
                        soundNotif.turnOn();


                    }
                    catch (UareUException e){
                        System.out.println(getClass().toString()+" : "+"Error");
                    }
                }

            } catch (UareUException e) {
//                e.printStackTrace();
                break;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return prefmd;
    }

    @Override
    public void run() {
        try {
            // check apakah sebelumnya udah di open atau belim
            Engine myEngine = UareUGlobal.GetEngine();
            if (!App.isOpen) {
//                Jika tidak dibuka maka buka
                System.out.println("Run here lah");
                imagePanel.clearImage(App.IMAGE_VIEW_FINGER);
                OpenAndReopenReader (myEngine);

            }
            else {
//                Jika kebuka maka harus di cancel capture
//                dan di close
                imagePanel.clearImage(App.IMAGE_VIEW_FINGER);
                myReader.CancelCapture();
                myReader.Close();
                OpenAndReopenReader (myEngine);
            }
        } catch (UareUException e ) {
            e.printStackTrace();
            soundNotifGagal.turnOn();
            textGui.appendText("\n\nGAGAL MEMBUAT SIDIK JARI, HARAP DIULANGI");
        }
    }


    private void OpenAndReopenReader (Engine myEngine) throws UareUException {
        myReader.Open(Reader.Priority.EXCLUSIVE);
//        System.out.println(getClass().toString()+" : "+myReader.GetStatus().status);
        App.isOpen = true;
        finalFmd = myEngine.CreateEnrollmentFmd(Fmd.Format.DP_REG_FEATURES,EnrollmenThread.this::GetFmd);

        if (null != finalFmd){
//            Pre fmd tidak kosong, maka beri pemberitahuan bahwasanya sedang upload ke data base
            textGui.appendText("\n\n____________________________________" +
                    "\n" +
                    "Isi data peserta disamping, \njika sudah benar makan tekan tombol submit . . . . . . . .");
            // enable button
            App.BUTTON_ENROLLMENT.setDisable(false);
            App.BUTTON_ENROLLMENT.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandlerButton());
        }
    }

    private class MyEventHandlerButton implements EventHandler<Event> {

        @Override
        public void handle(Event event) {
            Button buttonClicked = ((Button)event.getSource());
            switch (buttonClicked.getId()){
                case App.ACT_BUTTON_ENROLLMENT:
                    try {
                        // Jalankan jika final fmd tidak null
                        if (null != finalFmd){

                            TextField vNama = App.TEXT_FIELD_NAMA;
                            TextField vNIY = App.TEXT_FIELD_NIY;
                            TextField vEmail = App.TEXT_FIELD_EMAIL;
                            TextField vPhone = App.TEXT_FIELD_PHONE_NUMBER;

                            // check apakah nama NIY no telepon dan alamat email
                            // terisi, jika iya maka save to DB
                            if (!(vNama.getText().isEmpty()
                                    || vEmail.getText().isEmpty()
                                    || vNIY.getText().isEmpty()
                                    || vPhone.getText().isEmpty())
                            ){




                                // Insert to DB
                                MysqlDB mysqlDB = new MysqlDB();

                                // better check connection first
                                if (mysqlDB.Open()){
                                    // jika connect maka masukan kedalam fungsi insert
                                    mysqlDB.Insert(vNIY.getText(),
                                            finalFmd.getData(),vNama.getText(),
                                            vPhone.getText(),vEmail.getText());

                                    // Upadate gui jika berhasil insert data
                                    soundNotifSukses.turnOn();

                                    // clear Photo andd all related on that
                                    imagePanel.clearImage(App.IMAGE_VIEW_FINGER);
                                    App.BUTTON_ENROLLMENT.setDisable(true);
                                    App.INFORMATION_TEXT.setText("");
                                    finalFmd = null;

                                    // show alert untuk memberitahukan bahwa pendaftaran berhasil
                                    new WarningPopUp().informationPendaftaranBerhasil();


                                }
                                else {
                                    // Gagal terhubung ke server dari mysql

                                    // clear Photo andd all related on that
                                    imagePanel.clearImage(App.IMAGE_VIEW_FINGER);
                                    App.BUTTON_ENROLLMENT.setDisable(true);
                                    App.INFORMATION_TEXT.setText("");
                                    finalFmd = null;

                                    new WarningPopUp().errorPendafataranFingerPrint ();
                                }
                                // close the reader
                                vNama.setText(null);
                                vEmail.setText(null);
                                vNIY.setText(null);
                                vPhone.setText(null);
                                myReader.Close();
                                App.isOpen = false;
                            }
                            else {
                                new WarningPopUp().informationIsianWajib();
                            }
                        }
                    }
                    catch (UareUException|SQLException e){
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }
}
