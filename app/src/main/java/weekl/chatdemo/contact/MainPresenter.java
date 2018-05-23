package weekl.chatdemo.contact;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.model.User;

public class MainPresenter extends BasePresenter implements IContactPresenter.IPresenter {
    private static final String TAG = "MainPresenter";

    private IContactView.IView mView;
    private IContactView.IConversationView mConversationView;
    private IContactView.IFriendView mFriendView;
    private IContactPresenter.IContact mContactPresenter;
    private IContactPresenter.IMessage mMessagePresenter;

    MainPresenter(IContactView.IView IView) {
        super();
        mView = IView;
        mConversationView = (IContactView.IConversationView) mView.getView(IContactView.Index.INDEX_CONVERSATION);
        mFriendView = (IContactView.IFriendView) mView.getView(IContactView.Index.INDEX_FRIEND);
        mMessagePresenter = new ConversationPresenter(mConversationView);
        mContactPresenter = new FriendPresenter(mFriendView);
    }

    @Override
    public IContactPresenter getPresenter(IContactPresenter.Index index) {
        switch (index) {
            case INDEX_CONTACT:
                return mContactPresenter;
            case INDEX_MESSAGE:
                return mMessagePresenter;
        }
        return null;
    }

    @Override
    public void loadConversations() {
        new Thread(() -> {
            final List<ConversationObject> objects = new ArrayList<>();
            //获取所有会话
            Map<String, EMConversation> allConversation = getEMClient().chatManager().getAllConversations();
            //遍历Map中的所有会话
            for (Map.Entry<String, EMConversation> entry : allConversation.entrySet()) {
                //取出会话
                EMConversation conversation = entry.getValue();
                //获取会话中的最后一条消息
                EMMessage message = conversation.getLastMessage();
                ConversationObject object = new ConversationObject(message);
                objects.add(object);
            }
            Log.i(TAG, "历史会话：" + objects);
            getHandler().post(() -> mConversationView.onLoadConversationSuccess(objects));
        }).start();
    }

    @Override
    public void loadFriends() {
        new Thread(() -> {
            try {
                //获取好友用户名列表
                List<String> nameList = getEMClient().contactManager().getAllContactsFromServer();
                final List<User> userList = new ArrayList<>();
                //从云数据库查询用户信息
                for (String name : nameList) {
                    Log.i(TAG, "加载好友id:" + name);
                    BmobQuery<User> query = new BmobQuery<>();
                    query.addWhereEqualTo("userId", name);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                Log.i(TAG, "获取好友列表信息: " + list);
                                userList.addAll(list);
                            } else {
                                Log.e(TAG, "获取好友列表信息失败:" + e.getMessage());
                            }
                            Log.i(TAG, "done: " + userList.toString());
                            getHandler().post(() -> mFriendView.onLoadFriendSuccess(userList));
                        }
                    });
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true);
        mView.onLogoutSuccess();
    }

    @Override
    public void onDestroy() {
        mMessagePresenter.removeListener();
        mContactPresenter.removeListener();
        super.onDestroy();
    }
}
