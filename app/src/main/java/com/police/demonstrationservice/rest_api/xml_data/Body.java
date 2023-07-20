package com.police.demonstrationservice.rest_api.xml_data;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "body")
public
class Body {
    @Element(name = "items")
    Items items;

    public Items getItems() {
        return items;
    }
}
