package com.tourcoo.carnet.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.previewlibrary.loader.IZoomMediaLoader;
import com.previewlibrary.loader.MySimpleTarget;
import com.tourcoo.carnet.R;

/**
 * @author :JenkinsZhou
 * @description :图片加载器
 * @company :途酷科技
 * @date 2019年05月16日16:54
 * @Email: 971613168@qq.com
 */
public class GlideImageLoader implements IZoomMediaLoader {


    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {
        loadImage(context, imageView, path,simpleTarget);
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final MySimpleTarget simpleTarget) {
        loadImage(context, imageView, path,simpleTarget);
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();

    }

    @Override
    public void clearMemory(@NonNull Context c) {
        Glide.get(c).clearMemory();
    }

    private void loadImage(Fragment context, ImageView image, String url,MySimpleTarget simpleTarget) {
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.img_placeholder)
                .error(R.mipmap.img_placeholder)
                .priority(Priority.HIGH)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }
                })
                .apply(options)
                //.thumbnail(Glide.with(this).load(R.mipmap.ic_launcher))
                .into(image);
    }

}
