package com.itcag.split;

import com.itcag.util.txt.TextToolbox;
import java.util.ArrayList;

public final class Lists {
    
    private final boolean ignore ; 
    
    private final ArrayList<String> split = new ArrayList<>();
    private final ArrayList<String> separate = new ArrayList<>();
    private final ArrayList<String> combine = new ArrayList<>();
    
    public Lists(boolean ignore) {
        
        this.ignore = ignore;
        
        separate.add("brand");
        separate.add("description");
        separate.add("directions");
        separate.add("size");
        separate.add("use");
        separate.add("what else you need to know");
        separate.add("what it does");
        separate.add("what it is");
        separate.add("warnings");

        split.add("key benefits");
        split.add("benefits");
        split.add("features");
        split.add("gender");
        split.add("inactive ingredients");
        split.add("active ingredients");
        split.add("ingredients");
        split.add("instructions");
        split.add("solution");
        split.add("uses");
        split.add("old ingredient list");
        split.add("new ingredient list");
        split.add("old ingredients list");
        split.add("new ingredients list");
        split.add("ingredients list");
        split.add("list");

        combine.add("contains");
        combine.add("free of");
        combine.add("helps prevent");
        combine.add("helps");
        combine.add("includes");
        combine.add("may also contain");
        combine.add("may contain");
        combine.add("prevents");
        combine.add("protects against");
        combine.add("protects from");
        combine.add("provides");
        combine.add("used for");
        combine.add("without");

    }

    public final void correct(StringBuilder input) {
        if (ignore) {
            remove(input);
        } else {
            separate(input);
            split(input);
            combine(input);
        }
    }

    private void remove(StringBuilder input) {
        
        ArrayList<String> tmp = new ArrayList<>();
        tmp.addAll(separate);
        tmp.addAll(split);
        tmp.addAll(combine);
        
        for (String header : tmp) {
            int start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            if (start > -1) {
                int end = input.indexOf(".", start);
                if (end == -1) {
                    end = input.length();
                } else {
                    end++;
                }
                input.delete(start, end);
            }
        }
        
    }
    
    /*
     * Separate headers from the text before using a period.
     */
    private void separate(StringBuilder input) {

        for (String header : separate) {
            
            int start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            if (start > -1) {
                
                /*
                 * Separate the header from the previous text (if any) with a period.
                 */
                if (start > 0) {
                    input.insert(start, ". ");
                    start += 2;
                }

            }
        
        }
    
    }
    
    /*
     * Split list items following a header into separate sentences.
     */
    private void split(StringBuilder input) {
        
        for (String header : split) {
            
            int start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            while (start > -1) {
                
                /*
                 * Separate the header from the previous text (if any) with a period.
                 */
                if (start > 0) {
                    input.insert(start, ". ");
                    start += 2;
                }
                
                /*
                 * Replace colon following the header with a period.
                 */
                {
                    int test = input.indexOf(":", start);
                    input.replace(test, test + 1, ". ");
                    start = test + 2;
                }

                /*
                 * Look for the period as terminating the list.
                 * If none found, consider the list to continue
                 * for the rest of the text.
                 */
                int end = input.indexOf(".", start);
                if (end == -1) {
                    end = input.length();
                } else {
                    /*
                     * If the list is not terminated by a period,
                     * ensure that no other header is included in it.
                     */
                    int test = input.indexOf(":", start + header.length() + 1);
                    if (test > -1 && test < end) end = test;
                }
                
                if (end > start) {

                    /*
                     * Replace commas separating the list items with periods.
                     */
                    {
                        int pos = input.indexOf(",", start);
                        while (pos < end && pos > -1) {
                            input.replace(pos, pos + 1, ". ");
                            pos = input.indexOf(",", pos);
                        }
                    }

                    /*
                     * Replace semicolons separating the list items with periods.
                     */
                    {
                        int pos = input.indexOf(";", start);
                        while (pos < end && pos > -1) {
                            input.replace(pos, pos + 1, ". ");
                            pos = input.indexOf(";", pos);
                        }
                    }
                
                    /*
                     * Replace hyphens separating the list items with periods.
                     */
                    {
                        int pos = input.indexOf("-", start);
                        while (pos < end && pos > -1) {
                            input.replace(pos, pos + 1, ". ");
                            pos = input.indexOf("-", pos);
                        }
                    }
                
                }
            
                start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            
            }
        
        }
    
    }
    
