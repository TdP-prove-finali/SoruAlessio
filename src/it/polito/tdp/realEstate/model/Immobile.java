package it.polito.tdp.realEstate.model;

public class Immobile implements Comparable<Immobile>{
	
	private String address; // è la chiave primaria
	
	private Integer postalCode; // zona
	private Integer crimeRate;
	private Integer schoolRate;
	private float marketValue;
	private Integer estimatedRent;
	private Integer yearBuilt;
	private Integer mq; 
	private String propType;
	
	private float punteggio=0;
	
	public Immobile(String address, Integer postalCode, Integer crimeRate, Integer schoolRate, 
			float marketValue, Integer estimatedRent, Integer yearBuilt, Integer mq, 
			String propType) {
		super();
		this.address = address;
		this.postalCode = postalCode;
		this.crimeRate = crimeRate;
		this.schoolRate = schoolRate;
		this.marketValue = marketValue;
		this.estimatedRent = estimatedRent;
		this.yearBuilt = yearBuilt;
		this.mq = mq;
		this.propType = propType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	public Integer getCrimeRate() {
		return crimeRate;
	}

	public void setCrimeRate(Integer crimeRate) {
		this.crimeRate = crimeRate;
	}

	public Integer getSchoolRate() {
		return schoolRate;
	}

	public void setSchoolRate(Integer schoolRate) {
		this.schoolRate = schoolRate;
	}

	public float getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(float marketValue) {
		this.marketValue = marketValue;
	}

	public Integer getEstimatedRent() {
		return estimatedRent;
	}

	public void setEstimatedRent(Integer estimatedRent) {
		this.estimatedRent = estimatedRent;
	}

	public Integer getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(Integer yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public Integer getMq() {
		return mq;
	}

	public void setMq(Integer mq) {
		this.mq = mq;
	}

	public String getPropType() {
		return propType;
	}

	public void setPropType(String propType) {
		this.propType = propType;
	}

	public float getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(float punteggio) {
		this.punteggio = punteggio;
	}

	@Override
	public String toString() {
		return address;
	}

	@Override
	public int compareTo(Immobile imm) {
		// li compare in base al punteggio
		return -(int) ((this.punteggio-imm.getPunteggio())*1000);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// address è chiave primaria
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Immobile other = (Immobile) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}
	
	
	
	
	

}
