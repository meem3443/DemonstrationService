package com.police.demonstrationservice.rest_api.xml_data;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "header")
public class Header {
    @PropertyElement(name = "resultCode")
    String resultCode;
    @PropertyElement(name = "resultMsg")
    String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }
}
