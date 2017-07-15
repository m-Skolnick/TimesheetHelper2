package micaiahskolnick.timesheethelper;

import android.content.SharedPreferences;


import java.util.Calendar;

import android.provider.AlarmClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    /*
    To do list

    *Set to display doubele "0" when an exact hour is hit

     */

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private String testNumber;



    private EditText goalBox;
    private EditText currBox;
    private EditText clockInTimeHr;
    private EditText clockInTimeMin;
    private EditText clockOutTime;
    private TextView timeNeededHoursBox;
    private TextView timeNeededMinutesBox;
    private TextView timeNeededHours2Box;
    private TextView timeNeededMinutes2Box;
    private TextView timeNeededHeadingBox;
    private TextView hoursHeading;
    private TextView minutesHeading;
    private LinearLayout resultsLayout;
    private ToggleButton AmPmBtn;
    private Button NowBtn;
    private Calendar calendar;
    private AlarmClock alarmClock;



    private double goal;

    private double current;
    private double needed;
    private double neededHours;
    private double neededMinutes;
    private double clockInTimeMinDouble;
    private double clockInTimeHrDouble;
    private int neededHoursInt;
    private int neededMinInt;
    private String goalStr;
    private String timeNeededHoursStr;
    private String timeNeededMinutesStr;
    String clockOutTimeStr;
    private Button AlarmBtn;

    public void updateDisplay(){
        getNumbers();
        calculateTimeNeeded();
        setTimeNeededText();
        showOrHideResults();
        updateClockOutTime();
    }
    private void updateClockOutTime(){
        if(getClockInTime()) {
            calculateClockOutTime();
            updateClockOutDisplay();
        }

    }
    private boolean getClockInTime(){
        try {
            clockInTimeHrDouble = Double.parseDouble(clockInTimeHr.getText().toString());
        }
        catch (Exception e){
            clockInTimeHrDouble = 0;
            return false;
        }
        try {
            clockInTimeMinDouble = Double.parseDouble(clockInTimeMin.getText().toString());
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    private void calculateClockOutTime(){


        int clockOutTimeHrInt;
        int clockOutTimeMinInt;
        boolean PM = true;
        if(AmPmBtn.isChecked()){
            PM = false;
        }


        clockOutTimeHrInt = (int) clockInTimeHrDouble + neededHoursInt;
        clockOutTimeMinInt = (int) clockInTimeMinDouble + neededMinInt;

        while(clockOutTimeMinInt > 60){
            clockOutTimeHrInt++;
            clockOutTimeMinInt -=60;
        }
        while(clockOutTimeHrInt > 12){
            clockOutTimeHrInt -=12;
            PM = !PM;
            if(clockOutTimeHrInt == 12){
                PM = !PM;
            }
        }

        clockOutTimeStr = Integer.toString(clockOutTimeHrInt);


            //Concatenate all of the pieces of clock out time together to one string.

        if(clockOutTimeMinInt < 10){ //If the minutes are one integer, add a 0 before it.
            clockOutTimeStr = clockOutTimeStr.concat(":0").concat(Integer.toString(clockOutTimeMinInt));
        }
        else{ //If the minutes are two integers long, don't add any numbers before the minutes
            clockOutTimeStr = clockOutTimeStr.concat(":").concat(Integer.toString(clockOutTimeMinInt));
        }
        if(PM){
            clockOutTimeStr = clockOutTimeStr.concat(" PM");
        }
        else{
            clockOutTimeStr = clockOutTimeStr.concat(" AM");
        }


    }
    private void updateClockOutDisplay(){
        clockOutTime.setText(clockOutTimeStr);
    }
    private void setCurrentTimeAsClockInTime(){
        int currentHour,currentMinute;
        String currentMinuteStr;
        Calendar.getInstance();

        Time currentTime = new Time();
        currentTime.setToNow();
        currentHour = currentTime.hour;
        if(currentHour > 12){
            currentHour -=12;
            AmPmBtn.setChecked(false);
        }
        else{
            AmPmBtn.setChecked(true);
        }
        currentMinute = currentTime.minute;

        if(currentMinute < 10){ //If the current minute is one integer in length, add a 0 before it
            currentMinuteStr = "0";
            currentMinuteStr = currentMinuteStr.concat(Integer.toString(currentMinute));
        }
        else{ //If the current minute is not less than 0, don't add a 0
            currentMinuteStr = Integer.toString(currentMinute);
        }
            //Set both the clock-in time hour and minute
        clockInTimeHr.setText(Integer.toString(currentHour));
        clockInTimeMin.setText(currentMinuteStr);
    }
    private void showOrHideResults(){
        try {
            current = Double.parseDouble(currBox.getText().toString());
            resultsLayout.setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            current = 0;
            resultsLayout.setVisibility(View.INVISIBLE);
        }
    }
    public void getNumbers(){

        try {
            goal = Double.parseDouble(goalBox.getText().toString());
        }
        catch (Exception e){
            goal = 0;
        }
        try {
            current = Double.parseDouble(currBox.getText().toString());
        }
        catch (Exception e){
            current = 0;
        }
    }
    public void calculateTimeNeeded(){
        needed = goal - current;
        neededMinutes = needed*60;
        neededHours = (int) needed;
        neededMinutes =  neededMinutes - neededHours*60;
        neededHoursInt = (int) neededHours;
        neededMinInt = (int) neededMinutes;
    }
    public void setTimeNeededText(){

        timeNeededHoursStr = Integer.toString(neededHoursInt);
        timeNeededMinutesStr = Integer.toString(neededMinInt);
        // Set text in boxes
        timeNeededHoursBox.setText(timeNeededHoursStr);
        timeNeededMinutesBox.setText(timeNeededMinutesStr);
    }
    public void clearBox(EditText thisBox){
        thisBox.setText("");
        updateDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        getNumbers(); //Get the numbers before saving them.
        String goalStr = Double.toString(goal);
        String currStr = Double.toString(current);

        preferenceEditor.putString("savedGoal",goalStr);
        preferenceEditor.putString("savedCurr",currStr);

        preferenceEditor.putString("savedClockInHr",clockInTimeHr.getText().toString());
        preferenceEditor.putString("savedClockInMin",clockInTimeMin.getText().toString());
        preferenceEditor.putString("savedClockOutTime",clockOutTimeStr);

        // Apply the edits!
        preferenceEditor.apply();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        goalBox.setText(preferenceSettings.getString("savedGoal",""));
        currBox.setText(preferenceSettings.getString("savedCurr",""));
        clockInTimeHr.setText(preferenceSettings.getString("savedClockInHr",""));
        clockInTimeMin.setText(preferenceSettings.getString("savedClockInMin",""));
        clockOutTime.setText(preferenceSettings.getString("savedClockOutTime",""));

        getNumbers();
        if(current < 0.0000001){ //If current is 0, clear the box
            clearBox(currBox);
        }
        if(goal < 0.0000001){ //If current is 0, clear the box
            goalBox.setText("40");
        }
        currBox.requestFocus(); //Put the focus on the box for current total


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        currBox = (EditText) findViewById(R.id.curr_Box);
        goalBox = (EditText) findViewById(R.id.goal_Box);
        goalBox.setText("40");

        timeNeededHoursBox = (TextView) findViewById(R.id.time_needed_hours);
        timeNeededMinutesBox = (TextView) findViewById(R.id.time_needed_minutes);
        clockInTimeHr = (EditText) findViewById(R.id.clock_in_time_hr);
        clockInTimeMin = (EditText) findViewById(R.id.clock_in_time_min);
        clockOutTime = (EditText) findViewById(R.id.clock_out_time);
        AmPmBtn = (ToggleButton) findViewById(R.id.am_pm_btn);
        NowBtn = (Button) findViewById(R.id.now_btn);
        AlarmBtn = (Button) findViewById(R.id.alarm_btn);
        resultsLayout = (LinearLayout) findViewById(R.id.results_layout);
        //alarmClock = new(AlarmClock);

        currBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBox(currBox);
                updateDisplay();
            }
        });
        goalBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBox(goalBox);
                updateDisplay();
            }
        });
        clockInTimeHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBox(clockInTimeHr);
                // Need to update the clock out time, but haven't written function yet
            }
        });
        clockInTimeMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBox(clockInTimeMin);
            }
        });
        // Add a listener to update values when text in input box is changed.
        currBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                updateDisplay();
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
        goalBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                updateDisplay();
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
        clockInTimeMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                updateClockOutTime();
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
        clockInTimeHr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                updateClockOutTime();
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
        AmPmBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                updateDisplay();
            }
        });
        NowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentTimeAsClockInTime();
                updateDisplay();
            }
        });
        AlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "google", Toast.LENGTH_LONG).show();

                Snackbar.make(getCurrentFocus(), "'SetAlarm' is currently disabled (Coming Soon)", Snackbar.LENGTH_LONG).show();

            }
        });
    }
}
