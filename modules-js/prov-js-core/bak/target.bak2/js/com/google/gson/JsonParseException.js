/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonParseException extends Error {
                constructor(message, cause) {
                    if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof Error) || cause === null)) {
                        let __args = arguments;
                        super(message);
                        this.message = message;
                    }
                    else if (((typeof message === 'string') || message === null) && cause === undefined) {
                        let __args = arguments;
                        super(message);
                        this.message = message;
                    }
                    else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof Error) || message === null) && cause === undefined) {
                        let __args = arguments;
                        let cause = __args[0];
                        super(cause);
                        this.message = cause;
                    }
                    else if (message === undefined && cause === undefined) {
                        let __args = arguments;
                        super();
                    }
                    else
                        throw new Error('invalid overload');
                }
            }
            gson.JsonParseException = JsonParseException;
            JsonParseException["__class"] = "com.google.gson.JsonParseException";
            JsonParseException["__interfaces"] = ["java.io.Serializable"];
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
