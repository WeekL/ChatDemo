package weekl.chatdemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hyphenate.chat.EMClient;

import java.util.concurrent.CountDownLatch;

import weekl.chatdemo.model.User;
import weekl.chatdemo.util.BmobUtil;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private EMClient mClient;
    private String mCurUser;
    protected static User mUser;
    private static CountDownLatch latch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        latch = new CountDownLatch(1);
        BmobUtil.findUser(mCurUser, new BmobUtil.FindUserListener() {
            @Override
            public void onSuccess(User user) {
                mUser = user;
                latch.countDown();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "查找用户信息失败："+errorCode+","+errorMsg);
                latch.countDown();
            }
        });
    }

    private void init(){
        mClient = EMClient.getInstance();
        mCurUser = mClient.getCurrentUser();
    }

    public EMClient getEMClient() {
        return mClient;
    }

    public String getCurEmUser() {
        if (mCurUser != null) {
            return mCurUser;
        }
        return null;
    }

    public static User getCurUser(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mUser;
    }
}