
package com.r.library.demo.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxSeekBar;
import com.r.library.demo.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import kotlin.Unit;

public class RecyclerDemoActivity extends AppCompatActivity {
    private Context mContext;
    private Button mBtnRecycler, mBtnOWen, mBtnOWenNew, mBtnNormal, mBtnModule, mBtnModuleVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_demo);

        mContext = this;

        mBtnRecycler = findViewById(R.id.btn_recycler);
        mBtnOWen = findViewById(R.id.btn_owen);
        mBtnOWenNew = findViewById(R.id.btn_owen_new);
        mBtnNormal = findViewById(R.id.btn_normal);
        mBtnModule = findViewById(R.id.btn_module);
        mBtnModuleVertical = findViewById(R.id.btn_module_vertical);

        clickMethod();
    }

    @SuppressLint("CheckResult")
    private void clickMethod() {
        RxView.clicks(mBtnRecycler)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, RecyclerActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnOWen)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, OWenRecyclerActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnOWenNew)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, OWenNewRecyclerActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnNormal)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, NormalActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnModule)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ModuleFocusActivity.class);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mBtnModuleVertical)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ModuleFocusVerticalActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
