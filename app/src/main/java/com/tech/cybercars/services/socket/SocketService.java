package com.tech.cybercars.services.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tech.cybercars.constant.FieldName;
import com.tech.cybercars.constant.SocketEvent;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.services.eventbus.NotificationEvent;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SocketService extends Service {
    private Socket socket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InitSocketIO();

    }

    private void InitSocketIO() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnectionAttempts = Integer.MAX_VALUE;
        options.timeout = 10000;
        options.query = "token=" + SharedPreferencesUtil.GetString(
                getApplicationContext(),
                SharedPreferencesUtil.USER_TOKEN_KEY
        );
        try {

            socket = IO.socket(URL.BASE_URL);
            socket.connect();
            SocketListener();
        } catch (URISyntaxException ignored) {
            Log.e(Tag.CYBER_DEBUG, ignored.getMessage());
        }
        Log.i(Tag.CYBER_DEBUG, "Socket Service: Started");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Tag.CYBER_DEBUG, "Socket Service: Stoped");
    }

    private void SocketListener() {
        socket.on(SocketEvent.NOTIFICATION, OnNotificationEvent);
    }

    private final Emitter.Listener OnNotificationEvent = args -> {
        JSONObject data = (JSONObject) args[0];
        Gson gson = new Gson();
        NotificationEvent notification_event = gson.fromJson(String.valueOf(data), NotificationEvent.class);
        EventBus.getDefault().post(notification_event);
    };

}
