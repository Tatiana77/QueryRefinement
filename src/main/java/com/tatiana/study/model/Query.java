package com.tatiana.study.model;

public class Query {
	double[] att;
	int d;

	public Query(final double[] _att, final int _d) {
		d = _d;
		att = new double[d];
		System.arraycopy(_att, 0, att, 0, d);
	}

	public double[] getAtt() {
		return att;
	}

}
