package demo.base

/**
 * @author yyx
 */

interface IBasePresenter<V : IBaseView> {
    fun subscribe()

    fun unsubscribe()

    fun attachView(view: V)

    fun detachView()

}
