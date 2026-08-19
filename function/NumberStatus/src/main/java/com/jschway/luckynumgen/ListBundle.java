package com.jschway.luckynumgen;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

@JsonPropertyOrder({"starts","ends"})
public class ListBundle { 
    public List<String> starts;
    public List<String> ends;

    public ListBundle() { } 
    public ListBundle(List<String> starts, List<String> ends) {
        this.starts = starts; 
        this.ends = ends;
    }
    @JsonGetter("starts")
    public List<String> getStarts() { return starts; }

    @JsonSetter("starts")
    public void setStarts(List<String> starts) { this.starts = starts; }

    @JsonGetter("ends")
    public List<String> getEnds() { return ends; }
    @JsonSetter("ends")
    public void setEnds(List<String> ends) { this.ends = ends; }
}