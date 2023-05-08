package com.t4mako.geoquiz;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String answer;

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
