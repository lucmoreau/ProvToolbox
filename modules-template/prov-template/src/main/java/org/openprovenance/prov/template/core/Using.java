package org.openprovenance.prov.template.core;

import java.util.*;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.core.exception.MissingAttributeValue;
import org.openprovenance.prov.template.json.*;

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

    /** Check that the given index is valid for this Using.
     *
     * @param index the index to check
     * @return true if the index is valid
     */
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





    @Override
    public String toString () {
        return "<using:" + groups + "," + lengths + ">";
    }

    Map<QualifiedName, QDescriptor> get(Bindings b,
                                        Groupings gr,
                                        List<Integer> index) {
        Map<QualifiedName,QDescriptor> result= new HashMap<>();
        int count=0;
        for (int ind: index) {
            int group=groups.get(count);
            for (QualifiedName var: gr.get(group)) {
                if (b.var!=null) {
                    Descriptors ll = b.var.get(var.getLocalPart());
                    if (ll != null && ll.values!=null) {
                        Descriptor val = ll.values.get(ind);
                        if (val instanceof QDescriptor) {
                            result.put(var, (QDescriptor) val);
                        }
                    }
                }
                if (b.vargen!=null) {
                    Descriptors ll = b.vargen.get(var.getLocalPart());
                    if (ll != null && ll.values!=null) {
                        // note that there must always be a single value for vargen variables, so select 0, irrespective of ind
                        Descriptor val = ll.values.get(0);
                        if (val instanceof QDescriptor) {
                            result.put(var, (QDescriptor) val);
                        }
                    }
                }
            }
            count++;
        }
        return result;
    }



    public Map<QualifiedName, SingleDescriptors> getAttr(Set<QualifiedName> variables, Bindings bindings, UsingIterator iter) {
        Map<QualifiedName, SingleDescriptors> result= new HashMap<>();

        int ind=iter.getCount();
        for (QualifiedName var: variables) {

            Descriptors descriptors;
            String localPart = var.getLocalPart();
            if (InstantiateUtil.isGensymVariable(var)) {
                descriptors = bindings.vargen.get(localPart);
            } else {
                descriptors = bindings.var.get(localPart);
            }



            if (descriptors != null) {
                result.computeIfAbsent(var, k -> {
                    SingleDescriptors singleDescriptors = new SingleDescriptors();
                    singleDescriptors.values = new LinkedList<>();
                    return singleDescriptors;
                });
                SingleDescriptors sdlist = result.get(var);
                try {
                    Descriptor descriptor = descriptors.values.get(ind);
                    if (descriptor instanceof SingleDescriptors) {
                        SingleDescriptors sds = (SingleDescriptors) descriptor;
                        sdlist.values.addAll(sds.values);
                    } else if (descriptor instanceof SingleDescriptor) {
                        SingleDescriptor sd = (SingleDescriptor) descriptor;
                        sdlist.values.add(sd);
                    } else {
                        throw new IllegalArgumentException("Unexpected descriptor (1) type " + descriptor.getClass());
                    }
                } catch (IndexOutOfBoundsException excp) {
                    Descriptor descriptor = descriptors.values.get(0);
                    if (descriptor != null) {
                        if (descriptor instanceof SingleDescriptor) {
                            SingleDescriptor sd = (SingleDescriptor) descriptor;
                            sdlist.values.add(sd);
                            System.err.println("IndexOutOfBoundsWarning: index " + ind + " for variable '" + localPart + "'. Reusing: " + descriptor);
                        } else {
                            throw new IllegalArgumentException("Unexpected descriptor (2) type " + descriptor.getClass());
                        }
                    } else {
                        throw new MissingAttributeValue("Missing attribute value for variable " + var + ": index is " + ind + " and values are " + descriptors.values, excp);
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
        List<Integer> cachedNextIndex=null;



        public List<Integer> nextIndex(List<Integer> index) {
            if (cachedNextIndex!=null) {
                return cachedNextIndex;
            }
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
                cachedNextIndex=result;
                return result;
            } else {
                cachedNextIndex=null;
                return null;
            }
        }



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
                cachedNextIndex=null;
                return currentIndex;
            }
            if (currentIndex!=null) {
                currentIndex=nextIndex(currentIndex);
                if (currentIndex==null) {
                    throw new NoSuchElementException();
                }
                count++;
                cachedNextIndex=null;
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
