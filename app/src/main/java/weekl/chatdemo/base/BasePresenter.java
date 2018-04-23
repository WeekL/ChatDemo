package weekl.chatdemo.base;

import android.os.Handler;
import android.os.Looper;

import com.hyphenate.chat.EMClient;

public abstract class BasePresenter {
    private Handler mHandler;
    private EMClient mEMClient;

    public BasePresenter(){
        this.onAttach();
    }

    public void onAttach(){
        mHandler = new Handler(Looper.getMainLooper());
        mEMClient = EMClient.getInstance();
    }

    public void onDestroy(){
        mHandler = null;
        mEMClient = null;
    }

    public Handler getHandler(){
        return mHandler;
    }

    public EMClient getEMClient(){
        return mEMClient;
    }
}
