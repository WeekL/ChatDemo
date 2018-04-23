package weekl.chatdemo.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.adapter.CommonAdapter;
import weekl.chatdemo.adapter.CommonViewHolder;
import weekl.chatdemo.model.ConversationObject;

public class ConversationFragment extends Fragment implements IContactView.IConversationView {
    private IContactPresenter.IMessage mPresenter;
    private ContactActivity mActivity;
    private View mView;
    private ListView mConversationListView;
    private CommonAdapter<ConversationObject> mAdapter;

    private List<ConversationObject> mConversations;

    public ConversationFragment() {
        super();
        mConversations = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mActivity = (ContactActivity) getActivity();
            mActivity.setToolbarTitle(getString(R.string.conversation));
            mPresenter = mActivity.getMessagePresenter();
            mView = inflater.inflate(R.layout.fragment_conversation, container, false);
            initView();
        }
        return mView;
    }

    private void initView() {
        //会话列表
        mConversationListView = mView.findViewById(R.id.conversation_list);
        mAdapter = new CommonAdapter<ConversationObject>(mActivity, mConversations, R.layout.item_conversationn_list) {
            @Override
            public void convert(final CommonViewHolder holder, final ConversationObject bean) {
                holder.setText(R.id.chat_list_target, bean.target);
                holder.setText(R.id.chat_list_time, bean.time);
                holder.setText(R.id.chat_list_msg, bean.msg);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.openConversation(bean.target);
                    }
                });
                holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mPresenter.deleteConversation(bean.target);
                        return true;
                    }
                });
            }
        };
        mConversationListView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadConversationSuccess(List<ConversationObject> objects) {
        Collections.sort(objects, new Comparator<ConversationObject>() {
            @Override
            public int compare(ConversationObject o1, ConversationObject o2) {
                int i = (int)(o1.originTime - o2.originTime);
                return i;
            }
        });
        for (ConversationObject object : objects) {
            updateConversation(object);
        }
    }

    @Override
    public void updateConversation(ConversationObject conversationObject) {
        for (ConversationObject object : mConversations) {
            if (object.target.equals(conversationObject.target)) {
                mConversations.remove(object);
                break;
            }
        }
        mConversations.add(0, conversationObject);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteConversationSuccess(String target) {
        for (ConversationObject object : mConversations) {
            if (object.target.equals(target)) {
                mConversations.remove(object);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
}
