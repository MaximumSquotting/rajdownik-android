package com.example.barte_000.rajdownik;

/**
 * Created by barte_000 on 28.11.2016.
 */

public class Registrations {
    private String name;
    private String student_id;
    private boolean accepted;
    private boolean paid;
    private boolean attended;
    private String shirt_size;
    private boolean female_shirt;
    private String phone_number;
    private String surname;
    private boolean accepted_terms;
    private boolean signed_declaration;

    public Registrations(String name, String student_id, String surname, boolean accepted) {
        this.name = name;
        this.student_id = student_id;
        this.surname = surname;
        this.accepted = accepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getShirt_size() {
        return shirt_size;
    }

    public void setShirt_size(String shirt_size) {
        this.shirt_size = shirt_size;
    }

    public boolean isFemale_shirt() {
        return female_shirt;
    }

    public void setFemale_shirt(boolean female_shirt) {
        this.female_shirt = female_shirt;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isAccepted_terms() {
        return accepted_terms;
    }

    public void setAccepted_terms(boolean accepted_terms) {
        this.accepted_terms = accepted_terms;
    }

    public boolean isSigned_declaration() {
        return signed_declaration;
    }

    public void setSigned_declaration(boolean signed_declaration) {
        this.signed_declaration = signed_declaration;
    }
}
