package co.hub9.cordova;

import android.os.SystemClock;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class CDVPluginSntp extends CordovaPlugin {
    private final SntpClient client = new SntpClient();
    private String server;
    private Integer timeout;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "setServer":
                server = args.getString(0);
                timeout = args.getInt(1);
                callbackContext.success();
                return true;

            case "getTime":
                getTime(callbackContext);
                return true;

            case "getClockOffset":
                getClockOffset(callbackContext);
                return true;
        }
        return false;
    }

    /**
     * Starts a background thread and connect into SNTP server.
     *
     * @param callbackContext   Context to deliver async response.
     * @param runnable          Object to run when connection is successiful.
     */
    private void connect(final CallbackContext callbackContext, final Runnable runnable) {
        if (server == null || timeout == null) {
            callbackContext.error("Must call `setServer` before.");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!client.requestTime(server, timeout)) {
                    callbackContext.error("Error contacting SNTP Server.");
                }

                runnable.run();
            }
        }).start();
    }

    /**
     * Starts a background thread to connect into SNTP server and get the time.
     *
     * @param callbackContext Context to deliver async response.
     */
    private void getTime(final CallbackContext callbackContext) {
        connect(callbackContext, new Runnable() {
            @Override
            public void run() {
                JSONObject response = new JSONObject();
                long now = client.getNtpTime() + SystemClock.elapsedRealtime() -
                    client.getNtpTimeReference();
                response.put("time", now);
                callbackContext.success(response);
            }
        });
    }

    /**
     * Starts a background thread to connect into SNTP server and calculate system clock offset.
     *
     * @param callbackContext Context to deliver async response.
     */
    private void getClockOffset(final CallbackContext callbackContext) {
        connect(callbackContext, new Runnable() {
            @Override
            public void run() {
                JSONObject response = new JSONObject();
                long offset = client.getClockOffset();
                response.put("offset", offset);
                callbackContext.success(response);
            }
        });
    }
}
