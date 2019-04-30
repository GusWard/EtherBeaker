package uk.co.wardone.beaker.modal.api.exchange.shapeshift.data;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@Generated("com.robohorse.robopojogenerator")
@JsonObject
public class Market{

	@SerializedName("minerFee")
	@JsonField(name ="minerFee")
	private double minerFee;

	@SerializedName("rate")
	@JsonField(name ="rate")
	private double rate;

	@SerializedName("maxLimit")
	@JsonField(name ="maxLimit")
	private double maxLimit;

	@SerializedName("limit")
	@JsonField(name ="limit")
	private double limit;

	@SerializedName("minimum")
	@JsonField(name ="minimum")
	private double minimum;

	@SerializedName("pair")
	@JsonField(name ="pair")
	private String pair;

	public void setMinerFee(double minerFee){
		this.minerFee = minerFee;
	}

	public double getMinerFee(){
		return minerFee;
	}

	public void setRate(double rate){
		this.rate = rate;
	}

	public double getRate(){
		return rate;
	}

	public void setMaxLimit(double maxLimit){
		this.maxLimit = maxLimit;
	}

	public double getMaxLimit(){
		return maxLimit;
	}

	public void setLimit(double limit){
		this.limit = limit;
	}

	public double getLimit(){
		return limit;
	}

	public void setMinimum(double minimum){
		this.minimum = minimum;
	}

	public double getMinimum(){
		return minimum;
	}

	public void setPair(String pair){
		this.pair = pair;
	}

	public String getPair(){
		return pair;
	}

	@Override
	public String toString() {
		return "Market{" +
				"minerFee=" + minerFee +
				", rate=" + rate +
				", maxLimit=" + maxLimit +
				", limit=" + limit +
				", minimum=" + minimum +
				", pair='" + pair + '\'' +
				'}';
	}
}