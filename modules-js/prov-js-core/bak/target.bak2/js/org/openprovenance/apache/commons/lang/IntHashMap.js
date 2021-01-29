/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    /**
                     * <p>Constructs a new, empty hashtable with the specified initial
                     * capacity and the specified load factor.</p>
                     *
                     * @param {number} initialCapacity the initial capacity of the hashtable.
                     * @param {number} loadFactor the load factor of the hashtable.
                     * @throws IllegalArgumentException  if the initial capacity is less
                     * than zero, or if the load factor is nonpositive.
                     * @class
                     * @author Apache Software Foundation
                     */
                    class IntHashMap {
                        constructor(initialCapacity, loadFactor) {
                            if (((typeof initialCapacity === 'number') || initialCapacity === null) && ((typeof loadFactor === 'number') || loadFactor === null)) {
                                let __args = arguments;
                                if (this.table === undefined) {
                                    this.table = null;
                                }
                                if (this.count === undefined) {
                                    this.count = 0;
                                }
                                if (this.threshold === undefined) {
                                    this.threshold = 0;
                                }
                                if (this.loadFactor === undefined) {
                                    this.loadFactor = 0;
                                }
                                if (initialCapacity < 0) {
                                    throw Object.defineProperty(new Error("Illegal Capacity: " + initialCapacity), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                if (loadFactor <= 0) {
                                    throw Object.defineProperty(new Error("Illegal Load: " + loadFactor), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                if (initialCapacity === 0) {
                                    initialCapacity = 1;
                                }
                                this.loadFactor = loadFactor;
                                this.table = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(initialCapacity);
                                this.threshold = ((Math.fround(initialCapacity * loadFactor)) | 0);
                            }
                            else if (((typeof initialCapacity === 'number') || initialCapacity === null) && loadFactor === undefined) {
                                let __args = arguments;
                                {
                                    let __args = arguments;
                                    let loadFactor = 0.75;
                                    if (this.table === undefined) {
                                        this.table = null;
                                    }
                                    if (this.count === undefined) {
                                        this.count = 0;
                                    }
                                    if (this.threshold === undefined) {
                                        this.threshold = 0;
                                    }
                                    if (this.loadFactor === undefined) {
                                        this.loadFactor = 0;
                                    }
                                    if (initialCapacity < 0) {
                                        throw Object.defineProperty(new Error("Illegal Capacity: " + initialCapacity), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (loadFactor <= 0) {
                                        throw Object.defineProperty(new Error("Illegal Load: " + loadFactor), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (initialCapacity === 0) {
                                        initialCapacity = 1;
                                    }
                                    this.loadFactor = loadFactor;
                                    this.table = (s => { let a = []; while (s-- > 0)
                                        a.push(null); return a; })(initialCapacity);
                                    this.threshold = ((Math.fround(initialCapacity * loadFactor)) | 0);
                                }
                                if (this.table === undefined) {
                                    this.table = null;
                                }
                                if (this.count === undefined) {
                                    this.count = 0;
                                }
                                if (this.threshold === undefined) {
                                    this.threshold = 0;
                                }
                                if (this.loadFactor === undefined) {
                                    this.loadFactor = 0;
                                }
                            }
                            else if (initialCapacity === undefined && loadFactor === undefined) {
                                let __args = arguments;
                                {
                                    let __args = arguments;
                                    let initialCapacity = 20;
                                    let loadFactor = 0.75;
                                    if (this.table === undefined) {
                                        this.table = null;
                                    }
                                    if (this.count === undefined) {
                                        this.count = 0;
                                    }
                                    if (this.threshold === undefined) {
                                        this.threshold = 0;
                                    }
                                    if (this.loadFactor === undefined) {
                                        this.loadFactor = 0;
                                    }
                                    if (initialCapacity < 0) {
                                        throw Object.defineProperty(new Error("Illegal Capacity: " + initialCapacity), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (loadFactor <= 0) {
                                        throw Object.defineProperty(new Error("Illegal Load: " + loadFactor), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                    if (initialCapacity === 0) {
                                        initialCapacity = 1;
                                    }
                                    this.loadFactor = loadFactor;
                                    this.table = (s => { let a = []; while (s-- > 0)
                                        a.push(null); return a; })(initialCapacity);
                                    this.threshold = ((Math.fround(initialCapacity * loadFactor)) | 0);
                                }
                                if (this.table === undefined) {
                                    this.table = null;
                                }
                                if (this.count === undefined) {
                                    this.count = 0;
                                }
                                if (this.threshold === undefined) {
                                    this.threshold = 0;
                                }
                                if (this.loadFactor === undefined) {
                                    this.loadFactor = 0;
                                }
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Returns the number of keys in this hashtable.</p>
                         *
                         * @return  {number} the number of keys in this hashtable.
                         */
                        size() {
                            return this.count;
                        }
                        /**
                         * <p>Tests if this hashtable maps no keys to values.</p>
                         *
                         * @return  {boolean} <code>true</code> if this hashtable maps no keys to values;
                         * <code>false</code> otherwise.
                         */
                        isEmpty() {
                            return this.count === 0;
                        }
                        /**
                         * <p>Tests if some key maps into the specified value in this hashtable.
                         * This operation is more expensive than the <code>containsKey</code>
                         * method.</p>
                         *
                         * <p>Note that this method is identical in functionality to containsValue,
                         * (which is part of the Map interface in the collections framework).</p>
                         *
                         * @param      {*} value   a value to search for.
                         * @return     {boolean} <code>true</code> if and only if some key maps to the
                         * <code>value</code> argument in this hashtable as
                         * determined by the <tt>equals</tt> method;
                         * <code>false</code> otherwise.
                         * @throws  NullPointerException  if the value is <code>null</code>.
                         * @see        #containsKey(int)
                         * @see        #containsValue(Object)
                         * @see        java.util.Map
                         */
                        contains(value) {
                            if (value == null) {
                                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.NullPointerException', 'java.lang.Exception'] });
                            }
                            const tab = this.table;
                            for (let i = tab.length; i-- > 0;) {
                                {
                                    for (let e = tab[i]; e != null; e = e.next) {
                                        {
                                            if ( /* equals */((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(e.value, value)) {
                                                return true;
                                            }
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        /**
                         * <p>Returns <code>true</code> if this HashMap maps one or more keys
                         * to this value.</p>
                         *
                         * <p>Note that this method is identical in functionality to contains
                         * (which predates the Map interface).</p>
                         *
                         * @param {*} value value whose presence in this HashMap is to be tested.
                         * @return {boolean} boolean <code>true</code> if the value is contained
                         * @see    java.util.Map
                         * @since JDK1.2
                         */
                        containsValue(value) {
                            return this.contains(value);
                        }
                        /**
                         * <p>Tests if the specified object is a key in this hashtable.</p>
                         *
                         * @param  {number} key  possible key.
                         * @return {boolean} <code>true</code> if and only if the specified object is a
                         * key in this hashtable, as determined by the <tt>equals</tt>
                         * method; <code>false</code> otherwise.
                         * @see #contains(Object)
                         */
                        containsKey(key) {
                            const tab = this.table;
                            const hash = key;
                            const index = (hash & 2147483647) % tab.length;
                            for (let e = tab[index]; e != null; e = e.next) {
                                {
                                    if (e.hash === hash) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        /**
                         * <p>Returns the value to which the specified key is mapped in this map.</p>
                         *
                         * @param   {number} key   a key in the hashtable.
                         * @return  {*} the value to which the key is mapped in this hashtable;
                         * <code>null</code> if the key is not mapped to any value in
                         * this hashtable.
                         * @see     #put(int, Object)
                         */
                        get(key) {
                            const tab = this.table;
                            const hash = key;
                            const index = (hash & 2147483647) % tab.length;
                            for (let e = tab[index]; e != null; e = e.next) {
                                {
                                    if (e.hash === hash) {
                                        return e.value;
                                    }
                                }
                                ;
                            }
                            return null;
                        }
                        /**
                         * <p>Increases the capacity of and internally reorganizes this
                         * hashtable, in order to accommodate and access its entries more
                         * efficiently.</p>
                         *
                         * <p>This method is called automatically when the number of keys
                         * in the hashtable exceeds this hashtable's capacity and load
                         * factor.</p>
                         */
                        rehash() {
                            const oldCapacity = this.table.length;
                            const oldMap = this.table;
                            const newCapacity = oldCapacity * 2 + 1;
                            const newMap = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(newCapacity);
                            this.threshold = ((Math.fround(newCapacity * this.loadFactor)) | 0);
                            this.table = newMap;
                            for (let i = oldCapacity; i-- > 0;) {
                                {
                                    for (let old = oldMap[i]; old != null;) {
                                        {
                                            const e = old;
                                            old = old.next;
                                            const index = (e.hash & 2147483647) % newCapacity;
                                            e.next = newMap[index];
                                            newMap[index] = e;
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                        }
                        /**
                         * <p>Maps the specified <code>key</code> to the specified
                         * <code>value</code> in this hashtable. The key cannot be
                         * <code>null</code>. </p>
                         *
                         * <p>The value can be retrieved by calling the <code>get</code> method
                         * with a key that is equal to the original key.</p>
                         *
                         * @param {number} key     the hashtable key.
                         * @param {*} value   the value.
                         * @return {*} the previous value of the specified key in this hashtable,
                         * or <code>null</code> if it did not have one.
                         * @throws  NullPointerException  if the key is <code>null</code>.
                         * @see     #get(int)
                         */
                        put(key, value) {
                            let tab = this.table;
                            const hash = key;
                            let index = (hash & 2147483647) % tab.length;
                            for (let e = tab[index]; e != null; e = e.next) {
                                {
                                    if (e.hash === hash) {
                                        const old = e.value;
                                        e.value = value;
                                        return old;
                                    }
                                }
                                ;
                            }
                            if (this.count >= this.threshold) {
                                this.rehash();
                                tab = this.table;
                                index = (hash & 2147483647) % tab.length;
                            }
                            const e = new IntHashMap.Entry(hash, key, value, tab[index]);
                            tab[index] = e;
                            this.count++;
                            return null;
                        }
                        /**
                         * <p>Removes the key (and its corresponding value) from this
                         * hashtable.</p>
                         *
                         * <p>This method does nothing if the key is not present in the
                         * hashtable.</p>
                         *
                         * @param   {number} key   the key that needs to be removed.
                         * @return  {*} the value to which the key had been mapped in this hashtable,
                         * or <code>null</code> if the key did not have a mapping.
                         */
                        remove(key) {
                            const tab = this.table;
                            const hash = key;
                            const index = (hash & 2147483647) % tab.length;
                            for (let e = tab[index], prev = null; e != null; prev = e, e = e.next) {
                                {
                                    if (e.hash === hash) {
                                        if (prev != null) {
                                            prev.next = e.next;
                                        }
                                        else {
                                            tab[index] = e.next;
                                        }
                                        this.count--;
                                        const oldValue = e.value;
                                        e.value = null;
                                        return oldValue;
                                    }
                                }
                                ;
                            }
                            return null;
                        }
                        /**
                         * <p>Clears this hashtable so that it contains no keys.</p>
                         */
                        clear() {
                            const tab = this.table;
                            for (let index = tab.length; --index >= 0;) {
                                {
                                    tab[index] = null;
                                }
                                ;
                            }
                            this.count = 0;
                        }
                    }
                    lang.IntHashMap = IntHashMap;
                    IntHashMap["__class"] = "org.openprovenance.apache.commons.lang.IntHashMap";
                    (function (IntHashMap) {
                        /**
                         * <p>Innerclass that acts as a datastructure to create a new entry in the
                         * table.</p>
                         * @class
                         */
                        class Entry {
                            constructor(hash, key, value, next) {
                                if (this.hash === undefined) {
                                    this.hash = 0;
                                }
                                if (this.key === undefined) {
                                    this.key = 0;
                                }
                                if (this.value === undefined) {
                                    this.value = null;
                                }
                                if (this.next === undefined) {
                                    this.next = null;
                                }
                                this.hash = hash;
                                this.key = key;
                                this.value = value;
                                this.next = next;
                            }
                        }
                        IntHashMap.Entry = Entry;
                        Entry["__class"] = "org.openprovenance.apache.commons.lang.IntHashMap.Entry";
                    })(IntHashMap = lang.IntHashMap || (lang.IntHashMap = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
