package weekl.chatdemo;

import weekl.chatdemo.base.BaseApplication;

/**
 * 常量类
 */
public class Constant {

    //广播类型
    public static final String MESSAGE_BROADCAST = "weekl.chatdemo.EMCONN_MESSAGE_BROADCAST";
    public static final String CONTACT_BROADCAST = "weekl.chatdemo.EMCONN_CONTACT_BROADCAST";

    //设置项
    public static final String SETTING_USER_INFO = getKey(R.string.设置个人信息);
    public static final String SETTING_CHANGE_USER = getKey(R.string.切换用户);
    public static final String SETTING_SOUND = getKey(R.string.声音开关);
    public static final String SETTING_VIBRATE = getKey(R.string.震动开关);

    private static String getKey(int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }
}
