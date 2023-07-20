package com.police.demonstrationservice.rest_api.xml_data;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "response")
public class Sunset {
    @Element(name = "header")
    Header header;
    @Element(name = "body")
    Body body;

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }
}