package com.yanbin.threedview.view;

import android.opengl.GLES20;

public class GLShaderFactory {

    public static final String FIELD_POSITION = "aPosition";
    public static final String FIELD_COLOR_VARYING = "vColor";
    public static final String FIELD_MATRIX_MVP = "uMVPMatrix";

    private static final int STATUS_FAIL = 0;


    private static final String SHADER_CODE_FRAGMENT =
            "precision mediump float;" +
            "uniform vec4 " + FIELD_COLOR_VARYING + ";"  +
            "void main(){" +
            "   gl_FragColor = " + FIELD_COLOR_VARYING + ";"  +
            "}";

    private static final String SHADER_CODE_VERTEX_MVP =
            "uniform mat4 " + FIELD_MATRIX_MVP + ";" +
            "attribute vec4 " + FIELD_POSITION  + ";" +
            "void main() {" +
                    "  gl_Position = " + FIELD_MATRIX_MVP + " * " + FIELD_POSITION  + ";" +
        "}";


    public static int getDefaultShader(){
        GLShaderFactory shaderFactory = new GLShaderFactory();
        return shaderFactory.createShaderProgram(SHADER_CODE_VERTEX_MVP, SHADER_CODE_FRAGMENT);
    }

    private int loadShader(int type, String shaderCode){
        int shaderHandle = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shaderHandle, shaderCode);
        GLES20.glCompileShader(shaderHandle);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == STATUS_FAIL)
        {
            String errorLog = GLES20.glGetShaderInfoLog(shaderHandle);
            GLES20.glDeleteShader(shaderHandle);
            throw new RuntimeException("shader compile error!! " + errorLog);
        }

        return shaderHandle;
    }

    private int createShaderProgram(String vertexShaderCode, String fragmentShaderCode){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int shaderProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);

        GLES20.glLinkProgram(shaderProgram);

        // Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(shaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);

        // If the link failed, delete the program.
        if (linkStatus[0] == STATUS_FAIL)
        {
            String errorLog = GLES20.glGetShaderInfoLog(shaderProgram);
            GLES20.glDeleteProgram(shaderProgram);
            throw new GLRenderException("shader linking error!! " + errorLog);
        }

        return shaderProgram;
    }
}
