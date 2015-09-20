package com.jtao.coolweather.util;

/**
 * Created by Tap on 2015/9/20.
 */

import com.jtao.coolweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * XML解析工具类，通过pull解析
 */
public class XmlParser {
    public static List<Province> parserProvinceXMLWithPull(String xmlData){

        List<Province> provinces;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xmlData));  //读取数据

            int eventType = parser.getEventType();

            String quName = "";
            String pyName = "";
            String cityName = "";
            Province province = null;
            provinces  = new ArrayList<>();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = parser.getName();
                switch (eventType){

                    //开始解析某节点
                    case XmlPullParser.START_TAG:{   //开始标签
                        if("city".equals(nodeName)){ //省份
                            //获取属性
                            quName = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "quName");
                            pyName = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "pyName");
                            cityName = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "cityname");
                        }
                        break;
                    }

                    case  XmlPullParser.END_TAG:{   //结束标签
                        if("city".equals(nodeName)){
                            province = new Province();
                            province.setCityName(cityName);
                            province.setPyName(pyName);
                            province.setQuName(quName);
                            provinces.add(province);
                        }
                        break;
                    }

                }
                eventType = parser.next();  //下一个解析事件

            }

            return provinces;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
