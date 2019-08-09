package org.openmrs.module.patientsearch.api.dao;

import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.patientsearch.api.dao.PatientSearchCriteriaDAO;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.beans.factory.annotation.Autowired;

public class PatientSearchCriteriaDAOTest extends BaseModuleContextSensitiveTest {
	
	private final static String PATIENTS_XML = "org/openmrs/module/patientsearch/api/dao/include/PatientSearchCriteriaDAOTest-patients.xml";
	
	@Autowired
	private PatientSearchCriteriaDAO dao;
	
	@Before
	public void runBeforeEachTest() {
		try {
			initializeInMemoryDatabase();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executeDataSet(PATIENTS_XML);
		
		updateSearchIndex();
		authenticate();
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, Integer, Integer, Boolean)
	 */
	@Test
	@SkipBaseSetup
	public void getPatients_shouldReturnListOfPatientsWithRequriedGender() {
		List<Patient> patients = dao.getPatientsByGender("M", 0, 11, false);
		Assert.assertEquals(3, patients.size());
		Assert.assertEquals("M", patients.get(0).getGender());
		Assert.assertEquals("M", patients.get(1).getGender());
		Assert.assertEquals("M", patients.get(2).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatient_shouldReturnListOfPatientsWithRequriedBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByBirthdate(birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(2, patients.size());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(java.util.Date, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatient_shouldReturnListOfPatientsWithRequriedAge() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByRangeOfAge(from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(3, patients.size());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedGivenNameAndGender() {
		List<Patient> patients = dao.getPatientsByNameOrIdAndGender("Bethany", "F", 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
		Assert.assertEquals("F", patients.get(0).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedGivenNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndBirthdate("Bethany", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedGivenNameAndAgeRange() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndRangeOfAge("Bethany", from.getTime(), to.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedGivenNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndBirthdate("Adam", "M", birthdate.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedGivenNameAndAgeRangeAndGender() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndRangeOfAge("Adam", "M", from.getTime(), to.getTime(),
		    0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedMiddleNameAndGender() {
		List<Patient> patients = dao.getPatientsByNameOrIdAndGender("Benedict", "M", 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
		Assert.assertEquals("M", patients.get(0).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedMiddleNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndBirthdate("Benedict", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedMiddleNameAndAgeRange() {
		GregorianCalendar to = new GregorianCalendar(2014, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndRangeOfAge("Benedict", from.getTime(), to.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedMiddleNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndBirthdate("Frank", "F", birthdate.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedMiddleNameAndAgeRangeAndGender() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndRangeOfAge("Frank", "F", from.getTime(), to.getTime(),
		    0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedfamilyNameAndGender() {
		List<Patient> patients = dao.getPatientsByNameOrIdAndGender("Franklin", "M", 0, 11, false);
		Assert.assertEquals(2, patients.size());
		Assert.assertEquals("Franklin", patients.get(0).getFamilyName());
		Assert.assertEquals("Franklin", patients.get(1).getFamilyName());
		Assert.assertEquals("M", patients.get(0).getGender());
		Assert.assertEquals("M", patients.get(1).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedfamilyNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndBirthdate("Franklin", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedfamilyNameAndAgeRange() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndRangeOfAge("Franklin", from.getTime(), to.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedfamilyNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndBirthdate("Franklin", "M", birthdate.getTime(), 0, 11,
		    false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatients_shouldReturnListOfPatientsWithRequriedfamilyNameAndAgeRangeAndGender() {
		GregorianCalendar to = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar from = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatientsByNameOrIdAndGenderAndRangeOfAge("Franklin", "M", from.getTime(),
		    to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
}
