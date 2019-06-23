package org.openmrs.module.patientsearch.api.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.HibernatePatientDAO;
import org.openmrs.api.db.hibernate.HibernatePersonDAO;
import org.openmrs.api.db.hibernate.PersonLuceneQuery;
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
	public List<Patient> getPatients(String gender, Integer start, Integer length, Boolean includeVoided)
			throws DAOException {
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

		PatientLuceneQuery patientLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> genderQuery = patientLuceneQuery.getPatinetWithGender(gender, includeVoided);
		long namesSize = genderQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> patientGenders = genderQuery.listPartProjection(tmpStart, tmpLength, "personId");
			patientGenders.getList().forEach(patient -> patients.add(getPatient((Integer) patient[0])));
		} 

		return patients;
	}
	
	@Override
	public List<Patient> getPatients(Date from, Date to, Integer start, Integer length, Boolean includeVoided)
			throws DAOException {
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

		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> ageQuery = personLuceneQuery.getPatinetWithAgeRange(from, to, includeVoided);
		
		long namesSize = ageQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> patientAge = ageQuery.listPartProjection(tmpStart, tmpLength, "personId");
			patientAge.getList().forEach(patient -> patients.add(getPatient((Integer) patient[0])));
		} 

		return patients;
	}
	
	@Override
	public List<Patient> getPatients(Date birthdate, Integer start, Integer length, Boolean includeVoided)
			throws DAOException {
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

		
		
		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> birthdateQuery = personLuceneQuery.getPatinetWithBirthdate(birthdate, includeVoided);
		
		long namesSize = birthdateQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> patientBirthdate = birthdateQuery.listPartProjection(tmpStart, tmpLength, "personId");
			patientBirthdate.getList().forEach(patient -> patients.add(getPatient((Integer) patient[0])));
		} 

		return patients;
	}
	
	@Override
	public List<Patient> getPatients(String query, String gender, Integer start, Integer length, Boolean includeVoided)
	        throws DAOException {
		List<Patient> patients = findPatients(query, includeVoided, start, length);
		List<Patient> result=new LinkedList<>();
		for (Patient p : patients) {
			if (p.getGender().equals(gender)) {
				result.add(p);
			}
		}
		return result;
	}
	
	@Override
	public List<Patient> getPatients(String query, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		Integer tmpStart = start;
		if (tmpStart == null) {
			tmpStart = 0;
		}
		Integer maxLength = HibernatePersonDAO.getMaximumSearchResults();
		Integer tmpLength = length;
		if (tmpLength == null || tmpLength > maxLength) {
			tmpLength = maxLength;
		}
		query = LuceneQuery.escapeQuery(query);

		List<Patient> patients = new LinkedList<>();

		String minChars = Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_MIN_SEARCH_CHARACTERS);

		if (minChars == null || !StringUtils.isNumeric(minChars)) {
			minChars = "" + OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_MIN_SEARCH_CHARACTERS;
		}
		if (query.length() < Integer.valueOf(minChars)) {
			return patients;
		}
		
		PatientLuceneQuery patientLuceneQuery=new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> ageQuery=patientLuceneQuery.getPatinetWithAgeRange(from, to, includeVoided);
		List<Integer> tmp=new ArrayList<>();
		List<Object[]> tmpPatientId=ageQuery.listProjection("personId");
		tmpPatientId.forEach(id->tmp.add((Integer)id[0]));

		LuceneQuery<PatientIdentifier> identifierQuery = getPatientIdentifierLuceneQuery(query, includeVoided, false);

		long identifiersSize = identifierQuery.resultSize();
		if (identifiersSize > tmpStart) {
			ListPart<Object[]> patientIdentifiers = identifierQuery.listPartProjection(tmpStart, tmpLength, "patient.personId");
			patientIdentifiers.getList().forEach(patientIdentifier -> {if(tmp.contains((Integer)patientIdentifier[0])) patients.add(getPatient((Integer)patientIdentifier[0]));});

			tmpLength -= patientIdentifiers.getList().size();
			tmpStart = 0;
		} else {
			tmpStart -= (int) identifiersSize;
		}

		if (tmpLength == 0) {
			return patients;
		}

		PersonLuceneQuery personLuceneQuery = new PersonLuceneQuery(sessionFactory);

		LuceneQuery<PersonName> nameQuery = personLuceneQuery.getPatientNameQuery(query, includeVoided, identifierQuery);
		
		long namesSize = nameQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> personNames = nameQuery.listPartProjection(tmpStart, tmpLength, "person.personId");
			personNames.getList().forEach(personName ->{if(tmp.contains((Integer)personName[0])) patients.add(getPatient((Integer) personName[0]));});
			tmpLength -= personNames.getList().size();
			tmpStart = 0;
		} 
		
		return patients;

	}
	
	@Override
	public List<Patient> getPatients(String query,Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		Integer tmpStart = start;
		if (tmpStart == null) {
			tmpStart = 0;
		}
		Integer maxLength = HibernatePersonDAO.getMaximumSearchResults();
		Integer tmpLength = length;
		if (tmpLength == null || tmpLength > maxLength) {
			tmpLength = maxLength;
		}
		query = LuceneQuery.escapeQuery(query);

		List<Patient> patients = new LinkedList<>();

		String minChars = Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_MIN_SEARCH_CHARACTERS);

		if (minChars == null || !StringUtils.isNumeric(minChars)) {
			minChars = "" + OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_MIN_SEARCH_CHARACTERS;
		}
		if (query.length() < Integer.valueOf(minChars)) {
			return patients;
		}
		
		PatientLuceneQuery patientLuceneQuery=new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> birthdateQuery=patientLuceneQuery.getPatinetWithBirthdate(birthdate, includeVoided);
		List<Integer> tmp=new ArrayList<>();
		List<Object[]> tmpPatientId=birthdateQuery.listProjection("personId");
		tmpPatientId.forEach(id->tmp.add((Integer)id[0]));
		LuceneQuery<PatientIdentifier> identifierQuery = getPatientIdentifierLuceneQuery(query, includeVoided, false);

		long identifiersSize = identifierQuery.resultSize();
		if (identifiersSize > tmpStart) {
			ListPart<Object[]> patientIdentifiers = identifierQuery.listPartProjection(tmpStart, tmpLength, "patient.personId");
			patientIdentifiers.getList().forEach(patientIdentifier -> {if(tmp.contains((Integer)patientIdentifier[0])) patients.add(getPatient((Integer)patientIdentifier[0]));});

			tmpLength -= patientIdentifiers.getList().size();
			tmpStart = 0;
		} else {
			tmpStart -= (int) identifiersSize;
		}

		if (tmpLength == 0) {
			return patients;
		}

		PersonLuceneQuery personLuceneQuery = new PersonLuceneQuery(sessionFactory);

		LuceneQuery<PersonName> nameQuery = personLuceneQuery.getPatientNameQuery(query, includeVoided, identifierQuery);
		
		long namesSize = nameQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> personNames = nameQuery.listPartProjection(tmpStart, tmpLength, "person.personId");
			personNames.getList().forEach(personName ->{if(tmp.contains((Integer)personName[0])) patients.add(getPatient((Integer) personName[0]));});
			tmpLength -= personNames.getList().size();
			tmpStart = 0;
		} 
		return patients;

	}
	
	@Override
	public List<Patient> getPatients(String query, String gender, Date from, Date to, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		List<Patient> patients = getPatients(query, from, to, start, length, includeVoided);
		List<Patient> result= new LinkedList<>();
		for (Patient p : patients) {
			if (p.getGender().equals(gender)) {
				result.add(p);
			}
		}
		return result;
	}
	
	@Override
	public List<Patient> getPatients(String query, String gender, Date birthdate, Integer start, Integer length,
	        Boolean includeVoided) throws DAOException {
		List<Patient> patients = getPatients(query, birthdate, start, length, includeVoided);
		List<Patient> result=new LinkedList<>();
		for (Patient p : patients) {
			if (p.getGender().equals(gender)) {
				result.add(p);
			}
		}
		return result;
	}
	
	@Override
	public List<Patient> getPatientsByGenderAndBirthdate(String gender, Date birthdate, Integer start, Integer length,
			Boolean includeVoided) throws DAOException {
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
		List<Patient> result=new LinkedList<>();
		
		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> birthdateQuery = personLuceneQuery.getPatinetWithBirthdate(birthdate, includeVoided);
		long namesSize = birthdateQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> patientNames = birthdateQuery.listPartProjection(tmpStart, tmpLength, "personId");
			patientNames.getList().forEach(patientName -> patients.add(getPatient((Integer) patientName[0])));
			for(Patient p:patients) {
				if(p.getGender().equals(gender)) {
					result.add(p);
				}
			}
		} 
		
		return result;
	}
	
	@Override
	public List<Patient> getPatientsByGenderAndAge(String gender, Date from, Date to, Integer start, Integer length,
			Boolean includeVoided) throws DAOException {
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
		List<Patient> result=new LinkedList<>();
		PatientLuceneQuery personLuceneQuery = new PatientLuceneQuery(sessionFactory);
		LuceneQuery<Person> ageQuery = personLuceneQuery.getPatinetWithAgeRange(from, to, includeVoided);
		long namesSize = ageQuery.resultSize();
		if (namesSize > tmpStart) {
			ListPart<Object[]> patientNames = ageQuery.listPartProjection(tmpStart, tmpLength, "personId");
			patientNames.getList().forEach(patientName -> patients.add(getPatient((Integer) patientName[0])));
			for(Patient p:patients) {
				if(p.getGender().equals(gender)) {
					result.add(p);
				}
			}
		} 

		return result;
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
