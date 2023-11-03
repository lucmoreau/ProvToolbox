package org.openprovenance.prov.template.emitter;

public class Pair implements Element {
    private final String formatPart;
    private final Object arg;

    public Pair(String formatPart, Object arg) {
        this.formatPart=formatPart;
        this.arg=arg;
    }

    @Override
    public String toString() {
        return "Pair{" +
                formatPart + "->'" +
                arg.toString().substring(0,Math.min(arg.toString().length(),5)) +
                '}';
    }

    public String getFormatPart() {
        return formatPart;
    }

    public Object getArg() {
        return arg;
    }


    static public Object getArg(Element o) {
        if (o instanceof Pair) {
            return ((Pair) o).getArg();
        }
        throw new UnsupportedOperationException("Cannot getArg from " + o);
    }
    static public boolean isPair(Element o) {
        return o instanceof Pair;
    }
    static public String getToken(Element o) {
        if (o instanceof Token) {
            return ((Token ) o).token;
        }
        throw new UnsupportedOperationException("Cannot getToken from " + o);
    }
}
