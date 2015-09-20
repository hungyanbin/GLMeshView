package com.yanbin.threedview.view;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MeshParser {

    private InputStream meshStream;
    private List<Float> vertexData;
    private List<Short> indiceData;

    public MeshParser(InputStream meshStream){
        this.meshStream = meshStream;
    }

    public void parseVertex(){
        BufferedReader in = new BufferedReader(new InputStreamReader(meshStream));
        String line;

        vertexData = new ArrayList<>();
        indiceData = new ArrayList<>();

        try {
            while ((line = in.readLine()) != null) {
                addVertexByLine(line);
                addIndexByLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addVertexByLine(String line){
        final int pointCoorCount = 4;
        if(isNotVertex(line))
            return;

        String[] point = line.split(" ");
        if(point.length == pointCoorCount){
            vertexData.add(Float.valueOf(point[1]));
            vertexData.add(Float.valueOf(point[2]));
            vertexData.add(Float.valueOf(point[3]));
            Log.i("test", "add point:" + point[1] + ", " + point[2] + ", " + point[3]);

        }
    }

    private boolean isNotVertex(String line){
        if(line.startsWith("v "))
            return false;
        else
            return true;
    }

    private void addIndexByLine(String line){
        final int pointCoorCount = 4;
        if (isNotFace(line))
            return;

        String[] point = line.split(" ");
        if(point.length == pointCoorCount){
            indiceData.add(Short.valueOf(point[1].split("/")[0]));
            indiceData.add(Short.valueOf(point[2].split("/")[0]));
            indiceData.add(Short.valueOf(point[3].split("/")[0]));
            Log.i("test", "add index:" + point[1].split("/")[0] + ", " + point[2].split("/")[0] + ", " + point[3].split("/")[0]);
        }
    }

    private boolean isNotFace(String line){
        if(line.startsWith("f "))
            return false;
        else
            return true;
    }

    public Mesh getParseResult(){
        float[] vertex = new float[vertexData.size()];
        short[] indice = new short[indiceData.size()];
        for(int i=0;i<vertexData.size();i++)
            vertex[i] = vertexData.get(i);

        for(int i=0;i<indiceData.size();i++){
            indice[i] = indiceData.get(i);
        }

        Log.i("test", "vertex count:" + vertex.length);
        Log.i("test", "indice count:" + indice.length);

        return new Mesh(vertex, indice);
    }
}
