package edu.example;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class StudentsDataSource {

    static final String DATA_FILE_NAME = "students.csv";
    static StudentsDataSource instance = null;
    Map<Integer, Student> studentById;

    private StudentsDataSource() {
        studentById = new HashMap<>();
        try {
            Scanner sc = new Scanner(new FileInputStream("resources/" + DATA_FILE_NAME));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() < 4)
                    continue;
                String data[] = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String email = data[2];
                boolean honors = Boolean.parseBoolean(data[3]);
                Student student = new Student(id, name, email);
                student.setHonors(honors);
                studentById.put(id, student);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static StudentsDataSource getInstance() {
        if (instance == null)
            instance = new StudentsDataSource();
        return instance;
    }

    public Collection<Student> getStudents() {
        return studentById.values();
    }

    public Student getStudentById(int id) {
        return studentById.get(id);
    }

    @Override
    public String toString() {
        return "StudentsDataSource{" +
                "students=" + Arrays.toString(getStudents().toArray()) +
                '}';
    }

    public static void main(String[] args) {
        StudentsDataSource ds = StudentsDataSource.getInstance();
        System.out.println(ds);
        Gson gson = new Gson();
        String rawJson = gson.toJson(ds.getStudents());
        System.out.println(rawJson);
        rawJson = gson.toJson(ds.getStudentById(1));
        System.out.println(rawJson);
    }
}
