package org.example.warning;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class WarningPopUp {

    public void informationCostume (String messageHeader,String messageContext){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(messageHeader);
        alert.setContentText(messageContext);
        alert.show();
    }

    public void warningReaderNotReadAble (Reader reader) throws UareUException {
        ButtonType buttonType = new ButtonType("Terpasang");
        Alert alert = new Alert(Alert.AlertType.WARNING,"Reader tidak terbaca...\n\nMasukan reader sekarang lalu tekan oke"
        ,buttonType);

        alert.setTitle("Peringatan : Reader tidak terbaca");

        Optional<ButtonType> optionalButtonType = alert.showAndWait();

//        if (optionalButtonType.orElse(buttonType) == buttonType){
//            if (reader != null ){
//                System.out.println("Reader sebelumnya ada lalu tidak ada");
//            }
//            else
//            {
//                System.out.println("Reader tidak ada sejak awal");
//            }
//        }
    }

    public void informationPendaftaranBerhasil (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status Pendaftaran");
        alert.setContentText("Alhamdulillah\n\nPendaftaran berhasil!!!");
        alert.show();
    }

    public void errorPendafataranFingerPrint (){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal terhubung ke server");
        alert.setContentText("Pendaftaran gagal: \n" +
                "Gagal terbuhung ke server.");
        alert.show();
    }

    public void informationIsianWajib (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status Pendaftaran");
        alert.setContentText("Data calon anggota harus diisi.");
        alert.show();
    }
}
