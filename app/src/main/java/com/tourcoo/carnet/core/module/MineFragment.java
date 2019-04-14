package com.tourcoo.carnet.core.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.allen.library.CircleImageView;
import com.allen.library.SuperTextView;
import com.tourcoo.carnet.AccountInfoHelper;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.fragment.BaseTitleFragment;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.frame.retrofit.BaseObserver;
import com.tourcoo.carnet.core.frame.util.HelpFeedBackActivity;
import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.TourcooUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.MessageInfo;
import com.tourcoo.carnet.entity.account.UserInfoEntity;
import com.tourcoo.carnet.entity.event.BaseEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.tourcoo.carnet.ui.MsgSystemActivity;
import com.tourcoo.carnet.ui.factory.DoorToDoorServiceDetailActivity;
import com.tourcoo.carnet.ui.account.PersonalDataActivity;
import com.tourcoo.carnet.ui.car.CarsManagementActivity;
import com.tourcoo.carnet.ui.order.OrderHistoryActivity;
import com.tourcoo.carnet.ui.setting.BaseSettingActivity;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import static com.tourcoo.carnet.core.common.EventConstant.EVENT_REQUEST_MSG_COUNT;
import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * @author :zhoujian
 * @description : 个人中心
 * @company :翼迈科技
 * @date 2019年03月06日上午 10:11
 * @Email: 971613168@qq.com
 */
public class MineFragment extends BaseTitleFragment implements View.OnClickListener {
    private SuperTextView stvSystemMessage;
    private UserInfoEntity mUserInfoEntity;
    public static final int CODE_REQUEST_MSG = 1;
    private int messageCount;
    private TextView tvNickName;
    private CircleImageView civAvatar;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mContentView.findViewById(R.id.stvPersonalData).setOnClickListener(this);
        mContentView.findViewById(R.id.stvVehicleManagement).setOnClickListener(this);
        mContentView.findViewById(R.id.stvBasicSetting).setOnClickListener(this);
        mContentView.findViewById(R.id.stvHistoryOder).setOnClickListener(this);
        mContentView.findViewById(R.id.stvHelpFeedBack).setOnClickListener(this);
        mContentView.findViewById(R.id.stvTripReport).setOnClickListener(this);
        mContentView.findViewById(R.id.stvReportWarning).setOnClickListener(this);
        stvSystemMessage = mContentView.findViewById(R.id.stvSystemMessage);
        stvSystemMessage.setOnClickListener(this);
        civAvatar = mContentView.findViewById(R.id.civAvatar);
        tvNickName = mContentView.findViewById(R.id.tvNickName);
        mUserInfoEntity = AccountInfoHelper.getInstance().getUserInfoEntity();
    }

    @Override
    public void loadData() {
        super.loadData();
        showUserInfo();
        requestNoReadCount();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("我的");
    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stvPersonalData:
                TourcooUtil.startActivity(mContext, PersonalDataActivity.class);
                break;
            case R.id.stvVehicleManagement:
                TourcooUtil.startActivity(mContext, CarsManagementActivity.class);
                break;
            case R.id.stvBasicSetting:
                TourcooUtil.startActivity(mContext, BaseSettingActivity.class);
                break;
            case R.id.stvHistoryOder:
                TourcooUtil.startActivity(mContext, OrderHistoryActivity.class);
                break;
            case R.id.stvHelpFeedBack:
                TourcooUtil.startActivity(mContext, HelpFeedBackActivity.class);
                break;
            case R.id.stvTripReport:
                //行程报告
                break;
            case R.id.stvReportWarning:
                //报警记录
                TourcooUtil.startActivity(mContext, DoorToDoorServiceDetailActivity.class);
                break;
            case R.id.stvSystemMessage:
                //系统消息
                Intent intent = new Intent();
                intent.setClass(mContext, MsgSystemActivity.class);
                startActivityForResult(intent, CODE_REQUEST_MSG);
                break;
            default:
                break;
        }
    }


    private void requestNoReadCount() {
        if (mUserInfoEntity == null || mUserInfoEntity.getUserInfo() == null) {
            return;
        }
        ApiRepository.getInstance().getNoReadCount(mUserInfoEntity.getUserInfo().getId() + "").compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseEntity<MessageInfo.MessageBean>>() {
                    @Override
                    public void onRequestNext(BaseEntity<MessageInfo.MessageBean> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                    int count = entity.data.getCountUnreadMsg();
                                    showMessageCount(count);
                                    //发消息通知主页面
                                    EventBus.getDefault().post(new BaseEvent(count));
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private void showMessageCount(int count) {
        if (count <= 0) {
            stvSystemMessage.setRightString("");
        } else {
            stvSystemMessage.setRightString(count + "");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_REQUEST_MSG:
                requestNoReadCount();
                break;
            default:
                break;
        }
    }


    @SuppressLint("WrongConstant")
    @Subscriber(mode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent != null) {
            switch (baseEvent.id) {
                case EVENT_REQUEST_MSG_COUNT:
                    TourcooLogUtil.i(TAG, "收到消息");
                    requestNoReadCount();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void showUserInfo() {
        if (mUserInfoEntity != null && mUserInfoEntity.getUserInfo() != null) {
            if (TextUtils.isEmpty(mUserInfoEntity.getUserInfo().getNickname())) {
                tvNickName.setText("未填写");
            }
            GlideManager.loadImg(mUserInfoEntity.getUserInfo().getAvatar(), civAvatar, TourcooUtil.getDrawable(R.mipmap.img_default_minerva));
        }
    }
}
