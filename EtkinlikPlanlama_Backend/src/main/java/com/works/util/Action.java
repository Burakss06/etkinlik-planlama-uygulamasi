package com.works.util;

public class Action {

    public int add() { return 65; }
    public int update() { return 80; }
    public int delete() { return 85; }

    public boolean emailValidation(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.*)$");
    }
}
