package com.yanbin.threedview.view;

/**
 * Created by 彥彬 on 2015/4/5.
 */
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.IOException;

public class GLES20Renderer extends GLRenderer {

    private GLShape.Builder cubeBuilder;
    private float COLOR_DEFAULT[] = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] shapeColor = COLOR_DEFAULT;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mAccumulatedRotation = new float[16];

    private Context context;

    public GLES20Renderer(Context context){
        this.context = context;
    }

    @Override
    public void onCreate(int width, int height,
                         boolean contextLost) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glFrontFace(GLES20.GL_CCW);
       // GLES20.glCullFace(GLES20.GL_BACK);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        initProjectionMatrix(width, height);
            cubeBuilder = new GLShape.Builder(GLShapeFactory.getCube());
            cubeBuilder.drawMode(GLES20.GL_TRIANGLES)
                .color(shapeColor);
        Matrix.setIdentityM(mAccumulatedRotation, 0);
    }

    public void setColor(int color){
        int red = (color & 0xFF0000) >> 16;
        int green = (color & 0x00FF00) >> 8;
        int blue = color & 0x0000FF;

        final float SCALE = 0xFF;
        float f_red = red/SCALE;
        float f_green = green/SCALE;
        float f_blue = blue/SCALE;
        shapeColor = new float[]{f_red, f_green, f_blue, 1.0f};
    }

    private void initProjectionMatrix(int width, int height){
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 20.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);

        updateTouchRotationMatrix();

        float[] mTransMatrix = new float[16];
        Matrix.setIdentityM(mTransMatrix, 0);
        float[] mMVPMatrix = getMVPMatrix(mTransMatrix);

        GLShape cube = cubeBuilder.MVPMatrix(mMVPMatrix).build();
        cube.draw();
    }

    private void updateTouchRotationMatrix(){
        float[] mRotationMatrix = new float[16];

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.rotateM(mRotationMatrix, 0, xAngle, -1.0f, 0f, 0.0f);
        Matrix.rotateM(mRotationMatrix, 0, yAngle, 0, 1.0f, 0.0f);

        yAngle = 0;
        xAngle = 0;

        Matrix.multiplyMM(mAccumulatedRotation, 0, mRotationMatrix, 0, mAccumulatedRotation, 0);
    }

    private float[] getMVPMatrix(float[] matrix){
        float[] mMVPMatrix = new float[16];
        float[] mViewMatrix = getDefaultViewMatrix();

        Matrix.multiplyMM(matrix, 0, mAccumulatedRotation, 0, matrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, matrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        return mMVPMatrix;
    }

    private float[] mViewMatrix = null;
    private float[] getDefaultViewMatrix(){
        if(mViewMatrix == null) {
            float eyeX = 0f;
            float eyeY = 0f;
            float eyeZ = -4f;
            float centerX = 0f;
            float centerY = 0f;
            float centerZ = 0f;
            float upX = 0f;
            float upY = 1f;
            float upZ = 0f;

            mViewMatrix = new float[16];
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
        }

        return mViewMatrix;
    }

    private volatile float yAngle;
    private volatile float xAngle;

    public void setYAngle(float angle) {
        yAngle = angle;
    }

    public void setXAngle(float mXAngle) {
        this.xAngle = mXAngle;
    }
}
