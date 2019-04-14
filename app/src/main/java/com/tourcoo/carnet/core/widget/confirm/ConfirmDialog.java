package com.tourcoo.carnet.core.widget.confirm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
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
 * @description :通用对话框
 * @company :翼迈科技
 * @date 2019年 03月 16日 09时57分
 * @Email: 971613168@qq.com
 */
public class ConfirmDialog extends Dialog {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    public ConfirmDialog(Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title = "温馨提示";
        private String firstMessage;
        private String secondMessage;
        private boolean touchOutSide = true;
        private int gravity;
        private int positiveButtonPosition;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private float firstMsgTextSize = 12;
        private int firstMsgTextColor = R.color.black;
        private int firstMsgGravity;
        /**
         * 文字加粗
         */
        private boolean negativeButtonBold = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setFirstMessage(String message) {
            this.firstMessage = message;
            return this;
        }

        public Builder setNegativeButtonButtonBold(boolean isBold) {
            this.negativeButtonBold = isBold;
            return this;
        }
        public Builder setSecondMessage(String message) {
            this.secondMessage = message;
            return this;
        }

        public void setPositiveButtonPosition(int positiveButtonPosition) {
            this.positiveButtonPosition = positiveButtonPosition;
        }

        /**
         * 设置内容的对齐方式
         */
        public Builder setMessageGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置标题资源文件文件
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTouchOutSideCancelable(boolean touchOutSideCancelable) {
            this.touchOutSide = touchOutSideCancelable;
            return this;
        }


        /**
         * 设置标题
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置第一行文本字体大小
         * @param textSize
         * @return
         */
        public Builder setFirstMsgSize(float textSize) {
            this.firstMsgTextSize = textSize;
            return this;
        }
        public Builder setFirstTextColor(int  color) {
            this.firstMsgTextColor = color;
            return this;
        }
        /**
         * 设置确定点击事件监听
         *
         * @param positiveButtonText
         * @return
         */
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

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ConfirmDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ConfirmDialog dialog = new ConfirmDialog(context, R.style.BaseDialog);
            View layout = inflater.inflate(R.layout.dialog_confirm_common, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (!TextUtils.isEmpty(title)) {
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            if (!TextUtils.isEmpty(firstMessage)) {
                ((TextView) layout.findViewById(R.id.messageFirst)).setTextSize(TypedValue.COMPLEX_UNIT_SP, firstMsgTextSize);
            }
            if (firstMsgTextColor != -1) {
                ((TextView) layout.findViewById(R.id.messageFirst)).setTextColor(firstMsgTextColor);
            }
            // button position
            LinearLayout positiveButton_left = (LinearLayout) layout.findViewById(R.id.positiveButton_left);
            LinearLayout positiveButton_right = (LinearLayout) layout.findViewById(R.id.positiveButton_right);
            if (positiveButtonPosition == LEFT) {
                positiveButton_left.setVisibility(View.VISIBLE);
                positiveButton_right.setVisibility(View.GONE);
            } else if (positiveButtonPosition == RIGHT) {
                positiveButton_left.setVisibility(View.GONE);
                positiveButton_right.setVisibility(View.VISIBLE);
            } else {
                positiveButton_left.setVisibility(View.GONE);
                positiveButton_right.setVisibility(View.VISIBLE);
            }

            if (positiveButtonPosition == LEFT) {
                // set the confirm button
                ((Button) layout.findViewById(R.id.positiveButtonLeft)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButtonLeft)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
                // set the cancel button
                ((Button) layout.findViewById(R.id.negativeButtonRight)).setText(negativeButtonText);
                if (negativeButtonBold) {
                    Button negative = (Button) layout.findViewById(R.id.negativeButtonRight);
                    negative.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButtonRight)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // set the confirm button
                ((Button) layout.findViewById(R.id.positiveButtonRight)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.positiveButtonRight)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
                // set the cancel button
                ((Button) layout.findViewById(R.id.negativeButtonLeft)).setText(negativeButtonText);
                if (negativeButtonBold) {
                    Button negative = (Button) layout.findViewById(R.id.negativeButtonLeft);
                    negative.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButtonLeft)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }

            // 设置第一行内容
            if (StringUtils.isNotBlank(firstMessage)) {
                ((TextView) layout.findViewById(R.id.messageFirst)).setText(firstMessage);
            }
            // 设置第二行内容
            if (StringUtils.isNotBlank(secondMessage)) {
                ((TextView) layout.findViewById(R.id.messageSecond)).setText(secondMessage);
            }else {
                ((TextView) layout.findViewById(R.id.messageSecond)).setVisibility(View.GONE);
            }
            if (gravity != 0) {
                ((TextView) layout.findViewById(R.id.messageFirst)).setGravity(gravity);
                ((TextView) layout.findViewById(R.id.messageSecond)).setGravity(gravity);
            }
            dialog.setContentView(layout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(touchOutSide);
            return dialog;
        }

    }
}
