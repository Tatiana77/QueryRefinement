package com.tatiana.study.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Refinement {

	private static final Logger logger = LoggerFactory.getLogger(Refinement.class);

	public static Bussines[] refine(final Bussines[] bussinesArray, final int k, final BigDecimal bdc) {
		Bussines[] bussinesRefArray = new Bussines[k];
		logger.debug("Refining points bdc = " + bdc);
		for (int i = 0; i < k; i++) {
			bussinesRefArray[i] = new Bussines(bussinesArray[i]);
			bussinesRefArray[i].setLongit(bussinesArray[i].getLongit().add(bdc));
		}

		return bussinesRefArray;
	}

	public static Incident[] refine(final Incident[] incidentArray, final int k, final BigDecimal bdc) {
		Incident[] incidentRefArray = new Incident[k];
		logger.debug("Refining points bdc = " + bdc);
		for (int i = 0; i < k; i++) {
			incidentRefArray[i] = new Incident(incidentArray[i]);
			incidentRefArray[i].setLongit(incidentArray[i].getLongit().add(bdc));
		}

		return incidentRefArray;
	}

	public static double[] SAQR(final double[] i_q) {
		double[] r_q = new double[4];

		r_q[0] = i_q[0] + 0.005;
		r_q[1] = i_q[1] + 0.005;
		r_q[2] = i_q[2] + 0.005;
		r_q[3] = i_q[3] + 0.005;

		return r_q;
	}

	// att = [long_NE, lat_NE, long_SW, lat_SW]
	public static double[] SAQR_CSP_MDM(final double[] _att, final double[] _dmaxs, final double[] _dmins,
			final int _d, final int _k, final int _size, final double w, final double scale, final String sql_q,
			final boolean SHORT, final boolean BOUND, final boolean EARLY, final double _width, final double _minWidth,
			final int _rounds) {
		int size = _size;
		int d = _d;
		double[] dmaxs = new double[d];
		double[] dmins = new double[d];
		double[] att = new double[d];

		System.arraycopy(_att, 0, att, 0, d);
		System.arraycopy(_dmaxs, 0, dmaxs, 0, d);
		System.arraycopy(_dmins, 0, dmins, 0, d);

		int k = _k;
		int rounds = _rounds;
		double sz = 0;
		double width = 0;

		sz = _minWidth; // smallest width
		width = _width; // initial width
		rounds = (int) (_width / _minWidth);

		/*
		 * double[] ranges = new double[d]; for(int i=0; i<d;i++) { ranges[i] =
		 * Math.abs(dmaxs[i]-dmins[i]+1.0); //ranges[i] =
		 * Math.abs(dmaxs[i]-dmins[i]); }
		 */

		int est = -1;
		int compex = 0;

		ArrayList<Combination> combinations = new ArrayList<Combination>();

		ArrayList<Tuple> table = new ArrayList<Tuple>();

		// ArrayList<Dev> dev = new ArrayList<Dev>();

		// double[] start_att = new double[d];
		// System.arraycopy( dmins, 0, start_att, 0, d );
		double[] start_att = { 0.0, 0.0, 0.0, 0.0 };
		double[] end_att = { 100.0, 100.0, 0.0, 0.0 };

		Tuple t_start = new Tuple(start_att, 0, d, 0);
		if (!table.contains(t_start))
			table.add(t_start);

		// double[] end_att = new double[d];
		// System.arraycopy( dmaxs, 0, end_att, 0, d );

		Tuple t_end = new Tuple(end_att, size, d, 0);
		if (!table.contains(t_end))
			table.add(t_end);

		est = estCard_MDM(att, sql_q, d);
		double Ec0 = (Math.abs(est - k)) / (double) getMax(est, k);
		double Et0 = w * Ec0 + (1 - w) * 0.0;
		// double Et0= 1.0;
		double mini_err = Et0;

		// double[] curr_att = new double[d];
		// System.arraycopy( att, 0, curr_att, 0, d);

		double[] in_att = new double[d];
		System.arraycopy(att, 0, in_att, 0, d);

		// Should Ec0 be used here instead of 0?
		// Tuple t_in = new Tuple(in_att, est, d, Ec0);
		Tuple t_in = new Tuple(in_att, est, d, 0);
		if (!table.contains(t_in))
			table.add(t_in);

		// i and j are SW ,,,,, i = SW_long j = SW_lat
		// h and p are NE ,,,,, h = NE_long p = NE_lat
		if (d == 4) {
			for (double i = dmins[0]; i < dmaxs[0]; i = i + sz) {
				for (double j = dmins[1]; j < dmaxs[1]; j = j + sz) {
					for (double h = i + sz; h <= dmaxs[2]; h = h + sz) {
						for (double p = j + sz; p <= dmaxs[3]; p = p + sz) {
							double[] curr2_att = { h, p, i, j };
							double dist = computeDistML_MDM(_att, curr2_att, d, scale);
							Combination comb = new Combination(curr2_att, d, dist, -1, -1);
							// expect a lot of replicated values, so check first
							// if it's not there.
							// if( !contains( combinations, comb, d) )
							if (!combinations.contains(comb))
								combinations.add(comb);
						}
					}
				}

			}

		}

		// sort on distance:
		Collections.sort(combinations, new CombinationComparator());

		int round = 0;

		double[] fit_att = new double[d];
		System.arraycopy(att, 0, fit_att, 0, d);

		// mark points to check in first round
		int checkedCount = 0;
		for (int i = 0; i < combinations.size(); i++) {
			Combination comb = combinations.get(i);
			double[] t_att = comb.getAtt();
			if (checkToMark_MDM(t_att, width, d)) // **** Using the maximum
													// width.
			{
				comb.setRound(round);
				checkedCount++;
			}
		}

		compex = 0;

		double[] curr_att = new double[d];
		System.arraycopy(att, 0, curr_att, 0, d);

		while (round < rounds) {
			// int improve = 0;
			double limit = 0.0;
			// mas{
			int[] checked = new int[10000000]; // size of table?
			int num_check = 0;
			// mas}

			for (int i = 0; i < combinations.size(); i++) {
				if (combinations.get(i).getRound() == round)//
				{
					Combination comb = combinations.get(i);
					// mas{
					checked[num_check] = i;
					num_check++;
					// mas}

					// System.out.println("check: " + combinations[i][0] + ", "+
					// combinations[i][1]);
					double[] temp_att = comb.getAtt();
					double Es = computeDistML_MDM(_att, temp_att, d, scale);

					if (!SHORT || (1 - w) * Es <= mini_err) {
						Tuple lower = getClosestLower_MDM(table, temp_att, d);
						Tuple upper = getClosestUpper_MDM(table, temp_att, d);

						double klower;
						double kupper;

						klower = (mini_err - (1.0 - w) * Es) / w;
						klower = 1.0 - klower;
						klower = k * klower;

						kupper = (mini_err - (1.0 - w) * Es) / w;
						kupper = 1.0 - kupper;
						kupper = k / kupper;

						// if(lower==null || upper==null || lower.getK() >=
						// klower && lower.getK() <= kupper || upper.getK() >=
						// klower && upper.getK() <= kupper ){
						if (!BOUND || lower == null || upper == null
								|| !(upper.getK() < klower || lower.getK() > kupper)) {
							if (comb.getK() == -1) {
								est = estCard_MDM(temp_att, sql_q, d);
								compex++;
								comb.setK(est);
								// System.out.print("SAQR-CSP ");
								// print(temp_att);
								// System.out.println(combinations[i][0] + ", "+
								// combinations[i][1]);
							} else
								est = comb.getK();

							double Ec = (Math.abs(est - k)) / (double) getMax(est, k);
							double Et = w * Ec + (1 - w) * Es;
							// Add this new curr_att to the table:
							// (adding new points to table should be selective,
							// otherwise will end up with v large table)

							Tuple t = new Tuple(temp_att, est, d, Ec);
							if (!table.contains(t))
								table.add(t);

							// Dev dev_item = new Dev(Et, est, temp_att, d);
							// dev.add(dev_item);

							if (Et < mini_err) {
								mini_err = Et;
								double[] pAtt = comb.getAtt();
								System.arraycopy(pAtt, 0, curr_att, 0, d);
								// improve = 1;
							}

							if (EARLY && limit > mini_err)
								break;

						}
					} else {
						if (EARLY && limit > (1 - w) * Es)
							break;
					}

					limit = 0.0 + (1 - w) * Es;
				}
			}

			round++;
			double oldWidth = width;
			// mas{
			// If SAQR
			width = width / 2.0;
			// mas}

			if (width < _minWidth)
				break;

			for (int i = 0; i < num_check; i++) {
				Combination cc = combinations.get(checked[i]);
				Combination corner = new Combination(cc);

				double[] p_att = new double[d]; // pointer to att[] in corner
				p_att = corner.getAtt();

				int numCor = 4;
				double[][] corners = new double[numCor][d];
				/*
				 * corners[0][0] = p_att[0]-width*ranges[0]; corners[0][1] =
				 * p_att[1]-width*ranges[0]; corners[0][2] = p_att[2];
				 * corners[0][3] = p_att[3];
				 * 
				 * corners[1][0] = p_att[0]-width*ranges[0]; corners[1][1] =
				 * p_att[1]; corners[1][2] = p_att[2]+width*ranges[0];
				 * corners[1][3] = p_att[3];
				 * 
				 * corners[2][0] = p_att[0]; corners[2][1] =
				 * p_att[1]-width*ranges[0]; corners[2][2] = p_att[2];
				 * corners[2][3] = p_att[3]+width*ranges[0];
				 * 
				 * corners[3][0] = p_att[0]; corners[3][1] = p_att[1];
				 * corners[3][2] = p_att[2]+width*ranges[0]; corners[3][3] =
				 * p_att[3]+width*ranges[0];
				 */

				corners[0][0] = p_att[0] - width;
				corners[0][1] = p_att[1] - width;
				corners[0][2] = p_att[2];
				corners[0][3] = p_att[3];

				corners[1][0] = p_att[0] - width;
				corners[1][1] = p_att[1];
				corners[1][2] = p_att[2] + width;
				corners[1][3] = p_att[3];

				corners[2][0] = p_att[0];
				corners[2][1] = p_att[1] - width;
				corners[2][2] = p_att[2];
				corners[2][3] = p_att[3] + width;

				corners[3][0] = p_att[0];
				corners[3][1] = p_att[1];
				corners[3][2] = p_att[2] + width;
				corners[3][3] = p_att[3] + width;

				double min_dist = 2.0;
				double max_dist = 0.0;
				double[] closest = corner.getAtt();
				double[] furthest = corner.getAtt();

				for (int j = 0; j < numCor; j++) {
					// System.out.println( "n: " + corners[j][0]+ ", " +
					// corners[j][1]);
					double dist = computeDistML_MDM(_att, corners[j], d, scale);
					// double dist = computeDistPerpendicular(_att, corners[j],
					// d, scale);
					if (dist < min_dist) {
						min_dist = dist;
						closest = corners[j];
					}
					if (dist > max_dist) {
						max_dist = dist;
						furthest = corners[j];
					}
				}

				// System.out.println( "closest: " + closest[0]+ ", " +
				// closest[1]);
				// now find the bounds on K
				// maximum K is at the point itself!
				// System.out.println( "upper: " +combinations [checked[i]][0]+
				// ", " + combinations [checked[i]][1]);
				Combination ccc = combinations.get(checked[i]);
				Combination corner2 = new Combination(ccc);
				Tuple upper = getClosestUpper_MDM(table, corner2.getAtt(), d);
				// Tuple upper = getClosestUpper(table, corners[3], d);
				double kmax = upper.getK();

				Tuple lower = getClosestLower_MDM(table, corner.getAtt(), d);
				double kmin;
				if (lower == null)
					kmin = 0; // to fix bug
				else
					kmin = lower.getK();

				// est = estCard( corner , sql_q, d);
				// if(est!=klower){
				// System.out.println(">>>>>>>>>problem lower>>>>>>>>>>>>");
				// }

				double klower;
				double kupper;

				double Es = min_dist;
				// double Es = (min_dist+max_dist)/2.0;

				klower = (mini_err - (1.0 - w) * Es) / w;
				klower = 1.0 - klower;
				klower = k * klower;

				kupper = (mini_err - (1.0 - w) * Es) / w;
				kupper = 1.0 - kupper;
				kupper = k / kupper;

				// if(lower==null || upper==null || lower.getK() >= klower &&
				// lower.getK() <= kupper || upper.getK() >= klower &&
				// upper.getK() <= kupper ){

				if (!(kmax < klower || kmin > kupper)) { // expand
					// System.out.println("-----------------expand");
					int numChecked = 0;
					for (int j = 0; j < combinations.size(); j++) {
						Combination comb = combinations.get(j);
						Combination checkedComb = combinations.get(checked[i]);
						if (SAQR_isMarked_MDM(comb, width, oldWidth, checkedComb, d)) {
							comb.setRound(round);
							numChecked++;
							// print(comb.getAtt());
						}

					}
					// int ttt;
					// ttt = 0;
				}
				// int tt;
				// tt=0;
			}
		}

		System.arraycopy(curr_att, 0, att, 0, d);
		int estt = estCard(att, sql_q, d);
		double error = Math.abs(estt - k) / (double) getMax(estt, k);
		double dist = computeDistML_MDM(_att, att, d, scale);
		Result r = new Result(att, estt, compex, error, dist, d, w);

		// logger.debug("Refinement query: ", r.getAtt().toString());
		System.out.println("Refinement query: " + r.getAtt().toString());
		return r.getAtt();

	}

	public static boolean SAQR_isMarked_MDM(final Combination comb, final double width, final double oldWidth,
			final double[] ranges, final Combination checked, final int d) {
		boolean marked = true;
		double[] att = comb.getAtt();
		double[] ch_att = checked.getAtt();

		// NE:

		for (int i = 0; i < d - 2; i++) {
			if ((att[i] % (width * ranges[i])) != 0.0)
				marked = false;
		}

		for (int i = 2; i < d; i++) {
			if (!(att[i] <= ch_att[i] && att[i] > ch_att[i] - oldWidth * ranges[i]))
				marked = false;
		}
		return marked;
	}

	public static boolean SAQR_isMarked_MDM(final Combination comb, final double width, final double oldWidth,
			final Combination checked, final int d) {
		boolean marked = true;
		double[] att = comb.getAtt();
		double[] ch_att = checked.getAtt();

		// NE:

		for (int i = 0; i < d - 2; i++) {
			if ((att[i] % width) != 0.0)
				marked = false;
		}

		for (int i = 2; i < d; i++) {
			if (!(att[i] <= ch_att[i] && att[i] > ch_att[i] - oldWidth))
				marked = false;
		}
		return marked;
	}

	public static boolean checkToMark_MDM(final double[] att, final double width, final double[] ranges, final int d) {
		double[] ds = new double[d - 2];

		ds[0] = Math.abs(att[0] - att[2]) % (width * ranges[0]);
		ds[1] = Math.abs(att[1] - att[3]) % (width * ranges[0]);

		boolean mark = true;
		for (int i = 0; i < d - 2; i++)
			if (ds[i] != 0.0)
				mark = false;

		return mark;
	}

	public static boolean checkToMark_MDM(final double[] att, final double width, final int d) {
		double[] ds = new double[d - 2];

		ds[0] = Math.abs(att[0] - att[2]) % width;
		ds[1] = Math.abs(att[1] - att[3]) % width;

		boolean mark = true;
		for (int i = 0; i < d - 2; i++)
			if (ds[i] != 0.0)
				mark = false;

		return mark;
	}

	private static Tuple getClosestUpper_MDM(final ArrayList<Tuple> table, final double[] att, final int d) {
		boolean foundAtLeastOne = false;
		if (table.size() <= 1)
			return null;

		Tuple upper = table.get(1);
		for (int i = 0; i < table.size(); i++) {
			Tuple curr_tuple = table.get(i);
			if (greaterThanOrEq_MDM(curr_tuple.getAtt(), att, d))
				if (lessThanOrEq_MDM(curr_tuple.getAtt(), upper.getAtt(), d)) {
					upper = curr_tuple;
					foundAtLeastOne = true;
				}
		}
		if (foundAtLeastOne)
			return upper;
		else
			return upper;
	}

	private static Tuple getClosestLower_MDM(final ArrayList<Tuple> table, final double[] att, final int d) {
		boolean foundAtLeastOne = false;
		if (table.size() <= 1)
			return null;

		Tuple lower = null;
		for (int i = 0; i < table.size(); i++) {
			Tuple curr_tuple = table.get(i);
			if (lessThanOrEq_MDM(curr_tuple.getAtt(), att, d)) {
				lower = curr_tuple;
				break;
			}
		}

		for (int i = 0; i < table.size(); i++) {
			Tuple curr_tuple = table.get(i);
			if (lessThanOrEq_MDM(curr_tuple.getAtt(), att, d))
				if (greaterThanOrEq_MDM(curr_tuple.getAtt(), lower.getAtt(), d)) {
					lower = curr_tuple;
					foundAtLeastOne = true;
				}
		}
		if (foundAtLeastOne)
			return lower;
		else
			return lower;
	}

	// if a < b
	private static boolean lessThanOrEq_MDM(final double[] a, final double[] b, final int d) {
		if (a[2] >= b[2] && a[3] >= b[3]) {
			if (a[0] < b[0] && a[1] < b[1])
				return true;
		}

		if (a[2] > b[2] && a[3] > b[3]) {
			if (a[0] <= b[0] && a[1] <= b[1])
				return true;
		}

		return false;
	}

	// For MDM,
	// b is the initial query.
	// a is the candidate q.
	private static boolean greaterThanOrEq_MDM(final double[] a, final double[] b, final int d) {
		if (a[2] <= b[2] && a[3] <= b[3]) // for SW
		{
			if (a[0] > b[0] && a[1] > b[1])
				return true;
		}

		if (a[2] < b[2] && a[3] < b[3]) {
			if (a[0] >= b[0] && a[1] >= b[1])
				return true;
		}

		return false;
	}

	public static double computeDistML(final double[] q, final double[] p, final int d, final double scale) {
		// double sim = 0.0;
		// double scale = 99.0;

		double tot = 0.0;
		for (int i = 0; i < d; i++) {
			double dist = Math.abs((q[i] - p[i]) / scale);

			// dist = Math.pow(dist, 2);
			tot += dist;
		}
		tot = tot / d;
		// sim = Math.sqrt(tot );
		return tot;

	}

	// distance between two points q and p
	public static double computeDistML_MDM(final double[] q, final double[] p, final int d, final double scale) {
		// double sim = 0.0;
		// double scale = 99.0;
		double distance_NE = (Math.sqrt((q[0] - p[0]) * (q[0] - p[0]) + (q[1] - p[1]) * (q[1] - p[1]))) / scale;
		double distance_SW = (Math.sqrt((q[2] - p[2]) * (q[2] - p[2]) + (q[3] - p[3]) * (q[3] - p[3]))) / scale;

		return (distance_NE + distance_SW) / 2.0;
		/*
		 * double tot = 0.0; for( int i=0; i < d ; i++ ) { double dist =
		 * Math.abs((q[i]-p[i])/scale);
		 * 
		 * //dist = Math.pow(dist, 2); tot += dist; } tot = tot/(double)d; //sim
		 * = Math.sqrt(tot ); return tot;
		 */
	}

	// compute perpendicular distance btw query Q and corner point p.
	public static double computeDistPerpendicular(final double[] q, final double[] p, final int d, final double scale) {
		// compute the perpendicular distance between Query q and Point point:
		double distance = getSimilarity(q, p, d, scale);
		return distance;
	}

	// To compute the similartiy between Query Q and data point P. Both Q & P
	// must have the same number of dimensions.
	// Based on DASFAA paper, The Euclidean distance is used.
	// Query's predicates have the single-side form: X < C
	// P can also be the refined Query Q'.
	public static double getSimilarity(final double[] q, final double[] p, final int d, final double scale) // q:
																											// Query
																											// p:
																											// Point/refiend
																											// query
																											// Q'
	{
		double sim = 0.0;

		double tot = 0.0;
		for (int i = 0; i < d; i++) {
			double dist = getDistance(q[i], p[i], scale);
			dist = Math.pow(dist, 2);
			tot += dist;
		}
		tot = tot / d;
		sim = Math.sqrt(tot);
		return sim;
	}

	// based on DASFAA paper.
	private static double getDistance(final double q, final double p, final double scale) {
		// scale to normalize distances between [0-1]
		// scale = (dmax-dmin);
		// double dmax;
		// double dmin;
		// double scale = 99.0;

		// if( q <= p )
		// return 0.0;
		// else
		return Math.abs((q - p)) / scale;
	}

	// To compute the similartiy between Query Q and data point P. Both Q & P
	// must have the same number of dimensions.
	// Based on DASFAA paper, The Euclidean distance is used.
	// Query's predicates have the double-sides form: C1< X < C2
	//
	public static double getSimilarityDoubleSides(final double[] q_l, final double[] q_h, final double[] p) // q_l:
																											// Query
																											// Lower
																											// limits
																											// q_h:Q
																											// upper
																											// limits
																											// p:
																											// Point
	{
		double sim = 0.0;

		int tot = 0;
		for (int i = 0; i < q_l.length; i++) {
			double d = getDistance(q_l[i], q_h[i], p[i]);
			d = Math.pow(d, 2);
			tot += d;
		}
		sim = Math.sqrt(tot);
		return sim;
	}

	// based on DASFAA paper.
	private static double getDistance(final double q_l, final double q_h, final double p, final double scale) {
		if (p > q_h)
			return (p - q_h) / scale;
		else if (p < q_l)
			return (q_l - p) / scale;
		else
			return 0.0;
	}

	public static void print(final Result[] r) {
		for (int i = 0; i < r.length; i++)
			r[i].print2();
	}

	// for predicates: X <= C
	public static int estCard(final double[] arr, final String q, final int d) {
		int card = -1;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String url = "jdbc:mysql://localhost:3306/bussines";
			String user = "root";
			String password = "";

			con = DriverManager.getConnection(url, user, password);
			// pst =
			// con.prepareStatement("select * from r50k1att1to100 where x<=(?);");
			pst = con.prepareStatement(q);
			for (int i = 0; i < d; i++) {
				pst.setDouble((i + 1), arr[i]);
			}
			/*
			 * if( d == 1 ) pst.setDouble(1, arr[0]); if( d == 2 ) {
			 * //pst.setInt(1, (int)arr[0]); pst.setDouble(1, arr[0]);
			 * //pst.setInt(2, (int)arr[1]); pst.setDouble(2, arr[1]); } if( d
			 * == 3 ) { pst.setDouble(1, arr[0]); pst.setDouble(2, arr[1]);
			 * pst.setDouble(3, arr[2]); } if( d == 4 ) { pst.setDouble(1,
			 * arr[0]); pst.setDouble(2, arr[1]); pst.setDouble(3, arr[2]);
			 * pst.setDouble(4, arr[3]); }
			 */
			rs = pst.executeQuery();
			while (rs.next())
				card = rs.getInt("count");

			con.close();
			pst.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// System.out.println("estCard returns: "+card);
		return card;
	}

	// sql_q : select count(*) as count where long >= long_SW & long <= long_NE
	// &
	// lat >= lat_SW & lat <= lat_NE;

	// **Wrong! double arr={long_NE, lat_NE, long_SW, lat_SW}**
	// double arr={lat_NE, lon_NE, lat_SW, lon_SW}
	public static int estCard_MDM(final double[] arr, final String q, final int d) {
		int card = -1;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String url = "jdbc:mysql://localhost:3306/bussines";
			String user = "root";
			String password = "";

			con = DriverManager.getConnection(url, user, password);

			pst = con.prepareStatement(q);
			double[] real = toReal(arr, d, 33.0952238976, 32.5414967546, -116.928885733, -117.281367428);
			pst.setDouble(1, real[0]);
			pst.setDouble(2, real[1]);
			pst.setDouble(3, real[2]);
			pst.setDouble(4, real[3]);

			rs = pst.executeQuery();
			while (rs.next())
				card = rs.getInt("count");

			con.close();
			pst.close();
			rs.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		// System.out.println("estCard returns: "+card);
		return card;
	}

	public static void print(final int[][] a) {
		System.out.print("\t");
		// print the header:
		for (int i = 0; i < a[0].length; i++) {
			System.out.print(i);
			System.out.print("\t");
		}
		System.out.println();

		for (int i = 0; i < a.length; i++) {
			System.out.print(i);
			System.out.print("\t");
			for (int j = 0; j < a[i].length; j++) {
				System.out.print(a[i][j]);
				System.out.print("\t");

			}
			System.out.println(); // new line
		}
	}

	public static void print(final double[] a) {
		System.out.print("[");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if (i < a.length - 1)
				System.out.print(",");
		}
		System.out.println("]");
	}

	public static void print2(final double[] a) {
		System.out.print("[");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if (i < a.length - 1)
				System.out.print(",");
		}
		System.out.print("]");
	}

	public static void print(final double[] a, final String q, final int d) {
		System.out.print("[");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if (i < a.length - 1)
				System.out.print(",");
		}
		int est = estCard(a, q, d);
		System.out.println("]: " + est);
	}

	public static void print(final int[] a) {
		System.out.print("[");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if (i < a.length - 1)
				System.out.print(",");
		}
		System.out.println("]");
	}

	private static int getMax(final int a, final int b) {
		if (a > b)
			return a;
		else
			return b;
	}

	// based on a[lat_NE, long_NE, lat_SW, long_SW ]
	public static double[] toNorm(final double[] a, final int d, final double lat_max, final double lat_min,
			final double long_max, final double long_min) {
		double[] norm_val = new double[d];

		norm_val[0] = (a[0] - lat_min) / (lat_max - lat_min);
		norm_val[2] = (a[2] - lat_min) / (lat_max - lat_min);

		norm_val[1] = (a[1] - long_min) / (long_max - long_min);
		norm_val[3] = (a[3] - long_min) / (long_max - long_min);

		return norm_val;
	}

	// based on a[lat_NE, long_NE, lat_SW, long_SW ]
	public static double[] toReal(final double[] a, final int d, final double lat_max, final double lat_min,
			final double long_max, final double long_min) {
		double[] real_val = new double[d];

		real_val[0] = (a[0] * (lat_max - lat_min)) + lat_min;
		real_val[2] = (a[2] * (lat_max - lat_min)) + lat_min;

		real_val[1] = (a[1] * (long_max - long_min)) + long_min;
		real_val[3] = (a[3] * (long_max - long_min)) + long_min;

		return real_val;

	}

}
