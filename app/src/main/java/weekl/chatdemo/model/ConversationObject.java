package weekl.chatdemo.model;

import com.hyphenate.chat.EMMessage;

import weekl.chatdemo.R;

public class ConversationObject {
    public int avatarId;
    public String target;
    public String msg;
    public String time;

    public ConversationObject() {
        super();
        avatarId = R.mipmap.app_icon;
    }

    public ConversationObject(MsgObject msgObject){
        if (msgObject.direct == EMMessage.Direct.RECEIVE) {
            this.target = msgObject.from;
        } else {
            this.target = msgObject.to;
        }
        this.msg = msgObject.msgText;
        this.time = msgObject.getConversationLastTime();
    }
}
