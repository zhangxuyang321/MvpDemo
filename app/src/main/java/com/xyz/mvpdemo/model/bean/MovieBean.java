package com.xyz.mvpdemo.model.bean;

import java.util.List;

/**
 * 作者：xy_z on 2016/6/2 13:50
 * 邮箱：xyz@163.com
 */
public class MovieBean {
    public String error_code;            //错误码
    public String reason;                //结果说明
    public Resultclass result;           //返回结果

    public class Resultclass {
        public List<ResultData> data;    //具体电影数据,分为正在上映和即将上映
        public String date;              //电影日期
        public String m_url;             //手机访问的更多电影信息的url
    }

    public class ResultData {
        public List<MovieInfo> data;         //电影数据集合，
    }

    public class MovieInfo {
        public String grade;             //电影评分
        public String gradeNum;          //电影评分渠道
        public String iconaddress;       //电影海报
        public String m_iconlinkUrl;     //手机访问的具体电影的地址
        public String subHead;           //今日上映影院
        public String tvTitle;           //电影名称
        public Story story;
    }

    public class Story {
        public StoryData data;
    }

    public class StoryData {
        public String storyBrief;        //电影剧情
    }
}
