package com.t4mako.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExitActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private Button btnRestart;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        //获得intent引用
        Intent intent = getIntent();
        //根据KEY取出value
        String TrueFalseAcc = intent.getStringExtra("Account");
        //获得文本框引用，设置文字
        TextView mTrueFalseAcc = findViewById(R.id.tvTrueFalseAcc);
        mTrueFalseAcc.setText(TrueFalseAcc);

        initView();
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ExitActivity.this, QuizActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView() {
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        btnExit = (Button) findViewById(R.id.btnExit);
    }
}
