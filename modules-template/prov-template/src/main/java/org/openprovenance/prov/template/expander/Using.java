package org.openprovenance.prov.template.expander;

import java.util.*;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.template.expander.exception.MissingAttributeValue;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.Descriptors;

public class Using implements Iterable<List<Integer>> {


    final private List<Integer> groups;
    final private List<Integer> lengths;



    public Using() {
        groups= new LinkedList<>();
        lengths= new LinkedList<>();
    }



    public void addGroup(Integer group, Integer length) {
        groups.add(group);
        lengths.add(length);
    }

    public List<Integer> zeroIndex () {
        List<Integer> result= new LinkedList<>();
        for (@SuppressWarnings("unused") Object o: lengths) {
            result.add(0);
        }
        return result;
    }

    public boolean checkIndex(List<Integer> index) {
        if (index==null) return groups.isEmpty();
        if (index.size()==groups.size()) {
            int count=0;
            for (Integer in: index) {
                if (in >= lengths.get(count)) {
                    return false;
                }
                count++;
            }
            return true;
        }
        return false;
    }


    public List<Integer> nextIndex(List<Integer> index) {
        if (!checkIndex(index)) throw new IllegalArgumentException(""+index);
        List<Integer> result= new LinkedList<>();

        int count=0;
        int carryOver=1;

        for (Integer in: index) {
            int next=in+carryOver;
            if (next >= lengths.get(count)) {
                next=0;
                carryOver=1;
            } else {
                carryOver=0;
            }
            count++;
            result.add(next);
        }
        if (carryOver==0) {
            return result;
        } else {
            return null;
        }
    }


    @Override
    public String toString () {
        return "<using:" + groups + "," + lengths + ">";
    }

    Map<QualifiedName, QualifiedName> get(OldBindings b,
                                          Groupings gr,
                                          List<Integer> index) {
        Map<QualifiedName,QualifiedName> result= new HashMap<>();

        int count=0;
        for (int ind: index) {
            int group=groups.get(count);
            for (QualifiedName var: gr.get(group)) {
                List<QualifiedName> ll=b.getVariables().get(var);
                if (ll!=null) {
                    QualifiedName val=ll.get(ind);
                    result.put(var, val);
                }
            }
            count++;
        }
        return result;
    }
    Map<QualifiedName, QualifiedName> newGet(Bindings b,
                                             Groupings gr,
                                             List<Integer> index) {
        Map<QualifiedName,QualifiedName> result= new HashMap<>();

        int count=0;
        for (int ind: index) {
            int group=groups.get(count);
            for (QualifiedName var: gr.get(group)) {
                Descriptors ll=b.var.get(var);
                if (ll!=null) {
                   Descriptor val=ll.values.get(ind);
                    System.out.println("Variable " + var + " has descriptors " + val);
                    //result.put(var, val);
                }
            }
            count++;
        }
        return result;
    }


    public Map<QualifiedName, List<TypedValue>> getAttr(Set<QualifiedName> variables,
                                                        OldBindings b,
                                                        UsingIterator iter) {
        Map<QualifiedName, List<TypedValue>> result= new HashMap<>();
        int ind=iter.getCount();

        for (QualifiedName var: variables) {
            List<List<TypedValue>> val=b.getAttributes().get(var);
            if (val!=null) {
                try {
                    List<TypedValue> attVal = val.get(ind);
                    if (attVal==null) {
                        throw new MissingAttributeValue("Missing attribute value for variable " + var + ": index is " + ind + " and values are " + val);
                    }
                    result.put(var, val.get(ind));
                } catch (IndexOutOfBoundsException excp) {
                    List<TypedValue> attVal = val.get(0);
                    if (attVal!=null) {
                        System.err.println("IndexOutOfBoundsWarning: index " + ind + " for variable '" + var.getLocalPart() + "'. Reusing: " + attVal);
                        result.put(var, attVal);
                    } else {
                        throw new MissingAttributeValue("Missing attribute value for variable " + var + ": index is " + ind + " and values are " + val, excp);
                    }
                }
            }
        }
        return result;
    }


    public class UsingIterator implements Iterator<List<Integer>> {
        List<Integer> currentIndex;
        boolean initialized;
        private final Using u;
        private int count;

        @Override
        public boolean hasNext() {
            if (!initialized) return true;
            return (currentIndex!=null) && nextIndex(currentIndex)!=null;
        }

        public int getCount() {
            return count;
        }


        @Override
        public List<Integer> next() {
            if (!initialized) {
                currentIndex=u.zeroIndex();
                initialized=true;
                count=0;
                return currentIndex;
            }
            if (currentIndex!=null) {
                currentIndex=nextIndex(currentIndex);
                if (currentIndex==null) {
                    throw new NoSuchElementException();
                }
                count++;
                return currentIndex;
            } else {
                throw new NoSuchElementException();
            }
        }


        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public UsingIterator(Using u) {
            initialized=false;
            count = -1;
            this.u=u;
        }

    }


    @Override
    public Iterator<List<Integer>> iterator() {
        return new UsingIterator(this);
    }



}
