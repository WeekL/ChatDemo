package weekl.chatdemo.contact;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.User;

public class FriendPresenter extends BasePresenter
        implements IContactPresenter.IContact, EMContactListener {
    private static final String TAG = "FriendPresenter";
    private IContactView.IFriendView mView;

    public FriendPresenter(IContactView.IFriendView IFriendView) {
        super();
        getEMClient().contactManager().setContactListener(this);
        mView = IFriendView;
    }

    @Override
    public void sendApply(String name, String reason) {
        try {
            getEMClient().contactManager().addContact(name, reason);
            Log.i(TAG, "好友申请已发送: " + name + "，" + reason);
            mView.showToast("好友申请已发送");
        } catch (HyphenateException e) {
            String error = e.getDescription();
            Log.e(TAG, "发送好友申请失败:" + error);
            mView.showToast("发送好友申请失败:" + error);
        }
    }

    @Override
    public void deleteFriend(String name, boolean keepConversation) {
        try {
            getEMClient().contactManager().deleteContact(name, keepConversation);
            Log.i(TAG, "删除好友成功:" + name);
            mView.showToast("好友已删除");
            mView.onDeleteSuccess(name);
        } catch (HyphenateException e) {
            Log.d(TAG, "删除好友失败：" + e.getDescription());
            mView.showToast("删除好友失败：" + e.getDescription());
        }
    }

    @Override
    public void acceptFriend(final String name) {
        getEMClient().contactManager().asyncAcceptInvitation(name, new EMCallBack() {
            @Override
            public void onSuccess() {
                addFriend(name);
                String msg = "我们已经是好友了，快来聊天吧";
                EMMessage message = EMMessage.createTxtSendMessage(msg, name);
                message.setChatType(EMMessage.ChatType.Chat);
                message.setMsgTime(System.currentTimeMillis());
                //发送消息
                getEMClient().chatManager().sendMessage(message);
            }

            @Override
            public void onError(int i, final String s) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showToast("接受好友请求失败：" + s);
                        Log.i(TAG, "接受好友请求失败：" + s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public void refuseFriend(final String name) {
        getEMClient().contactManager().asyncDeclineInvitation(name, new EMCallBack() {
            @Override
            public void onSuccess() {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.onHandleApplySuccess(name);
                        mView.showToast("已拒绝");
                        Log.i(TAG, "拒绝好友请求:" + name);
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showToast("操作失败：" + s);
                        Log.i(TAG, "拒绝好友请求失败：" + s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public EMContactListener getListener() {
        return this;
    }

    @Override
    public void removeListener() {
        getEMClient().contactManager().removeContactListener(this);
    }

    //联系人监听事件
    @Override
    public void onContactAdded(final String s) {
        addFriend(s);
    }

    @Override
    public void onContactDeleted(final String s) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.onDeleteSuccess(s);
                Log.i(TAG, "删除好友:" + s);
            }
        });
    }

    @Override
    public void onContactInvited(final String s, final String s1) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.onContactInvited(s, s1);
                Log.i(TAG, "收到好友申请:" + s);
            }
        });
    }

    @Override
    public void onFriendRequestAccepted(final String s) {
        addFriend(s);
    }

    @Override
    public void onFriendRequestDeclined(final String s) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.showToast(s + "拒绝了你的好友请求");
                Log.i(TAG, "好友申请被拒绝:" + s);
            }
        });
    }

    private void addFriend(final String name){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", name);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    final User user = list.get(0);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mView.addFriend(user);
                            Log.i(TAG, "新增好友:" + name);
                        }
                    });
                }else {
                    Log.e(TAG, "查询用户信息失败:" + e.getMessage());
                }
            }
        });
    }
}
