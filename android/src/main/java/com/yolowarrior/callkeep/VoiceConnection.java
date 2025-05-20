package com.yolowarrior.callkeep;

import android.telecom.Connection;
import android.telecom.DisconnectCause;

public class VoiceConnection extends Connection {

  @Override
  public void onAnswer() {
    super.onAnswer();
    setActive();
    CallKeepPlugin.notifyAnswer(getConnectionId());
  }

  @Override
  public void onDisconnect() {
    super.onDisconnect();
    setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
    destroy();
    CallKeepPlugin.notifyEnd(getConnectionId());
  }

  public String getConnectionId() {
    return getExtras() != null ? getExtras().getString("CALL_ID") : null;
  }
}
