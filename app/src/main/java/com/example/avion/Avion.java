package com.example.avion;

import java.util.ArrayList;

public class Avion {
    private String depart, arrive, dateD, heureD, heureA,uid;
    private ArrayList<String> passagers;

    public Avion() {
        // Required default constructor for Firebase
    }

    public Avion(String depart, String arrive, String dateD, String heureD, String heureA) {
        this.depart = depart;
        this.arrive = arrive;
        this.dateD = dateD;
        this.heureD = heureD;
        this.heureA = heureA;
        this.passagers = new ArrayList<>();
    }

    public void addPassager(String passager) {
        passagers.add(passager);
    }

    public void removePassager(String passager) {
        passagers.remove(passager);
    }

    // Add getters and setters as needed

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getDateD() {
        return dateD;
    }

    public void setDateD(String dateD) {
        this.dateD = dateD;
    }

    public String getHeureD() {
        return heureD;
    }

    public void setHeureD(String heureD) {
        this.heureD = heureD;
    }

    public String getHeureA() {
        return heureA;
    }

    public void setHeureA(String heureA) {
        this.heureA = heureA;
    }

    public ArrayList<String> getPassagers() {
        return passagers;
    }

    public void setPassagers(ArrayList<String> passagers) {
        this.passagers = passagers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @Override
    public String toString() {
        // Customize the string representation of the Flight object
        return "Depart: " + depart + " Arrivé: " + arrive + " Date: " + dateD + " Heure: " + heureD + "-" + heureA;
    }

}
