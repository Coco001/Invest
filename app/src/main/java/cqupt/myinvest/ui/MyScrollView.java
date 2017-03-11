package cqupt.myinvest.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 自定义viewgroup，实现滑动的时候可以超出上下屏幕一段距离.
 */

public class MyScrollView extends ScrollView {
    private View mChildView;
    private int mLastY;
    private Rect normal = new Rect();//用于记录临界状态的左、上、右、下
    private boolean isFinishAnimation = true;//是否动画结束

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //加载完成，获取子控件
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mChildView = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mChildView == null || !isFinishAnimation) {
            return super.onTouchEvent(ev);
        }
        int eventY = (int) ev.getY();//获取当前的y轴坐标
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = eventY;
                break;

            case MotionEvent.ACTION_HOVER_MOVE:
                int dy = eventY - mLastY;//微小的移动量
                if (isNeedMove()) {
                    if (normal.isEmpty()) {
                        //记录了childView的临界状态的左、上、右、下
                        normal.set(mChildView.getLeft(),
                                mChildView.getTop(),
                                mChildView.getRight(),
                                mChildView.getBottom());
                    }
                    //重新布局
                    mChildView.layout(mChildView.getLeft(),
                            mChildView.getTop() + dy / 2,
                            mChildView.getRight(),
                            mChildView.getBottom() + dy / 2);
                }
                mLastY = eventY;//重新赋值
                break;

            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    //使用平移动画
                    int translateY = mChildView.getBottom() - normal.bottom;

                    /*ObjectAnimator animator = ObjectAnimator.ofFloat(mChildView, "translateY", 0, -translateY);
                    animator.setDuration(200);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isFinishAnimation = true;
                            mChildView.clearAnimation();//清除动画
                            //清除normal的数据
                            normal.setEmpty();
                        }
                    });*/
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -translateY);
                    translateAnimation.setDuration(200);

                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            mChildView.clearAnimation();//清除动画
                            //重新布局
                            mChildView.layout(normal.left, normal.top, normal.right, normal.bottom);
                            //清除normal的数据
                            normal.setEmpty();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //启动动画
                    mChildView.startAnimation(translateAnimation);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    private boolean isNeedMove() {
        int childMeasuredHeight = mChildView.getMeasuredHeight();//获取子视图的高度
        int scrollViewMeasuredHeight = this.getMeasuredHeight();//获取布局的高度

        int dy = childMeasuredHeight - scrollViewMeasuredHeight; //dy >= 0

        int scrollY = this.getScrollY();//获取用户在y轴方向上的偏移量 （上 + 下 -）
        if (scrollY <= 0 || scrollY >= dy) {
            return true;//按照我们自定义的MyScrollView的方式处理
        }
        //其他处在临界范围内的，返回false。表示仍按照ScrollView的方式处理
        return false;
    }
}
