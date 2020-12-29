package org.example.convertertext;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.example.warning.WarningPopUp;

import java.util.function.UnaryOperator;

public class CostumeConverterText {
    public CostumeConverterText (){

    }

    public TextFormatter <String> onlyAcceptDigit (){

        StringConverter<String> converter = new StringConverter<String>() {
            @Override
            public String toString(String s) {
                if(s == null || s.isEmpty()) return "";

                if(s.matches("\\d*")) {
                    return s;
                }

                return "";
            }

            @Override
            public String fromString(String s) {
                if(s == null || s.isEmpty()) return "";

                if(s.matches("\\d*")) {
                    return s;
                }

                throw new RuntimeException("Converter error");
            }
        };

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if(change.getControlNewText().matches("\\d*")) {
                return change;
            }

            return null;
        };

        return new TextFormatter <>(converter, null, filter);
    }


    public TextFormatter <String> onlyAcceptEmail (){
        StringConverter<String> converter = new StringConverter<String>() {
            @Override
            public String toString(String s) {
                if(s == null || s.isEmpty()) return "";

                if(s.matches("^(.+)@(.+)$")) {
                    return s;
                }

                return "";
            }

            @Override
            public String fromString(String s) {
                if(s == null || s.isEmpty()) return "";

                if(s.matches("^(.+)@(.+)$")) {
                    return s;
                }

                throw new RuntimeException("Converter error");
            }
        };

        UnaryOperator<TextFormatter.Change> filter = change -> {
                return change;
        };

        return new TextFormatter <>(converter, null, filter);
    }


}
