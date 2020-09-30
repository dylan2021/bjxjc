package com.haocang.commonlib.gismap;

import android.os.Environment;
import android.util.Log;

import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.haocang.commonlib.config.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 创建时间：2019/3/7
 * 编 写 人：ShenC
 * 功能描述：
 */

public class CacheTiledServiceLayer extends ArcGISTiledMapServiceLayer {
    private String cachepath;
    private String layerURL;


    public CacheTiledServiceLayer(String initLayer,String path) {
        super(initLayer);
        this.layerURL =initLayer;
        this.cachepath = Environment.getExternalStorageDirectory().getPath()+"/"+ Config.APP_Code+"/map/"+path;

        File file = new File(cachepath);
        if (!file.exists()) {
            file.mkdir();
        }
    }



    @Override
    protected void retrieveNoDataTileETag() {
        // TODO 自动生成的方法存根
        super.retrieveNoDataTileETag();
    }


    //获取瓦片  如果这方法总是进不去，别想。肯定是图层初始化TileInfo数据添加的不对
    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        // TODO Auto-generated method stub
        byte[] bytes = null;
        //根据图层、行、列找本地数据
        bytes = getOfflineCacheFile(level, col, row);
        //如果本地数据为空，则调用网络数据。
        if (bytes == null) {
            String strUrl = layerURL + "/tile" + "/" + level + "/" + row + "/"
                    + col;
            HashMap<String, String> localHashMap = null;
            //bytes = com.esri.core.internal.b.a.a.a(strUrl, localHashMap);
            Log.i(Config.LogFlag,"没有缓存"+strUrl);
            bytes =com.esri.core.internal.io.handler.a.a(strUrl, localHashMap);
            if(bytes !=null){
                AddOfflineCacheFile(level, col, row, bytes);
            }

        }
        return bytes;
    }
    // 将图片保存到本地 目录结构可以随便定义 只要你找得到对应的图片
    private byte[] AddOfflineCacheFile(int level, int col, int row, byte[] bytes) {
        File file = new File(cachepath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File levelfile = new File(cachepath + "/" + level);
        if (!levelfile.exists()) {
            levelfile.mkdirs();
        }
        File colfile = new File(cachepath + "/" + level + "/" + col);
        if (!colfile.exists()) {
            colfile.mkdirs();
        }
        File rowfile = new File(cachepath + "/" + level + "/" + col + "/" + row
                + ".dat");
        if (!rowfile.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(rowfile);
                out.write(bytes);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bytes;

    }

    // 从本地获取图片
    private byte[] getOfflineCacheFile(int level, int col, int row) {
        byte[] bytes = null;
        File rowfile = new File(cachepath + "/" + level + "/" + col + "/" + row
                + ".dat");

        if (rowfile.exists()) {
            try {
                bytes = CopySdcardbytes(rowfile);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            bytes = null;
        }
        return bytes;
    }

    // 读取本地图片流
    public byte[] CopySdcardbytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[1024];

        int size = 0;

        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] bytes = out.toByteArray();
        return bytes;
    }
}
