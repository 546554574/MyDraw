package com.toune.myapp.api

import com.lzy.okgo.OkGo
import com.lzy.okrx2.adapter.ObservableBody
import com.toune.myapp.helper.JsonConvert
import com.toune.myapp.helper.ResponseData
import com.toune.myapp.ui.model.TestVo
import com.toune.myapp.ui.model.UpdateAppVo
import io.reactivex.Observable

object ServiceManager {
    val Token = ""
    var BaseUrl = "http://pay-forum.zydl-tec.cn"
    var UpdateApp = "$BaseUrl/v1/app/my/check_version" //更新APP
    val TestUrl = "$BaseUrl/v1/app/videos/list"

    fun getVideo(): Observable<ResponseData<TestVo>> {
        return OkGo.get<ResponseData<TestVo>>(TestUrl)
            .converter(
                object : JsonConvert<ResponseData<TestVo>>() {

                })
            .adapt(ObservableBody())
    }

    fun updateApp(): Observable<ResponseData<UpdateAppVo>> {
        return OkGo.get<ResponseData<UpdateAppVo>>(UpdateApp)
            .params("client", "android")
            .converter(JsonConvert())
            .adapt(ObservableBody())
    }
}
