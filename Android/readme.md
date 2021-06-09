
# Foories - Android
Food Classification, Detection, and Calories Measurement

## Description
Branch for Foories  Android application. Contains architecture, used library, and steps to reproduce

## Table of Content

- [Used Library](#used-library)
- [Architecture](#architecture)
- [How To Build](#step-to-build)


### Used Library 
Foories use some library to support the functionality of the application

1. Kotlin Coroutine Android (1.5.0-native-mt)

	To support kotlin concurrency 
	
2. Koin (3.0.2)

	Android dependency injection library
	
3. Activity KTX (1.2.3)

	Jetpack library to support ViewModel binding with activity
	
4. FastAndroidNetworking (2.9.0)

	Networking library to handle Network Call and communicate with web server
	
5. Gson (2.9.0)

	library to parse Javascript Object Notation (JSON) data
	
6. Easy Permission (6.2.2)

	Android permission library
	
7. Firebase ML Kit


### Architecture
This implements Clean Architecture combining with Model-View-ViewModel (MVVM) 
Pattern which is part of Android Jetpack and recommended by google

### Step to Build

1. Clone this repository
2. Open with Android Studio 
3. Wait for the gradle to be sync
4. Launch the app
