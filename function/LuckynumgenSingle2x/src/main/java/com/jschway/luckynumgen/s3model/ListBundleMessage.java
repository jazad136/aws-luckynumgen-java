package com.jschway.luckynumgen.s3model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class ListBundleMessage { 
        public ListBundle generated;
        
        public ListBundleMessage() { } 
        public ListBundleMessage(List<String> starts, List<String> ends) {
            generated = new ListBundle(starts, ends);
        }
        @JsonGetter("generated")
        public ListBundle getGenerated() { return generated; } 
        @JsonSetter("generated")
        public void setGenerated(ListBundle value) { this.generated = value; } 
    }