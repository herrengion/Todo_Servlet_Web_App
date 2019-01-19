package jsonData;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUser {

    private String name;
    private String password;
    private JSONArray jsonArr;

    public JsonUser(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = null;
        String userName;
        String pwStr;
        try {
            jsonObj = (JSONObject) parser.parse(jsonString);
            userName = (String) jsonObj.get("name");
            pwStr = (String) jsonObj.get("password");
        } catch (ParseException e) {
            throw new VerifyError("User credential invalid");
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("User data type is invalid");
        }

        if(userName == null || userName.isEmpty() ||
            pwStr == null || pwStr.isEmpty())
        {
            throw new IllegalArgumentException("User credential are empty or null");
        }
        setName(userName);
        setPassword(pwStr);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
