package com.hxj.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Play2048Group play2048Group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play2048Group = findViewById(R.id.play2048);
        Button top = findViewById(R.id.btn_top);
        Button lower = findViewById(R.id.btn_lower);
        Button left = findViewById(R.id.btn_left);
        Button right = findViewById(R.id.btn_right);

        top.setOnClickListener(this);
        lower.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top:
                play2048Group.up();
                break;
            case R.id.btn_lower:
                play2048Group.lower();
                break;
            case R.id.btn_left:
                play2048Group.left();
                break;
            case R.id.btn_right:
                play2048Group.right();
                break;
        }
    }
}
