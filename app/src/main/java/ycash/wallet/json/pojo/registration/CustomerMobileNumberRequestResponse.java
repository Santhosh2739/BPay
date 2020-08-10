package ycash.wallet.json.pojo.registration;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class CustomerMobileNumberRequestResponse extends GenericResponse {

    /**
     * @author soumya
     */


    public CustomerMobileNumberRequestResponse(){

    }



    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

