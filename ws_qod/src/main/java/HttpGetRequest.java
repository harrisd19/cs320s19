import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpGetRequest {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Use: java HttpGetRequest <URL>");
            System.exit(1);
        }
        String strUrl = args[0];
        System.out.println("URL: " + strUrl);

        // make the connection
        URL url = new URL(strUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        // optional since defaults to GET anyway...
        httpConn.setRequestMethod("GET");
        //httpConn.setRequestProperty("User-Agent", USER_AGENT);

        // send the request
        System.out.println("Sending 'GET' request to URL : " + url);
        int responseCode = httpConn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        // check for redirect...
        if (responseCode == 301) {
            String newUrl = httpConn.getHeaderField("Location");
            System.out.println("Redirect: " + newUrl);
        }

        // read the response
        Scanner sc = new Scanner(httpConn.getInputStream());
        String rawJson = "";
        while (sc.hasNext()) {
            String line = sc.nextLine();
            rawJson += line;
        }
        sc.close();

//        System.out.println(rawJson);
        Gson gson = new Gson();
        Response response = gson.fromJson(rawJson, Response.class);
        Contents contents = response.contents;
        Quote quotes[] = contents.quotes;
        String quote = quotes[0].quote;
        System.out.println("Here is your quote of the day:");
        System.out.println(quote);



    }
}
