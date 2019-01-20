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

    private boolean isIdMatchingTodo(Long id, TodoList.Todo todo)
    {
        if(id.equals(todo.getId()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isCategoryMatchingTodo(String category, TodoList.Todo todo)
    {
        if(category.equals(todo.getCategory()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private  void convertTodoListToJsonArr(String category, Long id)
    {
        Iterator<TodoList.Todo> iterator = todoLinkedList.iterator();
        jsonArr = new JSONArray();
        while (iterator.hasNext()) {
            TodoList.Todo thisTodo = iterator.next();
            if(category == null && id == null) {
                jsonArr.add(generateJsonObjFromTodo(thisTodo));
            }
            else if(id != null && isIdMatchingTodo(id, thisTodo))
            {
                jsonArr.add(generateJsonObjFromTodo(thisTodo));
            }
            else if(category != null &&
                    (isCategoryMatchingTodo(category, thisTodo) ||
                            category.equals("")))
            {
                jsonArr.add(generateJsonObjFromTodo(thisTodo));
            }
        }
    }

    private JSONObject generateJsonObjFromTodo(TodoList.Todo todo)
    {
        JSONObject jsonTodoObj = new JSONObject();
        jsonTodoObj.put("id", new Long(todo.getId()));
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
        /* add all todos in the array*/
        convertTodoListToJsonArr("", null);
        return jsonArr;
    }

    public JSONArray getJsonArrOfCategory(String category)
    {
        /* add all todos in the array*/
        convertTodoListToJsonArr(category, null);
        return jsonArr;
    }
    public JSONArray getJsonArrOfTodoWithId(Long id)
    {
        /* add all todos in the array*/
        convertTodoListToJsonArr("", id);
        return jsonArr;
    }
}
