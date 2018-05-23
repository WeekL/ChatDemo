package weekl.chatdemo.contact.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;

import weekl.chatdemo.Constant;
import weekl.chatdemo.R;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactView;

public class SettingFragment extends PreferenceFragment implements IContactView.ISettingView {
    private static final String TAG = "SettingFragment";
    private ContactActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_notification);
        mActivity = (ContactActivity) getActivity();
        mActivity.setToolbarTitle(getString(R.string.setting));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        Log.d(TAG, "点击设置项: " + key);
        if (key.equals(Constant.SETTING_USER_INFO)) {
            Log.d(TAG, "点击了设置个人信息");
            mActivity.startActivity(new Intent(mActivity, UserInfoActivity.class));
        } else if (key.equals(Constant.SETTING_CHANGE_USER)) {
            Log.d(TAG, "点击了切换用户");
            mActivity.logout();
        } else if (key.equals(Constant.SETTING_SOUND)) {

        } else if (key.equals(Constant.SETTING_VIBRATE)) {

        }
        return true;
    }
}
