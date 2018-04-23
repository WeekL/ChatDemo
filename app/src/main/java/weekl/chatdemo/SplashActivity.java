package weekl.chatdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.chat.EMClient;

import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_splash);

        if (getEMClient().isLoggedInBefore()) {
            //自动登录,加载所有本地群和会话
            //不是必须的，不加sdk也会自动异步去加载(不会重复加载)
            //加上的话保证进了主页面会话和群组都已经load完毕
            getEMClient().groupManager().loadAllGroups();
            getEMClient().chatManager().loadAllConversations();
            startActivity(new Intent(SplashActivity.this, ContactActivity.class));
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

    private EMClient getEMClient(){
        return EMClient.getInstance();
    }
}
