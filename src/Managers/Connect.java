package Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Utils.DBUtils;

public class Connect {
	
	private String connaddr = "jdbc:mysql://localhost:3306/chatdetection?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	private String connname = "root";
//	private String connpassword = "root" ;
	private String connpassword = "lengaoLA66" ;

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	//���캯��������
	Connect(){
		
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
	try {
		conn = DriverManager.getConnection(connaddr,connname,connpassword);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
	System.out.println("conn:"+conn);
	try {
		stmt = conn.createStatement();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
	
	}
	
	//�ͷ�����
	public void utils(){
		DBUtils.release(rs,stmt,conn);
	}
}
