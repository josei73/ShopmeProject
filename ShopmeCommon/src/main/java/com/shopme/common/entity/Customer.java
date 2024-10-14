package com.shopme.common.entity;

import com.shopme.common.enums.AuthenticationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends AbstractAddressWithCountry {


    @Column(length = 45, nullable = false, unique = true)
    private String email;

    //ist 64 wegen der Verschlüsslung die ich später nutze
    @Column(length = 64, nullable = false)
    private String password;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;


    @Column(name = "created_time")
    private Date createdTime;
    private boolean enabled;


    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;


    public Customer(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }



    public String getFullName() {
        return firstName + " " + lastName;
    }





    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }




}
