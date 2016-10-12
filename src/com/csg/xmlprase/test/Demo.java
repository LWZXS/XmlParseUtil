package com.csg.xmlprase.test;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;

import com.csg.xmlprase.dom4j.XmlParse;

public class Demo {
	
	public static void main(String[] args) {
//		String xml = "<Approve><name>li</name><firstApprove>first</firstApprove><secondApprove>second</secondApprove></Approve>";
//		XmlParse parse = new XmlParse(xml);
		File file = new File("src/abroadData.xml");
		XmlParse parse = new XmlParse(file);
		AbroadApprove app = new AbroadApprove();
		try {
			List<AbroadApprove> arr = (List<AbroadApprove>) parse.parseToListByXML(Class.forName("com.csg.xmlprase.test.AbroadApprove"));
			for(AbroadApprove item : arr){
				System.out.println(item.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
