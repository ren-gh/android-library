
package com.r.library.common.view.viewpager;

import com.r.library.common.glide.GlideHelper;
import com.r.library.common.handler.WeakHandler;
import com.r.library.common.util.LogUtils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;

public class GalleryTransformer implements ViewPager.PageTransformer {
    private final String TAG = "GalleryTransformer";
    private Context mContext;
    private WeakHandler mHandler;
    private Animation mFirstAnimation;
    private int mNormalResId;
    private int mFocusResId;

    private static final float MAX_ALPHA = 0.75f;
    private static final float MAX_SCALE = 0.75f;

    private static final float MAX_ROTATION = 35f;// left
    private static final float MIN_ROTATION = -35f;// right

    private boolean mAnimationRun = true;
    private boolean mHasFocus = false;

    public GalleryTransformer(Context context, WeakHandler weakHandler) {
        this(context, weakHandler, null, -1, -1);
    }

    public GalleryTransformer(Context context, WeakHandler weakHandler, Animation animation) {
        this(context, weakHandler, animation, -1, -1);
    }

    public GalleryTransformer(Context context, WeakHandler weakHandler, int normalRes, int focusRes) {
        this(context, weakHandler, null, normalRes, focusRes);
    }

    public GalleryTransformer(Context context, WeakHandler weakHandler, Animation animation, int normalRes, int focusRes) {
        init(context, weakHandler, animation, normalRes, focusRes);
    }

    private void init(Context context, WeakHandler weakHandler, Animation animation, int normalRes, int focusRes) {
        mContext = context;
        mHandler = weakHandler;
        mFirstAnimation = animation;
        mNormalResId = normalRes;
        mFocusResId = focusRes;
    }

    public void clearAnimationFlag() {
        mAnimationRun = false;
    }

    public void setFocus(boolean hasFocus, View currentView) {
        mHasFocus = hasFocus;
        transformPage(currentView, 0);
    }

    private View[] views = new View[3];

    @Override
    public void transformPage(final View page, float position) {
        runFirstAnimation(page, position);
        setItemState(page, position);
    }

    private void setItemState(View page, float position) {
        if (position <= -1 || position >= 1) {
            if (-1 != mNormalResId) {
                GlideHelper.load2ViewBgRes(mContext, page, mNormalResId);
            }
            page.setAlpha(MAX_ALPHA);
            page.setScaleX(MAX_SCALE);
            page.setScaleY(MAX_SCALE);
            if (position <= -1) {
                page.setRotationY(MAX_ROTATION);
            } else if (position >= 1) {
                page.setRotationY(MIN_ROTATION);
            }
        } else if (position == 0) {
            LogUtils.i(TAG, "position: " + position + ", focus: " + mHasFocus);
            if (mHasFocus) {
                if (-1 != mFocusResId) {
                    GlideHelper.load2ViewBgRes(mContext, page, mFocusResId);
                }
            } else {
                if (-1 != mNormalResId) {
                    GlideHelper.load2ViewBgRes(mContext, page, mNormalResId);
                }
            }
            page.setAlpha(1f);
            page.setScaleX(1f);
            page.setScaleY(1f);
            page.setRotationY(0f);
        } else {
            if (-1 != mNormalResId) {
                GlideHelper.load2ViewBgRes(mContext, page, mNormalResId);
            }
            if (position < 0) {
                page.setAlpha(MAX_ALPHA + MAX_ALPHA * (1 + position));
                page.setRotationY((-1 * MAX_ROTATION) * position);
            } else {
                page.setAlpha(MAX_ALPHA + MAX_ALPHA * (1 - position));
                page.setRotationY(MIN_ROTATION * position);
            }
            float scale = Math.max(MAX_SCALE, 1 - Math.abs(position));
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
    }

    private void runFirstAnimation(final View page, float position) {
        if (null == mFirstAnimation) {
            return;
        }
        if (mAnimationRun) {
            page.setVisibility(View.GONE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean canRun = true;
                    for (int i = 0; i < views.length; i++) {
                        if (page == views[i]) {
                            canRun = false;
                            break;
                        }
                    }
                    LogUtils.i(TAG, "canRun: " + canRun + ", view: " + page);
                    if (!canRun) {
                        page.setVisibility(View.VISIBLE);
                        return;
                    }
                    boolean beNull = false;
                    for (int i = 0; i < views.length; i++) {
                        if (views[i] == null) {
                            views[i] = page;
                            beNull = true;
                            break;
                        }
                    }
                    if (!beNull) {
                        page.setVisibility(View.VISIBLE);
                        return;
                    }
                    page.startAnimation(mFirstAnimation);
                    page.setVisibility(View.VISIBLE);
                }
            }, (int) (300 * position));
        }
    }
}
