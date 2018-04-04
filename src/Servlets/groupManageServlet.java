package Servlets;

import Managers.groupManager;
import Tables.Group;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐畅 on 2017/3/13.
 */
@WebServlet(name = "groupManageServlet")
public class groupManageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        System.out.println("groupManageServlet");

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

                //System.out.println("jo:" + jo);

                String code = jo.getString("code");
                if(code.equals("10")){              //获取已设置为自动上传的群组表
                    //int user_id = jo.getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    groupManager groupmanager = new groupManager();
                    List<Map<String, Object>> groupList = groupmanager.getAutouploadGroups(user_id);
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> groupMap = new HashMap<String, Object>();
                    groupMap.put("groups", groupList);
                    map.put("code", "d");
                    map.put("data", groupMap);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);

                    System.out.println("returnstr" + returnstr);

                }else if(code.equals("11")){        //添加自动上传群组
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    int group_id, result = 0;
                    JSONArray groups = JSONArray.fromObject(dataObject.getJSONArray("groups"));
                    JSONObject group;
                    groupManager groupmanager = new groupManager();
                    for(int i = 0; i < groups.size(); i++){
                        group = JSONObject.fromObject(groups.get(i));
                        group_id = groupmanager.findGroupByUserId(user_id, group.getString("group"), group.getString("name"),1);
                        if(group_id == 0){
                            result = groupmanager.addGroupByUserId(user_id, group.getString("group"), group.getString("name"));
                        }else{
                            result = groupmanager.setAutouploadGroupById(user_id, group_id, 1);
                        }
                    }
                    returnstr = result + "";
                }else if(code.equals("12")){        //删除自动上传群组
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    int group_id, result = 0;
                    //List<Group> groupList = (List<Group>)jo.getJSONObject("data").get("groups");
                    groupManager groupmanager = new groupManager();
                    JSONArray groups = JSONArray.fromObject(dataObject.getJSONArray("groups"));
                    //JSONObject group;
                    for(int i = 0; i < groups.size(); i++){
                        //group = JSONObject.fromObject(groups.get(i));
                        group_id = groupmanager.findGroupByUserId(user_id, (String)groups.get(i), "",0);
                        if(group_id != 0) {
                            result = groupmanager.setAutouploadGroupById(user_id, group_id, 0);
                        }
                    }
                    returnstr = result + "";
                }
                out.println(returnstr);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }
}
