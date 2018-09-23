package com.salenko;

import com.salenko.model.ElementWithCounter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Finder {

    private static Logger LOGGER = LoggerFactory.getLogger(Finder.class);

    private static String CHARSET_NAME = "utf8";

    public static List<ElementWithCounter> find(String originalPagePath, String targetPagePath, String buttonId) {

        // find original element
        Optional<Element> originalElement = findElementById(new File(originalPagePath), buttonId);
        String originalTag = originalElement.get().tagName();

        // finding elements by tag - rough filter
        Optional<Elements> elementsFilteredByTag = findElementsByTag(new File(targetPagePath), originalTag);

        if (elementsFilteredByTag.get().size() != 0) {

            List<ElementWithCounter> rankedElements = new ArrayList<>();

            for (Element el : elementsFilteredByTag.get()) {
                rankedElements.add(new ElementWithCounter(el));
            }

            // ranking our elements
            rankedElements.forEach(new Consumer<ElementWithCounter>() {
                                       @Override
                                       public void accept(ElementWithCounter e) {
                                           originalElement.get().attributes().forEach(attr -> {
                                               // if only contains - counter+1, if equals - counter+2
                                               if (e.getElement().attr(attr.getKey()).contains(attr.getValue()))
                                                   e.incrementCounter();
                                               if (e.getElement().attr(attr.getKey()).equals(attr.getValue()))
                                                   e.incrementCounter();
                                           });
                                       }
                                   }
            );

            List<ElementWithCounter> resultElements = new ArrayList<>();
            int maxCounter = 0;

            // find highest rank
            for (ElementWithCounter e : rankedElements) {
                if (maxCounter < e.getCounter()) {
                    maxCounter = e.getCounter();
                }
            }

            // choose top ranked elements
            for (ElementWithCounter e : rankedElements) {
                if (maxCounter == e.getCounter()) {
                    resultElements.add(e);
                }
            }

            return resultElements;

        } else {
            return null;
        }
    }

    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.getElementById(targetElementId));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private static Optional<Elements> findElementsByTag(File htmlFile, String targetElementTag) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());
            return Optional.of(doc.getElementsByTag(targetElementTag));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
}
