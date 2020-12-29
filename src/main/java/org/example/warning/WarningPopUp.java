package org.example.warning;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class WarningPopUp {

    public void informationCostume (String messageHeader,String messageContext){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(messageHeader);
        alert.setContentText(messageContext);
        alert.show();
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
