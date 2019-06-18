package com.tourcoo.carnet.adapter.obd;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.TextView;


import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.log.widget.utils.DateUtil;
import com.tourcoo.carnet.entity.obd.DriveReportInfo;

import java.util.ArrayList;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年06月13日16:42
 * @Email: 971613168@qq.com
 */
public class DriveReportInfoAdapter extends BaseQuickAdapter<DriveReportInfo, BaseViewHolder> {
    private Context mContext;
    private static final String START_TAG = ",出发位置:";
    private static final String END_TAG = ",终点位置:";
    /**
     * 用于存储解析出来的地理位置信息（键为经纬度组合的字符串,值为地理位置信息）
     */
    private ArrayMap<String, String> mPointStringMap;

    public DriveReportInfoAdapter(Context context) {
        super(R.layout.item_driving_report);
        mContext = context;
        mPointStringMap = new ArrayMap<>(10);
    }

    @Override
    protected void convert(BaseViewHolder helper, DriveReportInfo item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvUpdateTime, "更新于" + DateUtil.getTimeStringChinaToMinute(item.getEndTime()));
        TextView tvContent = helper.getView(R.id.tvDriveReportContent);
        String info = parseReportContent(item);
        helper.setText(R.id.tvDriveReportContent, info);
        showDriveInfo(item, tvContent);
    }


    private String parseReportContent(DriveReportInfo info) {
        return "行程的平均油耗:" + info.getAvgFuelConsumption() + "KM/L," + "行程的总油耗:" + info.getFuelConsumption() + "升"
                + ",行驶开始时间:" + DateUtil.getTimeStringChinaToMinute(info.getStartTime()) + ",行驶结束时间:" + DateUtil.getTimeStringChinaToMinute(info.getEndTime())
                + ",行程总里程:" + info.getTripMileage() + "KM";
    }


    private String parsePositionKey(double latitude, double longtitude) {
        return latitude + "," + longtitude;
    }


    /**
     * 从缓存中获取位置信息
     *
     * @param la
     * @param lo
     * @return
     */
    private String getLocationFromCache(double la, double lo) {
        String value = mPointStringMap.get(parsePositionKey(la, lo));
        return value != null ? value : "";
    }


    private void getPositionAndCacheAsyc(double latitude, double longitude, TextView textView, DriveReportInfo info) {
        CoordinateConverter converter = new CoordinateConverter(mContext);
        // 将百度坐标转为高德坐标
        converter.from(CoordinateConverter.CoordType.BAIDU);
        // sourceLatLng待转换坐标点 DPoint类型
        DPoint dPoint = new DPoint();
        dPoint.setLatitude(latitude);
        dPoint.setLongitude(longitude);
        try {
            converter.coord(dPoint);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            LatLonPoint latLonPoint = new LatLonPoint(desLatLng.getLatitude(), desLatLng.getLongitude());
            GeocodeSearch search = new GeocodeSearch(mContext);
            search.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    RegeocodeAddress addresses = regeocodeResult.getRegeocodeAddress();
                    if (addresses != null) {
                        //这里先存储位置信息
                        mPointStringMap.put(parsePositionKey(latitude, longitude), addresses.getFormatAddress());
                        showDriveInfo(info, textView);
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
// 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
            search.getFromLocationAsyn(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDriveInfo(DriveReportInfo info, TextView textView) {
        String startPosition = getLocationFromCache(info.getStartLat(), info.getStartLng());
        String endPosition = getLocationFromCache(info.getEndLat(), info.getEndLng());
        if (TextUtils.isEmpty(startPosition)) {
            //说明需要异步解析位置信息
            TourCooLogUtil.d(TAG, TAG + "准备解析起始位置信息:");
            getPositionAndCacheAsyc(info.getStartLat(), info.getStartLng(), textView, info);
            return;
        }
        if (TextUtils.isEmpty(endPosition)) {
            //说明需要异步解析位置信息
            TourCooLogUtil.i(TAG, TAG + "准备解析终点位置信息:");
            getPositionAndCacheAsyc(info.getEndLat(), info.getEndLng(), textView, info);
            return;
        }
        if (!TextUtils.isEmpty(startPosition) && !TextUtils.isEmpty(endPosition)) {
            String reportContent = parseReportContent(info);
            reportContent += START_TAG + startPosition;
            reportContent += END_TAG + endPosition;
            textView.setText(reportContent);
        }
    }
}
