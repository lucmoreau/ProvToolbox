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
                    var reflect;
                    (function (reflect) {
                        /**
                         * <p>MethodUtils instances should NOT be constructed in standard programming.
                         * Instead, the class should be used as
                         * <code>MethodUtils.getAccessibleMethod(method)</code>.</p>
                         *
                         * <p>This constructor is public to permit tools that require a JavaBean
                         * instance to operate.</p>
                         * @class
                         * @author Apache Software Foundation
                         */
                        class MethodUtils {
                            constructor() {
                            }
                            static invokeMethod$java_lang_Object$java_lang_String$java_lang_Object(object, methodName, arg) {
                                return MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, [arg]);
                            }
                            static invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, args) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const __arguments = args.length;
                                const parameterTypes = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(__arguments);
                                for (let i = 0; i < __arguments; i++) {
                                    {
                                        parameterTypes[i] = args[i].constructor;
                                    }
                                    ;
                                }
                                return MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes);
                            }
                            static invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes) {
                                if (parameterTypes == null) {
                                    parameterTypes = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                                }
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const method = MethodUtils.getMatchingAccessibleMethod(object.constructor, methodName, parameterTypes);
                                if (method == null) {
                                    throw Object.defineProperty(new Error("No such accessible method: " + methodName + "() on object: " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NoSuchMethodException', 'java.lang.Object', 'java.lang.ReflectiveOperationException', 'java.lang.Exception'] });
                                }
                                return /* invoke */ method.fn.apply(object, [args]);
                            }
                            /**
                             * <p>Invoke a named method whose parameter type matches the object type.</p>
                             *
                             * <p>This method delegates the method search to {@link #getMatchingAccessibleMethod(Class, String, Class[])}.</p>
                             *
                             * <p>This method supports calls to methods taking primitive parameters
                             * via passing in wrapping classes. So, for example, a <code>Boolean</code> object
                             * would match a <code>boolean</code> primitive.</p>
                             *
                             * @param {*} object invoke method on this object
                             * @param {string} methodName get method with this name
                             * @param {java.lang.Object[]} args use these arguments - treat null as empty array
                             * @param {java.lang.Class[]} parameterTypes match these parameters - treat null as empty array
                             * @return {*} The value returned by the invoked method
                             *
                             * @throws NoSuchMethodException if there is no such accessible method
                             * @throws InvocationTargetException wraps an exception thrown by the method invoked
                             * @throws IllegalAccessException if the requested method is not accessible via reflection
                             */
                            static invokeMethod(object, methodName, args, parameterTypes) {
                                if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && ((parameterTypes != null && parameterTypes instanceof Array && (parameterTypes.length == 0 || parameterTypes[0] == null || (parameterTypes[0] != null && (parameterTypes[0]["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterTypes[0]))))) || parameterTypes === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes);
                                }
                                else if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, args);
                                }
                                else if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeMethod$java_lang_Object$java_lang_String$java_lang_Object(object, methodName, args);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object(object, methodName, arg) {
                                return MethodUtils.invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, [arg]);
                            }
                            static invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, args) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const __arguments = args.length;
                                const parameterTypes = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(__arguments);
                                for (let i = 0; i < __arguments; i++) {
                                    {
                                        parameterTypes[i] = args[i].constructor;
                                    }
                                    ;
                                }
                                return MethodUtils.invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes);
                            }
                            static invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                if (parameterTypes == null) {
                                    parameterTypes = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                                }
                                const method = MethodUtils.getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class_A(object.constructor, methodName, parameterTypes);
                                if (method == null) {
                                    throw Object.defineProperty(new Error("No such accessible method: " + methodName + "() on object: " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(object.constructor)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NoSuchMethodException', 'java.lang.Object', 'java.lang.ReflectiveOperationException', 'java.lang.Exception'] });
                                }
                                return /* invoke */ method.fn.apply(object, [args]);
                            }
                            /**
                             * <p>Invoke a method whose parameter types match exactly the parameter
                             * types given.</p>
                             *
                             * <p>This uses reflection to invoke the method obtained from a call to
                             * <code>getAccessibleMethod()</code>.</p>
                             *
                             * @param {*} object invoke method on this object
                             * @param {string} methodName get method with this name
                             * @param {java.lang.Object[]} args use these arguments - treat null as empty array
                             * @param {java.lang.Class[]} parameterTypes match these parameters - treat null as empty array
                             * @return {*} The value returned by the invoked method
                             *
                             * @throws NoSuchMethodException if there is no such accessible method
                             * @throws InvocationTargetException wraps an exception thrown by the
                             * method invoked
                             * @throws IllegalAccessException if the requested method is not accessible
                             * via reflection
                             */
                            static invokeExactMethod(object, methodName, args, parameterTypes) {
                                if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && ((parameterTypes != null && parameterTypes instanceof Array && (parameterTypes.length == 0 || parameterTypes[0] == null || (parameterTypes[0] != null && (parameterTypes[0]["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterTypes[0]))))) || parameterTypes === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A$java_lang_Class_A(object, methodName, args, parameterTypes);
                                }
                                else if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object_A(object, methodName, args);
                                }
                                else if (((object != null) || object === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactMethod$java_lang_Object$java_lang_String$java_lang_Object(object, methodName, args);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                if (parameterTypes == null) {
                                    parameterTypes = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                                }
                                const method = MethodUtils.getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class_A(cls, methodName, parameterTypes);
                                if (method == null) {
                                    throw Object.defineProperty(new Error("No such accessible method: " + methodName + "() on class: " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NoSuchMethodException', 'java.lang.Object', 'java.lang.ReflectiveOperationException', 'java.lang.Exception'] });
                                }
                                return /* invoke */ method.fn.apply(null, [args]);
                            }
                            /**
                             * <p>Invoke a static method whose parameter types match exactly the parameter
                             * types given.</p>
                             *
                             * <p>This uses reflection to invoke the method obtained from a call to
                             * {@link #getAccessibleMethod(Class, String, Class[])}.</p>
                             *
                             * @param {*} cls invoke static method on this class
                             * @param {string} methodName get method with this name
                             * @param {java.lang.Object[]} args use these arguments - treat null as empty array
                             * @param {java.lang.Class[]} parameterTypes match these parameters - treat null as empty array
                             * @return {*} The value returned by the invoked method
                             *
                             * @throws NoSuchMethodException if there is no such accessible method
                             * @throws InvocationTargetException wraps an exception thrown by the
                             * method invoked
                             * @throws IllegalAccessException if the requested method is not accessible
                             * via reflection
                             */
                            static invokeExactStaticMethod(cls, methodName, args, parameterTypes) {
                                if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && ((parameterTypes != null && parameterTypes instanceof Array && (parameterTypes.length == 0 || parameterTypes[0] == null || (parameterTypes[0] != null && (parameterTypes[0]["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterTypes[0]))))) || parameterTypes === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes);
                                }
                                else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, args);
                                }
                                else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object(cls, methodName, args);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object(cls, methodName, arg) {
                                return MethodUtils.invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, [arg]);
                            }
                            static invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, args) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const __arguments = args.length;
                                const parameterTypes = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(__arguments);
                                for (let i = 0; i < __arguments; i++) {
                                    {
                                        parameterTypes[i] = args[i].constructor;
                                    }
                                    ;
                                }
                                return MethodUtils.invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes);
                            }
                            static invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes) {
                                if (parameterTypes == null) {
                                    parameterTypes = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_CLASS_ARRAY_$LI$();
                                }
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const method = MethodUtils.getMatchingAccessibleMethod(cls, methodName, parameterTypes);
                                if (method == null) {
                                    throw Object.defineProperty(new Error("No such accessible method: " + methodName + "() on class: " + /* getName */ (c => typeof c === 'string' ? c : c["__class"] ? c["__class"] : c["name"])(cls)), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NoSuchMethodException', 'java.lang.Object', 'java.lang.ReflectiveOperationException', 'java.lang.Exception'] });
                                }
                                return /* invoke */ method.fn.apply(null, [args]);
                            }
                            /**
                             * <p>Invoke a named static method whose parameter type matches the object type.</p>
                             *
                             * <p>This method delegates the method search to {@link #getMatchingAccessibleMethod(Class, String, Class[])}.</p>
                             *
                             * <p>This method supports calls to methods taking primitive parameters
                             * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
                             * would match a <code>boolean</code> primitive.</p>
                             *
                             *
                             * @param {*} cls invoke static method on this class
                             * @param {string} methodName get method with this name
                             * @param {java.lang.Object[]} args use these arguments - treat null as empty array
                             * @param {java.lang.Class[]} parameterTypes match these parameters - treat null as empty array
                             * @return {*} The value returned by the invoked method
                             *
                             * @throws NoSuchMethodException if there is no such accessible method
                             * @throws InvocationTargetException wraps an exception thrown by the
                             * method invoked
                             * @throws IllegalAccessException if the requested method is not accessible
                             * via reflection
                             */
                            static invokeStaticMethod(cls, methodName, args, parameterTypes) {
                                if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && ((parameterTypes != null && parameterTypes instanceof Array && (parameterTypes.length == 0 || parameterTypes[0] == null || (parameterTypes[0] != null && (parameterTypes[0]["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterTypes[0]))))) || parameterTypes === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes);
                                }
                                else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null && args instanceof Array && (args.length == 0 || args[0] == null || (args[0] != null))) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, args);
                                }
                                else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((args != null) || args === null) && parameterTypes === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.invokeStaticMethod$java_lang_Class$java_lang_String$java_lang_Object(cls, methodName, args);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object(cls, methodName, arg) {
                                return MethodUtils.invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, [arg]);
                            }
                            static invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A(cls, methodName, args) {
                                if (args == null) {
                                    args = org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY_$LI$();
                                }
                                const __arguments = args.length;
                                const parameterTypes = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(__arguments);
                                for (let i = 0; i < __arguments; i++) {
                                    {
                                        parameterTypes[i] = args[i].constructor;
                                    }
                                    ;
                                }
                                return MethodUtils.invokeExactStaticMethod$java_lang_Class$java_lang_String$java_lang_Object_A$java_lang_Class_A(cls, methodName, args, parameterTypes);
                            }
                            static getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class(cls, methodName, parameterType) {
                                return MethodUtils.getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class_A(cls, methodName, [parameterType]);
                            }
                            /**
                             * <p>Return an accessible method (that is, one that can be invoked via
                             * reflection) with given name and a single parameter.  If no such method
                             * can be found, return <code>null</code>.
                             * Basically, a convenience wrapper that constructs a <code>Class</code>
                             * array for you.</p>
                             *
                             * @param {*} cls get method from this class
                             * @param {string} methodName get method with this name
                             * @param {*} parameterType taking this type of parameter
                             * @return {{ owner: any, name: string, fn : Function }} The accessible method
                             */
                            static getAccessibleMethod(cls, methodName, parameterType) {
                                if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((parameterType != null && (parameterType["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterType))) || parameterType === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class(cls, methodName, parameterType);
                                }
                                else if (((cls != null && (cls["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(cls))) || cls === null) && ((typeof methodName === 'string') || methodName === null) && ((parameterType != null && parameterType instanceof Array && (parameterType.length == 0 || parameterType[0] == null || (parameterType[0] != null && (parameterType[0]["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(parameterType[0]))))) || parameterType === null)) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class_A(cls, methodName, parameterType);
                                }
                                else if (((cls != null && (cls instanceof Object)) || cls === null) && methodName === undefined && parameterType === undefined) {
                                    return org.openprovenance.apache.commons.lang.reflect.MethodUtils.getAccessibleMethod$java_lang_reflect_Method(cls);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static getAccessibleMethod$java_lang_Class$java_lang_String$java_lang_Class_A(cls, methodName, parameterTypes) {
                                try {
                                    return MethodUtils.getAccessibleMethod$java_lang_reflect_Method(/* getMethod */ ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                        return { owner: c, name: p, fn: c.prototype[p] };
                                    else
                                        return null; })(cls, methodName));
                                }
                                catch (e) {
                                    return (null);
                                }
                            }
                            static getAccessibleMethod$java_lang_reflect_Method(method) {
                                if (!org.openprovenance.apache.commons.lang.reflect.MemberUtils.isAccessible(method)) {
                                    return null;
                                }
                                const cls = method.owner;
                                if (java.lang.reflect.Modifier.isPublic(cls.getModifiers())) {
                                    return method;
                                }
                                const methodName = method.name;
                                const parameterTypes = method.getParameterTypes();
                                method = MethodUtils.getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
                                if (method == null) {
                                    method = MethodUtils.getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
                                }
                                return method;
                            }
                            /**
                             * <p>Return an accessible method (that is, one that can be invoked via
                             * reflection) by scanning through the superclasses. If no such method
                             * can be found, return <code>null</code>.</p>
                             *
                             * @param {*} cls Class to be checked
                             * @param {string} methodName Method name of the method we wish to call
                             * @param {java.lang.Class[]} parameterTypes The parameter type signatures
                             * @return {{ owner: any, name: string, fn : Function }} the accessible method or <code>null</code> if not found
                             * @private
                             */
                            /*private*/ static getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes) {
                                let parentClass = cls.getSuperclass();
                                while ((parentClass != null)) {
                                    {
                                        if (java.lang.reflect.Modifier.isPublic(parentClass.getModifiers())) {
                                            try {
                                                return /* getMethod */ ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                                    return { owner: c, name: p, fn: c.prototype[p] };
                                                else
                                                    return null; })(parentClass, methodName);
                                            }
                                            catch (e) {
                                                return null;
                                            }
                                        }
                                        parentClass = parentClass.getSuperclass();
                                    }
                                }
                                ;
                                return null;
                            }
                            /**
                             * <p>Return an accessible method (that is, one that can be invoked via
                             * reflection) that implements the specified method, by scanning through
                             * all implemented interfaces and subinterfaces.  If no such method
                             * can be found, return <code>null</code>.</p>
                             *
                             * <p> There isn't any good reason why this method must be private.
                             * It is because there doesn't seem any reason why other classes should
                             * call this rather than the higher level methods.</p>
                             *
                             * @param {*} cls Parent class for the interfaces to be checked
                             * @param {string} methodName Method name of the method we wish to call
                             * @param {java.lang.Class[]} parameterTypes The parameter type signatures
                             * @return {{ owner: any, name: string, fn : Function }} the accessible method or <code>null</code> if not found
                             * @private
                             */
                            /*private*/ static getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes) {
                                let method = null;
                                for (; cls != null; cls = cls.getSuperclass()) {
                                    {
                                        const interfaces = cls.getInterfaces();
                                        for (let i = 0; i < interfaces.length; i++) {
                                            {
                                                if (!java.lang.reflect.Modifier.isPublic(interfaces[i].getModifiers())) {
                                                    continue;
                                                }
                                                try {
                                                    method = /* getDeclaredMethod */ ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                                        return { owner: c, name: p, fn: c.prototype[p] };
                                                    else
                                                        return null; })(interfaces[i], methodName);
                                                }
                                                catch (e) {
                                                }
                                                if (method != null) {
                                                    break;
                                                }
                                                method = MethodUtils.getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                                                if (method != null) {
                                                    break;
                                                }
                                            }
                                            ;
                                        }
                                    }
                                    ;
                                }
                                return method;
                            }
                            /**
                             * <p>Find an accessible method that matches the given name and has compatible parameters.
                             * Compatible parameters mean that every method parameter is assignable from
                             * the given parameters.
                             * In other words, it finds a method with the given name
                             * that will take the parameters given.<p>
                             *
                             * <p>This method is used by
                             * {@link
                             * #invokeMethod(Object object, String methodName, Object[] args, Class[] parameterTypes)}.
                             *
                             * <p>This method can match primitive parameter by passing in wrapper classes.
                             * For example, a <code>Boolean</code> will match a primitive <code>boolean</code>
                             * parameter.
                             *
                             * @param {*} cls find method in this class
                             * @param {string} methodName find method with this name
                             * @param {java.lang.Class[]} parameterTypes find method with most compatible parameters
                             * @return {{ owner: any, name: string, fn : Function }} The accessible method
                             */
                            static getMatchingAccessibleMethod(cls, methodName, parameterTypes) {
                                try {
                                    const method = ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                        return { owner: c, name: p, fn: c.prototype[p] };
                                    else
                                        return null; })(cls, methodName);
                                    org.openprovenance.apache.commons.lang.reflect.MemberUtils.setAccessibleWorkaround(method);
                                    return method;
                                }
                                catch (e) {
                                }
                                let bestMatch = null;
                                const methods = (c => Object.getOwnPropertyNames(c.prototype).filter(n => typeof c.prototype[n] == 'function').map(n => ({ owner: c, name: n, fn: c.prototype[n] })))(cls);
                                for (let i = 0, size = methods.length; i < size; i++) {
                                    {
                                        if ( /* getName */methods[i].name === methodName) {
                                            if (org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class_A$java_lang_Class_A$boolean(parameterTypes, methods[i].getParameterTypes(), true)) {
                                                const accessibleMethod = MethodUtils.getAccessibleMethod$java_lang_reflect_Method(methods[i]);
                                                if (accessibleMethod != null) {
                                                    if (bestMatch == null || org.openprovenance.apache.commons.lang.reflect.MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0) {
                                                        bestMatch = accessibleMethod;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    ;
                                }
                                if (bestMatch != null) {
                                    org.openprovenance.apache.commons.lang.reflect.MemberUtils.setAccessibleWorkaround(bestMatch);
                                }
                                return bestMatch;
                            }
                        }
                        reflect.MethodUtils = MethodUtils;
                        MethodUtils["__class"] = "org.openprovenance.apache.commons.lang.reflect.MethodUtils";
                    })(reflect = lang.reflect || (lang.reflect = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
