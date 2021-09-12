// Implement a Pair class which the two elements should not be null.
package com.finmind.premium;

public class Pair<T, U> {
    private final T first_;
    private final U second_;

    private void checkNullPointer() {
        if (first_ == null || second_ == null) {
            throw new NullPointerException();
        }
    }

    public Pair(T firstValue, U secondValue) {
        first_ = firstValue;
        second_ = secondValue;
    }

    public T first() {
        return first_;
    }

    public U second() {
        return second_;
    }

    public String toString() {
        checkNullPointer();
        return "(" + first_ + ", " + second_ + ")";
    }

    @Override
    public Pair<T, U> clone() {
        checkNullPointer();
        return new Pair<T, U>(first_, second_);
    }

    @Override
    public boolean equals(Object other) {
        checkNullPointer();
        if (!(other instanceof Pair<?,?>)) {
            return false;
        }
        Pair<?,?> otherObj = (Pair<?,?>)other;
        return otherObj.first_.equals(first_) && otherObj.second_.equals(second_);
    }

    @Override
    public int hashCode() {
        checkNullPointer();
        return first_.hashCode() * 29 + second_.hashCode();
    }
}
