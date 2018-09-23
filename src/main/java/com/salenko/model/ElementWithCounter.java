package com.salenko.model;

import org.jsoup.nodes.Element;

// org.jsoup.nodes.Element wrapper
public class ElementWithCounter {

    private Element element;
    private int counter;

    public ElementWithCounter(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;

    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void incrementCounter() {
        counter++;
    }

}
