package com.tatiana.study.model;

import java.util.ArrayList;

public class Result {
	public double[] att; // values of attributes
	public int k; // it's cardinlaity
	public int c; // it's complexity
	public double e; // it's error

	public double[] att_L;
	public double[] att_U;
	int d;

	public double sim = -1.0;
	public Distance dis;
	public double w = -1.0; // for weights of [e] and [getNormDist()]
	public double dist = -1.0;

	public ArrayList<Dev> dev;

	public Result(final double[] a, final int _k, final int _c, final double _e, final Distance _dis, final int _d,
			final double _w) {
		d = _d;
		att = new double[d];
		System.arraycopy(a, 0, att, 0, d);
		k = _k;
		c = _c;
		e = _e;
		dis = _dis;
		w = _w;
	}

	public Result(final double[] a, final int _k, final int _c, final double _e, final double _dist, final int _d,
			final double _w, final ArrayList<Dev> _dev) {
		d = _d;
		att = new double[d];
		System.arraycopy(a, 0, att, 0, d);
		k = _k;
		c = _c;
		e = _e;
		dist = _dist;
		w = _w;
		dev = _dev;
	}

	public Result(final double[] a, final int _k, final int _c, final double _e, final double _dist, final int _d,
			final double _w) {
		d = _d;
		att = new double[d];
		System.arraycopy(a, 0, att, 0, d);
		k = _k;
		c = _c;
		e = _e;
		dist = _dist;
		w = _w;

	}

	public Result(final double[] a, final double[] b) {
		System.arraycopy(a, 0, att_L, 0, a.length);
		System.arraycopy(b, 0, att_U, 0, b.length);
	}

	public Result() {

	}

	public int getK() {
		return k;
	}

	public int getC() {
		return c;
	}

	public double getE() {
		return e;
	}

	public double[] getAtt() {
		return att;
	}

	public ArrayList<Dev> getDev() {
		return dev;
	}

	public double getDistance() {
		return dist;
	}

	public void print() {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		System.out.print("]\tK=" + k + "\tC=" + c + "\tE=" + e);
	}

	public void print2() {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		System.out.print("]\t" + k + "\t" + c + "\t" + e + "\t" + sim);
	}

	public void print3() {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		System.out.print("]\t" + k + "\t" + c + "\t" + e + "\t" + dis.getAvrDist() + "\t" + dis.getMaxDist() + "\t"
				+ dis.getMiniDist() + "\n");
	}

	public void print4() {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		// System.out.print("]\tK="+k+"\tC="+c+"\tE="+e+"\tAvr="+dis.getAvrDist()+"\tMax="+dis.getMaxDist()+"\tMin="+dis.getMiniDistNotZero()+"\n");
		// System.out.format("]\tK=%d"+"\tC=%d"+"\tE=%.5f"+"\tAvrDist=%.5f"+"\tMaxDist=%.5f"+"\tMinDist=%.5f"+"%n",
		// k, c, e, dis.getAvrDist(), dis.getMaxDist(),
		// dis.getMiniDistNotZero());
		// System.out.print(
		// "]\tK="+k+"\tC="+c+"\tTotal E="+"\tE="+e+"\tDistAvr="+dis.getAvrDist()+"\tMaxDist="+dis.getMaxDist()+"\tMinDist="+dis.getMiniDist()+"\n");
		System.out.format("]\tK=%d" + "\tC=%d" + "\tTotal E=%.5f" + "\tE=%.5f" + "\tAvrDist=%.5f" + "\tMaxDist=%.5f"
				+ "\tMinDist=%.5f" + "%n", k, c, (e + dis.getNormDist()), e, dis.getAvrDist(), dis.getMaxDist(),
				dis.getMiniDistNotZero());
	}

