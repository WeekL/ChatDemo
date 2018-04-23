package weekl.chatdemo.chat;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import weekl.chatdemo.base.BasePresenter;
import weekl.chatdemo.model.MsgObject;

public class ChatPresenter extends BasePresenter implements IChat.Presenter, EMMessageListener {
    private static final String TAG = "ChatPresenter";
    private IChat.View mView;

    private String mTarget;
    private EMConversation mConversation;

    //最后的消息时间
    private long mLastTime;
    //已经显示过的日期
    private List<String> mDateList;

    public ChatPresenter(String target, IChat.View chatInterface) {
        super();
        mView = chatInterface;
        mTarget = target;
        mConversation = getEMClient().chatManager().getConversation(mTarget);
        mDateList = new ArrayList<>();
    }

    @Override
    public void loadMsgRecord() {
        Log.i(TAG, "开始加载消息记录:" + mConversation);
        if (mConversation != null) {
            //获取会话中的最后一条消息
            EMMessage msg = mConversation.getLastMessage();
            //最后一条消息的id，msgId
            String msgId = msg.getMsgId();
            //获取会话中的消息数量，msgCount
            int msgCount = mConversation.getAllMsgCount();
            //获取 msgId 之前的 msgCount 条消息
            List<EMMessage> messageList = mConversation.loadMoreMsgFromDB(msgId, msgCount);

            for (EMMessage message : messageList) {
                Log.i(TAG, "消息记录: " + message);
                final MsgObject object = new MsgObject(message);
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateMsg(object);
                    }
                });
            }
            final MsgObject object = new MsgObject(msg);
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    updateMsg(object);
                }
            });
        }
    }

    @Override
    public void sendTextMsg(String msg) {
        //构造一条EMMessage消息
        final EMMessage message = EMMessage.createTxtSendMessage(msg, mTarget);
        //设置类型为单聊
        message.setChatType(EMMessage.ChatType.Chat);
        //设置消息时间为当前系统时间
        message.setMsgTime(System.currentTimeMillis());
        //发送消息
        getEMClient().chatManager().sendMessage(message);
        //消息执行情况回调
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "消息发送成功");
                mConversation.appendMessage(message);
            }

            @Override
            public void onError(int i, final String s) {
                Log.e(TAG, "消息发送失败");
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showToast("消息发送失败：" + s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        updateMsg(new MsgObject(message));
    }

    private void updateMsg(MsgObject msgObject) {
        long time = msgObject.time;
        String msgTime = msgObject.getHistoryTime();
        //多重判断
        if (msgTime != null && !dateIsExists(msgTime) && msgObject.moreThanHideTime(mLastTime)) {
            msgObject.showTime = msgTime;
        }
        mLastTime = time;
        mView.updateMsg(msgObject);
    }

    private boolean dateIsExists(String date) {
        if (!date.contains("-") && !date.contains("星期")) {
            return false;
        }
        for (String s : mDateList) {
            if (s.equals(date)) {
                return true;
            }
        }
        mDateList.add(date);
        return false;
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            Log.i(TAG, "收到消息: " + message);
            mConversation.updateMessage(message);
            final MsgObject msgObject = new MsgObject(message);
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    updateMsg(msgObject);
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

    @Override
    public void onAttach() {
        super.onAttach();
        getEMClient().chatManager().addMessageListener(this);
    }

    @Override
    public void onDestroy() {
        getEMClient().chatManager().removeMessageListener(this);
        super.onDestroy();
    }
}
