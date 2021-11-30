package com.dn.nhc.remote.network

import com.dn.nhc.utils.UserManager
import com.lakue.lakue_library.ext.logI
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedCookiesInterceptor(private val userManager: UserManager) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                logI("QWRKLJQWLKR",header)
                cookies.add(header)
                if(userManager.cookie.isEmpty()){
                    userManager.cookie = header
                }
            }
        }
        return originalResponse
    }
}