    /*
     * Combine headers with the list items that follow them into separate sentences.
     */
    private void combine(StringBuilder input) {
        
        for (String header : combine) {

            int start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            while (start > -1) {
                
                /*
                 * Separate the header from the previous text (if any) with a period.
                 */
                if (start > 0) {
                    input.insert(start - 1, ".");
                    start++;
                }

                /*
                 * Remove colon following the header.
                 */
                {
                    int test = input.indexOf(":", start);
                    input.replace(test, test + 1, "");
                }
                
                /*
                 * Look for the period as terminating the list.
                 * If none found, consider the list to continue
                 * for the rest of the text.
                 */
                int end = input.indexOf(".", start);
                if (end == -1) {
                    end = input.length();
                } else {
                    /*
                     * If the list is not terminated by a period,
                     * ensure that no other header is included in it.
                     */
                    int test = input.indexOf(":", start + header.length() + 1);
                    if (test > -1 && test < end) end = test;
                }
                
                if (end > start) {

                    /*
                     * The list items may be conjuncted using
                     * "and", "or", and "and/or".
                     * For example: "Free of: sulfates, parabens and artificial colors."
                     * Nonetheless, the header should be also inserted
                     * in front of them:
                     * "Free of sulfates. Free of parabens. Free of artificial colors."
                     * Therefore, the conjunctions must be replaced by commas.
                     */
                    end = forceCommas(input, start, end);
                    
                    /*
                     * Replace commas separating the list items with periods,
                     * and insert the header in front of each list item.
                     */
                    {
                        int pos = input.indexOf(",", start);
                        while (pos < end && pos > -1) {
                            String replacement = ". " + TextToolbox.capitalize(header) + " ";
                            input.replace(pos, pos + 1, replacement);
                            end += replacement.length();
                            pos = input.indexOf(",", pos);
                        }
                    }

                    /*
                     * Replace semicolons separating the list items with periods,
                     * and insert the header in front of each list item.
                     */
                    {
                        int pos = input.indexOf(";", start);
                        while (pos < end && pos > -1) {
                            String replacement = ". " + TextToolbox.capitalize(header) + " ";
                            input.replace(pos, pos + 1, replacement);
                            end += replacement.length();
                            pos = input.indexOf(";", pos);
                        }
                    }
                
                    /*
                     * Replace hyphens separating the list items with periods.
                     */
                    {
                        int pos = input.indexOf("-", start);
                        while (pos < end && pos > -1) {
                            String replacement = ". " + TextToolbox.capitalize(header) + " ";
                            input.replace(pos, pos + 1, replacement);
                            end += replacement.length();
                            pos = input.indexOf("-", pos);
                        }
                    }
                
                }

                start = TextToolbox.indexOfCaIn(input.toString(), header + ":");
            
System.out.println(header + "\t|\t" + input);
System.out.println();
                
            }

        }

    }
    
    private int forceCommas(StringBuilder input, int start, int end) {

        String replacement = ", ";

        {
            String searchStr = ", and ";
            int test = input.indexOf(searchStr, start);
            while (test > -1 && test < end) {
                input.replace(test, test + searchStr.length(), replacement);
                end = end - searchStr.length() + replacement.length();
                test = input.indexOf(searchStr, start);
            }
        }

        {
            String searchStr = ",and ";
            int test = input.indexOf(searchStr, start);
            while (test > -1 && test < end) {
                input.replace(test, test + searchStr.length(), replacement);
                end = end - searchStr.length() + replacement.length();
                test = input.indexOf(searchStr, start);
            }
        }

        {
            String searchStr = " and ";
            int test = input.indexOf(searchStr, start);
            while (test > -1 && test < end) {
                input.replace(test, test + searchStr.length(), replacement);
                end = end - searchStr.length() + replacement.length();
                test = input.indexOf(searchStr, start);
            }
        }

        {
            String searchStr = " and/or ";
            int test = input.indexOf(searchStr, start);
            while (test > -1 && test < end) {
                input.replace(test, test + searchStr.length(), replacement);
                end = end - searchStr.length() + replacement.length();
                test = input.indexOf(searchStr, start);
            }
        }

        return end;
        
    }
    
    public boolean isListHeader(String test) {
        if (split.contains(test.toLowerCase())) return true;
        return combine.contains(test.toLowerCase());
    }
    
}
