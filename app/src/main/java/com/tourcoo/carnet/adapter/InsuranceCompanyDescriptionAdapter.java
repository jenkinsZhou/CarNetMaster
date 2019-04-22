package com.tourcoo.carnet.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.util.TourCooUtil;
import com.tourcoo.carnet.entity.InsuranceCompany;


/**
 * @author :JenkinsZhou
 * @description :保险公司适配器
 * @company :途酷科技
 * @date 2019年03月18日17:13
 * @Email: 971613168@qq.com
 */
public class InsuranceCompanyDescriptionAdapter extends BaseQuickAdapter<InsuranceCompany.CompanyInfo, BaseViewHolder> {
    public InsuranceCompanyDescriptionAdapter() {
        super(R.layout.item_insurance_company_description);
    }

    @Override
    protected void convert(BaseViewHolder helper, InsuranceCompany.CompanyInfo companyInfo) {
        helper.setText(R.id.tvInsuranceCompanyDistance, companyInfo.getDistance()+"km");
        helper.setText(R.id.tvInsuranceCompanyName, getNotNullValue(companyInfo.getInsuranceName()));
        helper.setText(R.id.tvInsuranceCompanyPhone, getPhone(companyInfo));
        helper.setText(R.id.tvInsuranceCompanyAddress, getNotNullValue(companyInfo.getAddress()));
    }

    private String getNotNullValue(String value) {
        return TourCooUtil.getNotNullValue(value);
    }

    private String getPhone(InsuranceCompany.CompanyInfo companyInfo) {
        String value = "电话:";
        value += companyInfo.getTeleAreaCode() + "-";
        value += companyInfo.getTelephone();
        return value;
    }
}
