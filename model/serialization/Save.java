package model.serialization;

import model.gui.Popup;
import model.map.Map;

import java.io.*;

public abstract class Save implements Popup, Serializable {

    public static void save(Save s, File f){
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
//            popup("Unable to save map.");
        }
    }

    public static Save load(File inFile) {

        Save readObject;

        try {
            FileInputStream fis = new FileInputStream(inFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            readObject = (Map) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException ioe) {
            //popup("Unable to open file");
            ioe.printStackTrace();
            return null;
        } //popup("Unable to open file, class not found");


        return readObject;

    }
}
