/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import com.kineticdata.bridgehub.adapter.Record;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author chad.rehm
 */
public class KineticCoreComparator implements Comparator<Record> {

  private Map<String, String> sortOrderItems;

  public KineticCoreComparator(Map<String, String> sortOrderItems) {
    this.sortOrderItems = sortOrderItems;
  }

  public int compare(Record r1, Record r2) {
    int result = 0;

    for (Map.Entry<String,String> sortOrderItem : sortOrderItems.entrySet()) {
        String fieldName = sortOrderItem.getKey();
        boolean isAscending = "asc".equals(sortOrderItem.getValue().toLowerCase());
        
        String r1Value = normalize(r1.getValue(fieldName));
        String r2Value = normalize(r2.getValue(fieldName));
        
        // Order based on field direction specified
        int fieldComparison = (isAscending)
            ? r1Value.compareTo(r2Value)
            : r2Value.compareTo(r1Value);
        if (fieldComparison != 0) {
            result = fieldComparison;
            break;
        }
    }

    return result;
  }

  protected String normalize(Object string) {
    String result;
    if (String.valueOf(string) == null) {
      result = "";
    } else {
      result = String.valueOf(string);
    }
    return result;
  }

}
