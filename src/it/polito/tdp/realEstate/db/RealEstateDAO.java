package it.polito.tdp.realEstate.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.realEstate.model.Immobile;
import it.polito.tdp.realEstate.model.Zona;



public class RealEstateDAO {

	public Map<Integer, Zona> getAllZones() {

		// sto selezionando le zone che ci sono in entrambi i db con una join
		String sql = "SELECT p.Postal_Code AS postalCode, " + 
				"Neighborhood AS neighborhoodName, " + 
				"AVG(p.Violent_Crime_Rate) AS avgCrimeRate, " + 
				"AVG(p.School_Score) AS avgSchoolRate, " + 
				"AVG(p.Zillow_Estimate) AS marketValue, " + 
				"AVG(p.Rent_Estimate ) AS avgRent, " + 
				"AVG(p.yearBuilt) AS avgYear, " + 
				"((AVG(p.finished_SqFt))/10.764) AS avgMq " + 
				"FROM properties AS p, neighborhood AS n " + 
				"WHERE p.Postal_Code = n.Postal_Code AND " + 
				"	p.Violent_Crime_Rate!=0 AND " + 
				"	p.School_Score!=0 AND " + 
				"	p.Zillow_Estimate!=0 AND " + 
				"	p.Rent_Estimate!=0 AND  " + 
				"	p.yearBuilt!=0 AND  " + 
				"	((p.finished_SqFt)/10.764)!=0  " + 
				"GROUP BY postalCode, neighborhoodName ";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Map<Integer, Zona> zone = new HashMap<Integer, Zona>();
			while (rs.next()) {
				Zona z = new Zona(rs.getInt("postalCode"), rs.getString("neighborhoodName"),
						rs.getFloat("avgCrimeRate"), rs.getFloat("avgSchoolRate"), 
						rs.getFloat("marketValue"), rs.getFloat("avgRent"),
						rs.getInt("avgYear"), rs.getFloat("avgMq"));
				zone.put(rs.getInt("postalCode"), z); // id e oggetto
			}

			conn.close();
			return zone;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Zona> getElencoPerCarattristica(String caratteristica, String ordine) {
		if(ordine == "Crescente")
			ordine="ASC";
		else if(ordine == "Decrescente")
			ordine = "DESC";		
		//ordino già nella query		
		String sql = "SELECT p.Postal_Code AS postalCode, " + 
				" n.Neighborhood AS neighborhoodName, " + 
				" AVG(p.Violent_Crime_Rate) AS avgCrimeRate, " + 
				" AVG(p.School_Score) AS avgSchoolRate, " + 
				" AVG(p.Zillow_Estimate) AS marketValue, " + 
				" AVG(p.Rent_Estimate ) AS avgRent, " + 
				" AVG(p.yearBuilt) AS avgYear, " + 
				" ((AVG(p.finished_SqFt))/10.764) AS avgMq " + 
				"FROM properties AS p, neighborhood AS n " + 
				"WHERE p.Postal_Code = n.Postal_Code AND " + 
				"	p.Violent_Crime_Rate!=0 AND " + 
				"	p.School_Score!=0 AND " + 
				"	p.Zillow_Estimate!=0 AND " + 
				"	p.Rent_Estimate!=0 AND  " + 
				"	p.yearBuilt!=0 AND  " + 
				"	((p.finished_SqFt)/10.764)!=0  " + 
				"GROUP BY postalCode, neighborhoodName " + 
				"ORDER BY "+caratteristica+" "+ordine+" " ;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Zona> zone = new ArrayList<Zona>();
			while (rs.next()) {
				Zona z = new Zona(rs.getInt("postalCode"), rs.getString("neighborhoodName"),
						rs.getFloat("avgCrimeRate"), rs.getFloat("avgSchoolRate"), 
						rs.getFloat("marketValue"), rs.getFloat("avgRent"), rs.getInt("avgYear"),
						rs.getFloat("avgMq"));
				zone.add(z); // id e oggetto
			}
			conn.close();
			return zone;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Map<String, Immobile> getImmobiliFiltrati(Integer postalCode, String propType, 
			Integer minMq, Integer maxMq, Integer minYear, Integer maxYear) {
		
		
		String filtriWhere="";
		if(propType!=null)
			filtriWhere += "AND PropType = ? ";
		if (postalCode!=null) 
			filtriWhere += "AND Postal_Code = ? ";
		if (minMq!=null) 
			filtriWhere += "AND (finished_SqFt/10.764) > ? ";
		if (maxMq!=null) 
			filtriWhere += "AND (finished_SqFt/10.764) < ? ";
		if (minYear!=null) 
			filtriWhere += "AND yearBuilt > ? ";
		if (maxYear!=null) 
			filtriWhere += "AND yearBuilt < ? ";
		// eventualmente aggiungere anche filtri su bagni e camere
		
		String sql = "SELECT Address, Postal_Code, Violent_Crime_Rate, " + 
				"	School_Score, Zillow_Estimate, Rent_Estimate, " + 
				"	yearBuilt, (finished_SqFt/10.764) AS mq, " + 
				"	PropType " + 
				" FROM properties p " + 
				"WHERE Postal_Code!=0 AND " + 
				"	Violent_Crime_Rate!=0 AND " + 
				"	School_Score!=0 AND " + 
				"	Zillow_Estimate!=0 AND " + 
				"	Rent_Estimate!=0 AND " + 
				"	yearBuilt!=0 AND " + 
				"	((finished_SqFt)/10.764)>10 AND " + 
				"	Address!='' AND " + 
				"	PropType!='' "+filtriWhere ;
		
		// se decido di filtrare anche su camere e bagni, inserisco anche
		// 

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			int i=1; // count dei ?, se non c'è allore il ?  numero i è il successivo
			if(propType!=null) {
				st.setString(i, propType);
				i++;
			}
			if (postalCode!=null)  {
				st.setInt(i, postalCode);
				i++;
			}
			if (minMq!=null)  {
				st.setInt(i, minMq);
				i++;
			}
			if (maxMq!=null)  {
				st.setInt(i, maxMq);
				i++;
			}
			if (minYear!=null)  {
				st.setInt(i, minYear);
				i++;
			}
			if (maxYear!=null)  {
				st.setInt(i, maxYear);
				i++;
			}
			// eventualmente aggiungere anche filtri su bagni e camere			

//	    	System.out.println(st);

			ResultSet rs = st.executeQuery();

			Map<String, Immobile> immobiliFiltrati = new HashMap<String, Immobile>();
			while (rs.next()) {
				Immobile z = new Immobile(rs.getString("Address"), rs.getInt("Postal_Code"),
						(int)((rs.getFloat("Violent_Crime_Rate"))*100), (int)((rs.getFloat("School_Score"))*100), 
						(rs.getFloat("Zillow_Estimate")), (int)rs.getFloat("Rent_Estimate"),
						rs.getInt("yearBuilt"), (int)(rs.getDouble("mq")), rs.getString("PropType"));
				immobiliFiltrati.put(z.getAddress(), z); // id e oggetto
//				System.out.println(z.getYearBuilt());
			}

			conn.close();
			return immobiliFiltrati;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Map<Integer, Float> getAllMq() {
		String sql = "SELECT DISTINCT (finished_SqFt/10.764) AS mq " + 
				"FROM properties  " + 
				"WHERE ((finished_SqFt)/10.764)>10 AND " + 
				" ((finished_SqFt)/10.764)>10!='' " + 
				"ORDER BY mq ASC  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Map<Integer, Float> mqs = new HashMap<Integer, Float>();
			while (rs.next()) {
				mqs.put(rs.getInt("mq"), (float) 0);
			}

			conn.close();
			return mqs;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Integer getMaxMq() {
		String sql = "SELECT MAX(finished_SqFt/10.764) AS max " + 
				"FROM properties  " + 
				"WHERE ((finished_SqFt)/10.764)>10 AND " + 
				" ((finished_SqFt)/10.764)>10!=''   " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer maxMq=0;
			if (rs.next()) {
				maxMq = rs.getInt("max");
			}

			conn.close();
			return maxMq;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Integer getMinMq() {
		String sql = "SELECT MIN(finished_SqFt/10.764) AS min " + 
				"FROM properties  " + 
				"WHERE ((finished_SqFt)/10.764)>10 AND " + 
				" ((finished_SqFt)/10.764)>10!=''   " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer minMq=0;
			if (rs.next()) {
				minMq = rs.getInt("min");
			}

			conn.close();
			return minMq;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	

	public Map<Integer, Float> getAllRents() {
		String sql = "SELECT DISTINCT Rent_Estimate AS rent " + 
				"FROM properties " + 
				"WHERE Rent_Estimate!=0  AND " + 
				" Rent_Estimate!='' " + 
				"ORDER BY rent asc " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Map<Integer, Float> rents = new HashMap<Integer, Float>();
			while (rs.next()) {
				rents.put(rs.getInt("rent"), (float) 0);
			}

			conn.close();
			return rents;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Integer getMaxRent() {
		String sql = "SELECT MAX(Rent_Estimate) AS max " + 
				"FROM properties " + 
				"WHERE Rent_Estimate!=0  AND " + 
				" Rent_Estimate!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer maxRent=0;
			if (rs.next()) {
				maxRent = rs.getInt("max");
			}

			conn.close();
			return maxRent;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Integer getMinRent() {
		String sql = "SELECT MIN(Rent_Estimate) AS min " + 
				"FROM properties " + 
				"WHERE Rent_Estimate!=0  AND " + 
				" Rent_Estimate!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer minRent=0;
			if (rs.next()) {
				minRent = rs.getInt("min");
			}

			conn.close();
			return minRent;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Map<Integer, Float> getAllYears() {
		String sql = "SELECT DISTINCT yearBuilt " + 
				"FROM properties " + 
				"WHERE yearBuilt!=0  AND " + 
				" yearBuilt!='' " + 
				" ORDER BY yearBuilt ASC  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Map<Integer, Float> years = new HashMap<Integer, Float>();
			while (rs.next()) {
//				System.out.println(rs.getInt("yearBuilt"));
				years.put(rs.getInt("yearBuilt"), (float) 0);
			}

			conn.close();
			return years;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Integer getMaxYear() {
		String sql = "SELECT MAX(yearBuilt) as max " + 
				"FROM properties  " + 
				"WHERE yearBuilt!=0 AND yearBuilt!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer maxYear=0;
			if (rs.next()) {
				maxYear = rs.getInt("max");
			}

			conn.close();
			return maxYear;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Integer getMinYear() {
		String sql = "SELECT MIN(yearBuilt) as min " + 
				"FROM properties  " + 
				"WHERE yearBuilt!=0 AND yearBuilt!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer minYear=0;
			if (rs.next()) {
				minYear = rs.getInt("min");
			}

			conn.close();
			return minYear;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Map<Integer, Float> getAllCrime() {
		String sql = "SELECT distinct Violent_Crime_Rate AS crime " + 
				"FROM properties " + 
				"WHERE Violent_Crime_Rate!=0  AND " + 
				" Violent_Crime_Rate!='' "+
				 "ORDER BY crime ASC " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
//			System.out.println(st);

			Map<Integer, Float> crimes = new HashMap<Integer, Float>();
			while (rs.next()) {
				int cr = (int)((rs.getFloat("crime"))*100);
//				System.out.println(cr+"\n");
				crimes.put(cr, (float) 0);
			}

			conn.close();
			return crimes;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Integer getMaxCrime() {
		String sql = "SELECT MAX(Violent_Crime_Rate) as max " + 
				"FROM properties  " + 
				"WHERE Violent_Crime_Rate!=0 AND Violent_Crime_Rate!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer maxCrime=0;
			if (rs.next()) {
				maxCrime = (int)((rs.getFloat("max"))*100);
			}

			conn.close();
			return maxCrime;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Integer getMinCrime() {
		String sql = "SELECT MIN(Violent_Crime_Rate) as min " + 
				"FROM properties  " + 
				"WHERE Violent_Crime_Rate!=0 AND Violent_Crime_Rate!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer minCrime=0;
			if (rs.next()) {
				minCrime = (int)((rs.getFloat("min"))*100);
			}

			conn.close();
			return minCrime;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Map<Integer, Float> getAllSchools() {
		String sql = "SELECT DISTINCT School_Score AS school " + 
				"FROM properties " + 
				"WHERE School_Score!=0  AND " + 
				" School_Score!='' " + 
				"ORDER BY school ASC  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Map<Integer, Float> schools = new HashMap<Integer, Float>();
			while (rs.next()) {
				int sr = (int)((rs.getFloat("school"))*100);
				schools.put(sr, (float) 0);
			}

			conn.close();
			return schools;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public Integer getMaxSchool() {
		String sql = "SELECT MAX(School_Score) AS max " + 
				"FROM properties " + 
				"WHERE School_Score!=0  AND " + 
				" School_Score!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer maxSchool=0;
			if (rs.next()) {
				maxSchool = (int)((rs.getFloat("max"))*100);
			}

			conn.close();
			return maxSchool;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public Integer getMinSchool() {
		String sql = "SELECT MIN(School_Score) AS min " + 
				"FROM properties " + 
				"WHERE School_Score!=0  AND " + 
				" School_Score!=''  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();
			Integer minSchool=0;
			if (rs.next()) {
				minSchool = (int)((rs.getFloat("min"))*100);
			}

			conn.close();
			return minSchool;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<String> getAllTipologie() {
		String sql = "SELECT DISTINCT properties.PropType AS ptype " + 
				"FROM properties " + 
				"ORDER BY ptype ASC  " ;

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<String> tipi = new ArrayList<String>();
			while (rs.next()) {
				String tipo = rs.getString("ptype");
				tipi.add(tipo);
			}

			conn.close();
			return tipi;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	
}
