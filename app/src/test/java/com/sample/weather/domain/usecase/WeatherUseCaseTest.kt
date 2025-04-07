package com.sample.weather.domain.usecase


import com.sample.weather.domain.model.usecase.GetWeatherUseCase
import com.sample.weather.domain.repository.WeatherRepository
import com.sample.weather.repository.WeatherInfo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetWeatherUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private lateinit var useCase: GetWeatherUseCase

    @Before
    fun setup() {
        useCase = GetWeatherUseCase(repository)
    }

    @Test
    fun `invoke returns weather info`() = runTest {
        val dummyWeather = WeatherInfo(26.0, 6.0, "Clear", "2025-04-07T14:00")

        coEvery { repository.getWeatherByLocation(any(), any()) } returns dummyWeather

        val result = useCase(28.6, 77.2)

        assertEquals(dummyWeather, result)
    }
}



