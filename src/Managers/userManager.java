package Managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tables.User;

public class userManager {
	
	//获取整个用户表
	public List<User> findAllUsers(){
		Connect myconn = new Connect();
		List<User> userList = new ArrayList<User>();
		
		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from user"));
			User user = null;
			
			while(myconn.getRs().next()){
				user = new User();
				user.setId(myconn.getRs().getInt("id"));
				user.setAccount(myconn.getRs().getString("account"));
				user.setPassword(myconn.getRs().getInt("password"));
				user.setIs_admin(myconn.getRs().getInt("is_admin"));
				userList.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return userList;
	}
	
	//获取用户信息（by id）
	public User findUserById(int userId){
			Connect myconn = new Connect();
			User user = null;
			
			try {
				myconn.setRs(myconn.getStmt().executeQuery("select * from user where id = '" + userId + "'"));
				
				while(myconn.getRs().next()){
					user = new User();
					user.setId(myconn.getRs().getInt("id"));
					user.setAccount(myconn.getRs().getString("account"));
					user.setPassword(myconn.getRs().getInt("password"));
					user.setIs_admin(myconn.getRs().getInt("is_admin"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				myconn.utils();
			}
			return user;
		}
	
	//添加用户
	public int addUser(User user){
		int result = 0;
		Connect myconn = new Connect();
		
		String update = "insert into user values ("+
						"null, " +
						"'"+ user.getAccount() + "',"+
						user.getPassword() + ","+
						user.getIs_admin() + ")";

		String finduserId = "select * from user order by id desc limit 1";

		int userId;
		groupManager groupmanager = new groupManager();
		keywordManager keywordmanager = new keywordManager();
		recordManager recordmanager = new recordManager();
		senderManager sendermanager = new senderManager();
		keywordcounterManager keywordcountermanager = new keywordcounterManager();
		wordcounterManager wordcountermanager = new wordcounterManager();

		//System.out.println("update: "+update);
		
		try {
			result = myconn.getStmt().executeUpdate(update);
			myconn.setRs(myconn.getStmt().executeQuery(finduserId));
			while(myconn.getRs().next()){
				userId = myconn.getRs().getInt("id");
				result = groupmanager.createGroupTableByUserId(userId);
				result = keywordmanager.createKeywordTableByUserId(userId);
				result = recordmanager.createRecordTableByUserId(userId);
				result = sendermanager.createSenderTableByUserId(userId);
				result = keywordcountermanager.createKeywordCounterTableByUserId(userId);
				result = wordcountermanager.createWordCounterTableByUserId(userId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return result;
	}
	
	//修改用户密码
	public int changePasswordByuserName(String userName, int newPwd){
		int result = 0;
		Connect myconn = new Connect();
		
		String update = "update user set password = '" + newPwd +"'"
						+ "where account = " + userName;
		
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
	
	//删除用户记录（通过 id 号)
	public int deleteUser(String userName){
		
		int result = 0;
		Connect myconn = new Connect();
		try {
			result = myconn.getStmt().executeUpdate("delete from user where account = '"+userName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			myconn.utils();
		}
		return result;
	}

	//获取用户Id（通过用户名）
	public int findUserByUserName(String  userName){
		Connect myconn = new Connect();
		int userId = 0;

		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from user where account = '" + userName + "'"));

			while(myconn.getRs().next()){
				userId = myconn.getRs().getInt("id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return userId;
	}

	//获取用户Id（通过用户名和密码）
	public Map<String, String> findUserByUserNameAndPassword(String userName, int password){
		Connect myconn = new Connect();
		Map<String, String> map = null;

		try {
			myconn.setRs(myconn.getStmt().executeQuery("select * from user where account = '" + userName + "' and  "+
					"password = " + password + ""));

			//System.out.println("findUserByUserNameAndPassword: no");

			while(myconn.getRs().next()){
				//System.out.println("findUserByUserNameAndPassword: yes");
				map = new HashMap<String, String>();
				map.put("id", myconn.getRs().getInt("id") + "");
				map.put("is_admin", myconn.getRs().getInt("is_admin") + "");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			myconn.utils();
		}
		return map;
	}
	
}
