package org.openmrs.module.patientsearch.api.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Person;
import org.openmrs.api.db.hibernate.search.LuceneQuery;
import org.openmrs.module.patientsearch.api.dao.hibernate.search.DateLuceneQuery;

/*
 
 Provides the lucene query for the patient search criteria

 */
public class PatientLuceneQuery {
	
	SessionFactory sessionFactory;
	
	public PatientLuceneQuery(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	//returns the lucene query for searching patients with gender
	public LuceneQuery<Person> getPatinetWithGender(String query) {
		return findPatientWithGender(query, true, null);
	}
	
	public LuceneQuery<Person> getPatinetWithGender(String query, boolean includeVoided) {
		return findPatientWithGender(query, includeVoided, null);
	}
	
	public LuceneQuery<Person> getPatinetWithGender(String query, LuceneQuery<?> skipSame) {
		return findPatientWithGender(query, true, skipSame);
	}
	
	public LuceneQuery<Person> getPatinetWithGender(String query, boolean includeVoided, LuceneQuery<?> skipSame) {
		return findPatientWithGender(query, includeVoided, skipSame);
	}
	
	private LuceneQuery<Person> findPatientWithGender(String query,boolean includeVoided,LuceneQuery<?> skipSame){
		List<String> fields=new ArrayList<>();
		fields.add("gender");
		
		LuceneQuery<Person> luceneQuery = LuceneQuery
				.newQuery(Person.class, sessionFactory.getCurrentSession(), query, fields);
		if (!includeVoided) {
			luceneQuery.include("voided", false);
		}
		
		if (skipSame != null) {
			luceneQuery.skipSame("personId", skipSame);
		} else {
			luceneQuery.skipSame("personId");
		}
		luceneQuery.include("isPatient", true);
		
		return luceneQuery;
		
	}
	
	//returns the lucene query for searching patients with birthdate
	public LuceneQuery<Person> getPatinetWithBirthdate(Date query) {
		return findPatientWithBirthdate(query, true, null);
	}
	
	public LuceneQuery<Person> getPatinetWithBirthdate(Date query, boolean includeVoided) {
		return findPatientWithBirthdate(query, includeVoided, null);
	}
	
	public LuceneQuery<Person> getPatinetWithBirthdate(Date query, LuceneQuery<?> skipSame) {
		return findPatientWithBirthdate(query, true, skipSame);
	}
	
	public LuceneQuery<Person> getPatinetWithBirthdate(Date query, boolean includeVoided, LuceneQuery<?> skipSame) {
		return findPatientWithBirthdate(query, includeVoided, skipSame);
	}
	
	private LuceneQuery<Person> findPatientWithBirthdate(Date query, boolean includeVoided, LuceneQuery<?> skipSame) {
		String field = "birthdate";
		
		DateLuceneQuery<Person> luceneQuery = DateLuceneQuery.newQuery(Person.class, sessionFactory.getCurrentSession(),
		    query, field);
		if (!includeVoided) {
			luceneQuery.include("voided", false);
		}
		
		if (skipSame != null) {
			luceneQuery.skipSame("personId", skipSame);
		} else {
			luceneQuery.skipSame("personId");
		}
		luceneQuery.include("isPatient", true);
		return luceneQuery;
		
	}
	
	//returns the lucene query for searching patients with range of age
	public LuceneQuery<Person> getPatinetWithAgeRange(Date from, Date to) {
		return findPatientWithAgeRange(from, to, true, null);
	}
	
	public LuceneQuery<Person> getPatinetWithAgeRange(Date from, Date to, boolean includeVoided) {
		return findPatientWithAgeRange(from, to, includeVoided, null);
	}
	
	public LuceneQuery<Person> getPatinetWithAgeRange(Date from, Date to, LuceneQuery<?> skipSame) {
		return findPatientWithAgeRange(from, to, true, skipSame);
	}
	
	public LuceneQuery<Person> getPatinetWithAgeRange(Date from, Date to, boolean includeVoided, LuceneQuery<?> skipSame) {
		return findPatientWithAgeRange(from, to, includeVoided, skipSame);
	}
	
	private LuceneQuery<Person> findPatientWithAgeRange(Date from, Date to, boolean includeVoided, LuceneQuery<?> skipSame) {
		String field = "birthdate";
		
		DateLuceneQuery<Person> luceneQuery = DateLuceneQuery.newQuery(Person.class, sessionFactory.getCurrentSession(),
		    from, to, field);
		if (!includeVoided) {
			luceneQuery.include("voided", false);
		}
		
		if (skipSame != null) {
			luceneQuery.skipSame("personId", skipSame);
		} else {
			luceneQuery.skipSame("personId");
		}
		luceneQuery.include("isPatient", true);
		return luceneQuery;
		
	}
	
}
