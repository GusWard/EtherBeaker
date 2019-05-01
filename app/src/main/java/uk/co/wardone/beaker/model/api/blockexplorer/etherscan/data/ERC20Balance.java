package uk.co.wardone.beaker.model.api.blockexplorer.etherscan.data;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@Generated("com.robohorse.robopojogenerator")
@JsonObject
public class ERC20Balance {

	@SerializedName("result")
	@JsonField(name ="result")
	private String result;

	@SerializedName("message")
	@JsonField(name ="message")
	private String message;

	@SerializedName("status")
	@JsonField(name ="status")
	private String status;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}