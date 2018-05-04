package weekl.chatdemo.contact.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import weekl.chatdemo.R;
import weekl.chatdemo.contact.ContactActivity;
import weekl.chatdemo.contact.IContactView;

public class MoreFragment extends Fragment implements IContactView.IMoreView, View.OnClickListener {
    private View mView;
    private ContactActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (ContactActivity) getActivity();
        mActivity.setToolbarTitle(getString(R.string.more));
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_more,container,false);

            (mView.findViewById(R.id.more_logout_btn)).setOnClickListener(this);
        }
        return mView;
    }



    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()){
            case R.id.more_logout_btn:
                mActivity.logout();
                break;
        }
    }
}
