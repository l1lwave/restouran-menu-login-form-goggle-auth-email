package ua.kiev.prog.models;

public enum UserRegisterType {
    FORM, GOOGLE;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
