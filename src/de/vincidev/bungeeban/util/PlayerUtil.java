package de.vincidev.bungeeban.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerUtil {

    public static UUID getUniqueId(String playername) {
        try {
            URLConnection conn = new URL("https://eu.mc-api.net/v3/uuid/" + playername).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONObject json = new JSONObject(response);
            return UUID.fromString(json.getString("full_uuid"));
        } catch (Exception e) {
        }
        return null;
    }

    public static String getUUID(String playername) {
        UUID uuid = getUniqueId(playername);
        if(uuid != null) {
            return uuid.toString();
        }
        return null;
    }

    public static String getPlayername(UUID uuid) {
        try {
            URLConnection conn = new URL("https://eu.mc-api.net/v3/name/" + uuid.toString()).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONObject json = new JSONObject(response);
            return json.getString("name");
        } catch (Exception e) {
        }
        return null;
    }

    public static String getPlayername(String uuid) {
        return getPlayername(UUID.fromString(uuid));
    }

    public static HashMap<Long, String> getNameHistory(UUID uuid) {
        try {
            URLConnection conn = new URL("https://eu.mc-api.net/v3/history/" + uuid.toString()).openConnection();
            String response = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(br.ready()) {
                response = response + br.readLine();
            }
            JSONArray arr = new JSONObject(response).getJSONArray("history");
            HashMap<Long, String> history = new HashMap<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject change = arr.getJSONObject(i);
                String name = change.getString("name");
                long changedAt = 0L;
                if(change.has("changedToAt")) {
                    changedAt = change.getLong("changedToAt");
                }
                history.put(changedAt, name);
            }
            return history;
        } catch (Exception e) {
        }
        return null;
    }

}