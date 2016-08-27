package ilyaandvladi.test08foodscale;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ilya on 9/10/15.
 */
public class ImageFiles {

/* --------------------------------- Fields -------------------------------- */

    private ArrayList<String> sourcePhotos;
    private ArrayList<String> scaledImages;

    private int current;

/* ---------------------------- Constant Values ---------------------------- */

    public static final int FIRST = 1;
    public static final int LAST = 14;

/* ---------------------------- DEBUG Environment -------------------------- */

    public static final String TAG = "ImageFiles";

/* ---------------------------- Object Construction ------------------------ */

    ImageFiles(){
        sourcePhotos = new ArrayList<>();
        scaledImages = new ArrayList<>();
        current = FIRST;

        sourcePhotos.add(0,"");
        scaledImages.add(0,"");
        arrayInit();
        Log.d(TAG,"Array Constructed!");
    }

/* ----------------------------- Public Methods ---------------------------- */

    public int First(){
        current = FIRST;
        return current;
    }

    public int Last(){
        current = LAST;
        return current;
    }

    public int Next(){
        if (current < LAST){
            current++;
        }
        return current;
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

/* ------------------------ Image Files Initialization --------------------- */

    private void arrayInit(){
        /* Add the corresponding filenames without the extension
         * E.G: if the filename is "foodname_t.png" write just "foodname_t" */

        sourcePhotos.add(1,"avocado_p");
        sourcePhotos.add(2,"burger_m");
        sourcePhotos.add(3,"burger_p");
        sourcePhotos.add(4,"mafe_m");
        sourcePhotos.add(5,"pastrama");
        sourcePhotos.add(6,"pepper_m");
        sourcePhotos.add(7,"pork_steak_pl");
        sourcePhotos.add(8,"shnizel_m");
        sourcePhotos.add(9,"steak_ml");
        sourcePhotos.add(10,"steak_pm");
        sourcePhotos.add(11,"steak_ps");
        sourcePhotos.add(12,"strip_m");
        sourcePhotos.add(13,"strip_p");
        sourcePhotos.add(14,"sugar_m");

        scaledImages.add(1,"avocado_p_t");
        scaledImages.add(2,"burger_m_t1");
        scaledImages.add(3,"burger_p_t");
        scaledImages.add(4,"mafe_m_t");
        scaledImages.add(5,"pastrama_t");
        scaledImages.add(6,"pepper_m_t");
        scaledImages.add(7,"pork_steak_pl_t");
        scaledImages.add(8,"shnizel_m_t");
        scaledImages.add(9,"steak_ml_t");
        scaledImages.add(10,"steak_pm_t");
        scaledImages.add(11,"steak_ps_t");
        scaledImages.add(12,"strip_m_t");
        scaledImages.add(13,"strip_p_t");
        scaledImages.add(14,"sugar_m_t");

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
