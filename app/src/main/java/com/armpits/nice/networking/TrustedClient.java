package com.armpits.nice.networking;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class TrustedClient {

    private final SSLContext trustAllSslContext;
    private final SSLSocketFactory trustAllSslSocketFactory;

    TrustedClient() {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    OkHttpClient createTrustedClient() {
        OkHttpClient clinet = new OkHttpClient();
        OkHttpClient.Builder builder = clinet.newBuilder()
                .cookieJar(new CookieJar() {
                    List<Cookie> list;
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        this.list = list;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        if (list != null)
                            return  list;
                        return new ArrayList<>();
                    }
                })
                .followRedirects(false)
                .followSslRedirects(false);
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }
}
