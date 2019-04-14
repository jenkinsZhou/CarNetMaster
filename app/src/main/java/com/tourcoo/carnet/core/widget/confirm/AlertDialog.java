package com.tourcoo.carnet.core.widget.confirm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourcoo.carnet.R;

import org.apache.commons.lang.StringUtils;

/**
 * @author :zhoujian
 * @description :弹窗对话框
 * @company :翼迈科技
 * @date 2019年 03月 16日 11时56分
 * @Email: 971613168@qq.com
 */
public class AlertDialog extends Dialog {

    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title = "温馨提示";
        private String message;
        private int gravity;
        private String positiveButtonText = "我知道啦";
        private OnClickListener positiveButtonClickListener;
        private AlertDialog dialog = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置内容的对齐方式
         */
        public Builder setMessageGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * @return
         * @description 创建对话框
         */
        public AlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            View layout = inflater.inflate(R.layout.dialog_alert_common, null);
            if (null == dialog) {
                dialog = new AlertDialog(context, R.style.BaseDialog);
            }
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if(!TextUtils.isEmpty(title)){
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            // set the confirm button
            if(!TextUtils.isEmpty(positiveButtonText)){
                ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            }
            if (positiveButtonClickListener != null) {
                ((LinearLayout) layout.findViewById(R.id.lilayout_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
                ((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            // set the content message
            if (StringUtils.isNotBlank(message)) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }
            if (gravity != 0) {
                ((TextView) layout.findViewById(R.id.message)).setGravity(gravity);
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            return dialog;
        }


        public boolean isShow() {
            if (null == dialog) {
                return false;
            } else {
                return dialog.isShowing();
            }
        }
    }
}
