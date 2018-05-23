package weekl.chatdemo.contact;

import java.util.List;

import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.model.User;

public interface IContactView {
    enum Index {
        INDEX_CONVERSATION,
        INDEX_FRIEND,
        INDEX_MORE
    }

    interface IView extends IContactView {
        /**
         * 获取子Fragment
         *
         * @param index
         * @return
         */
        IContactView getView(Index index);

        /**
         * 注销成功
         */
        void onLogoutSuccess();
    }

    interface IConversationView extends IContactView {

        /**
         * 加载会话列表成功
         */
        void onLoadConversationSuccess(List<ConversationObject> objects);

        /**
         * 更新会话
         *
         * @param conversationObject
         */
        void updateConversation(ConversationObject conversationObject);

        /**
         * 删除会话成功
         *
         * @param target
         */
        void onDeleteConversationSuccess(String target);
    }

    interface IFriendView extends IContactView {

        void showToast(String msg);

        /**
         * 加载好友列表成功
         *
         * @param userList
         */
        void onLoadFriendSuccess(List<User> userList);

        /**
         * 新增好友
         *
         * @param user EMContact类型的用户对象
         */
        void addFriend(User user);

        /**
         * 删除好友成功
         *
         * @param name
         */
        void onDeleteSuccess(String name);

        /**
         * 处理好友申请成功
         *
         * @param userName
         */
        void onHandleApplySuccess(String userName);

        /**
         * 收到好友申请
         *
         * @param name
         * @param reason
         */
        void onContactInvited(String name, String reason);
    }

    interface ISettingView extends IContactView {

    }
}
