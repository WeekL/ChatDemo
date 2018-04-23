package weekl.chatdemo.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import weekl.chatdemo.R;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private ILogin.Presenter mPresenter;
    private View mView;
    private EditText userNameView, passwordView, repeatView;
    private Button registerView;
    private TextView goLoginView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((LoginActivity) getActivity()).setToolbarTitle("注册");
        if (mView == null) {
            mPresenter = ((LoginActivity) getActivity()).getPresenter();
            mView = inflater.inflate(R.layout.fragment_register, container, false);
            userNameView = mView.findViewById(R.id.register_userName);
            passwordView = mView.findViewById(R.id.register_password);
            repeatView = mView.findViewById(R.id.register_repeat);
            registerView = mView.findViewById(R.id.btn_register);
            goLoginView = mView.findViewById(R.id.tv_login);

            registerView.setOnClickListener(this);
            goLoginView.setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                String userName = userNameView.getText().toString().trim();
                String password = passwordView.getText().toString().trim();
                String repeat = repeatView.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(repeat) || !password.equals(repeat)) {
                    mPresenter.showToast("请输入正确的账号密码");
                }
                mPresenter.register(userName, password);
                break;
            case R.id.tv_login:
                mPresenter.replaceFragment(0);
                break;
        }
    }
}
