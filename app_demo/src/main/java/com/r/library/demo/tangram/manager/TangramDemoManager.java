
package com.r.library.demo.tangram.manager;

import com.bumptech.glide.Glide;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.util.IInnerImageSetter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class TangramDemoManager {
    public static void init(Context context) {
        TangramBuilder.init(context, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view, @Nullable String url) {
                Glide.with(context).load(url).into(view);
            }
        }, ImageView.class);
    }
}
