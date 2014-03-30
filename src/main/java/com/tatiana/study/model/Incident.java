package com.tatiana.study.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Incident {
	private Date date;
	private BigDecimal longit;
	private BigDecimal latit;

	public Incident(final Incident other) {
		super();
		this.date = other.date;
		this.longit = other.longit;
		this.latit = other.latit;
	}

	private static final DateFormat sdf = SimpleDateFormat.getInstance();

	public Incident() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Incident(final Date date, final BigDecimal longit, final BigDecimal latit) {
		super();
		this.date = date;
		this.longit = longit;
		this.latit = latit;
	}

	public BigDecimal getLongit() {
		return longit;
	}

	public void setLongit(final BigDecimal longit) {
		this.longit = longit;
	}

	public BigDecimal getLatit() {
		return latit;
	}

	public void setLatit(final BigDecimal latit) {
		this.latit = latit;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public String getCategory() {
		return sdf.format(date);
	}

}
