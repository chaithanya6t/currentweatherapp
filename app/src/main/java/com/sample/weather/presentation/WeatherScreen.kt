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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.*
import com.sample.weather.R
import com.sample.weather.presentation.state.WeatherState
import com.sample.weather.presentation.viewmodel.WeatherViewModel
import com.sample.weather.repository.WeatherInfo
import com.sample.weather.utils.LocationHelper

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val locationHelper = remember { LocationHelper(context) }

    val state by viewModel.weatherState.collectAsState()

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
                    viewModel.fetchWeatherByLocation(lat, lon, context.getString(R.string.something_wrong))
                },
                onFailure = {
                    viewModel.setError(context.getString(R.string.unable_to_fetch_location))
                }
            )
        } else if (permissionState.status.shouldShowRationale.not()) {
            viewModel.setError(context.getString(R.string.location_permission_denied))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (state){
            is WeatherState.Loading -> {
                CircularProgressIndicator()
            }

            is WeatherState.Error.Generic -> {
                val errorMessage = (state as WeatherState.Error.Generic).message
                Text(
                    text = stringResource(R.string.error_prefix)+errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is WeatherState.Success -> {
                val weatherInfo = (state as WeatherState.Success).data
                WeatherCard(weatherInfo = weatherInfo)
            }

            else -> {
                Text(context.getString(R.string.fetching_weather), style = MaterialTheme.typography.bodyMedium)
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
                text = stringResource(R.string.current_weather),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            WeatherRow(iconRes = R.drawable.thermostat, label = stringResource(R.string.temperature_label), value = "${weatherInfo.temperature} °C")
            WeatherRow(iconRes = R.drawable.clearsky, label = stringResource(R.string.condition) , value = weatherInfo.condition)
            WeatherRow(iconRes = R.drawable.windy, label = stringResource(R.string.wind_spped) , value = "${weatherInfo.windSpeed} km/h")
            WeatherRow(iconRes = R.drawable.time, label =  stringResource(R.string.time_label) , value = weatherInfo.time)
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
