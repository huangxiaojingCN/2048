package com.hxj.a2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Play2048Group play2048Group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play2048Group = findViewById(R.id.play2048);
        ImageView top = findViewById(R.id.btn_top);
        ImageView lower = findViewById(R.id.btn_lower);
        ImageView left = findViewById(R.id.btn_left);
        ImageView right = findViewById(R.id.btn_right);

        final TextView score = findViewById(R.id.tv_score);

        top.setOnClickListener(this);
        lower.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);

        play2048Group.setOnGameOverListener(new Play2048Group.OnGameOverListener() {

            @Override
            public void onFinish(int maxValue, int x, int y) {
                score.setText("游戏得分： " + maxValue);
            }
        });
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
