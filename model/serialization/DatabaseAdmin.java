package model.serialization;
import java.io.*;
import java.util.ArrayList;


public class DatabaseAdmin<T> implements Serializable{

    public void upload(String path,ArrayList <T> uploaded ){

        try
        {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(uploaded);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public ArrayList<T> download(String path) throws IOException {
        ArrayList<T> Database;
        Database = new ArrayList<>();
        File inFile= new File(path);
        boolean newFile = inFile.createNewFile();


        if (!newFile) {
            try {
                FileInputStream fis = new FileInputStream(inFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Database = (ArrayList<T>) ois.readObject();
                ois.close();
                fis.close();
            }
            catch (IOException ioe)
            {
                return Database;
            }
            catch (ClassNotFoundException c)
            {
                System.out.println("Class not found");
                c.printStackTrace();
            }
        }

        return Database;
    }
}
