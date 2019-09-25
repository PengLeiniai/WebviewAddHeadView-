package com.n_add.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

public class MainActivity extends AppCompatActivity {

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadWebview("https://blog.csdn.net/wl724120268/article/details/78275686");
    }

    int last;
    int last1;
    boolean isDes = true;

    boolean isTop = false;
    LinearLayout main_view;
    boolean isTaskEnd = true;

    /**
     * 加载webview
     */
    private void loadWebview(String url) {
        TouchWebView mBridgeWebView = new TouchWebView(this);
        main_view = findViewById(R.id.main_view);
        view = findViewById(R.id.view);
        //打开其他应用时，弹窗咨询用户是否前往其他应用
        //拦截找不到相关页面的Scheme
        AgentWeb agentWeb = AgentWeb.with(this).setAgentWebParent(main_view, new LinearLayout.LayoutParams(-1, -1)).useDefaultIndicator(Color.parseColor("#DA9C29")).setWebViewClient(null).setWebChromeClient(null).setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DERECT)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .setWebView(mBridgeWebView).createAgentWeb().ready().go(url);


        final WebView webView = agentWeb.getWebCreator().getWebView();
        WebSettings webSettings = webView.getSettings();
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); // 根据cache-control决定是否从网络上取数据
        //是允许录屏截屏,才可以长安图片保存
        mBridgeWebView.setOnScrollChanged(new TouchWebView.OnScrollChanged() {
            @Override
            public void scroll(int l, int t, int oldl, int oldt) {

                boolean isTop = true;
                if (isTop && t < oldt) {
//                    Log.e("last", t + "    " + last);
                    if (last <= t) {
//                        Log.e("last", "true");
                        isTop = false;
                    } else {
//                        Log.e("last", "false");
                        isTop = true;
                    }
                    if (isTop) {
                        ch(true, -t);
                    }
                    last = t;
                } else {
//                    Log.e("last1", t + "    " + last1);
                    if (last1 <= t) {
//                        Log.e("last1", "true");
                        isTop = false;
                    } else {
//                        Log.e("last1", "false");
                        isTop = true;
                    }
                    if (!isTop) {
                        ch(false, -t);
                    }
                    last1 = t;
                }
            }
        });


    }

    int lastY;

    private void ch(boolean top, int t) {
        if (top) {
            if (t - lastY > dip2px(20)) {
                change(false);
            }
        } else {
            if (lastY - t > dip2px(20)) {
                change(true);
            }

        }
        lastY = t;
    }

    LinearLayout.LayoutParams layoutParams;

    boolean isTops = true;
    boolean isBotoom = false;

    private void change(boolean isHideHead) {
        layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        boolean isLoop = true;
        if (isHideHead) {
            if (isTops && !isBotoom) {
                isTops = false;
                isBotoom = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int index = 0;
                        while (index <= dip2px(150)) {
                            Message message = new Message();
                            message.obj = -index;
                            message.what = 2;
                            handler.sendMessage(message);
                            index++;
                            try {
                                if (index % 3 == 0) Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

        } else {
            if (!isTops && isBotoom) {
                isTops = true;
                isBotoom = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int index = -dip2px(150);
                        while (index <= 0) {
                            Message message = new Message();
                            message.obj = index;
                            message.what = 1;
                            handler.sendMessage(message);
                            index++;
                            try {
                                if (index % 3 == 0) Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("index", msg.what + "           " + msg.obj);
            layoutParams.topMargin = (int) msg.obj;
            view.setLayoutParams(layoutParams);
        }
    };


    public int dip2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}