package com.t4mako.geoquiz;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private Button cheat;
    boolean isCheat = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        //获得intent引用
        Intent intent = getIntent();

        if(savedInstanceState!=null){
            isCheat = savedInstanceState.getBoolean("isCheat");
            if(isCheat){
                //根据KEY取出value
                String message = intent.getStringExtra("QuestionAnswer");
                //获得文本框引用，设置文字
                TextView mAnswerText = findViewById(R.id.tvAnswer);
                mAnswerText.setText(message);
                //调用方法，确认作弊，将结果返回给QuizActivity
                isCheat = true;
                setAnswerShownResult(isCheat);
            }
        }

        //获取按钮
        cheat = findViewById(R.id.btnyes);

        cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据KEY取出value
                String message = intent.getStringExtra("QuestionAnswer");
                //获得文本框引用，设置文字
                TextView mAnswerText = findViewById(R.id.tvAnswer);
                mAnswerText.setText(message);
                //调用方法，确认作弊，将结果返回给QuizActivity
                isCheat = true;
                setAnswerShownResult(isCheat);
            }

        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent intent = new Intent();
        intent.putExtra("isCheat", isAnswerShown);
        // 实现子activity发送返回信息给父activity
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //将是否作弊的状态保存
        savedInstanceState.putBoolean("isCheat",isCheat);

    }
}
