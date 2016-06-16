package com.xyz.mvpdemo.Util;

/**
 * 作者：xy_z on 2016/5/21 14:52
 * 邮箱：xyz@163.com
 * 描述：新闻数据是通过知乎日报获取的，获取与共享之行为或有侵犯知乎权益的嫌疑。若被告知需停止共享与使用，本人会及时删除此页面与整个项目。
 *      电影数据是通过聚合数据提供，再次感谢聚合提供的免费数据
 */
public class Port {

    //登录，获取用户标示
    public static final String user = "http://https//api.huaban.com/favorite/food_drink?limit=20";

    //获取每日新闻
    public static final String news = "http://news-at.zhihu.com/api/4/news/latest";

    //获取新闻详情
    public static final String newsdetails = "http://news-at.zhihu.com/api/4/news/";

    //获取电影
    public static final String movies = "http://op.juhe.cn/onebox/movie/pmovie?key=101ed3a72665244e147c166362a0c48c&city=北京";

    //获取图片
    public static final String img = "http://api.huaban.com/favorite/quotes?limit=10";

    //图片根目录请求格式
    //_fw320sf 正常图
    //_sq75sf  小图
    //_fw658   大图
    public static final String imgurl = "http://img.hb.aicdn.com/";

    //获取更多图片 上拉加载
    //max为上次加载图片的最好一张的pin_id的值 然后新加载的第一个会跟上一次数据的最后一个所以得去除重复
    public static final String imgmore= " http://api.huaban.com/favorite/quotes?max=%s&limit=5";

}
