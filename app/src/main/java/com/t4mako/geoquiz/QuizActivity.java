package com.t4mako.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.QuoteSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private TextView mQuestionText;
    private boolean[] IsAnswered;
    private boolean[] IsCheated;
    private int rightCount = 0;
    private int wrongCount = 0;
    private int cheatCount = 0;
    private int mCurrentIndex = 0;
    private Button mAnswerButton;
    private static final String TAG = "QuizActivity";
    private static final int CHEAT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate(Bundle) called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //String[] Questions = getResources().getStringArray(R.array.question);
        //String[] Answers = getResources().getStringArray(R.array.answer);

        String[] json = getResources().getStringArray(R.array.question);
        String[] Questions = new String[json.length];
        String[] Answers = new String[json.length];
        for (int i = 0;i < json.length;i++) {
            Question question = JSON.parseObject(json[i], Question.class);
            Questions[i] = question.getQuestion();
            Answers[i] = question.getAnswer();
        }

        int questionCount = Questions.length;   //问题个数
        IsAnswered = new boolean[questionCount]; //存储是否回答的数组
        IsCheated = new boolean[questionCount];
        for (int i = 0; i < questionCount; i++) {
            IsAnswered[i] = false;
            IsCheated[i] = false;
        }

        //获取组件
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mQuestionText = findViewById(R.id.question_text_view);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);
        mAnswerButton = findViewById(R.id.btnAnswer);

        //savedInstanceState不为空，取出保存的信息
        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt("current");
            IsAnswered = savedInstanceState.getBooleanArray("btnStatus");
            IsCheated = savedInstanceState.getBooleanArray("IsCheated");
            rightCount = savedInstanceState.getInt("rightCount");
            wrongCount = savedInstanceState.getInt("wrongCount");
            cheatCount = savedInstanceState.getInt("chatCount");
            initButtonStatus(); //设置当前回答问题按钮状态
        }

        //设置问题文本
        mQuestionText.setText(Questions[mCurrentIndex]);

        //True按钮点击事件
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsAnswered[mCurrentIndex]) return;
                String answer = Answers[mCurrentIndex]; //获取答案
                IsAnswered[mCurrentIndex] = true;
                if (answer.equals("true")) {
                    Toast.makeText(QuizActivity.this, "回答正确" + (IsCheated[mCurrentIndex] ? "\n你已偷看过答案": ""), Toast.LENGTH_SHORT).show();
                    rightCount++;
                } else {
                    Toast.makeText(QuizActivity.this, "回答错误" + (IsCheated[mCurrentIndex] ? "\n你已偷看过答案": ""), Toast.LENGTH_SHORT).show();
                    wrongCount++;
                }
                initButtonStatus();
            }
        });
        //False按钮点击事件
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsAnswered[mCurrentIndex]) return;
                String answer = Answers[mCurrentIndex];
                IsAnswered[mCurrentIndex] = true;
                if (answer.equals("true")) {
                    Toast.makeText(QuizActivity.this, "回答错误" + (IsCheated[mCurrentIndex] ? "\n你已偷看过答案": ""), Toast.LENGTH_SHORT).show();
                    wrongCount++;
                } else {
                    Toast.makeText(QuizActivity.this, "回答正确" + (IsCheated[mCurrentIndex] ? "\n你已偷看过答案": ""), Toast.LENGTH_SHORT).show();
                    rightCount++;
                }
                initButtonStatus();
            }
        });

        //下一题按钮
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mCurrentIndex + 1;
                if (mCurrentIndex == questionCount) {
                    mCurrentIndex = mCurrentIndex - 1; //防止越界
                    if (rightCount + wrongCount == questionCount) {
                        String result = "共答对了" + rightCount + "题\n" +
                                "答错了" + wrongCount + "题\n" +
                                "正确率为：" + (float) rightCount / questionCount * 100 + "%\n" +
                                "查看答案次数为：" + cheatCount;

                        Toast.makeText(QuizActivity.this, result, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(QuizActivity.this, ExitActivity.class);
                        intent.putExtra("Account", result);
                        startActivity(intent);
                        finish();
                    }
                }
                mQuestionText.setText(Questions[mCurrentIndex]);
                initButtonStatus();
            }
        });
        //上一题按钮
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更改mCurrentIndex
                mCurrentIndex = (mCurrentIndex - 1);
                if (mCurrentIndex < 0) mCurrentIndex = 0; //防止越界
                //设置文本
                mQuestionText.setText(Questions[mCurrentIndex]);
                initButtonStatus();
            }

        });

        //作弊按钮
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取出题目和答案
                String answer = Answers[mCurrentIndex];
                String question = Questions[mCurrentIndex];
                //创建QuizActivity类和AnswerActivity类的连接信封
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                //在intent连接信封中附加消息
                intent.putExtra("QuestionAnswer", question + "\n答案为: " + answer);
                //startActivity(intent);
                //从子activity获取返回信息
                startActivityForResult(intent, CHEAT_CODE);
            }
        });
        //Log.e("error","error");

    }
    private void initButtonStatus() {
        mTrueButton.setEnabled(!IsAnswered[mCurrentIndex]);
        mFalseButton.setEnabled(!IsAnswered[mCurrentIndex]);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //将当前问题的指针与是否回答问题的数组保存
        savedInstanceState.putInt("current",mCurrentIndex);
        savedInstanceState.putBooleanArray("btnStatus",IsAnswered);
        savedInstanceState.putInt("rightCount",rightCount);
        savedInstanceState.putInt("wrongCount",wrongCount);
        savedInstanceState.putBooleanArray("IsCheated",IsCheated);
        savedInstanceState.putInt("cheatCount",cheatCount);
    }

    //从子 activity 获取返回结果
    //在用户按后退键回到QuizActivity时， ActivityManager调用父activity的以下方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        boolean isCheat = intent.getBooleanExtra("isCheat", false);
        if (isCheat) {
            cheatCount++;
            IsCheated[mCurrentIndex] = true;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}