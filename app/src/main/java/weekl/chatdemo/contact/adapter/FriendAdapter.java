package weekl.chatdemo.contact.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.base.CommonAdapter;
import weekl.chatdemo.base.CommonViewHolder;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactPresenter;
import weekl.chatdemo.model.User;

/**
 * 好友列表适配器
 */
public class FriendAdapter extends CommonAdapter<User> {
    private static final String TAG = "FriendAdapter";
    private ContactActivity mActivity;
    private IContactPresenter.IContact mPresenter;

    public FriendAdapter(ContactActivity contactActivity, List<User> datas) {
        super(contactActivity, datas, R.layout.item_friend_list);
        mActivity = contactActivity;
        mPresenter = mActivity.getContactPresenter();
    }

    @Override
    public void convert(CommonViewHolder holder, final User bean) {
        //获取好友信息
        final String friendId = bean.getUserId();
        final String friendName = bean.getNickName();
        final String avatarUrl = bean.getAvatarUrl();
        final String signature = bean.getSignature();
        holder.setText(R.id.item_friend_name, friendName);
        holder.setText(R.id.item_friend_signature,signature);
        ImageView avatarView = holder.getView(R.id.item_friend_avatar);
        Glide.with(mActivity).load(avatarUrl).into(avatarView);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openConversation(friendId);
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String choices[] = new String[]{"删除好友，并清空聊天记录","删除好友，保留聊天记录"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + which);
                        switch (which){
                            case 0:
                                mPresenter.deleteFriend(friendName, false);
                                break;
                            case 1:
                                mPresenter.deleteFriend(friendName, true);
                                break;
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                return true;
            }
        });
    }
}
