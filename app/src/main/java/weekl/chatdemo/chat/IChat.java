package weekl.chatdemo.chat;

import weekl.chatdemo.base.BaseViewInterface;
import weekl.chatdemo.model.MsgObject;

public interface IChat {

    interface View extends BaseViewInterface {
        void updateMsg(MsgObject msgObject);
    }

    interface Presenter{
        void loadMsgRecord();
        void sendTextMsg(String msg);
        void onDestroy();
    }
}
