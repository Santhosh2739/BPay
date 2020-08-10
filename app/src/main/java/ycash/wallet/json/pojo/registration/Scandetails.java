package ycash.wallet.json.pojo.registration;

/**
 * Created by 10037 on 11/30/2016.
 */
public class Scandetails {
    private String firstname;
    private String lastname;
    private String nationality;
    private String gender;
    private String civilID;

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCivilID() {
        return civilID;
    }

    public void setCivilID(String civilID) {
        this.civilID = civilID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
