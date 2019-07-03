/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientsearch.api;

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.util.PrivilegeConstants;

/**
 * This provide patient search criteria service. It helps to retrieve patient with different fields
 * such as gender, birthdate, age.
 */
public interface PatientSearchCriteriaService extends PatientService {
	
	/**
	 * Return the list of patient which has required name or identifier or gender or birthdate or
	 * age.
	 * 
	 * @param name (optional) this is a slight break from the norm, patients with a partial match on
	 *            this name will be returned
	 * @param identifier (optional) only patients with a matching identifier are returned. This
	 *            however applies only if <code>name</code> argument is null. Otherwise, its
	 *            ignored.
	 * @param identifierTypes (optional) the PatientIdentifierTypes to restrict to
	 * @param matchIdentifierExactly (required) if true, then the given <code>identifier</code> must
	 *            equal the id in the database. if false, then the identifier is 'searched' for by
	 *            using a regular expression
	 * @param gender(optional) : if user want to search patient by gender or filter the search
	 *            result by gender. value of gender parameter is either "M" or "F".
	 * @param to(optional) : user wants to search patients having age between some range, at that
	 *            time this paramater represent the upper boundary of range.
	 * @param from(optional):user wants to search patients having age between some range, at that
	 *            time this paramater represent the lower boundary of range.
	 * @param birthdate(optional) : User can search patient by birthdate.
	 * @return patients that matched the given criteria (and are not voided)
	 * @throws APIException
	 * @should fetch all patients that partially match given name
	 * @should fetch all patients that partially match given identifier if <code>name</code>
	 *         argument is null
	 * @should fetch all patients that partially match given identifier when match identifier
	 *         exactly equals false and if <code>name</code> argument is null
	 * @should fetch all patients that exactly match given identifier when match identifier exactly
	 *         equals true and if <code>name</code> argument is null
	 * @should fetch all patients that match given identifier types
	 * @should not return duplicates
	 * @should not return voided patients
	 * @should return empty list when no match is found
	 * @should search familyName2 with name
	 * @should support simple regex
	 * @should support pattern using last digit as check digit
	 * @should return empty list if name and identifier is empty
	 * @should support all the search criteria
	 */
	@Authorized({ PrivilegeConstants.GET_PATIENTS })
	public List<Patient> getPatients(String name, String identifier, List<PatientIdentifierType> identifierTypes,
	        boolean matchIdentifierExactly, String gender, Integer from, Integer to, Date birthdate) throws APIException;
	
}
