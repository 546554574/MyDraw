package com.toune.myapp.ui.activity

import android.os.Bundle
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.toune.myapp.R
import com.toune.myapp.base.BaseActivity
import com.toune.myapp.ui.presenter.WebPresenter
import com.toune.myapp.ui.view.WebView
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity<WebView, WebPresenter>(), WebView {
    private var mAgentWeb: AgentWeb? = null

    override fun initPresenter(): WebPresenter {
        return WebPresenter()
    }

    override fun loadMore() {

    }

    override fun refreData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(root_layout!!, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go("https://github.com/Justson/AgentWeb")
    }

    override fun getLayout(): Int {
        return R.layout.activity_web
    }

    override fun getTitleStr(): String {
        return ""
    }

    override fun initEventAndData() {

    }
}
