package model.serialization;

import model.gui.IAlert;
import model.map.Map;

import java.io.*;

public abstract class Save implements IAlert, Serializable {

    public void save(Save s, File f){
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            popup("Unable to save map.");
        }
    }

    public Save loadMap(File inFile) {

        Save readObject = null;

        try {
            FileInputStream fis = new FileInputStream(inFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            readObject = (Map) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            popup("Unable to open file");
            return null;
        } catch (ClassNotFoundException e) {
            popup("Unable to open file, class not found");
            return null;
        }

        return readObject;

    }
}
