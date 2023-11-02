package com.example.smartlamp.appModule

import android.content.Context
import androidx.room.Room
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Urls.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideSharedPref(@ApplicationContext context: Context) = SharedPref(context)


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
//    fun provideUserDatabase(@ApplicationContext context: Context) : UserDatabase {
//        return Room.databaseBuilder(context, UserDatabase::class.java, "user_database").build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUserDao(userDatabase: UserDatabase) : UserDao {
//        return userDatabase.getUserDao()
//    }
//
//  @Provides
//    @Singleton
//    fun provideModeDatabase(@ApplicationContext context: Context) : ModeDatabase {
//        return Room.databaseBuilder(context, ModeDatabase::class.java, "mode_database").build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideModeDao(modeDatabase: ModeDatabase) : ModeDao {
//        return modeDatabase.getModeDao()
//    }

    @Provides
    @Singleton
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


}