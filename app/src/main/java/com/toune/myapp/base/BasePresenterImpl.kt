package com.toune.myapp.base

import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 * Created by Administrator on 2018/4/26.
 */

open class BasePresenterImpl<V> {
    var mViewRef: Reference<V>? = null

    val view: V? = null

    val isViewAttached: Boolean
        get() = mViewRef != null && mViewRef!!.get() != null


    fun attachView(view: V) {
        mViewRef = WeakReference(view)
    }

    fun detachView() {
        if (mViewRef != null) {
            mViewRef!!.clear()
            mViewRef = null
        }
    }

}
