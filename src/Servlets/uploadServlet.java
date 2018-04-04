package Servlets;

import DataAnalyzer.DataAnalyzer;
import Managers.*;
import Tables.Group;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by 徐畅 on 2017/3/13.
 */
@WebServlet(name = "uploadServlet")
public class uploadServlet extends HttpServlet {

    static DataAnalyzer dataAnalyzer = DataAnalyzer.getInstance();
    private String filePath = "C:\\Users\\徐畅\\Desktop\\BlackWord.txt";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String acceptjson = "";
        PrintWriter out = response.getWriter();
        String returnstr = "";

        System.out.println("uploadServlet1.0.2");

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

                if(code.equals("30")){                  //获取群组-时间戳广义表
                    JSONObject datajo = JSONObject.fromObject(jo.get("data"));
                    int user_id = datajo.getInt("id");
                    JSONArray groups = JSONArray.fromObject(datajo.get("groups"));

                    int group_id;
                    String name, groupName;
                    JSONObject group;
                    List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
                    long maxTimestamp = 0;
                    Map<String, Object> map = null;
                    recordManager recordmanager = new recordManager();
                    groupManager groupmanager = new groupManager();
                    for(int i = 0; i < groups.size(); i++){
                        map = new HashMap<>();
                        group = JSONObject.fromObject(groups.get(i));
                        name = group.getString("name");
                        groupName = group.getString("group");
                        group_id = groupmanager.findGroupByUserId(user_id, groupName, name, 1);

                        //System.out.println("uploadServlet: group_id :" + group_id);

                        if(group_id > 0){
                            maxTimestamp = recordmanager.getMaxTimestamp(user_id, group_id);

                            //System.out.println("uploadServlet: timestampList :" + timestampList.size());

                            /*for(int s = 0; s < timestampList.size(); s++){
                                System.out.println("uploadServlet: timestampList :" + timestampList.get(s));
                            }*/

                            map.put("group", groupName);
                            map.put("timestamp", maxTimestamp+"");

                            System.out.println("uploadServlet: timestampList :" + maxTimestamp);

                            groupList.add(map);
                        }

                    }
                    map = new HashMap<>();
                    map.put("code", 1);
                    map.put("data", groupList);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }else if(code.equals("31") || code.equals("32")){            //上传聊天记录
                    //int user_id = jo.getJSONObject("data").getInt("id");
                    JSONObject datajo = JSONObject.fromObject(jo.get("data"));
                    int user_id = datajo.getInt("id");
                    int group_id, sender_id, result = 0;
                    String groupName = "", name = "";
                    Map<String, Object> map = null;
                    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                    JSONArray groups = JSONArray.fromObject(datajo.get("groups"));

                    groupManager groupmanager = new groupManager();
                    senderManager sendermanager = new senderManager();
                    recordManager recordmanager = new recordManager();
                    JSONObject group;
                    JSONArray chat_records;
                    //Map<String, Object> senderMap = null;
                    if(code.equals("32")){
                        chat_records = JSONArray.fromObject(datajo.getJSONArray("chat_records"));
                        group_id = -1;
                        long maxtimestamp = recordmanager.getMaxTimestamp(user_id, group_id);
                        JSONObject chat;
                        for(int s = 0; s < chat_records.size(); s++){
                            chat = JSONObject.fromObject(chat_records.get(s));
                            if(chat.getLong("timestamp") > maxtimestamp){
                                map = getChatRecords(user_id, chat);
                                map.put("group_id", group_id);
                                sender_id = sendermanager.findSenderIdByUserId(user_id, (String)map.get("sender"));

                                //如果未找到sender
                                if(sender_id == 0){
                                    sender_id = notFindSenderId(user_id, (String)map.get("sender"), (String)map.get("name"));
                                }

                                map.put("sender_id", sender_id);
                                mapList.add(map);
                            }
                        }
                        result = recordmanager.addRecordByUserId(user_id, mapList);
                    }else{
                        for (int i = 0; i < groups.size(); i++) {
                            group = JSONObject.fromObject(groups.get(i));
                            group_id = -1;
                            if(code.equals("31")){
                                groupName = group.getString("group");
                                name = group.getString("name");

                                group_id = groupmanager.findGroupByUserId(user_id, groupName, name, 1);
                                //System.out.println("notfindgroupId1: " +group_id);
                                if(group_id == 0){
                                    group_id = notFindGroupId(user_id, groupName, group.getString("name"), 1);
                                    //System.out.println("notfindgroupId1: " +group_id);
                                }
                            }
                            chat_records = JSONArray.fromObject(group.getJSONArray("chat_records"));
                            for(int s = 0; s < chat_records.size(); s++){
                                map = getChatRecords(user_id, chat_records.getJSONObject(s));
                                map.put("group_id", group_id);
                                sender_id = sendermanager.findSenderIdByUserId(user_id, (String)map.get("sender"));

                                if(sender_id == 0){
                                    sender_id = notFindSenderId(user_id, (String)map.get("sender"), (String)map.get("name"));
                                }
                                map.put("sender_id", sender_id);
                                mapList.add(map);
                            }
                            result = recordmanager.addRecordByUserId(user_id, mapList);
                        }
                    }

                    Map<String,String> retmap = new HashMap<>();
                    retmap.put("code", "1");
                    returnstr = new Gson().toJson(retmap);
                    //returnstr = "get it";
                }else if(code.equals("d11")){

                    //System.out.println("d11:jo: " + jo);

                    int user_id = Integer.parseInt(jo.getString("id"));
                    JSONObject dataObject = JSONObject.fromObject(jo.get("data"));
                    dataAnalyzerAndKeywordCounter(user_id, dataObject, Long.parseLong(dataObject.getString("timestamp")), false);


                    Map<String, String> map = new HashMap<>();
                    map.put("code", "1");
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }
                out.println(returnstr);
            }
        }catch (Exception e) {
            e.printStackTrace();

        }
    }

    int notFindGroupId(int user_id, String groupName, String name, int auto_upload){
        int group_id = 0;
        Group group = new Group();
       // group.setSid(groupName);
        //group.setName(name);
        //group.setAuto_upload(auto_upload);
        groupManager groupmanager = new groupManager();
        groupmanager.addGroupByUserId(user_id, groupName, name);
        group_id = groupmanager.findGroupByUserId(user_id, groupName, name, 1);
        return group_id;
    }

    int notFindSenderId(int user_id, String senderName, String name){
        int sender_id = 0;
        senderManager sendermanager = new senderManager();
        Map<String, Object> senderMap = new HashMap<String, Object>();
        senderMap.put("sender", senderName);
        senderMap.put("name", name);
        sendermanager.addSenderByUserId(user_id, senderMap);
        sender_id = sendermanager.findSenderIdByUserId(user_id, senderName);
        return sender_id;
    }

    Map<String ,Object> getChatRecords(int user_id, JSONObject chat){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Integer.parseInt(chat.getString("code")));
        map.put("sender", chat.getString("sender"));
        map.put("name", chat.getString("name"));
        map.put("timestamp", chat.getLong("timestamp"));
        map.put("order", chat.getInt("order"));
        map.put("data", chat.getString("data"));
        map.put("keyword_ids", dataAnalyzerAndKeywordCounter(user_id, chat, (long)map.get("timestamp"), true));
        return map;
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    String dataAnalyzerAndKeywordCounter(int user_id, JSONObject chat, long timestamp, boolean recordflag) {
        String keyword_ids = "";
        //数据分析
        try {
            //获得当前记录的全部keywords_id
            String []result = dataAnalyzer.analysis(chat);

            int flag = 0, s = 0, temp;
            keywordManager keywordmanager = new keywordManager();
            Map<String,Object> keywordMap = null;
            List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> keywordIds = keywordmanager.findKeywordIdsByUserIdAndKeywords(user_id, result);
            //消去无用词
            keywordIds = removeBlackWord(keywordIds);


            for(int i = 0; i < keywordIds.size(); i++){
                flag = 0;
                //keyword_ids = keyword_ids + " " + keywordIds.get(i);
                for(s = 0; s < resultList.size(); s++){
                    if(resultList.get(s).get("word").equals(keywordIds.get(i).get("word"))){         //如果找到这个词
                        flag = s + 1;
                        temp = (int)resultList.get(s).get("count");
                        resultList.get(s).put("count", (temp + 1));
                        break;
                    }
                }
                //在当前没有此词在list
                if(flag == 0){
                    keywordMap = new HashMap<>();
                    keywordMap.put("id", keywordIds.get(i).get("id"));
                    keywordMap.put("count", 1);
                    keywordMap.put("word", keywordIds.get(i).get("word"));
                    if((int)keywordIds.get(i).get("id") != 0){
                        keyword_ids = keyword_ids + keywordIds.get(i).get("id") + " ";
                    }
                    resultList.add(keywordMap);
                }
            }

            //把resultList的结果更新数据库
            keywordcounterManager keywordcountermanager = new keywordcounterManager();
            keywordcountermanager.addKeywordCounterByUserId(user_id, resultList, timestamp);
            if(recordflag == false){                //不是聊天记录就要更新wordcounter
                wordcounterManager wordcountermanager = new wordcounterManager();
                wordcountermanager.addWordCounterByUserId(user_id, resultList, timestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyword_ids;
    }

    protected List<Map<String, Object>> removeBlackWord(List<Map<String, Object>> resultList){
        List<String> blackWord = importDataServlet.readTxtFile(this.filePath);
        for(int i = 0; i < resultList.size(); i++){
            for(int s = 0; s < blackWord.size(); s++){
                //System.out.println("removeBlackWord: resultlist: "+resultList.get(i).get("word")+"        blackWord:"+blackWord.get(s));
                if(blackWord.get(s).equals(resultList.get(i).get("word"))){
                    resultList.remove(i);
                    i--;
                    break;
                }
            }
            /*if(blackWord.contains(resultList.get(i).get("word"))){
                System.out.println("removeBlackWord: " + resultList.get(i).get("word"));
                resultList.remove(i);
                i--;
            }*/
        }
        return resultList;
    }

}
