package weekl.chatdemo.chat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.adapter.ChatMessageAdapter;
import weekl.chatdemo.base.BaseActivity;
import weekl.chatdemo.model.MsgObject;

public class ChatActivity extends BaseActivity implements IChat.View {
    private static final String TAG = "ChatActivity";
    private IChat.Presenter mPresenter;
    private String mTarget;

    private ListView mChatMsgContent;
    private EditText inputMsg;
    private Button sendBtn;

    private ChatMessageAdapter mMsgAdapter;
    private List<MsgObject> mMsgObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTarget = getIntent().getStringExtra("target");
        initView();

        mPresenter = new ChatPresenter(mTarget, this);
        mPresenter.loadMsgRecord();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mTarget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputMsg = findViewById(R.id.input_msg);

        mMsgObjects = new ArrayList<>();
        mMsgAdapter = new ChatMessageAdapter(mMsgObjects);

        mChatMsgContent = findViewById(R.id.chat_msg_content);

        mChatMsgContent.setAdapter(mMsgAdapter);

        sendBtn = findViewById(R.id.send_msg);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = inputMsg.getText().toString();
                mPresenter.sendTextMsg(msgText);
                inputMsg.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void updateMsg(MsgObject msgObject) {
        mMsgObjects.add(msgObject);
        mMsgAdapter.notifyDataSetChanged();
        mChatMsgContent.setSelection(mMsgAdapter.getCount() - 1);
        Log.d(TAG, "更新消息：" + msgObject.msgText);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
