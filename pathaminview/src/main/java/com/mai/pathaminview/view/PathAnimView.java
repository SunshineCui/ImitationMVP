package com.mai.pathaminview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Billy_Cui
 * on 2017/1/6.
 * github: https://github.com/SunshineCui
 */

public class PathAnimView extends View {
    
    protected Path mSourcePath;//需要做动画的源Path
    protected Path mAnimPath;//用于绘制动画的Path
    protected Paint mPaint;
    protected int mColorBg = Color.GRAY;//背景色
    protected int mColorFg = Color.WHITE;//前景色 填充色
    protected PathAnimHelper mPathAnimHelper;//path动画工具类
    
    protected int mPaddingLeft,mPaddingTop;

    public PathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PathAnimView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PathAnimView(Context context) {
        this(context,null);
    }

    /**
     * GET SET FUNC
     */
    
    public Path getAnimPath() {
        return mAnimPath;
    }

    public void setAnimPath(Path animPath) {
        mAnimPath = animPath;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public PathAnimView setPaint(Paint paint) {
        mPaint = paint;
        return this;
    }

    public int getColorBg() {
        return mColorBg;
    }

    public PathAnimView setColorBg(int colorBg) {
        mColorBg = colorBg;
        return this;
    }

    public int getColorFg() {
        return mColorFg;
    }

    public PathAnimView setColorFg(int colorFg) {
        mColorFg = colorFg;
        return this;
    }

    public PathAnimHelper getPathAnimHelper() {
        return mPathAnimHelper;
    }

    public PathAnimView setPathAnimHelper(PathAnimHelper pathAnimHelper) {
        mPathAnimHelper = pathAnimHelper;
        return this;
    }

    public Path getSourcePath() {
        return mSourcePath;
    }

    /**
     * 这个方法可能会经常用到,用于设置源path
     * 
     * @param sourcePath
     */
    public PathAnimView setSourcePath(Path sourcePath) {
        mSourcePath = sourcePath;
        initAnimHelper();
        return this;
    }

    /**
     * INIT FUNC
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        
        //动画路径只要初始化即可
        mAnimPath = new Path();
        
        //初始化动画帮助类
        initAnimHelper();
    }

    /**
     * 初始化动画帮助类
     */
    private void initAnimHelper() {
        mPathAnimHelper = getInitAnimHelper();
    }

    /**
     * 子类可通过重写这个方法,返回自定义的animHelper
     * 
     */
    protected PathAnimHelper getInitAnimHelper(){
        return new PathAnimHelper(this, mSourcePath, mAnimPath);
    }

    /**
     * draw FUNC 
     */
    @Override       
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //平移
        canvas.translate(mPaddingLeft,mPaddingTop);
        
        //先绘制底
        mPaint.setColor(mColorBg);
        canvas.drawPath(mSourcePath,mPaint);
        
        //在绘制前景,mAnimPath不断变化,不断重绘view的话,就会有动画效果
        mPaint.setColor(mColorFg);
        canvas.drawPath(mAnimPath,mPaint);
    }
    
    
}
