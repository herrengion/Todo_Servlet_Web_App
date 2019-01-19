package jsonData;

import data.TodoList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.todo.DueDate;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class JsonTodos {

    private String name;
    private String password;
    private JSONArray jsonArr;
    private LinkedList<TodoList.Todo> todoLinkedList;



    public JsonTodos(LinkedList<TodoList.Todo> todoLinkedList)
    {
        this.todoLinkedList = todoLinkedList;
    }

    private  void convertTodoListToJsonArr()
    {
        Iterator<TodoList.Todo> iterator = todoLinkedList.iterator();
        jsonArr = new JSONArray();
        while (iterator.hasNext()) {
            TodoList.Todo thisTodo = iterator.next();
//            if(thisTodo.getCategory() == reqCategory)
            jsonArr.add(generateJsonObjFromTodo(thisTodo));
        }
    }

    private JSONObject generateJsonObjFromTodo(TodoList.Todo todo)
    {
        JSONObject jsonTodoObj = new JSONObject();
        jsonTodoObj.put("id", "foo");
        jsonTodoObj.put("title", new String(todo.getTitle()));
        jsonTodoObj.put("category", new String(todo.getCategory()));
        DueDate dueDate = new DueDate(todo.getDueDate());
        jsonTodoObj.put("dueDate", new String(dueDate.toString()));
        jsonTodoObj.put("important", new Boolean(todo.isImportant()));
        jsonTodoObj.put("completed", new Boolean(todo.isCompleted()));
        System.out.println("This todo ID:"+todo.getId());
        return jsonTodoObj;
    }

    public JSONArray getJsonArr()
    {
        convertTodoListToJsonArr();
        return jsonArr;
    }
}
