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
  - Add import statements
  ```
  import com.reactnativeperflogger.PerfLoggerPackage; 
  import com.reactnativeperflogger.RNPerfLogger;
  ``` 
to the imports at the top of the file
  - Add `private RNPerfLogger perfLogger` 
  - Add `perfLogger = new RNPerfLogger();` to the first line of `onCreate()` method:
  ```
  @Override
  public void onCreate() {
    perfLogger = new RNPerfLogger();
    super.onCreate();
    ...
  }
  ```
  - Add `new PerfLoggerPackage(perfLogger)` to the list returned by the `getPackages()` method

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

1. Add a prop `nativeID` to the last view of the first screen of every loading flow of your application.
```
  render() {
    return (
      <FlatList
        ...
        id={'HOME_SCREEN_LIST'}
        nativeID="tti_complete"
      />
    );
  }
```
2. Register all such nativeIDs with 
```javascript
PerfLogger.registerTTINativeIds();
```
3. Register to TTI completed event, and get the JSON includes all the recorded markers:

```javascript
import PerfLogger from 'react-native-perf-logger';
    PerfLogger.registerTTICompletedListener(async (time) => {
      console.log(`tti_complete time: ${time}`);
      const result = await PerfLogger.getMarkersJSON();
      
      // you can use alternatively PerfLogger.getIntervalBounds()
      // to get start and end time of all completed interval measurements 
      
      console.log(result);
    });
  }
```
4. Do whatever you want with this JSON (send to your server, send bi..)
5. To force the logger stop listening to events and remove all stored data use:
```javascript
PerfLogger.stopAndClear();
``` 
