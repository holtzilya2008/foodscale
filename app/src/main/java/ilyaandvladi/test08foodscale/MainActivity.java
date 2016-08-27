package ilyaandvladi.test08foodscale;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.MotionEvent.*;


public class MainActivity extends Activity {

/* --------------------------------- Fields -------------------------------- */

    /* --------------------------- Environment -------------------------- */

    private ImageView iv;
    private ImageView foodImage;
    private String filename;
    private Button buttonPrev;
    private Button buttonNext;
    private TextView text;

    ImageFiles imageFiles;

    /* ------------------------ Measuring Tools ------------------------ */

    private float x;
    private float y;
    private boolean moving;

    private final int D_UNITS = 1000;
    private final int PLATEA_D = 485;
    private float Ua;

    private float plateB_d;
    private float plateB_left;
    private float plateB_right;
    private float Ub;
    private float foodLeft;
    private float foodRight;
    private float foodTop;
    private float foodBottom;
    private float resultHeigth;
    private float resultWidth;
    private float currentScale;

    /* ---------------------- State machine variables ------------------ */

    private Button setButton;
    private Button cancelButton;

    private int state;
    private Button startByX;
    private Button startByY;
    private final int ACT_BY_X = 1;
    private final int ACT_BY_Y = 2;
    private final int IDLE = 0;

    private int step;
    private final int SET_PLATE_LEFT=1;
    private final int SET_PLATE_RIGHT=2;
    private final int SET_FOOD_LEFT=3;
    private final int SET_FOOD_RIGHT=4;
    private final int SET_FOOD_TOP=5;
    private final int SET_FOOD_BOTTOM=6;
    private final int CALIBRATE=7;
    private final int LOG_RESULTS=8;
    private final int RESTART=9;

/* ---------------------------- Constant Values ---------------------------- */

    public static final String START_IMAGE = "start_screen";

 /* --------------------------- DEBUG Environment -------------------------- */

    private static final String TAG = "DebugTag";

/* ---------------------------- Object Construction ------------------------ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iv = (ImageView)findViewById(R.id.PlateB);
        foodImage = (ImageView)findViewById(R.id.foodImage);

        buttonPrev = (Button)findViewById(R.id.prev_button);
        buttonPrev.setClickable(true);
        buttonNext = (Button)findViewById(R.id.next_button);
        buttonNext.setClickable(true);
        setButton = (Button)findViewById(R.id.set_button);
        setButton.setVisibility(View.GONE);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setVisibility(View.GONE);
        startByX = (Button)findViewById(R.id.startByX);
        startByY = (Button)findViewById(R.id.startByY);

        imageFiles = new ImageFiles();

        text = (TextView)findViewById(R.id.debugTest);
        setButtonsListener();
        setTouchListener();
        initValues();
        setActive();
    }

/* -------------------------------- Methods -------------------------------- */

    public void initValues(){
        Ua = (float)PLATEA_D/D_UNITS;
        plateB_d=0;
        plateB_left=0;
        plateB_right=0;
        Ub=0;
        foodLeft=0;
        foodRight=0;
        foodTop=0;
        foodBottom=0;
        resultHeigth=0;
        resultWidth=0;
        x=0;
        y=0;
        moving = false;
        currentScale = 1;
        foodImage.setOnTouchListener(null);

        step = IDLE;
        state = IDLE;
    }

