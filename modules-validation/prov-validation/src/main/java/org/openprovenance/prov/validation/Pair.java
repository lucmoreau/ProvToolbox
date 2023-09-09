package org.openprovenance.prov.validation;

public class Pair<T1,T2> {
    final T1 car;
    final T2 cdr;

    public Pair(T1 car, T2 cdr) {
        this.car=car;
        this.cdr=cdr;
    }

}
