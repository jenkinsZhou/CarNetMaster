package com.tourcoo.carnet.core.widget.custom;

/**
 * @author :zhoujian
 * @description :进度相关接口
 * @company :翼迈科技
 * @date 2019年 03月 23日 21时46分
 * @Email: 971613168@qq.com
 */
public interface IProgress {
    /**
     * 设置最大进度
     *
     * @param maxProgress:最大进度
     */
    void setMax(int maxProgress);

    /**
     * 设置当前进度
     *
     * @param progress
     */
    void setProgress(int progress);
}
