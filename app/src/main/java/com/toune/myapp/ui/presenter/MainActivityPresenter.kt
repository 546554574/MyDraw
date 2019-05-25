package com.toune.myapp.ui.presenter

import android.annotation.SuppressLint
import com.toune.myapp.api.ServiceManager
import com.toune.myapp.base.BasePresenterImpl
import com.toune.myapp.helper.rxjavahelper.RxObserver
import com.toune.myapp.helper.rxjavahelper.RxResultHelper
import com.toune.myapp.helper.rxjavahelper.RxSchedulersHelper
import com.toune.myapp.ui.model.TestVo
import com.toune.myapp.ui.view.MainView
import com.toune.util.rxtool.view.RxToast
import io.reactivex.disposables.Disposable

class MainActivityPresenter : BasePresenterImpl<MainView>() {

    fun getVideoList() {
        ServiceManager.getVideo()
            .compose(RxSchedulersHelper.io_main())
            .compose(RxResultHelper.handleResult())
            .subscribe(object : RxObserver<TestVo>() {
                override fun onSubscribeMy(d: Disposable) {
                    view!!.showLoading()
                }

                override fun onErrorMy(errorMessage: String) {
                    RxToast.error(errorMessage)
                }

                override fun onNextMy(t: TestVo) {
                    view!!.getVideoList(t)
                }

                override fun onCompleteMy() {
                    view!!.hideLoading()
                }
            })
    }
}
