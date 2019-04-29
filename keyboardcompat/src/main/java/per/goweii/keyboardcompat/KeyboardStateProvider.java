package per.goweii.keyboardcompat;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/4/29
 */
class KeyboardStateProvider extends PopupWindow {
    private OnKeyboardStateChangeListener mOnKeyboardStateChangeListener;
    private final Activity mActivity;
    private final View mPopupView;
    private final View mParentView;
    private int mKeyboardLandscapeHeight;
    private int mKeyboardPortraitHeight;

    KeyboardStateProvider(Activity activity) {
        super(activity);
        this.mActivity = activity;
        activity.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mPopupView = new LinearLayout(activity);
        setContentView(mPopupView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mParentView = activity.getWindow().getDecorView();
        setWidth(0);
        setHeight(LayoutParams.MATCH_PARENT);
        mPopupView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                handleOnGlobalLayout();
            }
        });
    }

    void start() {
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                if (!isShowing() && mParentView.getWindowToken() != null) {
                    setBackgroundDrawable(new ColorDrawable(0));
                    showAtLocation(mParentView, Gravity.NO_GRAVITY, 0, 0);
                }
            }
        });
    }

    void close() {
        this.mOnKeyboardStateChangeListener = null;
        dismiss();
    }

    void setKeyboardHeightObserver(OnKeyboardStateChangeListener observer) {
        this.mOnKeyboardStateChangeListener = observer;
    }

    private int getScreenOrientation() {
        return mActivity.getResources().getConfiguration().orientation;
    }

    private void handleOnGlobalLayout() {
        Point screenSize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        Rect rect = new Rect();
        mPopupView.getWindowVisibleDisplayFrame(rect);
        int orientation = getScreenOrientation();
        int keyboardHeight = screenSize.y - rect.bottom;
        if (keyboardHeight == 0) {
            notifyKeyboardStateChanged(false, 0, orientation);
        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.mKeyboardPortraitHeight = keyboardHeight;
                notifyKeyboardStateChanged(true, mKeyboardPortraitHeight, orientation);
            } else {
                this.mKeyboardLandscapeHeight = keyboardHeight;
                notifyKeyboardStateChanged(true, mKeyboardLandscapeHeight, orientation);
            }
        }
    }

    private void notifyKeyboardStateChanged(boolean isShow, int height, int orientation) {
        if (mOnKeyboardStateChangeListener != null) {
            mOnKeyboardStateChangeListener.onKeyboardStateChanged(isShow, height, orientation);
        }
    }
}