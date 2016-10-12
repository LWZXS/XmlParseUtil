package com.csg.xmlprase.dom4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * xml格式解析,基于dom4j
 * 解析为相应类型对象以及List
 * 
 * 可以以xml文本以及.xml文件形式传入
 * 
 * 依赖dom4j包
 * 
 * @author yiva
 *
 */
public class XmlParse {

	private String xmlString;
	private File xmlFile;
	private Document document = null;
	private Element root = null;

	public XmlParse(String xml) {
		this.xmlString = xml;
		try {
			document = DocumentHelper.parseText(xml);
			root = document.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			System.err.println("This Xml layout is not standard.\n"
					+ e.getMessage());
		}
	}
	
	public XmlParse(File file){
		this.xmlFile = file;
		SAXReader reader = new SAXReader();
	    try {
			document = reader.read(file);
			root = document.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			System.err.println("This Xml layout is not standard.\n"
					+ e.getMessage());
		}
	}

	
	/**
	 * 将xml转化为相关类型的List
	 * 
	 * @param clazz list类型
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> parseToListByXML(Class<T> clazz) throws Exception {
		if(root == null){
			return null;
		}
		List<T> arr = new ArrayList<>();
		Iterator<Element> it = root.elementIterator();
		while (it.hasNext()) {
			// 获取某个子节点对象
			T o = clazz.newInstance();
			Element e = it.next();
			o = parseToClazzByXML(e, clazz);
			arr.add(o);
		}
		return arr;
	}
	
	private <T> T parseToClazzByXML(Element node, Class<T> clazz)
			throws DocumentException {
		if (node == null) {
			return null;
		}
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listNodes(node, obj);
		return obj;
	}

	/**
	 * 将xml按相关类型存入对象
	 * 
	 * @param clazz 对象类型
	 * @return
	 * @throws DocumentException
	 */
	public <T> T parseToClazzByXML(Class<T> clazz)
			throws DocumentException {
		if(root == null){
			return null;
		}
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listNodes(root, obj);
		return obj;
	}

	/**
	 * 把document对象写入新的文件
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void writer(Document document) throws Exception {
		// 紧凑的格式
		// OutputFormat format = OutputFormat.createCompactFormat();
		// 排版缩进的格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// 创建XMLWriter对象,指定了写出文件及编码格式
		// XMLWriter writer = new XMLWriter(new FileWriter(new
		// File("src//a.xml")),format);
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src//a.xml")), "UTF-8"), format);
		// 写入
		writer.write(document);
		// 立即写入
		writer.flush();
		// 关闭操作
		writer.close();
	}

	/**
	 * 遍历当前节点元素下面的所有(元素的)子节点
	 * 
	 * @param node
	 */
	public void listNodes(Element node, Object obj) {

		// 获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		// 遍历属性节点
		for (Attribute attr : list) {
			try {
				Field attrField = obj.getClass().getDeclaredField(
						attr.getName());
				attrField.setAccessible(true);
				attrField.set(obj, attr.getValue());
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				//System.err.println(ex);
			}
		}

		String nodeName = node.getName();
		try {
			Field field = obj.getClass().getDeclaredField(nodeName);
			field.setAccessible(true);
			field.set(obj, node.getData());
		} catch (Exception ex) {
			//System.err.println(ex);
		}

		Iterator<Element> it = node.elementIterator();
		while (it.hasNext()) {
			// 获取某个子节点对象
			Element e = it.next();
			// 对子节点进行遍历
			listNodes(e, obj);
		}

	}

	/**
	 * 介绍Element中的element方法和elements方法的使用
	 * 
	 * @param node
	 */
	public void elementMethod(Element node) {
		// 获取node节点中，子节点的元素名称为西游记的元素节点。
		Element e = node.element("西游记");
		// 获取西游记元素节点中，子节点为作者的元素节点(可以看到只能获取第一个作者元素节点)
		Element author = e.element("作者");

		System.out.println(e.getName() + "----" + author.getText());

		// 获取西游记这个元素节点 中，所有子节点名称为作者元素的节点 。

		List<Element> authors = e.elements("作者");
		for (Element aut : authors) {
			System.out.println(aut.getText());
		}

		// 获取西游记这个元素节点 所有元素的子节点。
		List<Element> elements = e.elements();

		for (Element el : elements) {
			System.out.println(el.getText());
		}

	}
}
