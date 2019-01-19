package org.todo;

import jsonData.JsonUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.todo.auxiliary.LoginRoutine;
import users.UserList;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

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
            e.getStackTrace();
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
                    if( servletInitMap.get("contentTypeReq").equals("application/json")) {
                        UserList userDB = new UserList();
                        UserList.User newXMLUser = new UserList.User();
                        LoginRoutine loginRoutine = new LoginRoutine(request, response, getServletContext());
                        newXMLUser.setUsername((String) servletInitMap.get("userName"));
                        newXMLUser.setPassword((String) servletInitMap.get("pw"));
                        loginRoutine.addUserToXml(newXMLUser, userDB);
                        PrintWriter out = response.getWriter();
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.setStatus(SC_CREATED);
                    }
                    else
                    {
                        response.setStatus(SC_UNSUPPORTED_MEDIA_TYPE);
                    }
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
        initialParameterMap.put("contentTypeReq",request.getHeader("Content-Type"));
        initialParameterMap.put("acceptTypeReq",request.getHeader("Accept"));
        initialParameterMap.put("acceptEncodingReq",response.getHeader("Accept-Encoding"));
        //Extract user name
        if(initialParameterMap.get("switchCase").equals("/users"))
        {
            String body = request.getReader().lines().collect(Collectors.joining());
            JsonUser requestUser = new JsonUser(body);
            System.out.println("Name: "+ requestUser.getName() + "PW: " + requestUser.getPassword());
            initialParameterMap.put("userName", requestUser.getName());
            initialParameterMap.put("pw", requestUser.getPassword());
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
}
