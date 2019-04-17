package com.tourcoo.carnet.ui;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.adapter.SystemMsgAdapter;
import com.tourcoo.carnet.core.frame.UiConfigManager;
import com.tourcoo.carnet.core.frame.base.activity.BaseRefreshLoadActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :系统消息页面
 * @company :途酷科技
 * @date 2019年03月18日13:53
 * @Email: 971613168@qq.com
 */
public class MsgSystemActivity extends BaseRefreshLoadActivity<MessageInfo> {
    private SystemMsgAdapter systemMsgAdapter;
    private UserInfoEntity mUserInfoEntity;

    @Override
    public int getContentLayout() {
        return R.layout.layout_title_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mUserInfoEntity = AccountInfoHelper.getInstance().getUserInfoEntity();
        mStatusManager.showSuccessLayout();
        getMsgList(true, 1, mDefaultPageSize);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        systemMsgAdapter = new SystemMsgAdapter();
        return systemMsgAdapter;
    }

    @Override
    public void loadData(int page) {
        TourCooLogUtil.d("loadData:" + page);
        if (page == 0) {
            page = 1;
        }
        getMsgList(false, page, mDefaultPageSize);
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        mDefaultPage = 1;
    }

    @Override
    public void onLoadMoreRequested() {
        super.onLoadMoreRequested();
        TourCooLogUtil.d("onLoadMoreRequested:");
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("系统消息");
    }

    /**
     * 系统消息
     */
    private void getMsgList(boolean isShowLoading, int pageIndex, int pageSize) {
        if (mUserInfoEntity == null || mUserInfoEntity.getUserInfo() == null) {
            ToastUtil.show("未获取用户信息");
            return;
        }
        if (isShowLoading) {
            ApiRepository.getInstance().getMsgList(mUserInfoEntity.getUserInfo().getId()+"", pageIndex, pageSize).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                    subscribe(new BaseLoadingObserver<BaseEntity<MessageInfo>>() {
                        @Override
                        public void onRequestNext(BaseEntity<MessageInfo> entity) {
                            if (entity != null) {
                                if (entity.code == CODE_REQUEST_SUCCESS) {
                                    MessageInfo messageInfo = entity.data;
                                    if (messageInfo != null) {
                                        UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), messageInfo.getElements() == null ? new ArrayList<>() : messageInfo.getElements(), null);
                                    }
                                } else {
                                    ToastUtil.showFailed(entity.message);
                                }
                            }
                        }
                    });

        } else {
            ApiRepository.getInstance().getMsgList(mUserInfoEntity.getUserInfo().getId()+"", pageIndex, pageSize).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                    subscribe(new BaseObserver<BaseEntity<MessageInfo>>() {
                        @Override
                        public void onRequestNext(BaseEntity<MessageInfo> entity) {
                            if (entity != null) {
                                if (entity.code == CODE_REQUEST_SUCCESS) {
                                    MessageInfo messageInfo = entity.data;
                                    if (messageInfo != null) {
                                        UiConfigManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), messageInfo.getElements() == null ? new ArrayList<>() : messageInfo.getElements(), null);
                                    }
                                } else {
                                    ToastUtil.showFailed(entity.message);
                                }
                            }
                        }
                    });
        }
    }


}
