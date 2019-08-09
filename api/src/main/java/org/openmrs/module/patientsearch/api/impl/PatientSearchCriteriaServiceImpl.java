/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientsearch.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.PatientServiceImpl;
import org.openmrs.module.patientsearch.api.PatientSearchCriteriaService;
import org.openmrs.module.patientsearch.api.dao.PatientSearchCriteriaDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * All the methods of PatientSearchCriteriaService implemented here
 * 
 * @see org.openmrs.module.patientcriteria.api.PatientSearchCriteriaService
 **/

@Transactional
public class PatientSearchCriteriaServiceImpl extends PatientServiceImpl implements PatientSearchCriteriaService {
	
	PatientSearchCriteriaDAO dao;
	
	public void setDao(PatientSearchCriteriaDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Patient> getPatients(String name, String identifier, List<PatientIdentifierType> identifierTypes,
	        boolean matchIdentifierExactly, String gender, Integer from, Integer to, Date birthdate) throws APIException {
		if (identifierTypes == null) {
			Date fromDate = null;
			Date toDate = null;
			
			if (to != null && from != null) {
				Calendar today = Calendar.getInstance();
				fromDate = new GregorianCalendar(today.get(Calendar.YEAR) - from, 0, 1).getTime();
				toDate = new GregorianCalendar(today.get(Calendar.YEAR) - to, 11, 31).getTime();
			}
			
			if (gender == null && to == null && from == null && birthdate == null) {
				return dao.getPatients(name != null ? name : identifier, 0, null);
			}
			
			else if (gender != null && to == null && from == null && birthdate == null) {
				if (name == null && identifier == null) {
					return dao.getPatientsByGender(gender, 0, null, false);
				}
				return dao.getPatientsByNameOrIdAndGender(name != null ? name : identifier, gender, 0, null, false);
			}
			
			else if (name == null && identifier == null && gender == null && to != null && from != null && birthdate == null) {
				return dao.getPatientsByRangeOfAge(fromDate, toDate, 0, null, false);
			}
			
			else if (name == null && identifier == null && gender == null && to == null && from == null && birthdate != null) {
				return dao.getPatientsByBirthdate(birthdate, 0, null, false);
			}
			
			else {
				return getPatientsBySeachCriteria(name, identifier, identifierTypes, gender, fromDate, toDate, birthdate);
			}
			
		} else {
			return dao.getPatients(name != null ? name : identifier, identifierTypes, matchIdentifierExactly, 0, null);
		}
		
	}
	
	private List<Patient> getPatientsBySeachCriteria(String name, String identifier,
	        List<PatientIdentifierType> identifierTypes, String gender, Date from, Date to, Date birthdate) {
		if (name == null && identifier == null) {
			if (birthdate == null) {
				return dao.getPatientsByGenderAndAge(gender, from, to, 0, null, false);
			} else {
				return dao.getPatientsByGenderAndBirthdate(gender, birthdate, 0, null, false);
			}
		}
		if (gender == null) {
			if (birthdate == null) {
				return dao.getPatientsByNameOrIdAndRangeOfAge(name != null ? name : identifier, from, to, 0, null, false);
			} else {
				return dao.getPatientsByNameOrIdAndBirthdate(name != null ? name : identifier, birthdate, 0, null, false);
			}
		}
		
		if (birthdate == null) {
			return dao.getPatientsByNameOrIdAndGenderAndRangeOfAge(name != null ? name : identifier, gender, from, to, 0,
			    null, false);
		} else {
			return dao.getPatientsByNameOrIdAndGenderAndBirthdate(name != null ? name : identifier, gender, birthdate, 0,
			    null, false);
		}
	}
	
}
