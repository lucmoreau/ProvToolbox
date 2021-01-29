/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var configuration;
            (function (configuration) {
                class Configuration {
                    static __static_initialize() { if (!Configuration.__static_initialized) {
                        Configuration.__static_initialized = true;
                        Configuration.__static_initializer_0();
                    } }
                    static toolboxVersion_$LI$() { Configuration.__static_initialize(); return Configuration.toolboxVersion; }
                    static longToolboxVersion_$LI$() { Configuration.__static_initialize(); return Configuration.longToolboxVersion; }
                    static __static_initializer_0() {
                        Configuration.toolboxVersion = /* getProperty */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(Configuration.getPropertiesFromClasspath(Configuration.fileName), "toolbox.version");
                        Configuration.longToolboxVersion = Configuration.toolboxVersion + " (" + /* getProperty */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(Configuration.getPropertiesFromClasspath(Configuration.fileName), "timestamp") + ")";
                    }
                    static getPropertiesFromClasspath$java_lang_Class$java_lang_String(clazz, propFileName) {
                        const props = new Object();
                        const inputStream = clazz.getResourceAsStream(propFileName);
                        if (inputStream == null) {
                            return null;
                        }
                        try {
                            props.load(inputStream);
                        }
                        catch (ee) {
                            return null;
                        }
                        return props;
                    }
                    static getPropertiesFromClasspath(clazz, propFileName) {
                        if (((clazz != null && (clazz["__class"] != null || ((t) => { try {
                            new t;
                            return true;
                        }
                        catch (_a) {
                            return false;
                        } })(clazz))) || clazz === null) && ((typeof propFileName === 'string') || propFileName === null)) {
                            return org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath$java_lang_Class$java_lang_String(clazz, propFileName);
                        }
                        else if (((typeof clazz === 'string') || clazz === null) && propFileName === undefined) {
                            return org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath$java_lang_String(clazz);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static getPropertiesFromClasspath$java_lang_String(propFileName) {
                        return Configuration.getPropertiesFromClasspath$java_lang_Class$java_lang_String(Configuration, propFileName);
                    }
                }
                Configuration.__static_initialized = false;
                Configuration.fileName = "config.properties";
                configuration.Configuration = Configuration;
                Configuration["__class"] = "org.openprovenance.prov.configuration.Configuration";
            })(configuration = prov.configuration || (prov.configuration = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.prov.configuration.Configuration.__static_initialize();
