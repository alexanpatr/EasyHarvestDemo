/**
 * A simple implementation which adds noise to the time component of the
 * data that is produced by any sensing task.
 */

import java.util.*;
import java.io.*;

import android.content.Context;
import android.util.*;

@SuppressWarnings("unchecked")
public class DemoFilterTime {

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
      data.put("values", data.get("values"));

      float min = -1;
      float max = 1;

      Random r = new Random();
      float d = min + (max - min) * r.nextFloat();  // -1 < d < 1

      long var = (long) (((float) pref / 10) * d * 3600000000000L);   // (pref/10) * random * (1hr)

      data.put("timestamp", (Long) data.get("timestamp") + var);

    }

    oos.writeObject(list);

    return 0;

  }

}
