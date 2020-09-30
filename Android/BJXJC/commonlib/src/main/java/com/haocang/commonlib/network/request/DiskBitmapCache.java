package com.haocang.commonlib.network.request;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.haocang.commonlib.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public class DiskBitmapCache  implements ImageCache {
    LruCache<String, Bitmap> lruCache;
    DiskLruCache diskLruCache;
    //final  int RAM_CACHE_SIZE = (int)(Runtime.getRuntime().maxMemory() / 8);
    final int RAM_CACHE_SIZE = 15 * 1024 * 1024;
    String DISK_CACHE_DIR = "image";
    final long DISK_MAX_SIZE = 120 * 1024 * 1024;
    String cacheDirstr = Environment.getExternalStorageDirectory().getPath()+"/"+ Config.APP_Code+"/cache";
    /** 建立线程安全,支持高并发的容器 **/
    private static ConcurrentHashMap<String, SoftReference<Bitmap>> currentHashmap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

    public DiskBitmapCache() {
        this.lruCache = new LruCache<String, Bitmap>(RAM_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
            //这个方法当LruCache的内存容量满的时候会调用,将oldValue的元素移除出来腾出空间给新的元素加入
            @Override
            protected void entryRemoved(boolean evicted, String key,Bitmap oldValue, Bitmap newValue)
            {
                if(oldValue != null)
                {
                    // 当硬引用缓存容量已满时，会使用LRU算法将最近没有被使用的图片转入软引用缓存
                    currentHashmap.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }


        };

        File cacheDir = new File(cacheDirstr, DISK_CACHE_DIR);
        if(!cacheDir.exists())
        {
            cacheDir.mkdir();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        String key=generateKey(url);
        Bitmap bmp = lruCache.get(key);
        //lruCache.evictAll();
        if (bmp == null) {
            bmp = getBitmapFromDiskLruCache(key);
            //从磁盘读出后，放入内存
            if(bmp!=null)
            {
                lruCache.put(key,bmp);
            }
        }
        return bmp;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key=generateKey(url);
        if(bitmap.getByteCount() > 1000){
            putBitmapToDiskLruCache(key,bitmap);
        }else{
            lruCache.put(url, bitmap);
            putBitmapToDiskLruCache(key,bitmap);
        }


    }

    private void putBitmapToDiskLruCache(String key, Bitmap bitmap) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if(editor!=null)
            {
                OutputStream outputStream = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromDiskLruCache(String key) {
        try {
            DiskLruCache.Snapshot snapshot=diskLruCache.get(key);
            if(snapshot!=null)
            {
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 因为DiskLruCache对key有限制，只能是[a-z0-9_-]{1,64},所以用md5生成key
     * @param url
     * @return
     */
    private String generateKey(String url)
    {
        return MD5Utils.hashKeyForDisk(url);
    }

}
