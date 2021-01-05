package org.example;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.UareUException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import org.example.convertertext.CostumeConverterText;
import org.example.warning.WarningPopUp;


/**
 * JavaFX App
 */
public class App extends Application {

    public static Stage parentStage;
    private final BorderPane borderPane = new BorderPane();
    private NamedStyle namedStyle;
    private static final StackPane centerStackPane = new StackPane();

    public static boolean isOpen = false;


    /*
    OPEN: STATIC READER
     */
    public static Reader reader;

    static {
        try {
            reader = new GetReader().reader;
        } catch (UareUException e) {
            e.printStackTrace();
        }
    }
    /*
    close: STATIC READER
     */

    /*
    OPEN: Kumpulan public node untuk sharing information antar class
     */
    public static final Text INFORMATION_TEXT = new Text();
    public static final ImageView IMAGE_VIEW_FINGER = new ImageView();
    public static Button BUTTON_ENROLLMENT;
    public static TextField TEXT_FIELD_NAMA;
    public static TextField TEXT_FIELD_NIY;
    public static TextField TEXT_FIELD_PHONE_NUMBER;
    public static TextField TEXT_FIELD_EMAIL;

    /*
    CLOSE: Kumpulan node untuk sharing information antar class
     */



    /*
    OPEN : Kumpulan private variabel untuk menentukan action dari setiap link
     */
    private static final String ACT_ENROLLMENT = "enrollment";

    private static final String ACT_VERIFICATION = "verification";

    public static final String ACT_BUTTON_ENROLLMENT = "enrollment-go";

    public static final String ACT_ABSEN = "absen";

    public static final String ACT_DASHBOARD_ABSEN = "dashboard_absen";

    public static final String ACT_UNDUH_ABSEN = "unduh_absen";

    public App() {
    }

    /*
    CLOSE : Kumpulan private variabel untuk menentukan action dari setiap link
     */


    @Override
    public void start(Stage stage) {

        parentStage = stage;

        namedStyle= new NamedStyle();

        BUTTON_ENROLLMENT = new Button("Daftarkan");
        BUTTON_ENROLLMENT.setDisable(true);
        BUTTON_ENROLLMENT.setId(ACT_BUTTON_ENROLLMENT);

        TEXT_FIELD_NAMA = new TextField();
        TEXT_FIELD_NIY = new TextField();
        TEXT_FIELD_PHONE_NUMBER = new TextField();
        TEXT_FIELD_EMAIL = new TextField();


//        set borderpane
        // set to stackpane first

        borderPane.setTop(addHbox("Perpustakaan Sekolah Kita"));

        borderPane.setLeft(addVbox());
        borderPane.setBottom(setFooter());
        borderPane.setRight(setLog());
        borderPane.setCenter(centerStackPane);



        StackPane myStackPane = new StackPane();
        myStackPane.getChildren().add(borderPane);

        Scene myScene = new Scene(myStackPane,950,800);

//        url dari css file
        // get css file
        ClassLoader classLoader = getClass().getClassLoader();

        myScene.getStylesheets().add(classLoader.getResource("style/decoration.css").toString());

        stage.setTitle("Perpustakaan Sekolah Kita");

        stage.getIcons().add(new Image(classLoader.getResource("picture/book.png").toString()));
        stage.setScene(myScene);
        stage.setMinHeight(800);
        stage.setMinWidth(950);

        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }

    private HBox addHbox (String readerId){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);
        hBox.getStyleClass().add(namedStyle.HEADER);

        Text mTextReader = new Text();
        mTextReader.getStyleClass().add(namedStyle.HEADER_TEXT);
        mTextReader.setText(readerId);
        mTextReader.setTextAlignment(TextAlignment.CENTER);
        mTextReader.setBoundsType(TextBoundsType.LOGICAL);

        hBox.getChildren().add(mTextReader);

