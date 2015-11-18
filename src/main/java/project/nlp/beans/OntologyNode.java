package project.nlp.beans;

public class OntologyNode {

	private String entity;
	private Integer frequency;
	private Double weight;
	
	/**
	 * Increment Frequency
	 */
	public void addFrequency()
	{
		frequency=frequency+1;
	}

	public OntologyNode(String entity) {
		this.entity = entity;
		this.frequency=1;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the frequency
	 */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OntologyNode [entity=" + entity + ", frequency=" + frequency + ", weight=" + weight + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return this.getEntity().equals(((OntologyNode) obj).getEntity());
	}

	@Override
	public int hashCode() {
		return this.getEntity().hashCode();
	}

}
