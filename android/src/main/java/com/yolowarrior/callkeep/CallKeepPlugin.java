package com.yolowarrior.callkeep;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.ConnectionRequest;
import android.net.Uri;
import java.util.List;

import androidx.core.app.ActivityCompat;

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
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .addSupportedUriScheme(PhoneAccount.SCHEME_TEL)
            .build();

    telecomManager.registerPhoneAccount(account);


    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    List<PhoneAccountHandle> enabledAccounts = telecomManager.getCallCapablePhoneAccounts();
    boolean isEnabled = false;
    for (PhoneAccountHandle handleCheck : enabledAccounts) {
      if (handleCheck.equals(accountHandle)) {
        isEnabled = true;
        break;
      }
    }

    if (!isEnabled) {
      Intent intent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
      call.reject("Phone account is not enabled yet. Opened settings.");
      return;
    }

    // ✅ Только если активен
    Bundle extras = new Bundle();
    extras.putString("CALL_ID", uuid);

    Intent serviceIntent = new Intent(context, CallForegroundService.class);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(serviceIntent);
    } else {
      context.startService(serviceIntent);
    }

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
