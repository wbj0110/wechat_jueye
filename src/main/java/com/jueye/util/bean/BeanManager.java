package com.jueye.util.bean;


import com.jueye.db.DBHelper;
import com.jueye.entity.qa.QA;
import org.apache.zookeeper.server.SessionTracker;

/**
 * Created by soledede.weng on 2016-11-23.
 */
public class BeanManager {

    //session db管理
    public static DBHelper sessionDBHelper = new DBHelper<SessionTracker.Session>();

    //qa db管理
    public static DBHelper qaDBHelper = new DBHelper<QA>();


}
