package weekl.chatdemo.contact.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.CountDownLatch;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import weekl.chatdemo.R;
import weekl.chatdemo.base.BaseActivity;
import weekl.chatdemo.model.User;
import weekl.chatdemo.util.BmobUtil;

public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoActivity";
    private User mUser;

    private ImageView avatar;
    private LinearLayout itemContent;

    private boolean hasChange = false;

    private final String params[] = new String[]{"nickName", "userId", "signature", "sex", "age", "phoneNum", "email"};
    private final String keys[] = new String[]{"昵称", "用户名", "个性签名", "性别", "年龄", "手机号", "邮箱"};
    private String values[] = new String[keys.length];
    private TextView[] views = new TextView[keys.length];

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initItem();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("修改个人信息");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        avatar = findViewById(R.id.avatar);
        itemContent = findViewById(R.id.item_content);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //初始化用户信息
    private void initData() {
        BmobUtil.findUser(getEMClient().getCurrentUser(), new BmobUtil.FindUserListener() {
            @Override
            public void onSuccess(User user) {
                mUser = user;
                Log.d(TAG, "当前用户： " + mUser);
                String sex = "";
                if (mUser.getSex() == User.Sex.male) {
                    sex = "男";
                } else if (mUser.getSex() == User.Sex.female) {
                    sex = "女";
                }
                values = new String[]{mUser.getNickName(), mUser.getUserId(),
                        mUser.getSignature(), sex, String.valueOf(mUser.getAge()),
                        mUser.getPhoneNum(), "没有邮箱字段"};
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(UserInfoActivity.this).load(mUser.getAvatarUrl()).into(avatar);
                        for (int i = 0; i < keys.length; i++) {
                            views[i].setText(values[i]);
                        }
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "查找用户信息失败：" + errorCode + "," + errorMsg);
            }
        });
    }

    //初始化设置项
    private void initItem() {
        itemContent.removeAllViews();
        int position = 0;
        for (String key : keys) {
            View item = LayoutInflater.from(this).inflate(R.layout.item_user_info, itemContent, false);
            ((TextView) item.findViewById(R.id.key)).setText(key);
            TextView textView = item.findViewById(R.id.value);
            textView.setTag(key);
            item.setTag(position++);
            item.setOnClickListener(itemClickListener);
            itemContent.addView(item);
            if (key.equals("用户名") || key.equals("邮箱")) {
                item.setClickable(false);
            }
        }
        for (int i = 0; i < keys.length; i++) {
            views[i] = itemContent.findViewWithTag(keys[i]);
        }
    }

    /**
     * 设置项点击监听器
     */
    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final TextView value = v.findViewById(R.id.value);
            final EditText changeText = new EditText(UserInfoActivity.this);
            changeText.setHint(value.getText().toString());

            AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
            builder.setTitle("修改信息");
            builder.setView(changeText);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newValue = changeText.getText().toString();
                    value.setText(newValue);

                    int position = Integer.valueOf(v.getTag().toString());
                    hasChange = !values[position].equals(newValue);
                    if (hasChange) {
                        if (newValue.equals("男") || newValue.equals("女")) {
                            User.Sex sex = newValue.equals("男") ? User.Sex.male : User.Sex.female;
                            mUser.setValue("sex", sex);
                        } else {
                            mUser.setValue(params[position], newValue);
                        }
                        values[position] = newValue;
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }
    };

    /**
     * 同步更新云端数据
     *
     * @throws InterruptedException
     */
    private void uploadInfo() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("更新中...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                progressDialog.dismiss();
                final String msg;
                if (e == null) {
                    msg = "更新成功";
                } else {
                    msg = "更新失败:" + e.getMessage();
                }
                Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "更新数据: " + msg);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                //保存更改
                if (hasChange) {
                    uploadInfo();
                } else {
                    Toast.makeText(this, "没有发生改变", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (hasChange) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            uploadInfo();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                    UserInfoActivity.super.onBackPressed();
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否保存修改？");
            builder.setPositiveButton("保存", listener);
            builder.setNegativeButton("取消", listener);
            builder.show();
        } else {
            super.onBackPressed();
        }
    }
}
