package org.todo;

import jsonData.Users;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.todo.auxiliary.*;
import users.UserList;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet({"/users","/todos","/todos/*","/categories"})
public class RestServlet extends HttpServlet{

    //------------------------------------------------------------------------------------------------------------------
    //HTTP Methods:
    //------------------------------------------------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessge = "No error";
        Map servletInitMap;
        try
        {
            servletInitMap = initialRestApiValues(request, response);
        }
        catch (Exception e)
        {
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }

        System.out.println("Got request on REST API");
        System.out.println("SwitchCase: " + servletInitMap.get("switchCase"));
        System.out.println("servlet cont: " + servletInitMap.get("servletContext"));
        System.out.println("contentTypeResp: " + servletInitMap.get("contentTypeResp"));
        System.out.println("acceptTypeReq: " + servletInitMap.get("acceptTypeReq"));
        System.out.println("acceptEncodingReq: " + servletInitMap.get("acceptEncodingReq"));
        System.out.println("userName: " + servletInitMap.get("userName"));
        System.out.println("password: " + servletInitMap.get("pw"));
        try
        {
            switch ((String)servletInitMap.get("switchCase")) {

                case "/users":
                    UserList userDB = new UserList();
                    UserList.User newXMLUser = new UserList.User();
                    LoginRoutine loginRoutine = new LoginRoutine(request, response, getServletContext());
                    newXMLUser.setUsername((String) servletInitMap.get("userName"));
                    newXMLUser.setPassword((String) servletInitMap.get("pw"));
                    loginRoutine.addUserToXml(newXMLUser ,userDB);
                    System.out.println("users get");

                    break;

                case "/todos":
                    System.out.println("todos get");
                    break;

                default:
                    System.err.println("None matching case");
                    errorMessge = "Switch passes default and no exception occurred!";
                    break;
            }
        }
        catch (LoginException e)
        {
            System.err.println("Login exception: "+ e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/text");
        response.setCharacterEncoding("UTF-8");
        out.print(" test for REST API");

    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------
    private Map<String, String> initialRestApiValues(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> initialParameterMap= new HashMap<String, String>();

        initialParameterMap.put("switchCase",request.getServletPath());
        initialParameterMap.put("servletContext",getServletContext().getContextPath());
        initialParameterMap.put("contentTypeResp",response.getHeader("Content-Type"));
        initialParameterMap.put("acceptTypeReq",request.getHeader("Accept"));
        initialParameterMap.put("acceptEncodingReq",response.getHeader("Accept-Encoding"));
        //Extract user name
        if(initialParameterMap.get("switchCase").equals("/users"))
        {
            String jsonString = "{\"name\":1,\"password\":\"Gupta\"}}";
            jaxbJsonStringToObject(jsonString);
            initialParameterMap.put("userName", request.getParameter("name"));
            initialParameterMap.put("pw", request.getParameter("pw"));
        }
        else
        {
            String authHeader = request.getHeader("Authorization");
            String scheme = authHeader.split(" ")[0];
            if (!scheme.equals("Basic")) throw new Exception();
            String credentials = authHeader.split(" ")[1];
            credentials = new String(DatatypeConverter.parseBase64Binary(credentials), ISO_8859_1);
            String username = credentials.split(":")[0];
            String pw = credentials.split(":")[1];
            initialParameterMap.put("userName", username);
            initialParameterMap.put("pw", "");
        }
        return initialParameterMap;
    }
    private static void jaxbJsonStringToObject(String jsonString)
    {
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(Users.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Set JSON type
            jaxbUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
            jaxbUnmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);

            Users employee = (Users) jaxbUnmarshaller.unmarshal(new StringReader(jsonString));

            System.out.println(employee);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
    }

}
