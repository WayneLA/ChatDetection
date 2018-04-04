package Managers;

import java.sql.SQLException;
import java.util.*;

import Tables.Group;
import Tables.Keyword;

/**
 * Created by 徐畅 on 2017/2/17.
 */
public class keywordManager {

    //创建关键词表（通过userId）
    public int createKeywordTableByUserId(int userId){
        int result = 0;
        Connect myconn = new Connect();
        String create = "create table keyword_"+ userId +"(" +
                "id int NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY(id), " +
                "keyword VARCHAR(20) NOT NULL, " +
                "weight int NOT NULL," +
                "property VARCHAR(5) NOT NULL)";


        System.out.println("create:" + create);

        try {
            result = myconn.getStmt().executeUpdate(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            myconn.utils();
        }

        return result;
    }

    //获取关键词表（通过用户名）
    public List<Map<String,String>> findAllKeywords(String userName){
        Connect myconn = new Connect();
        List<Map<String,String>> keywordList = new ArrayList<Map<String,String>>();

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + ""));
            Map<String,String> map = null;
            while(myconn.getRs().next()){
                map = new HashMap<String,String>();
                map.put("id", myconn.getRs().getInt("id")+"");
                map.put("keyword", myconn.getRs().getString("keyword"));
                keywordList.add(map);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keywordList;

        /*Connect myconn = new Connect();
        List<Keyword> keywordList = new ArrayList<Keyword>();

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + ""));
            Keyword keyword = null;

            while(myconn.getRs().next()){
                keyword = new Keyword();
                keyword.setId(myconn.getRs().getInt("id"));
                keyword.setKeyword(myconn.getRs().getString("keyword"));
                keyword.setWeight(myconn.getRs().getInt("weight"));
                keywordList.add(keyword);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keywordList;*/
    }

