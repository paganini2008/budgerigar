package com.github.budgerigar.doc;

import java.util.Iterator;
import java.util.Objects;

/**
 * 
 * @Description: SingleElementIterator
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class SingleElementIterator<E> implements Iterator<E> {

    private final E element;
    private boolean hasNext;

    public SingleElementIterator(E element) {
        this.element = element;
        this.hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public E next() {
        hasNext = false;
        return element;
    }

    @Override
    public String toString() {
        return Objects.toString(element, "");
    }

}
