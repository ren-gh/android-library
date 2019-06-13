
package com.r.library.demo.tangram.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.r.library.demo.R;
import com.r.library.demo.tangram.view.CustomCellView;
import com.tmall.wireless.tangram.MVHelper;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.ExposureSupport;

import android.support.annotation.NonNull;

public class CustomCell extends BaseCell<CustomCellView> {
    private String imageUrl;
    private String text;

    @Override
    public void parseWith(@NonNull JSONObject data, @NonNull MVHelper resolver) {
        try {
            if (data.has("imageUrl")) {
                imageUrl = data.getString("imageUrl");
            }
            if (data.has("text")) {
                text = data.getString("text");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindView(@NonNull CustomCellView view) {
        view.setImageUrl(imageUrl);
        view.setText(view.getClass().getSimpleName() + " text(" + pos + "): " + text);
        view.setTextColor(view.getResources().getColor(R.color.colorWhite));
        view.setFocusable(true);
        view.setBackgroundResource(R.drawable.view_bg);
        view.setClickable(true);
        view.setOnClickListener(this);
        if (serviceManager != null) {
            ExposureSupport exposureSupport = serviceManager.getService(ExposureSupport.class);
            if (exposureSupport != null) {
                exposureSupport.onTrace(view, this, type);
            }
        }
    }

}
