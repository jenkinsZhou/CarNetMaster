package com.tourcoo.carnet.ui.car;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.frame.base.activity.BaseTourCooTitleActivity;
import com.tourcoo.carnet.core.frame.retrofit.BaseLoadingObserver;
import com.tourcoo.carnet.core.frame.util.KeyboardUtil;
import com.tourcoo.carnet.core.log.TourCooLogUtil;
import com.tourcoo.carnet.core.log.widget.utils.DateUtil;
import com.tourcoo.carnet.core.util.ToastUtil;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.builder.TimePickerBuilder;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.view.OptionsPickerView;
import com.tourcoo.carnet.core.widget.bigkoo.pickerview.view.TimePickerView;
import com.tourcoo.carnet.core.widget.core.view.titlebar.TitleBarView;
import com.tourcoo.carnet.entity.BaseEntity;
import com.tourcoo.carnet.entity.car.CarCategory;
import com.tourcoo.carnet.entity.car.CarFaultRemindType;
import com.tourcoo.carnet.entity.car.CarMaintainRule;
import com.tourcoo.carnet.entity.car.CarModel;
import com.tourcoo.carnet.entity.car.ObdReceiveMode;
import com.tourcoo.carnet.entity.event.CarRefreshEvent;
import com.tourcoo.carnet.retrofit.ApiRepository;
import com.trello.rxlifecycle3.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.AppCompatEditText;

import static com.tourcoo.carnet.core.common.RequestConfig.CODE_REQUEST_SUCCESS;

/**
 * NSDictionary *dic = @{@"modelId":model_id,
 *
 * @"displacement":displacement,
 * @"engine":engine,
 * @"insurer":insurer,
 * @"yearly":yearly,
 * @"insurerRemind":@(insurer_remind),
 * @"yearlyRemind":@(yearly_remind),
 * @"insurerDate":insurer_date,
 * @"yearlyDate":yearly_date,
 * @"maintain":maintain,
 * @"maintainRule":maintain_rule,
 * @"maintainRemind":@(maintain_remind),
 * @"maintainDate":maintain_date,
 * @"faultRemind":fault_remind,
 * @"obdSn":obdSn,
 * @"obdReceive":obd_receive};
 */

/**
 * @author :zhoujian
 * @description :添加车辆
 * @company :翼迈科技
 * @date 2019年 04月 06日 22时25分
 * @Email: 971613168@qq.com
 */
public class AddCarActivity extends BaseTourCooTitleActivity implements View.OnClickListener {
    private static final String EMPTY = "下拉选择";
    private TimePickerView timePicker;
    private OptionsPickerView opvCarCategory;
    private OptionsPickerView opvCarModel;
    private OptionsPickerView opvCarMaintainRule;
    private OptionsPickerView opvObdReceiveMode;
    /**
     * 故障提醒模式
     */
    private OptionsPickerView opvFaultRemind;

    private TextView tvCarCategory;
    private TextView tvCarModel;
    private KeyboardUtil keyboardUtil;
    /**
     * 当前选择的车辆类型
     */
    private CarCategory mCurrentCategory;
    private CarModel mCurrentCarModel;
    private CarMaintainRule mCurrentCarMaintainRule;
    private CarFaultRemindType mCurrentCarFaultRemindType;
    private ObdReceiveMode mCurrentObdReceiveMode;


    private AppCompatEditText etEngineNumber;
    /**
     * 排量
     */
    private AppCompatEditText etDisplacement;
    /**
     * 保险信息
     */
    private AppCompatEditText etCarInsuranceInfo;

    /**
     * 年检信息
     */
    private AppCompatEditText etCarYearlyInspection;

    /**
     * 保险提醒开关
     */
    private Switch switchRemindInsurance;

    /**
     * 年检提醒开关
     */
    private Switch switchRemindYearlyInspection;


    /**
     * 上次保险时间
     */
    private TextView tvLastInsuranceTime;


    /**
     * 上次年检时间
     */
    private TextView tvLastYearlyInspectionTime;

    /**
     * 上次保养时间
     */
    private TextView tvLastMaintainTime;


    /**
     * 保养提醒开关
     */
    private Switch switchRemindMaintain;

    /**
     * 故障提醒开关
     */
    private LinearLayout llFaultRemindType;
    private TextView tvMaintainRule;

    private TextView tvObdReceiveMode;

    private TextView tvFaultRemindType;
    private AppCompatEditText etObdNumber;

    /**
     * 车辆保养信息
     */
    private AppCompatEditText etCarMaintainInfo;

