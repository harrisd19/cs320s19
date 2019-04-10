/*
package edu.example;


import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("students")
public class StudentsResource {

    static StudentsDataSource ds = StudentsDataSource.getInstance();
    static Gson gson;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStudents() {
        String rawJson = gson.toJson(ds.getStudents());
        System.out.println(rawJson);
        return rawJson;
    }
}
*/

package edu.example;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("students")
public class StudentsResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudents(@QueryParam("id") int id) {
        StudentsDataSource ds = StudentsDataSource.getInstance();
        String rawJson = "";
        Gson gson = new Gson();
        if (id == 0) {
            Collection<Student> students = ds.getStudents();
            rawJson = gson.toJson(students);
        }
        else {
            Student student = ds.getStudentById(id);
            rawJson = gson.toJson(student);
        }
        return rawJson;
    }
}
