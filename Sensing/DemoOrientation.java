/**
 * A simple sensing task that identifies the orientation of the device's screen
 * and reports the accelerometer's values whenever the screen faces:
 * - TOP
 * - BOTTOM
 * - LEFT
 * - RIGHT
 */

import java.util.*;
import java.io.*;

import android.content.Context;
import android.hardware.*;
import android.util.*;

public class DemoOrientation implements SensorEventListener {

	private static Context context;

	private static int state;

	List<Object> data;

	private static SensorManager sensorManager;

	// Save the last measurement outcome (do not report multiple TOPs).
	private static String face;

	public void onStart(Context c, ObjectInputStream s) throws Exception {
		String TAG =  getClass().getName() + "@onStart: ";

		Log.wtf(TAG, "...");

		context = c;

		// Save a counter to debug the overall functionality of the framework.
		if (s != null) {
			state = (Integer) s.readObject();
		} else {
			state = 1;
		}

		data = new ArrayList<>();

		// Monitor the accelerometer of the device.
		sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

	}

	public boolean saveState(ObjectOutputStream s) throws Exception {
		String TAG =  getClass().getName() + "@saveState: ";

		Log.wtf(TAG, "...");

		s.writeObject(state);

		return true;

	}

	public List<Object> getData() {
		String TAG =  getClass().getName() + "@getData: ";

		Log.wtf(TAG, "...");

		List<Object> l = data;
		data = new ArrayList<>();

		return l;

	}

	public void onStop() {
		String TAG =  getClass().getName() + "@onStop: ";

		Log.wtf(TAG, "...");

		sensorManager.unregisterListener(this);

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	// Called when sensor values have changed.
	@Override
	public void onSensorChanged(SensorEvent event) {
		String TAG =  getClass().getName() + "@onSensorChanged: ";

		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			Map<String, Object> newData = new HashMap<>();

			double[] values = new double[3];
			values[0] = (double) event.values[0];
			values[1] = (double) event.values[1];
			values[2] = (double) event.values[2];

			newData.put("sensor", event.sensor.getType());
			// newData.put("timestamp", event.timestamp);
			newData.put("timestamp", System.currentTimeMillis() * 1000000L);
			newData.put("values", values);

			// LEFT
			if(x > 9 && y > 0 && y < 1 && z > 0 && z < 1) {

				if(!"LEFT".equals(face)) {
					Log.wtf(TAG, Integer.toString(state+1) + ". LEFT");

					data.add(newData);

					state++;

				}

				face = "LEFT";

			// RIGHT
			} else if(x < -9 && y > 0 && y < 1 && z > 0 && z < 1) {

				if(!"RIGHT".equals(face)) {
					Log.wtf(TAG, Integer.toString(state+1) + ". RIGHT");

					data.add(newData);

					state++;

				}

				face = "RIGHT";

			// UP
			} else if(z > 9.5) {

				if(!"UP".equals(face)) {
					Log.wtf(TAG, Integer.toString(state+1) + ". UP");

					data.add(newData);

					state++;
				}

				face = "UP";

			// DOWN
			} else if(z < -9.5) {

				if(!"DOWN".equals(face)) {
					Log.wtf(TAG, Integer.toString(state+1) + ". DOWN");

					data.add(newData);

					state++;
				}

				face = "DOWN";

			}
		}
	}
}
