package Kiosk_Metro;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataTest {
    String str_line_table;
    ArrayList<StationList> arr = new ArrayList<>();
    public DataTest(String str) {
        this.str_line_table = str;
        String url = "https://raw.githubusercontent.com/LDK511/mydata/main/";

        BufferedReader in = null;
        
        try {
            String url_encode = URLEncoder.encode(str, "UTF-8");
            try {
                URL obj = new URL(url + url_encode + ".json");
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");

                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONParser js = new JSONParser();
                JSONArray jsonArr = (JSONArray) js.parse(response.toString());

                for (int i = 0; i < jsonArr.size(); i++) {
                    JSONObject jj = (JSONObject) jsonArr.get(i);
                    arr.add(new StationList(
                            jj.get("code").toString(),
                            jj.get("name").toString(),
                            Integer.parseInt(jj.get("type").toString()),
                            Integer.parseInt(jj.get("express").toString()),
                            jj.get("html").toString(),
                            Integer.parseInt(jj.get("size").toString()),
                            Integer.parseInt(jj.get("x").toString()),
                            Integer.parseInt(jj.get("y").toString()),
                            Integer.parseInt(jj.get("train").toString())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
