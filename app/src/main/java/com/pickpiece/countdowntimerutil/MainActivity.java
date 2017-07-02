package com.pickpiece.countdowntimerutil;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pickpiece.countdowntimerutil.util.CountDownUtil;

public class MainActivity extends AppCompatActivity {

    TextView mTimerText;
    TextView mTimerText2;

    Button mTimerButton;
    CountDownUtil timer1;
    CountDownUtil timer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerText = (TextView) findViewById(R.id.main_timer);
        mTimerText2 = (TextView) findViewById(R.id.main_timer_2);
        mTimerButton = (Button) findViewById(R.id.main_timer_button);
        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClicked();
            }
        });

        long TIMER_LENGHT_1 = 15;
        Intent intent1 = new Intent(this, MainActivityExpiredReceiver.class);
        intent1.putExtra("aaa", "ONE");
        timer1 = new CountDownUtil(TIMER_LENGHT_1, this, "a1", intent1);
        timer1.setOnCountEvent(onCountEvent1);

        long TIMER_LENGHT_2 = 10;
        Intent intent2 = new Intent(this, MainActivityExpiredReceiver.class);
        intent2.putExtra("aaa", "TWO");
        timer2 = new CountDownUtil(TIMER_LENGHT_2, this, "a2", intent2);
        timer2.setOnCountEvent(onCountEvent2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        timer1.initTimer();
        timer1.removeAlarm();

        timer2.initTimer();
        timer2.removeAlarm();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer2.pauseTimer();
        timer1.pauseTimer();
    }

    private CountDownUtil.OnCountEvent onCountEvent1 = new CountDownUtil.OnCountEvent() {
        @Override
        public void onTimerFinish() {
            Toast.makeText(getApplicationContext(), R.string.timer_finished, Toast.LENGTH_SHORT).show();
            mTimerText.setText(String.valueOf(timer1.getTimeToGo()));
        }

        @Override
        public void onTickEvent() {
            mTimerText.setText(String.valueOf(timer1.getTimeToGo()));
        }
    };

    private CountDownUtil.OnCountEvent onCountEvent2 = new CountDownUtil.OnCountEvent() {
        @Override
        public void onTimerFinish() {
            Toast.makeText(getApplicationContext(), R.string.timer_finished, Toast.LENGTH_SHORT).show();
            mTimerText2.setText(String.valueOf(timer2.getTimeToGo()));
        }

        @Override
        public void onTickEvent() {
            mTimerText2.setText(String.valueOf(timer2.getTimeToGo()));
        }
    };

    public void onButtonClicked() {
        timer1.startTimer();
        timer2.startTimer();
    }
}
