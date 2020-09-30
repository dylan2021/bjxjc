package com.haocang.bjxjc.utils.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.haocang.bjxjc.ui.login.bean.UserInfoBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间：2019/3/4
 * 编 写 人：ShenC
 * 功能描述：保存用户信息的业务方法
 * 注意：
 * 《1》getFilesDir()  得到目录  /data/date/包名/files/
 * 《2》getCacheDir()  得到目录/data/data/包名/cache/    --缓存文件，文件不要超过1MB
 */

public class SharedPreferencesUtil {

    //保存数据
    public static void SaveUserInfo(Context context, String user_id,String user_account, String user_name, String user_pass , String user_deptid, String user_deptname,String user_headicon,String user_issubmitpass){
        SharedPreferences.Editor e = context.getSharedPreferences("com.haocang.bjxjc",Context.MODE_PRIVATE).edit();
        e.putString("user_id",user_id);
        e.putString("user_account", user_account);
        e.putString("user_name", user_name);
        e.putString("user_pass", user_pass);
        e.putString("user_deptid", user_deptid);
        e.putString("user_deptname", user_deptname);
        e.putString("user_headicon", user_headicon);
        e.putString("user_issubmitpass", user_issubmitpass);
        e.commit();
    }

    //读取数据
    public static Map<String,String> GetUserInfoMap(Context context){
        Map<String,String> map = new HashMap<>();
        SharedPreferences  shard = context.getSharedPreferences("com.haocang.bjxjc", Context.MODE_PRIVATE);
        map.put("user_id",shard.getString("user_id",null));
        map.put("user_account",shard.getString("user_account",null));
        map.put("user_name",shard.getString("user_name",null));
        map.put("user_pass",shard.getString("user_pass",null));
        map.put("user_deptid",shard.getString("user_deptid",null));
        map.put("user_deptname",shard.getString("user_deptname",null));
        map.put("user_issubmitpass",shard.getString("user_issubmitpass",null));
        return map;
    }

    //是否记住密码(自动登录)
    public static Boolean GetIsSubmitPass(Context context){
        try {
            Map<String, String> map = new HashMap<>();
            SharedPreferences shard = context.getSharedPreferences("com.haocang.bjxjc", Context.MODE_PRIVATE);
            String  IsSubmitPass = shard.getString("user_issubmitpass", "N");
            if(IsSubmitPass.equals("Y")){
                return true;
            }
            return false;
        }catch (Exception er){
            return false;
        }
    }

    //读取数据
    public static UserInfoBean GetUserInfo(Context context){
        SharedPreferences  shard = context.getSharedPreferences("com.haocang.bjxjc", Context.MODE_PRIVATE);
        UserInfoBean userinfo = new UserInfoBean();
        userinfo.setF_UserId(shard.getString("user_id",null));
        userinfo.setF_Account(shard.getString("user_account",null));
        userinfo.setF_RealName(shard.getString("user_name",null));
        userinfo.setF_Password(shard.getString("user_pass",null));
        userinfo.setF_DepartmentId(shard.getString("user_deptid",null));
        userinfo.setF_DepartmentName(shard.getString("user_deptname",null));
        userinfo.setF_HeadIcon(shard.getString("user_headicon",null));
        return userinfo;
    }
}
