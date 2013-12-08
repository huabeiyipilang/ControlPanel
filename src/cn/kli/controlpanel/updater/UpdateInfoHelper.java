package cn.kli.controlpanel.updater;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class UpdateInfoHelper {
	
	private final static String UPDATE_ELEMENT = "app";
	private final static String VALUE_NAME = "name";
	private final static String VALUE_PACKAGE = "package";
	private final static String VALUE_VERSION_CODE = "version_code";
	private final static String VALUE_VERSION_NAME = "version_name";
	private final static String VALUE_DESCRIPTION = "description";
	private final static String VALUE_URL = "url";
	private final static String VALUE_SIZE = "size";
	private final static String VALUE_MD5 = "md5";
	
	private static MyLog klilog = new MyLog(UpdateInfoHelper.class);
	
	private List<UpdateInfo> mUpdateList;
	public UpdateInfoHelper(String xml){
		parse(xml);
	}
	
	public UpdateInfoHelper(InputStream is){
		parse(is);
	}
	
	public List<UpdateInfo> getUpdateList(){
		return mUpdateList;
	}

	private boolean parse(String xml){
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		return parse(stream);
	}
	
	private boolean parse(InputStream is){
		boolean res = false;
		SAXParserFactory saxParser = SAXParserFactory.newInstance();
		try {
			SAXParser sp = saxParser.newSAXParser();
			XMLReader reader = sp.getXMLReader();
			XmlHandler handler = new XmlHandler(); 
			reader.setContentHandler(handler);
			reader.parse(new InputSource(is));
			mUpdateList = handler.update_list;
			for(UpdateInfo info:mUpdateList){
				info.dump();
			}
			res = true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private class XmlHandler extends DefaultHandler{
		private List<UpdateInfo> update_list = new ArrayList<UpdateInfo>();
		private UpdateInfo info;
		private String tmp;
		
		@Override
		public void startElement(String uri, String localName,
				String qName, Attributes attributes)
				throws SAXException {
			if(UPDATE_ELEMENT.equals(localName)){
				info = new UpdateInfo();
			}
			tmp = localName;
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String value = new String(ch, start, length);
			if (info != null) {
				if (VALUE_NAME.equals(tmp)) {
					info.app_name = value;
				} else if (VALUE_PACKAGE.equals(tmp)) {
					info.pkg_name = value;
				} else if (VALUE_VERSION_CODE.equals(tmp)) {
					info.version_code = value;
				} else if (VALUE_VERSION_NAME.equals(tmp)) {
					info.version_name = value;
				} else if (VALUE_DESCRIPTION.equals(tmp)) {
					info.description = value;
				} else if (VALUE_URL.equals(tmp)) {
					info.url = value;
				} else if (VALUE_SIZE.equals(tmp)) {
					info.size = value;
				} else if (VALUE_MD5.equals(tmp)) {
					info.md5 = value;
				}
			}

		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(UPDATE_ELEMENT.equals(localName)){
				update_list.add(info);
			}
			tmp = null;
		}

	}
}
