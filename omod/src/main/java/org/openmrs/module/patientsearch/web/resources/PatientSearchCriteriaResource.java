package org.openmrs.module.patientsearch.web.resources;

import org.openmrs.Patient;
import org.openmrs.module.patientsearch.web.controller.PatientSearchCriteriaController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_8.PatientResource1_8;

@Resource(name = RestConstants.VERSION_1 + PatientSearchCriteriaController.PATIENTSEARCH_REST_NAMESPACE + "/patient", supportedClass = Patient.class, supportedOpenmrsVersions = {
        "1.8.*", "1.9.*", "1.10.*, 1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*" })
public class PatientSearchCriteriaResource extends PatientResource1_8 {
	
	public PatientSearchCriteriaResource() {
	}
	
}
