package weekl.chatdemo.model;

import android.util.Log;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import weekl.chatdemo.util.BmobUtil;

public class ConversationObject {
    private static final String TAG = "ConversationObject";

    public String userId;
    public String avatarUrl;
    public String target;
    public String msg;
    public String time;
    public long originTime;

    public ConversationObject(final EMMessage message) {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            this.userId = message.getFrom();
        } else {
            this.userId = message.getTo();
        }
        this.msg = ((EMTextMessageBody) message.getBody()).getMessage();
        this.originTime = message.getMsgTime();
        this.time = MsgObject.getConversationLastTime(this.originTime);
        BmobUtil.findUser(userId, new BmobUtil.FindUserListener() {
            @Override
            public void onSuccess(User user) {
                ConversationObject.this.target = user.getNickName();
                ConversationObject.this.avatarUrl = user.getAvatarUrl();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "查找用户信息失败:" + errorCode + "，" + errorMsg);
            }
        });
    }
}
