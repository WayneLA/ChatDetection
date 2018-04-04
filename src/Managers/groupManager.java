package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tables.Group;
import Tables.User;

public class groupManager {
	
	//创建群组表（通过userId）
	public int createGroupTableByUserId(int userId){
		int result = 0;
		Connect myconn = new Connect();
		String create = "create table group_"+ userId +"(" +
						"id int NOT NULL AUTO_INCREMENT, " +
						"PRIMARY KEY(id), " +
						"sid VARCHAR(20) NOT NULL, " +
						"name text NOT NULL, " +
						"auto_upload int NOT NULL )";


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
	
	//获取自动上传群组表
	public List<Map<String, Object>> getAutouploadGroups(int user_id){
		
		Connect myconn = new Connect();
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		
		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from group_"+ user_id +" where auto_upload = 1"));
			Group group = null;
			Map<String, Object> groupMap = null;
			
			while(myconn.getRs().next()){
				/*group = new Group();
				group.setId(myconn.getRs().getInt("id"));
				group.setSid(myconn.getRs().getString("sid"));
				group.setName(myconn.getRs().getString("name"));
				group.setAuto_upload(myconn.getRs().getInt("auto_upload"));
				groupList.add(group);*/

				groupMap = new HashMap<String, Object>();
				groupMap.put("group", myconn.getRs().getString("sid"));
				groupMap.put("name", myconn.getRs().getString("name"));
				groupList.add(groupMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return groupList;
	}

	//添加群组（通过userId找到群组表）
	public int addGroupByUserId(int userId, String sid, String Name){
		int result = 0;
		Connect myconn = new Connect();
		String update = null;

		update = "insert into group_" + userId + " values (" +
				"null, " +
				"'" + sid + "',"
				+ "'" + Name + "',"
				+  " 1 )";

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

	//设置群组为自动上传或取消自动上传
	public int setAutouploadGroup(int userId, String groupName, int flag){
		int result = 0;
		Connect myconn = new Connect();

		//userManager usermanager = new userManager();
		//int userId = usermanager.findUserByUserName(userName);

		String update = "update group_"+ userId +" set auto_upload = " + flag +
						" where sid = " + groupName;

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

	//设置群组为自动上传或取消自动上传
	public int setAutouploadGroupById(int userId, int groupId, int flag){
		int result = 0;
		Connect myconn = new Connect();

		String update = "update group_"+ userId +" set auto_upload = " + flag +
				" where id = " + groupId;

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

	//获取群组表（通过userName）
	public List<Group> findAllGroups(String userName){
		Connect myconn = new Connect();
		List<Group> groupList = new ArrayList<Group>();

		userManager usermanager = new userManager();
		int userId = usermanager.findUserByUserName(userName);
		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from group_" + userId + ""));
			Group group = null;

			while(myconn.getRs().next()){
				group = new Group();
				group.setId(myconn.getRs().getInt("id"));
				group.setSid(myconn.getRs().getString("sid"));
				group.setName(myconn.getRs().getString("name"));
				group.setAuto_upload(myconn.getRs().getInt("auto_upload"));
				groupList.add(group);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return groupList;
	}

	//获取群组表中groupName（通过userId）
	public List<Map<String, Object>> findAllGroupByUserId(int userId){
		Connect myconn = new Connect();
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();

		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from group_" + userId + ""));

			Map<String, Object> map = null;
			while(myconn.getRs().next()){
				map = new HashMap<String, Object>();
				map.put("id", myconn.getRs().getInt("id"));  //target_id
				map.put("x", myconn.getRs().getString("sid"));//target_name
				//group.setAuto_upload(myconn.getRs().getInt("auto_upload"));
				groupList.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return groupList;
	}

	//获取群组表中groupId（通过userId）
	public int findGroupByUserId(int userId, String groupName, String name, int flag){
		Connect myconn = new Connect();
		int group_id = 0;
		String sql = "select * from group_" + userId + " where sid = '" +
				groupName + "'";
		//System.out.println("findGroupByUserId: "+ sql);
		try {
			myconn.setRs(myconn.getStmt().executeQuery(sql));


			while(myconn.getRs().next()){
				group_id = myconn.getRs().getInt("id");
				if(flag == 1 && (!name.equals(myconn.getRs().getString("name")))){  //为一才判断
					String update = "update group_"+ userId +" set name = '" + name +
							"' where id = " + group_id;
					int result = myconn.getStmt().executeUpdate(update);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		//System.out.println("findGroupByUserId: group_id   "+ group_id);
		return group_id;
	}








}
