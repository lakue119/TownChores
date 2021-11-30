package com.dn.nhc.utils.converter

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.dn.nhc.utils.BaseUtils.context
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException


object ConvertPhoto {
    val Tag = ConvertPhoto::class.java.name

    fun convertBitmapToUri(bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Photo", null)
        return Uri.parse(path)
    }

    fun convertUriToBitmap(uri: Uri): Bitmap?{
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return bitmap
    }

}