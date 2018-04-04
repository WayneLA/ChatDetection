package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 徐畅 on 2017/5/24.
 */
public class wordcounterManager {

    //创建关键词表（通过userId）
    public int createWordCounterTableByUserId(int userId){
        int result = 0;
        Connect myconn = new Connect();
        String create = "create table wordcounter_"+ userId +"(" +
                "id int NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY(id), " +
                "word TEXT NOT NULL, " +
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
    public int addWordCounterByUserId(int userId, List<Map<String, Object>> resultList, long timestamp){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        for(int i = 0; i < resultList.size(); i++){
            update = "insert into wordcounter_" + userId + " values (" +
                    "null, " +
                    "'"+ resultList.get(i).get("word") +"', " +
                    + timestamp + "," +
                    resultList.get(i).get("count") + ")";

            //System.out.println("update:" + update);

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
    public List<Map<String, Object>> getKeywordAndTimes(int user_id, long bg_time, long ed_time, int num){
        Connect myconn = new Connect();
        List<Map<String,Object>> keywordList = new ArrayList<Map<String,Object>>();
        String Select = "select SUM(times) AS counts, word " +
                "from wordcounter_" + user_id + " k " +
                " where timestamp BETWEEN " + bg_time + " and " + ed_time +
                " group by word" +
                " ORDER BY counts DESC limit " + num;
        try {
            myconn.setRs(myconn.getStmt().executeQuery(Select));
            Map<String,Object> map = null;
            while(myconn.getRs().next()){
                map = new HashMap<>();
                map.put("x", myconn.getRs().getString("word"));
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
