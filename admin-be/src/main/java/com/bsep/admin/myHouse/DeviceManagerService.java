package com.bsep.admin.myHouse;

import com.bsep.admin.model.Device;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DeviceManagerService {

    private final String serverUrl = "http://localhost:5000";

    public void startDevice(Device device) {
        String urlEncodedUUID = device.getId().toString().replace("-", "%2D");
        var req = HttpRequest
                .newBuilder()
                .uri(java.net.URI.create(serverUrl + "/start_device?id=" + urlEncodedUUID + "&type=" + device.getType().toString()))
                .GET()
                .build();

        try {
            var resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                throw new RuntimeException("Failed to start device");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopDevice(Device device) {
        var req = HttpRequest
                .newBuilder()
                .uri(java.net.URI.create(serverUrl + "/stop_device?id=" + device.getId().toString()))
                .GET()
                .build();

        try {
            var resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                throw new RuntimeException("Failed to stop device");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