	// opt
	// 0: no column names (easier to export to excel)
	// 1: w colmun names
	public void print5(final int opt) {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		// System.out.print("]\tK="+k+"\tC="+c+"\tE="+e+"\tAvr="+dis.getAvrDist()+"\tMax="+dis.getMaxDist()+"\tMin="+dis.getMiniDistNotZero()+"\n");
		// System.out.format("]\tK=%d"+"\tC=%d"+"\tE=%.5f"+"\tAvrDist=%.5f"+"\tMaxDist=%.5f"+"\tMinDist=%.5f"+"%n",
		// k, c, e, dis.getAvrDist(), dis.getMaxDist(),
		// dis.getMiniDistNotZero());
		// System.out.print(
		// "]\tK="+k+"\tC="+c+"\tTotal E="+"\tE="+e+"\tDistAvr="+dis.getAvrDist()+"\tMaxDist="+dis.getMaxDist()+"\tMinDist="+dis.getMiniDist()+"\n");
		if (opt == 1)
			System.out.format("]\tK=%d" + "\tC=%d" + "\tEt=%.5f" + "\tEc=%f" + "\tEs=%f" + "\tAvrDist=%.5f%n", k, c,
					(e * (0.5)) + ((0.5) * dis.getNormDist()), e, dis.getNormDist(), dis.getAvrDist());
		else
			System.out.format("]\t%d" + "\t%d" + "\t%.5f" + "\t\t%f" + "\t%f" + "\t%.5f%n", k, c, (e * (0.5))
					+ ((0.5) * dis.getNormDist()), e, dis.getNormDist(), dis.getAvrDist());
	}

	public void print6(final int opt) {

		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		// System.out.print("]\tK="+k+"\tC="+c+"\tE="+e+"\tAvr="+dis.getAvrDist()+"\tMax="+dis.getMaxDist()+"\tMin="+dis.getMiniDistNotZero()+"\n");
		// System.out.format("]\tK=%d"+"\tC=%d"+"\tE=%.5f"+"\tAvrDist=%.5f"+"\tMaxDist=%.5f"+"\tMinDist=%.5f"+"%n",
		// k, c, e, dis.getAvrDist(), dis.getMaxDist(),
		// dis.getMiniDistNotZero());
		// System.out.print(
		// "]\tK="+k+"\tC="+c+"\tTotal E="+"\tE="+e+"\tDistAvr="+dis.getAvrDist()+"\tMaxDist="+dis.getMaxDist()+"\tMinDist="+dis.getMiniDist()+"\n");
		if (opt == 1)
			System.out.format("]\tK=%d" + "\tC=%d" + "\tEt=%.5f" + "\tEc=%f" + "\tEs=%f" + "\tAvrDist=%.5f%n", k, c,
					(e * (w)) + ((1 - w) * dis.getNormDist()), e, dis.getNormDist(), dis.getAvrDist());
		else
			System.out.format("]\t%d" + "\t%d" + "\t%.5f" + "\t\t%f" + "\t%f" + "\t%.5f%n", k, c, (e * (w))
					+ ((1 - w) * dis.getNormDist()), e, dis.getNormDist(), dis.getAvrDist());

	}

	public double getEt() {
		return (e * w) + ((1 - w) * dist);
	}

	public double getEc() {
		return e;
	}

	public double getEs() {
		return dist;
	}

	public void printML(final int opt) {
		System.out.print("[");
		for (int i = 0; i < att.length; i++) {
			System.out.print(att[i]);
			if (i < att.length - 1)
				System.out.print(",");
		}

		if (opt == 1)
			System.out.format("]\tK=%d" + "\tC=%d" + "\tEt=%.5f" + "\tEc=%f" + "\tEs=%f" + "%n", k, c, (e * (w))
					+ ((1 - w) * dist), e, dist);
		else
			System.out.format("]\t%d" + "\t%d" + "\t%.5f" + "\t\t%f" + "\t%f" + "%n", k, c, (e * (w))
					+ ((1 - w) * dist), e, dist);
	}
}