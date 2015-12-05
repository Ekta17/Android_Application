# Android_Application
This is an Android Application which displays the schedule of Drexel University shuttles and gives user an option to set an Reminder.

Build Instructions:


1) To Build and test the application, please download the project as a zip file.
2) Extract the zip file into an appropriate folder.
3) To run the application any IDE can be used among Android Studio and Eclipse. Both these IDEs are open source and can be easily downloaded from their following official website:
    
    Eclipse: https://www.eclipse.org/downloads/
      Also, it is required to install Android plugins in Eclipse to run Android Applications. The steps to install Android plugin in        Eclipse can be found on the following link:
          http://developer.android.com/sdk/installing/installing-adt.html
    
    Android Studio: http://developer.android.com/sdk/index.html
  
4) After installation of the IDE, import the Android Application into the workspace using the instructions:

  Eclipse: 
    1)Goto File
    2)Import
    3)Import an exisiting Android code into Workspace
    4) In the Root Directory, browse the extracted code of the android application and select the 'Android_Application-master' folder.
    5) Click 'Finish'
  
  Android Studio:
    1) Choose the option Import project in the first screen on the start of Android Studio.
    2) Browse and provide the location of 'Android_Application-master' folder and click OK.
  
  If you face any difficuties please refer to the instructions given in the following website:
  
    Eclipse: http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm
    Android Studio: http://developer.android.com/sdk/installing/migrate.html#migrate
    
 5) To Run the Application follow the following instructions:
    
    Eclipse: 
      1) Right click on the application folder and select 'Rus As'. 
      2) For the list options available in Run configuration, select 'Android Application'.
      3) Now connect the android phone and use that as a target to run the application.
      4) In step 3 we can also you Android Emulator device to run the application but it is not recommended. 
          To read about Android Emulator, please follow the following link:
            http://developer.android.com/tools/devices/emulator.html
          
    Android Studio:
        1) Goto 'Run' and select the option 'Run 'app''.
        2) Now you will be prompted to choose the device to run the application. You can choose between running the application on                actual android device or on android emulator.
        3) To run the application on your phone, keep the phone connected to the computer using USB cable and activate the device for             use.
  
  
  To set up an Android Emulator, please follow the instructions given in the following link:
    http://developer.android.com/tools/devices/index.html
  Please note: While creating Emulator, please choose the following options for the application to work successfully:
    API level: 22
    Release Name: Lollipop
    
  6) We can also generate an .apk file for our android application and then this .apk file can be used to run the application on          android devices which satisfy the minimun sdk requirement. For this application, minimum sdk requirement are :
    Android Version: 5 
    API level: 22
    Release Name: Lollipop
    
    To generte .apk file, please follow the instructions given on the following link:
       http://developer.android.com/tools/publishing/app-signing.html
    
    There is an apk file named 'DrexelBusAlarm.apk' has been provided.
    
