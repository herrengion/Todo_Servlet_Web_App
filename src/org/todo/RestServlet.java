package org.todo;

import data.TodoList;
import data.Todos;
import jsonData.JsonTodos;
import jsonData.JsonUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.todo.auxiliary.LoginRoutine;
import org.todo.auxiliary_REST.AuthenticationFilter;
import users.UserList;

import javax.security.auth.login.LoginException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.*;

@WebServlet({"/users","/todos","/todos/*","/categories"})
public class RestServlet extends HttpServlet{
    private static final String VALID_RESPONSE_TYPE = "application/json";
    private static final String VALID_RESQUEST_TYPE = "application/json";

    public static final String DATA_PATH_WEB_INF = "/WEB-INF";
    public static final String DATA_PATH_WEB_INF_DATA = DATA_PATH_WEB_INF + "/data";
    public static final String DATA_PATH_WEB_INF_USER_DATA = DATA_PATH_WEB_INF_DATA + "/UserData";
    //------------------------------------------------------------------------------------------------------------------
    //HTTP Methods:
    //------------------------------------------------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessge = "No error";
        String contextPath = getServletContext().getRealPath("/");
        Map servletInitMap;
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        FilterChain chain = new FilterChain() {
            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {

            }
        };
        authenticationFilter.doFilter(request, response, chain);
        try
        {
            servletInitMap = initialRestApiValues(request, response);
            switch ((String)servletInitMap.get("switchCase"))
            {
                case "/todos":
                    if( servletInitMap.get("acceptTypeReq").equals(VALID_RESPONSE_TYPE)) {
                        String category = request.getParameter("category");
                        String idStr = request.getParameter("id");
                        String ouputString;
                        JSONArray jsonArray = getToDoJsonFromUser(servletInitMap, contextPath, category, idStr);
                        if(idStr != null) {
                            if(Long.parseLong(idStr,10) >= 0 &&
                                    jsonArray.size() > 0)
                            {
                                JSONObject jsonTodo = (JSONObject) jsonArray.get(0);
                                prepareSendResponse(response, jsonTodo.toJSONString());
                            }
                            else
                            {
                                throw new IOException("Invalid id or todo does not exist");
                            }
                        }
                        else
                        {
                            prepareSendResponse(response, jsonArray.toJSONString());
                        }
                        response.setStatus(SC_OK);
                    }
                    else
                    {
                        throw new InvalidPropertiesFormatException("Unsupported content type for Request");
                    }
                    System.out.println("todos get");
                    break;

                default:
                    System.err.println("None matching case");
                    errorMessge = "Switch passes default and no exception occurred!";
                    throw new IllegalArgumentException(errorMessge);
            }
        }
        catch (VerifyError | IllegalArgumentException e)
        {
            System.err.println("Login exception: "+ e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        catch (InvalidPropertiesFormatException e)
        {
            System.err.println(e.getClass());
            System.err.println("Accept type exception: "+ e.getMessage());
            response.setStatus(SC_NOT_ACCEPTABLE);
            return;
        }
        catch (IOException e)
        {
            System.err.println("Todos exception: "+ e.getMessage());
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        catch (Exception e)
        {
            e.getStackTrace();
            System.err.println("Exception: "+ e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessge = "No error";
        Map servletInitMap;
        try
        {
            servletInitMap = initialRestApiValues(request, response);

            AuthenticationFilter authenticationFilter = new AuthenticationFilter();
            FilterChain chain = new FilterChain() {
                @Override
                public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {

                }
            };
            authenticationFilter.doFilter(request, response, chain);

            System.out.println("Got request on REST API");
            System.out.println("SwitchCase: " + servletInitMap.get("switchCase"));
            System.out.println("servlet cont: " + servletInitMap.get("servletContext"));
            System.out.println("contentTypeResp: " + servletInitMap.get("contentTypeResp"));
            System.out.println("acceptTypeReq: " + servletInitMap.get("acceptTypeReq"));
            System.out.println("acceptEncodingReq: " + servletInitMap.get("acceptEncodingReq"));
            System.out.println("userName: " + servletInitMap.get("userName"));
            System.out.println("password: " + servletInitMap.get("pw"));

            switch ((String)servletInitMap.get("switchCase")) {

                case "/users":
                    if( servletInitMap.get("contentTypeReq").equals(VALID_RESQUEST_TYPE)) {
                        UserList userDB = new UserList();
                        UserList.User newXMLUser = new UserList.User();
                        LoginRoutine loginRoutine = new LoginRoutine(request, response, getServletContext().getRealPath("/"));
                        newXMLUser.setUsername((String) servletInitMap.get("userName"));
                        newXMLUser.setPassword((String) servletInitMap.get("pw"));
                        loginRoutine.addUserToXml(newXMLUser, userDB);
                        response.setStatus(SC_CREATED);
                    }
                    else
                    {
                        throw new InvalidPropertiesFormatException("Unsupported content type for Request");
                    }
                    break;

                case "/todos":
                    System.out.println("todos post");
                    if( servletInitMap.get("acceptTypeReq") == null ||
                            servletInitMap.get("contentTypeReq") == null)
                    {
                        throw new InvalidPropertiesFormatException("Missing parameter in header for content type");
                    }
                    if( servletInitMap.get("acceptTypeReq").equals(VALID_RESQUEST_TYPE) &&
                            servletInitMap.get("contentTypeReq").equals(VALID_RESPONSE_TYPE))
                    {
                        TodoUser todoUser = initTodoUser(servletInitMap, getServletContext().getRealPath("/"));
                        Long highestTodoId = todoUser.getHighestTodoId();
                        String body = request.getReader().lines().collect(Collectors.joining());
                        TodoList.Todo newTodo = convertJsonToTodo(body);
                        newTodo.setId((highestTodoId+1));
                        todoUser.addTodo(newTodo);
                        todoUser.updateTodo();
                        JSONObject jsonReponseObj = new JSONObject();
                        jsonReponseObj.put("id", newTodo.getId());
                        String outputString = jsonReponseObj.toJSONString();
                        prepareSendResponse(response, outputString);
                        response.setStatus(SC_CREATED);
                    }
                    else
                    {
                        throw new InvalidPropertiesFormatException("Unsupported content type for Request or Response");
                    }
                    break;

                default:
                    System.err.println("None matching case");
                    errorMessge = "Switch passes default and no exception occurred!";
                    throw new IllegalArgumentException(errorMessge);
            }
        }
        catch (LoginException e)
        {
            System.err.println("Login exception: "+ e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }
        catch (VerifyError | IllegalArgumentException e)
        {
            System.err.println("Login exception: "+ e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        catch (InvalidPropertiesFormatException e)
        {
            System.err.println(e.getClass());
            System.err.println("Login exception: "+ e.getMessage());
            response.setStatus(SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        catch (Exception e)
        {

            e.getStackTrace();
            System.err.println("Exception: "+ e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessge = "No error";
        String contextPath = getServletContext().getRealPath("/");
        Map servletInitMap;
        try
        {
            servletInitMap = initialRestApiValues(request, response);
            switch ((String)servletInitMap.get("switchCase"))
            {
                case "/todos":
                    if( servletInitMap.get("contentTypeReq").equals(VALID_RESQUEST_TYPE))
                    {
                        String idStr = request.getParameter("id");
                        Long id;
                        if(idStr != null) {
                            id = Long.parseLong(idStr, 10);
                        }
                        else
                        {
                            throw new IOException("Missing id parameter to update todo");
                        }
                        TodoUser todoUser = initTodoUser(servletInitMap, contextPath);
                        String body = request.getReader().lines().collect(Collectors.joining());
                        TodoList.Todo newTodo = convertJsonToTodo(body);
                        todoUser.updateTodoTitle(id, newTodo.getTitle());
                        todoUser.updateTodoCategory(id, newTodo.getCategory());
                        todoUser.updateTodoDueDate(id, newTodo.getDueDate().toString());
                        todoUser.updateTodoImportant(id, newTodo.isImportant());
                        todoUser.updateTodoCompleted(id, newTodo.isCompleted());
                        todoUser.updateTodo();
                        response.setStatus(SC_NO_CONTENT);
                    }
                    else
                    {
                        throw new InvalidPropertiesFormatException("Unsupported content type for Request");
                    }
                    System.out.println("todos put");
                    break;

                default:
                    System.err.println("None matching case");
                    errorMessge = "Switch passes default and no exception occurred!";
                    throw new IllegalArgumentException(errorMessge);
            }
        }
        catch (InvalidPropertiesFormatException e)
        {
            System.err.println("Put exception: "+ e.getMessage());
            response.setStatus(SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        catch (IOException e)
        {
            System.err.println("Todos exception: "+ e.getMessage());
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        catch (Exception e)
        {
            e.getStackTrace();
            System.err.println("Exception: "+ e.getMessage());
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String errorMessge = "No error";
        String contextPath = getServletContext().getRealPath("/");
        Map servletInitMap;
        try
        {
            servletInitMap = initialRestApiValues(request, response);
            switch ((String)servletInitMap.get("switchCase"))
            {
                case "/todos":

                    String idStr = request.getParameter("id");
                    Long id;
                    if(idStr != null) {
                        id = Long.parseLong(idStr, 10);
                    }
                    else
                    {
                        throw new IOException("Missing id parameter to update todo");
                    }
                    TodoUser todoUser = initTodoUser(servletInitMap, contextPath);
                    if(!todoUser.deleteTodoEntry(id))
                    {
                        errorMessge = "Requested todo with ID " + id + " does not exist";
                        throw new IOException(errorMessge);
                    }
                    todoUser.updateTodo();
                    response.setStatus(SC_NO_CONTENT);

                    System.out.println("todos delete");
                    break;

                default:
                    System.err.println("None matching case");
                    errorMessge = "Switch passes default and no exception occurred!";
                    throw new IllegalArgumentException(errorMessge);
            }
        }
        catch (IOException e)
        {
            System.err.println("Todos exception: "+ e.getMessage());
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        catch (Exception e)
        {
            e.getStackTrace();
            System.err.println("Exception: "+ e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            return;
        }
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

        if(initialParameterMap.get("switchCase").equals("/users") &&
                request.getMethod().equals("POST"))
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
            initialParameterMap.put("pw", pw);
        }
        return initialParameterMap;
    }

    private TodoUser initTodoUser(Map servletInitMap, String contextPath)
    {
        String userName = (String) servletInitMap.get("userName");
        String pw = (String) servletInitMap.get("pw");
        File userToDoXmlFile = new File(contextPath +
                DATA_PATH_WEB_INF_USER_DATA +
                "/" + userName +
                "/ToDo_list_" + userName + ".xml");
        File xmlSchemaFile = new File(contextPath + DATA_PATH_WEB_INF_DATA + "/ToDo.xsd");
        TodoUser todoUser = new TodoUser(userName, pw);
        try
        {
            todoUser.setUserTodoList(userToDoXmlFile, xmlSchemaFile);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Read user data not possible as file does not exist");
        }
        return todoUser;
    }

    private void prepareSendResponse(HttpServletResponse response, String outputString) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType(VALID_RESPONSE_TYPE);
        response.setCharacterEncoding("UTF-8");
        out.print(outputString);
    }
    private TodoList.Todo convertJsonToTodo(String jsonTodoBody)
    {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj;
        String title;
        String category;
        String dueDate;
        Boolean important;
        Boolean completed;
        Long id;
        try
        {
            jsonObj = (JSONObject) parser.parse(jsonTodoBody);
            title = (String) jsonObj.get("title");
            category = (String) jsonObj.get("category");
            dueDate = (String) jsonObj.get("dueDate");
            important = (Boolean) jsonObj.get("important");
            completed = (Boolean) jsonObj.get("completed");
            id = (Long) jsonObj.get("id");
        }
        catch (ParseException e)
        {
            throw new VerifyError("Todo data parsing not possible");
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Todo data type is invalid");
        }

        TodoList.Todo todo = new TodoList.Todo();
        todo.setTitle(title);
        todo.setCategory(category);
        DueDate dueDateObj = new DueDate(dueDate);
        todo.setDueDate(dueDateObj.getXmlGregorianCalendar());
        todo.setImportant(important);
        todo.setCompleted(completed);
        if(id != null)
        {
            todo.setId(id);
        }
        return todo;
    }

    private JSONArray getToDoJsonFromUser(Map servletInitMap, String contextPath, String category, String id) throws IOException {
        String userName = (String) servletInitMap.get("userName");
        TodoUser todoUser = initTodoUser(servletInitMap, contextPath);
        LinkedList<TodoList.Todo> userTodoList;
        try
        {
            userTodoList = todoUser.getUserTodoList();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Read user data not possible as file does not exist");
        }
        try
        {
            JsonTodos jsonTodos = new JsonTodos(userTodoList);
            JSONArray jsonArray;
            if(id == null) {
                jsonArray = jsonTodos.getJsonArrOfCategory(category);
            }
            else
            {
                jsonArray = jsonTodos.getJsonArrOfTodoWithId(Long.parseLong(id,10));
            }
            return jsonArray;
        }
        catch (Exception e)
        {
            String errorMsg = "Could not read the todos from user"+userName;
            throw new IOException(errorMsg);
        }

    }
}
