package com.tech.cybercars.services.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tech.cybercars.constant.SocketEvent;
import com.tech.cybercars.constant.Tag;
import com.tech.cybercars.constant.URL;
import com.tech.cybercars.data.local.AppDBContext;
import com.tech.cybercars.data.models.Notification;
import com.tech.cybercars.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    private Socket socket;
    public static boolean is_running = false;
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
        String user_token = SharedPreferencesUtil.GetString(
                getApplicationContext(),
                SharedPreferencesUtil.USER_TOKEN_KEY
        );
        if(user_token.equals("")){
            return;
        }

        Map<String, String> auth = new HashMap<>();
        auth.put("token", user_token);
        IO.Options options = IO.Options.builder()
                .setForceNew(true)
                .setReconnectionAttempts(Integer.MAX_VALUE)
                .setTimeout(10000)
                .setAuth(auth)
                .build();

        try {
            socket = IO.socket(URL.BASE_URL, options);
            SocketListener();
            socket.connect();
            is_running = true;
        } catch (URISyntaxException ignored) {
            Log.e(Tag.CYBER_DEBUG, ignored.getMessage());
        }
        Log.i(Tag.CYBER_DEBUG, "Socket Service: Started");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        socket.disconnect();
        socket.off(SocketEvent.CONNECT, OnConnectEvent);
        socket.off(SocketEvent.DISCONNECT, OnDisconnectEvent);
        socket.off(SocketEvent.NOTIFICATION, OnNotificationEvent);
        is_running = false;
        Log.e(Tag.CYBER_DEBUG, "Socket Service: Stoped");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        Log.e(Tag.CYBER_DEBUG, "onStartCommand");
        if(!is_running){
            InitSocketIO();
        }
        return START_STICKY;
    }

    private void SocketListener() {
        socket.on(SocketEvent.CONNECT, OnConnectEvent);
        socket.on(SocketEvent.DISCONNECT, OnDisconnectEvent);
        socket.on(SocketEvent.NOTIFICATION, OnNotificationEvent);
    }

    private final Emitter.Listener OnNotificationEvent = args -> {
        String notification_str = (String) args[0];
        Notification notification = new Gson().fromJson(notification_str, Notification.class);
        EventBus.getDefault().post(notification);
        AppDBContext.GetInstance(this).NotificationDAO().InsertNotification(notification);
    };

    private final Emitter.Listener OnDisconnectEvent = args -> {
        Log.e(Tag.CYBER_DEBUG, "Socket disconnect");
    };

    private final Emitter.Listener OnConnectEvent = args -> {
        Log.e(Tag.CYBER_DEBUG, "Socket id::" + socket.id());
    };

}