    public void setButtonsListener(){
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPrevious();
                state = IDLE;
                setActive();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetNext();
                state = IDLE;
                setActive();
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentValue();
                step++;
                setActive();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = IDLE;
                setActive();
            }
        });
        startByX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = ACT_BY_X;
                step = 1;
                setActive();
            }
        });
        startByY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = ACT_BY_Y;
                step = 1;
                setActive();
            }
        });
    }

    private void GetNext(){
        imageFiles.Next();
        setFilename();
        bringTofrontButtons();
    }

    private void GetPrevious(){
        if(filename == START_IMAGE){
            imageFiles.First();
        }
        imageFiles.Prev();
        setFilename();
        bringTofrontButtons();
    }

    private void setFilename(){
        filename = imageFiles.getCurrentSource();
        text.setText(filename);
        int icon_id = getResources().getIdentifier(filename, "drawable", getPackageName());
        iv.setImageResource(icon_id);
        Log.d(TAG,"current file: f" + filename);
    }

    public void setActive(){
        switch(state){
            case ACT_BY_X: runMashine(); break;
            case ACT_BY_Y: runMashine(); break;
            case IDLE:     runIdleState(); break;
            default:       runIdleState(); break;
        }
    }

    public void setCurrentValue(){
        switch(step){
            case SET_PLATE_LEFT:
                if(state == ACT_BY_X){
                    plateB_left = x;
                }else {
                    plateB_left = y;
                }
                break;
            case SET_PLATE_RIGHT:
                if(state == ACT_BY_X){
                    plateB_right = x;
                }else {
                    plateB_right = y;
                }
                break;
            case SET_FOOD_LEFT: foodLeft = x; break;
            case SET_FOOD_RIGHT: foodRight = x; break;
            case SET_FOOD_TOP: foodTop = y;  break;
            case SET_FOOD_BOTTOM: foodBottom = y; break;
            default:
        }
    }

    public void runIdleState(){
        state = IDLE;
        step = IDLE;
        initValues();
        setButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        startByX.setVisibility(View.VISIBLE);
        startByY.setVisibility(View.VISIBLE);
        iv.bringToFront();
        foodImage.setVisibility(View.GONE);
        bringTofrontButtons();
    }

    public void runMashine(){
        switch(step){
            case SET_PLATE_LEFT:
                setButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                startByX.setVisibility(View.GONE);
                startByY.setVisibility(View.GONE);
                text.setText("Set Plate Start");
                break;
            case SET_PLATE_RIGHT:
                text.setText("Set Plate End");
                break;
            case SET_FOOD_LEFT:
                text.setText("Set food left");
                break;
            case SET_FOOD_RIGHT:
                text.setText("Set food right");
                break;
            case SET_FOOD_TOP:
                text.setText("Set food top");
                break;
            case SET_FOOD_BOTTOM:
                text.setText("Set food bottom");
                break;
            case CALIBRATE:
                text.setText("Calculating results ...");
                calcResults();
                calibrateFoodImage();
                text.setText("SET to continue \nCANCEL to abort");
                break;
            case LOG_RESULTS:
                logResults();
                break;
            case IDLE:
            case RESTART:
            default: state = IDLE; setActive();
        }
    }

    public void calcResults(){
        float height = abs(foodBottom-foodTop);
        Log.d("Calc","Original height (dp) = " + height);
        float width = abs(foodRight-foodLeft);
        Log.d("Calc","Original width (dp) = " + width);
        plateB_d = abs(plateB_right - plateB_left);
        Log.d("Calc","Plate B diameter (dp) = " + plateB_d);
        Ub = plateB_d/D_UNITS;
        Log.d("Calc","Unit size for plate B (dp) = " + Ub);
        Log.d("Calc","Unit size for plate A (dp) = " + Ua);
        currentScale = Ua/Ub;
        Log.d("Calc","currentScale (Ua/Ub)  = " + currentScale);
        float heightUnits = height/Ub;
        Log.d("Calc","height (Ub) = " + heightUnits);
        float widthUnits = width/Ub;
        Log.d("Calc","width (Ub) = " + widthUnits);
        resultHeigth = heightUnits*Ua;
        Log.d("Calc","height scaled (dp) = " + resultHeigth);
        resultWidth = widthUnits*Ua;
        Log.d("Calc","width scaled (dp) = " + resultWidth);
    }

    public void logResults() {
        Log.d("Results", filename + ": W = " + ((int) resultWidth)+" H = "+((int) resultHeigth));
        text.setText("W = " + ((int) resultWidth) + " H = " + ((int) resultHeigth) + "\npress SET to continue...");
    }

    public void setTouchListener(){
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (step == LOG_RESULTS) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moving = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (moving) {
                            x = event.getRawX();
                            y = event.getRawY();
                            text.setText(String.format("Rx: %.02f " +
                                    "Ry: %.02f", x, y));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        break;
                }

                return true;
            }
        });
    }

    public void calibrateFoodImage(){
        int icon_id = getResources().getIdentifier(imageFiles.getCurrentScaled(), "drawable", getPackageName());
        foodImage.setImageResource(icon_id);
        ImageView plate = (ImageView)findViewById(R.id.plateImage);
        FrameLayout.LayoutParams newParams;
        newParams = new FrameLayout.LayoutParams(
                ((int) resultWidth), ((int) resultHeigth));
        plate.bringToFront();
        foodImage.setVisibility(View.GONE);
        foodImage.setLayoutParams(newParams);

        foodImage.setScaleType(ImageView.ScaleType.FIT_XY);
        foodImage.setVisibility(View.VISIBLE);
        foodImage.bringToFront();

        foodImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moving = true;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (moving) {
                            x = event.getRawX() - (foodImage.getWidth()/2);
                            y = event.getRawY() - (foodImage.getHeight()*3/4);
                            foodImage.setX(x);
                            foodImage.setY(y);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        break;
                }
                return true;
            }
        });
    }

    private float abs(float val){
        if(val>=0){
            return val;
        }
        return (-1)*val;
    }

    private void bringTofrontButtons(){
        buttonPrev.bringToFront();
        buttonNext.bringToFront();
        setButton.bringToFront();
        cancelButton.bringToFront();
        startByX.bringToFront();
        startByY.bringToFront();
        text.bringToFront();
    }

}
