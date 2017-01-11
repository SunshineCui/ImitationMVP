package com.mai.pathaminview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * * 介绍：一个自定义View Path动画的工具类
 * <p>
 * 一个SourcePath 内含多段（一段）Path，循环取出每段Path，并做一个动画,
 * <p>
 * 默认动画时间1500ms，无限循环
 * 可以通过构造函数修改这两个参数
 * <p>
 * 对外暴露 startAnim() 和 stopAnim()两个方法
 * <p>
 * 子类可通过重写onPathAnimCallback（）方法，对animPath进行再次操作，从而定义不同的动画效果
 * Created by Billy_Cui
 * on 2017/1/6.
 * github: https://github.com/SunshineCui
 */

public class PathAnimHelper {
    private static final String TAG = "PathAnimHelper";
    protected static final long mDefaultAnimTime = 1500;//默认动画总时间

    protected View mView;//执行动画的view
    protected Path mSourcePath;//源Path
    protected Path mAnimPath;//用于绘制动画的Path
    protected long mAnimTime;//动画一共的时间
    protected boolean mIsInfinite;//是否无限循环

    protected ValueAnimator mAnimator;//动画对象

    /**
     * INIT FUNC
     */
    public PathAnimHelper(View view, Path sourcePath, Path animPath) {
        this(view, sourcePath, animPath, mDefaultAnimTime, true);
    }

    public PathAnimHelper(View view, Path sourcePath, Path animPath, long animTime, boolean isInfinite) {
        if (view == null || sourcePath == null || animPath == null) {
            Log.e(TAG, "PathAnimHelper init error: view 、sourcePath、animPath can not be null");
            return;
        }
        mView = view;
        mSourcePath = sourcePath;
        mAnimPath = animPath;
        mAnimTime = animTime;
        mIsInfinite = isInfinite;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public Path getSourcePath() {
        return mSourcePath;
    }

    public PathAnimHelper setSourcePath(Path sourcePath) {
        mSourcePath = sourcePath;
        return this;
    }

    public Path getAnimPath() {
        return mAnimPath;
    }

    public PathAnimHelper setAnimPath(Path animPath) {
        mAnimPath = animPath;
        return this;
    }

    public long getAnimTime() {
        return mAnimTime;
    }

    public void setAnimTime(long animTime) {
        mAnimTime = animTime;
    }

    public ValueAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(ValueAnimator animator) {
        mAnimator = animator;
    }

    public boolean isInfinite() {
        return mIsInfinite;
    }

    public void setInfinite(boolean infinite) {
        mIsInfinite = infinite;
    }

    /**
     * 执行动画
     */
    public void startAnim() {
        startAnim(mView, mSourcePath, mAnimPath, mAnimTime, mIsInfinite);
    }

    /**
     * 一个SourcePath 内含多段path,循环取出每段path,并做一个动画
     * 自定义动画的总时间 和 是否循环
     *
     * @param view       需要做动画的自定义view
     * @param sourcePath 源Path
     * @param animPath   自定义view用这个Path做动画
     * @param animTime   动画一共的时间
     * @param isInfinite 是否无限循环
     */
    protected void startAnim(View view, Path sourcePath, Path animPath, long animTime, boolean isInfinite) {
        if (view == null || sourcePath == null || animPath == null) {
            return;
        }
        PathMeasure pathMeasure = new PathMeasure();
        //先重置一下需要显示动画的path
        animPath.reset();
        animPath.lineTo(0, 0);
        pathMeasure.setPath(sourcePath, false);
        //这里仅仅是为了 计算一下每一段的duration
        int count = 0;
        while (pathMeasure.getLength() != 0) {
            pathMeasure.nextContour();
            count++;
        }
        //经过上面这段计算durat码的折腾 需要重新初始化pathMeasure
        pathMeasure.setPath(sourcePath, false);
        loopAnim(view, sourcePath, animPath, animTime, pathMeasure, animTime / count, isInfinite);
    }

    /**
     * 循环取出每一段path,并执行动画
     *
     * @param view
     * @param sourcePath
     * @param animPath      自定义view用这个path做动画
     * @param totalDuration
     * @param pathMeasure   用于测量的pathMeasure
     * @param duration
     * @param isInfinite
     */
    protected void loopAnim(final View view, final Path sourcePath, final Path animPath, long totalDuration, final PathMeasure pathMeasure, long duration, final boolean isInfinite) {
        //动画正在运行的话,先stop.
        stopAnim();

        mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(duration);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //增加一个callback  便于子类重写
                onPathAnimCallback(view, sourcePath, animPath, pathMeasure, animator);
                //通知view刷新自己
                view.invalidate();
            }
        });

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                //每段path走完后,要补一下 某些现 animPath不满的情况
                pathMeasure.getSegment(0, pathMeasure.getLength(), animPath, true);

                //绘制完一条Path之后,要绘制吓一条
                pathMeasure.nextContour();
                //长度为0 说明一次循环结束
                if (pathMeasure.getLength() == 0) {
                    if (isInfinite) {//如果需要循环动画
                        animPath.reset();
                        animPath.lineTo(0, 0);
                        pathMeasure.setPath(sourcePath, false);
                    } else {
                        animation.end();
                    }
                }
            }
        });
        mAnimator.start();
    }

    /**
     * 用于子类继承 , 对animPath进行再次操作的函数
     * 
     * @param view
     * @param sourcePath
     * @param animPath
     * @param pathMeasure
     * @param animator
     */
    private void onPathAnimCallback(View view, Path sourcePath, Path animPath, PathMeasure pathMeasure, ValueAnimator animator) {
        float value = (float) animator.getAnimatedValue();
        //获取一个段落
        pathMeasure.getSegment(0,pathMeasure.getLength() * value ,animPath,true);
    }

    /**
     * 停止动画
     */
    private void stopAnim() {
        if (null != mAnimator && mAnimator.isRunning()) {
            mAnimator.end();
        }
    }
}
