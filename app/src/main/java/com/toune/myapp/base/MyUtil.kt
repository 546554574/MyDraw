package com.toune.myapp.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.toune.myapp.api.ServiceManager
import com.toune.myapp.helper.rxjavahelper.RxObserver
import com.toune.myapp.helper.rxjavahelper.RxResultHelper
import com.toune.myapp.helper.rxjavahelper.RxSchedulersHelper
import com.toune.myapp.ui.model.UpdateAppVo
import com.toune.util.rxtool.GsonBinder
import com.toune.util.rxtool.RxAppTool
import com.toune.util.rxtool.RxFileTool
import com.toune.util.rxtool.view.RxToast

object MyUtil {

    /**
     * 截图照片存放路径
     *
     * @return
     */
    val basePath: String
        get() = RxFileTool.getSDCardPath() + "myapp/app/"

    /**
     * 更新APP对话框等
     *
     * @param context
     * @param isShowToast
     */
    fun updateApp(context: Context, isShowToast: Boolean) {
        ServiceManager.updateApp()
            .compose(RxSchedulersHelper.io_main())
            .compose(RxResultHelper.handleResult())
            .subscribe(object : RxObserver<UpdateAppVo>() {
                override fun onNextMy(updateAppVo: UpdateAppVo) {
                    try {
                        val netCode = Integer.parseInt(updateAppVo!!.data!!.num!!.replace(".", ""))
                        val localCode = Integer.parseInt(RxAppTool.getAppVersionName(context).replace(".", ""))
                        if (netCode > localCode) {
                            //创建Intent
                            val intent = Intent(AppConstant.UPDATE_ACTION)
                            intent.component = ComponentName(
                                RxAppTool.getAppPackageName(context),
                                "com.zydl.pay.receiver.UpdateReceiver"
                            )
                            intent.putExtra("vo", GsonBinder.toJsonStr(updateAppVo))
                            //开启广播
                            context.sendBroadcast(intent)
                        } else {
                            if (isShowToast) {
                                RxToast.info("已经是最新版本")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onErrorMy(errorMessage: String) {

                }
            })
    }

    /**
     * 跳转到支付宝并打开百度
     */
    fun toAliPayScan(context: Context) {
        try {
            //利用Intent打开支付宝
            //支付宝跳过开启动画打开扫码和付款码的urlscheme分别是：
            //alipayqr://platformapi/startapp?saId=10000007
            //alipayqr://platformapi/startapp?saId=20000056
            val uri = Uri.parse("alipayqr://platformapi/startapp?appId=20000067&url=http://marketpay.ccb.com/online/mobile/paychoose.html?cmdtyOrdrNo=20191115105054&pyOrdrNo=105005373999480050817294739076&ordrEnqrFcnCd=1&MAC=6d5fb74375741dee2c459bd0ec4360da")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {

        }

    }
}
