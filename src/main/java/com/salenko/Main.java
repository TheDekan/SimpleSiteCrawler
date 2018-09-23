package com.salenko;

import com.salenko.model.ElementWithCounter;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        String resourcePath = args[0]; //"./samples/sample-0-origin.html";
        String otherResourcePath = args[1]; //"./samples/sample-3-the-escape.html";

        // use value from args or default value
        String buttonId = args.length >= 3 ? args[2] : "make-everything-ok-button";

        List<ElementWithCounter> result = Finder.find(resourcePath, otherResourcePath, buttonId);

        if (result != null) {
            LOGGER.info("Founded " + result.size() + " element(s).");
            for (ElementWithCounter e : result) {
                LOGGER.info("Element : " + getCssSelector(e.getElement()));
                String attributesStr = e.getElement().attributes().asList().stream()
                        .map(attr -> attr.getKey() + " = " + attr.getValue())
                        .collect(Collectors.joining(", "));
                LOGGER.info(attributesStr);
            }
        } else {
            LOGGER.info("No elements founded.");
        }
    }

    // this is modified function org.jsoup.nodes.Element.cssSelector
    // added full path to highest hierarchy element (html > body)
    private static String getCssSelector(Element element) {

        // Translate HTML namespace ns:tag to CSS namespace syntax ns|tag
        String tagName = element.tagName().replace(':', '|');
        StringBuilder selector = new StringBuilder(tagName);
        if (element.id().length() > 0) {
            selector.append("#" + element.id());
        } else {
            String classes = StringUtil.join(element.classNames(), ".");
            if (classes.length() > 0)
                selector.append('.').append(classes);
        }

        if (element.parent() == null || element.parent() instanceof Document) // don't add Document to selector, as will always have a html node
            return selector.toString();

        selector.insert(0, " > ");
        if (element.parent().select(selector.toString()).size() > 1)
            selector.append(String.format(
                    ":nth-child(%d)", element.elementSiblingIndex() + 1));

        return getCssSelector(element.parent()) + selector.toString();
    }

}
