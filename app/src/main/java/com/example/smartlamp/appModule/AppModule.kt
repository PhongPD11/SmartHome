package com.example.smartlamp.appModule

import android.content.Context
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.utils.Urls.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

//    @Provides
//    @Singleton
//    fun provideWeatherDatabase(@ApplicationContext appContext: Context): WeatherDatabase{
//        return Room.databaseBuilder(appContext, WeatherDatabase::class.java, "weather_database").build()
//    }
//
//    @Provides
//    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao{
//        return weatherDatabase.getWeatherDao()
//    }


}