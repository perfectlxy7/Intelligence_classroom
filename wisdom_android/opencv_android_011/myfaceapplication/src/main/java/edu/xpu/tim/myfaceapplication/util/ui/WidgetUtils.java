package edu.xpu.tim.myfaceapplication.util.ui;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.xuexiang.xui.R;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.LoadingDialog;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.progress.loading.IMessageLoader;
import com.xuexiang.xui.widget.progress.loading.LoadingViewLayout;

/**
 * 组件工具类
 */
public final class WidgetUtils {

    private WidgetUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 让Activity全屏显示
     *
     * @param activity
     */
    public static void requestFullScreen(@NonNull Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Spinner统一风格
     *
     * @param items
     * @param spinner
     */
    public static void initSpinnerStyle(Spinner spinner, String[] items) {
        setSpinnerDropDownVerticalOffset(spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinner.getContext(), R.layout.xui_layout_spinner_selected_item, R.id.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.xui_layout_spinner_drop_down_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 设置系统Spinner的下拉偏移
     *
     * @param spinner
     */
    public static void setSpinnerDropDownVerticalOffset(Spinner spinner) {
        int itemHeight = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_item_height_size);
        int dropdownOffset = ThemeUtils.resolveDimension(spinner.getContext(), R.attr.ms_dropdown_offset);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            spinner.setDropDownVerticalOffset(0);
        } else {
            spinner.setDropDownVerticalOffset(itemHeight + dropdownOffset);
        }
    }

    /**
     * 获取loading加载框
     *
     * @param context
     * @return
     */
    public static LoadingDialog getLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 获取loading加载框
     *
     * @param context
     * @param message 提示文字
     * @return
     */
    public static LoadingDialog getLoadingDialog(Context context, String message) {
        return new LoadingDialog(context, message);
    }

    /**
     * 更新loading加载框的提示信息
     *
     * @param loadingDialog loading加载框
     * @param message       提示文字
     * @return
     */
    public static LoadingDialog updateLoadingMessage(LoadingDialog loadingDialog, Context context, String message) {
        if (loadingDialog == null) {
            loadingDialog = getLoadingDialog(context);
        }
        loadingDialog.updateMessage(message);
        loadingDialog.show();
        return loadingDialog;
    }


    /**
     * 获取IMessageLoader
     *
     * @param isDialog
     * @param context
     * @return
     */
    public static IMessageLoader getMessageLoader(boolean isDialog, Context context) {
        return isDialog ? new LoadingDialog(context) : new LoadingViewLayout(context);
    }


    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context
     * @return
     */
    public static MiniLoadingDialog getMiniLoadingDialog(Context context) {
        return new MiniLoadingDialog(context);
    }

    /**
     * 获取MiniLoadingDialog加载框
     *
     * @param context
     * @param message 提示文字
     * @return
     */
    public static MiniLoadingDialog getMiniLoadingDialog(Context context, String message) {
        return new MiniLoadingDialog(context, message);
    }

}
