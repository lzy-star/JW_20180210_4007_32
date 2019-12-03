package dao;


import domain.*;
import service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from teacher");
		while(resultSet.next()){
			Teacher teacher = new Teacher(
					resultSet.getInt("id"),
					resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleService.getInstance().find(resultSet.getInt("proftitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id")));
			teachers.add(teacher);
		}
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "SELECT * FROM teacher where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();

		Teacher teacher = new Teacher(
				resultSet.getInt("id"),
				resultSet.getString("name"),
				resultSet.getString("no"),

				ProfTitleService.getInstance().find(resultSet.getInt("proftitle_id")),
				DegreeService.getInstance().find(resultSet.getInt("degree_id")),
				DepartmentService.getInstance().find(resultSet.getInt("department_id"))
		);
		return teacher;
	}
	
	public void update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "UPDATE teacher SET name=?,no = ?,proftitle_id = ?,degree_id = ?,department_id=? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setString(1,teacher.getName());
		pstmt.setString(2,teacher.getNo());
		pstmt.setInt(3,teacher.getTitle().getId());
		pstmt.setInt(4,teacher.getDegree().getId());
		pstmt.setInt(5,teacher.getDepartment().getId());

		pstmt.setInt(6,teacher.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}
	
	public void add(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {

			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			String addTeacher_sql = "INSERT INTO teacher(name,no,proftitle_id,degree_id,department_id) VALUES" +
					" (?,?,?,?,?)";
			pstmt = connection.prepareStatement(addTeacher_sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, teacher.getName());
			pstmt.setString(2, teacher.getNo());
			pstmt.setInt(3,teacher.getTitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());
			int affectedRowNum = pstmt.executeUpdate();
			System.out.println("添加了 " + affectedRowNum +" 行记录");

			ResultSet resultSet = pstmt.getGeneratedKeys();
			resultSet.next();
			int teacherId = resultSet.getInt(1);
			teacher.setId(teacherId);
			System.out.println(teacher.getId());
			Date date_util = new Date();
			Long date_long = date_util.getTime();
			java.sql.Date date_sql = new java.sql.Date(date_long);

			User user = new User(
					teacher.getNo(),
					teacher.getNo(),
					date_sql,
					teacher
			);
			System.out.println(user.getTeacher().getId());
			UserService.getInstance().add(connection,user);


//			String addUser_sql = "INSERT INTO user(username,password,loginTime,teacher_id) VALUES" +
//					" (?,?,?,?)";
//			pstmt = connection.prepareStatement(addUser_sql);
//			pstmt.setString(1,teacher.getNo());
//			pstmt.setString(2,teacher.getNo());
//			pstmt.setDate(3,date_sql);
//			pstmt.setInt(4,teacherId);
//			int affectedRowNum1 = pstmt.executeUpdate();
//			System.out.println("添加了 " + affectedRowNum1 +" 行记录");


		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}

	}

	public void delete(Integer id) throws SQLException {
		Teacher teacher = this.find(id);
		this.delete(teacher);
	}
	
	public void delete(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {

			connection = JdbcHelper.getConn();
			String deleteUser_sql = "DELETE FROM user WHERE teacher_id = ?";
			pstmt = connection.prepareStatement(deleteUser_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum +" 行记录");

			String deleteTeacher_sql = "DELETE FROM teacher WHERE id = ?";
			pstmt = connection.prepareStatement(deleteTeacher_sql);
			pstmt.setInt(1,teacher.getId());
			int affectedRowNum1 = pstmt.executeUpdate();
			System.out.println("删除了 " + affectedRowNum1 +" 行记录");


		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\nerrorCode = " + e.getErrorCode());
			try {
				if (connection != null){
					connection.rollback();
				}
			} catch (SQLException e1){
				e.printStackTrace();
			}

		} finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(pstmt,connection);
		}
	}
	
	
	
}
