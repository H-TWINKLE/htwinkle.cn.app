package cn.htwinkle.app.componet;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import cn.hutool.core.util.RandomUtil;

public class GradualView extends View {

    // 随机颜色变化
    public static final int COLOR_TAG = RandomUtil.randomInt(10, 255);

    private int animatedValue;
    private int colorEnd;
    private int colorStart;

    public GradualView(Context context) {
        super(context);
        init();
        requestLayout();
    }

    public GradualView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        requestLayout();
    }

    public GradualView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        requestLayout();
    }

    public GradualView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    public void init() {
        postInvalidate();
        ValueAnimator animator = ValueAnimator.ofInt(0, 255);
        animator.setDuration(10000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(animation -> {
            animatedValue = (int) animation.getAnimatedValue();
            if (animatedValue < 255) {
                colorStart = Color.rgb(COLOR_TAG, animatedValue, 255 - animatedValue);
                colorEnd = Color.rgb(animatedValue, 0, 255 - animatedValue);
            }
            invalidate();
        });
        animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        Paint paint = new Paint();
        LinearGradient backGradient = new LinearGradient(width, 0, 0, 0,
                new int[]{colorStart, colorEnd},
                new float[]{0, 1f}, Shader.TileMode.CLAMP);
        paint.setShader(backGradient);

        canvas.drawRect(0, 0, width, height, paint);
    }
}
