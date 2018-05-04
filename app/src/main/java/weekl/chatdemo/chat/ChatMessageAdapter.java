package weekl.chatdemo.chat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hyphenate.chat.EMMessage;

import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.model.MsgObject;

public class ChatMessageAdapter extends BaseAdapter {
    private List<MsgObject> mDatas;

    public ChatMessageAdapter(List<MsgObject> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId;
        MsgObject msgObject = (MsgObject) getItem(position);
        if (getItemViewType(position) == 1) {
            layoutId = R.layout.item_msg_receive;
        } else {
            layoutId = R.layout.item_msg_send;
        }
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,
                parent, false);
        MsgHolder holder = new MsgHolder(view);
        holder.infoView.setText(msgObject.from);
        holder.msgView.setText(msgObject.msgText);
        String showTime = msgObject.showTime;
        if (!showTime.equals(MsgObject.NOT_SHOW)) {
            holder.timeView.setText(showTime);
            holder.timeView.setVisibility(View.VISIBLE);
        }
        return view;
    }


    @Override
    public int getItemViewType(int position) {
        MsgObject object = (MsgObject) getItem(position);
        if (object.direct.equals(EMMessage.Direct.RECEIVE)) {
            return 1;
        } else {
            return 2;
        }
    }

    static class MsgHolder {
        TextView infoView;
        TextView msgView;
        TextView timeView;

        public MsgHolder(View view) {
            infoView = view.findViewById(R.id.msg_info);
            msgView = view.findViewById(R.id.msg_txt);
            timeView = view.findViewById(R.id.msg_time);
        }
    }
}
