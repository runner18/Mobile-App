package com.example.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    DatePicker alarmDatePicker;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmDatePicker = (DatePicker) findViewById(R.id.datePicker);
        textView = (TextView) findViewById(R.id.textView);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    // OnToggleClicked() method is implemented the time functionality
    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            // calendar is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
            calendar.set(Calendar.DAY_OF_MONTH, alarmDatePicker.getDayOfMonth());
            calendar.set(Calendar.YEAR, alarmDatePicker.getYear());

            // takes in AlarmReceiver (extends BroadcastReceiver)

            //public Intent(Context packageContext, Class<?> cls) {
            //        throw new RuntimeException("Stub!");
            //    }
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("MESSAGE", textView.getText());

            // we call broadcast using pendingIntent, the broadcast lets apps talk to each other
            //PendingIntent allows the foreign application to use your application's permissions to execute a predefined piece of code
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            long timeinmillis = calendar.getTimeInMillis();

            //this calculation gets rid of the leftover seconds so it's just the minute
            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));


            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }
}