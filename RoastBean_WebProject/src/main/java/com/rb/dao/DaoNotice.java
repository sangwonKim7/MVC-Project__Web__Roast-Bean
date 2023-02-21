package com.rb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.rb.dto.DtoNotice;

public class DaoNotice {

	// Fields
	DataSource dataSource;

	// Constructor
	public DaoNotice() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/roastbean");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method
	
	// Date: 2022-11-12, SangwonKim
	// Desc: 공지사항 리스트 가져오기
	public ArrayList<DtoNotice> noticeList() {
		ArrayList<DtoNotice> dtos = new ArrayList<DtoNotice>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			String query1 = "SELECT @rownum := @rownum + 1 rownumber, notice_write_seq, notice_write_title, ";
			String query2 = "notice_write_content, notice_write_initdate, notice_write_updatedate ";
			String query3 = "FROM notice_write WHERE(@rownum := 0) = 0 and notice_write_deletedate is null ";
			preparedStatement = connection.prepareStatement(query1 + query2 + query3);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int rownumber = resultSet.getInt("rownumber");
				int notice_write_seq = resultSet.getInt("notice_write_seq");
				String notice_write_title = resultSet.getString("notice_write_title");
				String notice_write_content = resultSet.getString("notice_write_content");
				String notice_write_initdate = resultSet.getString("notice_write_initdate");
				String notice_write_updatedate = resultSet.getString("notice_write_updatedate");

				DtoNotice dto = new DtoNotice(rownumber, notice_write_seq, notice_write_title, notice_write_content,
						notice_write_initdate, notice_write_updatedate);
				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dtos;
	} // noticList

	public void insertAction(String admin_id, String notice_write_title, String notice_write_content) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();

			String query = "insert into notice_write (admin_id, notice_write_title, notice_write_content, notice_write_initdate) values (?,?,?,now())";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, admin_id);
			preparedStatement.setString(2, notice_write_title);
			preparedStatement.setString(3, notice_write_content);

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // write

	public void updateAction(String notice_write_title, String notice_write_content, String notice_write_seq) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();

			String query = "update notice_write set notice_write_title = ?, notice_write_content = ?, notice_write_updatedate = now() where notice_write_seq = ? ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, notice_write_title);
			preparedStatement.setString(2, notice_write_content);
			preparedStatement.setString(3, notice_write_seq);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	} // modify

	public void updateActionDelete(String notice_write_seq) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();

			String query = "update notice_write set notice_write_deletedate = now() where notice_write_seq = ? ";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, notice_write_seq);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	} // modify

	//

	/// -------------------------------------------------------Hosik-----------------------------------------------------------------------------//

	public int sum_today_community() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		int result = 0;
		try {
			connection = dataSource.getConnection();
			String query = "select count(community_initdate) as count_community from community WHERE DATE_FORMAT (community_initdate, '%Y-%m-%d') = curdate();";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt("count_community");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}// order_week_sum

}// class end