    private List<CarCategory> mCarCategoryList = new ArrayList<>();
    private List<CarModel> mCarModelList = new ArrayList<>();
    private List<CarMaintainRule> mCarMaintainRuleList = new ArrayList<>();
    private List<CarFaultRemindType> mCarFaultRemindList = new ArrayList<>();
    private List<ObdReceiveMode> mObdReceiveModeList = new ArrayList<>();
    private EditText etPlateNumber;
    /**
     * 时间选择类型 0 表示上次保险时间 1 表示 上次年检时间
     */
    private int mTimeSelectType;
    private static final int TYPE_INSURANCE = 0;

    private static final int TYPE_YEARLY_INSPECTION = 1;

    private static final int TYPE_MAINTAIN = 2;

    @Override
    public int getContentLayout() {
        return R.layout.activity_add_car;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTimePicker();
        initCarCategoryOptionsPicker();
        initCarModelPicker();
        initCarMaintainRulePicker();
        initOpvObdReceiveModePicker();
        initFaultRepairTypePicker();
        tvCarCategory = findViewById(R.id.tvCarCategory);
        tvCarModel = findViewById(R.id.tvCarModel);
        tvMaintainRule = findViewById(R.id.tvMaintainRule);
        etPlateNumber = findViewById(R.id.etPlateNumber);
        tvFaultRemindType = findViewById(R.id.tvFaultRemindType);
        tvObdReceiveMode = findViewById(R.id.tvObdReceiveMode);
        etEngineNumber = findViewById(R.id.etEngineNumber);
        etDisplacement = findViewById(R.id.etDisplacement);
        etCarMaintainInfo = findViewById(R.id.etCarMaintainInfo);
        etCarInsuranceInfo = findViewById(R.id.etCarInsuranceInfo);
        etCarYearlyInspection = findViewById(R.id.etCarYearlyInspection);
        switchRemindInsurance = findViewById(R.id.switchRemindInsurance);
        switchRemindYearlyInspection = findViewById(R.id.switchRemindYearlyInspection);
        tvLastInsuranceTime = findViewById(R.id.tvLastInsuranceTime);
        tvLastYearlyInspectionTime = findViewById(R.id.tvLastYearlyInspectionTime);
        tvLastMaintainTime = findViewById(R.id.tvLastMaintainTime);
        switchRemindMaintain = findViewById(R.id.switchRemindMaintain);
        llFaultRemindType = findViewById(R.id.llFaultRemindType);
        etObdNumber = findViewById(R.id.etObdNumber);
        llFaultRemindType.setOnClickListener(this);
        findViewById(R.id.llCarCategory).setOnClickListener(this);
        findViewById(R.id.llCarMode).setOnClickListener(this);
        findViewById(R.id.llLastInsuranceDate).setOnClickListener(this);
        findViewById(R.id.llLastYearlyInspectionDate).setOnClickListener(this);
        findViewById(R.id.llMaintainRule).setOnClickListener(this);
        findViewById(R.id.tvSaveCarInfo).setOnClickListener(this);
        findViewById(R.id.llObdReceiveMode).setOnClickListener(this);
        findViewById(R.id.llLastMaintainDate).setOnClickListener(this);
        initKeyBoard();
        listenKeyboard(etEngineNumber);
        listenKeyboard(etDisplacement);
        listenKeyboard(etCarInsuranceInfo);
        listenKeyboard(etCarMaintainInfo);
        listenKeyboard(etCarYearlyInspection);

    }

