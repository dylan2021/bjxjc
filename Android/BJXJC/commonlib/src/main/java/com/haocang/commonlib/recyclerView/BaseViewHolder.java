package com.haocang.commonlib.recyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.haocang.commonlib.otherutil.ImageUtils;


/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/03/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */


public class BaseViewHolder extends RecyclerView.ViewHolder {
    private String TAG = BaseViewHolder.class.getSimpleName();
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private BaseAdapter mAdapter;

    protected BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
    }

    public static BaseViewHolder getViewHolder(ViewGroup parent, int layoutId) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new BaseViewHolder(parent.getContext(), view);
    }

    public static BaseViewHolder getViewHolder(ViewGroup parent, View view) {
        return new BaseViewHolder(parent.getContext(), view);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setTextAndColor(@IdRes int viewId, CharSequence text,int color) {
        TextView textView = getView(viewId);
        textView.setText(text);
        textView.setTextColor(color);
        return this;
    }

    public BaseViewHolder setTypefaceColor(Context context, @IdRes int viewId, String path,int text, int Color) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), path);
        TextView textView = getView(viewId);
        textView.setTextColor(Color);
        textView.setText(context.getResources().getText(text));
        textView.setTypeface(iconfont);
        return this;
    }

    public BaseViewHolder setChecked(@IdRes int viewId, Boolean b) {
        CheckBox cb = getView(viewId);
        cb.setChecked(b);
        return this;
    }


    public BaseViewHolder addTextView(@IdRes int viewId, CharSequence text,Context context) {

        LinearLayout lin = getView(viewId);
        TextView tv = new TextView(context);
        //2.把信息设置为文本框的内容
        tv.setText(text);
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER);
        tv.setEms(6);
        tv.setLines(2);
        tv.setTextColor(Color.BLACK);
        //3.把textView设置为线性布局的子节点
        lin.addView(tv);
        return this;
    }

    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setImageFile(@IdRes int viewId,String ImgUrl) {
        Bitmap bitmaps = ImageUtils.getBitmapFilePath(ImgUrl); // 从本地取图片
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmaps);
        return this;
    }

    public BaseViewHolder setBackgroundColor(@IdRes int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setTextColor(@IdRes int viewId, int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    public BaseViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setAlpha(@IdRes int viewId, float value) {
        View view = getView(viewId);
        view.setAlpha(value);
        return this;
    }

    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public BaseViewHolder setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public BaseViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 子类点击事件需要在convert里面添加
     *
     * @param viewId
     * @return this
     */
    public BaseViewHolder addOnClickListener(@IdRes int viewId) {
        View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getOnItemChildClickListener() != null) {
                    mAdapter.getOnItemChildClickListener().onItemChildClick(mAdapter, v, BaseViewHolder.this.getAdapterPosition() - mAdapter.getHeaderCount());
                }
            }
        });
        return this;
    }

    public BaseViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 子类点击事件需要在convert里面添加
     *
     * @param viewId
     * @return this
     */
    public BaseViewHolder addOnLongClickListener(@IdRes int viewId) {
        View view = getView(viewId);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mAdapter.getOnItemChildLongClickListener() != null &&
                        mAdapter.getOnItemChildLongClickListener().onItemChildLongClick(mAdapter, v,
                                BaseViewHolder.this.getAdapterPosition() - mAdapter.getHeaderCount());
            }
        });
        return this;
    }

    public BaseViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemClickListener(@IdRes int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemLongClickListener(@IdRes int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnItemSelectedClickListener(@IdRes int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    public BaseViewHolder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

//    public BaseViewHolder setImageUrl(@IdRes int viewId, String url) {
//        ImageView imageView = getView(viewId);
//        ImageLoader.display(mContext, url, imageView);
//        return this;
//    }

    public View getItemView() {
        return mConvertView;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
    }
}

