package com.dn.nhc.utils.camera

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.dn.nhc.utils.BaseUtils.context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraUtil(val context: Context, val activity: Activity) {

    companion object{
        var cw = ContextWrapper(context)

        val REQUEST_CAMERA_PERMISSION = 1002
        val HOME_DIRECTORY = Environment.getExternalStorageDirectory().path
        val HOME_DIRECTORY_CACHE = cw.externalCacheDir?.path
        val NEW_HOME_DIRECTORY = cw.getExternalFilesDir(null)!!.path
        val NEW_HOME_DIRECTORY_CACHE = cw.externalCacheDir?.path
    }

    internal var tempFile: File? = null
//    lateinit var photoUri: Uri

    private var onShowCameraListener: OnShowCameraListener? = null
    private var onShowGalleryListener: OnShowGalleryListener? = null
    private var onCameraPermissionListener: OnCameraPermissionListener? = null

    fun setOnShowCameraListener(onShowCameraListener: OnShowCameraListener){
        this.onShowCameraListener = onShowCameraListener
    }

    fun setOnShowGalleryListener(onShowGalleryListener: OnShowGalleryListener){
        this.onShowGalleryListener = onShowGalleryListener
    }

    fun setOnCameraPermissionListener(onCameraPermissionListener: OnCameraPermissionListener){
        this.onCameraPermissionListener = onCameraPermissionListener
    }

    fun onShowCamera() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            XXPermissions.with(context)
                    .permission(READ_EXTERNAL_STORAGE)
                    .permission(CAMERA)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            showCamera()
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            super.onDenied(permissions, never)

                        }
                    })
        } else {
            XXPermissions.with(context)
                    .permission(READ_EXTERNAL_STORAGE)
                    .permission(WRITE_EXTERNAL_STORAGE)
                    .permission(CAMERA)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            showCamera()
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            super.onDenied(permissions, never)
                        }
                    })
        }
    }

    fun showCamera(){
        try {
            tempFile = createImageFile()
        } catch (e: IOException) {
            Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        if (tempFile != null) {
            val photoUri =  FileProvider.getUriForFile(context, context.packageName, tempFile!!)
            onShowCameraListener?.onShowCamera(photoUri)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        // 이미지 파일 이름 ( blackJin_{시간}_ )
        val timeStamp: String = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "neighborhoodchores" + timeStamp + "_"
        // 이미지가 저장될 폴더 이름 ( blackJin )
        var storageDir: File = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            File(NEW_HOME_DIRECTORY)
        } else {
            File("$HOME_DIRECTORY/neighborhoodchores/")
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        // 빈 파일 생성
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    fun onShowCameraAlbum() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            XXPermissions.with(context)
                    .permission(READ_EXTERNAL_STORAGE)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            showCameraAlbum()
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            super.onDenied(permissions, never)

                        }
                    })
        } else {
            XXPermissions.with(context)
                    .permission(READ_EXTERNAL_STORAGE)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            showCameraAlbum()
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            super.onDenied(permissions, never)

                        }
                    })
        }
    }

    fun showCameraAlbum(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        onShowGalleryListener?.onShowGallery(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun rotateBitmap(uri: Uri): Bitmap {

        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)//BitmapFactory.decodeFile(imageFilePath)
        var exif: ExifInterface? = null
        try {
            exif = context.contentResolver.openInputStream(uri)?.let { ExifInterface(it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val exifOrientation: Int
        val exifDegree: Int
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            exifDegree = exifOrientationToDegrees(exifOrientation)
        } else {
            exifDegree = 0
        }
        val rotatebitmap = rotate(bitmap, exifDegree.toFloat())
        val stream = ByteArrayOutputStream()
        rotatebitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return rotatebitmap
    }

    //이미지 회전 각도 구하기
    open fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                90
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                180
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                270
            }
            else -> 0
        }
    }

    //이미지 회전
    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}