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

public class LoginFragment extends Fragment implements View.OnClickListener {
    private ILogin.Presenter mPresenter;
    private View mView;
    private EditText userNameView, passwordView;
    private Button loginView;
    private TextView goRegisterView;

    private String defUser = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((LoginActivity)getActivity()).setToolbarTitle("登录");
        if (mView == null){
            mPresenter = ((LoginActivity)getActivity()).getPresenter();
            mView = inflater.inflate(R.layout.fragment_login,container,false);
            userNameView = mView.findViewById(R.id.login_userName);
            passwordView = mView.findViewById(R.id.login_password);
            loginView = mView.findViewById(R.id.btn_login);
            goRegisterView = mView.findViewById(R.id.tv_register);

            userNameView.setText(defUser);
            loginView.setOnClickListener(this);
            goRegisterView.setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_login:
                String userName = userNameView.getText().toString().trim();
                String password = passwordView.getText().toString().trim();
                if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
                    mPresenter.showToast("请输入正确的账号密码");
                    return;
                }
                mPresenter.login(userName,password);
                break;
            case R.id.tv_register:
                mPresenter.replaceFragment(1);
                break;
        }
    }

    public void setUserName(String userName){
        defUser = userName;
    }
}
