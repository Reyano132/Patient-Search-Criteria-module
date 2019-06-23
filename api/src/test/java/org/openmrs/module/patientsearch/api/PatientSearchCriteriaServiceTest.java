package org.openmrs.module.patientsearch.api;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.patientsearch.api.PatientSearchCriteriaService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.beans.factory.annotation.Autowired;

public class PatientSearchCriteriaServiceTest extends BaseModuleContextSensitiveTest {
	
	private final static String PATIENTS_XML = "org/openmrs/module/patientsearch/api/include/PatientSearchCriteriaServiceTest-patients.xml";
	
	@Autowired
	private PatientSearchCriteriaService patientservice;
	
	@Before
	public void runBeforeEachTest() {
		try {
			initializeInMemoryDatabase();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		executeDataSet(PATIENTS_XML);
		updateSearchIndex();
		authenticate();
	}
	
	@Test
	@SkipBaseSetup
	public void getPatientsByGender() {
		List<Patient> patients = patientservice.getPatients(null, null, null, true, "F", null, null, null);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("F", patients.get(0).getGender());
	}
	
	@Test
	public void getPatientByBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = patientservice.getPatients(null, null, null, false, null, null, null, birthdate.getTime());
		Assert.assertEquals(2, patients.size());
	}
	
	@Test
	public void getPatientByAgeRange() {
		List<Patient> patients = patientservice.getPatients(null, null, null, false, null, 3, 10, null);
		Assert.assertEquals(3, patients.size());
	}
	
	@Test
	public void getPatientByGenderAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = patientservice.getPatients(null, null, null, false, "M", null, null, birthdate.getTime());
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("M", patients.get(0).getGender());
	}
	
	@Test
	public void getPatientByGenderAndAgeRange() {
		List<Patient> patients = patientservice.getPatients(null, null, null, false, "F", 3, 10, null);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("F", patients.get(0).getGender());
		
	}
	
	@Test
	public void getPatientByNameAndGender() {
		List<Patient> patients = patientservice.getPatients("Frank", null, null, true, "M", null, null, null);
		Assert.assertEquals(3, patients.size());
	}
	
	@Test
	public void getPatientByPatientIdentifierAndGender() {
		List<Patient> patients = patientservice.getPatients(null, "82-82-82", null, true, null, null, null, null);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals(82, patients.get(0).getId().longValue());
		Assert.assertEquals("M", patients.get(0).getGender());
	}
	
	@Test
	public void getPatientByNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = patientservice
		        .getPatients("Adam", null, null, false, null, null, null, birthdate.getTime());
		Assert.assertEquals(1, patients.size());
	}
	
	@Test
	public void getPatientByNameAndAgeRange() {
		List<Patient> patients = patientservice.getPatients("Frank", null, null, false, null, 3, 10, null);
		Assert.assertEquals(3, patients.size());
	}
	
	@Test
	public void getPatientByNameAndGenderAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = patientservice
		        .getPatients("Frank", null, null, false, "F", null, null, birthdate.getTime());
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
	@Test
	public void getPatientByNameAndGenderAndAgeRange() {
		List<Patient> patients = patientservice.getPatients("Frank", null, null, false, "F", 3, 10, null);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
}
