package com.jueye.entity.qa;

import com.jueye.db.mapper.RowMapper;
import com.jueye.util.bean.BeanManager;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 问答记录
 * Created by soledede.weng on 2016-11-07.
 */
public class QA implements Serializable, RowMapper<QA> {
    private static String qaSql = "insert into qa(question,answer,session_id,create_time,user_name) values(?,?,?,?,?)";

    private String question;
    private String answer;
    private Long createTime;
    private String userName="default";

    public QA() {
    }

    public QA(String question) {
        this.question = question;
    }

    public QA(String question, String answer,String userName) {
        if (createTime == null) this.createTime = System.currentTimeMillis();
        this.question = question;
        this.answer = answer;
        this.userName=userName;
    }

    public Long getCreateTime() {
        if (createTime == null) return System.currentTimeMillis();
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        if (createTime == null) this.createTime = System.currentTimeMillis();
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        if (createTime == null) this.createTime = System.currentTimeMillis();
        this.answer = answer;
    }


    @Override
    public QA mapRow(ResultSet rs) throws SQLException {
        return null;
    }


    public void persistToDB() {
        try {
            BeanManager.sessionDBHelper.executeSQL(qaSql, this.getQuestion(), this.getAnswer(), "", new Timestamp(this.getCreateTime()),this.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

