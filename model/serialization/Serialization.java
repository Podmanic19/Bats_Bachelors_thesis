package model.serialization;

import model.gui.IAlert;
import model.map.Map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialization implements IAlert {
    public static Serialization instance = null;

    private Serialization() {
    }

    public static Serialization getInstance() {
        if (instance == null)
            instance = new Serialization();
        return instance;
    }

    public void saveMap(Map m, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            popup("Unable to save map.");
        }
    }

    public Map loadMap(String path) throws IOException {

        File inFile = new File(path);
        boolean newFile = inFile.createNewFile();
        Map readObject = null;

        if (!newFile) {
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
        }

        return readObject;

    }

    public Map loadMap(File inFile) throws IOException {

        Map readObject = null;

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

    public void saveMap(Map m, File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            popup("Unable to save map.");
        }
    }

}
