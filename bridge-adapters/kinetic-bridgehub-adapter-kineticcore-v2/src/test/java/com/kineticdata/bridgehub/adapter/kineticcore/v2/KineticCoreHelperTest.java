/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import com.kineticdata.bridgehub.adapter.kineticcore.v2.KineticCoreAdapter;
import com.kineticdata.bridgehub.adapter.Record;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author chad.rehm
 */
public class KineticCoreHelperTest {
    
    @Test
    public void test_paginationSupported_3() throws Exception {
        List<String> paginationFields = new ArrayList<>();
        LinkedHashMap<String, String> sortOrderItems = new LinkedHashMap<>();
      
        paginationFields.add("values[Status]");
        sortOrderItems.put("values[Status]","ASC");
        Map<String, String> parameters = new HashMap<String, String>();
        
        // Test index and order fields match
        boolean supported = false;
        supported = KineticCoreAdapter.paginationSupportedForIndexedModel(
            paginationFields, parameters, "", sortOrderItems);
        Assert.assertTrue(supported);
        
        // Test additional indexs and order fields with same direction
        paginationFields.add("values[Related Id]");
        sortOrderItems.put("values[Related Id]","ASC");
        supported = KineticCoreAdapter.paginationSupportedForIndexedModel(
            paginationFields, parameters, "", sortOrderItems);
        Assert.assertTrue(supported);
        
        // Test that mixed direction fails
        sortOrderItems.replace("values[Related Id]", "DESC");
        supported = KineticCoreAdapter.paginationSupportedForIndexedModel(
            paginationFields, parameters, "", sortOrderItems);
        Assert.assertFalse(supported);
        
        // Test that mismatched list sizes fails
        sortOrderItems.remove("values[Related Id]");
        supported = KineticCoreAdapter.paginationSupportedForIndexedModel(
            paginationFields, parameters, "", sortOrderItems);
        Assert.assertFalse(supported);
        
        // Test that index out of order fails
        sortOrderItems.clear();
        sortOrderItems.put("values[Related Id]","ASC");
        sortOrderItems.put("values[Status]","ASC");
        supported = KineticCoreAdapter.paginationSupportedForIndexedModel(
            paginationFields, parameters, "", sortOrderItems);
        Assert.assertFalse(supported);
    }
    
    @Test
    public void test_paginationSupported_1() throws Exception {
        List<String> paginationFields = new ArrayList<>();
        LinkedHashMap<String, String> sortOrderItems = new LinkedHashMap<>();
      
        String sortByValue = "createdAt";
        String sortByDirection = "DESC";
        paginationFields.add("createdAt");
        sortOrderItems.put(sortByValue,sortByDirection);
        String structure = "Submissions";
        Map parameters = new HashMap();
        
        // Test paginatable field is included in query string
        boolean supported = false;
        supported = KineticCoreAdapter.paginationSupportedForRestrictedModel(
            paginationFields, parameters, structure, sortOrderItems);
        Assert.assertTrue(supported);
        Assert.assertTrue(parameters.containsKey("timeline"));
        
        // Test that parameters have had the new key/values added
        if (parameters.containsKey("timeline") && parameters
            .containsKey("direction")) {
            
            Assert.assertTrue(parameters.get("timeline").equals(sortByValue));
            Assert.assertTrue(parameters.get("direction").equals(sortByDirection));
        } else {
            Assert.assertTrue(false);
        }
        
        // Test paginatable fields does not match sortOrderItems
        sortOrderItems.remove("createdAt");
        supported = KineticCoreAdapter.paginationSupportedForRestrictedModel(
            paginationFields, parameters, structure, sortOrderItems);
        Assert.assertFalse(supported);
    }
    /*--------------------------------------------------------------------------
    Temp Test for flattenNestedFields
    --------------------------------------------------------------------------*/
    JSONParser parser = new JSONParser();
    String forms = "[{"
        + "\"attributes\":"
            + "[{"
                + "\"name\":\"Icon\","
                + "\"values\":[\"fa-truck\"]},"
                + "{\"name\":\"Owning Team\","
                + "\"values\":[\"Facilities\"]"
            + "}],"
        + "\"name\":\"Cleaning\""
        + "\"slug\":\"cleaning\""
    + "}]";
    
