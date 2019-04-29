package per.goweii.keyboardcompat;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/4/29
 */
public class KeyboardCompat implements OnKeyboardStateChangeListener {

    private final KeyboardStateProvider mProvider;
    private final KeyboardHeightPreferences mPreferences;
    private OnStateChangeListener mOnStateChangeListener = null;

    public static int getHeightPortrait(Context context) {
        return KeyboardHeightPreferences.getInstance(context).getHeightPortrait();
    }

    public static int getHeightLandscape(Context context) {
        return KeyboardHeightPreferences.getInstance(context).getHeightLandscape();
    }

    public static KeyboardCompat with(Activity activity) {
        return new KeyboardCompat(activity);
    }

    private KeyboardCompat(Activity activity) {
        mProvider = new KeyboardStateProvider(activity);
        mProvider.setKeyboardHeightObserver(this);
        mPreferences = KeyboardHeightPreferences.getInstance(activity);
    }

    public KeyboardCompat setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
        return this;
    }

    public void attach() {
        mProvider.start();
    }

    public void detach() {
        mProvider.close();
    }

    @Override
    public void onKeyboardStateChanged(boolean isShown, int height, int orientation) {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onStateChanged(isShown, height, orientation);
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPreferences.saveHeightPortrait(height);
        } else {
            mPreferences.saveHeightLandscape(height);
        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(boolean isShown, int height, int orientation);
    }
}
