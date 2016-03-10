package iba.processing.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import iba.model.StudentModel;
import iba.reading.ResourceLoader;
import iba.reading.SAXParserStudent;

@WebServlet(asyncSupported = true, urlPatterns = { "/autocomplete" })
public class ProcessingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<StudentModel> studentList;

	public ProcessingServlet() {
		super();
	}

	public void init() {
		studentList = new ArrayList<StudentModel>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		}
		SAXParserStudent saxParserStudent = new SAXParserStudent();

		try {
			parser.parse(ResourceLoader.loadResource("input.xml"), saxParserStudent);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		studentList = saxParserStudent.getStudentList();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		String targetId = request.getParameter("id");
		StringBuffer sbJSON = new StringBuffer();
		boolean namesAdded = false;
		if (targetId != null) {
			targetId = targetId.trim().toLowerCase();
		} else {
			targetId = "";
			namesAdded = false;
		}

		if (action.equals("complete")) {

			// check if user sent empty string
			if (!targetId.equals("")) {

				Iterator<StudentModel> it = studentList.iterator();
				while (it.hasNext()) {
					StudentModel student = (StudentModel) it.next();
					if ( // targetId matches group
					student.getNumberGroup().contains(targetId) ||
							// targetId matches first name
							student.getFirstName().toLowerCase().contains(targetId) ||
							// targetId matches last name
							student.getSecondName().toLowerCase().contains(targetId) ||
							// targetId matches partronymic
							student.getPartronymic().toLowerCase().contains(targetId) ||
							// targetId matches full name
							student.getFirstName().toLowerCase().concat(" ")
									.concat(student.getSecondName().toLowerCase()).concat(" ")
									.concat(student.getPartronymic().toLowerCase()).contains(targetId)
									// below targetId matches name and group
							|| student.getFirstName().toLowerCase().concat(" ").concat(student.getNumberGroup())
									.contains(targetId)
							|| student.getSecondName().toLowerCase().concat(" ").concat(student.getNumberGroup())
									.contains(targetId)
							|| student.getFirstName().toLowerCase().concat(" ")
									.concat(student.getSecondName().toLowerCase()).concat(" ")
									.concat(student.getNumberGroup()).contains(
											targetId)
							|| student.getFirstName().toLowerCase().concat(" ")
									.concat(student.getSecondName().toLowerCase()).concat(" ")
									.concat(student.getPartronymic().toLowerCase().concat(" ")
											.concat(student.getNumberGroup()))
									.contains(targetId)) {

						if (sbJSON.length() > 0) {
							sbJSON.append(",");
						}
						sbJSON.append("{");
						sbJSON.append("\"firstName\": \"" + student.getFirstName() + "\",");
						sbJSON.append("\"lastName\": \"" + student.getSecondName() + "\",");
						sbJSON.append("\"partronymic\": \"" + student.getPartronymic() + "\",");
						sbJSON.append("\"group\": \"" + student.getNumberGroup() + "\"");
						sbJSON.append("}");
						namesAdded = true;
					}
				}

			}

			if (namesAdded)

			{
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write("{ \"students\": [ " + sbJSON.toString() + "]}");

			} else

			{
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
		}
	}

}
