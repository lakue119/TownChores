package com.dn.nhc.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object BitmapUtil {


    fun getBitmap(url: String): Bitmap? {
        var imgUrl: URL? = null
        var connection: HttpURLConnection? = null
        var `is`: InputStream? = null
        var retBitmap: Bitmap? = null
        try {
            imgUrl = URL(url)
            connection = imgUrl.openConnection() as HttpURLConnection
            connection.doInput = true //url로 input받는 flag 허용
            connection!!.connect() //연결
            `is` = connection.inputStream // get inputstream
            retBitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            connection?.disconnect()
            return retBitmap
        }
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }

    fun downloadImage(url: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var stream: InputStream? = null
        val bmOptions = BitmapFactory.Options()
        bmOptions.inSampleSize = 1
        try {
            stream = getHttpConnection(url)
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions)
            stream!!.close()
        } catch (e1: IOException) {
            e1.printStackTrace()
            println("downloadImage$e1")
        }
        return bitmap
    }

    // Makes HttpURLConnection and returns InputStream
    @Throws(IOException::class)
    fun getHttpConnection(urlString: String?): InputStream? {
        var stream: InputStream? = null
        val url = URL(urlString)
        val connection = url.openConnection()
        try {
            val httpConnection = connection as HttpURLConnection
            httpConnection.requestMethod = "GET"
            httpConnection.connect()
            if (httpConnection.responseCode == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.inputStream
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("downloadImage$ex")
        }
        return stream
    }
}