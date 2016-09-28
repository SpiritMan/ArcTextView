package com.yolocc.arctextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yolocc.arctextview.ArcTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArcTextView textView = (ArcTextView) findViewById(R.id.text);
        textView.setArcBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }
}
