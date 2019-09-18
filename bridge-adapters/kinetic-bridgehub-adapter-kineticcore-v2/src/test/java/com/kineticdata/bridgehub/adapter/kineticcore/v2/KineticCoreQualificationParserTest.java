package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import com.kineticdata.bridgehub.adapter.kineticcore.v2.KineticCoreQualificationParser;
import org.junit.Before;
import org.junit.Test;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class KineticCoreQualificationParserTest {

    protected KineticCoreQualificationParser parser;

    @Before
    public void beforeEach() throws Exception {
        parser = new KineticCoreQualificationParser();
    }

    /*----------------------------------------------------------------------------------------------
     * TESTS
     *--------------------------------------------------------------------------------------------*/

    @Test
    public void test_parsePath() throws Exception {
        String path = parser.parsePath(
            "kapps/services/forms?q=name=*\"c\" AND status=\"Active\""
        );
        
        assertEquals("kapps/services/forms",  path);
    }
    
    @Test
    public void test_parse_ParameterWithBackslash() throws Exception {
        // `\` should be escaped to `\\`

        // Build the parameter map
        Map<String, String> bridgeParameters = new LinkedHashMap<>();
        bridgeParameters.put("widget", "\\");
        String queryString = parser.parse("q=\"<%=parameter[widget]%>\"",
            bridgeParameters);
        
        assertEquals("q=\"" + "\\\\" + "\"", queryString);
    }
    
    @Test
    public void test_parse_ParameterWithBackslashAndQuotation() throws Exception {
        // `\"` should be escaped to `\\\"`

        // Build the parameter map
        Map<String, String> bridgeParameters = new LinkedHashMap<>();
        bridgeParameters.put("widget", "\\\"");
        String queryString = parser.parse("q=\"<%=parameter[widget]%>\"",
            bridgeParameters);
        
        assertEquals("q=\"" + "\\\\\\\"" + "\"", queryString);
    }

    @Test
    public void test_parse_ParameterWithQuotation() throws Exception {
        // `"` should be escaped to `\"`

        // Build the parameter map
        Map<String, String> bridgeParameters = new LinkedHashMap<>();
        bridgeParameters.put("widget", "\"abc");
        String queryString = parser.parse("q=\"<%=parameter[widget]%>\"",
            bridgeParameters);
        
        assertEquals("q=\"" + "\\\"abc" + "\"", queryString);
    }

}
