package org.example;

import com.digitalpersona.uareu.Fid;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImagePanel {
    private BufferedImage myImage;

    public void saveImage (Fid image){
        Fid.Fiv fiv =image.getViews()[0];
        myImage = new BufferedImage(fiv.getWidth(),fiv.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
        myImage.getRaster().setDataElements(0,0,fiv.getWidth(), fiv.getHeight(), fiv.getImageData());
        System.out.println("My Image stream "+myImage);
        saveBack(myImage);
    }

    public void saveBack(final BufferedImage myImage){
        new SwingWorker<Object,Object>(){

            @Override
            protected Object doInBackground() throws Exception {

                try {

                    BufferedImage bi =myImage;
                    File outputFile = new File ("saved.png");
                    ImageIO.write(bi,"png",outputFile);
                    System.out.println(outputFile);
                }
                catch (Exception e){

                }
                return null;
            }
        }.execute();
    }

    public void showImage (ImageView imageView)  {

        String path = "file:saved.png";
        Image myImage = new Image(path);
        imageView.setImage(myImage);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    public void clearImage (ImageView imageView){

            imageView.setImage(null);

    }
}
