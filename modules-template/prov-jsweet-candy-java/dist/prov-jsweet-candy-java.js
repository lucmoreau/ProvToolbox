/* Generated from Java with JSweet 3.1.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var utils;
            (function (utils) {
                /**
                 * T36: JSweet candy of {@code org.openprovenance.prov.utils.JsonText} (prov-model), so that the generated client writes and
                 * reads the JSON text of an {@code @inJson} carrier in JavaScript as it does in Java. Both must give the same text for the
                 * same values: the conformance vectors of the compiler's tests ({@code t36/json-text-vectors.json}) are run against each.
                 * <p>
                 * This is the JavaScript implementation: JavaScript has one number type, so a number is written as the language writes it,
                 * which is the rule of RFC 8785 ({@code "" + d}); it is not meant to run on a JVM, where prov-model's class is used. Written
                 * with what JSweet translates: no {@code BigDecimal}, no {@code String.format}, no exceptions from number parsing. An object is a
                 * JavaScript object, so a key that reads as an array index (an integer) is written before the others, whatever its place.
                 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
                 * @class
                 */
                class JsonText {
                    static newObject() {
                        return ({});
                    }
                    static write(map) {
                        if (map == null)
                            throw Object.defineProperty(new Error("T36: JSON text of a null map"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        return JsonText.toText(map);
                    }
                    static toText(value) {
                        const sb = { str: "", toString: function () { return this.str; } };
                        JsonText.writeValue(sb, value);
                        return /* toString */ sb.str;
                    }
                    /**
                     * The text of a number or a boolean as the carrier writes it, a string as itself, null as null (T36 part E: the conversion "as text" of a flow).
                     * @param {*} value
                     * @return {string}
                     */
                    static text(value) {
                        if (value == null)
                            return null;
                        if (typeof value === 'string')
                            return value;
                        if (typeof value === 'boolean')
                            return JsonText.toText(value);
                        if (typeof value === 'number')
                            return JsonText.plain(JsonText.toText(value));
                        throw Object.defineProperty(new Error("T36: a value of that kind has no text"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                    }
                    /**
                     * T42: the text of a number without its exponent: the decimal point of RFC 8785's text ("1.2345e+25", "-5e-324") moved by the exponent.
                     * @param {string} text
                     * @return {string}
                     */
                    static plain(text) {
                        const e = text.indexOf('e');
                        if (e < 0)
                            return text;
                        let mantissa = text.substring(0, e);
                        const exponent = text.substring(e + 1);
                        const exp = parseInt((c => c.charCodeAt == null ? c : c.charCodeAt(0))(exponent.charAt(0)) == '+'.charCodeAt(0) ? exponent.substring(1) : exponent);
                        const negative = (c => c.charCodeAt == null ? c : c.charCodeAt(0))(mantissa.charAt(0)) == '-'.charCodeAt(0);
                        if (negative)
                            mantissa = mantissa.substring(1);
                        const dot = mantissa.indexOf('.');
                        const digits = dot < 0 ? mantissa : mantissa.substring(0, dot) + mantissa.substring(dot + 1);
                        const point = (dot < 0 ? mantissa.length : dot) + exp;
                        const sb = { str: "", toString: function () { return this.str; } };
                        if (negative) /* append */
                            (sb => { sb.str += "-"; return sb; })(sb);
                        if (point >= digits.length) {
                            /* append */ (sb => { sb.str += digits; return sb; })(sb);
                            for (let i = digits.length; i < point; i++) { /* append */
                                (sb => { sb.str += "0"; return sb; })(sb);
                            }
                        }
                        else if (point > 0) {
                            /* append */ (sb => { sb.str += digits.substring(point); return sb; })(/* append */ (sb => { sb.str += "."; return sb; })(/* append */ (sb => { sb.str += digits.substring(0, point); return sb; })(sb)));
                        }
                        else {
                            /* append */ (sb => { sb.str += "0."; return sb; })(sb);
                            for (let i = 0; i < -point; i++) { /* append */
                                (sb => { sb.str += "0"; return sb; })(sb);
                            }
                            /* append */ (sb => { sb.str += digits; return sb; })(sb);
                        }
                        return /* toString */ sb.str;
                    }
                    static writeValue(sb, v) {
                        if (v == null) {
                            /* append */ (sb => { sb.str += "null"; return sb; })(sb);
                        }
                        else if (typeof v === 'string') {
                            JsonText.writeString(sb, v);
                        }
                        else if (typeof v === 'boolean') {
                            /* append */ (sb => { sb.str += v ? "true" : "false"; return sb; })(sb);
                        }
                        else if (typeof v === 'number') {
                            const d = v;
                            if (d !== d || d === Infinity || d === -Infinity) {
                                throw Object.defineProperty(new Error("T36: not a JSON number: " + d), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            /* append */ (sb => { sb.str += "" + d; return sb; })(sb);
                        }
                        else if (v != null && (v instanceof Array)) {
                            /* append */ (sb => { sb.str += '['; return sb; })(sb);
                            let first = true;
                            const l = v;
                            for (let i = 0; i < /* size */ l.length; i++) {
                                {
                                    if (!first) /* append */
                                        (sb => { sb.str += ','; return sb; })(sb);
                                    first = false;
                                    JsonText.writeValue(sb, /* get */ l[i]);
                                }
                                ;
                            }
                            /* append */ (sb => { sb.str += ']'; return sb; })(sb);
                        }
                        else if (v != null && (v instanceof Object)) {
                            /* append */ (sb => { sb.str += '{'; return sb; })(sb);
                            let first = true;
                            const m = v;
                            {
                                let array = /* keySet */ Object.keys(m);
                                for (let index = 0; index < array.length; index++) {
                                    let k = array[index];
                                    {
                                        if (!first) /* append */
                                            (sb => { sb.str += ','; return sb; })(sb);
                                        first = false;
                                        JsonText.writeString(sb, k);
                                        /* append */ (sb => { sb.str += ':'; return sb; })(sb);
                                        JsonText.writeValue(sb, /* get */ ((m, k) => m[k] === undefined ? null : m[k])(m, k));
                                    }
                                }
                            }
                            /* append */ (sb => { sb.str += '}'; return sb; })(sb);
                        }
                        else {
                            throw Object.defineProperty(new Error("T36: not a JSON value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                    }
                    static writeString(sb, s) {
                        /* append */ (sb => { sb.str += '\"'; return sb; })(sb);
                        for (let i = 0; i < s.length; i++) {
                            {
                                const c = s.charAt(i);
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\"'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\\""; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\\'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\\\"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\b'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\b"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\f'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\f"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\n'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\n"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\r'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\r"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\t'.charCodeAt(0)) {
                                    /* append */ (sb => { sb.str += "\\t"; return sb; })(sb);
                                }
                                else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) < 32) {
                                    /* append */ (sb => { sb.str += JsonText.HEX.charAt((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) & 15); return sb; })(/* append */ (sb => { sb.str += JsonText.HEX.charAt(((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) >> 4) & 15); return sb; })(/* append */ (sb => { sb.str += "\\u00"; return sb; })(sb)));
                                }
                                else {
                                    /* append */ (sb => { sb.str += c; return sb; })(sb);
                                }
                            }
                            ;
                        }
                        /* append */ (sb => { sb.str += '\"'; return sb; })(sb);
                    }
                    static read(text) {
                        if (text == null || text.trim().length === 0)
                            return JsonText.newObject();
                        const v = JsonText.parse(text);
                        if ((v != null && (v instanceof Array)) || !(v != null && (v instanceof Object)))
                            throw Object.defineProperty(new Error("T36: JSON text is not an object"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        return v;
                    }
                    static parse(text) {
                        const p = new JsonText.Parser(text);
                        p.ws();
                        const v = p.value();
                        p.ws();
                        if (p.i !== text.length)
                            throw p.error("trailing characters");
                        return v;
                    }
                    static merge(declared, others, where) {
                        const m = JsonText.newObject();
                        {
                            let array = /* keySet */ Object.keys(declared);
                            for (let index = 0; index < array.length; index++) {
                                let k = array[index];
                                /* put */ (m[k] = /* get */ ((m, k) => m[k] === undefined ? null : m[k])(declared, k));
                            }
                        }
                        if (others != null) {
                            {
                                let array = /* keySet */ Object.keys(others);
                                for (let index = 0; index < array.length; index++) {
                                    let k = array[index];
                                    {
                                        if ( /* containsKey */declared.hasOwnProperty(k)) {
                                            throw Object.defineProperty(new Error("T36: " + where + ": the key " + k + " is declared, it cannot also be among the other keys"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                        }
                                        /* put */ (m[k] = /* get */ ((m, k) => m[k] === undefined ? null : m[k])(others, k));
                                    }
                                }
                            }
                        }
                        return m;
                    }
                    static others(object, declared) {
                        const m = JsonText.newObject();
                        {
                            let array = /* keySet */ Object.keys(object);
                            for (let index = 0; index < array.length; index++) {
                                let k = array[index];
                                {
                                    let isDeclared = false;
                                    for (let i = 0; i < declared.length; i++) {
                                        if (declared[i] === k)
                                            isDeclared = true;
                                        ;
                                    }
                                    if (!isDeclared) /* put */
                                        (m[k] = /* get */ ((m, k) => m[k] === undefined ? null : m[k])(object, k));
                                }
                            }
                        }
                        return m;
                    }
                    static notA(key, what) {
                        return Object.defineProperty(new Error("T36: the member " + key + " is not " + what), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                    }
                    static getString(m, key) {
                        const v = ((m, k) => m[k] === undefined ? null : m[k])(m, key);
                        if (v == null)
                            return null;
                        if (!(typeof v === 'string'))
                            throw JsonText.notA(key, "a string");
                        return v;
                    }
                    static getJson(m, key) {
                        const v = ((m, k) => m[k] === undefined ? null : m[k])(m, key);
                        return v == null ? null : JsonText.toText(v);
                    }
                    static getBoolean(m, key) {
                        const v = ((m, k) => m[k] === undefined ? null : m[k])(m, key);
                        if (v == null)
                            return null;
                        if (!(typeof v === 'boolean'))
                            throw JsonText.notA(key, "true or false");
                        return v;
                    }
                    static getLong(m, key) {
                        const v = ((m, k) => m[k] === undefined ? null : m[k])(m, key);
                        if (v == null)
                            return null;
                        if (!(typeof v === 'number'))
                            throw JsonText.notA(key, "an integer");
                        const d = v;
                        if (d !== Math.floor(d) || Math.abs(d) >= 9.007199254740992E15)
                            throw JsonText.notA(key, "an integer");
                        return (n => n < 0 ? Math.ceil(n) : Math.floor(n))(d);
                    }
                    static getInteger(m, key) {
                        const l = JsonText.getLong(m, key);
                        if (l == null)
                            return null;
                        if ( /* longValue */l < -2147483648 || /* longValue */ l > 2147483647)
                            throw JsonText.notA(key, "an integer that fits an int");
                        return (l | 0);
                    }
                    static getDouble(m, key) {
                        const v = ((m, k) => m[k] === undefined ? null : m[k])(m, key);
                        if (v == null)
                            return null;
                        if (!(typeof v === 'number'))
                            throw JsonText.notA(key, "a number");
                        return /* doubleValue */ v;
                    }
                    static getFloat(m, key) {
                        const d = JsonText.getDouble(m, key);
                        return d == null ? null : Math.fround(/* doubleValue */ d);
                    }
                    static same(actual, constant) {
                        if ((typeof actual === 'number') && (typeof constant === 'number')) {
                            return /* doubleValue */ actual === /* doubleValue */ constant;
                        }
                        if (actual == null)
                            return constant == null;
                        return /* equals */ ((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(actual, constant);
                    }
                }
                JsonText.HEX = "0123456789abcdef";
                utils.JsonText = JsonText;
                JsonText["__class"] = "org.openprovenance.prov.utils.JsonText";
                (function (JsonText) {
                    class Parser {
                        constructor(s) {
                            if (this.s === undefined) {
                                this.s = null;
                            }
                            this.i = 0;
                            this.s = s;
                        }
                        error(why) {
                            return Object.defineProperty(new Error("T36: JSON text, offset " + this.i + ": " + why), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                        ws() {
                            while ((this.i < this.s.length && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == ' '.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '\t'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '\n'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '\r'.charCodeAt(0)))) {
                                this.i++;
                            }
                            ;
                        }
                        value() {
                            if (this.i >= this.s.length)
                                throw this.error("unexpected end");
                            const c = this.s.charAt(this.i);
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '{'.charCodeAt(0))
                                return this.object();
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '['.charCodeAt(0))
                                return this.array();
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\"'.charCodeAt(0))
                                return this.string();
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == 't'.charCodeAt(0)) {
                                this.literal("true");
                                return true;
                            }
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == 'f'.charCodeAt(0)) {
                                this.literal("false");
                                return false;
                            }
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == 'n'.charCodeAt(0)) {
                                this.literal("null");
                                return null;
                            }
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '-'.charCodeAt(0) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) <= '9'.charCodeAt(0)))
                                return this.number();
                            throw this.error("unexpected character");
                        }
                        literal(word) {
                            if (!((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(this.s, word, this.i))
                                throw this.error("expected " + word);
                            this.i += word.length;
                        }
                        object() {
                            const m = JsonText.newObject();
                            this.i++;
                            this.ws();
                            if (this.i < this.s.length && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '}'.charCodeAt(0)) {
                                this.i++;
                                return m;
                            }
                            while ((true)) {
                                {
                                    this.ws();
                                    if (this.i >= this.s.length || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) != '\"'.charCodeAt(0))
                                        throw this.error("expected a key");
                                    const k = this.string();
                                    this.ws();
                                    if (this.i >= this.s.length || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) != ':'.charCodeAt(0))
                                        throw this.error("expected \':\'");
                                    this.i++;
                                    this.ws();
                                    /* put */ (m[k] = this.value());
                                    this.ws();
                                    if (this.i >= this.s.length)
                                        throw this.error("unexpected end");
                                    const c = this.s.charAt(this.i);
                                    this.i++;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '}'.charCodeAt(0))
                                        return m;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) != ','.charCodeAt(0))
                                        throw this.error("expected \',\' or \'}\'");
                                }
                            }
                            ;
                        }
                        array() {
                            const l = ([]);
                            this.i++;
                            this.ws();
                            if (this.i < this.s.length && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == ']'.charCodeAt(0)) {
                                this.i++;
                                return l;
                            }
                            while ((true)) {
                                {
                                    this.ws();
                                    /* add */ (l.push(this.value()) > 0);
                                    this.ws();
                                    if (this.i >= this.s.length)
                                        throw this.error("unexpected end");
                                    const c = this.s.charAt(this.i);
                                    this.i++;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == ']'.charCodeAt(0))
                                        return l;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) != ','.charCodeAt(0))
                                        throw this.error("expected \',\' or \']\'");
                                }
                            }
                            ;
                        }
                        hex(c) {
                            const h = org.openprovenance.prov.utils.JsonText.HEX.indexOf(/* toLowerCase */ c.toLowerCase());
                            if (h < 0)
                                throw this.error("bad \\u escape");
                            return h;
                        }
                        string() {
                            const sb = { str: "", toString: function () { return this.str; } };
                            this.i++;
                            while ((true)) {
                                {
                                    if (this.i >= this.s.length)
                                        throw this.error("unterminated string");
                                    const c = this.s.charAt(this.i);
                                    this.i++;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) == '\"'.charCodeAt(0))
                                        return /* toString */ sb.str;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) < 32)
                                        throw this.error("control character in string");
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(c) != '\\'.charCodeAt(0)) {
                                        /* append */ (sb => { sb.str += c; return sb; })(sb);
                                        continue;
                                    }
                                    if (this.i >= this.s.length)
                                        throw this.error("unterminated escape");
                                    const e = this.s.charAt(this.i);
                                    this.i++;
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == '\"'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\"'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == '\\'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\\'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == '/'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '/'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 'b'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\b'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 'f'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\f'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 'n'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\n'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 'r'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\r'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 't'.charCodeAt(0)) /* append */
                                        (sb => { sb.str += '\t'; return sb; })(sb);
                                    else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(e) == 'u'.charCodeAt(0)) {
                                        if (this.i + 4 > this.s.length)
                                            throw this.error("short \\u escape");
                                        const code = (this.hex(this.s.charAt(this.i)) << 12) | (this.hex(this.s.charAt(this.i + 1)) << 8) | (this.hex(this.s.charAt(this.i + 2)) << 4) | this.hex(this.s.charAt(this.i + 3));
                                        /* append */ (sb => { sb.str += String.fromCharCode(code); return sb; })(sb);
                                        this.i += 4;
                                    }
                                    else {
                                        throw this.error("bad escape");
                                    }
                                }
                            }
                            ;
                        }
                        digit() {
                            return this.i < this.s.length && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) <= '9'.charCodeAt(0);
                        }
                        number() {
                            const start = this.i;
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '-'.charCodeAt(0))
                                this.i++;
                            if (this.i >= this.s.length)
                                throw this.error("bad number");
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '0'.charCodeAt(0)) {
                                this.i++;
                            }
                            else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) >= '1'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) <= '9'.charCodeAt(0)) {
                                while ((this.digit())) {
                                    this.i++;
                                }
                                ;
                            }
                            else {
                                throw this.error("bad number");
                            }
                            if (this.i < this.s.length && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '.'.charCodeAt(0)) {
                                this.i++;
                                const d = this.i;
                                while ((this.digit())) {
                                    this.i++;
                                }
                                ;
                                if (d === this.i)
                                    throw this.error("bad number");
                            }
                            if (this.i < this.s.length && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == 'E'.charCodeAt(0))) {
                                this.i++;
                                if (this.i < this.s.length && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '+'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(this.s.charAt(this.i)) == '-'.charCodeAt(0)))
                                    this.i++;
                                const d = this.i;
                                while ((this.digit())) {
                                    this.i++;
                                }
                                ;
                                if (d === this.i)
                                    throw this.error("bad number");
                            }
                            return /* parseDouble */ parseFloat(this.s.substring(start, this.i));
                        }
                    }
                    JsonText.Parser = Parser;
                    Parser["__class"] = "org.openprovenance.prov.utils.JsonText.Parser";
                })(JsonText = utils.JsonText || (utils.JsonText = {}));
            })(utils = prov.utils || (prov.utils = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var utils;
            (function (utils) {
                /**
                 * T31 (R16): JSweet candy of {@code org.openprovenance.prov.utils.Pseudonyms} (prov-model), so that the
                 * generated client checks a pseudonymised variable in JavaScript as it does in Java. Patterns are anchored
                 * ({@code ^...$}), so {@code RegExp.test} matches the whole value, as Java's {@code matches()} does; the {@code u} flag
                 * reads non-ASCII characters as code points.
                 * @class
                 */
                class Pseudonyms {
                    static matches(value, pattern) {
                        return value == null || new RegExp(pattern, "u").test(value);
                    }
                    static check(value, pattern, template, variable, method) {
                        if (!Pseudonyms.matches(value, pattern)) {
                            throw Object.defineProperty(new Error("T31: template " + template + ", variable " + variable + ": value refused, not in the form of @pseudonym(" + method + ")"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                    }
                }
                utils.Pseudonyms = Pseudonyms;
                Pseudonyms["__class"] = "org.openprovenance.prov.utils.Pseudonyms";
            })(utils = prov.utils || (prov.utils = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
var java;
(function (java) {
    var util;
    (function (util) {
        var concurrent;
        (function (concurrent) {
            var atomic;
            (function (atomic) {
                /**
                 * JSweet candy implementation of {@code java.util.concurrent.atomic.AtomicInteger}.
                 *
                 * JavaScript is single-threaded, so a plain field provides the required
                 * semantics; only the api surface of the JDK class is reproduced (the subset
                 * commonly used by generated template code, plus the usual accessors).
                 * @param {number} initialValue
                 * @class
                 */
                class AtomicInteger {
                    constructor(initialValue) {
                        if (((typeof initialValue === 'number') || initialValue === null)) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = 0;
                            }
                            this.value = initialValue;
                        }
                        else if (initialValue === undefined) {
                            let __args = arguments;
                            if (this.value === undefined) {
                                this.value = 0;
                            }
                            this.value = 0;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    get() {
                        return this.value;
                    }
                    set(newValue) {
                        this.value = newValue;
                    }
                    getAndSet(newValue) {
                        const previous = this.value;
                        this.value = newValue;
                        return previous;
                    }
                    getAndIncrement() {
                        return this.value++;
                    }
                    getAndDecrement() {
                        return this.value--;
                    }
                    incrementAndGet() {
                        return ++this.value;
                    }
                    decrementAndGet() {
                        return --this.value;
                    }
                    getAndAdd(delta) {
                        const previous = this.value;
                        this.value = this.value + delta;
                        return previous;
                    }
                    addAndGet(delta) {
                        this.value = this.value + delta;
                        return this.value;
                    }
                    compareAndSet(expectedValue, newValue) {
                        if (this.value === expectedValue) {
                            this.value = newValue;
                            return true;
                        }
                        return false;
                    }
                    intValue() {
                        return this.value;
                    }
                    longValue() {
                        return this.value;
                    }
                    floatValue() {
                        return this.value;
                    }
                    doubleValue() {
                        return this.value;
                    }
                    toString() {
                        return /* valueOf */ String(this.value).toString();
                    }
                }
                atomic.AtomicInteger = AtomicInteger;
                AtomicInteger["__class"] = "org.openprovenance.candies.java.util.concurrent.atomic.AtomicInteger";
            })(atomic = concurrent.atomic || (concurrent.atomic = {}));
        })(concurrent = util.concurrent || (util.concurrent = {}));
    })(util = java.util || (java.util = {}));
})(java || (java = {}));
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    class StringEscapeUtils {
                        static escapeJavaScript(format) {
                            return format;
                        }
                        static escapeCsv(format) {
                            const newFormat = format.split("\"").join("\"\"");
                            if (newFormat === format) {
                                if ( /* contains */(format.indexOf(",") != -1) || /* contains */ (format.indexOf("\n") != -1) || /* contains */ (format.indexOf("\r") != -1)) {
                                    return "\"" + format + "\"";
                                }
                                else {
                                    return format;
                                }
                            }
                            else {
                                return "\"" + newFormat + "\"";
                            }
                        }
                    }
                    lang.StringEscapeUtils = StringEscapeUtils;
                    StringEscapeUtils["__class"] = "org.openprovenance.apache.commons.lang.StringEscapeUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
