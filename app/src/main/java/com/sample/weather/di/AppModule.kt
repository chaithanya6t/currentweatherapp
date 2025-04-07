package com.sample.weather.di

import android.content.Context
import com.sample.weather.data.remote.api.WeatherApi
import com.sample.weather.domain.model.usecase.GetWeatherUseCase
import com.sample.weather.domain.model.usecase.WeatherRepositoryImpl
import com.sample.weather.domain.repository.WeatherRepository
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
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi =
         Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        api: WeatherApi,
        @ApplicationContext context: Context
    ): WeatherRepository = WeatherRepositoryImpl(api, context)

    @Provides
    @Singleton
    fun provideUseCase(
        repository: WeatherRepository
    ): GetWeatherUseCase = GetWeatherUseCase(repository)
}




