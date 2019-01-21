/* PERSON CLASS
 * CREATED BY CONNOR MEADS
 *
 * CLASS FLOWS AS FOLLOWS:
 *
 * Person() //default constructor defaulting all class variables to empty strings
 * @Override public String toString() //overrides the toString method  **RETURNS THE FULL NAME IN A STRING**
 *
 * getters & setters
 *
 * END OF CLASS FILE
 */

package com.company;

import java.util.Date;

public class Person
{
    Person() {
        this.firstName = "empty";
        this.middleName = "empty";
        this.lastName = "empty";
        this.prefix = "empty";
        //this.dateOfBirth.setTime(0);
        this.primaryAddress = "empty";
        this.secondaryAddress = "empty";
        this.homePhoneNumber = "empty";
        this.cellPhoneNumber = "empty";
        this.personalEmail = "empty";
        this.workEmail = "empty";
    }

    private String firstName, middleName, lastName, prefix;
    private Date dateOfBirth;
    private String primaryAddress, secondaryAddress;
    private String homePhoneNumber, cellPhoneNumber;
    private String personalEmail, workEmail;

    //toString override
    @Override
    public String toString()
    {
        return this.lastName + ", " + this.firstName + " " + this.middleName;
    }

    //getters & setters
    public String getFirstName(){return this.firstName;}
    public void setFirstName (String l_firstName){this.firstName = l_firstName;}

    public String getMiddleName(){return this.middleName;}
    public void setMiddleName(String l_middleName){this.middleName = l_middleName;}

    public String getLastName(){return this.lastName;}
    public void setLastName(String l_lastName){this.lastName = l_lastName;}

    public String getPrefix(){return this.prefix;}
    public void setPrefix(String l_prefix){this.prefix = l_prefix;}

    public Date getDateOfBirth(){return this.dateOfBirth;}
    public void setDateOfBirth(Date l_dateOfBirth){this.dateOfBirth = l_dateOfBirth;}

    public String getPrimaryAddress(){return this.primaryAddress;}
    public void setPrimaryAddress(String l_primaryAddress){this.primaryAddress = l_primaryAddress;}

    public String getSecondaryAddress(){return this.secondaryAddress;}
    public void setSecondaryAddress(String l_secondaryAddress){this.secondaryAddress = l_secondaryAddress;}

    public String getHomePhoneNumber(){return this.homePhoneNumber;}
    public void setHomePhoneNumber(String l_homePhoneNumber){this.homePhoneNumber = l_homePhoneNumber;}

    public String getCellPhoneNumber(){return this.cellPhoneNumber;}
    public void setCellPhoneNumber(String l_cellPhoneNumber){this.cellPhoneNumber = l_cellPhoneNumber;}

    public String getPersonalEmail(){return this.personalEmail;}
    public void setPersonalEmail(String l_personalEmail){this.personalEmail = l_personalEmail;}

    public String getWorkEmail(){return this.workEmail;}
    public void setWorkEmail(String l_workEmail){this.workEmail = l_workEmail;}

}
