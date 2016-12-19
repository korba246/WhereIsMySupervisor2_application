package com.example.korba.gdziejestmojpromotor2.model;

/**
 * Created by korba on 15.12.16.
 */

public class LecturersLocalization {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String building;
    private String floor;
    private String x;
    private String y;
    private String time;
    private String date;

    public LecturersLocalization(){

    }
    public LecturersLocalization(int id, String name, String surname, String email, String building, String floor, String x, String y, String time, String date){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.building = building;
        this.floor = floor;
        this.x = x;
        this.y = y;
        this.time = time;
        this.date = date;

    }


    public LecturersLocalization(String name, String surname, String email, String building, String floor, String x, String y, String time, String date){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.building = building;
        this.floor = floor;
        this.x = x;
        this.y = y;
        this.time = time;
        this.date = date;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
