/**
 * A simple sensing task that logs the location the device has remained at for
 * a defined period of time.
 */

import java.io.*;
import java.util.*;

import android.content.*;
import android.hardware.Sensor;
import android.location.*;
import android.os.*;
import android.util.*;

public class DemoLocation implements LocationListener  {

  Context context;

  List<Object> data;

  LocationManager locMgr;
  LocationListener locLnr;
  Location lastLoc;

  // Timer and total time definition.
  CountDownTimer timer;

  int hours 	= 0;
  int minutes = 0;
  int seconds = 5;

  // Convert the above to milliseconds.
  int timeTotal = (hours * 3600 + minutes * 60 + seconds) * 1000;

  public void onStart(Context c, ObjectInputStream s) throws Exception {
    String TAG =  getClass().getName() + "@onStart: ";

    Log.wtf(TAG, "...");

    context = c;

    data = new ArrayList<>();

    timer = new CountDownTimer(timeTotal, 1000) {

      public void onTick(long arg0) {
        String TAG =  getClass().getName() + "@onTick: ";
        // Log.wtf(TAG, arg0 / 1000 + " seconds remaining...");
      }

      // Log the current location.
      public void onFinish() {
        String TAG =  getClass().getName() + "@onFinish: ";

        Log.wtf(TAG, lastLoc.toString());

        // add location to data
        Map<String, Object> newData = new HashMap<>();

        double[] values = new double[2];
        values[0] = lastLoc.getLatitude();
        values[1] = lastLoc.getLongitude();

        newData.put("sensor", Sensor.TYPE_ALL);
        // newData.put("timestamp", lastLoc.getTime() * 1000000);
        newData.put("timestamp", System.currentTimeMillis() * 1000000L);
        newData.put("values", values);

        data.add(newData);

      }

    };

    // Monitor location changes.
    locMgr = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

    Criteria criteria = new Criteria();

    String provider = locMgr.getBestProvider(criteria, true);
    if(provider == null) {
      provider = locMgr.getBestProvider(criteria, false);
    }

    locMgr.requestLocationUpdates(provider, 1000, 1, this);

    Location loc = locMgr.getLastKnownLocation(provider);
    if (loc != null) {
      Log.wtf(TAG, provider + " provider selected.");
      onLocationChanged(loc);
    }
    else {
      Log.wtf(TAG, "Location not available.");
    }

  }

  public boolean saveState(ObjectOutputStream s) {
    String TAG =  getClass().getName() + "@saveState: ";

    Log.wtf(TAG, "...");

    return false;
  }

  public List<Object> getData() {
    String TAG =  getClass().getName() + "@getData: ";

    Log.wtf(TAG, "...");

    List<Object> tmp = data;
    data = new ArrayList<>();

    return tmp;
  }

  public void onStop() {
    String TAG =  getClass().getName() + "@onStop: ";

    Log.wtf(TAG, "...");

    // Stop the timer.
    if(timer != null) {
      timer.cancel();
    }

    //  Release the location listener.
    if(locMgr != null) {
      locMgr.removeUpdates(locLnr = this);
    }

  }
  /**/

  // Called when the location has changed.
  @Override
  public void onLocationChanged(Location loc) {
    String TAG =  getClass().getName() + "@onLocationChanged: ";

    Log.wtf(TAG, "...");

    Log.wtf(TAG, loc.toString());

    lastLoc = loc;

    if (timer != null) {
      timer.cancel();
    }
    timer.start();

  }

  @Override
  public void onProviderDisabled(String arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onProviderEnabled(String arg0) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    // TODO Auto-generated method stub
  }

}
