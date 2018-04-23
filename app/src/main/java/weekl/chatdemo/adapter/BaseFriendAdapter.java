package weekl.chatdemo.adapter;

import java.util.List;

import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactPresenter;
import weekl.chatdemo.contact.IContactView;

public abstract class BaseFriendAdapter<T> extends CommonAdapter<T> {
    protected ContactActivity mActivity;
    protected IContactPresenter.IContact mPresenter;

    public BaseFriendAdapter(ContactActivity contactActivity, List<T> datas, int itemLayoutId) {
        super(contactActivity, datas, itemLayoutId);
        mActivity = contactActivity;
        mPresenter = mActivity.getContactPresenter();
    }
}
