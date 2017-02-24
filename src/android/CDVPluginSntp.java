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
    
    public static final String SET_SERVER = "setServer";
    public static final String GET_TIME = "getTime";
    public static final String GET_CLOCK_OFFSET = "getClockOffset";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(SET_SERVER)) {
            server = args.getString(0);
            timeout = args.getInt(1);
            callbackContext.success();
        } else if (action.equals(GET_TIME)) {
            getTime(callbackContext);
        } else if (action.equals(GET_CLOCK_OFFSET)) {
            getClockOffset(callbackContext);
        } else {
            return false;
        }
    
        return true;
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
                try {
                    response.put("time", now);
                } catch(JSONException ex) {
                    callbackContext.error("Error creating JSON object.");
                }
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
                try {
                    response.put("offset", offset);
                } catch(JSONException ex) {
                    callbackContext.error("Error creating JSON object.");
                }
                callbackContext.success(response);
            }
        });
    }
}
