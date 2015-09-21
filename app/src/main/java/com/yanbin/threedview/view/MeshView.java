package com.yanbin.threedview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yanbin.threedview.R;


public class MeshView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private GLES20Renderer mRenderer;
    private int mColor;
    private int meshFile;
    private Context context;

    public MeshView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        final int GL_VERSION = 2;
        setEGLContextClientVersion(GL_VERSION);
        setPreserveEGLContextOnPause(true);

        GLES20Renderer renderer = new GLES20Renderer(context, meshFile);
        renderer.setColor(mColor);
        setRenderer(renderer);
    }


    public MeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MeshView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.MeshView_shapeColor:
                    mColor = a.getColor(attr, 0);
                    break;
                case R.styleable.MeshView_meshFile:
                    meshFile = a.getResourceId(attr, 0);
                    break;
            }
        }
        a.recycle();

        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = (x - mPreviousX) ;
                float dy = (y - mPreviousY) ;

                mRenderer.setYAngle((dx * TOUCH_SCALE_FACTOR));
                mRenderer.setXAngle((dy * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


    @Override
    public void setRenderer(Renderer renderer){
        super.setRenderer(renderer);
        this.mRenderer = (GLES20Renderer)renderer;
    }


}
