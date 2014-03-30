package com.tatiana.study.model;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Bussines {

	private String bussines_id;
	private String category;
	private String city;
	private String state;
	private int review_count;
	private int stars;
	private BigDecimal longit;
	private BigDecimal latit;

	public Bussines(final Bussines other) {
		super();
		this.bussines_id = other.bussines_id;
		this.category = other.category;
		this.city = other.city;
		this.state = other.state;
		this.review_count = other.review_count;
		this.stars = other.stars;
		this.longit = new BigDecimal(other.longit.doubleValue());
		this.latit = new BigDecimal(other.latit.doubleValue());
		;
	}

	public Bussines(final String bussines_id, final String category, final String city, final String state,
			final int review_count, final int stars, final BigDecimal longit, final BigDecimal latit) {
		super();
		this.bussines_id = bussines_id;
		this.category = category;
		this.city = city;
		this.state = state;
		this.review_count = review_count;
		this.stars = stars;
		this.longit = longit;
		this.latit = latit;
	}

	@JsonIgnore
	public String getBussines_id() {
		return bussines_id;
	}

	public void setBussines_id(final String bussines_id) {
		this.bussines_id = bussines_id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	@JsonIgnore
	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	@JsonIgnore
	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	@JsonIgnore
	public int getReview_count() {
		return review_count;
	}

	public void setReview_count(final int review_count) {
		this.review_count = review_count;
	}

	@JsonIgnore
	public int getStars() {
		return stars;
	}

	public void setStars(final int stars) {
		this.stars = stars;
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

}
