package com.jschway.luckynumgen;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author jsaddle
 */
public class PrelimChecks {
    
    
    public static String getReadParameter(Map<?, String> parameters) { 
        String lastX = "";
        if(parameters != null)
            for (var x : parameters.values())  
                lastX = x;
        return lastX;
    }
    public static List<String> gatherPrevious(ListBundleMessage startsEnds) {
        TreeSet<String> previous = new TreeSet<>();
        previous.addAll(startsEnds.getGenerated().getStarts());
        previous.addAll(startsEnds.getGenerated().getEnds());
        return new LinkedList<>(previous);
    }
}