    private String getTime(Date date) {
        //可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * Dialog 模式下，在底部弹出
     */
    private void initTimePicker() {
        timePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String selectedTime = DateUtil.getDateString(date);
                switch (mTimeSelectType) {
                    case TYPE_INSURANCE:
                        setLastInsuranceTime(selectedTime);
                        break;
                    case TYPE_YEARLY_INSPECTION:
                        setLastYearlyInspectionTime(selectedTime);
                        break;
                    case TYPE_MAINTAIN:
                        setLastMaintainTime(selectedTime);
                        break;
                    default:
                        break;
                }
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                //默认设置false ，内部实现将DecorView 作为它的父控件。
                .isDialog(true)
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .build();

        Dialog mDialog = timePicker.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            timePicker.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM);
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //车辆品类选择
            case R.id.llCarCategory:
                keyboardUtil.hideKeyboard();
                findCategoryList();
                break;
            //车辆型号选择(必须先选择车辆品类)
            case R.id.llCarMode:
                keyboardUtil.hideKeyboard();
                findCarModelList();
                break;
            //车辆保养规则
            case R.id.llMaintainRule:
                keyboardUtil.hideKeyboard();
                findMaintainRuleList();
                break;
            //上次保险时间
            case R.id.llLastInsuranceDate:
                keyboardUtil.hideKeyboard();
                mTimeSelectType = TYPE_INSURANCE;
                timePicker.show();
                break;
            //上次年检时间
            case R.id.llLastYearlyInspectionDate:
                keyboardUtil.hideKeyboard();
                mTimeSelectType = TYPE_YEARLY_INSPECTION;
                timePicker.show();
                break;
            case R.id.tvSaveCarInfo:
                keyboardUtil.hideKeyboard();
                addCar();
                break;
            case R.id.llObdReceiveMode:
                keyboardUtil.hideKeyboard();
                findObdReceiveList();
                break;
            //故障提醒类型选择
            case R.id.llFaultRemindType:
                keyboardUtil.hideKeyboard();
                findFaultRemindList();
                break;
            //上次保养时间
            case R.id.llLastMaintainDate:
                keyboardUtil.hideKeyboard();
                mTimeSelectType = TYPE_MAINTAIN;
                timePicker.show();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化车辆品类选择器
     */
    @SuppressWarnings("uncheked")
    private void initCarCategoryOptionsPicker() {
        // 不联动
        opvCarCategory = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mCurrentCategory = mCarCategoryList.get(options1);
                tvCarCategory.setText(mCurrentCategory.getName());
                resetCarModel();
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvCarCategory.setNPicker(mCarCategoryList, null, null);
        opvCarCategory.setSelectOptions(0, 1, 1);
    }

    /**
     * 初始化车辆型号选择器
     */
    @SuppressWarnings("uncheked")
    private void initCarModelPicker() {
        // 不联动
        opvCarModel = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mCarModelList.isEmpty()) {
                    ToastUtil.show("未获取到车辆型号");
                    return;
                }
                mCurrentCarModel = mCarModelList.get(options1);
                tvCarModel.setText(mCurrentCarModel.getName());
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvCarModel.setNPicker(mCarModelList, null, null);
        opvCarModel.setSelectOptions(0, 1, 1);
    }

