package es.urjc.code.daw.marketplace.util;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.User;

public class EmailMessageFactory {

    public static String newWelcomeTitle() {
        return "Welcome to DAWHostServices";
    }

    public static String newWelcomeMessage(User user) {
        return EmailContent.create()
                .addHeading("Thanks for registering")
                .addUnorderedList(
                    "Here is your profile information",
                    Lists.newArrayList(
                        "Name: " + user.getFirstName(),
                        "Surname: " + user.getSurname(),
                        "Email: " + user.getEmail()
                    )
                )
                .addHeading("and welcome to DAWHostServices!")
            .build();
    }

}
