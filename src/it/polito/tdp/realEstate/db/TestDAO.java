package it.polito.tdp.realEstate.db;

import java.util.List;
import java.util.Map;

import it.polito.tdp.realEstate.model.Immobile;
import it.polito.tdp.realEstate.model.Zona;

public class TestDAO {

	public static void main(String[] args) {
		

		RealEstateDAO dao = new RealEstateDAO();

//		Map<Integer, Zona> zone = dao.getAllZones();
//		System.out.println(zone);
//
//		List<Zona> elenco = dao.getElencoPerCarattristica("AVG(p.Violent_Crime_Rate)", "Decrescente");
//		System.out.println(elenco);
//
//		Map<String, Immobile> immobiliFiltrati = dao.getImmobiliFiltrati(19104, "SingleFamily",
//		50, 100, 1950, 1970);
//		System.out.println(immobiliFiltrati);
//		System.out.println(immobiliFiltrati.size());
//
//		//mq
//		System.out.println("\n MQ \n");
//		System.out.println(dao.getAllMq()+"\n");
//		System.out.println(dao.getMaxMq()+"\n");
//		System.out.println(dao.getMinMq()+"\n");
//
//		//rent
//		System.out.println("\n RENT \n");
//		System.out.println(dao.getAllRents()+"\n");
//		System.out.println(dao.getMaxRent()+"\n");
//		System.out.println(dao.getMinRent()+"\n");
//		
//		//mq
//		System.out.println("\n YEAR \n");
//		System.out.println(dao.getAllYears()+"\n");
//		System.out.println(dao.getMaxYear()+"\n");
//		System.out.println(dao.getMinYear()+"\n");
//		
//		//mq
//		System.out.println("\n CRIME \n");
//		System.out.println(dao.getAllCrime()+"\n");
//		System.out.println(dao.getMaxCrime()+"\n");
//		System.out.println(dao.getMinCrime()+"\n");
//		
//
//		//mq
//		System.out.println("\n SCHOOL \n");
//		System.out.println(dao.getAllSchools()+"\n");
//		System.out.println(dao.getMaxSchool()+"\n");
//		System.out.println(dao.getMinSchool()+"\n");
		

		System.out.println(dao.getImmobiliFiltrati(19115, null, null, null, null, null)+"\n");
		
	}

}
