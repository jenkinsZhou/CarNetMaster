package com.tourcoo.carnet.adapter;


import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.carnet.R;
import com.tourcoo.carnet.core.common.RequestConfig;
import com.tourcoo.carnet.core.frame.manager.GlideManager;
import com.tourcoo.carnet.core.widget.ratingstar.RatingStarView;
import com.tourcoo.carnet.entity.garage.GarageInfo;

import java.util.Arrays;
import java.util.List;


/**
 * @author :zhoujian
 * @description :修理厂适配器
 * @company :翼迈科技
 * @date 2019年 03月 18日 22时37分
 * @Email: 971613168@qq.com
 */
public class RepairDepotDescriptionAdapter extends BaseQuickAdapter<GarageInfo, BaseViewHolder> {
    public RepairDepotDescriptionAdapter() {
        super(R.layout.item_repir_depot);
    }

    @Override
    protected void convert(BaseViewHolder helper, GarageInfo item) {
        helper.setText(R.id.tvRepairDepotName, item.getName());
        helper.setText(R.id.tvRepairDepotDistance, item.getDistance() + "km");
        helper.setText(R.id.tvVisitCount, item.getVisit() + "人");
        helper.setText(R.id.tvPoint, item.getLevel() + "分");
        RatingStarView ratingStarView = helper.getView(R.id.rsvRating);
        if (ratingStarView != null) {
            ratingStarView.setEnabled(false);
            ratingStarView.setRating(item.getLevel());
        }
        if( item.getTagNames() == null){
            item.setTagNames("");
        }
        String[] tagNames = item.getTagNames().split(",");
        List<String> tagList = Arrays.asList(tagNames);
        if (!TextUtils.isEmpty(item.getImage())) {
            GlideManager.loadImg(RequestConfig.BASE + item.getImage(), helper.getView(R.id.rivRepairDepot));
        }
        if (tagList != null && !tagList.isEmpty()) {
            switch (tagList.size()) {
                case 1:
                    if (!TextUtils.isEmpty(tagList.get(0))) {
                        helper.setVisible(R.id.tvGoodField1, true);
                        helper.setVisible(R.id.tvGoodField2, false);
                        helper.setVisible(R.id.tvGoodField3, false);
                        setLabel(helper, R.id.tvGoodField1, tagList.get(0));
                    }

                    break;
                case 2:
                    setLabel(helper, R.id.tvGoodField1, tagList.get(0));
                    setLabel(helper, R.id.tvGoodField2, tagList.get(1));
                    helper.setVisible(R.id.tvGoodField1, true);
                    helper.setVisible(R.id.tvGoodField2, true);
                    helper.setVisible(R.id.tvGoodField3, false);
                    break;
                default:
                    setLabel(helper, R.id.tvGoodField1, tagList.get(0));
                    setLabel(helper, R.id.tvGoodField2, tagList.get(1));
                    setLabel(helper, R.id.tvGoodField3, tagList.get(2));
                    helper.setVisible(R.id.tvGoodField1, true);
                    helper.setVisible(R.id.tvGoodField2, true);
                    helper.setVisible(R.id.tvGoodField3, true);
                    break;
            }
        }
    }


    private void setLabel(BaseViewHolder helper, int id, CharSequence value) {
        if (TextUtils.isEmpty(value)) {
            helper.setVisible(id, false);
        } else {
            helper.setText(id, value);
        }
    }

}
