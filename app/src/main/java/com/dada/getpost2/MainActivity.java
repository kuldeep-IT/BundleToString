package com.dada.getpost2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: write the logic for the convert bundle to string - complete
        Bundle bundle = new Bundle();
        bundle.putString("key_1", "param1");
        bundle.putString("key_2", "param2");
        bundle.putString("key_3", "param3");
        bundle.putString("key_4", "param4");

        Bundle bundle2 = new Bundle();
        bundle2.putString("app_instance_id","34838297489237");
        bundle2.putString("deviceId","123456778");

        Log.d("OUR_BUNDLE_TO_STRING", "params: "+ bundleToStr(bundle));

        Log.d("OUR_BUNDLE_TO_STRING_3", "params: "+ bundleToJsonString3(bundle));
        //params: {"key_1":"param1","key_2":"param2","key_3":"param3","key_4":"param4"}

        executorService = Executors.newSingleThreadExecutor();

        final String[] response = {""};

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    // Step 2: Create instances of the objects with your desired data
                    InnerObject innerObject = new InnerObject("Inner data");

                    OuterObject outerObject = new OuterObject();
                    outerObject.setEventName("btn_click");
                    outerObject.setInnerObject(innerObject);

                    // Step 3: Convert the object to JSON manually
//                    String json = "{\"params\":{\"param1\":\"" + outerObject.getInnerObject().getInnerData() + "\"}}";
//                    String json = "{\"params\":\""+ "{" + bundleToStr(bundle) + "}" + "\"}";

                    /*final output - take as reference*/
//                    String json = "{\"params\":"+ bundleToJsonString3(bundle)  + "}";

                    String json = "{\"event_name\":"+ "\"btn_click\"" +","+
                            "\"params\":"+ bundleToJsonString3(bundle)+","+
                            "\"meta\":"+bundleToJsonString3(bundle2)+
                            "}";

                    Log.d("OUR_JSON_RES", "DAta: "+json);

                    // Step 4: Make the POST network call
                    String urlEndpoint = "https://debugger-dev.tagmate.app/api/v1/debugger/appRequests";
                    URL url = new URL(urlEndpoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream outputStream = connection.getOutputStream()) {
                        byte[] input = json.getBytes(StandardCharsets.UTF_8);
                        outputStream.write(input, 0, input.length);
                    }

                    int responseCode = connection.getResponseCode();
                    // Process the response as needed

                    if (responseCode == 200) {
                        try (
                                InputStream inputStream = connection.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        ) {
                            Log.d("RES_CODE_HERE_RES", bufferedReader.readLine()+" ");
                            response[0] = bufferedReader.readLine();
                        }
                    }

                    Log.d("YOUR_RES_CODE", "responseCode: " + responseCode);

                    connection.disconnect();
                } catch (Exception e) {
                    Log.d("YOUR_RES_C", "error: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
//        String jSOn = "{event:"+savedInstanceState.getString('name');



    }

    private String bundleToStr(Bundle bundle) {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{");

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            stringBuilder.append(key).append(":").append(value).append(", ");
        }

        if (stringBuilder.length() > 1) {
            // Remove the trailing comma and space
            stringBuilder.setLength(stringBuilder.length() - 2);
        }

//        stringBuilder.append("}");
        String bundleToString = stringBuilder.toString();

        return bundleToString;
    }

    private String bundleToJsonString3(Bundle bundle) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            stringBuilder.append("\"").append(key).append("\":").append(valueToJsonString(value)).append(",");
        }

        if (stringBuilder.length() > 1) {
            // Remove the trailing comma
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    // Method to convert a value to its JSON string representation
    private String valueToJsonString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeString((String) value) + "\"";
        } else if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Boolean) {
            return value.toString();
        } else {
            // Handle other value types accordingly
            return "\"" + escapeString(value.toString()) + "\"";
        }
    }

    // Method to escape special characters in a string
    private String escapeString(String value) {
        value = value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        // Handle other special characters if needed

        return value;
    }

}