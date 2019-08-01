/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verifier;

/**
 *
 * @author 003-0823
 */
public class Conas {

    private final int id;
    private final String lastName;
    private final String firstName;
    private final String middleName;
    private final String npers;
    private final String birthday;
    private final String sex;

    public Conas(int id,
            String lastName,
            String firstName,
            String middleName,
            String npers,
            String birthday,
            String sex) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.npers = npers;
        this.birthday = birthday;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNpers() {
        return npers;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }
}
