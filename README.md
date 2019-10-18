# PEPPERDashboard
![](https://github.com/alexrussellbrookes/PEPPERChart/blob/master/images/PEPPER_Logo.jpg)

This implementation of the PEPPER app's dashboard relies on the [PEPPERChart](https://github.com/alexrussellbrookes/PEPPERChart) charting 
library.

This code provides an architecture for the dynamic data visualizations used in the PEPPER application, which can be updated as new data 
is generated. The visualization designs can also be easily modified with minimal disruption to the business logic of the app. 

This is achieved through the separation of concerns in the ModelView-ViewModel design pattern. In this framework, the DataModel supplies 
data from the app’s data structures to the visualization module. The ViewModel takes data from the DataModel and formats it so that it can 
be used by the View. It ‘listens’ for changes in the data and then notifies the View. Lastly, the View component presents the data on the 
screen. It relies on [PEPPERChart](https://github.com/alexrussellbrookes/PEPPERChart) (a custom-built SVG charting library for Android) to 
load the visualization as a webpage into an Android WebView. 

The code here has two components (other than the charting library mentioned above).
1.	PEPPER dashboard module. This is a module which creates a visualization for the PEPPER app’s dashboard display using the MVVM 
architecture. The module has been designed so that it may be easily integrated into a future version of the app. 
2. The Android app itself is a test implementation of the dashboard module. To demonstrate how the implementation might be 
carried out, I have created a test implementation with real, though anonymized, data. 

A diagram of the relationship of the key classes is shown below:

![](https://github.com/alexrussellbrookes/PEPPERDashboard/blob/master/images/PEPPERMVVM.jpg)

In the code found here, the View and ViewModel are part of the **pepperdashboard** module. This module could be integrated into a version of the app 'as is'. In the main app code the DashboardDataRep mocks a real data repository provided by the business logic of the app, and the DashboardActivity mocks the Activity controlling the dashboard visualization.      

This code has been created for the [PEPPER Project](http://www.pepper.eu.com/) which has received funding from the European Union’s 
Horizon 2020 research and innovation programme under grant agreement 689810.
