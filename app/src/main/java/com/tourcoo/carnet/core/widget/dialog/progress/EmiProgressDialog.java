package com.tourcoo.carnet.core.widget.dialog.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.widget.custom.IProgress;

import androidx.core.content.ContextCompat;

/**
 * @author :zhoujian
 * @description :
 * @company :翼迈科技
 * @date 2019年 03月 23日 22时23分
 * @Email: 971613168@qq.com
 */
public class EmiProgressDialog extends Dialog {
    private IProgress mProgressView;
    private float mDimAmount;
    private int mWindowColor;
    private Indeterminate mIndeterminateView;
    private View mView;
    private TextView mLabelText;
    private TextView mDetailsText;
    private String mLabel;
    private String mDetailsLabel;
    private FrameLayout mCustomViewContainer;
    private BackgroundLayout mBackgroundLayout;
    private int mWidth, mHeight;
    private int mLabelColor = Color.WHITE;
    private int mDetailColor = Color.WHITE;
    private float mCornerRadius = 10;
    private Context mContext;
    private int mAnimateSpeed = 1;
    private int mMaxProgress = 100;
    private boolean mIsAutoDismiss = true;
    private int mGraceTimeMs;
    private Handler mGraceTimer;
    private boolean mFinished = false;

    public EmiProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = mDimAmount;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        mBackgroundLayout = findViewById(R.id.background);
        mBackgroundLayout.setBaseColor(mWindowColor);
        mBackgroundLayout.setCornerRadius(mCornerRadius);
        if (mWidth != 0) {
            updateBackgroundSize();
        }
        mCustomViewContainer = findViewById(R.id.container);
        addViewToFrame(mView);
        if (mProgressView != null) {
            mProgressView.setMax(mMaxProgress);
        }
        if (mIndeterminateView != null) {
            mIndeterminateView.setAnimationSpeed(mAnimateSpeed);
        }
        mLabelText = findViewById(R.id.labelProgress);
        setLabel(mLabel, mLabelColor);
        mDetailsText = findViewById(R.id.labelDetailProgress);
        setDetailsLabel(mDetailsLabel, mDetailColor);
        setView(new CircleProgressView(mContext));
    }

    private void addViewToFrame(View view) {
        if (view == null) {
            return;
        }
        int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
        mCustomViewContainer.addView(view, params);
    }

    private void updateBackgroundSize() {
        ViewGroup.LayoutParams params = mBackgroundLayout.getLayoutParams();
        params.width = dpToPixel(mWidth, getContext());
        params.height = dpToPixel(mHeight, getContext());
        mBackgroundLayout.setLayoutParams(params);
    }

    public void setProgress(int progress) {
        if (mProgressView != null) {
            mProgressView.setProgress(progress);
            if (mIsAutoDismiss && progress >= mMaxProgress) {
                dismiss();
            }
        }
    }

    public void setView(View view) {
        if (view != null) {
            if (view instanceof IProgress) {
                mProgressView = (IProgress) view;
            }
            if (view instanceof Indeterminate) {
                mIndeterminateView = (Indeterminate) view;
            }
            mView = view;
            addViewToFrame(view);
            if (isShowing()) {
                mCustomViewContainer.removeAllViews();
            }
        }
    }

    public void setLabel(String label) {
        mLabel = label;
        if (mLabelText != null) {
            if (label != null) {
                mLabelText.setText(label);
                mLabelText.setVisibility(View.VISIBLE);
            } else {
                mLabelText.setVisibility(View.GONE);
            }
        }
    }


    public void setDetailsLabel(String detailsLabel) {
        mDetailsLabel = detailsLabel;
        if (mDetailsText != null) {
            if (detailsLabel != null) {
                mDetailsText.setText(detailsLabel);
                mDetailsText.setVisibility(View.VISIBLE);
            } else {
                mDetailsText.setVisibility(View.GONE);
            }
        }
    }

    public void setLabel(String label, int color) {
        mLabel = label;
        mLabelColor = color;
        if (mLabelText != null) {
            if (label != null) {
                mLabelText.setText(label);
                mLabelText.setTextColor(color);
                mLabelText.setVisibility(View.VISIBLE);
            } else {
                mLabelText.setVisibility(View.GONE);
            }
        }
    }

    public void setDetailsLabel(String detailsLabel, int color) {
        mDetailsLabel = detailsLabel;
        mDetailColor = color;
        if (mDetailsText != null) {
            if (detailsLabel != null) {
                mDetailsText.setText(detailsLabel);
                mDetailsText.setTextColor(color);
                mDetailsText.setVisibility(View.VISIBLE);
            } else {
                mDetailsText.setVisibility(View.GONE);
            }
        }
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        if (mBackgroundLayout != null) {
            updateBackgroundSize();
        }
    }


    public EmiProgressDialog setBackgroundColor(int color) {
        mWindowColor = ContextCompat.getColor(mContext, color);
        return this;
    }

    private float scale;

    private int dpToPixel(float dp, Context context) {
        if (scale == 0) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * scale);
    }


    public EmiProgressDialog setCornerRadius(float radius) {
        mCornerRadius = radius;
        return this;
    }


    @Override
    public void dismiss() {
        mFinished = true;
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
        super.dismiss();
    }

    public static EmiProgressDialog create(Context context) {
        return new EmiProgressDialog(context);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            mFinished = false;
        }
        super.show();
    }


    public EmiProgressDialog setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        return this;
    }

}
