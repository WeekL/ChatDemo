package weekl.chatdemo.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 好友申请数据表
 */
public class ApplyList extends BmobObject {
    private static final String TAG = "Bmob好友申请表";

    private static String curUser;

    private String from;
    private String to;
    private String reason;
    private Boolean handle;

    public static void addApply(@NonNull String to, String reason) {
        String curUser = EMClient.getInstance().getCurrentUser();
        if (curUser == null) {
            throw new NullPointerException("未登录");
        }
        ApplyList apply = new ApplyList();
        apply.from = curUser;
        apply.to = to;
        apply.reason = reason;
        apply.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "发送好友申请：" + s);
                } else {
                    Log.e(TAG, "添加好友申请失败：" + e.getErrorCode() + "，" + e.getMessage());
                }
            }
        });
    }

    public static List<ApplyList> getApplyList() {
        String curUser = EMClient.getInstance().getCurrentUser();
        if (curUser == null) {
            throw new NullPointerException("未登录");
        }
        final List<ApplyList> results = new ArrayList<>();
        BmobQuery<ApplyList> query = new BmobQuery<>();
        query.addWhereEqualTo("to", curUser);
        query.findObjects(new FindListener<ApplyList>() {
            @Override
            public void done(List<ApplyList> list, BmobException e) {
                if (e == null) {
                    results.addAll(list);
                } else {
                    Log.e(TAG, "查询好友申请列表失败：" + e.getErrorCode() + "，" + e.getMessage());
                }
            }
        });
        return results;
    }

    private ApplyList() {
        curUser = EMClient.getInstance().getCurrentUser();
    }

    @Override
    public String getObjectId() {
        if (curUser.equals(from) || curUser.equals(to)) {
            return super.getObjectId();
        }
        Log.e(TAG, "getObjectId: 当前用户无权获取此申请的信息");
        return null;
    }

    @Override
    public void setValue(String key, Object value) {
        /*switch (key) {
            case "from":
            case "to":
            case "reason":
                Log.e(TAG, "key:" + key + "不可更改");
                break;
            case "handle":
                if (curUser.equals(to)) {
                    super.setValue(key, value);
                } else {
                    Log.e(TAG, "仅被申请人可修改handle值");
                }
                break;
            default:
                Log.e(TAG, "key:" + key + "不存在");
        }*/
        Log.e(TAG, "setValue: 请使用setHandle方法");
    }

    public String getFrom() {
        if (curUser.equals(from) || curUser.equals(to)) {
            return from;
        }
        Log.e(TAG, "getObjectId: 当前用户无权获取此申请的信息");
        return null;
    }

    public String getTo() {
        if (curUser.equals(from) || curUser.equals(to)) {
            return to;
        }
        Log.e(TAG, "getObjectId: 当前用户无权获取此申请的信息");
        return null;
    }

    public String getReason() {
        if (curUser.equals(from) || curUser.equals(to)) {
            return reason;
        }
        Log.e(TAG, "getObjectId: 当前用户无权获取此申请的信息");
        return null;
    }

    public Boolean getHandle() {
        if (curUser.equals(from) || curUser.equals(to)) {
            return handle;
        }
        Log.e(TAG, "getObjectId: 当前用户无权获取此申请的信息");
        return null;
    }

    public void setHandle(Boolean handle) {
        if (curUser.equals(to)) {
            this.handle = handle;
            this.update();
        } else {
            Log.e(TAG, "仅被申请人可修改handle值");
        }
    }
}