        return hBox;
    }

    private VBox addVbox (){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15,12,15,12));
        vBox.setSpacing(10);
        vBox.getStyleClass().add(namedStyle.LEFT_BORDER_PANE);
        vBox.getStyleClass().add(namedStyle.DEFAULT_BG_COLOR);


        // Menu utama
        Text titleMenu = new Text("Menu Utama");
        titleMenu.setFont(Font.font("Arial", FontWeight.BOLD,14));
        vBox.getChildren().add(titleMenu);
        Hyperlink[] menuUtama = new Hyperlink[]{
                new Hyperlink("Absen"),
                new Hyperlink("Dash board rekap absen"),
                new Hyperlink("Unduh rekap absen")
        };

        menuUtama[0].setId(ACT_ABSEN);
        menuUtama[1].setId(ACT_DASHBOARD_ABSEN);
        menuUtama[2].setId(ACT_UNDUH_ABSEN);

        for (Hyperlink hyperlink : menuUtama) {

            hyperlink.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
            VBox.setMargin(hyperlink, new Insets(5, 0, 2, 10));
            vBox.getChildren().add(hyperlink);
        }



        Text title = new Text("Keanggotaan");
        title.setFont(Font.font("Arial", FontWeight.BOLD,14));

        Text subTitle1 = new Text("Pegawai");
        subTitle1.setFont(Font.font("Arial", FontWeight.SEMI_BOLD,13));

        vBox.getChildren().addAll(title,subTitle1);
        Hyperlink[] options = new Hyperlink[]{
                new Hyperlink("Daftar Baru Peserta Perpus"),
                new Hyperlink("Ubah Data Peserta Perpus")
        };
        options[0].setId(ACT_ENROLLMENT);
        options[1].setId(ACT_VERIFICATION);

        for (Hyperlink option : options) {

            option.addEventHandler(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
            VBox.setMargin(option, new Insets(5, 0, 2, 10));
            vBox.getChildren().add(option);
        }

        Text subTitle2 = new Text("Siswa");
        subTitle2.setFont(Font.font("Arial", FontWeight.SEMI_BOLD,13));
        vBox.getChildren().add(subTitle2);

        return vBox;
    }

    private VBox addCapturedBox (){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15,12,15,12));
        vBox.setSpacing(10);

        Text mTextReader = new Text();
        mTextReader.setText("Ini pendaftaran");
        mTextReader.setTextAlignment(TextAlignment.CENTER);
        mTextReader.setBoundsType(TextBoundsType.LOGICAL);

        vBox.getChildren().add(mTextReader);

        return vBox;
    }

    private GridPane addVerificationBox (){

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);

        gridPane.getStyleClass().add(new NamedStyle().DEFAULT_BG_COLOR);


        // Box untuk log absen masukan ke pane kanan
        VBox vBox = new VBox();
//        vBox.setMinWidth(centerStackPane.getWidth()/2);
//        vBox.setMaxWidth(centerStackPane.getWidth()/2);
        vBox.getStyleClass().add(new NamedStyle().DEFAULT_BG_COLOR);
        vBox.setPadding(new Insets(15,12,15,12));
        vBox.setSpacing(10);


        Text title = new Text("Status Pendaftaran Peserta Baru");
        title.setFont(Font.font("Arial", FontWeight.BOLD,14));
        INFORMATION_TEXT.setText("Pendafataran Sidik Jari Perpustakaan Sekola Kita");
        INFORMATION_TEXT.setTextAlignment(TextAlignment.LEFT);
        INFORMATION_TEXT.setBoundsType(TextBoundsType.LOGICAL);
        vBox.getChildren().add(IMAGE_VIEW_FINGER);
        vBox.getChildren().add(title);
        vBox.getChildren().add(INFORMATION_TEXT);


        // vbox untuk data informasi
        VBox vBoxData = new VBox();
