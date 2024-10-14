package com.shopme.common.entity.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "osei2022";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(encodedPassword);

        boolean match = passwordEncoder.matches(rawPassword,encodedPassword);
        assertThat(match).isTrue();
    }
}
