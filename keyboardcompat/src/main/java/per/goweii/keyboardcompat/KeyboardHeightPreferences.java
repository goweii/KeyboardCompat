package per.goweii.keyboardcompat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/4/29
 */
class KeyboardHeightPreferences {

    private static final String SP_NAME = "keyboard_height";
    private static final String KEY_HEIGHT_PORTRAIT = "height_portrait";
    private static final String KEY_HEIGHT_LANDSCAPE = "height_landscape";

    private static volatile KeyboardHeightPreferences INSTANCE = null;
    private final SharedPreferences mPreferences;

    private KeyboardHeightPreferences(Context context){
        mPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    static KeyboardHeightPreferences getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (KeyboardHeightPreferences.class){
                if (INSTANCE == null) {
                    INSTANCE = new KeyboardHeightPreferences(context);
                }
            }
        }
        return INSTANCE;
    }

    int getHeightPortrait(){
        return mPreferences.getInt(KEY_HEIGHT_PORTRAIT, 0);
    }

    int getHeightLandscape(){
        return mPreferences.getInt(KEY_HEIGHT_LANDSCAPE, 0);
    }

    void saveHeightPortrait(int height){
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(KEY_HEIGHT_PORTRAIT, height);
        edit.apply();
    }

    void saveHeightLandscape(int height){
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(KEY_HEIGHT_LANDSCAPE, height);
        edit.apply();
    }
}
