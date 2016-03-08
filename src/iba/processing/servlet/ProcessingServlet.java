package iba.processing.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import iba.model.StudentModel;
import iba.reading.ReadData;

@WebServlet(asyncSupported = true, urlPatterns = { "/autocomplete" })
public class ProcessingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<StudentModel> studentList;

	public ProcessingServlet() {
		super();
		ReadData readList = new ReadData();
		readList.readFile();
		studentList = readList.getStudentList();

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		String targetId = request.getParameter("id");
		StringBuffer sb = new StringBuffer();
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

				Iterator it = studentList.iterator();
				System.out.println(targetId);
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
							|| student.getSecondName().toLowerCase().concat(" ")
									.concat(student.getPartronymic().toLowerCase()).contains(
											targetId)
							|| student.getFirstName().toLowerCase().concat(" ")
									.concat(student.getSecondName().toLowerCase()).concat(" ")
									.concat(student.getPartronymic().toLowerCase().concat(" ")
											.concat(student.getNumberGroup()))
									.contains(targetId)) {

						sb.append("<student>");
						sb.append("<firstName>" + student.getFirstName() + "</firstName>");
						sb.append("<lastName>" + student.getSecondName() + "</lastName>");
						sb.append("<partronymic>" + student.getPartronymic() + "</partronymic>");
						sb.append("<group>" + student.getNumberGroup() + "</group>");
						sb.append("</student>");
						namesAdded = true;
					}
				}

			}

			if (namesAdded)

			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write("<students>" + sb.toString() + "</students>");
				System.out.println(sb);
			} else

			{
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			}
		}
	}

}
