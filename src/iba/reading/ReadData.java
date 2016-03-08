package iba.reading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import iba.model.StudentModel;

public class ReadData {
	private List<StudentModel> studentList;

	public ReadData() {
		studentList = new ArrayList<StudentModel>();
	}

	public void readFile() {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(ResourceLoader.loadResource("input.txt")));
		String inputData = null;
		try {
			while ((inputData = reader.readLine()) != null) {
				StudentModel newStudent = new StudentModel();
				newStudent.setFirstName(inputData);
				if ((inputData = reader.readLine()) != null)
					newStudent.setSecondName(inputData);
				if ((inputData = reader.readLine()) != null)
					newStudent.setPartronymic(inputData);
				if ((inputData = reader.readLine()) != null)
					newStudent.setNumberGroup(inputData);
				studentList.add(newStudent);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<StudentModel> getStudentList() {
		return studentList;
	}

}
