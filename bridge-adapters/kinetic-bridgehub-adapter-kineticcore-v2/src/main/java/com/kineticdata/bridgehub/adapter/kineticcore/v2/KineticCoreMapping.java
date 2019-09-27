/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kineticdata.bridgehub.adapter.kineticcore.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
  * This class is used to define valid Structures.
  *  Properties:
  *      String structure - name of a model of data.
  *      String plural - property name accessor when multiple results returned
  *      String plural - property name accessor when single result returned
  *      Set<String> implicitIncludes - additions placed on the parameters of
  *          the request to source system.
  *      PaginationPredicate paginationPredicate - method called to determine
  *          if request may be paginated server side.
  */
public class KineticCoreMapping {
    private final String structure;
    private final String plural;
    private final String singular;
    private final Set<String> implicitIncludes;
    private List<String> paginationFields;
    private PaginationPredicate paginationPredicate;

    public KineticCoreMapping(String structure, String plural, String singular, 
        Collection<String> implicitIncludes, 
        PaginationPredicate paginationPredicate) {

        this.structure = structure;
        this.plural = plural;
        this.singular = singular;
        this.implicitIncludes = new LinkedHashSet<>(implicitIncludes);
        this.paginationPredicate = paginationPredicate;
    }

    public KineticCoreMapping(String structure, String plural, String singular, 
        Collection<String> implicitIncludes, 
        Collection<String> paginationFields,
        PaginationPredicate paginationPredicate) {

        this.structure = structure;
        this.plural = plural;
        this.singular = singular;
        this.implicitIncludes = new LinkedHashSet<>(implicitIncludes);
        this.paginationFields = new ArrayList<>(paginationFields);
        this.paginationPredicate = paginationPredicate;
    }

    public String getStructure() {
        return structure;
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public Set<String> getImplicitIncludes() {
        return implicitIncludes;
    }

    public List<String> getPaginationFields() {
        return paginationFields;
    }

    public PaginationPredicate getPaginationPredicate() {
        return paginationPredicate;
    }
}
