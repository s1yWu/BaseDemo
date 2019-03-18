package demo.view.login

import android.os.Bundle
import demo.base.BasePresenter
import org.json.JSONObject

/**
 * @author yyx
 */

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val msg = jsonObject.optString("msg")
        val status = jsonObject.optInt("status")
//        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
        mView?.onSuccessData(url_type,load_type,msg,status)
    }

    override fun loadRepositories() {
        reqData()
    }
}