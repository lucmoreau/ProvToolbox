package org.openprovenance.prov.template.core;

import java.util.*;

import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.core.exception.MissingAttributeValue;
import org.openprovenance.prov.template.json.*;

public class Using implements Iterable<int[]> {

    private static final int[] EMPTY = new int[0];

    private int[] groups;
    private int[] lengths;
    private int size;


    public Using() {
        groups = EMPTY;
        lengths = EMPTY;
        size = 0;
    }



    public void addGroup(int group, int length) {
        if (size == groups.length) {
            int newCap = Math.max(4, groups.length * 2);
            groups = Arrays.copyOf(groups, newCap);
            lengths = Arrays.copyOf(lengths, newCap);
        }
        groups[size] = group;
        lengths[size] = length;
        size++;
    }

    public int[] zeroIndex() {
        return new int[size];
    }

    /** Check that the given index is valid for this Using.
     *
     * @param index the index to check
     * @return true if the index is valid
     */
    public boolean checkIndex(int[] index) {
        if (index == null) return size == 0;
        if (index.length != size) return false;
        for (int i = 0; i < size; i++) {
            if (index[i] >= lengths[i]) return false;
        }
        return true;
    }




    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("<using:[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(groups[i]);
        }
        sb.append("],[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(lengths[i]);
        }
        sb.append("]>");
        return sb.toString();
    }

    Map<QualifiedName, QDescriptor> get(Bindings b,
                                        Groupings gr,
                                        int[] index) {
        Map<QualifiedName, QDescriptor> result = new HashMap<>();
        for (int count = 0; count < size; count++) {
            int ind = index[count];
            int group = groups[count];
            for (QualifiedName var : gr.get(group)) {
                if (b.var != null) {
                    Descriptors ll = b.var.get(var.getLocalPart());
                    if (ll != null && ll.values != null) {
                        Descriptor val = ll.values.get(ind);
                        if (val instanceof QDescriptor) {
                            result.put(var, (QDescriptor) val);
                        }
                    }
                }
                if (b.vargen != null) {
                    Descriptors ll = b.vargen.get(var.getLocalPart());
                    if (ll != null && ll.values != null) {
                        // note that there must always be a single value for vargen variables, so select 0, irrespective of ind
                        Descriptor val = ll.values.get(0);
                        if (val instanceof QDescriptor) {
                            result.put(var, (QDescriptor) val);
                        }
                    }
                }
            }
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


    public class UsingIterator implements Iterator<int[]> {
        int[] currentIndex;
        boolean initialized;
        private final Using u;
        private int count;
        int[] cachedNextIndex = null;



        public int[] nextIndex(int[] index) {
            if (cachedNextIndex != null) {
                return cachedNextIndex;
            }
            if (!checkIndex(index)) throw new IllegalArgumentException(Arrays.toString(index));
            int[] result = new int[size];

            int carryOver = 1;

            for (int i = 0; i < size; i++) {
                int next = index[i] + carryOver;
                if (next >= lengths[i]) {
                    next = 0;
                    carryOver = 1;
                } else {
                    carryOver = 0;
                }
                result[i] = next;
            }
            if (carryOver == 0) {
                cachedNextIndex = result;
                return result;
            } else {
                cachedNextIndex = null;
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
        public int[] next() {
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
    public Iterator<int[]> iterator() {
        return new UsingIterator(this);
    }



}
