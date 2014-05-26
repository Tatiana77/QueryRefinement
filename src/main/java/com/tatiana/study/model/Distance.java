package com.tatiana.study.model;

public class Distance {

	public double sum;
	public int num;
	public int d;
	public double[] dist;
	public double max;
	public double mini;

	public Distance(final double _sum, final int _num, final int _d, final double[] _dist) {
		d = _d;
		num = _num;
		sum = _sum;
		dist = new double[_dist.length];
		System.arraycopy(_dist, 0, dist, 0, _dist.length);
	}

	public Distance(final double _sum, final int _num) {
		// d = _d;
		num = _num;
		sum = _sum;
		// dist = new double[0];
	}

	public double getSum() {
		return sum;
	}

	public int getNum() {
		return num;
	}

	public double[] getDist() {
		return dist;
	}

	public double getAvrDist() {
		return (sum / num);
	}

	public double getMaxDist() {
		double max = 0.0;
		for (int i = 0; i < dist.length; i++) {
			if (dist[i] > max)
				max = dist[i];

		}
		return max;
	}

	public double getMiniDist() {
		double mini = dist[0];
		for (int i = 0; i < dist.length; i++) {
			if (dist[i] < mini)
				mini = dist[i];

		}
		return mini;
	}

	// returns the smallest distance which is not zero.
	public double getMiniDistNotZero() {
		// first, change all 0s values to +inf/max:
		double max = getMaxDist();
		double[] t_dist = new double[dist.length];
		System.arraycopy(dist, 0, t_dist, 0, t_dist.length);

		for (int i = 0; i < t_dist.length; i++) {
			if (t_dist[i] == 0.0)
				t_dist[i] = max;
		}

		// now t_dist doesn't have any 0 value/distance.
		double mini = t_dist[0];
		for (int i = 0; i < t_dist.length; i++) {
			if (t_dist[i] < mini)
				mini = t_dist[i];

		}
		return mini;
	}

	// return the distance in normalized form.
	//
	public double getNormDist() {
		// k is the size of the table.
		double k = 100000.0;
		return sum / k;
	}
}
