package iba.reading;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import iba.model.StudentModel;

public class SAXParserStudent extends DefaultHandler {
	private StudentModel student;
	private List<StudentModel> studentList;
	private StringBuilder text;

	public SAXParserStudent() {
		studentList = new ArrayList<StudentModel>();
	}

	public List<StudentModel> getStudentList() {
		return studentList;
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (qName.equals("student")) {
			student = new StudentModel();
		}
		text = new StringBuilder();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		text.append(ch, start, length);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (qName.equals("firstName")) {
			student.setFirstName(text.toString());
		}
		if (qName.equals("lastName")) {
			student.setSecondName(text.toString());
		}
		if (qName.equals("partronymic")) {
			student.setPartronymic(text.toString());
		}
		if (qName.equals("group")) {
			student.setNumberGroup(text.toString());
		}
		if (qName.equals("student")) {
			studentList.add(student);
		}
	}

	@Override
	public void endDocument() {
	}
}
