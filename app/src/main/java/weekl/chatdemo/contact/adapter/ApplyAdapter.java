package weekl.chatdemo.contact.adapter;

import android.view.View;
import android.widget.Button;

import java.util.List;

import weekl.chatdemo.R;
import weekl.chatdemo.base.CommonAdapter;
import weekl.chatdemo.base.CommonViewHolder;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactPresenter;
import weekl.chatdemo.model.ApplyInfo;

public class ApplyAdapter extends CommonAdapter<ApplyInfo>{
    private IContactPresenter.IContact mPresenter;

    public ApplyAdapter(ContactActivity contactActivity, List<ApplyInfo> datas) {
        super(contactActivity, datas, R.layout.item_new_friend);
        mPresenter = contactActivity.getContactPresenter();
    }

    @Override
    public void convert(CommonViewHolder holder, ApplyInfo bean) {
        final String name = bean.name;
        holder.setText(R.id.new_friend_name, name);
        holder.setText(R.id.new_friend_reason, bean.reason);
        final Button refuseBtn = holder.getView(R.id.new_friend_refuse);
        final Button acceptBtn = holder.getView(R.id.new_friend_accept);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.new_friend_refuse:
                        mPresenter.refuseFriend(name);
                        refuseBtn.setText("已拒绝");
                        refuseBtn.setEnabled(false);
                        acceptBtn.setEnabled(false);
                        break;
                    case R.id.new_friend_accept:
                        mPresenter.acceptFriend(name);
                        acceptBtn.setText("已同意");
                        refuseBtn.setEnabled(false);
                        acceptBtn.setEnabled(false);
                        break;
                }
            }
        };
        refuseBtn.setOnClickListener(listener);
        acceptBtn.setOnClickListener(listener);
    }
}
