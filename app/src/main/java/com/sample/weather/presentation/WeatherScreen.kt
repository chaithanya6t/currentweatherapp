package com.sample.weather.presentation


import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.*
import com.sample.weather.R
import com.sample.weather.presentation.viewmodel.WeatherViewModel
import com.sample.weather.repository.WeatherInfo
import com.sample.weather.utils.LocationHelper

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val locationHelper = remember { LocationHelper(context) }

    val weatherInfo by viewModel.weatherInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    var hasRequested by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted && !hasRequested) {
            hasRequested = true
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            locationHelper.getLastKnownLocation(
                onLocationReceived = { lat, lon ->
                    viewModel.fetchWeatherByLocation(lat, lon)
                },
                onFailure = {
                    viewModel.setErrorMessage("Unable to fetch location")
                }
            )
        } else if (permissionState.status.shouldShowRationale.not()) {
            viewModel.setErrorMessage("Location permission denied")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            weatherInfo != null -> {
                WeatherCard(weatherInfo = weatherInfo!!)
            }

            else -> {
                Text("Fetching weather...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun WeatherCard(weatherInfo: WeatherInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Current Weather",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            WeatherRow(iconRes = R.drawable.thermostat, label = "Temperature", value = "${weatherInfo.temperature} °C")

            WeatherRow(iconRes = R.drawable.clearsky, label = "Condition", value = weatherInfo.condition)
            WeatherRow(iconRes = R.drawable.windy, label = "Wind Speed", value = "${weatherInfo.windSpeed} km/h")
            WeatherRow(iconRes = R.drawable.time, label = "Time", value = weatherInfo.time)
        }
    }
}

@Composable
fun WeatherRow(
    iconRes: Int,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
