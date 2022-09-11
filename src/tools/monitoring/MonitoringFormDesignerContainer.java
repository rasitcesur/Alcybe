package tools.monitoring;

import eu.hansolo.medusa.Gauge.SkinType;

public class MonitoringFormDesignerContainer {
	
	public SkinType skinType;
	
	public double maxValue, minValue, value;
	
	public int numberOfDigits=3, decimals;
	
	public String textBinding;

	public String sensorId;
	
	public MonitoringFormDesignerContainer(){ }
	
	public MonitoringFormDesignerContainer(SkinType skinType, double maxValue, double minValue, double value, int numberOfDigits, int decimals,
			String textBinding, String sensorId){ 
		this.numberOfDigits = numberOfDigits < 3 ? 3 : numberOfDigits;
		this.skinType = skinType;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.value = value;
		this.decimals = decimals;
		this.textBinding = textBinding;
		this.sensorId = sensorId;
	}
	
	public int getWidth() {
		if(numberOfDigits<5)
			return 60 * numberOfDigits + 10;
		else
			return 60 * numberOfDigits - 30;
	}

	public SkinType getSkinType() {
		return skinType;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getValue() {
		return value;
	}

	public int getNumberOfDigits() {
		return numberOfDigits;
	}

	public int getDecimals() {
		return decimals;
	}

	public void setSkinType(SkinType skinType) {
		this.skinType = skinType;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setNumberOfDigits(int numberOfDigits) {
		this.numberOfDigits = numberOfDigits;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	public String getTextBinding() {
		return textBinding;
	}

	public void setTextBinding(String textBinding) {
		this.textBinding = textBinding;
	}
}
