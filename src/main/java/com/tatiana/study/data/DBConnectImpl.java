package com.tatiana.study.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatiana.study.model.Bussines;
import com.tatiana.study.model.Incident;

@Repository
public class DBConnectImpl implements DBConnect {

	public final DataSource datasource;

	private static final Logger logger = LoggerFactory.getLogger(DBConnectImpl.class);

	@Autowired
	public DBConnectImpl(final DataSource datasource) {
		super();
		this.datasource = datasource;
	}

	private Connection getConnection() throws SQLException {
		return datasource.getConnection();

	}

	@Override
	public Bussines[] findBussines(final float nELat, final float nELng, final float sWLat, final float sWLng) {
		Statement stmt = null;
		String query = "select * from bussines.bussines where latit <= " + nELat + " and latit >= " + sWLat
				+ " and longit <= " + nELng + " and longit >= " + sWLng;
		System.out.println("The query is: " + query);

		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// Obtains total number of records retrieved from the database |X|.
			int i = 0;
			while (rs.next()) {
				i++;
			}
			Bussines[] bussinesArray = new Bussines[i];
			int j = 0;
			rs.beforeFirst();
			while (rs.next()) {
				Bussines c = new Bussines(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getInt(6), rs.getBigDecimal(7), rs.getBigDecimal(8));
				bussinesArray[j] = c;
				j++;
			}
			stmt.close();
			return bussinesArray;
		} catch (SQLException exception) {
			logger.error("Error running generateCities ", exception);
		}
		return null;
	}

	@Override
	public Incident[] findIncidents(final float nELat, final float nELng, final float sWLat, final float sWLng) {
		Statement stmt = null;
		String query = "select * from bussines.incidents where lat <= " + nELat + " and lat >= " + sWLat
				+ " and lon <= " + nELng + " and lon >= " + sWLng;
		System.out.println("The query is: " + query);

		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// Obtains total number of records retrieved from the database |X|.
			int i = 0;
			while (rs.next()) {
				i++;
			}
			Incident[] incidentArray = new Incident[i];
			int j = 0;
			rs.beforeFirst();
			while (rs.next()) {
				Incident c = new Incident(rs.getDate(1), rs.getBigDecimal(3), rs.getBigDecimal(2));
				incidentArray[j] = c;
				j++;
			}
			stmt.close();
			return incidentArray;
		} catch (SQLException exception) {
			logger.error("Error running findIncidents ", exception);
		}
		return null;
	}

	@Override
	public Incident[] findIncidents(final int maxPoints) {
		Statement stmt = null;
		String query = "select * from bussines.incidents LIMIT 0," + maxPoints;
		System.out.println("The query is: " + query);
		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// Obtains total number of records retrieved from the database |X|.
			int i = 0;
			while (rs.next()) {
				i++;
			}
			Incident[] incidentArray = new Incident[i];
			int j = 0;
			rs.beforeFirst();
			while (rs.next()) {
				Incident c = new Incident(rs.getDate(1), rs.getBigDecimal(3), rs.getBigDecimal(2));
				incidentArray[j] = c;
				j++;
			}
			stmt.close();
			return incidentArray;
		} catch (SQLException exception) {
			logger.error("Error running findIncidents ", exception);
		}
		return null;
	}
}
