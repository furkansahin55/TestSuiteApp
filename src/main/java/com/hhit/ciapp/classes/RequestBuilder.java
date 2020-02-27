package com.hhit.ciapp.classes;

import lombok.Data;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Data
public class RequestBuilder {

    private String urlPath;
    private String method;
    private String body;
    private final String USER_AGENT = "Mozilla/5.0";


    public RequestBuilder(String urlPath, String method, String body) {
        this.urlPath = urlPath;
        this.method = method;
        this.body = body;
    }

    public JSONObject makeRequest() throws IOException {
        URL url = new URL(this.urlPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // set the request method and properties.
        con.setRequestMethod(this.method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("authorizationKey", "123456789");
        con.setRequestProperty("x-api-key", "Vvl1hSX7yq9q7CA0MSNEJ25TGn3RQPemFRJCMEK0");
        con.setRequestProperty("requestSystem", "null");
        con.setRequestProperty("userID", "Furkan");
        con.setRequestProperty("Content-Type", "application/json");


        //if method is POST
        if (this.method.equals("POST")) {
            // set the doOutput to true.
            con.setDoOutput(true);

            //Create an OutputStream and write request body
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            //Write body only if method type is POST
            osw.write(this.body);
            osw.flush();
            osw.close();
            os.close();  //don't forget to close the OutputStream
            con.connect();
        }


        //Read the input stream and return it as json object
        String result1;
        BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result2 = bis.read();
        while(result2 != -1) {
            buf.write((byte) result2);
            result2 = bis.read();
        }
        result1 = buf.toString();

        return(new JSONObject(result1));

    }
}
