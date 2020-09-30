package com.haocang.bjxjc.utils.tools;

import android.os.Environment;

import java.io.File;

/**
 * 创建时间：2019/3/13
 * 编 写 人：ShenC
 * 功能描述：
 */

public class FileUtils {

    /**
     * 方法说明：创建项目的文件工作区
     */
    public static void initFile() {
        File sd = Environment.getExternalStorageDirectory();
        String paths = sd.getPath() + "/" + ApiConstant.APP_Code;
        String paths2 = sd.getPath() + "/" + ApiConstant.APP_Code + "/log";
        String paths21 = sd.getPath() + "/" + ApiConstant.APP_Code + "/apk";
        String paths22 = sd.getPath() + "/" + ApiConstant.APP_Code + "/map";
        String paths23 = sd.getPath() + "/" + ApiConstant.APP_Code + "/camera";
        String paths24 = sd.getPath() + "/" + ApiConstant.APP_Code + "/cache";
        String paths25 = sd.getPath() + "/" + ApiConstant.APP_Code + "/video";
        String paths26 = sd.getPath() + "/" + ApiConstant.APP_Code + "/audio";

        File file = new File(paths);
        File file2 = new File(paths2);
        File file21 = new File(paths21);
        File file22 = new File(paths22);
        File file23 = new File(paths23);
        File file24 = new File(paths24);
        File file25 = new File(paths25);
        File file26 = new File(paths26);
        if (!file.exists()) {
            file.mkdir();
            file2.mkdir();
            file21.mkdir();
            file22.mkdir();
            file23.mkdir();
            file24.mkdir();
            file25.mkdir();
            file26.mkdir();
        } else {
            if (!file2.exists()) {
                file2.mkdir();
            }
            if (!file21.exists()) {
                file21.mkdir();
            }
            if (!file22.exists()) {
                file22.mkdir();
            }
            if (!file23.exists()) {
                file23.mkdir();
            }
            if (!file24.exists()) {
                file24.mkdir();
            }
            if (!file25.exists()) {
                file25.mkdir();
            }
            if (!file26.exists()) {
                file26.mkdir();
            }
        }


    }


    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;

        return true;
        // return dirFile.delete();//删除当前空目录
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

    /**
     * 获取文件夹大小 kb
     * @param file File实例
     * @return long
     */
    public static int getFolderSize(File file){

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        //return size/1048576;
        return (int)size/1000;
    }
    public static String getFolderSize_Str(File file){

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        if(size == 0)  return "";
        if(size < 1000) return size+"B";
        if(size < 1000000) return (int)size/1000+"kb";
        return (int)size/1000000+"Mb";
    }


}
