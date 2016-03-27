package utils;

import models.*;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DemoData {

    public User user1;
    public User user2;

    @Inject
    public DemoData(Environment environment) {
        if (environment.isDev() || environment.isTest()) {
            if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {
                Logger.info("Loading Demo Data");

                user1 = new User("user1@demo.com", "password", "John Doe");
                user1.save();

                user2 = new User("user2@demo.com", "password", "Jane Doe");
                user2.save();
            }
        }
    }

}