    //获取关键词表（通过用户Id）
    public List<Map<String,String>> findAllKeywordsByUserId(int userId){
        Connect myconn = new Connect();
        List<Map<String,String>> keywordList = new ArrayList<Map<String,String>>();

        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + ""));
            Map<String,String> map = null;
            while(myconn.getRs().next()){
                map = new HashMap<String,String>();
                map.put("id", myconn.getRs().getInt("id")+"");
                map.put("keyword", myconn.getRs().getString("keyword"));
                keywordList.add(map);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keywordList;
    }

    //获取关键词表（通过用户Id）
    public Keyword findKeywordByUserIdAndKeywordId(int userId, int keyword_id){
        Connect myconn = new Connect();
        Keyword keyword = null;

        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + " where id = " + keyword_id));
            while(myconn.getRs().next()){
                keyword = new Keyword();
                keyword.setId(myconn.getRs().getInt("id"));
                keyword.setKeyword(myconn.getRs().getString("keyword"));
                keyword.setProperty(myconn.getRs().getString("property"));
                keyword.setWeight(myconn.getRs().getInt("weight"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keyword;
    }

    public String findkeywordByUserIdAndKeywordId(int userId, int keyword_id){
        Connect myconn = new Connect();
        String keyword = null;

        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + " where id = " + keyword_id));
            while(myconn.getRs().next()){
                keyword = myconn.getRs().getString("keyword");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keyword;
    }

    //获取关键词表中weight（通过keyword_id）
    public int findWeightByUserIdAndKeywordId(int user_id, int keyword_id){
        Connect myconn = new Connect();
        int weight = 0;

        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + user_id + " where id = " + keyword_id));
            while(myconn.getRs().next()){
                weight = myconn.getRs().getInt("weight");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return weight;
    }

    //获取关键词表中keyword_id（通过keyword）
    public int findKeywordIdByUserIdAndKeyword(int user_id, String keyword){
        Connect myconn = new Connect();
        int keyword_id = 0;

        String sql = "select * from keyword_" + user_id + " where keyword = '" + keyword + "'";
        try {
            myconn.setRs(myconn.getStmt().executeQuery(sql));
            //System.out.println("findKeywordIdByUserIdAndKeyword:  " + sql);
            while(myconn.getRs().next()){
                keyword_id = myconn.getRs().getInt("id");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return keyword_id;
    }

    public List<Map<String, Object>> findKeywordIdsByUserIdAndKeywords(int user_id, String[] keyword){
        Connect myconn = new Connect();
        int keyword_id = 0;
        boolean flag = true;
        List<Map<String, Object>> keywordList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        String sql;
        try {
            for(int i = 0; i < keyword.length; i++) {
                keyword_id = 0;
                sql = "select * from keyword_" + user_id + " where keyword = '" + keyword[i] + "'";
                myconn.setRs(myconn.getStmt().executeQuery(sql));
                //System.out.println("findKeywordIdByUserIdAndKeyword:  " + sql);
                while(myconn.getRs().next()){
                    keyword_id = myconn.getRs().getInt("id");
                    /*if(keyword_id != 0){
                        keywords.add(keyword_id);
                    }*/
                }
                //写入map
                map = new HashMap<>();
                map.put("word", keyword[i]);
                map.put("id", keyword_id);
                keywordList.add(map);
                /*map.put("count", 1);
                if(keyword_id == 0){
                    flag = false;
                }
                map.put("flag", flag);*/
                //keywords.add(keyword_id);
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }

        return keywordList;
    }

    //获取关键词-权值表（通过用户名）
    public List<Map<String,String>> findAllKeywordsAndWeights(int userId) {
        Connect myconn = new Connect();
        List<Map<String, String>> keywordList = new ArrayList<Map<String, String>>();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from keyword_" + userId + ""));
            Map<String, String> map = null;
            while (myconn.getRs().next()) {
                map = new HashMap<String, String>();
                map.put("id", myconn.getRs().getInt("id") + "");
                map.put("keyword", myconn.getRs().getString("keyword"));
                map.put("weight", myconn.getRs().getInt("weight") + "");
                map.put("property", myconn.getRs().getString("property"));
                keywordList.add(map);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            myconn.utils();
        }
        return keywordList;
    }

    //添加关键词（通过userId找到表）
    public int TESTaddKeywordByUserId(int userId, List<String> keywordList){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        for(int i = 0; i < keywordList.size(); i++){
            update = "insert into keyword_" + userId + " values (" +
                    "null, " +
                    "'" + keywordList.get(i) + "',"
                    + 8 + "," +
                    " 'n')";

            System.out.println("update:" + update);

            try {
                result = myconn.getStmt().executeUpdate(update);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {

            }
        }
        myconn.utils();
        return result;
    }

    //添加关键词（通过userId找到表）
    public int addKeywordByUserId(int userId, Keyword keyword){
        int result = 0;
        Connect myconn = new Connect();
        String update = "insert into keyword_" + userId + " values (" +
                        "null, " +
                        "'" + keyword.getKeyword() + "',"
                        + keyword.getWeight() + "," +
                        "'" +keyword.getProperty() + "')";

        System.out.println("update:" + update);

        try {
            result = myconn.getStmt().executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            myconn.utils();
        }
        return result;
    }

    //修改关键词（通过用户Id和关键词来唯一确定）
    public int changeKeywordInfo(int userId, Keyword keyword){
        int result = 0;
        Connect myconn = new Connect();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);

        String update = "update keyword_"+ userId +" set weight = " + keyword.getWeight() +
                " ,property = '"+ keyword.getProperty() +"' where keyword = '" + keyword.getKeyword() + "'";

        System.out.println("update: "+update);

        try {
            result = myconn.getStmt().executeUpdate(update);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return result;
    }

    //删除关键词（通过用户名和关键词名唯一确定)
    public int deleteKeyword(String userName, List<Keyword> keywordList){

        int result = 0;
        Connect myconn = new Connect();

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);

        for(Keyword keyword:keywordList){
            try {
                result = myconn.getStmt().executeUpdate("delete from keyword_"+ userId +" where keyword = '"+ keyword.getKeyword() +"'");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{

            }
        }
        myconn.utils();
        return result;
    }

    //删除关键词（通过用户名和关键词名唯一确定)
    public int deleteKeywordByUserId(int userId, List<String> keywordList){

        int result = 0;
        Connect myconn = new Connect();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);

        for(int i = 0; i < keywordList.size(); i++){
            try {
                result = myconn.getStmt().executeUpdate("delete from keyword_"+ userId +" where keyword = '"+ keywordList.get(i) +"'");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{

            }
        }
        myconn.utils();
        return result;
    }
}
