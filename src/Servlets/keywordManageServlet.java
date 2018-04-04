package Servlets;

import Managers.groupManager;
import Managers.keywordManager;
import Tables.Group;
import Tables.Keyword;
import com.google.gson.Gson;
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
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐畅 on 2017/3/13.
 */
@WebServlet(name = "keywordManageServlet")
public class keywordManageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

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
                if(code.equals("20")){                  //获取关键词表
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    keywordManager keywordmanager = new keywordManager();
                    List<Map<String, String>> keywordList = keywordmanager.findAllKeywordsByUserId(user_id);
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> datamap = new HashMap<String, Object>();
                    datamap.put("id", user_id);
                    datamap.put("keywords", keywordList);
                    map.put("code", "d");
                    map.put("data", datamap);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }else if(code.equals("21")){            //获取关键词-优先级表
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    keywordManager keywordmanager = new keywordManager();
                    List<Map<String, String>> keywordList = keywordmanager.findAllKeywordsAndWeights(user_id);
                    Map<String, Object> map = new HashMap<String, Object>();
                    Map<String, Object> keywordMap = new HashMap<String, Object>();
                    keywordMap.put("keywords", keywordList);
                    map.put("code", "d");
                    map.put("data", keywordMap);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }else if(code.equals("22")){            //添加关键词
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = Integer.parseInt(dataObject.getString("id"));
                    String keywordName = dataObject.getString("keyword");
                    int weight = dataObject.getInt("weight");
                    String property = dataObject.getString("property");
                    Keyword keyword = new Keyword();
                    keyword.setKeyword(keywordName);
                    keyword.setProperty(property);
                    keyword.setId(user_id);
                    keyword.setWeight(weight);
                    keywordManager keywordmanager = new keywordManager();
                    int result = keywordmanager.addKeywordByUserId(user_id, keyword);
                    returnstr = result + "";
                }else if(code.equals("23")){            //修改关键词
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    String keywordName = dataObject.getString("keyword");
                    int weight = dataObject.getInt("weight");
                    String property = dataObject.getString("property");
                    Keyword keyword = new Keyword();
                    keyword.setKeyword(keywordName);
                    keyword.setProperty(property);
                    keyword.setId(user_id);
                    keyword.setWeight(weight);
                    keywordManager keywordmanager = new keywordManager();
                    int result = keywordmanager.changeKeywordInfo(user_id, keyword);

                    System.out.println("keywordManageServlet:result:  " + result);

                    returnstr = result + "";
                }else if(code.equals("24")){            //删除关键词
//                    int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    int user_id = dataObject.getInt("id");
                    List<String> keywordList = (List<String>)jo.getJSONObject("data").get("keywords");
                    keywordManager keywordmanager = new keywordManager();
                    int result = keywordmanager.deleteKeywordByUserId(user_id, keywordList);
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
