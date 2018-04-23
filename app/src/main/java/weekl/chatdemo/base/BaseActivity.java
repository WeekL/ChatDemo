package weekl.chatdemo.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;

public abstract class BaseActivity extends AppCompatActivity {
    private EMClient mClient;
    private String mCurUser;
    protected int mToolbarHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        //getResources().getDimension(android.R.attr.actionBarSize)
        init();
    }

    private void init(){
        mClient = EMClient.getInstance();
        mCurUser = mClient.getCurrentUser();
    }

    public EMClient getEMClient() {
        return mClient;
    }

    public String getCurUser() {
        if (mCurUser != null) {
            return mCurUser;
        }
        return null;
    }
}