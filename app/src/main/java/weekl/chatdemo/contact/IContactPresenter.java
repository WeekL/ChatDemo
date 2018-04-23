package weekl.chatdemo.contact;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;

public interface IContactPresenter {
    enum Index {
        INDEX_MESSAGE,
        INDEX_CONTACT
    }

    interface IPresenter {
        /**
         * 获取子Presenter
         *
         * @param index
         * @return
         */
        IContactPresenter getPresenter(IContactPresenter.Index index);

        /**
         * 加载会话列表
         */
        void loadConversations();


        /**
         * 加载好友列表
         */
        void loadFriends();

        /**
         * 注销登录
         */
        void logout();

        /**
         * 销毁
         */
        void onDestroy();

    }

    interface IMessage extends IContactPresenter {

        /**
         * 删除会话
         *
         * @param target
         */
        void deleteConversation(String target);

        /**
         * 获取消息监听器
         *
         * @return
         */
        EMMessageListener getListener();

        /**
         * 移除监听器
         */
        void removeListener();
    }

    interface IContact extends IContactPresenter {
        /**
         * 发送好友申请
         *
         * @param name
         * @param reason
         */
        void sendApply(String name, String reason);

        /**
         * 删除好友
         *
         * @param name
         * @param keepConversation
         */
        void deleteFriend(String name, boolean keepConversation);

        /**
         * 接受好友请求
         *
         * @param name
         */
        void acceptFriend(String name);

        /**
         * 拒绝好友请求
         *
         * @param name
         */
        void refuseFriend(String name);

        /**
         * 获取联系人监听器
         *
         * @return
         */
        EMContactListener getListener();

        /**
         * 移除监听器
         */
        void removeListener();
    }
}
