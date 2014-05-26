package com.tatiana.study.model;

import java.util.Comparator;

public class Combination {
	public int d = -1; // dimensions
	public double[] att;
	public double dist = -1.0; // distance
	public int k = -1; // cardinality
	public int round = -1; // flag for rounds
	public boolean visited = false;

	public Combination(final double[] _att, final int _d, final double _dist, final int _k, final int _round,
			final boolean _visited) {
		d = _d;
		att = new double[d];
		System.arraycopy(_att, 0, att, 0, d);
		dist = _dist;
		k = _k;
		round = _round;
		visited = _visited;
	}

	public Combination(final double[] _att, final int _d, final double _dist, final int _k, final int _round) {
		d = _d;
		att = new double[d];
		System.arraycopy(_att, 0, att, 0, d);
		dist = _dist;
		k = _k;
		round = _round;

	}

	public Combination(final Combination c) {
		this.d = c.d;
		this.k = c.k;
		this.round = c.round;
		this.dist = c.dist;
		this.att = new double[d];
		System.arraycopy(c.att, 0, this.att, 0, d);
		this.visited = c.visited;
	}

	public void setVisited() {
		visited = true;
	}

	public void resetVisited() {
		visited = false;
	}

	public void setAtt(final double[] _att, final int d) {
		System.arraycopy(_att, 0, att, 0, d);
	}

	public void setDist(final double _dist) {
		dist = _dist;
	}

	public void setK(final int _k) {
		k = _k;
	}

	public void setRound(final int _round) {
		round = _round;
	}

	public double[] getAtt() {
		return att;
	}

	public int getK() {
		return k;
	}

	public double getDist() {
		return dist;
	}

	public int getRound() {
		return round;
	}
}

class CombinationComparator implements Comparator<Combination> {
	@Override
	public int compare(final Combination c1, final Combination c2) {
		int dd = (int) ((c1.getDist() - c2.getDist()) * 1000000000);
		// System.out.println(dd);
		return (int) ((c1.getDist() - c2.getDist()) * 1000000000);
	}
}