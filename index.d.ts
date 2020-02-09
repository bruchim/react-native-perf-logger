declare module 'react-native-perf-logger' {
  interface PerfLogger {
    getAllMarkers(): Promise<string>;
    getIntervalBounds(): Promise<string>;
    stopAndClear(): void;
    registerTTICompletedListener(callback: () => {}): void;
    unregisterTTICompletedListener(callback: () => {}): void;
  }

  const perfLogger: PerfLogger;
  export = perfLogger;
}
