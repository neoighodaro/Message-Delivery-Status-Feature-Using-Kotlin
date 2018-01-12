package com.example.android.messagedeliverystatus


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitClient {

    companion object {
        fun getRetrofitClient(): ApiService {
            val httpClient = OkHttpClient.Builder()
            val builder = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:9000/")
                    .addConverterFactory(ScalarsConverterFactory.create())

            val retrofit = builder
                    .client(httpClient.build())
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}
