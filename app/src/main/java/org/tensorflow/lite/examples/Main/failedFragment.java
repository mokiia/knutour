package org.tensorflow.lite.examples.Main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.Common.PageInfo;
import org.tensorflow.lite.examples.R;

public class failedFragment extends Fragment {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_failed, container, false);

        final WebView webView = view.findViewById(R.id.course);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.loadUrl(PageInfo.PAGE1);

        return view;
    }

}