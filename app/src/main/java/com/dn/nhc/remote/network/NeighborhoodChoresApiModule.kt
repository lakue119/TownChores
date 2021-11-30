package com.dn.nhc.remote.network

import android.app.Application
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.dn.nhc.BuildConfig
import com.dn.nhc.utils.UserManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NeighborhoodChoresApiModule {

    @Provides
    @Singleton
    fun provideClubHouseApi(retrofit: Retrofit): NeighborhoodChoresApi = retrofit.create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @RequiresApi(Build.VERSION_CODES.N)
    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application, userManager: UserManager): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor(userManager))
            .addInterceptor(ReceivedCookiesInterceptor(userManager))
            .addInterceptor {

                val original = it.request()

                val locales: LocaleList =
                    application.resources.configuration.locales
                val requestBuilder = original
                    .newBuilder()
                    .header("Accept", "application/json")
                if (userManager.getLoginCheck()) {
                    requestBuilder.header("authorization", "Bearer " + userManager.userToken)
//                        .header("CH-UserID", userManager.userId)
                }

                val newRequest = requestBuilder.build()
                it.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
}