    String submissions = "[{"
        + "\"coreState\":\"Draft\","
        + "\"createdBy\":\"joe.bar@foo.com\","
        + "\"id\":\"3250911c-5afc-11e9-bf69-29dd7c482cf1\","
        + "\"values\":{"
            + "\"Status\":\"Draft\","
            + "\"Requested For\":\"joe.bar@foo.com\""
        + "}"
    + "}]";
    
    String users = "[{"
        + "\"attributesMap\": {"
            + "\"Organization\": ["
                + "\"Architecture & Planning\""
            + "]," 
        + "},"
        + "\"displayName\": \"Aaliyah Wisoky\","
        + "\"email\": \"aaliyah.wisoky@rogahnlarkin.org\","
        + "\"spaceAdmin\": false,"
        + "\"timezone\": null,"
        + "\"username\": \"aaliyah.wisoky@rogahnlarkin.org\""
    + "}]";
    
    @Test
    public void test_form() throws Exception {

        List<String> fields = new ArrayList();
        fields.add("name");
        fields.add("attributes[Icon]");
        fields.add("attributes");
        
        KineticCoreAdapter helper = new KineticCoreAdapter();
       
        Object obj = parser.parse(forms);
        JSONArray array = (JSONArray)obj;
        
        Map<String,Object> mockRecordMap = new LinkedHashMap<String,Object>();
        mockRecordMap.put("attributes[Icon]", "[\"fa-truck\"]");
        mockRecordMap.put("name", "Cleaning");
        mockRecordMap.put("attributes", "[{"
                + "\"name\":\"Icon\","
                + "\"values\":[\"fa-truck\"]},"
                + "{\"name\":\"Owning Team\","
                + "\"values\":[\"Facilities\"]"
            + "}]");
        Record mockRecord = new Record(mockRecordMap);
        
        List<Record> mockRecords = new ArrayList<Record>();
        mockRecords.add(mockRecord);
        
        List<Record> records = helper.createRecords(fields, array);
        
//       assertEquals(records, mockRecords);
    }
    
    @Test
    public void test_submission() throws Exception {

        List<String> fields = new ArrayList();
        fields.add("id");
        fields.add("values[Status]");
        
        KineticCoreAdapter helper = new KineticCoreAdapter();
       
        Object obj = parser.parse(submissions);
        JSONArray array = (JSONArray)obj;
        
        Map<String,Object> mockRecordMap = new LinkedHashMap<String,Object>();
        mockRecordMap.put("id", "3250911c-5afc-11e9-bf69-29dd7c482cf1");
        mockRecordMap.put("values[Status]", "Draft");
        Record mockRecord = new Record(mockRecordMap);
        
        List<Record> mockRecords = new ArrayList<Record>();
        mockRecords.add(mockRecord);
        
        List<Record> records = helper.createRecords(fields, array);
        int x = 1;
    }

    @Test
    public void test_user() throws Exception {

        List<String> fields = new ArrayList();
        fields.add("displayName");
        fields.add("attributesMap[Organization]");
        fields.add("spaceAdmin");
        fields.add("attributesMap");
        fields.add("timezone");
        
        KineticCoreAdapter helper = new KineticCoreAdapter();
       
        Object obj = parser.parse(users);
        JSONArray array = (JSONArray)obj;
        
        Map<String,Object> mockRecordMap = new LinkedHashMap<String,Object>();
        mockRecordMap.put("displayName", "Aaliyah Wisoky");
        mockRecordMap.put("attributesMap[Organization]", "Architecture & Planning");
        mockRecordMap.put("spaceAdmin", "false");
        mockRecordMap.put("attributesMap", "{"
                + "\"Organization\": ["
                    + "\"Architecture & Planning\""
                + "]," 
            + "},");
        mockRecordMap.put("timezone", "null");
        Record mockRecord = new Record(mockRecordMap);
        
        List<Record> mockRecords = new ArrayList<Record>();
        mockRecords.add(mockRecord);
        
        List<Record> records = helper.createRecords(fields, array);
        int x = 1;
    }
}
