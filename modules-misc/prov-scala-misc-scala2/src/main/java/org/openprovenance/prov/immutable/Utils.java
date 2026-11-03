package org.openprovenance.prov.immutable;

import java.util.List;

public class Utils {

    public String longestCommonPrefix(List<String> strs) {
        return longestCommonPrefix(strs.toArray(new String[] {}));
    }

    public String longestCommonPrefix(String[] strs) {
        String prefix = new String();
        if (strs.length > 0)
            prefix = strs[0];
        for (int i = 1; i < strs.length; ++i) {
            String s = strs[i];
            int j = 0;
            for (; j < Math.min(prefix.length(), s.length()); ++j) {
                if (prefix.charAt(j) != s.charAt(j)) {
                    break;
                }
            }
            prefix = prefix.substring(0, j);
        }
        return prefix;
    }

    /* Heuristics */

    public String commonLocalName(List<String> strs) {
        String pre = longestCommonPrefix(strs);
        //System.out.println("prefix is " + pre);

        while (!pre.isEmpty() &&
                (pre.charAt(pre.length()-1)=='/'
                ||
                pre.charAt(pre.length()-1)==':')) {
            pre=pre.substring(0, pre.length()-1);
        }
        
        int i = pre.lastIndexOf('/');
        
        if (i<=0) {
            i = pre.lastIndexOf(':');  // as in urn:schemas:
            if (i<0)
                return null;
        }
        if ((i < pre.length() - 1)) {
            String local = pre.substring(i + 1);
            if (local.contains("#")) {
                local = local.replace("#", "");
                if (local.length() == 0) {
                    return null;
                }
            } 
            //System.out.println("local is " + local);
            try {
                @SuppressWarnings("unused")
                int val = Integer.valueOf(local);
                pre = pre.substring(0, i);
                int j = pre.lastIndexOf('/');
                if (j<=0) {
                    j = pre.lastIndexOf(':');
                }
                if ((j > 0) && (j < pre.length() - 1)) {
                    String local2 = pre.substring(j + 1);
                    if (local2.length() == 0) {
                        return null;
                    } else {
                        return local2;
                    }
                }

            } catch (NumberFormatException e) {
                // OK, it's not a number
            }
            return local;
        } else {
            
        }
        return null;
    }


}
