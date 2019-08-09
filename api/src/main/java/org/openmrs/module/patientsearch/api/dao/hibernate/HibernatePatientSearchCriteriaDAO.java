package org.openmrs.module.patientsearch.api.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.HibernatePatientDAO;
import org.openmrs.api.db.hibernate.HibernatePersonDAO;
import org.openmrs.api.db.hibernate.search.LuceneQuery;
import org.openmrs.collection.ListPart;
import org.openmrs.module.patientsearch.api.dao.PatientSearchCriteriaDAO;
import org.openmrs.util.OpenmrsConstants;

/**
 * With help of Hibernate API , implement the methods of PatientSearchCriteriaDAO
 * 
 * @see org.openmrs.module.patientcriteria.api.dao.PatientSearchCriteriaDao
 */

public class HibernatePatientSearchCriteriaDAO extends HibernatePatientDAO implements PatientSearchCriteriaDAO {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<Patient> getPatientsByGender(String gender, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException {
		
		PatientLuceneQuery patientLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> genderQuery = patientLuceneQuery.getPatinetWithGender(gender, includeVoided);
		return getPatientsWithLuceneQuery(genderQuery, start, length);
	}
	
	@Override
	public List<Patient> getPatientsByRangeOfAge(Date from, Date to, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException {
		
		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> ageQuery = personLuceneQuery.getPatinetWithAgeRange(from, to, includeVoided);
		return getPatientsWithLuceneQuery(ageQuery, start, length);
	}
	
	@Override
	public List<Patient> getPatientsByBirthdate(Date birthdate, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException {
		
		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> birthdateQuery = personLuceneQuery.getPatinetWithBirthdate(birthdate, includeVoided);
		return getPatientsWithLuceneQuery(birthdateQuery, start, length);
	}
	
	@Override
	public List<Patient> getPatientsByNameOrIdAndGender(String query, String gender, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException {
		return findPatients(query, includeVoided, start, length).stream().filter(patient -> patient.getGender().equals(gender)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByNameOrIdAndRangeOfAge(String query, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		
		PatientLuceneQuery patientLuceneQuery=new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> ageQuery=patientLuceneQuery.getPatinetWithAgeRange(from, to, includeVoided);
		List<Patient> temp=getPatientsWithLuceneQuery(ageQuery,start,length);
		return findPatients(query, includeVoided, start, length).stream().filter(patient -> temp.contains(patient)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByNameOrIdAndBirthdate(String query,Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		
		PatientLuceneQuery patientLuceneQuery=new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> birthdateQuery=patientLuceneQuery.getPatinetWithBirthdate(birthdate, includeVoided);
		List<Patient> temp=getPatientsWithLuceneQuery(birthdateQuery,start,length);
		return findPatients(query, includeVoided, start, length).stream().filter(patient -> temp.contains(patient)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByNameOrIdAndGenderAndRangeOfAge(String query, String gender, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		return getPatientsByNameOrIdAndRangeOfAge(query, from, to, start, length, includeVoided).stream().filter(patient -> patient.getGender().equals(gender)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByNameOrIdAndGenderAndBirthdate(String query, String gender, Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {	
		return getPatientsByNameOrIdAndBirthdate(query, birthdate, start, length, includeVoided).stream().filter(patient -> patient.getGender().equals(gender)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByGenderAndBirthdate(String gender, Date birthdate, Integer start, Integer length,
			Boolean includeVoided) throws DAOException {
		return getPatientsByBirthdate(birthdate, start, length, includeVoided).stream().filter(patient -> patient.getGender().equals(gender)).collect(Collectors.toList());
	}
	
	@Override
	public List<Patient> getPatientsByGenderAndAge(String gender, Date from, Date to, Integer start, Integer length,
			Boolean includeVoided) throws DAOException {
		return getPatientsByRangeOfAge(from, to, start, length, includeVoided).stream().filter(patient -> patient.getGender().equals(gender)).collect(Collectors.toList());
	}
	
	private List<Patient> getPatientsWithLuceneQuery(LuceneQuery<Person> query, Integer start, Integer length){
		Integer tmpStart = start;
		if (tmpStart == null) {
			tmpStart = 0;
		}
		Integer maxLength = HibernatePersonDAO.getMaximumSearchResults();
		Integer tmpLength = length;
		if (tmpLength == null || tmpLength > maxLength) {
			tmpLength = maxLength;
		}

		List<Patient> patients = new LinkedList<>();
		
		long querySize = query.resultSize();
		if (querySize > tmpStart) {
			ListPart<Object[]> tempPatients = query.listPartProjection(tmpStart, tmpLength, "personId");
			tempPatients.getList().forEach(patient -> patients.add(getPatient((Integer) patient[0])));
		} 
		
		return patients;
		
		
	}
	
	//Below methods will be removed at time of merging with openmrs core. 
	
	@SuppressWarnings("unused")
	private LuceneQuery<PatientIdentifier> getPatientIdentifierLuceneQuery(String query,
	        List<PatientIdentifierType> identifierTypes, boolean matchExactly) {
		LuceneQuery<PatientIdentifier> patientIdentifierLuceneQuery = getPatientIdentifierLuceneQuery(query, matchExactly);
		for (PatientIdentifierType identifierType : identifierTypes) {
			patientIdentifierLuceneQuery.include("identifierType.patientIdentifierTypeId", identifierType.getId());
		}
		patientIdentifierLuceneQuery.include("patient.isPatient", true);
		patientIdentifierLuceneQuery.skipSame("patient.personId");
		
		return patientIdentifierLuceneQuery;
	}
	
	private LuceneQuery<PatientIdentifier> getPatientIdentifierLuceneQuery(String paramQuery, boolean matchExactly) {
		String query = removeIdentifierPadding(paramQuery);
		List<String> tokens = tokenizeIdentifierQuery(query);
		query = StringUtils.join(tokens, " OR ");
		List<String> fields = new ArrayList<>();
		fields.add("identifierPhrase");
		fields.add("identifierType");
		String matchMode = Context.getAdministrationService()
			.getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_IDENTIFIER_SEARCH_MATCH_MODE);
		if (matchExactly) {
			fields.add("identifierExact");
		}
		else if (OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_SEARCH_MATCH_START.equals(matchMode)) {
			fields.add("identifierStart");
		} 
		else  {
			fields.add("identifierAnywhere");
		}
		return LuceneQuery.newQuery(PatientIdentifier.class, sessionFactory.getCurrentSession(), query, fields);
	
	}	private LuceneQuery<PatientIdentifier> getPatientIdentifierLuceneQuery(String query, boolean includeVoided,
	        boolean matchExactly) {
		LuceneQuery<PatientIdentifier> luceneQuery = getPatientIdentifierLuceneQuery(query, matchExactly);
		if (!includeVoided) {
			luceneQuery.include("voided", false);
			luceneQuery.include("patient.voided", false);
		}
		
		luceneQuery.include("patient.isPatient", true);
		luceneQuery.skipSame("patient.personId");
		
		return luceneQuery;
	}
	
	private String removeIdentifierPadding(String query) {
		String regex = Context.getAdministrationService().getGlobalProperty(
		    OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_IDENTIFIER_REGEX, "");
		if (Pattern.matches("^\\^.{1}\\*.*$", regex)) {
			String padding = regex.substring(regex.indexOf("^") + 1, regex.indexOf("*"));
			Pattern pattern = Pattern.compile("^" + padding + "+");
			query = pattern.matcher(query).replaceFirst("");
		}
		return query;
	}
	
	private List<String> tokenizeIdentifierQuery(String query) {
		List<String> searchPatterns = new ArrayList<>();

		String patternSearch = Context.getAdministrationService().getGlobalProperty(
				OpenmrsConstants.GLOBAL_PROPERTY_PATIENT_IDENTIFIER_SEARCH_PATTERN, "");

		if (StringUtils.isBlank(patternSearch)) {
			searchPatterns.add(query);
		} else {
			// split the pattern before replacing in case the user searched on a comma
			// replace the @SEARCH@, etc in all elements
			for (String pattern : patternSearch.split(",")) {
				searchPatterns.add(replaceSearchString(pattern, query));
			}
		}
		return searchPatterns;
	}
	
	private String replaceSearchString(String regex, String identifierSearched) {
		String returnString = regex.replaceAll("@SEARCH@", identifierSearched);
		if (identifierSearched.length() > 1) {
			// for 2 or more character searches, we allow regex to use last character as check digit
			returnString = returnString.replaceAll("@SEARCH-1@",
			    identifierSearched.substring(0, identifierSearched.length() - 1));
			returnString = returnString.replaceAll("@CHECKDIGIT@",
			    identifierSearched.substring(identifierSearched.length() - 1));
		} else {
			returnString = returnString.replaceAll("@SEARCH-1@", "");
			returnString = returnString.replaceAll("@CHECKDIGIT@", "");
		}
		return returnString;
	}
	
}
