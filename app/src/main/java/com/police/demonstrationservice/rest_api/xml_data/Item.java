package com.police.demonstrationservice.rest_api.xml_data;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "item")
public class Item {
    @PropertyElement(name = "sunrise")
    String sunrise;
    @PropertyElement(name = "sunset")
    String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
