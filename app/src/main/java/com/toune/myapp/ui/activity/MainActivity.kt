package com.toune.myapp.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.blankj.rxbus.RxBus
import com.toune.myapp.R
import com.toune.myapp.base.BaseActivity
import com.toune.myapp.base.MyUtil
import com.toune.myapp.ui.model.TestVo
import com.toune.myapp.ui.presenter.MainActivityPresenter
import com.toune.myapp.ui.view.MainView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.nineoldandroids.view.ViewHelper
import com.toune.myapp.api.DownLoadFileUtils
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.toune.myapp.widget.PathEvaluator
import com.toune.myapp.widget.AnimatorPath
import com.toune.myapp.widget.PathPoint
import com.toune.util.rxtool.RxPhotoTool
import com.toune.util.rxtool.RxSPTool
import com.toune.util.rxview.dialog.RxDialogChooseImage
import com.toune.util.rxview.dialog.RxDialogEditSureCancel
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity<MainView, MainActivityPresenter>(), MainView {

    var mScaleGestureDetector: ScaleGestureDetector? = null
    override fun initPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun loadMore() {

    }

    override fun refreData() {
//        mPresenter!!.getVideoList()
    }

    override fun init(savedInstanceState: Bundle?) {
//        DownLoadFileUtils.downloadFile(context!!,"https://www.pgyer.com/apiv2/app/install?appKey=dcd5840676da5a8019d7e51618295cf8&_api_key=b0910ebfabd346fe332f7f8a8ef69f9b",MyUtil.basePath,"ksgj")
        reLineBtn.setOnClickListener {
            //            drawView.path
            startAnimatorPath(fab, "fab", drawView.animotorPath)
        }
        clearBtn.setOnClickListener {
            drawView.clearView()
        }
        changePhoteBtn.setOnClickListener {
            var rxDialogChooseImage: RxDialogChooseImage = RxDialogChooseImage(this)
            rxDialogChooseImage.show()
        }
        changeTextBtn.setOnClickListener {
            //提示弹窗
            val rxDialogEditSureCancel = RxDialogEditSureCancel(this)
            rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.logo)
            rxDialogEditSureCancel.getSureView()
                .setOnClickListener(View.OnClickListener {
                    textTv.text = rxDialogEditSureCancel.editText.text.toString()
                    rxDialogEditSureCancel.cancel()
                })
            rxDialogEditSureCancel.getCancelView()
                .setOnClickListener(View.OnClickListener { rxDialogEditSureCancel.cancel() })
            rxDialogEditSureCancel.show()
        }
    }

    /**
     * 设置动画
     * @param view
     * @param propertyName
     * @param path
     */
    private fun startAnimatorPath(view: View, propertyName: String, path: AnimatorPath) {
        val toTypedArray = path.points.toTypedArray()
        if (toTypedArray.size<=0){
            return
        }
        val anim = ObjectAnimator.ofObject(this, propertyName, PathEvaluator(), *path.points.toTypedArray())
        anim.setInterpolator(DecelerateInterpolator())//动画插值器
        anim.setDuration(toTypedArray.size * 300L)
        anim.start()
    }

    /**
     * 设置View的属性通过ObjectAnimator.ofObject()的反射机制来调用
     * @param newLoc
     */
    fun setFab(newLoc: PathPoint) {
        fab.setTranslationX(newLoc.mX)
        fab.setTranslationY(newLoc.mY)
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun getTitleStr(): String {
        return "Main"
    }

    override fun initEventAndData() {}

    override fun onDestroy() {
        super.onDestroy()
        RxBus.getDefault().unregister(this)
    }

    override fun getVideoList(testVo: TestVo) {
        //        Glide.with(context).load(testVo.getData().get(0).getVideos().getCover()).into(imgIv);
    }

    fun onViewClicked() {
//        RxActivityTool.skipActivity(context, WebActivity::class.java)
//        SkinManager.getInstance().changeSkin("night")
        MyUtil.toAliPayScan(this)
    }

    private var resultUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RxPhotoTool.GET_IMAGE_FROM_PHONE//选择相册之后的处理
            -> if (resultCode == RESULT_OK) {
                //                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                initUCrop(data!!.data)
            }
            RxPhotoTool.GET_IMAGE_BY_CAMERA//选择照相机之后的处理
            -> if (resultCode == RESULT_OK) {
                /* data.getExtras().get("data");*/
                //                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                initUCrop(RxPhotoTool.imageUriFromCamera)
            }
            RxPhotoTool.CROP_IMAGE//普通裁剪后的处理
            -> {
                val options = RequestOptions()
                    .placeholder(R.mipmap.d11)
                    //异常占位图(当加载异常的时候出现的图片)
                    .error(R.mipmap.d11)
                    //禁止Glide硬盘缓存缓存
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

                Glide.with(this).load(RxPhotoTool.cropImageUri).apply(options).thumbnail(0.5f).into(imgIv)
            }

            UCrop.REQUEST_CROP//UCrop裁剪之后的处理
            -> if (resultCode == RESULT_OK) {
                resultUri = UCrop.getOutput(data!!)
                roadImageView(resultUri!!, imgIv)
                RxSPTool.putContent(context, "AVATAR", resultUri.toString())
            } else if (resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(data!!)
            }
            UCrop.RESULT_ERROR//UCrop裁剪错误之后的处理
            -> {
                val cropError = UCrop.getError(data!!)
            }
            else -> {
            }
        }//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(context, RxPhotoTool.cropImageUri)));
        super.onActivityResult(requestCode, resultCode, data)
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private fun roadImageView(uri: Uri, imageView: ImageView): File {
        val options = RequestOptions()
            .placeholder(R.mipmap.d11)
            //异常占位图(当加载异常的时候出现的图片)
            .error(R.mipmap.d11)
            .transform(CircleCrop())
            //禁止Glide硬盘缓存缓存
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(this).load(uri).apply(options).thumbnail(0.5f).into(imageView)

        return File(RxPhotoTool.getImageAbsolutePath(this, uri))
    }

    private fun initUCrop(uri: Uri?) {
        val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val time = System.currentTimeMillis()
        val imageName = timeFormatter.format(Date(time))

        val destinationUri = Uri.fromFile(File(cacheDir, "$imageName.jpeg"))

        val options = UCrop.Options()
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary))
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark))

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5f)
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666)
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri!!, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .withOptions(options)
            .start(this)
    }

}
