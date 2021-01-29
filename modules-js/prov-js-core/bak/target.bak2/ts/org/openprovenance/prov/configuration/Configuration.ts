/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.configuration {
    export class Configuration {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!Configuration.__static_initialized) { Configuration.__static_initialized = true; Configuration.__static_initializer_0(); } }

        static fileName: string = "config.properties";

        public static toolboxVersion: string; public static toolboxVersion_$LI$(): string { Configuration.__static_initialize();  return Configuration.toolboxVersion; }

        public static longToolboxVersion: string; public static longToolboxVersion_$LI$(): string { Configuration.__static_initialize();  return Configuration.longToolboxVersion; }

        static  __static_initializer_0() {
            Configuration.toolboxVersion = /* getProperty */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>Configuration.getPropertiesFromClasspath(Configuration.fileName), "toolbox.version");
            Configuration.longToolboxVersion = Configuration.toolboxVersion + " (" + /* getProperty */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>Configuration.getPropertiesFromClasspath(Configuration.fileName), "timestamp") + ")";
        }

        public static getPropertiesFromClasspath$java_lang_Class$java_lang_String(clazz: any, propFileName: string): any {
            const props: any = <any>new Object();
            const inputStream: { str: string, cursor: number } = clazz.getResourceAsStream(propFileName);
            if (inputStream == null){
                return null;
            }
            try {
                props.load(inputStream);
            } catch(ee) {
                return null;
            }
            return props;
        }

        public static getPropertiesFromClasspath(clazz?: any, propFileName?: any): any {
            if (((clazz != null && (clazz["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(clazz))) || clazz === null) && ((typeof propFileName === 'string') || propFileName === null)) {
                return <any>org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath$java_lang_Class$java_lang_String(clazz, propFileName);
            } else if (((typeof clazz === 'string') || clazz === null) && propFileName === undefined) {
                return <any>org.openprovenance.prov.configuration.Configuration.getPropertiesFromClasspath$java_lang_String(clazz);
            } else throw new Error('invalid overload');
        }

        public static getPropertiesFromClasspath$java_lang_String(propFileName: string): any {
            return Configuration.getPropertiesFromClasspath$java_lang_Class$java_lang_String(Configuration, propFileName);
        }
    }
    Configuration["__class"] = "org.openprovenance.prov.configuration.Configuration";

}


org.openprovenance.prov.configuration.Configuration.__static_initialize();
