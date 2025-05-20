import { registerPlugin } from '@capacitor/core';

import type { CallKeepPluginPlugin } from './definitions';

const CallKeepPlugin = registerPlugin<CallKeepPluginPlugin>('CallKeepPlugin', {
  web: () => import('./web').then((m) => new m.CallKeepPluginWeb()),
});

export * from './definitions';
export { CallKeepPlugin };
