package com.tatiana.study.data;

import com.tatiana.study.model.Bussines;
import com.tatiana.study.model.Incident;

public interface DBConnect {
	public abstract Bussines[] findBussines(float nELat, float nELng, float sWLat, float sWLng);

	public Incident[] findIncidents(final float nELat, final float nELng, final float sWLat, final float sWLng);

	public Incident[] findIncidents(int maxPoints);

}