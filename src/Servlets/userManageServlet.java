package Servlets;

import Managers.userManager;
import Tables.User;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐畅 on 2017/3/5.
 */
@WebServlet(name = "userManageServlet")
public class userManageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("utf-8");

        System.out.println("userManageServlet");

        String acceptjson = "";
        PrintWriter out = response.getWriter();
        String returnstr = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (ServletInputStream) request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            acceptjson = sb.toString();
            if (acceptjson != "") {
                JSONObject jo = JSONObject.fromObject(acceptjson);
                String code = jo.getString("code");
                if(code.equals("d1") || code.equals("00")){
                    String account = jo.getJSONObject("data").getString("account");
                    int password = jo.getJSONObject("data").getInt("password");

                    //System.out.println("account: " + account);
                    //System.out.println("password: " + password);

                    userManager usermanager = new userManager();
                    Map<String,String> map = usermanager.findUserByUserNameAndPassword(account, password);
                    map.put("code", "1");

                    System.out.println("map: " + map.get("is_admin"));

                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                    //System.out.println("1 returnstr: " + returnstr);
                    out.println(returnstr);

                }else if(code.equals("d2")){
                    List<User> userList = new ArrayList<User>();
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("code", "d");
                    map.put("data", userList);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }else if(code.equals("d3")){
                    String account = jo.getString("account");
                    int password = jo.getInt("password");
                    userManager usermanager = new userManager();
                    User user = new User();
                    user.setAccount(account);
                    user.setPassword(password);
                    int result = usermanager.addUser(user);
                    returnstr = result + "";
                }else if(code.equals("d4")){
                    String account = jo.getString("account");
                    int password = jo.getInt("password");
                    userManager usermanager = new userManager();
                    int result = usermanager.changePasswordByuserName(account, password);
                    returnstr = result + "";
                }else if(code.equals("d5")){
                    String account = jo.getString("account");
                    userManager usermanager = new userManager();
                    int result = usermanager.deleteUser(account);
                    returnstr = result + "";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
