package com.tourcoo.carnet;

import android.text.TextUtils;

import com.tourcoo.carnet.core.frame.util.SharedPreferencesUtil;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.entity.account.UserInfo;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_ACCOUNT;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_IS_REMIND_PASSWORD;
import static com.tourcoo.carnet.core.common.CommonConstant.PREF_KEY_PASSWORD;


/**
 * @author :zhoujian
 * @description :账户信息帮助类
 * @company :途酷科技
 * @date 2019年 04月 3日 16时52分
 * @Email: 971613168@qq.com
 */
public class AccountInfoHelper {
    private UserInfoEntity mUserInfoEntity;
    private CarInfoEntity currentCar;
    private static final String TAG = "AccountInfoHelper";
    /**
     * 车辆信息列表
     */
    private List<CarInfoEntity> mCarInfoEntityList;

    private AccountInfoHelper() {
        mCarInfoEntityList = new ArrayList<>();
    }

    private static class SingletonInstance {
        private static final AccountInfoHelper INSTANCE = new AccountInfoHelper();
    }

    public static AccountInfoHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public String getToken() {
        if (mUserInfoEntity != null && mUserInfoEntity.getToken() != null) {
            return mUserInfoEntity.getToken();
        }
        return "";
    }


    public List<CarInfoEntity> getCarInfoEntityList() {
        return mCarInfoEntityList;
    }

    public CarInfoEntity getCurrentInfoCarInfo() {
        if (mCarInfoEntityList != null && !mCarInfoEntityList.isEmpty()) {
            return mCarInfoEntityList.get(0);
        }
        return null;

    }

    public void setCarInfoEntityList(List<CarInfoEntity> carInfoEntityList) {
        mCarInfoEntityList.clear();
        if (carInfoEntityList == null || carInfoEntityList.isEmpty()) {
            return;
        }
        mCarInfoEntityList.addAll(carInfoEntityList);
    }


    public UserInfoEntity findUserInfoByLocal() {
        UserInfoEntity userInfoEntity = LitePal.findFirst(UserInfoEntity.class);
        if (userInfoEntity != null) {
            UserInfo userInfo = LitePal.findFirst(UserInfo.class);
            if (userInfo != null) {
                userInfoEntity.setUserInfo(userInfo);
                return userInfoEntity;
            }
        }
        return null;
    }


    public void saveUserInfo(UserInfoEntity userInfoEntity) {
        if (userInfoEntity != null && userInfoEntity.getUserInfo() != null) {
            userInfoEntity.save();
            userInfoEntity.getUserInfo().save();
        }
    }

    public void updateAndSaveUserInfo(UserInfo userInfo) {
        if (mUserInfoEntity != null && userInfo != null) {
            LitePal.deleteAll(UserInfo.class);
            userInfo.save();
            mUserInfoEntity.setUserInfo(LitePal.findFirst(UserInfo.class));
            TourcooLogUtil.i("更新成功", userInfo);
        }
    }

    public void setUserInfoEntity(UserInfoEntity userInfoEntity) {
        mUserInfoEntity = userInfoEntity;
    }

    public UserInfoEntity getUserInfoEntity() {
        return mUserInfoEntity;
    }


    /**
     * 设置当前车辆(可能为null)
     *
     * @param carInfoEntity
     */
    public void setDefaultCar(CarInfoEntity carInfoEntity) {
        currentCar = carInfoEntity;
    }

    /**
     * 获取当前车辆信息
     *
     * @return
     */
    public CarInfoEntity getCurrentCar() {
        return currentCar;
    }

    /**
     * 删除用户数据
     */
    public void deleteUserAccount() {
        LitePal.deleteAll(UserInfoEntity.class);
        LitePal.deleteAll(UserInfo.class);
    }

    /**
     * 是否记住密码
     */
    public boolean isRemindPassword() {
        boolean flag;
        try {
            flag = (boolean) SharedPreferencesUtil.get(PREF_KEY_IS_REMIND_PASSWORD, true);
            TourcooLogUtil.e(TAG, "正常:" + flag);
        } catch (Exception e) {
            flag = true;
            TourcooLogUtil.e(TAG, "出现异常了");
        }
        return flag;
    }

    /**
     * 修改本地存储的用户密码
     * @param newPass
     */
    public void changePassWord(String newPass) {
        if (TextUtils.isEmpty(newPass)) {
            TourcooLogUtil.e(TAG, "密码为空");
            return;
        }
        TourcooLogUtil.i(TAG, "修改成功:"+newPass);
        SharedPreferencesUtil.put(PREF_KEY_PASSWORD, newPass);
    }

    /**
     * 修改本地存储的用户手机号
     * @param newPhone
     */
    public void changeMobile(String newPhone) {
        if (TextUtils.isEmpty(newPhone)) {
            TourcooLogUtil.e(TAG, "手机号为空");
            return;
        }
        TourcooLogUtil.i(TAG, "手机号修改成功:"+newPhone);
        SharedPreferencesUtil.put(PREF_KEY_ACCOUNT, newPhone);
    }


}