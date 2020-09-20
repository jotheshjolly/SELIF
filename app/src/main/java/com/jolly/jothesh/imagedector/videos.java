package com.jolly.jothesh.imagedector;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class videos extends AppCompatActivity {


    WebView webView;
    //ProgressBar progressBar;
    ImageView back_btn;

    //String video_url = "PLd0sYhqUtnFTCT9joH7sIWSngx1PqFpSo", html = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        webView = (WebView) findViewById(R.id.youtube);
        // progressBar = (ProgressBar) findViewById(R.id.progressBar);

       /* if (video_url.equalsIgnoreCase("")) {
            finish();
            return;
        }

        WebSettings ws = webView.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.reload();

        //html = getHTML(video_url);


        /*if (networkUtil.isConnectingToInternet(Youtube.this)) {
            html = getHTML(video_url);
        } else {
            html = "" + getResources().getString(R.string.The_internet_connection_appears_to_be_offline);
           // CustomToast.animRedTextMethod(Act_VideoPlayer.this, getResources().getString(R.string.The_internet_connection_appears_to_be_offline));
        }*/

        //webView.loadData(html, "text/html", "UTF-8");
        webView.loadUrl("https://youtu.be/BsVq5R_F6RA");

        WebClientClass webViewClient = new WebClientClass();
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webView.loadData("", "text/html", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            webView.loadData("", "text/html", "UTF-8");
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class WebClientClass extends WebViewClient {
        android.widget.ProgressBar ProgressBar = null;

        /*WebClientClass(ProgressBar progressBar) {
            //ProgressBar = progressBar;
        }*/

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //ProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //ProgressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // LogShowHide.LogShowHideMethod("webview-click :", "" + url.toString());
            //view.loadUrl(getHTML(video_url));
            return true;
        }
    }

    /*public String getHTML(String videoId) {
        String html = "<iframe class=\"youtube-player\" " + "style=\"border: 0; width: 100%; height: 96%;"
                + "padding:0px; margin:0px\" " + "id=\"ytplayer\" type=\"text/html\" "
                + "src=\"http://www.youtube.com/embed/" + videoId
                + "?&theme=dark&autohide=2&modestbranding=1&showinfo=0&autoplay=1\fs=0\" frameborder=\"0\" "
                + "allowfullscreen autobuffer " + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
        //LogShowHide.LogShowHideMethod("video-id from html url= ", "" + html);
        return html;
    }*/


   /* public String getHTML(String videoId) {
        String html = "<iframe id=\"ytplayer\" type=\"text/html\" width=\"100%\" height=\"100%\"\n" +
                "src=\"https://www.youtube.com/embed/?listType=playlist&list="+video_url+"\n" +
                "frameborder=\"0\" allowfullscreen>";
        //LogShowHide.LogShowHideMethod("video-id from html url= ", "" + html);
        return html;
    }*/


    public void play(View view) {
        //webView.loadData(html, "text/html", "UTF-8");
        webView.loadUrl("https://youtu.be/BsVq5R_F6RA");
    }
}
