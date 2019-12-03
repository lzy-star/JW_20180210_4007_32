package dao;


import domain.Department;
import service.DepartmentService;
import service.SchoolService;
import util.JdbcHelper;
import util.ShowTable;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {
	private static Collection<Department> departments;
	static {

		departments = new TreeSet<Department>();
	}

	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}


	public Collection<Department> findAll() throws SQLException {
		Collection<Department> departments = new TreeSet<Department>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from department");
		while(resultSet.next()){
			System.out.print("id = " + resultSet.getInt("id"));
			System.out.print(",");
			System.out.print("no = " + resultSet.getString("no"));
			System.out.print(",");
			System.out.print("description = " + resultSet.getString("description"));
			System.out.print(",");
			System.out.print("remarks = " + resultSet.getString("remarks"));
			System.out.print(",");
			System.out.print("school_id = " + resultSet.getString("school_id"));
			System.out.println(".");
			Department department = new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id")));
			departments.add(department);
		}
		return departments;
	}
	    //定义一个有参数的方法，
	public Collection<Department> findAllBySchool(int schoolId) throws SQLException {
         //创建一个treeset(有序)，department类型的集合
		Collection<Department> departments = new TreeSet<Department>();
         //连接数据库
		Connection connection = JdbcHelper.getConn();
         //创建sql语句
		String findAllBySchool_sql = "SELECT * FROM department where school_id = ?";
        //创建预编译对象，并与sql语句连接
		PreparedStatement pstmt = connection.prepareStatement(findAllBySchool_sql);
        //给第一个参数赋值
		pstmt.setInt(1,schoolId);
        //执行预编译语句
		ResultSet resultSet = pstmt.executeQuery();
        //遍历
		while(resultSet.next()){
			Department department = new Department(
          //获得ID的值
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
                        //通过schoolID找到school对象
					SchoolService.getInstance().find(resultSet.getInt("school_id")));
           //将得到的department的添加到集合departments
			departments.add(department);
		}
		return departments;
	}

	public Department find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "SELECT * FROM department where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();

		Department department = new Department(resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks"),
				SchoolDao.getInstance().find(resultSet.getInt("school_id")));
		return department;
	}

	public void update(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "UPDATE department SET description = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setString(1,department.getDescription());
		pstmt.setInt(2,department.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}

	public void add(Department department) throws SQLException {
		departments.add(department);
		Connection connection = JdbcHelper.getConn();
		String addDegree_sql = "INSERT INTO department(id,description,no,remarks,school_id) VALUES" +
				" (?,?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		pstmt.setInt(1,department.getId());
		pstmt.setString(2, department.getDescription());
		pstmt.setString(3,department.getNo());
		pstmt.setString(4,department.getRemarks());
		pstmt.setInt(5,department.getSchool().getId());

		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
		ShowTable.showTable(pstmt);
		JdbcHelper.close(pstmt,connection);
	}

	public void delete(Integer id) throws SQLException {
		Department department = this.find(id);
		this.delete(department);
	}

	public void delete(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addDegree_sql = "DELETE FROM department WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		pstmt.setInt(1,department.getId());
		int affectedRowNum = pstmt.executeUpdate();
		departments.remove(department);
		System.out.println("删除了 " + affectedRowNum +" 行记录");
		ShowTable.showTable(pstmt);
		JdbcHelper.close(pstmt,connection);
	}

	public static void main(String[] args) throws SQLException {
		Department departmentToChange = DepartmentService.getInstance().find(1);
		departmentToChange.setDescription("管理");
		departmentDao.update(departmentToChange);
		Department departmentChanged = DepartmentService.getInstance().find(1);
		System.out.println(departmentChanged.getDescription());

	}
}

