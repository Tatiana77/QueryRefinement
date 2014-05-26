package com.tatiana.study.controllers;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tatiana.study.data.DBConnect;
import com.tatiana.study.model.Bussines;
import com.tatiana.study.model.Incident;
import com.tatiana.study.model.Refinement;
import com.tatiana.study.web.IResponse;
import com.tatiana.study.web.MapMBean;
import com.tatiana.study.web.Response;

@Controller
@SessionAttributes("formBean")
public class HomeController {

	// TPC Dataset:
	public String query = "select count(*) as count from bussines.incidents where lat<=(?) and lon<=(?) and lat>=(?) and  lon>= (?);";

	@Autowired
	private DBConnect dbConnect;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		// Doing the mapping to the file that will be called when get request.
		return "index";
	}

	@RequestMapping(value = "/refinement", method = RequestMethod.GET)
	public String refinement() {
		return "refinement";
	}

	@ModelAttribute("formBean")
	public MapMBean createFormBean() {

		return new MapMBean();

	}

	@RequestMapping(value = "/ajax/process", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody
	Response process(@Valid @RequestBody final MapMBean formBean, final BindingResult result, final Model model,
			final HttpSession session) {

		Response response = new Response("OK");
		try {

			Bussines[] bussinesArray = dbConnect.findBussines(formBean.getnELat(), formBean.getnELng(),
					formBean.getsWLat(), formBean.getsWLng());

			if (bussinesArray.length == 0) {
				response.setStatus("ERROR");
				response.addError("", "No bussinesses found!");
			}
			response.setBussinesArray(bussinesArray);

			Bussines[] bussinesRefArray = Refinement.refine(bussinesArray, bussinesArray.length,
					new BigDecimal("0.005"));

			response.setBussinesRefArray(bussinesRefArray);
		} catch (Exception e) {
			response = new Response("ERROR");
			response.addError("", e.getMessage());

		}
		return response;

	}

	@RequestMapping(value = "/ajax/processIncidents", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody
	IResponse processIncidents(@Valid @RequestBody final MapMBean formBean, final BindingResult result,
			final Model model, final HttpSession session) {

		IResponse response = new IResponse("OK");
		try {

			double[] i_q = new double[4];

			i_q[0] = formBean.getnELat();
			i_q[1] = formBean.getnELng();
			i_q[2] = formBean.getsWLat();
			i_q[3] = formBean.getsWLng();

			double[] r_q = new double[4];
			int d = 4;
			double[] dmaxs = { 1.0, 1.0, 1.0, 1.0 };
			double[] dmins = { 0.0, 0.0, 0.0, 0.0 };
			int size = 100000;
			double w = 0.5;
			double scale = 1.0;
			int k = 20; //retrieve k from the textbox

			double[] iq_norm = Refinement.toNorm(i_q, d, 33.0952238976, 32.5414967546, -116.928885733, -117.281367428);

			// The refinement process
			r_q = Refinement.SAQR_CSP_MDM(iq_norm, dmaxs, dmins, d, k, size, w, scale, query, true, true, true, 0.5,
					0.125, 0);

			double[] rq_real = Refinement.toReal(r_q, d, 33.0952238976, 32.5414967546, -116.928885733, -117.281367428);
			response.setR_q(rq_real);
			response.setRefCardinality(77);// get this values from resutl.java
			response.setDeviation(0.77); //same up

		} catch (Exception e) {
			logger.error("Error: " + e.getMessage(), e);
			response = new IResponse("ERROR");
			response.addError("", e.getClass().getName() + " " + e.getMessage());

		}
		return response;

	}

	@RequestMapping(value = "/ajax/getIncidents", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody
	IResponse getIncidents(@Valid @RequestBody final MapMBean formBean, final BindingResult result, final Model model,
			final HttpSession session) {

		IResponse response = new IResponse("OK");
		try {

			Incident[] incidentArray = dbConnect.findIncidents(100000);

			if (incidentArray.length == 0) {
				response.setStatus("ERROR");
				response.addError("", "No incidents found!");
			}
			response.setIncidentArray(incidentArray);

		} catch (Exception e) {
			response = new IResponse("ERROR");
			response.addError("", e.getMessage());

		}
		return response;

	}

}
