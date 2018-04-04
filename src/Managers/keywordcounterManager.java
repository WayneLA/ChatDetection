package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐畅 on 2017/5/10.
 */
public class keywordcounterManager {

    //创建关键词表（通过userId）
    public int createKeywordCounterTableByUserId(int userId){
        int result = 0;
        Connect myconn = new Connect();
        String create = "create table keywordcounter_"+ userId +"(" +
                "id int NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY(id), " +
                "keyword_id int NOT NULL, " +
                "timestamp bigint NOT NULL, " +
                "times int NOT NULL)";


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

    //添加关键词（通过userId找到表）
    public int addKeywordCounterByUserId(int userId, List<Map<String, Object>> resultList, long timestamp){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        for(int i = 0; i < resultList.size(); i++){
            if((int)resultList.get(i).get("id") != 0){
                update = "insert into keywordcounter_" + userId + " values (" +
                        "null, " +
                        + (int)resultList.get(i).get("id") +", " +
                        + timestamp + "," +
                        resultList.get(i).get("count") + ")";

                System.out.println("update:" + update);

                try {
                    result = myconn.getStmt().executeUpdate(update);
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {

                }
            }

        }
        myconn.utils();
        return result;
    }

    public List<Map<String, Object>> getKeywordAndTimes(int user_id, long bg_time, long ed_time, int num){
        Connect myconn = new Connect();
        List<Map<String,Object>> keywordList = new ArrayList<Map<String,Object>>();

        String select = "select SUM(kc.times) AS counts , keyword_id " +
                 "from keywordcounter_" + user_id + " kc " +
                 "where timestamp between " + bg_time + " and " + ed_time + " group by kc.keyword_id";
        /*String Select = "select counts, keyword " +
                        "from keyword_" + user_id + " k left join " + select +
                        " ON k.id = keyword_id " +
                        "ORDER BY counts DESC limit " + num;*/
        try {
            System.out.println("keywordcounter getKeywordAndTimes: select: "+select);
            myconn.setRs(myconn.getStmt().executeQuery(select));


            keywordManager keywordmanager  = new keywordManager();
            Map<String,Object> map = null;
            String keyword;
            while(myconn.getRs().next()){
                map = new HashMap<>();

                keyword = keywordmanager.findkeywordByUserIdAndKeywordId(user_id, myconn.getRs().getInt("keyword_id"));

                map.put("x", keyword);
                map.put("y", myconn.getRs().getInt("counts"));
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
}
