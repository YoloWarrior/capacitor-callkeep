package com.yolowarrior.callkeep;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;

import com.yolowarrior.callkeep.VoiceConnection;

public class VoiceConnectionService extends ConnectionService {

  @Override
  public Connection onCreateIncomingConnection(PhoneAccountHandle handle, ConnectionRequest request) {
    VoiceConnection connection = new VoiceConnection();
    Bundle extras = request.getExtras();
    connection.setExtras(extras);
    connection.setRinging();
    return connection;
  }

  @Override
  public Connection onCreateOutgoingConnection(PhoneAccountHandle handle, ConnectionRequest request) {
    VoiceConnection connection = new VoiceConnection();
    connection.setDialing();
    return connection;
  }
}
