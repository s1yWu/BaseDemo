package demo.base;

import android.content.Context
import android.os.Bundle
import com.lzy.okgo.OkGo
import com.lzy.okgo.convert.StringConvert
import com.lzy.okgo.model.Response
import com.lzy.okrx2.adapter.ObservableResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

/**
 * @author yyx
 */

abstract class BasePresenter<V : IBaseView> : IBasePresenter<V> {

    protected var mView: V? = null
    protected var compositeDisposable: CompositeDisposable? = null
    // ======== url请求列表 ========
    protected val URL_LIST = 0
    // ======== 网络请求加载模式 ========
    protected val LOAD_AUTO = 0 // 加载方式 （自�?0、顶部刷�?1、加载更�?2�?
    protected val LOAD_TOP = 1
    protected val LOAD_MORE = 2

    open fun addDisposable(disposable: Disposable) {
            if (compositeDisposable == null) {
                compositeDisposable = CompositeDisposable()
            }
            compositeDisposable!!.add(disposable)
    }

    override fun subscribe() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
    }

    override fun unsubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
    }

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }


    protected val TAG = CommonUtils.getTag(this)
    protected var isShowError = true //cancel上次请求 默认true
    protected var isCancel = true //cancel上次请求 默认true

    protected var url_type: Int = URL_LIST
    protected var load_type: Int = LOAD_AUTO
    protected var bundle: Bundle? = null

    init {
        this.isCancel = isShowError
        this.isShowError = isCancel
    }

    fun reqData() {
        reqData(url_type, load_type)
    }

    fun reqData(url_type: Int) {
        reqData(url_type, load_type)
    }

    fun reqDataReTry() {
        reqData(url_type, load_type, bundle!!)
    }

    fun reqData(url_type: Int, load_type: Int, vararg bundle: Bundle) {

        this.url_type = url_type
        this.load_type = load_type
        val bundle1 = if (bundle.size > 0) bundle[0] else null
        this.bundle = bundle1

        if (mView?.getContext() != null && !mView?.getContext()!!.isFinishing) {
            mView?.showLoadingUI(url_type, load_type)
        }

        val url = mView?.getUrl(url_type)
        var params: Map<String, String>? = mView?.getParams(url_type, load_type, bundle1)

        if (params == null) {
            params = HashMap()
        }
        //

        addDisposable(OkGo.post<String>(url)
                .params(params)
                .converter(StringConvert())
                .adapt<Observable<Response<String>>>(ObservableResponse())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe({ disposable ->
                    mView?.showLoadingUI(url_type, load_type)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { t: Response<String> ->
                            try {
                                val jsonObject = JSONObject(t.body())
                                if (t.code() >= 400 || jsonObject.length() <= 0) {
                                    val error = "服务器错误" + t.code()
                                    showError(mView?.getContext(), error)
                                    mView?.hideLoadingUI(url_type, load_type, false)
                                    mView?.onError(url_type, load_type, error)
                                } else {
                                    if (mView is BaseFragment<*, *> || mView?.getContext() != null && !mView?.getContext()!!.isFinishing) {
                                        mView?.hideLoadingUI(url_type, load_type, true)
                                        onSuccessData(jsonObject, url_type, load_type, bundle1)
                                        //错误码处�?
                                    }
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                val error = ExceptionHelper.handleException(e)
                                showError(mView?.getContext(), error)
                            }
                        }, { t: Throwable ->
                    val error = ExceptionHelper.handleException(t)
                    showError(mView?.getContext(), error)
                    mView?.hideLoadingUI(url_type, load_type, false)
                    mView?.onError(url_type, load_type, error)

                }, {
                    mView?.hideLoadingUI(url_type, load_type, false)
                }
                )
        )

    }


    protected abstract fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?)


    protected fun showError(context: Context?, error: String) {
        if (isShowError && context != null) {
            toast(error)
        }
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
        CommonUtils.toast(mView?.getContext()!!, text)
    }

    /**
     * log 输出
     */
    fun log(msg: String, vararg tags: String) {
        CommonUtils.log(msg, TAG)
    }

}
