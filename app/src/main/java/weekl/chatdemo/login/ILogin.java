package weekl.chatdemo.login;


public interface ILogin {
    interface View{
        //登录成功
        void onLoginSuccess();
        //注册成功
        void onRegisterSuccess();
        //执行失败
        void onError();
        //展现提示
        void showToast(String msg);
        //展现进度框
        void showDialog(String msg);
        //切换碎片
        void replaceFragment(int index);
    }

    interface Presenter{
        //登录
        void login(String userName, String password);
        //注册
        void register(String userName, String password);
        //切换碎片
        void replaceFragment(int index);
        //提示消息
        void showToast(String msg);
    }
}
