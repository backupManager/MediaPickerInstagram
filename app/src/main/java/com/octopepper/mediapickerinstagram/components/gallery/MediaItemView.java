package com.octopepper.mediapickerinstagram.components.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.commons.modules.ReboundModule;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaItemView extends RelativeLayout {

    @BindView(R.id.mMediaThumb)
    ImageView mMediaThumb;

    private File mCurrentFile;
    private ReboundModule mReboundModule = ReboundModule.getInstance();
    private WeakReference<MediaItemViewListener> mWrListener;

    void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        View v = View.inflate(context, R.layout.media_item_view, this);
        ButterKnife.bind(this, v);
    }

    public void bind(File file) {
        mCurrentFile = file;
        mReboundModule.init(mMediaThumb);
        Picasso.with(getContext())
                .load(Uri.fromFile(file))
                .resize(350, 350)
                .centerCrop()
                .placeholder(R.drawable.placeholder_media)
                .error(R.drawable.placeholder_error_media)
                .noFade()
                .into(mMediaThumb);
        mMediaThumb.setOnTouchListener(addOnTouchListener());
    }

    private OnTouchListener addOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mReboundModule.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                        mWrListener.get().onClickItem(mCurrentFile);
                    case MotionEvent.ACTION_CANCEL:
                        mReboundModule.setEndValue(0);
                        break;
                }
                return true;
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}