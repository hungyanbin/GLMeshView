package com.yanbin.threedview.view.Mesh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MeshParser {

    private InputStream meshStream;
    private List<Float> vertices;
    private List<Short> vertexIndice;

    public MeshParser(InputStream meshStream){
        this.meshStream = meshStream;
    }

    public void parseVertex(){
        BufferedReader in = new BufferedReader(new InputStreamReader(meshStream));
        String line;

        vertices = new ArrayList<>();
        vertexIndice = new ArrayList<>();

        addDumpPoint();
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

    //because obj file is 1-based, so add one dump point for correct indexing
    private void addDumpPoint(){
        vertices.add(0f);
        vertices.add(0f);
        vertices.add(0f);
    }

    private void addVertexByLine(String line){
        final int pointCoorCount = 4;
        if(isNotVertex(line))
            return;

        String[] point = line.split(" ");
        if(point.length == pointCoorCount){
            vertices.add(Float.valueOf(point[1]));
            vertices.add(Float.valueOf(point[2]));
            vertices.add(Float.valueOf(point[3]));
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
            vertexIndice.add(Short.valueOf(point[1].split("/")[0]));
            vertexIndice.add(Short.valueOf(point[2].split("/")[0]));
            vertexIndice.add(Short.valueOf(point[3].split("/")[0]));
        }
    }

    private boolean isNotFace(String line){
        if(line.startsWith("f "))
            return false;
        else
            return true;
    }

    public Mesh getParseResult(){
        float[] vertex = new float[vertices.size()];
        short[] indice = new short[vertexIndice.size()];
        for(int i=0;i< vertices.size();i++)
            vertex[i] = vertices.get(i);

        for(int i=0;i< vertexIndice.size();i++)
            indice[i] = vertexIndice.get(i);

        return new Mesh(vertex, indice);
    }
}
