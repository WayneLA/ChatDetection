package Servlets;

import Managers.*;
import Tables.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.*;

/**
 * Created by 徐畅 on 2017/3/5.
 */
@WebServlet(name = "statisticsServlet")
public class statisticsServlet extends HttpServlet {

    private int oneDay = 86400;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        System.out.println("statisticsServlet 1.1");

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

                System.out.println("d:"+code);

                if(code.equals("d6")) {
                    int user_id = jo.getInt("id");
                    int unit = jo.getInt("unit");
                    Map<String, Object> map = new HashMap<String, Object>();
                    if(unit == 0){
                       senderManager sendermanager = new senderManager();
                       List<Map<String, Object>> senderList = sendermanager.findAllSendersByUserId(user_id);
                       map.put("data", senderList);
                       map.put("code", "d");
                       Gson gson = new Gson();
                       returnstr = gson.toJson(map);
                    }else{
                        groupManager groupmanager = new groupManager();
                        List<Map<String, Object>> groupList = groupmanager.findAllGroupByUserId(user_id);
                        map.put("data", groupList);
                        map.put("code", "d");
                        Gson gson = new Gson();
                        returnstr = gson.toJson(map);
                    }
                }else if(code.equals("d7")){                    //获取对象的聊天记录数
                    Map<String, Object> map = new HashMap<String, Object>();
                    int user_id = jo.getInt("user_id");
                    int unit = jo.getInt("unit");
                    int target_id = jo.getInt("target");
                    int bg_time = jo.getInt("bg_time");
                    int ed_time = jo.getInt("ed_time");
                    recordManager recordmanager = new recordManager();
                    map.put("code", 1);
                    map.put("data", recordmanager.findRecordByUnitAndTime(user_id, unit,target_id, bg_time, ed_time).size());
                    Gson gson = new Gson();
                    returnstr =  gson.toJson(map);
                }else if(code.equals("d8")){                    //获取统计目标常用词表(每个关键词个数/总数)
                    float allNumber = 0;
                    int i, place, tempNumber;
                    int user_id = jo.getInt("id");
                    int unit = jo.getInt("unit");
                    int target_id = Integer.parseInt(jo.getString("target"));
                    int bg_time = jo.getInt("bg_time");
                    int ed_time = jo.getInt("ed_time");
                    Map<String, Object> resultmap = new HashMap<String, Object>();
                    Map<String, Object> map;
                    keywordManager keywordmanager = new keywordManager();
                    recordManager recordmanager = new recordManager();
                    List<Record> recordList = recordmanager.findRecordByUnitAndTime(user_id, unit,target_id, bg_time, ed_time);
                    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                    String []strArray = null;
                    //keywordValue keywordvalue = null;
                    Keyword keyword = null;
                    for(Record record: recordList){
                        strArray = record.getKeyword_ids().split(" ");
                        if(strArray.length != 0 && strArray[0] != ""){
                            for (i = 0; i < strArray.length; i++) {
                                place = findKeywordId(Integer.parseInt(strArray[i]), resultList);
                                if(place == -1){
                                    map = new HashMap<String, Object>();
                                    map.put("keyword_id", Integer.parseInt(strArray[i]));
                                    keyword = keywordmanager.findKeywordByUserIdAndKeywordId(user_id, (int)map.get("keyword_id"));
                                    if(keyword.getWeight() == -1){
                                        map.put("is_special", 1);
                                    }else{
                                        map.put("is_special", 0);
                                    }
                                    map.put("keyword", keyword.getKeyword());
                                    map.put("number", 1);
                                    resultList.add(map);

                                    //keywordvalue = new keywordValue();
                                    //keywordvalue.setKeyword_id(Integer.parseInt(strArray[i]));
                                    //keyword = keywordmanager.findKeywordByUserIdAndKeywordId(user_id, keywordvalue.getKeyword_id());
                                    //keywordvalue.setKeyword(keyword.getKeyword());
                                    //keywordvalue.setNumber(1);
                                    //resultList.add(keywordvalue);
                                }else{
                                    tempNumber = (int)resultList.get(place).get("number");
                                    resultList.get(i).put("number", tempNumber + 1);
                                }
                                allNumber++;
                            }

                        }
                    }
                    for(i = 0; i < resultList.size(); i++){
                        resultList.get(i).put("percent", Float.parseFloat(resultList.get(i).get("number")+"")/allNumber);
                    }
                    resultmap.put("code", "d");
                    resultmap.put("data", resultList);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(resultmap);
                }else if(code.equals("d9")){                    //获取统计单位总体报表
                    float tempNumber;
                    long betweenDays;
                    int oneday = 60 * 60 *24;

                    System.out.println("jo: "+ jo.toString());
                    //System.out.println("user_id: " + jo.getString("id"));

                    int user_id = jo.getInt("id");
                    int unit = jo.getInt("unit");
                    int bg_time = jo.getInt("bg_time");
                    int ed_time = jo.getInt("ed_time");
                    int type = jo.getInt("type");

                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    List<Map<String, Object>> resultList = findResultListByUnit(unit, user_id);
                    switch (type){
                        case 0:                                 //权值表
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalWeight(0, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber);           //totalWeight
                            }
                            break;
                        case 1:                                 //关键词表
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalKeywords(0, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber);//totalKeyword
                            }
                            break;
                        case 2:                                 //权值/单位时间表
                            betweenDays = (long)((ed_time - bg_time) / oneday + 0.5);
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalWeight(0, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber/betweenDays);//weightPerTime
                            }
                            break;
                        case 3:                                 //关键词/单位时间表
                            betweenDays = (long)((ed_time - bg_time) / oneday + 0.5);
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalKeywords(0, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber/betweenDays);//keywordPerTime
                            }
                            break;
                        case 4:                                 //权值/聊天记录数表
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalWeight(1, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber);//weightPerRecord
                            }
                            break;
                        case 5:                                 //关键词/聊天记录数表
                            for(int s = 0; s < resultList.size(); s++){
                                tempNumber = totalKeywords(1, user_id, unit, (int)resultList.get(s).get("id"), bg_time, ed_time);
                                resultList.get(s).put("y", tempNumber);//keywordPerRecord
                            }
                            break;
                        default:
                            break;
                    }
                    resultMap.put("code", "1");
                    resultMap.put("data", resultList);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.serializeSpecialFloatingPointValues();
                    Gson gson = gsonBuilder.create();


                    /*//UserFloat userFloat = new UserFloat("Norman", Float.POSITIVE_INFINITY);
                    String usersJson = gson.toJson(resultMap);*/


                    //Gson gson = new Gson();
                    returnstr = gson.toJson(resultMap);

                    System.out.println("return:"+returnstr.toString());

                }else if(code.equals("d10")){                   //获取统计目标报表
                    int user_id = jo.getInt("id");
                    int unit = jo.getInt("unit");
                    int target_id = Integer.parseInt(jo.getString("target"));
                    long bg_time = jo.getLong("bg_time");
                    long ed_time = jo.getLong("ed_time");
                    int type = jo.getInt("type");
                    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;
                    Long nowTime = bg_time;
                    while((nowTime + oneDay) <= ed_time){
                        map = new HashMap<String, Object>();
                        map.put("x", nowTime);
                        map.put("y", findResultNumber(type, user_id, unit, target_id, nowTime, (nowTime + oneDay)));
                        nowTime += oneDay;
                        resultList.add(map);
                    }
                    map = new HashMap<>();
                    map.put("code", 1);
                    map.put("data", resultList);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }else if(code.equals("d12")){
                    //System.out.println("d11:jo: " + jo);

                    int user_id = Integer.parseInt(jo.getString("id"));
                    long bg_time = jo.getLong("bg_time");
                    long ed_time = jo.getLong("ed_time");
                    int keywordFlag = Integer.parseInt(jo.getString("keyword"));
                    int num = Integer.parseInt(jo.getString("num"));
                    List<Map<String,Object>> resultList = null;
                    if(keywordFlag == 1){           //从keywordcounter里面获取
                        keywordcounterManager keywordcountermanager = new keywordcounterManager();
                        resultList = keywordcountermanager.getKeywordAndTimes(user_id, bg_time, ed_time, num);
                    }else{
                        wordcounterManager wordcountermanager = new wordcounterManager();
                        resultList = wordcountermanager.getKeywordAndTimes(user_id, bg_time, ed_time, num);
                    }
                    /*for(int s = 0; s < resultList.size(); s++){
                        System.out.println("D12: x:" + resultList.get(s).get("x") + "    y:" + resultList.get(s).get("y"));
                    }*/

                    Map<String, Object> map = new HashMap<>();
                    map.put("code", "1");
                    map.put("data", resultList);
                    Gson gson = new Gson();
                    returnstr = gson.toJson(map);
                }

                out.println(returnstr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    float findResultNumber(int type, int user_id, int unit, int target_id, long bg_time, long ed_time){
        float tempNumber = 0;
        //long betweenDays;
        switch (type){
            case 0:                                 //权值表
                tempNumber = totalWeight(0, user_id, unit, target_id, bg_time, ed_time);
                break;
            case 1:                                 //关键词表
                tempNumber = totalKeywords(0, user_id, unit, target_id, bg_time, ed_time);
                break;
            case 2:                                 //权值/单位时间表
                //betweenDays = (long)((ed_time - bg_time) / (1000 * 60 * 60 *24) + 0.5);
                tempNumber = totalWeight(0, user_id, unit, target_id, bg_time, ed_time);
                //tempNumber /=betweenDays;
                break;
            case 3:                                 //关键词/单位时间表
                //betweenDays = (long)((ed_time - bg_time) / (1000 * 60 * 60 *24) + 0.5);
                tempNumber = totalKeywords(0, user_id, unit, target_id, bg_time, ed_time);
                //tempNumber /=betweenDays;
                break;
            case 4:                                 //权值/聊天记录数表
                tempNumber = totalWeight(1, user_id, unit, target_id, bg_time, ed_time);
                break;
            case 5:                                 //关键词/聊天记录数表
                tempNumber = totalKeywords(1, user_id, unit, target_id, bg_time, ed_time);
                break;
            default:
                break;
        }
        return tempNumber;
    }

    List<Map<String, Object>> findResultListByUnit(int unit, int user_id){
        List<Map<String, Object>> resultList = null;
        if(unit == 0){
            senderManager sendermanager = new senderManager();
            resultList = sendermanager.findAllSendersByUserId(user_id);
        }else{
            groupManager groupmanager = new groupManager();
            resultList = groupmanager.findAllGroupByUserId(user_id);
        }
        return resultList;
    }

    int totalKeywords(int flag, int user_id, int unit, int target_id, long bg_time, long ed_time){
        int tempNumber = 0;
        String []strArray = null;
        recordManager recordmanager = new recordManager();
        //keywordManager keywordmanager = new keywordManager();
        List<String> keywordIdsList = recordmanager.findKeywordIdsByUnitAndTime(user_id, unit, target_id, bg_time, ed_time);
        for(int i = 0; i < keywordIdsList.size(); i++){
            strArray = keywordIdsList.get(i).split(" ");
            if(strArray.length != 0 && strArray[0] != ""){
                tempNumber += strArray.length;
            }
        }
        if(flag == 1){       //返回除以聊天记录数后的结果
            if(keywordIdsList.size() != 0){
                tempNumber = tempNumber / keywordIdsList.size();
            }else{
                tempNumber = 0;
            }

        }
        return tempNumber;
    }

    float totalWeight(int flag, int user_id, int unit, int target_id, long bg_time, long ed_time){
        float tempNumber = 0;
        String []strArray = null;
        recordManager recordmanager = new recordManager();
        keywordManager keywordmanager = new keywordManager();
        List<String> keywordIdsList = recordmanager.findKeywordIdsByUnitAndTime(user_id, unit, target_id, bg_time, ed_time);
        for(int i = 0; i < keywordIdsList.size(); i++){

            //System.out.println(i+ "keywordIdsList: "+);

            if(keywordIdsList.get(i) == ""){
                keywordIdsList.remove(i);
            }else{
                strArray = keywordIdsList.get(i).split(" ");
                for (int j = 0; j < strArray.length; j++) {
                    tempNumber += keywordmanager.findWeightByUserIdAndKeywordId(user_id, Integer.parseInt(strArray[j]));
                }
            }

        }
        if(flag == 1){       //返回除以聊天记录数后的结果
            if(keywordIdsList.size() != 0){
                tempNumber = tempNumber / keywordIdsList.size();
            }else{
                tempNumber = 0;
            }

        }
        return tempNumber;
    }

    int findKeywordId(int keyword_id, List<Map<String, Object>> resultList){
        for(int i = 0; i < resultList.size(); i++){
            if((int)resultList.get(i).get("keyword_id") == keyword_id){
                return i;
            }
        }
        return -1;
    }

    int checkTargetId(int keyword_id, List<Map<String, Object>> resultList){
        for(int i = 0; i < resultList.size(); i++){
            if((int)resultList.get(i).get("keyword_id") == keyword_id){
                return i;
            }
        }
        return -1;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
