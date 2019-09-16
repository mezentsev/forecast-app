package pro.mezentsev.forecast.ui

import androidx.annotation.CallSuper

interface Contract {
    interface BaseView

    abstract class BasePresenter<V: BaseView> {
        protected var view: V? = null

        @CallSuper
        open fun attach(v: V) {
            view = v
        }

        abstract fun onResume()

        abstract fun onPause()

        @CallSuper
        open fun detach() {
            view = null
        }
    }
}