package weekl.chatdemo.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import weekl.chatdemo.R;
import weekl.chatdemo.base.BaseActivity;
import weekl.chatdemo.contact.ContactActivity;

public class LoginActivity extends BaseActivity implements ILogin.View {
    private static final String TAG = "LoginActivity";
    private ILogin.Presenter mPresenter;

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mPresenter = new LoginPresenter(this);
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        replaceFragment(0);

        String defUser = getIntent().getStringExtra("userName");
        if (defUser != null) {
            loginFragment.setUserName(defUser);
        }
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onLoginSuccess() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Intent intent = new Intent(LoginActivity.this, ContactActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterSuccess() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("注册成功！是否立即登录？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerFragment.goLogin();
            }
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
    }

    @Override
    public void onError() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(String msg) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void replaceFragment(int index) {
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = loginFragment;
                break;
            case 1:
                fragment = registerFragment;
                break;
            default:
                Log.e(TAG, "登陆/注册碎片切换出错，index = " + index);
                return;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.login_content, fragment);
        transaction.commit();
    }

    public ILogin.Presenter getPresenter() {
        return mPresenter;
    }
}
