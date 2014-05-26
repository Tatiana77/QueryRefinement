package com.tatiana.study.model;

public class Tuple {
	double[] att;
	int k;
	int d;
	double Ec;

	public Tuple(final double[] _att, final int _k, final int _d, final double _Ec) {
		d = _d;
		k = _k;
		att = new double[d];
		Ec = _Ec;
		System.arraycopy(_att, 0, att, 0, d);
	}

	public Tuple(final double[] _att, final int _d) {
		d = _d;
		k = -1;
		att = new double[d];
		Ec = -1;
		System.arraycopy(_att, 0, att, 0, d);
	}

	public Tuple() {
		k = -1;
		d = -1;
		att = null;
	}

	public double[] getAtt() {
		return att;
	}

	public int getK() {
		return k;
	}

	public double getEc() {
		return Ec;
	}

	// checks att[] if equals to given _att[] array
	public boolean equals(final double[] _att, final int d) {
		boolean answer = true;

		for (int i = 0; i < d; i++) {
			if (_att[i] != this.att[i])
				answer = false;
		}
		return answer;
	}

	@Override
	public boolean equals(final Object o) {
		boolean same = true;

		Tuple x = (Tuple) o;

		for (int i = 0; i < this.d; i++)
			if (x.att[i] != this.att[i])
				same = false;

		if (x.getK() != this.getK())
			same = false;

		return same;
	}
}