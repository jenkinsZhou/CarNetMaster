package com.tourcoo.carnet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.entity.InsuranceCompanyEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author :JenkinsZhou
 * @description :保险公司描述适配器
 * @company :途酷科技
 * @date 2019年03月18日17:13
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyDescriptionAdapter extends BaseQuickAdapter<InsuranceCompanyEntity, BaseViewHolder> {
    public InsuranceCompanyDescriptionAdapter(@Nullable List<InsuranceCompanyEntity> data) {
        super(R.layout.item_insurance_company_description, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InsuranceCompanyEntity companyEntity) {
        helper.setText(R.id.tvInsuranceCompanyDistance, companyEntity.companyDistance);
        helper.setText(R.id.tvInsuranceCompanyName, companyEntity.companyName);
        helper.setText(R.id.tvInsuranceCompanyPhone, companyEntity.companyPhone);
        helper.setText(R.id.tvInsuranceCompanyAddress, companyEntity.companyAddress);
    }
}
