package com.xyz.mvpdemo.model.bean;

/**
 * 作者：xy_z on 2016/5/21 13:47
 * 邮箱：xyz@163.com
 */
public class UserBean {
    public String state;
    public DataEntity data;

    @Override
    public String toString() {
        return "UserBean{" +
                "state='" + state + '\'' +
                ", data=" + data +
                '}';
    }

    public class DataEntity {
        public String bbtype;
        public String bbid;
        public String bbdyid;

        @Override
        public String toString() {
            return "DataEntity{" +
                    "bbtype='" + bbtype + '\'' +
                    ", bbid='" + bbid + '\'' +
                    ", bbdyid='" + bbdyid + '\'' +
                    '}';
        }
    }
}
