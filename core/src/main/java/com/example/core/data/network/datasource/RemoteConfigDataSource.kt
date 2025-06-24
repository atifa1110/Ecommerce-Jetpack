package com.example.core.data.network.datasource

import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.PaymentResponse
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigDataSource  @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    suspend fun getPaymentConfig(): EcommerceResponse<PaymentResponse> {
        return try {
            remoteConfig.fetchAndActivate().await()

            val json = remoteConfig.getString("payment_config")
            val response = Gson().fromJson(json, PaymentResponse::class.java)

            if(response.code == 200) {
                EcommerceResponse.success(response)
            }else{
                EcommerceResponse.failure(response.code,response.message)
            }
        } catch (e: Exception) {
            EcommerceResponse.failure(-1, e.message ?: "Unexpected error")
        }
    }
}
