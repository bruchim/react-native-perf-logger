declare module 'react-native-perf-logger' {
  interface PerfLogger {
    getAllMarkers(): Promise<string>;
    getIntervalBounds(): Promise<string>;
    stopAndClear(): void;
    registerTTINativeIds(ids: string[]): void;
    registerTTICompletedListener(callback: (id: string, timeAsString: string) => {}): void;
    unregisterTTICompletedListener(id: string, callback: (timeAsString: string) => {}): void;
  }

  const perfLogger: PerfLogger;
  export = perfLogger;
}
