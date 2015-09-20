package com.yanbin.threedview;

import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.yanbin.threedview.view.MeshParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 彥彬 on 2015/9/19.
 */
public class MeshParserTest extends AndroidTestCase {

    public void test_parse() throws Exception{
        AssetManager assetManager = getContext().getAssets();
        InputStream inputStream = assetManager.open("wt_teapot.obj");

        MeshParser meshParser = new MeshParser(inputStream);


        Log.i("test", "data:" + meshParser.parseVertex());
        assertTrue(true);
    }
}
