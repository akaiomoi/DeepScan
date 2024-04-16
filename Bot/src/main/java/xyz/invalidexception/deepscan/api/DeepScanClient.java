package xyz.invalidexception.deepscan.api;

import lombok.SneakyThrows;
import okhttp3.*;
import xyz.invalidexception.deepscan.ScanBot;

import java.util.HashMap;
import java.util.Map;

public class DeepScanClient {
    @SneakyThrows
    public ApiResponse reviewMessage(String message) {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Map<String, String> queryParameters = new HashMap<>();

        queryParameters.put("message", message);

        String bodyJson = ScanBot.getGson().toJson(
                queryParameters);

        Response response = client.newCall(new Request.Builder().url("http://127.0.0.1:5000/message/review").post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson)).build()).execute();

        String body = response.body().string();
        return ScanBot.getGson().fromJson(body, ApiResponse.class);
    }
}
