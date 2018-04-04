package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Tables.Record;

/**
 * Created by 徐畅 on 2017/2/18.
 */
public class recordManager {

    //创建聊天记录表（通过userId）
    public int createRecordTableByUserId(int userId){
        int result = 0;
        Connect myconn = new Connect();
        String create = "create table record_"+ userId +"(" +
                "id int NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY(id), " +
                "group_id int NOT NULL, " +
                "sender_id int NOT NULL, " +
                "name text NOT NULL, " +
                "timestamp bigint NOT NULL, " +
                "order int NOT NULL, " +
                "code int NOT NULL, " +
                "data text NOT NULL, " +
                "keyword_ids text NOT NULL )";


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

    //获取聊天记录表（通过userName）
    public List<Record> findAllRecordsByTime(int userId, int startTime, int endTime){
        Connect myconn = new Connect();
        List<Record> recordList = new ArrayList<Record>();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from record_" + userId + " where timestamp between " +
                    startTime + " and " + endTime +""));
            Record record = null;

            while(myconn.getRs().next()){
                record = new Record();
                record.setId(myconn.getRs().getInt("id"));
                record.setGroup_id(myconn.getRs().getInt("group_id"));
                record.setSender_id(myconn.getRs().getInt("sender_id"));
                record.setName(myconn.getRs().getString("name"));
                record.setTimestamp(myconn.getRs().getLong("timestamp"));
                record.setOrder(myconn.getRs().getInt("order"));
                record.setCode(myconn.getRs().getInt("code"));
                record.setData(myconn.getRs().getString("data"));
                record.setKeyword_ids(myconn.getRs().getString("keyword_ids"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return recordList;
    }

    /*//获取聊天记录表（通过userName和时间戳）
    public List<Record> findAllGroupsByTime(String userName ,int startTime, int endTime){
        Connect myconn = new Connect();
        List<Record> recordList = new ArrayList<Record>();

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from record_" + userId + "where timestamp between startTime and endTime"));
            Record record = null;

            while(myconn.getRs().next()){
                record = new Record();
                record.setId(myconn.getRs().getInt("id"));
                /*record.setGroup_id(myconn.getRs().getInt("group_id"));
                record.setSender_id(myconn.getRs().getInt("sender_id"));
                record.setTimestamp(myconn.getRs().getInt("timestamp"));
                record.setOrder(myconn.getRs().getInt("order"));
                record.setCode(myconn.getRs().getInt("code"));
                record.setData(myconn.getRs().getString("data"));
                record.setKeyword_ids(myconn.getRs().getString("keyword_ids"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return recordList;
    }*/

    /*//添加聊天记录（通过userId找到聊天记录表）
    public int addRecordByUserId(int userId, List<Record> recordList){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        for(Record record: recordList){
            update = "insert into record_" + userId + "values (" +
                    "null, " +
                    record.getGroup_id() + "," +
                    record.getSender_id() + "," +
                    record.getName() + "," +
                    record.getTimestamp() + "," +
                    record.getOrder() + "," +
                    record.getCode() + "," +
                    "'" + record.getData() + "'," +
                    "'" + record.getKeyword_ids() + "')";

            System.out.println("update:" + update);

            try {
                result = myconn.getStmt().executeUpdate(update);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                myconn.utils();
            }
        }
        return result;
    }*/

    //添加聊天记录（通过userId找到聊天记录表）
    public int addRecordByUserId(int userId, List<Map<String, Object>> recordList){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        for(Map<String, Object> map: recordList){
            update = "insert into record_" + userId + " values (" +
                    "null, " +
                    map.get("group_id") + "," +
                    map.get("sender_id") + "," +
                    "'" + map.get("name") + "'," +
                    map.get("timestamp") + "," +
                    map.get("order") + "," +
                    map.get("code") + "," +
                    "'" + map.get("data") + "'," +
                    "'" + map.get("keyword_ids") + "')";

            System.out.println("update:" + update);

            try {
                result = myconn.getStmt().executeUpdate(update);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {

            }
        }
        myconn.utils();
        System.out.println("addRecordByUserId result:" + result);
        return result;
    }

    //获取聊天记录表（通过userId和时间戳和target_id）
    public List<Record> findRecordByUnitAndTime(int userId ,int unit, int target_id, int startTime, int endTime){
        Connect myconn = new Connect();
        List<Record> recordList = new ArrayList<Record>();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);

        String excuteString = "";
        if(unit == 0){
            excuteString = "select * from record_" + userId + " where sender_id = " + target_id + " and timestamp between "+startTime+"  and "+endTime;
        }else{
            excuteString = "select * from record_" + userId + " where group_id = " + target_id + " and timestamp between "+startTime+"  and "+endTime;
        }

        try {
            myconn.setRs(myconn.getStmt().executeQuery(excuteString));
            Record record = null;

            while(myconn.getRs().next()){
                record = new Record();
                record.setId(myconn.getRs().getInt("id"));
                record.setGroup_id(myconn.getRs().getInt("group_id"));
                record.setSender_id(myconn.getRs().getInt("sender_id"));
                record.setName(myconn.getRs().getString("name"));
                record.setTimestamp(myconn.getRs().getLong("timestamp"));
                record.setOrder(myconn.getRs().getInt("order"));
                record.setCode(myconn.getRs().getInt("code"));
                record.setData(myconn.getRs().getString("data"));
                record.setKeyword_ids(myconn.getRs().getString("keyword_ids"));
                recordList.add(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return recordList;
    }

    //获取聊天记录表中的Keyword_ids字段（通过userId和时间戳和target_id）
    public List<String> findKeywordIdsByUnitAndTime(int userId ,int unit, int target_id, long startTime, long endTime){
        Connect myconn = new Connect();
        List<String> recordList = new ArrayList<String>();

        String excuteString = "";
        if(unit == 0){
            excuteString = "select * from record_" + userId + " where sender_id = " + target_id + " and timestamp between "+startTime+"  and "+endTime;
        }else{
            excuteString = "select * from record_" + userId + " where group_id = " + target_id + " and timestamp between "+startTime+"  and "+endTime;
        }

        try {
            myconn.setRs(myconn.getStmt().executeQuery(excuteString));

            while(myconn.getRs().next()){
                recordList.add(myconn.getRs().getString("keyword_ids"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return recordList;
    }

    //获取聊天记录表中的Timestamp字段（通过userId和group_id和name）
    public List<String> findTimestampByUnitAndTime(int userId , int group_id){
        Connect myconn = new Connect();
        List<String> timestampList = new ArrayList<String>();

        String excuteString = "select * from record_" + userId + " where group_id = " + group_id + "";
        System.out.println("findTimestampByUnitAndTime : " +excuteString);

        try {
            myconn.setRs(myconn.getStmt().executeQuery(excuteString));

            while(myconn.getRs().next()){
                timestampList.add(myconn.getRs().getLong("timestamp")+"");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return timestampList;
    }

    //获取聊天记录表中最大的Timestamp字段（通过userId和group_id和name）
    public long getMaxTimestamp(int userId , int group_id){
        Connect myconn = new Connect();
        long maxTimestamp = 0;

        String excuteString = "select MAX(timestamp) AS maxTimestamp from record_" + userId + " where group_id = " + group_id + "";
        System.out.println("findTimestampByUnitAndTime : " +excuteString);

        try {
            myconn.setRs(myconn.getStmt().executeQuery(excuteString));

            while(myconn.getRs().next()){
                maxTimestamp = myconn.getRs().getLong("maxTimestamp");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return maxTimestamp;
    }
}
