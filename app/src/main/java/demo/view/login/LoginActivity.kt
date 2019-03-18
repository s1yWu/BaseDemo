package demo.view.login

import demo.R
import demo.base.BaseActivity

/**
 * @author yyx
 */

class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View{

    override var mPresenter: LoginPresenter = LoginPresenter()


    override fun layoutResID(): Int  = R.layout.activity_login

    override fun initView() {
    }

    override fun initData() {
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