//        vBoxData.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        vBoxData.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // bind property vbox
        vBoxData.prefWidthProperty().bind(centerStackPane.widthProperty().divide(2));

        vBoxData.getStyleClass().add(new NamedStyle().DEFAULT_BG_COLOR);
        vBoxData.setPadding(new Insets(15,12,15,12));
        vBoxData.setSpacing(10);


        TEXT_FIELD_PHONE_NUMBER.setTextFormatter(new CostumeConverterText().onlyAcceptDigit());
        TEXT_FIELD_EMAIL.setTextFormatter(new CostumeConverterText().onlyAcceptEmail());

        Text textInformation = new Text();
        textInformation.setText("Isikan data diatas sesuai dengan data calon anggota perpustakaan sdit " +
                "Sekolah Kita.\n\n" +
                "Setelah berhasil mendaftarkan peserta lalu ingin mendaftarkan peserta selanjutnya" +
                "dapat menekan perintah \"Daftar Baru Peserta Perpus\"." +
                "\n\nJazakalaALlahu Khairan");
        textInformation.wrappingWidthProperty().bind(centerStackPane.widthProperty().divide(2));


        vBoxData.getChildren().addAll(new Label("Nama"),
                TEXT_FIELD_NAMA,new
                        Label("NIY"), TEXT_FIELD_NIY,
                new Label("Nomor Telepon (Whats App)"), TEXT_FIELD_PHONE_NUMBER,
                new Label("Alamat Email"), TEXT_FIELD_EMAIL,textInformation,BUTTON_ENROLLMENT);


//        hBox.getChildren().addAll(vBoxData,vBox);

        gridPane.add(vBoxData,0,0);
        gridPane.add(vBox,1,0);
        return gridPane;
    }


    private VBox setFooter (){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15,12,15,12));
        vBox.setSpacing(10);
        vBox.setCenterShape(true);

        // text footer
        Text footer = new Text("Perpustakaan Sekolah Kita Version : 1.1.0 " +
                "Dikembangkan oleh : Abu Faris Ma'ruf");
        footer.setTextAlignment(TextAlignment.CENTER);

        vBox.getChildren().add(footer);
        return vBox;
    }

    private VBox setLog (){
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(15,12,15,12));
        vBox.setSpacing(10);
        vBox.setCenterShape(true);

        // text footer
        Text footer = new Text("Log aktivitas terkahir\n\n" +
                "Masih dalam pengembangan");
        footer.setTextAlignment(TextAlignment.CENTER);

        vBox.getChildren().add(footer);
        return vBox;
    }

    private void changeNodeBorderPane (Node nodeCenter){
        centerStackPane.getChildren().removeAll(centerStackPane.getChildren());
        centerStackPane.getChildren().add(nodeCenter);
    }

    private void closeReaderIfItsOpen () {
        if (isOpen){
            System.out.println(getClass().toString()+" : "+"Reader is open");

            try {
                reader.CancelCapture();
                reader.Close();

//                            change flag
                isOpen = false;
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(getClass().toString()+" : "+"Reader is closed");
        }
    }

    private class MyEventHandler implements EventHandler<Event> {

        @Override
        public void handle(Event event) {
            Hyperlink selectedHyperLink = ((Hyperlink)event.getSource());
            switch (selectedHyperLink.getId()){
                case ACT_VERIFICATION:

                    changeNodeBorderPane(addCapturedBox());
                    closeReaderIfItsOpen ();
                    System.out.println(getClass().toString()+" : "+"Daftar kan absen");

                    break;
                case ACT_ENROLLMENT:
                    changeNodeBorderPane(addVerificationBox());
                    closeReaderIfItsOpen ();

                    EnrollmenThread enrollmenThread = new EnrollmenThread();

                    new Thread(enrollmenThread).start();

                    try {

                        enrollmenThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(getClass().toString()+" : "+"Verifikasi");
                    break;

                case ACT_ABSEN:

                    changeNodeBorderPane(addCapturedBox());
                    closeReaderIfItsOpen ();

                    Stage myStage = new Stage();
                    try {
                        new Absen().start(myStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                default:
                    new WarningPopUp().informationCostume("Status","Masih dalam tahap pengembangan.");
                    break;
            }
        }
    }
}