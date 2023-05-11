import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRpcSample {
    public static void main(String[] args) {
        String RpcUrl = "https://rpc.anduschain.io/rpc";
        try {
            JSONObject jsonObject =
                    JsonRpc.request(RpcUrl, "eth_blockNumber", "[]", 1);
            System.out.println(jsonObject.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
