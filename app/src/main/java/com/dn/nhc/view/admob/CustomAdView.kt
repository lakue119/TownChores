package com.dn.nhc.view.admob

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.dn.nhc.R
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.kakao.adfit.ads.ba.BannerAdView
import java.lang.Exception


class CustomAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var view: View
    private var onAdViewState: AdViewState? = null

    private lateinit var adFit: BannerAdView
    private lateinit var adMob: com.google.android.gms.ads.AdView
    private lateinit var adFacebook: LinearLayout

    fun setAdViewStateListener(onAdViewState: AdViewState) {
        this.onAdViewState = onAdViewState
    }

    init {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val li = getContext().getSystemService(infService) as LayoutInflater
        view = li.inflate(R.layout.custom_adview, this, false)
        this.addView(view)

        adFit = view.findViewById(R.id.adFit)
        adMob = view.findViewById(R.id.adView)
        adFacebook = view.findViewById(R.id.facebookAd)

        showAdMob()
//        initView(attrs)
    }


    private fun initView(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAdView
        )
    }

    private fun showAdFit() {
        adFit.setClientId(context.getString(R.string.adfit_id))
        adFit.setAdListener(object :
            com.kakao.adfit.ads.AdListener {  // optional :: 광고 수신 리스너 설정

            override fun onAdLoaded() {
                // 배너 광고 노출 완료 시 호출
                Log.i("QWLKRJLQKWJRKL", "KAKAO ADFIT 호출완료")
                adFit.visibility = View.VISIBLE
                onAdViewState?.onAdLoaded()
            }

            override fun onAdFailed(errorCode: Int) {
                // 배너 광고 노출 실패 시 호출
                Log.i("릭", "ADFIT $errorCode false")
                onAdViewState?.onFailed(errorCode)
            }

            override fun onAdClicked() {
                // 배너 광고 클릭 시 호출
                Log.i("QWLKRJLQKWJRKL", "광고 클릭")
                onAdViewState?.onAdClicked()
            }

        })
        adFit.loadAd()
    }

    //
    private fun showAdMob() {
        MobileAds.initialize(context)
        val adRequest: AdRequest =
            AdRequest.Builder()
                .build()

        adMob.loadAd(adRequest)
        adMob.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.i("QWLKRJLQKWJRKL", "ADMOB Fail ${p0.responseInfo}")
//                showFacebookAd()
                showAdFit()
            }

            override fun onAdImpression() {
                onAdViewState?.onAdLeftApplication()
            }

            override fun onAdLoaded() {
                Log.i("QWLKRJLQKWJRKL", "ADMOB 호출완료")
                adMob.visibility = View.VISIBLE
                onAdViewState?.onAdLoaded()
            }

//            override fun onAdFailedToLoad(adError: Int) {
//                Log.i("QWLKRJLQKWJRKL", "Fail $adError")
//                showFacebookAd()
//            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                onAdViewState?.onAdOpened()
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                onAdViewState?.onAdClicked()
            }

//            override fun onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                onAdViewState?.onAdLeftApplication()
//            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                onAdViewState?.onAdClosed()
            }
        }
    }

    fun showFacebookAd() {
        val adView = AdView(
            context,
            context.getString(R.string.facebook_banner_id),
//            "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID",
//            866567340822256_885107028968287
            AdSize.BANNER_HEIGHT_50
        )

        adFacebook.visibility = View.VISIBLE
        adFacebook.addView(adView)

        val adListener: com.facebook.ads.AdListener = object : com.facebook.ads.AdListener {
            override fun onError(ad: Ad?, adError: AdError?) {
                Log.i("QWLKRJLQKWJRKL", "FACEBOOK Fail ${adError?.errorMessage}")
                adFacebook.visibility = View.GONE
                showAdFit()
            }

            override fun onAdLoaded(ad: Ad?) {
                Log.i("QWLKRJLQKWJRKL", "FACEBOOK AD 호출완료")
                adFacebook.visibility = View.VISIBLE
                onAdViewState?.onAdLoaded()
            }

            override fun onAdClicked(ad: Ad?) {

            }

            override fun onLoggingImpression(ad: Ad?) {

            }

        }

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
    }

    fun resume() {
        try {
            adFit.resume()
        } catch (e: Exception) {

        }
        try {
            adMob.resume()
        } catch (e: Exception) {

        }
    }
    fun destroy() {
        try {
            adFit.destroy()
        } catch (e: Exception) {

        }
        try {
            adMob.destroy()
        } catch (e: Exception) {

        }
    }

    fun pause() {
        try {
            adFit.pause()
        } catch (e: Exception) {

        }
        try {
            adMob.pause()
        } catch (e: Exception) {

        }
    }
}
