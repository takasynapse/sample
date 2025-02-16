import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccelerometerApp(sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager)
        }
    }
}

@Composable
fun AccelerometerApp(sensorManager: SensorManager) {
    val ax = remember { mutableStateOf(0f) }
    val ay = remember { mutableStateOf(0f) }
    val az = remember { mutableStateOf(0f) }

    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    ax.value = it.values[0]
                    ay.value = it.values[1]
                    az.value = it.values[2]
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    lifecycleScope.launchWhenStarted {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Accelerometer Data", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "X: ${ax.value}", fontSize = 20.sp)
        Text(text = "Y: ${ay.value}", fontSize = 20.sp)
        Text(text = "Z: ${az.value}", fontSize = 20.sp)
    }
}
