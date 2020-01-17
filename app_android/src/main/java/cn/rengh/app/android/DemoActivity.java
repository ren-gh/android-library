package cn.rengh.app.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.flutter.embedding.android.FlutterActivity;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOpenFlutter, btnOpenFlutterTestUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        btnOpenFlutter = findViewById(R.id.btn_open_flutter);
        btnOpenFlutterTestUI = findViewById(R.id.btn_open_flutter_test_ui);
        btnOpenFlutter.setOnClickListener(this);
        btnOpenFlutterTestUI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_flutter: {
                startActivity(FlutterActivity
                        .createDefaultIntent(this));
            }
            break;
            case R.id.btn_open_flutter_test_ui: {
                startActivity(FlutterActivity
                        .withNewEngine()
                        .initialRoute("/lib2_test")
                        .build(this));
            }
            break;
        }
    }
}
