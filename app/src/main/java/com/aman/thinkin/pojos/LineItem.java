package com.aman.thinkin.pojos;

/**
 * Created by 1305274 on 03-05-2016.
 */
public class LineItem {
    public int sectionManager;

    public int sectionFirstPosition;

    public boolean isHeader;

    public String text;

    public LineItem(String text, boolean isHeader, int sectionManager,
                    int sectionFirstPosition) {
        this.isHeader = isHeader;
        this.text = text;
        this.sectionManager = sectionManager;
        this.sectionFirstPosition = sectionFirstPosition;
    }
}
