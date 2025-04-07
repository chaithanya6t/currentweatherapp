package com.sample.weather.presentation.Weather

import android.content.Context
import com.sample.weather.domain.model.usecase.GetWeatherUseCase
import com.sample.weather.presentation.viewmodel.WeatherViewModel
import com.sample.weather.repository.WeatherInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private val context = mockk<Context>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        getWeatherUseCase = mockk()
        viewModel = WeatherViewModel(getWeatherUseCase, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch weather successfully updates state`() = runTest {
        val dummyWeather = WeatherInfo(25.0, 5.0, "Sunny", "2025-04-07T12:00")

        coEvery { getWeatherUseCase.invoke(any(), any()) } returns dummyWeather

        viewModel.fetchWeatherByLocation(28.6, 77.2)

        assertEquals(dummyWeather, viewModel.weatherInfo.value)
        assertEquals(null, viewModel.errorMessage.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `fetch weather with error updates error state`() = runTest {
        coEvery { getWeatherUseCase.invoke(any(), any()) } throws RuntimeException("API Error")

        viewModel.fetchWeatherByLocation(28.6, 77.2)

        assertEquals(null, viewModel.weatherInfo.value)
        assertEquals("API Error", viewModel.errorMessage.value)
        assertEquals(false, viewModel.isLoading.value)
    }
}



