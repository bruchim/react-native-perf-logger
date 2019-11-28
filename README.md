# react-native-perf-logger

## background
This library created to enable production measurement of your react native application.
- In the first phase (based on Parashuram's blog: [React Native Performance Playbook](http://blog.nparashuram.com/2018/11/react-native-performance-playbook-part-i.html)) - I tried to measure the duration of app's startup time.
- On the way, allow measurement of memory consumption, battery consumption and other footprints of your application.



## Getting started

`$ npm install react-native-perf-logger --save`


### Manual installation

#### iOS

[ WIP] 
1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-perf-logger` and add `PerfLogger.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libPerfLogger.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactnativeperflogger.PerfLoggerPackage;` to the imports at the top of the file
    
  - Add `new PerfLoggerPackage()` to the list returned by the `getPackages()` method
  - Add `ReactNativePerfLogger.Initialize();` to the first line of `onCreate()` method:
  ```
  @Override
  public void onCreate() {
    ReactNativePerfLogger.Initialize();
    super.onCreate();
    ...
  }
  ```
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-perf-logger'
  	project(':react-native-perf-logger').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-perf-logger/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-perf-logger')
  	```

## Usage

1. Add a prop `nativeID="tti_complete"` to the last view of the first screen of your application.
2. Register to TTI completed event, and get the JSON includes all the recorded markers:

```javascript
import PerfLogger from 'react-native-perf-logger';
    PerfLogger.registerTTICompletedListener(async () => {
      const result = await PerfLogger.getMarkersJSON();
      console.log(result);
    })
  }
```
3. Do whatever you want with this JSON (send to your server, send bi..)

