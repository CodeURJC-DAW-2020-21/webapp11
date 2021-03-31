package es.urjc.code.daw.marketplace.util;

import lombok.Setter;
import java.util.List;

/**
 * The responsibility of this class is to construct the
 * html message and then return it as a string.
 */
@Setter
public class EmailContent {

    private StringBuilder builder;

    public EmailContent() {
        this.builder = new StringBuilder();
    }

    public EmailContent addHeading(String text) {
        builder.append("<h2>");
        builder.append(text);
        builder.append("</h2>");
        builder.append("<br>");
        return this;
    }

    public EmailContent addUnorderedList(String title, List<String> items) {
        builder.append("<h3>");
        builder.append(title);
        builder.append("</h3>");
        builder.append("<ul>");
        items.forEach(item -> builder.append(String.format("<li>%s</li>", item)));
        builder.append("</ul>");
        builder.append("<br>");
        return this;
    }

    public String build() {
        return builder.toString();
    }

    public static EmailContent create() {
        return new EmailContent();
    }

}
