package weekl.chatdemo.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class CommonViewHolder {
    private Context mContext;
    private View mConvertView;
    private SparseArray mViews;
    private int mPosition;
    private int mItemViewType;

    private CommonViewHolder(Context context, ViewGroup parent, int layoutResId, int position, int itemViewType) {
        mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        mConvertView.setTag(this);
        mViews = new SparseArray();
        mPosition = position;
        mItemViewType = itemViewType;
    }

    /**
     * 获取ViewHolder的公开静态方法，
     *
     * @param context
     * @param parent
     * @param layoutResId
     * @param position
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent,
                                       int layoutResId, int position, int itemViewType) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutResId, position,itemViewType);
        }
        return (CommonViewHolder) convertView.getTag();
    }

    /**
     * 获取ViewHolder中的控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        T view = (T) mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    /**
     * 获取position
     *
     * @return
     */
    public int getPosition() {
        return mPosition;
    }

    public View getConvertView(){
        return mConvertView;
    }

    public int getIemViewType(){
        return mItemViewType;
    }

    /**
     * 为文本子控件设置文字
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int resId){
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }
}
