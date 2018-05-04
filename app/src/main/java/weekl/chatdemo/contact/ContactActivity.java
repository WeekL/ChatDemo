package weekl.chatdemo.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import weekl.chatdemo.R;
import weekl.chatdemo.base.BaseActivity;
import weekl.chatdemo.chat.ChatActivity;
import weekl.chatdemo.contact.fragment.ConversationFragment;
import weekl.chatdemo.contact.fragment.FriendFragment;
import weekl.chatdemo.contact.fragment.MoreFragment;
import weekl.chatdemo.login.LoginActivity;

public class ContactActivity extends BaseActivity implements IContactView.IView {
    private Toolbar mToolbar;
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

        mPresenter = new ContactBasePresenter(this);
        mMessagePresenter = (IContactPresenter.IMessage)
                mPresenter.getPresenter(IContactPresenter.Index.INDEX_MESSAGE);
        mContactPresenter = (IContactPresenter.IContact)
                mPresenter.getPresenter(IContactPresenter.Index.INDEX_CONTACT);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        conversationFragment = new ConversationFragment();
        friendFragment = new FriendFragment();
        moreFragment = new MoreFragment();

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
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_contact,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    public IContactPresenter.IMessage getMessagePresenter() {
        return mMessagePresenter;
    }

    public IContactPresenter.IContact getContactPresenter() {
        return mContactPresenter;
    }

    public void requestLoadConversations() {
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
        switch (index) {
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
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEUTRAL:
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean(alwaysExists, true);
                            editor.apply();
                            ContactActivity.super.onBackPressed();
                            break;
                        case DialogInterface.BUTTON_POSITIVE:
                            logout();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            ContactActivity.super.onBackPressed();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("注销用户或退出");
            builder.setPositiveButton("注销", listener);
            builder.setNeutralButton("退出，不再提醒", listener);
            builder.setNegativeButton("退出", listener);
            builder.create().show();
        }
    }

    public void logout() {
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
