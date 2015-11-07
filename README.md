# EasyHarvest
EasyHarvest aims to simplify the deployment and controlled execution of large-scale sensing applications on smartphones. On the one hand, application owners submit to a server sensing tasks for distribution on smartphones, and collect the data produced by them in a simple manner. On the other hand, smartphone owners control the execution of sensing tasks on their devices through a single interface, without having to repeatedly download, install and configure individual sensing applications. The interaction between the smartphone and the server occurs in a transparent way, with tolerance to intermittent connectivity and support for disconnected operation.

*For more information read* [here](http://www.inf.uth.gr/wp-content/uploads/formidable/Katsomallos_Emmanouil.pdf) *and* [here](http://www.inf.uth.gr/wp-content/uploads/formidable/Katsomallos_Emmanouil1.pdf).

## Privacy Mechanisms
### Data Structure
Data recorded by Sensing Tasks, processed by Privacy Mechanisms and sent to the EasyHarvestServer by the EasyHarvestClients should follow a specific structure shown in the table below to achieve proper communication and function of the framework entities. This way, we patronize the data collected without limiting application capabilities and Sensing Task developer freedom. For instance, the values field from an application that monitors location would typically contain [0] longitude, [1] latitude and optionally [2] altitude and [3] speed.

| Type          | Name          | Description   |
| ------------- | ------------- | ------------- |
| int           | device        |
| int           | device        |
| int           | device        |
| int           | device        |
