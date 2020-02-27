package com.hhit.ciapp.classes;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;


public class JSONUtils {
    /**
     * Convert a JSON string to pretty print version
     *
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }


    public static JSONArray mapResultSet(ResultSet rs) throws SQLException, JSONException
    {
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        do
        {
            jsonObject = new JSONObject();
            for (int index = 1; index <= columnCount; index++)
            {
                String column = rsmd.getColumnName(index);
                Object value = rs.getObject(column);
                if (value == null)
                {
                    jsonObject.put(column, "");
                } else if (value instanceof Integer) {
                    jsonObject.put(column, value);
                } else if (value instanceof String) {
                    jsonObject.put(column, value);
                } else if (value instanceof Boolean) {
                    jsonObject.put(column, value);
                } else if (value instanceof Date) {
                    jsonObject.put(column, ((Date) value).getTime());
                } else if (value instanceof Long) {
                    jsonObject.put(column, value);
                } else if (value instanceof Double) {
                    jsonObject.put(column, value);
                } else if (value instanceof Float) {
                    jsonObject.put(column, value);
                } else if (value instanceof BigDecimal) {
                    jsonObject.put(column, value);
                } else if (value instanceof Byte) {
                    jsonObject.put(column, value);
                } else if (value instanceof byte[]) {
                    jsonObject.put(column, value);
                } else {
                    throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
                }
            }
            jArray.put(jsonObject);
        }while(rs.next());
        return jArray;
    }






}
