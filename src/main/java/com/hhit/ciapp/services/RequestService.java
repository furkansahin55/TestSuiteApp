package com.hhit.ciapp.services;

import com.hhit.ciapp.classes.RequestBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service()
public class RequestService {
    public JSONObject getJSON() throws IOException {
        String url = "https://oaf8593py2.execute-api.us-east-2.amazonaws.com/MIG/api/analytics/numbers/countByTypeAndStatus";
        String method = "POST";
        String body = "{}";
        RequestBuilder request = new RequestBuilder(url,method,body);
        return request.makeRequest();
    }

}
