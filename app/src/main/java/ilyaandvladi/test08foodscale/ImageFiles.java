package ilyaandvladi.test08foodscale;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ilya on 9/10/15.
 */
public class ImageFiles {

/* --------------------------------- Fields -------------------------------- */

    private ArrayList<String> sourcePhotos;
    private ArrayList<String> scaledImages;

    private int current;

/* ---------------------------- Constant Values ---------------------------- */

    public static final int FIRST = 0;
    //public static final int LAST = 0;

/* ---------------------------- DEBUG Environment -------------------------- */

    public static final String TAG = "ImageFiles";

/* ---------------------------- Object Construction ------------------------ */

    ImageFiles(){
        sourcePhotos = new ArrayList<>();
        scaledImages = new ArrayList<>();
        current = FIRST;
        arrayInit();
        Log.d(TAG,"Array Constructed!");
    }

/* ----------------------------- Public Methods ---------------------------- */

    public int First(){
        current = FIRST;
        return current;
    }

    public int Next(){
        if (current + 1 >= sourcePhotos.size() || !(current + 1 > 0)) {
            return current;
        }
        return ++current;
    }

    public int Prev(){
        if(current > FIRST){
            current--;
        }
        return current;
    }

    public String getCurrentSource(){
        return sourcePhotos.get(current);
    }

    public String getCurrentScaled(){
        return scaledImages.get(current);
    }

/* ----------------------------- Object Methods ---------------------------- */
    private ArrayList<String> getList(File parentDir, String pathToParentDir) {

        ArrayList<String> inFiles = new ArrayList<String>();
        String[] fileNames = parentDir.list();

        Log.d(pathToParentDir,"grabbing from parent directory");

        for (String fileName : fileNames) {
            if (fileName.toLowerCase().endsWith(".jpg")  ||
                fileName.toLowerCase().endsWith(".jpeg") ||
                fileName.toLowerCase().endsWith(".png")  ||
                fileName.toLowerCase().endsWith(".gif")    )  {
                inFiles.add(pathToParentDir + fileName);
            } else {
                File file = new File(parentDir.getPath() + "/" + fileName);
                if (file.isDirectory()) {
                    inFiles.addAll(getList(file, pathToParentDir + fileName + "/"));
                }
            }
        }

        return inFiles;
    }
/* ------------------------ Image Files Initialization --------------------- */

    private void arrayInit(){
        // In DCIM/Camera must be two folders: /regular, with view on plate, and /transparent without background
        String camera_folder_regular = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/Camera/regular/";
        sourcePhotos = getList(new File(camera_folder_regular), camera_folder_regular);
        Collections.sort(sourcePhotos);
        String camera_folder_transparent = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"/Camera/transparent/";
        scaledImages = getList(new File(camera_folder_transparent), camera_folder_transparent);
        Collections.sort(scaledImages);
    }

    @Override
    public String toString() {
        return "ImageFiles{" +
                "sourcePhotos=" + sourcePhotos +
                ", scaledImages=" + scaledImages +
                ", current=" + current +
                '}';
    }
} // End of Class ImageFiles ----------------------------------------------- //
