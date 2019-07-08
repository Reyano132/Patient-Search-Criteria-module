package org.openmrs.module.patientsearch.web.search;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientsearch.api.PatientSearchCriteriaService;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

public class PatientSearchHandlerTest extends MainResourceControllerTest {
	
	private final static String PATIENT_UUID = "da7f524f-27ce-4bb2-86d6-6d1d05312bd5";
	
	/**
	 * @see MainResourceControllerTest#getURI()
	 */
	@Override
	public String getURI() {
		return "patientsearch/patient";
	}
	
	/**
	 * @return
	 * @see MainResourceControllerTest#getAllCount()
	 */
	@Override
	public long getAllCount() {
		return Context.getService(PatientSearchCriteriaService.class).getAllPatients(false).size();
	}
	
	/**
	 * @see MainResourceControllerTest#getUuid()
	 */
	@Override
	public String getUuid() {
		return PATIENT_UUID;
	}
	
	@Override
	@Test(expected = ResourceDoesNotSupportOperationException.class)
	public void shouldGetAll() throws Exception {
		super.shouldGetAll();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getSearchConfig_shouldReturnPatientByIdentifier() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("q", "7TU-8");
		
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(Context.getPatientService().getPatient(8).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByName() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("q", "Horatio");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByFewInitialsOfName() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("q", "Hor");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByNameAndGender() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("q", "Horatio");
		req.addParameter("gender", "M");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByBirthdate() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		GregorianCalendar birthdate = new GregorianCalendar(1975, 3, 8);
		req.addParameter("birthdate", String.valueOf(birthdate.getTime().getTime()));
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByRangeOfAge() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("from", String.valueOf(40));
		req.addParameter("to", String.valueOf(50));
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(3, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
		Assert.assertEquals(Context.getPatientService().getPatient(6).getUuid(),
		    PropertyUtils.getProperty(hits.get(1), "uuid"));
		Assert.assertEquals(Context.getPatientService().getPatient(7).getUuid(),
		    PropertyUtils.getProperty(hits.get(2), "uuid"));
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByNameAndBirthdate() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		GregorianCalendar birthdate = new GregorianCalendar(1975, 3, 8);
		req.addParameter("birthdate", String.valueOf(birthdate.getTime().getTime()));
		req.addParameter("q", "Horatio");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByGenderAndRangeOfAge() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("from", String.valueOf(40));
		req.addParameter("to", String.valueOf(50));
		req.addParameter("gender", "F");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(7).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByNameAndRangeOfAge() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("from", String.valueOf(40));
		req.addParameter("to", String.valueOf(50));
		req.addParameter("q", "Collet");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(7).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByGenderAndBirthdate() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		GregorianCalendar birthdate = new GregorianCalendar(1975, 3, 8);
		req.addParameter("birthdate", String.valueOf(birthdate.getTime().getTime()));
		req.addParameter("gender", "M");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByNameAndGenderAndRangeOfAge() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		req.addParameter("from", String.valueOf(40));
		req.addParameter("to", String.valueOf(50));
		req.addParameter("gender", "F");
		req.addParameter("q", "Collet");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(7).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getPatientByNameAndGenderAndBirthdate() throws Exception {
		MockHttpServletRequest req = request(RequestMethod.GET, getURI());
		GregorianCalendar birthdate = new GregorianCalendar(1975, 3, 8);
		req.addParameter("birthdate", String.valueOf(birthdate.getTime().getTime()));
		req.addParameter("gender", "M");
		req.addParameter("q", "Horatio");
		SimpleObject result = deserialize(handle(req));
		List<Object> hits = (List<Object>) result.get("results");
		Assert.assertEquals(1, hits.size());
		Assert.assertEquals(Context.getPatientService().getPatient(2).getUuid(),
		    PropertyUtils.getProperty(hits.get(0), "uuid"));
	}
	
}
