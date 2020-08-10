package ycash.wallet.json.pojo.registration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 4/14/2017.
 */

public class UpdateCustomerDetailsResponse extends GenericResponse implements JsonDeserializer<CustomerRegistrationRequest> {

    private CustomerRegistrationRequest customerRegistrationRequest;

    public CustomerRegistrationRequest getCustomerRegistrationRequest() {
        return customerRegistrationRequest;
    }

    public void setCustomerRegistrationRequest(CustomerRegistrationRequest customerRegistrationRequest) {
        this.customerRegistrationRequest = customerRegistrationRequest;
    }

    public UpdateCustomerDetailsResponse() {
        super();

    }

    @Override
    public CustomerRegistrationRequest deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest();
        customerRegistrationRequest.setFirstName(jsonObject.get("firstName").getAsString());
        customerRegistrationRequest.setLastName(jsonObject.get("lastName").getAsString());
        customerRegistrationRequest.setCivilID(jsonObject.get("civilID").getAsString());
        customerRegistrationRequest.setEmailID(jsonObject.get("emailID").getAsString());

        return customerRegistrationRequest;
    }
}