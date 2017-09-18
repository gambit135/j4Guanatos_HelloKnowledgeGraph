package org.j4g.kg;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.jayway.jsonpath.JsonPath;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Alejandro_Tellez <java.util.fck@hotmail.com>
 */
public class HelloKnowledgeGraphWorld {

    public static Properties properties = new Properties();

    public static void main(String[] args) {
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");

        try {
            properties.load(new FileInputStream("./src/main/resources/kgsearch.properties"));

            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();

            JSONParser jsonParser = new JSONParser();

            GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
            url.put("query", "Taylor Swift");
            url.put("limit", "10");
            url.put("indent", "true");
            url.put("key", properties.get("API_KEY"));

            System.out.println("URL: " + url.toString());

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();

            JSONObject jsonResponse = (JSONObject) jsonParser.parse(httpResponse.parseAsString());
            JSONArray jsonArrayElements = (JSONArray) jsonResponse.get("itemListElement");

            for (Object element : jsonArrayElements) {
                System.out.println("\n");
                System.out.println("Name: " + JsonPath.read(element, "$.result.name").toString());
                System.out.println("Descripton: " + JsonPath.read(element, "$.result.description").toString());
                System.out.print("Types: ");

                ArrayList<String> elementTypes = JsonPath.read(element, "$.result.@type[*]");
                for (String type : elementTypes) {
                    System.out.print(type + " | ");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
