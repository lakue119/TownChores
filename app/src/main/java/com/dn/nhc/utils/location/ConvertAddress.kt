package com.dn.nhc.utils.location

import android.location.Address
import android.location.Geocoder
import com.dn.nhc.R
import com.dn.nhc.utils.BaseUtils.context
import java.io.IOException
import java.lang.StringBuilder

object ConvertAddress {
    fun getCurrentAddress(latitude: Double?, longitude: Double?): String {
        if(latitude == null || longitude == null){
            return ""
        }
        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(context, context.resources.configuration.locale)
        val addresses: List<Address>?
        addresses = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            return context.getString(R.string.geocoder_network_error)
        } catch (illegalArgumentException: IllegalArgumentException) {
            return context.getString(R.string.geocoder_gps_error)
        }
        if (addresses == null || addresses.isEmpty()) {
            return context.getString(R.string.geocoder_missing_gps)
        }
        val address: Address = addresses[0]
//        val cityAddress = address.getAddressLine(0)
        val splitAddress:List<String> = address.getAddressLine(0).split(" ")
        val cityAddress = StringBuilder("")
        for(i in splitAddress.indices){
            when {
                i == 0 -> { }
                i >= splitAddress.size-1 -> {
                    cityAddress.append("${splitAddress[i]}")
                }
                else -> {
                    cityAddress.append("${splitAddress[i]} ")
                }
            }
        }
        if(address.subLocality == null){
            return address.thoroughfare
        }
        if(address.thoroughfare == null){
            address.subLocality
        }
        return "${address.subLocality} ${address.thoroughfare}" //cityAddress.toString()
    }
}