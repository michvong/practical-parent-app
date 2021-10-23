package ca.cmpt276.titanium.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import ca.cmpt276.titanium.R;

public class MenuActivity extends AppCompatActivity {
    private int numOfAddedChildren; // will change
    Button flipCoinButton, timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numOfAddedChildren=10;//will change
        setupScrollAllChildren();

        flipCoinButton = findViewById(R.id.menuGoToFlipCoin);
        flipCoinButtonClick();
        timerButton = findViewById(R.id.menuGoToTimer);
        timerButtonClick();
    }

    private void flipCoinButtonClick(){
        flipCoinButton.setOnClickListener(view -> {
            Intent i = CoinActivity.makeLaunchIntent(MenuActivity.this);
            startActivity(i);
        });
    }

    private void timerButtonClick(){
        timerButton.setOnClickListener(view -> {
            Intent i = TimerActivity.makeLaunchIntent(MenuActivity.this);
            startActivity(i);
        });
    }

    private void setupScrollAllChildren() {
        TableRow scroll = findViewById(R.id.menuRow);
        for(int i =0; i<numOfAddedChildren;i++){
            View child = LayoutInflater.from(this).inflate(R.layout.child_icon,null);
            //onclick not working, fix if possible otherwise leave.
            scroll.addView(child);
        }
    }
}