package com.police.demonstrationservice.rest_api.xml_data;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "items")
public class Items {
    @Element(name = "item")
    Item item;

    public Item getItem() {
        return item;
    }
}
