package com.kline.city;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mei on 2016/3/29.
 */
public class XmlParserHandler extends DefaultHandler {
    private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

    ProvinceModel provinceModel = new ProvinceModel();
    CityModel cityModel = new CityModel();
    DistrictModel districtModel = new DistrictModel();

    public XmlParserHandler() {
    }

    public List<ProvinceModel> getDataList(){
        return provinceList;
    }

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一标签是时， 会触发这个方法
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 当遇到开始标记时，调用这个方法

        if(qName.equals("province")){
            provinceModel = new ProvinceModel();
            provinceModel.setName(attributes.getValue(0));
            provinceModel.setCityList(new ArrayList<CityModel>());
        } else if(qName.equals("city")){
            cityModel = new CityModel();
            cityModel.setName(attributes.getValue(0));
            cityModel.setDistrictList(new ArrayList<DistrictModel>());
        } else if(qName.equals("district")) {
            districtModel = new DistrictModel();
            districtModel.setName(attributes.getValue(0));
            districtModel.setZipCode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 遇到结束标签时， 会调用这个方法
        if(qName.equals("district")){
            cityModel.getDistrictList().add(districtModel);
        } else if(qName.equals("city")) {
            provinceModel.getCityList().add(cityModel);
        } else if(qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }
}
