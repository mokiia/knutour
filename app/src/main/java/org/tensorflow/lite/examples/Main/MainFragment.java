package org.tensorflow.lite.examples.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.Common.PageInfo;
import org.tensorflow.lite.examples.R;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        final WebView webView = view.findViewById(R.id.webview1);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.loadUrl(PageInfo.INDEX_PAGE);

        return view;

    }
}
