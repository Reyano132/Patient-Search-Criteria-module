/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientsearch.web.controller;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/${rootArtifactid}/${rootArtifactid}Link.form'.
 */
//@Controller("${rootrootArtifactid}.PatientSearchCriteriaController")
//@RequestMapping(value = "module/${rootArtifactid}/${rootArtifactid}.form")
@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + PatientSearchCriteriaController.PATIENTSEARCH_REST_NAMESPACE)
public class PatientSearchCriteriaController extends MainResourceController {
	
	public static final String PATIENTSEARCH_REST_NAMESPACE = "/patientsearch";
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return RestConstants.VERSION_1 + PATIENTSEARCH_REST_NAMESPACE;
	}
	
}
