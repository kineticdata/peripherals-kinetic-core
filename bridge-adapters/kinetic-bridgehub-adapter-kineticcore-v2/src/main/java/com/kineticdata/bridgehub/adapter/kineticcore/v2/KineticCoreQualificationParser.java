package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import com.kineticdata.bridgehub.adapter.BridgeError;
import com.kineticdata.bridgehub.adapter.QualificationParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class KineticCoreQualificationParser extends QualificationParser {
     /** Defines the logger */
    protected static final org.slf4j.Logger logger 
        = LoggerFactory.getLogger(KineticCoreAdapter.class);
    
    protected Map<String, String> getParameters (String queryString)
        throws BridgeError {
        
        // Split the query from the rest of the string
        String[] parts = queryString.split("[?]",2);
        
        Map<String, String> parameters = new HashMap<>();

        if (parts.length > 1) {
            // Split into individual queries by splitting on the & between each 
            // distinct query
            String[] queries = parts[1].split("&(?=[^&]*?=)");
            for (String query : queries) {
                // Split the query on the = to determine the field/value key-pair. 
                // Anything before the first = is considered to be the field and 
                // anything after (including more = signs if there are any) is 
                // considered to be part of the value
                String[] str_array = query.split("=",2);
                parameters.merge(str_array[0].trim(), str_array[1].trim(), 
                    (prev, curr) -> {
                        return String.join(",", prev, curr);
                    }
                );
            }
        }
        
        return parameters;
    }
    
    public String parsePath (String queryString) {
        // Split the api path from the rest of the string
        String[] parts = queryString.split("[?]",2);
        
        return parts[0];
    }
    
    @Override
    public String encodeParameter(String name, String value) {
        String result = null;
        if (value != null) {
            result = value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
        return result;
    }
}