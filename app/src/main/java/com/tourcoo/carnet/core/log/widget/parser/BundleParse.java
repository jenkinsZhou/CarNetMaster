package com.tourcoo.carnet.core.log.widget.parser;

import android.os.Bundle;

import com.tourcoo.carnet.core.log.widget.utils.ObjectUtil;


/**
 * @author :zhoujian
 * @description : Bundle解析器
 * @company :翼迈科技
 * @date 2018年09月07日下午 03:09
 * @Email: 971613168@qq.com
 */

public class BundleParse implements IParser<Bundle> {
    @Override
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    @Override
    public String parseString(Bundle bundle) {
        if (bundle != null) {
            StringBuilder builder = new StringBuilder(bundle.getClass().getName());
            builder.append(" [");
            builder.append(LINE_SEPARATOR);
            for (String key : bundle.keySet()) {
                builder.append(String.format("'%s' => %s " + LINE_SEPARATOR,
                        key, ObjectUtil.objectToString(bundle.get(key))));
            }
            builder.append("]");
            return builder.toString();
        }
        return "null";
    }
}
