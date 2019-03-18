package demo.base;

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import java.lang.reflect.ParameterizedType


/**
 * BaseActivity
 * @author yyx
 */

abstract class BaseActivity<V : IBaseView, T : BasePresenter<V>>
    : AppCompatActivity(), IBaseView, View.OnClickListener {

    val VISIBLE = View.VISIBLE
    val GONE = View.GONE
    val INVISIBLE = View.INVISIBLE

    // ======== url请求列表 ========
    val URL_LIST = 0

    // ======== 网络请求加载模式 ========
    protected val LOAD_AUTO = 0 // 加载方式 （自�?0、顶部刷�?1、加载更�?2�?
    protected val LOAD_TOP = 1
    protected val LOAD_MORE = 2

    protected val TAG = CommonUtils.getTag(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = this!!.getInstance(this, 1)!!
        mPresenter.attachView(this as V)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true)
        }
        setContentView(layoutResID())
        initView()
        initData()
    }

    @TargetApi(19)
    private fun setTranslucentStatus(on: Boolean) {
        val win = window
        val winParams = win.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    protected abstract fun layoutResID(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract var mPresenter: T

    override fun getContext(): Activity = this

    override fun getUrl(url_type: Int): String {
        return ""
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        return mutableMapOf()
    }

    override fun onError(url_type: Int, load_type: Int, error: String) {
    }

    override fun showLoadingUI(url_type: Int, load_type: Int) {
    }

    override fun hideLoadingUI(url_type: Int, load_type: Int, success: Boolean) {
    }

    override fun onClick(v: View?) {
    }

    //通过这个方法快�?�获取一个view，自动强制类型转�?
    protected fun <T : View> fv(id: Int, vararg view: View): T {
        return if (view.size > 0) {
            view[0].findViewById<View>(id) as T
        } else findViewById<View>(id) as T
    }

    /**
     * 判断�?
     */
    fun isEmpty(list: Any): Boolean {
        return CommonUtils.isEmpty(list)
    }

    /**
     * toast
     */
    fun toast(text: CharSequence) {
        CommonUtils.toast(getContext(), text)
    }

    /**
     * log 输出
     */
    fun log(msg: String, vararg tags: String) {
        CommonUtils.log(msg, TAG)
    }

    /**
     * print log 输出
     */
    fun println(text: Any) {
        CommonUtils.println(text)
    }

    /**
     * 格式�?
     */
    fun format(format: String, vararg args: Any): String {
        return CommonUtils.format(format, *args)
    }

    /**
     * 格式化N位小�?
     *
     *
     * 默认保留2�?
     */
    fun formatDouble(number: Double, vararg n: Int): String {
        return CommonUtils.formatDouble(number, *n)
    }

    /**
     * 设置view 可见�?
     */
    fun setViewVisible(view: View, vararg isVisible: Boolean) {
        CommonUtils.setViewVisible(view, *isVisible)
    }

    /**
     * 设置点击监听
     */
    fun setOnClickListener(view: View) {
        CommonUtils.setOnClickListener(view, this)
    }

    /**
     * 设置view 选中
     */
    fun setViewSelect(view: View, vararg isSelect: Boolean) {
        CommonUtils.setViewSelect(view, *isSelect)
    }

    /**
     * 设置 文本
     */
    fun setText(view: TextView, text: CharSequence) {
        CommonUtils.setText(view, text)
    }

    /**
     * 设置view 选中
     */
    fun setViewEnable(view: View, vararg isEnable: Boolean) {
        CommonUtils.setViewEnable(view, *isEnable)
    }

    /**
     * 加载 layout
     */
    fun inflateView(layoutId: Int, vararg root: ViewGroup): View {
        return CommonUtils.inflateView(getContext(), layoutId, *root)
    }

    /**
     * 显示Toast
     */
    fun showToast(msg: Any) {
        toast(msg.toString())
    }

    /**
     * 隐藏软键�?
     */
    fun hideSoftKeyboard() {
        val imm = getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && getContext().currentFocus != null) {
            if (getContext().currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(getContext().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    /**
     * 加载 尺寸
     */
    fun getDimen_(resId: Int): Float {
        return CommonUtils.getDimen_(getContext(), resId)
    }

    /**
     * 加载 drawable
     */
    fun getDrawable_(resId: Int): Drawable {
        return CommonUtils.getDrawable_(getContext(), resId)
    }

    /**
     * 加载 color
     */
    fun getColor_(resId: Int): Int {
        return CommonUtils.getColor_(getContext(), resId)
    }

    /**
     * 设置 color
     * eg: 0xff00ff00 16进制
     */
    fun setViewColor(view: View, color_hex: Int) {
        CommonUtils.setViewColor(view, color_hex)
    }

    /**
     * 设置 color
     * eg: R.color|drawable.xx
     */
    fun setViewColorRes(view: View, resId: Int) {
        CommonUtils.setViewColorRes(view, resId)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        mPresenter.unsubscribe()
    }

    fun <T> getInstance(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                    .genericSuperclass as ParameterizedType).getActualTypeArguments()[i] as Class<T>)
                    .newInstance()
        } catch (e: Fragment.InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: java.lang.InstantiationException) {
            e.printStackTrace()
        }

        return null
    }

}
