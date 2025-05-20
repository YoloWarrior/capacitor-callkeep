import { WebPlugin } from '@capacitor/core';

import type { CallKeepPluginPlugin } from './definitions';

export class CallKeepPluginWeb extends WebPlugin implements CallKeepPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
