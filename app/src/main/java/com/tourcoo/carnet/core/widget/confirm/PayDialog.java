package com.tourcoo.carnet.core.widget.confirm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourcoo.carnet.R;


/**
 * @author :JenkinsZhou
 * @description :自定义支付对话框
 * @company :途酷科技
 * @date 2019年03月20日20:51
 * @Email: 971613168@qq.com
 */
public class PayDialog extends Dialog implements View.OnClickListener {
    private ImageView ivAliAPayCheckBox;
    private ImageView ivWeChatPayCheckBox;
    public static final int PAY_TYPE_ALI = 1;
    public static final int PAY_TYPE_WE_XIN = 0;
    private int payType = PAY_TYPE_ALI;
    private TextView tvMoney;
    private Context context;
    private double money;
    private PayListener mPayListener;

    public PayDialog(Context context, double money, PayListener payListener) {
        super(context, R.style.PayDialogStyle);
        this.context = context;
        this.money = money;
        this.mPayListener = payListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        findViewById(R.id.tvPayConfirm).setOnClickListener(this);
        findViewById(R.id.rlWeChatPay).setOnClickListener(this);
        findViewById(R.id.rlAliPay).setOnClickListener(this);

        ivAliAPayCheckBox = findViewById(R.id.ivAliAPayCheckBox);
        ivWeChatPayCheckBox = findViewById(R.id.ivWeChatPayCheckBox);
        tvMoney = findViewById(R.id.tvMoney);
        tvMoney.setText("￥" + money);
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = AbsListView.LayoutParams.MATCH_PARENT;
        //设置Dialog距离底部的距离
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        usePayWeiChat();
    }


    private void setSelect(boolean select, ImageView imageView) {
        if (select) {
            imageView.setImageResource(R.mipmap.ic_check_selected);
        } else {
            imageView.setImageResource(R.mipmap.ic_check_normal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlWeChatPay:
                usePayWeiChat();
                break;
            case R.id.rlAliPay:
                usePayAli();

                break;
            case R.id.tvPayConfirm:
                if (mPayListener != null) {
                    mPayListener.pay(payType, this);
                }
                break;
            default:
                break;
        }
    }


    private void usePayWeiChat() {
        setSelect(true, ivWeChatPayCheckBox);
        setSelect(false, ivAliAPayCheckBox);
        payType = PAY_TYPE_WE_XIN;
    }

    private void usePayAli() {
        setSelect(false, ivWeChatPayCheckBox);
        setSelect(true, ivAliAPayCheckBox);
        payType = PAY_TYPE_ALI;
    }

    /**
     * 支付回调
     */
    public interface PayListener {

        /**
         * 支付
         *
         * @param payType
         * @param dialog
         */
        void pay(int payType, Dialog dialog);

    }
}
