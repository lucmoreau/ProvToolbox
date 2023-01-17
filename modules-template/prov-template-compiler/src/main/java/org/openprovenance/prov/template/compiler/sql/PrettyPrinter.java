package org.openprovenance.prov.template.compiler.sql;

//
// Simple PrettyPrinter -- Andrew C. Myers, March 1999
//   For use in Cornell University Computer Science 412/413

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PrettyPrinter
{
    private final ByteArrayOutputStream o;

    // A pretty-printer formats text onto an
    // output stream "o" while keeping the width of the output
    // within "width" characters if possible
    public PrettyPrinter(ByteArrayOutputStream o, int width) {
        output = new DataOutputStream(o);
        this.width = width;
        current = input = new Block(null, 0);
        this.o=o;
    }

    public ByteArrayOutputStream getOutputStream() {
        return o;
    }



    public void writeBlue(String s) {
        write0(OKBLUE); write(s); write0(ENDC);
    }
    public void writeOrange(String s) {
        write0(ORANGE256); write(s); write0(END256);
    }
    public void writeGreen(String s) {
        write0(GREEN3_256); write(s); write0(END256);
    }
    public void writeRed(String s) {
        write0(RED256); write(s); write0(END256);
    }

    public void writePurple(String s) {
        write0(PURPLE256); write(s); write0(END256);
    }

    public void write(String s) {
        // Print the string "s" on the output stream
        current.add(new StringItem(s));
    }
    public void write0(String s) {
        // Print the string "s" on the output stream
        current.add(new StringItem0(s));
    }
    public void begin(int n) {
        // Start a new block with indentation increased
        // by "n" characters
        Block b = new Block(current, n);
        current.add(b);
        current = b;
    }
    public void end() {
        // Terminate the most recent outstanding "begin"
        current = current.parent;
        if (current == null) throw new RuntimeException();
    }
    public void allowBreak(int n) {
        // Allow a newline. Indentation will be preserved.
        current.add(new AllowBreak(n));
    }
    public void newline(int n) {
        // Force a newline. Indentation will be preserved.
        current.add(new Newline(n));

    }

    String HEADER = "\033[95m";
    String OKBLUE = "\033[94m";
    String OKCYAN = "\033[96m";
    String OKGREEN = "\033[92m";
    String WARNING = "\033[93m";
    String FAIL = "\033[91m";
    String ENDC = "\033[0m";
    String BOLD = "\033[1m";
    String UNDERLINE = "\033[4m";

    String YELLOW_BRIGHT="\033[0;93m";
    //String OKRED="\\u001b[31;1m";

    public static String color(int r, int g, int b) {
        return "\033[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static String colorEnd="\033[0m";
    String ORANGE24=color(255,127,0);

    public static String color256(int v) {
        return "\033[38;5;" + v + "m";
    }
    static public String GREEN_256=color256(2);
    static public String RED256=color256(9);
    static public String ORANGE256=color256(202);
    static public String GREEN3_256 =color256(34);
    static public String PURPLE256=color256(129);
    static public String END256="\033[0m";

    public void flush()  {
        // Send out the current batch of text
        // to be formatted, closing all
        // outstanding "begin"'s and resetting
        // the indentation level to 0.
        try {
            input.format(0, 0, width, true);
        } catch (Overrun e) {
        }
        try {
            input.sendOutput(output, 0, 0);
            output.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        current = input = new Block(null, 0);
    }

    Block input;
    Block current;

    DataOutputStream output;
    int width;

    public void comma() {
        write(", ");
        allowBreak(0);
    }
    public void open() {
        write ("("); begin(0);
    }
    public void close() {
        end();
        write (")");
    }

}

class Overrun extends Exception {
    // An overrun represents a formatting that failed because the right
    // margin was exceeded by at least "amount" chars.
    int amount;
    Overrun(int amount_) { amount = amount_; }
}

abstract class Item {
    Item() { next = null; }
    int format(int lmargin, int pos, int rmargin, boolean canBreak) throws Overrun {
        // try to format a whole sequence of items in the manner of format1
        if (pos > rmargin) throw new Overrun(pos - rmargin);
        pos = format1(lmargin, pos, rmargin, canBreak);
        if (next == null) return pos;
        else return next.format(lmargin, pos, rmargin, canBreak);
    }
    abstract int format1(int lmargin, int pos, int rmargin, boolean canBreak) throws Overrun;
    // Try to format this item with a current cursor position of
    // "pos", left and right margins as specified. Returns the
    // final position. If breaks
    // may be broken, "canBreak" is set. Return the new cursor
    // position and set any contained breaks appropriately if formatting
    // was successful. Requires rmargin > lmargin, pos <= rmargin.
    abstract int sendOutput(DataOutputStream o, int lmargin, int pos) throws IOException;
    // Send the output associated with this item to "o", using the
    // current break settings.
    Item next;
}

class Block extends Item {
    Block parent;
    Item first;
    Item last;
    int indent;
    Block(Block parent, int indent) {
        this.parent = parent;
        this.first = this.last = null;
        this.indent = indent;
    }
    void add(Item it) {
        if (first == null) first = it; else last.next = it;
        last = it;
    }
    int format1(int lmargin, int pos, int rmargin, boolean canBreak) throws Overrun
    {
        if (first == null) return pos;
        try {
            return first.format(pos + indent, pos, rmargin, false);
        } catch (Overrun overrun) {
            if (!canBreak) throw overrun;
            return first.format(pos + indent, pos, rmargin, true);
        }
    }
    int sendOutput(DataOutputStream o, int lmargin, int pos)
            throws IOException {
        Item it = first;
        lmargin = pos+indent;
        while (it != null) {
            pos = it.sendOutput(o, lmargin, pos);
            it = it.next;
        }
        return pos;
    }
}

class StringItem extends Item {
    String s;
    StringItem(String s_) { s = s_; }
    int format1(int lmargin, int pos, int rmargin, boolean canBreak) throws Overrun {
        pos += s.length();
        if (pos > rmargin) throw new Overrun(rmargin - pos);
        return pos;
    }
    int sendOutput(DataOutputStream o, int lm, int pos) throws IOException {
        o.writeBytes(s);
        return pos + s.length();
    }
}
class StringItem0 extends Item {
    String s;
    StringItem0(String s_) { s = s_; }
    int format1(int lmargin, int pos, int rmargin, boolean canBreak) throws Overrun {
        pos += 0;
        if (pos > rmargin) throw new Overrun(rmargin - pos);
        return pos;
    }
    int sendOutput(DataOutputStream o, int lm, int pos) throws IOException {
        o.writeBytes(s);
        return pos + 0;
    }
}


class AllowBreak extends Item {
    int indent;
    boolean broken = true;
    AllowBreak(int n_) { indent = n_; }
    int format1(int lmargin, int pos, int rmargin, boolean canBreak)  throws Overrun {
        if (canBreak) {
            broken = true;
            return lmargin + indent;
        } else {
            broken = false;
            return pos;
        }
    }
    int sendOutput(DataOutputStream o, int lmargin, int pos)  throws IOException {
        if (broken) {
            o.writeBytes("\r\n");
            for (int i = 0; i < lmargin + indent; i++) o.writeBytes(" ");
            return lmargin + indent;
        } else {
            return pos;
        }
    }
}

class Newline extends AllowBreak {
    Newline(int n_) { super(n_); }
    int format1(int lmargin, int pos, int rmargin, boolean canBreak, boolean forceBreak) throws Overrun {
        if (!canBreak) throw new Overrun(1);
        broken = true;
        return lmargin + indent;
    }
    int sendOutput(DataOutputStream o, int lmargin, int pos) throws IOException {
        o.writeBytes("\r\n");
        for (int i = 0; i < lmargin + indent; i++) o.writeBytes(" ");
        return lmargin + indent;
    }
}
