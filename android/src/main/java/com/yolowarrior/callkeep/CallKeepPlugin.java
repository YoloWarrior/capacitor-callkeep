package com.yolowarrior.callkeep;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.ConnectionRequest;
import android.net.Uri;

import com.getcapacitor.*;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;

@CapacitorPlugin(name = "CallKeep")
public class CallKeepPlugin extends Plugin {

  private static Bridge staticBridge;
  private static CallKeepPlugin instance;

  @Override
  public void load() {
    staticBridge = getBridge();
    instance = this;
  }

  @PluginMethod
  public void displayIncomingCall(PluginCall call) throws JSONException {
    String uuid = call.getString("uuid");
    String handle = call.getString("handle");

    Context context = getContext();
    TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
    ComponentName componentName = new ComponentName(context, VoiceConnectionService.class);
    PhoneAccountHandle accountHandle = new PhoneAccountHandle(componentName, "CallKeep");
    PhoneAccount account = PhoneAccount.builder(accountHandle, "CallKeep")
            .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
            .build();

    telecomManager.registerPhoneAccount(account);

    Bundle extras = new Bundle();
    extras.putString("CALL_ID", uuid);

    ConnectionRequest request = new ConnectionRequest(
            accountHandle,
            Uri.fromParts("tel", handle, null),
            extras
    );

    telecomManager.addNewIncomingCall(accountHandle, extras);
    call.resolve();
  }

  public static void notifyAnswer(String uuid) {
    if (instance != null) {
      JSObject ret = new JSObject();
      ret.put("uuid", uuid);
      instance.notifyListeners("answerCall", ret);
    }
  }

  public static void notifyEnd(String uuid) {
    if (instance != null) {
      JSObject ret = new JSObject();
      ret.put("uuid", uuid);
      instance.notifyListeners("endCall", ret);
    }
  }
}
