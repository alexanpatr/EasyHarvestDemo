/**
 * A dummy sensing task that logs the values of the accelerometer sensor of the
 * device and reports the 100th value.
 */

import java.util.*;
import java.io.*;

import android.content.Context;
import android.hardware.*;
import android.util.*;

public class DemoTest implements SensorEventListener {

	private static Context context;

	private static int state;

	List<Object> data;

	private static SensorManager sensorManager;

	private static String face;

	public void onStart(Context c, ObjectInputStream s) throws Exception {
		String TAG =  getClass().getName() + "@onStart: ";

		Log.wtf(TAG, "...");

		context = c;

		if (s != null) {
			state = (Integer) s.readObject();
		} else {
			state = 1;
		}

		data = new ArrayList<>();

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

		// Log.wtf(TAG, "...");

		// We are interested only for the accelerometer values.
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			Map<String, Object> newData = new HashMap<>();

			double[] values = new double[3];
			values[0] = (double) event.values[0];
			values[1] = (double) event.values[1];
			values[2] = (double) event.values[2];

			// Log only the 100th measurement.
			if (state % 100 == 0) {
				Log.wtf(TAG, Integer.toString(state) + ". DATA");

				newData.put("sensor", event.sensor.getType());
				// newData.put("timestamp", event.timestamp);
				newData.put("timestamp", System.currentTimeMillis() * 1000000L);
				newData.put("values", values);

				data.add(newData);

			}

			state++;

		}

	}

}
