package dev.gmathur.sec03;

import dev.gmathur.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec08OneOf {
    private static final Logger LOG = LoggerFactory.getLogger(Lec08OneOf.class);

    private static void login(Credentials cred) {
        switch (cred.getAuthCase()) {
            case EMAIL -> LOG.info("Email: {}", cred.getEmail());
            case PHONE -> LOG.info("Phone: {}", cred.getPhone());
            default -> LOG.info("Unknown");
        }
    }

    public static void main(String[] args) {
        var creds1 = Credentials.newBuilder()
                .setEmail(Email.newBuilder().setAddress("goo").setPassword("bar").build()).build();
        var creds2 = Credentials.newBuilder()
                .setPhone(Phone.newBuilder().setNumber(123).setCode(33).build()).build();
        var creds3 = Credentials.newBuilder()
                .setEmail(Email.newBuilder().setAddress("foo").setPassword("baz").build()) // this will be ignored
                .setPhone(Phone.newBuilder().setNumber(123).setCode(33).build()) // this overrides the previous
                .build();

        login(creds1);
        login(creds2);
        login(creds3);
    }
}
