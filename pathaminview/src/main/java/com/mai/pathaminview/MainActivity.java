package com.mai.pathaminview;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mai.pathaminview.utils.SvgPathParser;
import com.mai.pathaminview.view.PathAnimView;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    PathAnimView mPathAnimView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPathAnimView = (PathAnimView) findViewById(R.id.pav);
        
        //SVG 转 path
        SvgPathParser svgPathParser = new SvgPathParser();

        String okPath = getString(R.string.penPath);

        try {
            Path path = svgPathParser.parsePath(okPath);
            mPathAnimView.setSourcePath(path);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        mPathAnimView.getPathAnimHelper().setAnimTime(10000);
        mPathAnimView.startAnim();
    }
}
