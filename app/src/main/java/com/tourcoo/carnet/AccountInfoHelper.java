package com.tourcoo.carnet;

import com.tourcoo.carnet.entity.account.UserInfo;
import com.tourcoo.carnet.entity.car.CarInfoEntity;
import com.tourcoo.carnet.entity.account.UserInfoEntity;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;


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

}