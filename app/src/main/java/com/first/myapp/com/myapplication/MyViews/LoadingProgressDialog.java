package com.first.myapp.com.myapplication.MyViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.myapp.com.myapplication.R;
import com.first.myapp.com.myapplication.util.StringUtil;


public class LoadingProgressDialog extends AlertDialog {
    private ImageView mSpaceshipImage;
    private TextView mTitleText;
    private TextView mTipTextView;
    private TextView mSubTitleTextView;
    private TextView mButtonText;
    private static MyOnClickListener mListener;

    public LoadingProgressDialog(Context context) {
        super(context);
    }

    public LoadingProgressDialog(Context context, int them) {
        super(context, them);

    }

    public static LoadingProgressDialog showDialog(Context context, CharSequence msg) {
        return showDialog(context, "", msg);
    }

    public static LoadingProgressDialog showDialog(Context context, String title, CharSequence msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_without_button, null);
        return initCommonViewAndShow(context, title, msg, v);
    }

    public static LoadingProgressDialog showOneButtonDialog(Context context, CharSequence msg) {
        return showOneButtonDialog(context, "", context.getString(R.string.cancel), msg, null);
    }

    public static LoadingProgressDialog showOneButtonDialog(Context context, String title, String buttonText, CharSequence msg, MyOnClickListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mListener = listener;
        View v = inflater.inflate(R.layout.dialog_with_one_button, null);

        final LoadingProgressDialog loadingDialog = initCommonViewAndShow(context, title, msg, v);
        loadingDialog.mButtonText = (TextView) v.findViewById(R.id.dialog_ok_button);
        loadingDialog.mButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                if (mListener != null) {
                    mListener.onclick(v);
                }
            }
        });
        loadingDialog.mSubTitleTextView = (TextView) v.findViewById(R.id.dialog_sub_title_text);
        if (buttonText != null) {
            loadingDialog.mButtonText.setText(buttonText);
        }

        return loadingDialog;
    }

    @NonNull
    private static LoadingProgressDialog initCommonViewAndShow(Context context, String title, CharSequence msg, View v) {
        LoadingProgressDialog loadingDialog = new LoadingProgressDialog(context, R.style.loading_dialog);

        loadingDialog.mSpaceshipImage = (ImageView) v.findViewById(R.id.loading_oval);
        loadingDialog.mTitleText = (TextView) v.findViewById(R.id.dialog_title_text);
        loadingDialog.mTipTextView = (TextView) v.findViewById(R.id.dialog_content_text);
//        loadingDialog.mTipTextView.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        loadingDialog.mSpaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (title != null) {
            loadingDialog.mTitleText.setText(title);
            if (StringUtil.isEmpty(title)) {
                loadingDialog.mTitleText.setVisibility(View.GONE);
            } else {
                loadingDialog.mTitleText.setVisibility(View.VISIBLE);
            }
        }
        if (msg != null && !StringUtil.isEmpty(msg.toString())) {
            loadingDialog.mTipTextView.setText(msg);
        } else {
            loadingDialog.mTipTextView.setVisibility(View.GONE);
        }
        if (!loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
            loadingDialog.show();
//            Window window = loadingDialog.getWindow();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.gravity = Gravity.CENTER;
//            int screenWidth = ScreenUtils.getScreenWidth(context);
//            lp.width = (int) (screenWidth * 0.8);
//            loadingDialog.getWindow().setAttributes(lp);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(v);
        return loadingDialog;
    }

    @Override
    public void show() {
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation);
        mSpaceshipImage.startAnimation(hyperspaceJumpAnimation);

        super.show();
    }

    public void updateTitle(String title) {
        if (title != null) {
            mTitleText.setText(title);
        }
    }

    public void updateTipMessage(CharSequence msg) {
        if (msg != null && !StringUtil.isEmpty(msg.toString())) {
            mTipTextView.setVisibility(View.VISIBLE);
            mTipTextView.setText(msg);
        } else {
            mTipTextView.setVisibility(View.GONE);
        }
    }

    public void updateSubTitle(CharSequence subTitle) {
        if (mSubTitleTextView != null) {
            if (subTitle != null && !StringUtil.isEmpty(subTitle.toString())) {
                mSubTitleTextView.setVisibility(View.VISIBLE);
                mSubTitleTextView.setText(subTitle);
            } else {
                mSubTitleTextView.setVisibility(View.GONE);
            }
        }
    }

    public void SetTipMessageGravity(int gravity) {
        mTipTextView.setGravity(gravity);
    }

    public interface MyOnClickListener {
        void onclick(View v);
    }
}
