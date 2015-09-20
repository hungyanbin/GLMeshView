package com.yanbin.threedview.view;

/**
 * Created by 彥彬 on 2015/9/20.
 */
public class Mesh {

    private float[] vertex;
    private short[] index;

    public Mesh(float[] vertex, short[] index){
        this.index = index;
        this.vertex = vertex;
    }

    public float[] getVertex() {
        return vertex;
    }

    public short[] getIndex() {
        return index;
    }
}
