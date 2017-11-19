package com.techbridge.smsreader.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.List;

public class Month extends ExpandableGroup<MonthDetails> {
    private List<MonthDetails> items;
    private String title;

    public Month(String title, List<MonthDetails> items) {
        super(title, items);
        this.items = items;
        this.title = title;
    }

    public String getName() {
        return this.title;
    }

    public List<MonthDetails> getItems() {
        return this.items;
    }
}
