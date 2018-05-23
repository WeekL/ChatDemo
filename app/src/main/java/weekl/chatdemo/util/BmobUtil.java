package weekl.chatdemo.util;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import weekl.chatdemo.model.User;

public class BmobUtil {

    public interface FindUserListener {
        void onSuccess(User user);
        void onError(int errorCode, String errorMsg);
    }

    public static void findUser(final String userId, final FindUserListener listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",userId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null){
                    listener.onSuccess(list.get(0));
                }else {
                    listener.onError(e.getErrorCode(),e.getMessage());
                }
            }
        });
    }
}
