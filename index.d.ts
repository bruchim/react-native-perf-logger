declare module 'react-native-perf-logger' {
  interface PerfLogger {
    getAllMarkers(): Promise<string>;
    getIntervalBounds(): Promise<string>;
    stopAndClear(): void;
    registerTTICompletedListener(callback: (timeAsString: string) => {}): void;
    unregisterTTICompletedListener(callback: (timeAsString: string) => {}): void;
  }

  const perfLogger: PerfLogger;
  export = perfLogger;
}
