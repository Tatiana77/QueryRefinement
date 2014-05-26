package com.tatiana.study.model;/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author uqaalbar
 */
public class Dev {
	public double err = -1;
	public int k = -1;
	public int d;
	public double[] att;

	public Dev(final double _err, final int _k, final double[] _att, final int _d) {
		err = _err;
		k = _k;
		d = _d;
		att = new double[d];
		System.arraycopy(_att, 0, att, 0, d);
	}

	public double getError() {
		return err;

	}

	public int getK() {
		return k;

	}

	public String getAttAsString() {
		String s = new String();
		for (int i = 0; i < d; i++) {
			if (i == (d - 1))
				s = s + att[i];
			else
				s = s + att[i] + "\t";
		}
		return s;
	}

	public double[] getAtt() {
		return att;
	}
}
