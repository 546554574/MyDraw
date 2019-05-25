package com.toune.myapp.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.toune.myapp.R
import com.toune.myapp.ui.activity.MainActivity
import com.toune.util.rxtool.RxActivityTool
import com.toune.util.rxtool.RxPermissionsTool
import com.toune.util.rxtool.view.RxToast
import com.toune.util.rxview.progressing.style.ThreeBounce
import com.zhy.changeskin.SkinManager
import kotlinx.android.synthetic.main.activity_basic.*
import kotlinx.android.synthetic.main.layout_loading.*

/**
 * Created by wangjitao on 2016/11/8 0008.
 * 基类Activity的封装
 * 一般使用mvp模式的话会在BaseActivity中进行P和V的初始化绑定
 */
abstract class BaseActivity<V, T : BasePresenterImpl<V>> : AppCompatActivity(), BaseView {
    protected var mPresenter: T? = null
    var context: Context? = null
    /**
     * 传来的initViewID对应的View
     */
    var viewActivity: View? = null
    private var exitTime: Long = 0

    /**
     * 判断是否是主页面
     *
     * @return
     */
    private//                | getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())
    val isLoginOut: Boolean
        get() = javaClass.simpleName == MainActivity::class.java.simpleName

    fun showProDialog() {
        try {
            loading_layout!!.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun hideProDialog() {
        try {
            loading_layout!!.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        RxActivityTool.addActivity(this)
        SkinManager.getInstance().register(this)
        val view = View.inflate(context, R.layout.activity_basic, null)
        setContentView(view)
        /**
         * 设置loading样式
         */
        setLoadingStyle()
        this.viewActivity = View.inflate(context, getLayout(), null)
        val layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        basic_view.addView(viewActivity, layoutParams)
        //initPresenter()是抽象方法，让view初始化自己的presenter
        mPresenter = initPresenter()
        //presenter和view的绑定
        if (mPresenter != null) {
            mPresenter!!.attachView(this as V)
        }
        //标题  ：布局文件中引入lyout_title
        if (!TextUtils.isEmpty(getTitleStr())) {
            if (left_ll != null) {
                left_ll!!.setOnClickListener { onTopLeftListener() }
            }
            if (right_tv != null) {
                right_tv!!.setOnClickListener { onTopRightTvListener() }
            }
            if (right_iv != null) {
                right_iv!!.setOnClickListener { onTopRightIvListener() }
            }
            setToolBar(getTitleStr())
        } else {
            toolbar.visibility = View.GONE
        }
        RxPermissionsTool.with(this)
            .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .initPermission()
        //butter绑定
        init(savedInstanceState)
        //返回的不是null和空则是为有标题，标题的为返回值
        initEventAndData()
    }

    private fun setLoadingStyle() {
        if (progress != null) {
            val color = android.graphics.Color.parseColor("#89CFF0")
            val chasingDots = ThreeBounce()
            chasingDots.color = color
            progress!!.indeterminateDrawable = chasingDots
        }
    }

    abstract fun getLayout(): Int
    fun onTopRightTvListener() {}

    fun onTopRightIvListener() {}

    fun onTopLeftListener() {
        onBackIv()
    }

    // 实例化presenter
    abstract fun initPresenter(): T

    override fun onStart() {
        super.onStart()
    }

    abstract fun loadMore()

    abstract fun refreData()

    /**
     * 返回按钮，可不实现
     */
    fun onBackIv() {
        RxActivityTool.finishActivity()
    }

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter!!.detachView()
        }
        SkinManager.getInstance().unregister(this)
        super.onDestroy()
    }

    fun setToolBar(title: String) {
        title_tv.text = title
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun getTitleStr(): String

    abstract fun initEventAndData()


    override fun showLoading() {
        showProDialog()
    }

    override fun hideLoading() {
        hideProDialog()
    }

    override fun onBackPressed() {
        if (isLoginOut) {
            //连续按2次返回键退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                RxToast.showToast("再按一次退出")
                exitTime = System.currentTimeMillis()
            } else {
                RxActivityTool.AppExit(context)
            }
        } else {
            onBackIv()
        }
    }

    companion object {
        val TAG = BaseActivity!!.javaClass.simpleName
    }

}
