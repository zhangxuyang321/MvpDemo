package com.xyz.mvpdemo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.xyz.mvpdemo.base.MyApplication;
import com.xyz.mvpdemo.presenters.PresenterFactory;
import com.xyz.mvpdemo.view.fragment.FragmentFactory;

/**
 * 应用介绍：应用采用的是M参考谷歌官方的MVP架构，view和presenter的创建都用到了工厂模式，本应用中的许多动画都参考了
 * 许多的开源项目，会在具体使用的地方说明，有些 地方可能会有漏写，如果有雷同请指出，谢谢；
 * 参考：Activity的动画参考了泡在网上的日子http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0204/2415.html
 * 注：代码中会出现 assert 判断一个view是否为null这个是在更新SDK后出现的根据studio提示生成的，具体还没有学习
 *     Demo中不同模块之间其实很相似，但是考虑到可能实际应用中每个模块会有不同，每个模块都对应自己的view，自己的presenter
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int TYPE = FragmentFactory.NEWS_FG;       //Fragment工厂对应生产标记
    private String tag = "news";                      //fragment对应标记
    private boolean enterintoAnimation;               //动画执行标记，只有在Activity创建的时候执行
    private Toolbar toolbar;
    private FrameLayout fg_content;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.addActivity(this);
        if (savedInstanceState == null) {
            enterintoAnimation = true;
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fg_content = (FrameLayout) findViewById(R.id.fg_content);

        //默认显示新闻fragment
        replaceFragment(TYPE, tag);
    }


    @Override
    public void onBackPressed() {
        //解决返回键关闭主界面侧栏与退出应用时的冲突
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (enterintoAnimation) {
            enterintoAnimation = false;
            startEnterIntoAnimation();
        }
        return true;
    }

    /**
     * 主界面具体的执行动画的方法
     */
    private void startEnterIntoAnimation() {
        int actionbarSize = toolbar.getHeight();  //得到toolbar高度
        int contentSize = fg_content.getHeight(); //得到fragment容器的高度
        toolbar.setTranslationY(-actionbarSize);  //将toolbar移除界面
        fg_content.setTranslationY(contentSize);  //将fragment容器移除界面

        //同时执行动画
        toolbar.animate()
                .translationY(0)     //平移到原位置
                .setDuration(500)    //动画执行时间
                .setStartDelay(300); //动画延时多少毫秒在执行
        fg_content.animate()
                .translationY(0)
                .setDuration(500)
                .setStartDelay(600);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.drawer_news:
                TYPE = FragmentFactory.NEWS_FG;
                tag = "news";
                break;
            case R.id.drawer_movie:
                TYPE = FragmentFactory.MOVIES_FG;
                tag = "movie";
                break;
            case R.id.drawer_img:
                TYPE = FragmentFactory.IMG_FG;
                tag = "img";
                break;
        }
        replaceFragment(TYPE, tag);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void replaceFragment(int type, String tag) {

        PresenterFactory.createPresenter(type, FragmentFactory.createFragment(type));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fg_content, FragmentFactory.createFragment(type), tag)
                .commit();
    }


    /**
     * 返回键退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            assert drawer != null;
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if ((System.currentTimeMillis() - exitTime) > 3000) {
                Snackbar.make(coordinatorLayout, "再按一次退出应用", Snackbar.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.exitAll();
    }
}
