package com.yanbin.threedview.view;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLShape {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    private float[] mMVPMatrix;

    private static final int VERTEX_DATA_COUNT = 3;
    private static final int COLOR_DATA_COUNT = 4;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    private float color[] = null;

    private int indiceCount;
    private int vertexCount;

    private int mProgram;
    private int mDrawMode = GLES20.GL_TRIANGLE_FAN;

    private GLShape(){

    }

    public void setMVPMatrix(float[] matrix){
        final int ELEMENT_COUNT = 16;
        if(matrix.length != ELEMENT_COUNT)
            throw new GLRenderException("MVP Matrix element count must be 16");
        mMVPMatrix = matrix;
    }

    public void draw(){
        GLES20.glUseProgram(mProgram);

        int positionHandle = getAttrHandle(mProgram, GLShaderFactory.FIELD_POSITION);
        int colorHandle = getUniformHandle(mProgram, GLShaderFactory.FIELD_COLOR_VARYING);
        int mMVPMatrixHandle = getUniformHandle(mProgram, GLShaderFactory.FIELD_MATRIX_MVP);

        GLES20.glVertexAttribPointer(positionHandle, VERTEX_DATA_COUNT,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        GLES20.glDrawElements(mDrawMode, indiceCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private int getAttrHandle(int program, String argName){
        int handle = GLES20.glGetAttribLocation(program, argName);
        checkFieldHandle(handle, "attr", argName);
        return handle;
    }

    private int getUniformHandle(int program, String fieldName){
        int handle = GLES20.glGetUniformLocation(program, fieldName);
        checkFieldHandle(handle, "uniform", fieldName);
        return handle;
    }

    private void checkFieldHandle(int handle, String type, String fieldName){
        final int HANDLE_ERROR = -1;
        if(handle == HANDLE_ERROR)
            throw new GLRenderException("Wrong " + type + " handle name : " + fieldName);
    }

    public static class Builder{
        private FloatBuffer vertices = null;
        private ShortBuffer indices = null;
        private float color[];
        private int indicesCount;
        private int vertexCount;

        private int mDrawMode = GLES20.GL_TRIANGLE_FAN;

        public Builder(Mesh mesh){
            this.vertices = floatArrayToBuffer(mesh.getVertex());
            this.indices = shortArrayToBuffer(mesh.getIndex());

            vertexCount = mesh.getVertex().length;
            indicesCount = mesh.getIndex().length;

            if(vertexCount % VERTEX_DATA_COUNT != 0)
                throw new IllegalArgumentException("coords length must multiples of 3, length : " + vertexCount);
            if(vertexCount % VERTEX_DATA_COUNT != 0)
                throw new IllegalArgumentException("index length must multiples of 3, length : " + vertexCount);


        }


        public Builder color(float color[]){
            if(color.length % COLOR_DATA_COUNT != 0)
                throw new IllegalArgumentException("color length must multiples of 4, length :" + color.length);
            this.color = color;
            return this;
        }

        public Builder drawMode(int mDrawMode){
            if(mDrawMode != GLES20.GL_TRIANGLE_FAN &&
                    mDrawMode != GLES20.GL_TRIANGLE_STRIP &&
                    mDrawMode != GLES20.GL_TRIANGLES)
                throw new IllegalArgumentException("wrong value for drawMode : " + mDrawMode);
            this.mDrawMode = mDrawMode;
            return this;
        }

        public GLShape build(){
            GLShape shape = new GLShape();
            shape.mProgram = GLShaderFactory.getDefaultShader();
            shape.vertexBuffer = vertices;
            shape.indexBuffer = indices;
            shape.mDrawMode = mDrawMode;
            shape.color = color;
            shape.indiceCount = indicesCount;
            shape.vertexCount = vertexCount / VERTEX_DATA_COUNT;

            return shape;
        }

        private FloatBuffer floatArrayToBuffer(float[] array){
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_FLOAT);
            byteBuffer.order(ByteOrder.nativeOrder());

            FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
            floatBuffer.put(array);
            floatBuffer.position(0);
            return floatBuffer;
        }

        private ShortBuffer shortArrayToBuffer(short[] array){
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length * BYTES_PER_SHORT);
            byteBuffer.order(ByteOrder.nativeOrder());

            ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
            shortBuffer.put(array);
            shortBuffer.position(0);
            return shortBuffer;
        }
    }
}
