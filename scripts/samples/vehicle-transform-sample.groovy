import com.opnitech.esb.client.util.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Vehicle {
	private String uuid;
	private String accountId;

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) { 
		this.uuid = uuid;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) { 
		this.accountId = accountId;
	}
}

Vehicle vehicle = JSONUtil.unmarshall(Vehicle.class, body.getDocumentAsJSON());

String newVehicleAsJSON = JSONUtil.marshall(vehicle);
body.setDocumentAsJSON(newVehicleAsJSON);

return body;