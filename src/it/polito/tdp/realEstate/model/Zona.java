package it.polito.tdp.realEstate.model;

public class Zona implements Comparable<Zona>{

	private Integer postalCode;
	private String neighborhoodName;
	
	private float avgCrimeRate;
	private float avgSchoolRate;
	private float marketValue;
	private float avgRent;
	private Integer avgYear;
	private float avgMq;
	
	public Zona(Integer postalCode, String neighborhoodName, float avgCrimeRate, 
			float avgSchoolRate, float marketValue, float avgRent,
			Integer avgYear, float avgMq) {
		super();
		this.postalCode = postalCode;
		this.neighborhoodName = neighborhoodName;
		this.avgCrimeRate = avgCrimeRate;
		this.avgSchoolRate = avgSchoolRate;
		this.marketValue = marketValue;
		this.avgRent = avgRent;
		this.avgYear = avgYear;
		this.avgMq = avgMq;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	public String getneighborhoodName() {
		return neighborhoodName;
	}

	public void setNeighborhoodName(String neighborhoodName) {
		this.neighborhoodName = neighborhoodName;
	}

	public float getAvgCrimeRate() {
		return avgCrimeRate;
	}

	public void setAvgCrimeRate(float avgCrimeRate) {
		this.avgCrimeRate = avgCrimeRate;
	}

	public float getAvgSchoolRate() {
		return avgSchoolRate;
	}

	public void setAvgSchoolRate(float avgSchoolRate) {
		this.avgSchoolRate = avgSchoolRate;
	}

	public float getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(float marketValue) {
		this.marketValue = marketValue;
	}

	public Integer getAvgYear() {
		return avgYear;
	}

	public void setAvgYear(Integer avgYear) {
		this.avgYear = avgYear;
	}
	

	public float getAvgRent() {
		return avgRent;
	}

	public void setAvgRent(float avgRent) {
		this.avgRent = avgRent;
	}

	public float getAvgMq() {
		return avgMq;
	}

	public void setAvgMq(float avgMq) {
		this.avgMq = avgMq;
	}
	
	

	@Override
	public String toString() {
		return postalCode + " - " + neighborhoodName;
	}

	public String getValoreDaMetodoCaratteristica(String caratteristica) {

    	System.out.println(caratteristica);
		switch(caratteristica) {
		case "Tasso di criminalità":
			System.out.println(String.valueOf(this.getAvgCrimeRate()));
			return String.valueOf(this.getAvgCrimeRate());
		case "Livello scolastico":
			return String.valueOf(this.getAvgSchoolRate());
		case "Valore di mercato medio":
			return String.valueOf(this.getMarketValue())+" $";
		case "Affitto medio stimato":
			return String.valueOf(this.getAvgRent())+" $";
		case "Anno di costruzione":
			return String.valueOf(this.getAvgYear());
		case "Metratura media":
			return String.valueOf(this.getAvgMq())+" mq";		
		}
		

		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// zona identificata univocamente dal postal code
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Zona other = (Zona) obj;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		return true;
	}

	@Override
	public int compareTo(Zona altra) {
		return this.postalCode.compareTo(altra.getPostalCode());
	}
	
	
	
}
