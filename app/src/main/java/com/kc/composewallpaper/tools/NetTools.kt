package com.kc.composewallpaper.tools

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetTools {
    @Throws(java.lang.Exception::class)
    fun getOkHttpClient(): OkHttpClient {
        val trustManager: X509TrustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate?>?, authType: String?
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate?>?, authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls<X509Certificate>(0)
            }
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        return OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true }).build()
    }
}