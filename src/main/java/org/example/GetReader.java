package org.example;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;

public class GetReader {

    public ReaderCollection readerCollection;
    public Reader reader;

    public GetReader () throws UareUException {
        this.readerCollection = UareUGlobal.GetReaderCollection();
        this.readerCollection.GetReaders();
        if (readerCollection.size() != 0){
            this.reader = readerCollection.get(0);
        }
        else {
            reader = null;
        }
    }

    public boolean IsReaderDetected () throws UareUException {

        if (UareUGlobal.GetReaderCollection().size() == 0){
            return false;
        }
        return true;

    }
}