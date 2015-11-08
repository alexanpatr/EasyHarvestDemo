# EasyHarvest

EasyHarvest aims to simplify the deployment and controlled execution of large-scale sensing applications on smartphones. On the one hand, application owners submit to a server sensing tasks for distribution on smartphones, and collect the data produced by them in a simple manner. On the other hand, smartphone owners control the execution of sensing tasks on their devices through a single interface, without having to repeatedly download, install and configure individual sensing applications. The interaction between the smartphone and the server occurs in a transparent way, with tolerance to intermittent connectivity and support for disconnected operation.

*For more information read* [here](http://www.inf.uth.gr/wp-content/uploads/formidable/Katsomallos_Emmanouil.pdf) *and* [here](http://www.inf.uth.gr/wp-content/uploads/formidable/Katsomallos_Emmanouil1.pdf).

## Data Structure

Data recorded by Sensing Tasks (STs), processed by Privacy Mechanisms (PMs) and sent to the EasyHarvestServer (EHS) by the EasyHarvestClients (EHCs) should follow a specific structure shown in the table below to achieve proper communication and function of the framework entities. This way, we patronize the data collected without limiting application capabilities and Sensing Task developer freedom. For instance, the values field from an application that monitors location would typically contain [0] longitude, [1] latitude and optionally [2] altitude and [3] speed.

| Type          | Name          | Description   |
| ------------- | ------------- | ------------- |
| int           | device        | The device id assigned by the server during the client registration. |
| int           | task          | The identifier of the sensing task that produced this data. |
| int           | sensor        | The identifier of the sensor that generated this event. |
| long          | timestamp     | Date and time in nanoseconds when the event happened. |
| double[]      | values        | The length and contents of the values array depends on which sensor type is being monitored. |

## API

### Sensing Tasks

```java
void onStart (Context, int, ObjectInputStream)
```

Initialize the task state and install listeners for the sensors of the smartphone. If the task has previously saved its state, this can be retrieved via s (if not NULL).

* Parameters

  `Context`: The interface to global information about the EHC environment, required to gain access to key system resources.

  `ObjectInputStream`: The ST state defined by the ST. If it is the first time the ST starts or no state is saved, it is null.

* Returns

  None.

```java
void onStop ()
```

Release all the resources held by the tasks and unregister all listeners so that it can be gracefully stopped.

* Parameters

  None.

* Returns

  None.

```java
boolean saveState (ObjectOutputStream)
```

Save task state information that is required on a subsequent restart; the return value indicates whether any state was actually saved.

* Parameters

  `ObjectOutputStream`: A data stream for writing state information.

* Returns

  False/True indicating whether state was not/was saved.

```java
public List<Object> getData()
```

Return the data that was produced so far by the task; this will be eventually uploaded to the server behind the scenes and in a lazy and incremental way, subject to Internet connectivity.

* Parameters

  None.

* Returns

  `List<Object>`: A list of the produced data so far.


### Privacy Mechanisms

```java
void onStart (Context, int, ObjectInputStream)
```

Called once to initialize the PM state and install system listeners.
* Parameters

  `Context`: The interface to global information about the EHC environment, required to gain access to key system resources.

  `int`: The desired privacy level (0-100) defined by the device owner. 100 means that the user wants maximum privacy whereas, 0 means that she does not care at all.

  `ObjectInputStream`: The PM state defined by the PM. If it is the first time the PM starts or no state is saved, it is null.

* Returns

  None.

```java
void onStop ()
```

Releases all the resources held, unregisters all listeners and stops the PM execution.

* Parameters

  None.

* Returns

  None.

```java
void onPreferenceChanged (int)
```

Called by the EHC whenever the user changes the privacy preferences (currently privacy level), to update and adapt the PM’s functioning.

* Parameters

  `int`: The new privacy level [0, 100].

* Returns

  None.

```java
void onPeersChanged (List <Map <String, String>>)
```

Optional function that informs the mechanism when the state of the group members have changed by passing the updated list of key – value pairs (currently “id” and “privacy level”) ids of the peers currently available.

* Parameters

  `List<Map<String, String>`: The members of the group.

* Returns

  None.

```java
boolean saveState (ObjectOutputStream)
```

Periodically called by EHC to save PM's state information required on a subsequent restart and/or buffer data for future use.

* Parameters

  `ObjectOutputStream`: A data stream for writing state information.

* Returns

  False/True indicating whether state was not/was saved.

```java
int processData (ObjectInputStream, ObjectOutputStream)
```

Called by the EHC to process generated data by the ST. The returned data are transparently returned to the EHS or the network cluster head in case the PM is collaborative.

* Parameters

  `ObjectInputStream`: A data stream, containing sensing data to be processed.

  `ObjectOutputStream`: A data stream for writing the processed data.

* Returns

  `int`: The EHC identifier where the data should be sent, 0 if no networking action is needed and -1 in case of error.

```java
boolean aggregateData (ObjectInputStream, ObjectOutputStream)
```

Optional function used by collaborative PMs and called only by the network cluster head whenever new data arrive. Determines what part of a dataset satisfies the PM defined criteria and thus it is safe to be sent to the EHS.

* Parameters

  `ObjectInputStream`: The dataset that has locally or remotely been processed.

  `ObjectOutputStream`: The dataset that satisfies the device owner's privacy preferences (null, if not satisfied).

* Returns

  False/True indicating whether data was not/was saved.
