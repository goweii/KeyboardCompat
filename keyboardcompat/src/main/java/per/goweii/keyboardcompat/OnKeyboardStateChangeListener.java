package per.goweii.keyboardcompat;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/4/29
 */
public interface OnKeyboardStateChangeListener {
    void onKeyboardStateChanged(boolean isShown, int height, int orientation);
}