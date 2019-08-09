/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientsearch.api.dao;

import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.PatientDAO;

/**
 * methods to search patients with different fields
 */
public interface PatientSearchCriteriaDAO extends PatientDAO {
	
	/**
	 * @param query : name or identifier of patients
	 * @param gender : gender of patients
	 * @param length : maximum number of patients should return
	 * @return : list of patients who follow the query regex and having required gender
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByNameOrIdAndGender(String query, String gender, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException;
	
	/**
	 * @return : list of patient having required gender
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByGender(String gender, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException;
	
	/**
	 * @param from: lower boundary of range of age
	 * @param to : upper boundary of range of age
	 * @return list of patients, who's age is in between the required range
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByRangeOfAge(Date from, Date to, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException;
	
	/**
	 * @param birthdate : birthdate of patient
	 * @return list of patient/s , who's birthdate is similar to required birthdate
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByBirthdate(Date birthdate, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException;
	
	/**
	 * @param query : name or identifier of patient/s.
	 * @param gender : gender of patient
	 * @param from : lower boundary of range of age
	 * @param to : upper boundary of range of age
	 * @return list of patients who follow the query regex and having required gender and having age
	 *         in required range
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByNameOrIdAndGenderAndRangeOfAge(String query, String gender, Date from, Date to,
	        Integer start, Integer length, Boolean includeVoided) throws DAOException;
	
	/**
	 * @param query : name or identifier of patient/s
	 * @param gender : gender of patient
	 * @param birthdate : birthdate of patient
	 * @return list of patients who follow the query regex and having required gender and having
	 *         required birthdate
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByNameOrIdAndGenderAndBirthdate(String query, String gender, Date birthdate,
	        Integer start, Integer length, Boolean includeVoided) throws DAOException;
	
	/**
	 * @param from: lower boundary of range of age
	 * @param to : upper boundary of range of age
	 * @return list of patients who follow the query regex and having age in required range
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByNameOrIdAndRangeOfAge(String query, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException;
	
	/**
	 * @param birthdate: birthdate of patient
	 * @return list of patients who follow the query regex and having birthdate as required.
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByNameOrIdAndBirthdate(String query, Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException;
	
	/**
	 * @param gender : gender of patients
	 * @param birthdate: birthdate of patient
	 * @return list of patients which have required birthdate and gender
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByGenderAndBirthdate(String gender, Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException;
	
	/**
	 * @param gender : gender of patients
	 * @param from: lower boundary of range of age
	 * @param to : upper boundary of range of age
	 * @return list of patients which have required gender and age in required range.
	 * @throws DAOException
	 */
	public List<Patient> getPatientsByGenderAndAge(String gender, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException;
	
}
