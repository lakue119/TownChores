package com.dn.nhc.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.dn.nhc.utils.camera.CameraUtil.Companion.HOME_DIRECTORY_CACHE
import com.dn.nhc.utils.camera.CameraUtil.Companion.NEW_HOME_DIRECTORY_CACHE
import com.lakue.lakue_library.ext.logE
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageTypeBitmapConvertFile(var context: Context) {
    var image: String? = null

    fun getBitmap(image: String): File? {
        var imgFile: File? = null
        this.image = image
        try {
            //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
            val imageUri = Uri.parse("file://$image")
            //            Bitmap bitmap = ((BitmapDrawable) iv_edit.getDrawable()).getBitmap();
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

            //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.

            //MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            imgFile = File(image)
        } catch (e: Exception) {
            logE("ERROR", e.message.toString())
        }
        return imgFile
    }

    fun BitmapConvertFile(bitmap: Bitmap): File {
        // 파일 선언 -> 경로는 파라미터에서 받는다
        val timeStamp: String = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "neighborhood_chores" + timeStamp + "_"
        // 이미지가 저장될 폴더 이름 ( blackJin )
        val storageDir: File = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            File(NEW_HOME_DIRECTORY_CACHE)
        } else {
            File(HOME_DIRECTORY_CACHE)
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val file = File.createTempFile(imageFileName, ".jpg", storageDir)
        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도
        var out: OutputStream? = null
        try {
            // 파일 초기화
            file.createNewFile()
            // OutputStream에 출력될 Stream에 파일을 넣어준다
            out = FileOutputStream(file)
            // bitmap 압축
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }


//    fun imagePathToBitmap(image: String?): File? {
//        var imgFile: File? = null
//        val rotatebitmap: Bitmap?
//        this.image = image
//        try {
//            //            MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//            imgFile = File(image)
//            try {
//                imgFile.createNewFile()
//                rotatebitmap = getRotatebitmap(image)
//                val bos = ByteArrayOutputStream()
//                rotatebitmap!!.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
//                val bitmapdata = bos.toByteArray()
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        } catch (e: Exception) {
//            logE("ERROR", e.message.toString())
//        }
//        return imgFile
//    }

//    fun getRotatebitmap(image: String?): Bitmap? {
//        var rotatebitmap: Bitmap? = null
//        val bitmap = BitmapFactory.decodeFile(image)
//        var exif: ExifInterface? = null
//        try {
//            exif = ExifInterface(image!!)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val exifOrientation: Int
//        val exifDegree: Int
//        if (exif != null) {
//            exifOrientation = exif.getAttributeInt(
//                ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_NORMAL
//            )
//            exifDegree = exifOrientationToDegrees(exifOrientation)
//        } else {
//            exifDegree = 0
//        }
//        rotatebitmap = rotate(bitmap, exifDegree.toFloat())
//        val stream = ByteArrayOutputStream()
//        rotatebitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        //
//        val currentData = stream.toByteArray()
//        SaveImageTask().execute(currentData)
//        return rotatebitmap
//    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }
}