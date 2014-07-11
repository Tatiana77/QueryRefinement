package com.tatiana.study.data;

import java.util.Date;

import com.tatiana.study.model.Bussines;
import com.tatiana.study.model.Incident;

public interface DBConnect {
	public abstract Bussines[] findBussines(float nELat, float nELng,
			float sWLat, float sWLng);

	public Incident[] findIncidents(final float nELat, final float nELng,
			final float sWLat, final float sWLng, final Date startDate,
			final Date endDate);

	public Incident[] findIncidents(int maxPoints, final Date startDate,
			final Date endDate);

}