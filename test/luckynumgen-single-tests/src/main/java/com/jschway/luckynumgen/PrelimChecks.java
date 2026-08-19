package com.jschway.luckynumgen;

import com.jschway.luckynumgen.s3model.ListBundle;
import com.jschway.luckynumgen.s3model.ListBundleMessage;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author JonathanSaddler
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
//        TreeSet<String> previous = new TreeSet<>();
//        previous.addAll(startsEnds.getGenerated().getStarts());
//        previous.addAll(startsEnds.getGenerated().getEnds());
//        return new LinkedList<>(previous);
        return new LinkedList<>();
    }
    public static boolean bundleFilled(ListBundle remainder, String numberIn) { 
        LinkedHashSet<String> picks = new LinkedHashSet<>();
        // construct potentials list on the fly
        for(int j = 1; j <= 9; j++) {
            picks.add(numberIn + j);
        }
        for (int k = 9; k >= 1; k--)
            picks.add(k+ numberIn);
        for(String p : picks) { 
            if(remainder.getStarts().contains(p) || remainder.getEnds().contains(p)) 
                return true;
        }
        return false;
    }
}
