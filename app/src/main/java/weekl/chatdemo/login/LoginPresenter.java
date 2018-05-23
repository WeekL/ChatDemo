package weekl.chatdemo.login;

import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.User;

public class LoginPresenter extends BasePresenter implements ILogin.Presenter {
    private static final String TAG = "LoginPresenter";
    ILogin.View mView;

    public LoginPresenter(ILogin.View ILoginView) {
        mView = ILoginView;
    }

    /**
     * 登录用户
     *
     * @param userName
     * @param password
     */
    @Override
    public void login(final String userName, final String password) {
        mView.showDialog("登录中...");
        final String userId = userName.toLowerCase();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
            mView.onError();
            mView.showToast("请输入正确的用户名和密码");
            return;
        }
        getEMClient().login(userId, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                mView.onLoginSuccess();
                Log.i(TAG, userId + "登录成功");
            }

            @Override
            public void onError(int i, String s) {
                final String errorInfo = s;
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.onError();
                        mView.showToast("登录失败：" + errorInfo);
                    }
                });
                Log.e(TAG, userId + "登录失败：" + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 注册用户
     *
     * @param userName
     * @param password
     */
    @Override
    public void register(final String userName, final String password) {
        mView.showDialog("注册中...");
        final String userId = userName.toLowerCase();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
            mView.onError();
            mView.showToast("请输入正确的用户名和密码");
            return;
        }
        //注册新用户
        //先进行云数据库注册
        final User user = new User(userName);
        user.save(new SaveListener<String>() {
            @Override
            public void done(final String objectId, final BmobException e) {
                if (e == null) {
                    //云数据库注册成功，开始真正的用户注册
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getEMClient().createAccount(userId, password);
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onRegisterSuccess();
                                    }
                                });
                            } catch (HyphenateException e1) {
                                //服务器用户注册失败，删除云数据库用户数据
                                user.delete(objectId,null);

                                String errorInfo = "";
                                switch (e1.getErrorCode()) {
                                    case EMError.NETWORK_ERROR:
                                        errorInfo = "：网络错误";
                                        break;
                                    case EMError.USER_ALREADY_EXIST:
                                        errorInfo = "：账户已存在";
                                        break;
                                    case EMError.USER_ILLEGAL_ARGUMENT:
                                        errorInfo = "：参数非法";
                                        break;
                                }
                                final String errorMsg = "注册失败" + errorInfo;
                                Log.e(TAG, errorMsg);
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onError();
                                        mView.showToast(errorMsg);
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    mView.onError();
                    mView.showToast("注册失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void replaceFragment(int index) {
        mView.replaceFragment(index);
    }

    @Override
    public void showToast(String msg) {
        final String msgInfo = msg;
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.showToast(msgInfo);
            }
        });
    }
}
