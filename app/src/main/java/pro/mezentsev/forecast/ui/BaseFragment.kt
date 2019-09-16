package pro.mezentsev.forecast.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment<P : Contract.BasePresenter<out Contract.BaseView>> : Fragment() {
    lateinit var presenter : P
}