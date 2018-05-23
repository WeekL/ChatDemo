package weekl.chatdemo.model;

import android.util.Log;

import com.hyphenate.chat.EMClient;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 用户类，存储在云数据库
 */
public class User extends BmobObject {
    private static final String TAG = "Bmob用户表";
    private String curUser;

    private String userId;//账号
    private String nickName;//昵称
    private String avatarUrl;//头像url，用于加载用户头像
    private String signature;//个性签名
    private Boolean sex;//性别
    private Integer age;//年龄
    private String phoneNum;//手机号

    public enum Sex {male, female}

    public User() {
        curUser = EMClient.getInstance().getCurrentUser();
    }

    public User(String name) {
        this();
        this.userId = name;
        this.nickName = name;
    }

    @Override
    public Subscription update() {
        if (curUser.equals(userId)) {
            return super.update();
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    @Override
    public Subscription update(UpdateListener listener) {
        if (curUser.equals(userId)) {
            return super.update(listener);
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    @Override
    public Subscription update(String objectId, UpdateListener listener) {
        if (curUser.equals(userId)) {
            return super.update(objectId, listener);
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void setValue(String key, Object value) {
        if (curUser.equals(userId)) {
            super.setValue(key,value);
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
    }

    @Override
    protected Subscription delete() {
        if (curUser.equals(userId)) {
            return delete();
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    @Override
    public Subscription delete(String objectId, UpdateListener listener) {
        if (curUser.equals(userId)) {
            return delete(objectId,listener);
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    @Override
    public Subscription delete(UpdateListener listener) {
        if (curUser.equals(userId)) {
            return delete(listener);
        }else {
            Log.e(TAG, "账号写入操作：仅该用户可操作！");
        }
        return null;
    }

    /**
     * 修改账户，登录后可修改
     *
     * @param userId
     * @return
     */
    public User updateUserId(String userId) {
        if (curUser.equals(userId)) {
            this.userId = userId;
            this.update();
        }
        return this;
    }

    public String getPhoneNum() {
        if (curUser.equals(userId)) {
            return phoneNum;
        }
        return null;
    }

    /**
     * 修改手机号码，登录后可修改
     *
     * @param phoneNum
     * @return
     */
    public User setPhoneNum(String phoneNum) {
        if (curUser.equals(userId)) {
            this.phoneNum = phoneNum;
            this.update();
        }
        return this;
    }

    public Sex getSex() {
        if (sex) {
            return Sex.male;
        } else {
            return Sex.female;
        }
    }

    /**
     * 修改性别，登录后可修改
     *
     * @param sex
     * @return
     */
    public User setSex(Sex sex) {
        if (curUser.equals(userId)) {
            if (sex == Sex.male) {
                this.sex = true;
            } else if (sex == Sex.female) {
                this.sex = false;
            }
            this.update();
        }
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    /**
     * 修改昵称，登录后可修改
     *
     * @param nickName
     * @return
     */
    public User setNickName(String nickName) {
        if (curUser.equals(userId)) {
            this.nickName = nickName;
            this.update();
        }
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 修改头像，登录后可修改
     *
     * @param avatarUrl
     * @return
     */
    public User setAvatarUrl(String avatarUrl) {
        if (curUser.equals(userId)) {
            this.avatarUrl = avatarUrl;
            this.update();
        }
        return this;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * 修改个性签名，登录后可修改
     *
     * @param signature
     * @return
     */
    public User setSignature(String signature) {
        if (curUser.equals(userId)) {
            this.signature = signature;
            this.update();
        }
        return this;
    }

    public Integer getAge() {
        return age;
    }

    /**
     * 修改年龄，登录后可修改
     *
     * @param age
     * @return
     */
    public User setAge(Integer age) {
        if (curUser.equals(userId)) {
            this.age = age;
            this.update();
        }
        return this;
    }
}
