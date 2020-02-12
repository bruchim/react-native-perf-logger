declare module 'react-native-perf-logger' {
  interface PerfLogger {
    getAllMarkers(): Promise<string>;
    getIntervalBounds(): Promise<string>;
    stopAndClear(): void;
    registerTTICompletedListener(callback: (time: number) => {}): void;
    unregisterTTICompletedListener(callback: (time: number) => {}): void;
  }

  const perfLogger: PerfLogger;
  export = perfLogger;
}