    /**
     * 初始化保险规则
     */
    @SuppressWarnings("uncheked")
    private void initCarMaintainRulePicker() {
        // 不联动
        opvCarMaintainRule = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mCarMaintainRuleList.isEmpty()) {
                    ToastUtil.show("未获取到保险规则");
                    return;
                }
                mCurrentCarMaintainRule = mCarMaintainRuleList.get(options1);
                tvMaintainRule.setText(getMaintainRule());
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvCarMaintainRule.setNPicker(mCarMaintainRuleList, null, null);
        opvCarMaintainRule.setSelectOptions(0, 1, 1);
    }


    private String getFaultRemindType() {
        if (mCurrentCarFaultRemindType != null) {
            return mCurrentCarFaultRemindType.getName();
        }
        return "";
    }

    /**
     * 初始化故障提醒类型控件
     */
    @SuppressWarnings("uncheked")
    private void initFaultRepairTypePicker() {
        // 不联动
        opvFaultRemind = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mCarFaultRemindList.isEmpty()) {
                    ToastUtil.show("未获取到提醒规则");
                    return;
                }
                mCurrentCarFaultRemindType = mCarFaultRemindList.get(options1);
                tvFaultRemindType.setText(mCurrentCarFaultRemindType.getName());
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvFaultRemind.setNPicker(mCarFaultRemindList, null, null);
        opvFaultRemind.setSelectOptions(0, 1, 1);
    }

    /**
     * 初始化OBD接收模式
     */
    @SuppressWarnings("uncheked")
    private void initOpvObdReceiveModePicker() {
        // 不联动
        opvObdReceiveMode = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (mObdReceiveModeList.isEmpty()) {
                    ToastUtil.show("未获取到OBD模式信息");
                    return;
                }
                mCurrentObdReceiveMode = mObdReceiveModeList.get(options1);
                setObdReceiveMode(mCurrentObdReceiveMode.getName());
                tvObdReceiveMode.setText(getObdReceiveMode());
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {

                    }
                })
                // .setSelectOptions(0, 1, 1)
                .build();
        opvObdReceiveMode.setNPicker(mObdReceiveModeList, null, null);
        opvObdReceiveMode.setSelectOptions(0, 1, 1);
    }


    /**
     * 获取汽车品类
     */
    private void findCategoryList() {
        ApiRepository.getInstance().findCategoryList().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                mCarCategoryList.clear();
                                mCarCategoryList.addAll(getCarCategoryList(entity.data));
                                opvCarCategory.show();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    private List<CarCategory> getCarCategoryList(Object object) {
        List<CarCategory> carCategoryList = new ArrayList<>();
        try {
            return JSONObject.parseArray(JSON.toJSONString(object), CarCategory.class);
        } catch (Exception e) {
            return carCategoryList;
        }
    }

    private List<CarModel> getCarModelList(Object object) {
        List<CarModel> carCategoryList = new ArrayList<>();
        try {
            List<CarModel> carModelList = JSONObject.parseArray(JSON.toJSONString(object), CarModel.class);
            if (carModelList != null) {
                for (int i = carModelList.size() - 1; i >= 0; i--) {
                    if (carModelList.get(i) == null || TextUtils.isEmpty(carModelList.get(i).getName())) {
                        carModelList.remove(i);
                        TourCooLogUtil.i(TAG, "移除了一个空数据");
                    }
                }
            }
            return carModelList;
        } catch (Exception e) {
            return carCategoryList;
        }
    }

    private List<CarMaintainRule> getCarMaintainRuleList(Object object) {
        List<CarMaintainRule> carCategoryList = new ArrayList<>();
        try {
            return JSONObject.parseArray(JSON.toJSONString(object), CarMaintainRule.class);
        } catch (Exception e) {
            return carCategoryList;
        }
    }

    private List<ObdReceiveMode> getObdReceiveList(Object object) {
        List<ObdReceiveMode> carCategoryList = new ArrayList<>();
        try {
            return JSONObject.parseArray(JSON.toJSONString(object), ObdReceiveMode.class);
        } catch (Exception e) {
            return carCategoryList;
        }
    }

    /**
     * 根据车辆品类ID获取汽车型号
     */
    private void findCarModelList() {
        if (mCurrentCategory == null || TextUtils.isEmpty(mCurrentCategory.getName())) {
            ToastUtil.show("请先选择车辆品类");
            return;
        }
        ApiRepository.getInstance().findCarModelList(mCurrentCategory.getId() + "").compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                mCarModelList.clear();
                                mCarModelList.addAll(getCarModelList(entity.data));
                                initCarModelPicker();
                                opvCarModel.show();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    /**
     * 获取输入的发动机编号
     *
     * @return
     */
    private String getCarEngineNumber() {
        return etEngineNumber.getText().toString();
    }

    /**
     * 获取汽车排量
     *
     * @return
     */
    private String getCarDisplacement() {
        return etDisplacement.getText().toString();
    }

    /**
     * 获取汽车保险信息
     *
     * @return
     */
    private String getCarInsuranceInfo() {
        return etCarInsuranceInfo.getText().toString();
    }

    /**
     * 获取保养信息
     *
     * @return
     */
    private String getCarMaintainInfo() {
        return etCarMaintainInfo.getText().toString();
    }

    /**
     * 获取汽车年检信息
     *
     * @return
     */
    private String getCarYearlyInspection() {
        return etCarYearlyInspection.getText().toString();
    }

    /**
     * 获取保养规则
     *
     * @return
     */
    private String getMaintainRule() {
        if (mCurrentCarMaintainRule != null) {
            return mCurrentCarMaintainRule.getDescription();
        }
        return "";
    }


    /**
     * 是否开启保险提醒 1代表提醒 0代表不提醒
     *
     * @return
     */
    private int isRemindInsurance() {
        if (switchRemindInsurance.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * 是否开启年检提醒
     *
     * @return
     */
    private int isRemindYearlyInspection() {
        if (switchRemindYearlyInspection.isChecked()) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取上次保险时间
     */
    private String getLastInsuranceTime() {
        return tvLastInsuranceTime.getText().toString();
    }

    /**
     * 获取上次年检时间
     */
    private String getLastYearlyInspectionTime() {
        return tvLastYearlyInspectionTime.getText().toString();
    }


    /**
     * 获取上次保养时间
     */
    private String getLastMaintainTime() {
        return tvLastMaintainTime.getText().toString();
    }

    /**
     * 是否开启保养提醒
     *
     * @return
     */
    private int isRemindMaintain() {
        if (switchRemindMaintain.isChecked()) {
            return 1;
        }
        return 0;
    }

    private void resetCarModel() {
        tvCarModel.setText(EMPTY);
    }

    /**
     * 添加车辆
     */
    private void addCar() {
        if (TextUtils.isEmpty(getPlatNumber())) {
            ToastUtil.show("请输入车牌号");
            return;
        }
       /* if(!TourcooUtil.isCarNumber(getPlatNumber())){
            ToastUtil.show("请输入正确的车牌号");
            return;
        }*/
        if (mCurrentCategory == null) {
            ToastUtil.show("请选择车辆品类");
            return;
        }
        if (mCurrentCarModel == null) {
            ToastUtil.show("请选择车辆型号");
            return;
        }
        if (TextUtils.isEmpty(getCarEngineNumber())) {
            ToastUtil.show("请输入发动机编号");
            return;
        }
        if (TextUtils.isEmpty(getLastInsuranceTime()) || EMPTY.equals(getLastInsuranceTime())) {
            ToastUtil.show("请选择上次保险时间");
            return;
        }
        if (TextUtils.isEmpty(getLastYearlyInspectionTime()) || EMPTY.equals(getLastYearlyInspectionTime())) {
            ToastUtil.show("请选择上次年检时间");
            return;
        }
        if (TextUtils.isEmpty(getLastMaintainTime()) || EMPTY.equals(getLastMaintainTime())) {
            ToastUtil.show("请选择上次保养时间");
            return;
        }
        if (mCurrentCarMaintainRule == null || TextUtils.isEmpty(getMaintainRule()) || EMPTY.equals(getMaintainRule())) {
            ToastUtil.show("请选择保养规则");
            return;
        }
        if (mCurrentCarFaultRemindType == null || TextUtils.isEmpty(getFaultRemindType()) || EMPTY.equals(getFaultRemindType())) {
            ToastUtil.show("请选择故障提醒规则");
            return;
        }
      /*  if (TextUtils.isEmpty(getObdNumber())) {
            ToastUtil.show("请输入OBD编号");
            return;
        }*/
        if (mCurrentObdReceiveMode == null || TextUtils.isEmpty(getObdReceiveMode()) || EMPTY.equals(getObdReceiveMode())) {
            ToastUtil.show("请选择OBD接收模式");
            return;
        }
        Map<String, Object> hashMap = new HashMap<>(15);
        //车辆类型
        hashMap.put("modelId", mCurrentCarModel.getId());
        //发动机编号(必填)
        hashMap.put("engine", getCarEngineNumber());
        //保养规则
        hashMap.put("maintainRule", mCurrentCarMaintainRule.getName());
        hashMap.put("faultRemind", mCurrentCarFaultRemindType.getName());
        //obd编号
        hashMap.put("obdReceive", mCurrentObdReceiveMode.getName());
        //TODO：车牌号
        hashMap.put("plateNum", getPlatNumber());
        //上次保险时间
        hashMap.put("insurerDate", getLastInsuranceTime());
        //上次年检时间
        hashMap.put("yearlyDate", getLastYearlyInspectionTime());
        //上次保养时间
        hashMap.put("maintainDate", getLastMaintainTime());
        TourCooLogUtil.i(TAG, "getLastYearlyInspectionTime时间:" + getLastYearlyInspectionTime());
        TourCooLogUtil.i(TAG, "getLastInsuranceTime时间:" + getLastInsuranceTime());
        TourCooLogUtil.i(TAG, "getLastMaintainTime时间:" + getLastMaintainTime());
        //非必须参数
        HashMap<String, Object> nonEssentialParams = getNonEssentialParams();
        for (Map.Entry<String, Object> entry : nonEssentialParams.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            TourCooLogUtil.i(TAG, entry.getKey() + "：" + entry.getValue());
        }
        ApiRepository.getInstance().addCar(hashMap).compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                ToastUtil.showSuccess("添加成功");
                                setResult(RESULT_OK);
                                EventBus.getDefault().post(new CarRefreshEvent());
                                finish();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    /**
     * 获取非必填项参数
     */
    private HashMap<String, Object> getNonEssentialParams() {
        /**
         * NSDictionary *dic = @{@"modelId":model_id,
         *                           @"displacement":displacement,
         *                           @"engine":engine,
         *                           @"insurer":insurer,
         *                           @"yearly":yearly,
         *                           @"insurerRemind":@(insurer_remind),
         *                           @"yearlyRemind":@(yearly_remind),
         *                           @"insurerDate":insurer_date,
         *                           @"yearlyDate":yearly_date,
         *                           @"maintain":maintain,
         *                           @"maintainRule":maintain_rule,
         *                           @"maintainRemind":@(maintain_remind),
         *                           @"maintainDate":maintain_date,
         *                           @"faultRemind":fault_remind,
         *                           @"obdSn":obdSn,
         *                           @"obdReceive":obd_receive};
         */
        HashMap<String, Object> hashMap = new HashMap<>(15);
        //汽车排量
        hashMap.put("displacement", getCarDisplacement());
        //车辆保险信息
        hashMap.put("insurer", getCarInsuranceInfo());
        //车辆年检信息
        hashMap.put("yearly", getCarYearlyInspection());
        //是否保险提醒
        hashMap.put("insurerRemind", isRemindInsurance());
        //是否年检提醒
        hashMap.put("yearlyRemind", isRemindYearlyInspection());
        //车辆保养信息
        hashMap.put("maintain", getCarMaintainInfo());
        //是否开启保养提醒
        hashMap.put("maintainRemind", isRemindMaintain());
        //是否开启故障提醒
        hashMap.put("faultRemind", getFaultRemindType());
        if (!TextUtils.isEmpty(getObdNumber())) {
            try {
                int number = Integer.parseInt(getObdNumber());
                hashMap.put("obdSn", number);
            } catch (Exception e) {
                TourCooLogUtil.e(TAG, e.toString());
            }
        }

        return hashMap;
    }

    /**
     * 获取Obd编号
     *
     * @return
     */
    private String getObdNumber() {
        return etObdNumber.getText().toString();
    }

    /**
     * 获取保养规则列表
     */
    private void findMaintainRuleList() {
        ApiRepository.getInstance().findMaintainRuleList().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                mCarMaintainRuleList.clear();
                                mCarMaintainRuleList.addAll(getCarMaintainRuleList(entity.data));
                                opvCarMaintainRule.show();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    /**
     * 获取规则列表
     */
    private void findFaultRemindList() {
        ApiRepository.getInstance().findFaultRemindList().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity<List<CarFaultRemindType>>>() {
                    @Override
                    public void onRequestNext(BaseEntity<List<CarFaultRemindType>> entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                if (entity.data != null) {
                                    mCarFaultRemindList.clear();
                                    TourCooLogUtil.i("数据长度：" + entity.data.size());
                                    mCarFaultRemindList.addAll(entity.data);
                                    opvFaultRemind.show();
                                }
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }


    /**
     * 获取保养规则列表
     */
    private void findObdReceiveList() {
        ApiRepository.getInstance().findObdReceiveList().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseEntity>() {
                    @Override
                    public void onRequestNext(BaseEntity entity) {
                        if (entity != null) {
                            if (entity.code == CODE_REQUEST_SUCCESS) {
                                mObdReceiveModeList.clear();
                                mObdReceiveModeList.addAll(getObdReceiveList(entity.data));
                                opvObdReceiveMode.show();
                            } else {
                                ToastUtil.showFailed(entity.message);
                            }
                        }
                    }
                });
    }

    private void setLastInsuranceTime(String time) {
        tvLastInsuranceTime.setText(time);
    }

    private void setLastYearlyInspectionTime(String time) {
        tvLastYearlyInspectionTime.setText(time);
    }

    private void setLastMaintainTime(String time) {
        tvLastMaintainTime.setText(time);
    }

    private String getObdReceiveMode() {
        return tvObdReceiveMode.getText().toString();
    }

    private void setObdReceiveMode(String text) {
        tvObdReceiveMode.setText(text);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    private String getPlatNumber() {
        return etPlateNumber.getText().toString();
    }

    private void initKeyUtil() {
        if (keyboardUtil == null) {
            keyboardUtil = new KeyboardUtil(AddCarActivity.this, etPlateNumber);
        }
    }

    private void initKeyBoard() {
        initKeyUtil();
        etPlateNumber.setInputType(InputType.TYPE_NULL);
        etPlateNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(AddCarActivity.this, etPlateNumber);
                    keyboardUtil.hideSoftInputMethod();
                    keyboardUtil.showKeyboard();
                } else {
                    keyboardUtil.showKeyboard();
                }
                return false;
            }
        });


        etPlateNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("字符变换后", "afterTextChanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("字符变换前", s + "-" + start + "-" + count + "-" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("字符变换中", s + "-" + "-" + start + "-" + before + "-" + count);
            }
        });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("添加车辆");
    }



    private void listenKeyboard(EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.hideKeyboard();
                return false;
            }
        });
    }
}
