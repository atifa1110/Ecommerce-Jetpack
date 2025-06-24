package com.example.ecommerceapp.di

import android.content.Context
import com.example.core.data.network.api.FirebaseApiService
import com.example.ecommerceapp.firebase.AnalyticsManager
import com.example.ecommerceapp.firebase.FirebaseAnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

//    @Provides
//    @Singleton
//    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
//
//    @Provides
//    @Singleton
//    fun provideFirebaseCloudMessagingApi(factory: GsonConverterFactory): FirebaseApiService =
//        Retrofit.Builder()
//            .baseUrl("https://fcm.googleapis.com/tokophincon-59381/")
//            .addConverterFactory(factory)
//            .build()
//            .create(FirebaseApiService::class.java)

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAnalyticsManager(firebaseAnalytics: FirebaseAnalytics): AnalyticsManager {
        return FirebaseAnalyticsManager(firebaseAnalytics)
    }

}