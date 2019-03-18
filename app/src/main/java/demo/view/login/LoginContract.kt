package demo.view.login

import demo.base.IBasePresenter
import demo.base.IBaseView

/**
 * @author yyx
 */

object LoginContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
