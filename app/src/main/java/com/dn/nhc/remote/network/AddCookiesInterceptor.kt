package com.dn.nhc.remote.network

import com.dn.nhc.utils.UserManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.Throws
import java.io.IOException


class AddCookiesInterceptor(private val userManager: UserManager) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        // Preference에서 cookies를 가져오는 작업을 수행
        val cookie: String = userManager.cookie

        builder.addHeader("Cookie", cookie)

        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")
        return chain.proceed(builder.build())
    }
}