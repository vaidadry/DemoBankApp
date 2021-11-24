package com.vaidadry.demobankapp.di

import android.content.Context
import android.content.SharedPreferences
import com.vaidadry.demobankapp.BuildConfig
import com.vaidadry.demobankapp.data.TransactionsApi
import com.vaidadry.demobankapp.repository.TransactionsRepository
import com.vaidadry.demobankapp.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(): TransactionsApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TransactionsApi::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: TransactionsApi, realm: Realm): MainRepository =
        TransactionsRepository(api, realm)

    @Singleton
    @Provides
    fun provideRealm(@ApplicationContext context: Context): Realm {
        Realm.init(context)
        val builder = RealmConfiguration.Builder()
            .name("user_transactions.realm")
            .schemaVersion(1)
            .build()
        return Realm.getInstance(builder)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("pin_code", Context.MODE_PRIVATE)
    }
}