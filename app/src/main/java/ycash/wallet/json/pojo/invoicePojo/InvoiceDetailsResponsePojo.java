package ycash.wallet.json.pojo.invoicePojo;

import java.util.ArrayList;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 22-Nov-17.
 */

public class InvoiceDetailsResponsePojo extends GenericResponse {
    ArrayList<InvoiceDetailsPojo> invoiceDetailsPojo;

    public ArrayList<InvoiceDetailsPojo> getInvoiceDetailsPojo() {
        return invoiceDetailsPojo;
    }

    public void setInvoiceDetailsPojo(ArrayList<InvoiceDetailsPojo> invoiceDetailsPojo) {
        this.invoiceDetailsPojo = invoiceDetailsPojo;
    }
}
