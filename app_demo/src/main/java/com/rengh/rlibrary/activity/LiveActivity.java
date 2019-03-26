
package com.rengh.rlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.rengh.library.common.handler.WeakHandler;
import com.rengh.library.common.handler.WeakHandlerListener;
import com.rengh.library.common.player.PlayerActivity;
import com.rengh.library.common.player.PlayerActivity2;
import com.rengh.library.common.player.PlayerHelper;
import com.rengh.library.common.player.PlayerItem;
import com.rengh.library.common.player.PlayerParams;
import com.rengh.library.common.util.FileUtils;
import com.rengh.library.common.util.LogUtils;
import com.rengh.library.common.util.ThreadManager;
import com.rengh.rlibrary.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveActivity extends AppCompatActivity implements WeakHandlerListener {
    private final String TAG = "LiveActivity";
    private Context mContext;
    private WeakHandler mWeakHandler;

    private GridView mGridView;
    private SimpleAdapter mGridAdapter;
    private List<Map<String, Object>> mGridItems;
    private View mBakSelectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        mContext = this;
        mWeakHandler = new WeakHandler(this);

        mGridView = findViewById(R.id.gridview);

        initGridView();

        processHtml();
    }

    @Override
    public void process(Message msg) {
        switch (msg.what) {
            case 1: {
                mGridItems = (List<Map<String, Object>>) msg.obj;
                LogUtils.i(TAG, "Item list: " + mGridItems);
                String[] from = {
                        "name"
                };
                int[] to = {
                        R.id.btn_grid_item
                };
                mGridAdapter = new SimpleAdapter(this, mGridItems, R.layout.layout_live_gridview_item, from, to);
                mGridView.setAdapter(mGridAdapter);
            }
                break;
        }
    }

    private void processHtml() {
        ThreadManager.getInstance().excuteCached(new Runnable() {
            @Override
            public void run() {
                List<Map<String, Object>> playerItemList = new ArrayList<>();
                InputStream in = FileUtils.getAssetsFileInputStream(mContext, "live_list.html");
                String html = FileUtils.getContent(in);
                if (!TextUtils.isEmpty(html)) {
                    String[] lines = html.split("\n");
                    if (null != lines) {
                        int number = 0;
                        for (int i = 0; i < lines.length; i++) {
                            String line = lines[i];
                            if (!TextUtils.isEmpty(line) && line.contains("移动端") && i >= 2) {
                                number++;
                                PlayerItem item = new PlayerItem();
                                String name = number + ". " + lines[i - 2]
                                        .replaceAll("<p>", "")
                                        .replaceAll("</p>", "")
                                        .trim();
                                String url = "http://ivi.bupt.edu.cn/" + lines[i]
                                        .substring(lines[i].indexOf("href=\"") + 6,
                                                lines[i].indexOf("\" target=\""));
                                LogUtils.i(TAG, "name: " + name + " url: " + url);
                                item.setName(name);
                                item.setPath(url);
                                item.setAd(false);
                                Map<String, Object> map = new HashMap<>();
                                map.put("name", item.getName());
                                map.put("item", item);
                                playerItemList.add(map);
                            }
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = playerItemList;
                mWeakHandler.sendMessage(msg);
            }
        });
    }

    private void initGridView() {
        mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != view) {
                    if (null != mBakSelectedView) {
                        mBakSelectedView.setBackgroundResource(R.drawable.dialog_btn_bg_narmal);
                    }
                    view.setBackgroundResource(R.drawable.dialog_btn_bg_focus);
                    mBakSelectedView = view;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (null != mBakSelectedView) {
                    mBakSelectedView.setBackgroundResource(R.drawable.dialog_btn_bg_narmal);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (mGridItems != null && mGridItems.size() > arg2) {
                    PlayerItem item = (PlayerItem) mGridItems.get(arg2).get("item");
                    LogUtils.i(TAG, "Item: " + item);
                    if (null != item) {
                        PlayerParams params = new PlayerParams();
                        params.setCoverDrawable(null);
                        params.setDoubleClick(false);
                        params.setAdVideo(false);
                        params.setAutoFinish(false);
                        params.setShowLoading(true);
                        params.setAutoFinishDelay(0);
                        params.setVideoTile(item.getName());
                        params.setVideoUri(Uri.parse(item.getPath()));
                        PlayerHelper.setPlayerParams(params);
                        Intent intent = new Intent();
                        intent.setClass(mContext, PlayerActivity2.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
