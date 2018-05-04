package weekl.chatdemo.contact.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactPresenter;
import weekl.chatdemo.contact.IContactView;
import weekl.chatdemo.contact.adapter.ApplyAdapter;
import weekl.chatdemo.contact.adapter.FriendAdapter;
import weekl.chatdemo.model.ApplyInfo;
import weekl.chatdemo.model.User;

public class FriendFragment extends Fragment implements IContactView.IFriendView {
    private static final String TAG = "IFriendView";
    private ContactActivity mActivity;
    private IContactPresenter.IContact mPresenter;
    private View mView;


    //好友列表容器ListView
    private ListView friendListView;
    //好友列表
    private List<User> friends;
    //好友列表适配器
    private FriendAdapter friendAdapter;

    //功能列表容器LinearLayout
    private LinearLayout functionViewContainer;
    //功能View列表
    private List<View> functionViews;

    //申请列表
    private List<ApplyInfo> applyInfos;
    //申请列表适配器
    private ApplyAdapter applyAdapter;
    //申请未处理数量
    private int applyCount;
    //申请数量TextView
    private TextView applyCountText;

    public FriendFragment() {
        super();
        functionViews = new ArrayList<>();
        friends = new ArrayList<>();
        applyInfos = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (ContactActivity) getActivity();
        mActivity.setToolbarTitle(getString(R.string.contact));
        if (mView == null) {
            mPresenter = mActivity.getContactPresenter();
            mView = inflater.inflate(R.layout.fragment_friend, container, false);
            initView();
        }
        return mView;
    }

    private void updateApplyCount(int count) {
        if (applyCountText != null) {
            if (applyCount > 0) {
                applyCountText.setText(count + "");
                applyCountText.setVisibility(View.VISIBLE);
            } else {
                applyCountText.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        //功能列表
        functionViewContainer = mView.findViewById(R.id.friend_function_content);
        functionViewContainer.removeAllViews();
        if (functionViews.size() == 0) {
            String functions[] = {"添加好友", "新朋友", "群聊"};
            for (int i = 0; i < functions.length; i++) {
                View view = LayoutInflater.from(getActivity()).
                        inflate(R.layout.item_friend_function, functionViewContainer, false);
                TextView functionName = view.findViewById(R.id.friend_function_name);
                functionName.setText(functions[i]);
                view.setTag(functions[i]);
                view.setOnClickListener(functionClickListener);
                functionViews.add(view);
                functionViewContainer.addView(view);
                if (i == 1) {
                    applyCountText = view.findViewById(R.id.friend_function_unread);
                }
            }
        } else {
            for (View view : functionViews) {
                functionViewContainer.addView(view);
            }
        }

        //好友列表
        friendListView = mView.findViewById(R.id.friend_list);
        friendAdapter = new FriendAdapter(mActivity, friends);
        friendListView.setAdapter(friendAdapter);

        //好友申请列表
        applyAdapter = new ApplyAdapter(mActivity, applyInfos);
    }

    @Override
    public void onDeleteSuccess(String name) {
        for (User user : friends) {
            if (user.name.equals(name)) {
                friends.remove(user);
                if (friendAdapter != null) {
                    friendAdapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    /**
     * 在界面上添加好友
     *
     * @param friend
     */
    @Override
    public void addFriend(User friend) {
        boolean isExists = false;
        for (User user : friends) {
            if (user.name.equals(friend.name)) {
                isExists = true;
                break;
            }
        }
        if (!isExists) {
            friends.add(friend);
            if (friendAdapter != null) {
                friendAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 已处理好友申请
     *
     * @param name
     */
    @Override
    public void onHandleApplySuccess(String name) {
        for (ApplyInfo info : applyInfos) {
            if (info.name.equals(name)) {
                applyInfos.remove(info);
                updateApplyCount(--applyCount);
                mActivity.requestLoadConversations();
                break;
            }
        }
    }

    /**
     * 收到好友申请
     *
     * @param name
     * @param reason
     */
    @Override
    public void onContactInvited(String name, String reason) {
        ApplyInfo info = new ApplyInfo(name, reason);
        for (ApplyInfo applyInfo : applyInfos) {
            if (applyInfo.name.equals(name)) {
                applyInfos.remove(applyInfo);
                applyCount--;
                break;
            }
        }
        applyInfos.add(info);
        updateApplyCount(++applyCount);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFriendSuccess(List<User> userList) {
        for (User friend : userList) {
            addFriend(friend);
        }
    }

    /**
     * 添加朋友Dialog
     *
     * @return
     */
    private AlertDialog getAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加好友");
        View convertView = View.inflate(getActivity(), R.layout.dialog_add_friend, null);
        builder.setView(convertView);
        final EditText inputName = convertView.findViewById(R.id.add_friend_name);
        final EditText inputReason = convertView.findViewById(R.id.add_friend_reason);
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString();
                String reason = inputReason.getText().toString();
                showProgressDialog();
                mPresenter.sendApply(name, reason);
                closeProgressDialog();
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    /**
     * 查看好友申请Dialog
     *
     * @return
     */
    private AlertDialog getNewFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("新朋友");
        LinearLayout convertView = (LinearLayout) View.inflate(getActivity(), R.layout.dialog_new_friend, null);
        ListView newFriendList = convertView.findViewById(R.id.new_friend_list);
        newFriendList.setAdapter(applyAdapter);
        builder.setView(convertView);
        return builder.create();
    }

    /**
     * FriendFunction的监听
     */
    private View.OnClickListener functionClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            switch ((String) v.getTag()) {
                case "添加好友":
                    getAddFriendDialog().show();
                    break;
                case "新朋友":
                    getNewFriendDialog().show();
                    break;
                case "群聊":
                    break;
            }
        }
    };


    private ProgressDialog dialog;

    private void showProgressDialog() {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new ProgressDialog(mActivity);
            dialog.show();
        }
    }

    private void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
