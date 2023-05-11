import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRpc {
    public static JSONObject request(String urlString, String method, String params, Integer id ) throws Exception {
        JSONObject jsonObject = null;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(10000); //10 secs
        connection.setReadTimeout(10000); //10 secs
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream os = new DataOutputStream(connection.getOutputStream());

        JSONObject request = new JSONObject();
        request.put("jsonrpc", "2.0");
        request.put("method", method);
        request.put("params", params);
        request.put("id", id);

        String body = request.toString();
        byte[] wBuf = body.getBytes("UTF-8");
        os.write(wBuf, 0, wBuf.length);
        os.flush();
        os.close();

        Integer code = connection.getResponseCode();
        String message = connection.getResponseMessage();

        String rBody = "";
        if(code==200 ) {
            BufferedReader rd;
            try {
                rd = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuffer strbuf = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    strbuf.append(line);
                }
                rBody = strbuf.toString();
                jsonObject = new JSONObject(rBody);
            } catch (Exception e) {
                jsonObject = new JSONObject();
                jsonObject.put("code", code);
                jsonObject.put("message", message);
                jsonObject.put("error", e.toString());
            }
        }
        else {
            BufferedReader rd;
            try {
                rd = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream()));
                StringBuffer strbuf = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    strbuf.append(line);
                }
                rBody = strbuf.toString();
                jsonObject = new JSONObject(rBody);
            } catch (Exception e) {
                jsonObject = new JSONObject();
                jsonObject.put("code", code);
                jsonObject.put("message", message);
                jsonObject.put("error", e.toString());
            }
        }
        return jsonObject;
    }
}
