package ua.kiev.prog.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class CustomUser {
    @Id
    @GeneratedValue
    private Long id;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserRegisterType type;

    private String email;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    public CustomUser(String password, UserRole role, UserRegisterType type,
                      String email, String phone, String address) {
        this.password = password;
        this.role = role;
        this.type = type;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public CustomUser() {
    }

    public static CustomUser of(String password, String email, UserRole role, UserRegisterType type, String phone, String address) {
        return new CustomUser(password, role, type, email, phone, address);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public UserRegisterType getType() {
        return type;
    }

    public void setType(UserRegisterType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CustomUser{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", orders=" + orders +
                '}';
    }
}
