package org.example.updategui;

import javafx.scene.Node;
import javafx.scene.text.Text;


public class UpdateGui {
    Node node;
    public UpdateGui (Node viewUpdated){
        this.node = viewUpdated;
    }


    public void appendText (String text){
        String oldValue = ((Text)node).getText();

        ((Text) node).setText(oldValue+text);
    }
}
