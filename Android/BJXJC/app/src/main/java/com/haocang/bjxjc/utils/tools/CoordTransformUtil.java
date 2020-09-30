package com.haocang.bjxjc.utils.tools;

import android.content.Context;
import android.widget.Toast;

import com.haocang.bjxjc.requestcenter.DataModel;
import com.haocang.commonlib.network.interfaces.InitDataCallBack;

/**
 * 创建时间：2019/5/29
 * 编 写 人：ShenC
 * 功能描述：坐标转换工具类
 */

public class CoordTransformUtil {


    /**===============================================
     * 创建时间：2019/5/29 13:44
     * 编 写 人：ShenC
     * 方法说明：wgs84坐标转换为gis坐标
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static void CoordTransformToGis(final Context context,float x,float y) {
        String ApiUrl = ApiConstant.MAPCoordTransformUrl+"?inSR=4326&outSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A"+x+"%2C%22y%22%3A"+y+"%2C%22spatialReference%22%3A%7B%22wkid%22%3A4326%7D%7D%5D%7D&transformation=&transformForward=true&f=json" ;
        DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {

                } else {
                    Toast.makeText(context,"转换为gis坐标失败:"+string,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**===============================================
     * 创建时间：2019/5/29 13:45
     * 编 写 人：ShenC
     * 方法说明：gis坐标转换为wgs84坐标
     *================================================
     * 修改内容：      修改时间：      修改人：
     *===============================================*/
    public static void CoordTransformToWgs84(final Context context,float x,float y) {
        String ApiUrl = ApiConstant.MAPCoordTransformUrl+ "?inSR=%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D&outSR=4326&geometries=%7B%22geometryType%22%3A%22esriGeometryPoint%22%2C%22geometries%22%3A%5B%7B%22x%22%3A"+x+"%2C%22y%22%3A"+y+"%2C%22spatialReference%22%3A%7B%22wkt%22%3A%22PROJCS%5B%5C%22BNAH%5C%22%2CGEOGCS%5B%5C%22GCS_WGS_1984%5C%22%2CDATUM%5B%5C%22D_WGS_1984%5C%22%2CSPHEROID%5B%5C%22WGS_1984%5C%22%2C6378137.0%2C298.257223563%5D%5D%2CPRIMEM%5B%5C%22Greenwich%5C%22%2C0.0%5D%2CUNIT%5B%5C%22Degree%5C%22%2C0.0174532925199433%5D%5D%2CPROJECTION%5B%5C%22Berghaus_Star%5C%22%5D%2CPARAMETER%5B%5C%22False_Easting%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22False_Northing%5C%22%2C500000.0%5D%2CPARAMETER%5B%5C%22Central_Meridian%5C%22%2C116.385%5D%2CPARAMETER%5B%5C%22Latitude_Of_Origin%5C%22%2C39.461%5D%2CPARAMETER%5B%5C%22XY_Plane_Rotation%5C%22%2C-7.0%5D%2CUNIT%5B%5C%22Meter%5C%22%2C1.0%5D%5D%22%7D%7D%5D%7D&transformation=&transformForward=true&vertical=false&f=json";
        DataModel.requestGET(context, ApiUrl, new InitDataCallBack() {
            @Override
            public void callbak(boolean b, String string) {
                if (b) {

                } else {
                    Toast.makeText(context,"转换为wgs84坐标失败:"+string,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
