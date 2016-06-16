package com.xyz.mvpdemo.model.bean;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/29 20:14
 * 邮箱：xyz@163.com
 */
public class News {

    public String date;
    public List<Stories> stories;
    public class Stories {
        public String ga_prefix;
        public String id;
        public String title;
        public String type;
        public List<String> images;
    }
}
