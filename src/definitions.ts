export interface CallKeepPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
