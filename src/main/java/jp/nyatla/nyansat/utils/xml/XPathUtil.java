package jp.nyatla.nyansat.utils.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathUtil{
	private XPath _inst;
	private Node _doc;
	public XPathUtil(Document i_doc){
		this._inst=XPathFactory.newInstance().newXPath();
		this._doc=i_doc;
	}
	public XPathUtil(Node item) {
		this._inst=XPathFactory.newInstance().newXPath();
		this._doc=item;
	}
	public NodeList select(String i_xpath){
        try {
            XPathExpression expr = this._inst.compile(i_xpath);
			return (NodeList)expr.evaluate(this._doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	public Element selectSingleElement(String i_xpath) throws XPathExpressionException{
		return (Element)this.select(i_xpath).item(0);
	}
	public XPathUtil getXPathUtil(String i_path)
	{
		try {
			return new XPathUtil(this.selectSingleElement(i_path));
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	public double getDouble(String i_path)
	{
		try {
			return Double.parseDouble(this.selectSingleElement(i_path).getTextContent());
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		} catch (DOMException e) {
			throw new RuntimeException();
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	public int getInt(String i_path)
	{
		try {
			return Integer.parseInt(this.selectSingleElement(i_path).getTextContent());
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		} catch (DOMException e) {
			throw new RuntimeException(e);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
	public String getString(String i_path)
	{
		try {
			return this.selectSingleElement(i_path).getTextContent();
		} catch (DOMException e) {
			throw new RuntimeException(e);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}		
	}
	
	
	
}