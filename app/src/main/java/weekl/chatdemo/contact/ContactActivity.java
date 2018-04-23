package weekl.chatdemo.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.base.BaseActivity;
import weekl.chatdemo.login.LoginActivity;
import weekl.chatdemo.model.ConversationObject;
import weekl.chatdemo.chat.ChatActivity;
import weekl.chatdemo.model.User;

public class ContactActivity extends BaseActivity implements IContactView.IView {
    private RelativeLayout mToolbar;
    private BottomNavigationView bottomNavigation;

    private ConversationFragment conversationFragment;
    private FriendFragment friendFragment;
    private MoreFragment moreFragment;

    private IContactPresenter.IPresenter mPresenter;
    private IContactPresenter.IMessage mMessagePresenter;
    private IContactPresenter.IContact mContactPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initView();

        mPresenter = new ContactBasePresenter(this) ;
        mMessagePresenter = (IContactPresenter.IMessage)
                mPresenter.getPresenter(IContactPresenter.Index.INDEX_MESSAGE);
        mContactPresenter = (IContactPresenter.IContact)
                mPresenter.getPresenter(IContactPresenter.Index.INDEX_CONTACT);
    }

    private void initView() {
        conversationFragment = new ConversationFragment();
        friendFragment = new FriendFragment();
        moreFragment = new MoreFragment();

        mToolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.contact_bottom_view);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.contact_bottom_msg:
                        replaceFragment(conversationFragment);
                        break;
                    case R.id.contact_bottom_contact:
                        replaceFragment(friendFragment);
                        break;
                    case R.id.contact_bottom_more:
                        replaceFragment(moreFragment);
                        break;
                }
                return true;
            }
        });
        replaceFragment(conversationFragment);
        bottomNavigation.getMenu().getItem(0).setChecked(true);
    }

    public void setToolbarTitle(String title) {
        TextView titleView = mToolbar.findViewById(R.id.toolbar_title);
        titleView.setText(title);
    }

    public IContactPresenter.IMessage getMessagePresenter(){
        return mMessagePresenter;
    }

    public IContactPresenter.IContact getContactPresenter(){
        return mContactPresenter;
    }

    public void requestLoadConversations(){
        mPresenter.loadConversations();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.contact_content, fragment);
        transaction.commit();
    }

    public void openConversation(String target) {
        Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
        intent.putExtra("target", target);
        startActivity(intent);
    }

    @Override
    public IContactView getView(Index index) {
        switch (index){
            case INDEX_CONVERSATION:
                return conversationFragment;
            case INDEX_FRIEND:
                return friendFragment;
            case INDEX_MORE:
                return moreFragment;
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadConversations();
        mPresenter.loadFriends();
    }

    @Override
    public void onBackPressed() {
        final String alwaysExists = getCurUser() + "alwaysExists";
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean exists = pref.getBoolean(alwaysExists, false);
        if (exists) {
            ContactActivity.super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("注销用户或退出");
            builder.setPositiveButton("注销", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            });
            builder.setNeutralButton("退出，不再提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(alwaysExists, true);
                    editor.apply();
                    ContactActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContactActivity.super.onBackPressed();
                }
            });
            builder.create().show();
        }
    }

    public void logout(){
        mPresenter.logout();
    }

    @Override
    public void onLogoutSuccess() {
        Intent intent = new Intent(ContactActivity.this, LoginActivity.class);
        intent.putExtra("userName", getCurUser());
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
