/**
 * A simple implementation which adds noise to the location component of the
 * data that is produced by the "DemoLocation" sensing task.
 */

import java.util.*;
import java.io.*;

import android.content.Context;
import android.util.*;

@SuppressWarnings("unchecked")
public class DemoFilterLocation {

  private static Context context;
  private static int pref;
  private static List <Object> state;

  public void onStart (Context c, int i, ObjectInputStream ois) throws Exception {
    String TAG =  getClass().getName() + "@onStart: ";

    Log.wtf(TAG, "...");

    // Get application context.
    context = c;

    // Get privacy preferences.
    pref = i;

    // Restore state.
    if (ois != null) {
      Log.wtf(TAG, "Restoring state...");
      state = (List<Object>) ois.readObject();
    } else {
      Log.wtf(TAG, "Initializing state...");
      state = new ArrayList<>();
    }

  }

  public void onStop () {
    String TAG =  getClass().getName() + "@onStop: ";

    Log.wtf(TAG, "...");

  }

  public void onPreferenceChanged (int i) {
    String TAG =  getClass().getName() + "@onPreferenceChanged: ";

    Log.wtf(TAG, "" + i);

    pref = i;

  }

  public boolean saveState (ObjectOutputStream oos) throws Exception {
    String TAG =  getClass().getName() + "@saveState: ";

    Log.wtf(TAG, "...");

    // s.writeObject(state);

    return false;

  }

  public int processData (ObjectInputStream ois, ObjectOutputStream oos) throws Exception {
    String TAG =  getClass().getName() + "@processData: ";

    Log.wtf(TAG, "...");

    // Get list of data.
    List <Object> list = (List<Object>) ois.readObject();

    // Edit each data entry of the data list.
    for (Object o : list) {

      Map data = (Map) o;

      data.put("device", data.get("device"));
      data.put("task", data.get("task"));
      data.put("sensor", data.get("sensor"));
      data.put("timestamp", data.get("timestamp"));

      double[] location = (double[]) data.get("values");

      double x0 = location[0];   // lat
      double y0 = location[1];   // lng

      double r = (double) pref * 10 / 111300;

      double w = r * Math.sqrt(Math.random());
      double t = 2 * Math.PI * Math.random();
      double x = w * Math.cos(t);
      double y = w * Math.sin(t);

      double x1 = x / Math.cos(y0);

      double values[] = {x1 + x0, y + y0};

      data.put("values", values);
    }

    oos.writeObject(list);

    return 0;

  }

}
