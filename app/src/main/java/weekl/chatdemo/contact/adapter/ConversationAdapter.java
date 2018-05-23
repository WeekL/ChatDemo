package weekl.chatdemo.contact.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.view.SwipeRecyclerView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private ContactActivity mActivity;
    private List<ConversationObject> mDatas;

    public ConversationAdapter(ContactActivity activity, List<ConversationObject> datas) {
        mActivity = activity;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_conversation_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ConversationObject object = mDatas.get(position);
        holder.msgView.setText(object.msg);
        holder.nameView.setText(object.target);
        holder.timeView.setText(object.time);
        Glide.with(mActivity).load(mDatas.get(position).avatarUrl).into(holder.avatarView);
        holder.setOnClickListener(v -> mActivity.openConversation(object.userId));
        holder.deleteView.setOnClickListener(v -> mActivity.getMessagePresenter().deleteConversation(object.target, false));
        holder.setOnLongClickListener(v -> {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        mActivity.getMessagePresenter().deleteConversation(object.target, true);
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mActivity.getMessagePresenter().deleteConversation(object.target, false);
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage("是否删除该会话?");
            builder.setNeutralButton("删除会话和记录", listener);
            builder.setNegativeButton("仅删除会话", listener);
            builder.setPositiveButton("取消", listener);
            builder.create().show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 获取目标项所在的位置
     * 不存在则返回-1
     *
     * @param targetName
     * @return
     */
    public int getItemPosition(String targetName) {
        for (int i = 0; i < getItemCount(); i++) {
            ConversationObject object = mDatas.get(i);
            if (object.target.equals(targetName)) {
                mDatas.remove(object);
                return i;
            }
        }
        return -1;
    }

    class ViewHolder extends SwipeRecyclerView.ViewHolder {
        ImageView avatarView;
        TextView nameView;
        TextView timeView;
        TextView msgView;
        TextView deleteView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.chat_list_avatar);
            nameView = itemView.findViewById(R.id.chat_list_target);
            timeView = itemView.findViewById(R.id.chat_list_time);
            msgView = itemView.findViewById(R.id.chat_list_msg);
            deleteView = itemView.findViewById(R.id.item_conversation_delete);
        }

        @Override
        public int bindContentLayout() {
            return R.id.item_conversation_content;
        }

        @Override
        public int bindMenuLayout() {
            return R.id.item_conversation_delete;
        }

        public void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }
    }
}
