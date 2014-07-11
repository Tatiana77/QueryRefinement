package com.tatiana.study.web;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.tatiana.study.controllers.CustomJsonDateDeserializer;

public class MapMBean {

	// Inputs
	private float nELat = 0;
	private float nELng = 0;
	private float sWLat = 0;
	private float sWLng = 0;
	private int cardinality = 1;
	private int reqCardinality = 1;
	private float alpha;
	private String scheme = "saqrcs";

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date startDate = new DateTime(2007, 1, 1, 0, 0, 0).toDate();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	private Date endDate = new DateTime(2012, 12, 31, 0, 0, 0).toDate();

	// Outputs
	private float refNELat = 0;
	private float refNELng = 0;
	private float refSWLat = 0;
	private float refSWLng = 0;
	private int refCardinality = 1;
	private float deviation = 0;

	public MapMBean() {
		super();
	}

	public float getnELat() {
		return nELat;
	}

	public void setnELat(final float nELat) {
		this.nELat = nELat;
	}

	public float getnELng() {
		return nELng;
	}

	public void setnELng(final float nELng) {
		this.nELng = nELng;
	}

	public float getsWLat() {
		return sWLat;
	}

	public void setsWLat(final float sWLat) {
		this.sWLat = sWLat;
	}

	public float getsWLng() {
		return sWLng;
	}

	public void setsWLng(final float sWLng) {
		this.sWLng = sWLng;
	}

	public float getRefNELat() {
		return refNELat;
	}

	public void setRefNELat(final float refNELat) {
		this.refNELat = refNELat;
	}

	public float getRefNELng() {
		return refNELng;
	}

	public void setRefNELng(final float refNELng) {
		this.refNELng = refNELng;
	}

	public float getRefSWLat() {
		return refSWLat;
	}

	public void setRefSWLat(final float refSWLat) {
		this.refSWLat = refSWLat;
	}

	public float getRefSWLng() {
		return refSWLng;
	}

	public void setRefSWLng(final float refSWLng) {
		this.refSWLng = refSWLng;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(final float alpha) {
		this.alpha = alpha;
	}

	public int getCardinality() {
		return cardinality;
	}

	public int getReqCardinality() {
		return reqCardinality;
	}

	public String getScheme() {
		return scheme;
	}

	public int getRefCardinality() {
		return refCardinality;
	}

	public void setRefCardinality(final int refCardinality) {
		this.refCardinality = refCardinality;
	}

	public float getDeviation() {
		return deviation;
	}

	public void setDeviation(final float deviation) {
		this.deviation = deviation;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "MapMBean [nELat=" + nELat + ", nELng=" + nELng + ", sWLat="
				+ sWLat + ", sWLng=" + sWLng + ", cardinality=" + cardinality
				+ ", reqCardinality=" + reqCardinality + ", alpha=" + alpha
				+ ", scheme=" + scheme + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", refNELat=" + refNELat
				+ ", refNELng=" + refNELng + ", refSWLat=" + refSWLat
				+ ", refSWLng=" + refSWLng + ", refCardinality="
				+ refCardinality + ", deviation=" + deviation + "]";
	}

	
}
