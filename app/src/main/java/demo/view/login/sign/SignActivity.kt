package demo.view.login.sign

import android.os.Bundle

import demo.Contents
import demo.R
import demo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sign.*

/**
 * @author yyx
 */

class SignActivity : BaseActivity<SignContract.View, SignPresenter>(), SignContract.View{

    override var mPresenter: SignPresenter = SignPresenter()


    override fun layoutResID(): Int  = R.layout.activity_sign

    override fun initView() {
        btn_okgo.setOnClickListener {
                mPresenter.loadRepositories()
        }
    }

    override fun initData() {
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
        log("msg" + msg)
    }


    override fun getUrl(url_type: Int): String = Contents.FACE

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        var params = mutableMapOf<String , String>()
        params.put("name","吴思宇")
        params.put("uid","52270119940871015")
        return params
    }
}
