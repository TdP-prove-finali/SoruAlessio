package it.polito.tdp.realEstate.model;

import java.util.List;
import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		
//		System.out.println(model.getZoneIdMap());
		
//
//		long startTime = System.nanoTime();
//		System.out.println(model.getElencoPerCarattristica("AVG(p.Zillow_Estimate)", "DESC"));
//		long endTime = System.nanoTime();
//		System.out.println(endTime-startTime);
		
//		long startTime = System.nanoTime();
//		System.out.println(model.getZonaMigliorePerCaratteristica("Valore di mercato medio"));
//		long endTime = System.nanoTime();
//		System.out.println(endTime-startTime);
		

		long startTime = System.nanoTime();
		System.out.println(model.gestioneFiltri(19138, "Townhouse", null, null, null, null, 50, 30, 11, 61, 87));
		long endTime = System.nanoTime();
		System.out.println("t: "+(endTime-startTime));
		
//		long startTime = System.nanoTime();
//		List<Float> budgetList = model.gestioneFiltri(19104, null, 
//				null, null, null, null,
//				10, 20, 30, 40, 50,
//				null);
		// budget in zero è il massimoBufgetMinimo
		// budget in 1 è il massimo sforamento
//		Float maxBmin = budgetList.get(0);
//		Float maxSforamento = budgetList.get(1);
//		Float budget = (float) 100000;
//		System.out.println("Sforo:"+maxSforamento);
//		if(budget>maxBmin) {
//			System.out.println("NON PUOI AVERE UN BUDGET COSì ALTO !!");
//		} else {
//////			List<Immobile> portafoglio = model.getPortafoglioImmobiliare(false, 
////					budget, maxSforamento); // qualita true, quantita false
//			long endTime = System.nanoTime();
//			long duration = (endTime-startTime)/1000000 ; //  in millisecondi
//			
//			float costo = 0;
//			float punteggio = 0;
//	
////			System.out.println("\n ------ ECCO IL TUO PORTAFOGLIO! -------\n");
////			for(Immobile i : portafoglio) {
////				System.out.println(i.toString()+" - "+i.getMarketValue());
////				costo+= i.getMarketValue();
////				System.out.println("costo "+costo);
////				System.out.println("punteggio singolo: "+i.getPunteggio());
////			}
//			System.out.println("costo "+costo);
//	
//			System.out.println(" punteggio: "+model.getbestPunteggioPortafoglioMedio()+"\nDURATA: "+duration+"ms");
//	
//			System.out.println("TIMEOUT "+model.isTimeout());
//		}
	}

}
