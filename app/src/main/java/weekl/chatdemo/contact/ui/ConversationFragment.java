package weekl.chatdemo.contact.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactPresenter;
import weekl.chatdemo.contact.IContactView;
import weekl.chatdemo.contact.adapter.ConversationAdapter;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.view.SwipeRecyclerView;

public class ConversationFragment extends Fragment implements IContactView.IConversationView {
    private static final String TAG = "ConversationFragment";
    private IContactPresenter.IMessage mPresenter;
    private ContactActivity mActivity;
    private View mView;
    private SwipeRecyclerView mConversationView;
    private ConversationAdapter mAdapter;

    private List<ConversationObject> mConversations;
    public ConversationFragment() {
        super();
        mConversations = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (ContactActivity) getActivity();
        mActivity.setToolbarTitle(getString(R.string.conversation));
        if (mView == null) {
            mPresenter = mActivity.getMessagePresenter();
            mView = inflater.inflate(R.layout.fragment_conversation, container, false);
            initView();
        }
        return mView;
    }

    private void initView() {
        //会话列表
        mConversationView = mView.findViewById(R.id.conversation_list);
        mAdapter = new ConversationAdapter(mActivity, mConversations);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mConversationView.setLayoutManager(manager);
        mConversationView.setAdapter(mAdapter);
    }

    @Override
    public void onLoadConversationSuccess(List<ConversationObject> objects) {
        if (objects == null){
            return;
        }
        //对历史会话记录进行排序
        Collections.sort(objects, (o1, o2) -> {
            int i = (int) (o1.originTime - o2.originTime);
            return i;
        });
        for (ConversationObject object : objects) {
            updateConversation(object);
        }
    }

    @Override
    public void updateConversation(ConversationObject conversationObject) {
        //移除原先的会话，此处可考虑使用RecyclerView的移动子项实现
        for (int i = 0; i < mConversations.size(); i++) {
            ConversationObject object = mConversations.get(i);
            if (object.userId.equals(conversationObject.userId)){
                mConversations.remove(object);
            }
        }
        //把最新的会话插入第一行
        mConversations.add(0, conversationObject);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "更新会话： " + (mAdapter != null ? mAdapter.getItemCount() : 0));
    }

    @Override
    public void onDeleteConversationSuccess(String target) {
        /*if (mAdapter != null && mAdapter.getItemPosition(target) != -1){
            int position = mAdapter.getItemPosition(target);
            mConversations.remove(position);
            mAdapter.notifyItemRemoved(position);
            Log.d(TAG, "删除会话" + position + target);
        }*/
        for (int i = 0; i < mConversations.size(); i++) {
            ConversationObject object = mConversations.get(i);
            if (object.target.equals(target)){
                mConversations.remove(object);
                if (mAdapter != null){
                    Log.d(TAG, "删除会话" + i + target);
                    mAdapter.notifyItemRemoved(i);
                }
                break;
            }
        }
    }
}
