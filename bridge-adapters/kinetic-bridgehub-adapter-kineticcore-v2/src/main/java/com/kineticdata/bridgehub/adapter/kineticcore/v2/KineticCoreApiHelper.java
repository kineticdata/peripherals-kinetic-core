package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import com.kineticdata.bridgehub.adapter.BridgeError;
import com.kineticdata.bridgehub.adapter.BridgeRequest;
import static com.kineticdata.bridgehub.adapter.kineticcore.v2.KineticCoreAdapter.logger;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class KineticCoreApiHelper {
    private final String username;
    private final String password;

    public KineticCoreApiHelper (String username, String password, String space) {
        this.username = username;
        this.password = password;
    }
    
    public String executeRequest (BridgeRequest request,
        String url, KineticCoreQualificationParser parser) throws BridgeError{
        String output = "";
        
        try (
            CloseableHttpClient client = HttpClients.createDefault()
        ) {
            HttpResponse response;
            HttpGet get = new HttpGet(url);
            
            get = addAuthenticationHeader(get, this.username, this.password);
            response = client.execute(get);

            logger.trace("Request response code: " + response.getStatusLine()
                .getStatusCode());
            
            HttpEntity entity = response.getEntity();
            output = EntityUtils.toString(entity);
            
            if (response.getStatusLine().getStatusCode() == 404) {
                throw new BridgeError(String.format(
                    "Not Found: %s not found at %s.", request.getStructure(),
                    String.join(",", parser.parsePath(request.getQuery()))));
            } else if (response.getStatusLine().getStatusCode() == 400) {
                JSONObject json = (JSONObject)JSONValue.parse(output);
                throw new BridgeError("Bad Reqeust: " + json.get("error"));
            } else if (response.getStatusLine().getStatusCode() == 500) {
                throw new BridgeError("500 Internal Server Error");
            }
            
        }
        catch (IOException e) {
            logger.error("An unexpected IO exception was encountered calling the"
                + " core API.", e);
            throw new BridgeError(
                "Unable to make a connection to the Kinetic Core server.");
        }
        
        return output;
    }
    
    /*--------------------------------------------------------------------------
     * HELPER METHODS
     *------------------------------------------------------------------------*/   
    private HttpGet addAuthenticationHeader(HttpGet get, String username,
        String password) {
        
        String creds = username + ":" + password;
        byte[] basicAuthBytes = Base64.encodeBase64(creds.getBytes());
        get.setHeader("Authorization", "Basic " + new String(basicAuthBytes));

        return get;
    }
}
