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
 * xml��ʽ����,����dom4j
 * ����Ϊ��Ӧ���Ͷ����Լ�List
 * 
 * ������xml�ı��Լ�.xml�ļ���ʽ����
 * 
 * ����dom4j��
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
	 * ��xmlת��Ϊ������͵�List
	 * 
	 * @param clazz list����
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
			// ��ȡĳ���ӽڵ����
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
	 * ��xml��������ʹ������
	 * 
	 * @param clazz ��������
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
	 * ��document����д���µ��ļ�
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void writer(Document document) throws Exception {
		// ���յĸ�ʽ
		// OutputFormat format = OutputFormat.createCompactFormat();
		// �Ű������ĸ�ʽ
		OutputFormat format = OutputFormat.createPrettyPrint();
		// ���ñ���
		format.setEncoding("UTF-8");
		// ����XMLWriter����,ָ����д���ļ��������ʽ
		// XMLWriter writer = new XMLWriter(new FileWriter(new
		// File("src//a.xml")),format);
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(new File("src//a.xml")), "UTF-8"), format);
		// д��
		writer.write(document);
		// ����д��
		writer.flush();
		// �رղ���
		writer.close();
	}

	/**
	 * ������ǰ�ڵ�Ԫ�����������(Ԫ�ص�)�ӽڵ�
	 * 
	 * @param node
	 */
	public void listNodes(Element node, Object obj) {

		// ��ȡ��ǰ�ڵ���������Խڵ�
		List<Attribute> list = node.attributes();
		// �������Խڵ�
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
			// ��ȡĳ���ӽڵ����
			Element e = it.next();
			// ���ӽڵ���б���
			listNodes(e, obj);
		}

	}

	/**
	 * ����Element�е�element������elements������ʹ��
	 * 
	 * @param node
	 */
	public void elementMethod(Element node) {
		// ��ȡnode�ڵ��У��ӽڵ��Ԫ������Ϊ���μǵ�Ԫ�ؽڵ㡣
		Element e = node.element("���μ�");
		// ��ȡ���μ�Ԫ�ؽڵ��У��ӽڵ�Ϊ���ߵ�Ԫ�ؽڵ�(���Կ���ֻ�ܻ�ȡ��һ������Ԫ�ؽڵ�)
		Element author = e.element("����");

		System.out.println(e.getName() + "----" + author.getText());

		// ��ȡ���μ����Ԫ�ؽڵ� �У������ӽڵ�����Ϊ����Ԫ�صĽڵ� ��

		List<Element> authors = e.elements("����");
		for (Element aut : authors) {
			System.out.println(aut.getText());
		}

		// ��ȡ���μ����Ԫ�ؽڵ� ����Ԫ�ص��ӽڵ㡣
		List<Element> elements = e.elements();

		for (Element el : elements) {
			System.out.println(el.getText());
		}

	}
}
