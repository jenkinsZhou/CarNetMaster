package com.tourcoo.carnet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tourcoo.carnet.core.log.TourcooLogUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.dialog.alert.EmiAlertDialog;
import com.tourcoo.carnet.core.widget.core.util.SizeUtil;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {
    private EmiAlertDialog emiAlertDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_main);
        context = this;
        initAlert();
        TourcooLogUtil.d("测试安达市大所多啥所发生奥术大师大大所多奥术大师大所多的");
      /*  findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emiAlertDialog.show();
                ToastUtil.show("测试一下吐司");
            }
        });*/
    }


    private void initAlert() {
        emiAlertDialog = new EmiAlertDialog.DividerQqBuilder(this)
                .setTitle("测试安达市大所多啥所发生奥术大师大大所多奥术大师大所多的")
                .setTitleTextColorResource(R.color.colorAlertTitle)
                .setMessage("1、本次更新修复多个重大爱仕达阿士大夫撒打发斯蒂芬BUG\n2、新阿斯顿发生发我AS的AS的AS的AS大神阿萨德AS的AS的奥术大师师范复古风格增用户反馈接口")
                .setMessageTextColorResource(R.color.colorAlertMessage)
                .setNegativeButton("否定", onAlertClick)
                .setNegativeButtonTextColorResource(R.color.colorAlertButton)
                .setPositiveButton("肯定", onAlertClick)
                .setMinHeight(SizeUtil.dp2px(160))
                .setPositiveButtonTextColorResource(R.color.colorAlertButton)
                .create();
        emiAlertDialog.setDimAmount(0.6f);
    }


    DialogInterface.OnClickListener onAlertClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg ;
            switch (which) {
                case BUTTON_NEGATIVE:
                    msg = "否定";
                    ToastUtil.showFailed(msg);
                    break;
                case BUTTON_POSITIVE:
                    msg = "肯定";
                    ToastUtil.showSuccess(msg);
                    break;
                case BUTTON_NEUTRAL:
                    msg = "中性";
                    break;
                default:
                    break;
            }
            if (dialog instanceof EmiAlertDialog) {
                msg = ((EmiAlertDialog) dialog).getButton(which).getText().toString();
            }
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        }
    };
}
