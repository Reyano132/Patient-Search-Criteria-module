package org.openmrs.module.patientsearch.web.search;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientsearch.api.PatientSearchCriteriaService;
import org.openmrs.module.patientsearch.web.controller.PatientSearchCriteriaController;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.stereotype.Component;

@Component
public class PatientSearchHandler implements SearchHandler {
	
	private final SearchConfig searchConfig = new SearchConfig("patientBySearchCriteria", RestConstants.VERSION_1
	        + PatientSearchCriteriaController.PATIENTSEARCH_REST_NAMESPACE + "/patient", Arrays.asList("1.8.*", "1.9.*",
	    "1.10.*, 1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*"), Arrays.asList(new SearchQuery.Builder(
	        "Allows you to find Patients by name or id").withRequiredParameters("q").build(), new SearchQuery.Builder(
	        "Allow you to find Patients by gender").withRequiredParameters("gender").build(), new SearchQuery.Builder(
	        "Allow you to find Patients by birthdate").withRequiredParameters("birthdate").build(), new SearchQuery.Builder(
	        "Allow you to find Patients by range of age").withRequiredParameters("to", "from").build(),
	    new SearchQuery.Builder("Allow you to find Patients by name or id and gender").withRequiredParameters("q", "gender")
	            .build(), new SearchQuery.Builder("Allow you to find Patients by name or id and birthdate")
	            .withRequiredParameters("q", "birthdate").build(), new SearchQuery.Builder(
	            "Allow you to find Patients by name or id and range of age").withRequiredParameters("q", "to", "from")
	            .build(), new SearchQuery.Builder("Allow you to find Patients by name or id and gender and birthdate")
	            .withRequiredParameters("q", "gender", "birthdate").build(),
	    new SearchQuery.Builder("Allow you to find Patients by name or id and gender and range of age")
	            .withRequiredParameters("q", "gender", "to", "from").build(), new SearchQuery.Builder(
	            "Allow you to find Patients by gender and range of age").withRequiredParameters("gender", "to", "from")
	            .build(), new SearchQuery.Builder("Allow you to find Patients by birthdate and gender")
	            .withRequiredParameters("gender", "birthdate").build()));
	
	@Override
	public SearchConfig getSearchConfig() {
		return this.searchConfig;
	}
	
	@Override
	public PageableResult search(RequestContext context) throws ResponseException {
		String q = context.getParameter("q");
		String gender = context.getParameter("gender");
		String toString = context.getParameter("to");
		String fromString = context.getParameter("from");
		String birthdateString = context.getParameter("birthdate");
		Integer to = (toString == null) ? null : Integer.parseInt(toString);
		Integer from = (fromString == null) ? null : Integer.parseInt(fromString);
		Date birthdate = (birthdateString == null) ? null : new Date(Long.valueOf(birthdateString));
		List<Patient> patients = Context.getService(PatientSearchCriteriaService.class).getPatients(null, q, null, true,
		    gender, from, to, birthdate);
		if (patients != null && patients.size() > 0) {
			return new NeedsPaging<Patient>(patients, context);
		}
		return new EmptySearchResult();
	}
	
}
