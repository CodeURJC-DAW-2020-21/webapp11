package es.urjc.code.daw.marketplace.util;

import com.google.common.collect.Lists;
import es.urjc.code.daw.marketplace.domain.Order;
import es.urjc.code.daw.marketplace.domain.Product;
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

    public static String newPurchaseTitle(Order order) {
        return "#" + order.getId() + " Purchase receipt";
    }

    public static String newPurchaseMessage(User user, Product product) {
        return EmailContent.create()
                .addHeading("Thanks for your purchase, " + user.getFirstName() + " " + user.getSurname())
                .addUnorderedList(
                    "Here is your purchased product information",
                    Lists.newArrayList(
                        "Price: " + product.getPrice(),
                        "Ram: " + product.getRam(),
                        "Cores: " + product.getCores(),
                        "Storage: " + product.getStorage(),
                        "Transfer: " + product.getTransfer()
                    )
                )
                .addHeading("Remember that you can manage each purchased product from my services page!")
            .build();
    }

}
