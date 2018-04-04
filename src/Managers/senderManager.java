package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tables.Group;
import Tables.Sender;

/**
 * Created by 徐畅 on 2017/2/18.
 */
public class senderManager {

    //创建被检测人表（通过userId）
    public int createSenderTableByUserId(int userId){
        int result = 0;
        Connect myconn = new Connect();
        String create = "create table sender_"+ userId +"(" +
                "id int NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY(id), " +
                "sender VARCHAR(20) NOT NULL, " +
                "name TEXT NOT NULL)";


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

    //添加被检测人（通过用户名唯一确定）
    public int addSendersByUserName(String userName, List<Sender> senderList){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);

        for(Sender sender: senderList){
            update = "insert into sender_" + userId + " values (" +
                    "null, " +
                    "'" + sender.getSender() + "',"+
                    sender.getName() + ")";

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
    }

    //添加被检测人（通过用户Id唯一确定）
    public int addSenderByUserId(int userId, Map<String, Object> sender){
        int result = 0;
        Connect myconn = new Connect();
        String update = null;

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);

        update = "insert into sender_" + userId + " values (" +
                "null, " +
                "'" + sender.get("sender") + "',"+
                "'" + sender.get("name") + "')";



        try {
            result = myconn.getStmt().executeUpdate(update);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            myconn.utils();
        }
        System.out.println("update:" + update);
        System.out.println("addSenderByUserId  result:" + result);
        return result;
    }

    //获取被检测人表（通过userName）
    public List<Sender> findAllSenders(String userName ){
        Connect myconn = new Connect();
        List<Sender> senderList = new ArrayList<Sender>();

        userManager usermanager = new userManager();
        int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from sender_" + userId + ""));
            Sender sender = null;

            while(myconn.getRs().next()){
                sender = new Sender();
                sender.setId(myconn.getRs().getInt("id"));
                sender.setSender(myconn.getRs().getString("sender"));
                sender.setName(myconn.getRs().getString("name"));
                senderList.add(sender);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return senderList;
    }

    //获取被检测人表（通过userId）
    public List<Map<String, Object>> findAllSendersByUserId(int userId){
        Connect myconn = new Connect();
        List<Map<String, Object>> senderList = new ArrayList<Map<String, Object>>();

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);
        try {
            myconn.setRs(myconn.getStmt().executeQuery("select * from sender_" + userId + ""));
            Map<String, Object> map = null;

            while(myconn.getRs().next()){
                map = new HashMap<String, Object>();
                map.put("id", myconn.getRs().getInt("id"));//target_id
                map.put("x", myconn.getRs().getString("sender"));//target_name
                senderList.add(map);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        return senderList;
    }

    //获取被检测人Id（通过userId和sender）
    public int findSenderIdByUserId(int userId, String sender){
        Connect myconn = new Connect();
        int sender_id = 0;

        //userManager usermanager = new userManager();
        //int userId = usermanager.findUserByUserName(userName);
        String sql = "select * from sender_" + userId + " where sender = '"+ sender + "'";
        System.out.println("findSenderIdByUserId:  " + sql);
        try {
            myconn.setRs(myconn.getStmt().executeQuery(sql));

            while(myconn.getRs().next()){
                sender_id = myconn.getRs().getInt("id");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            myconn.utils();
        }
        System.out.println("findSenderIdByUserId   sender_id :  " + sender_id);
        return sender_id;
    }
}
