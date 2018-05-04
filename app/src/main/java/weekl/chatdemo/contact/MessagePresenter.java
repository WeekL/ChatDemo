package weekl.chatdemo.contact;

import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.model.MsgObject;

public class MessagePresenter extends BasePresenter implements
        IContactPresenter.IMessage, EMMessageListener {
    private static final String TAG = "MessagePresenter";
    private IContactView.IConversationView mView;

    public MessagePresenter(IContactView.IConversationView IConversation){
        super();
        getEMClient().chatManager().addMessageListener(this);
        this.mView = IConversation;
    }

    @Override
    public void deleteConversation(String target, boolean deleteRecord) {
        getEMClient().chatManager().deleteConversation(target,deleteRecord);
    }

    @Override
    public EMMessageListener getListener() {
        return this;
    }

    @Override
    public void removeListener() {
        getEMClient().chatManager().removeMessageListener(this);
    }

    //消息监听事件
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            Log.i(TAG, "收到消息: " + message);
            final MsgObject object = new MsgObject(message);
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mView.updateConversation(new ConversationObject(object));
                }
            });
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
