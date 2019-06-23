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
	public void getPatientsByGender() {
		List<Patient> patients = dao.getPatients("M", 0, 11, false);
		Assert.assertEquals(3, patients.size());
		Assert.assertEquals("M", patients.get(0).getGender());
		Assert.assertEquals("M", patients.get(1).getGender());
		Assert.assertEquals("M", patients.get(2).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientByBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients(birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(2, patients.size());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(java.util.Date, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatientByAge() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients(from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(3, patients.size());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByGivenNameAndGender() {
		List<Patient> patients = dao.getPatients("Bethany", "F", 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
		Assert.assertEquals("F", patients.get(0).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByGivenNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Bethany", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatientsByGivenNameAndAgeRange() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Bethany", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Bethany", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatientsByGivenNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Adam", "M", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByGivenNameAndAgeRangeAndGender() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Adam", "M", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByMiddleNameAndGender() {
		List<Patient> patients = dao.getPatients("Benedict", "M", 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
		Assert.assertEquals("M", patients.get(0).getGender());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByMiddleNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Benedict", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatientsByMiddleNameAndAgeRange() {
		GregorianCalendar from = new GregorianCalendar(2014, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Benedict", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Benedict", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatientsByMiddleNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Frank", "F", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByMiddleNameAndAgeRangeAndGender() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Frank", "F", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Frank", patients.get(0).getMiddleName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByfamilyNameAndGender() {
		List<Patient> patients = dao.getPatients("Franklin", "M", 0, 11, false);
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
	public void getPatientsByfamilyNameAndBirthdate() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Franklin", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, java.util.Date, java.util.Date, Integer,
	 *      Integer, Boolean)
	 */
	@Test
	public void getPatientsByfamilyNameAndAgeRange() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Franklin", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, Integer, Integer,
	 *      Boolean)
	 */
	@Test
	public void getPatientsByfamilyNameAndBirthdaterAndGender() {
		GregorianCalendar birthdate = new GregorianCalendar(2014, 7, 28);
		List<Patient> patients = dao.getPatients("Franklin", "M", birthdate.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
	/**
	 * @see PatientSearchCriteriaDAO#getPatients(String, String, java.util.Date, java.util.Date,
	 *      Integer, Integer, Boolean)
	 */
	@Test
	public void getPatientsByfamilyNameAndAgeRangeAndGender() {
		GregorianCalendar from = new GregorianCalendar(2013, 7, 28);
		GregorianCalendar to = new GregorianCalendar(2015, 7, 28);
		List<Patient> patients = dao.getPatients("Franklin", "M", from.getTime(), to.getTime(), 0, 11, false);
		Assert.assertEquals(1, patients.size());
		Assert.assertEquals("Adam", patients.get(0).getGivenName());
	}
	
}
