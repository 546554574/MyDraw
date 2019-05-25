package com.toune.myapp.ui.view

import com.toune.myapp.base.BaseView
import com.toune.myapp.ui.model.TestVo

interface MainView : BaseView {
    fun getVideoList(testVo: TestVo)
}
