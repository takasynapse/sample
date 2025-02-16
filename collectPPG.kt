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
            PpgSensorApp(sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager)
        }
    }
}

@Composable
fun PpgSensorApp(sensorManager: SensorManager) {
    val ppgValue = remember { mutableStateOf(0f) }

    val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    ppgValue.value = it.values[0]
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    val ppgSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    lifecycleScope.launchWhenStarted {
        sensorManager.registerListener(sensorEventListener, ppgSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "PPG Sensor Data", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = "PPG: ${ppgValue.value}", fontSize = 20.sp)
    }
}
