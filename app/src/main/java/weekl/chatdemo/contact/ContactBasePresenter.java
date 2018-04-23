package weekl.chatdemo.contact;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.model.MsgObject;
import weekl.chatdemo.model.User;

public class ContactBasePresenter extends BasePresenter implements IContactPresenter.IPresenter {
    private IContactView.IView mView;
    private IContactView.IConversationView mConversationView;
    private IContactView.IFriendView mFriendView;
    private IContactPresenter.IContact mContactPresenter;
    private IContactPresenter.IMessage mMessagePresenter;

    public ContactBasePresenter(IContactView.IView IView){
        super();
        mView = IView;
        mConversationView = (IContactView.IConversationView) mView.getView(IContactView.Index.INDEX_CONVERSATION);
        mFriendView = (IContactView.IFriendView) mView.getView(IContactView.Index.INDEX_FRIEND);
        mMessagePresenter = new MessagePresenter(mConversationView);
        mContactPresenter = new ContactPresenter(mFriendView);
    }

    @Override
    public IContactPresenter getPresenter(IContactPresenter.Index index) {
        switch (index){
            case INDEX_CONTACT:
                return mContactPresenter;
            case INDEX_MESSAGE:
                return mMessagePresenter;
        }
        return null;
    }

    @Override
    public void loadConversations() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ConversationObject> objects = new ArrayList<>();
                //获取所有会话
                Map<String, EMConversation> allConversation = getEMClient().chatManager().getAllConversations();
                //遍历Map中的所有会话
                Iterator<Map.Entry<String, EMConversation>> iterator = allConversation.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, EMConversation> entry = iterator.next();
                    //取出会话
                    EMConversation conversation = entry.getValue();
                    //获取会话中的最后一条消息
                    EMMessage message = conversation.getLastMessage();
                    ConversationObject object = new ConversationObject(new MsgObject(message));
                    objects.add(object);
                }
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onLoadConversationSuccess(objects);
                    }
                });
            }
        }).start();
    }

    @Override
    public void loadFriends() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> nameList = getEMClient().
                            contactManager().getAllContactsFromServer();
                    final List<User> userList = new ArrayList<>();
                    for (String name:nameList){
                        User user = new User(name);
                        userList.add(user);
                    }
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mFriendView.onLoadFriendSuccess(userList);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
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
