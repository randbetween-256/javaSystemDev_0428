package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {

    /**
     * 1件取得
     */
    public ClassNum get(String class_num, School school) throws Exception {

        ClassNum classNum = null;

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            statement = connection.prepareStatement(
                "select * from class_num where class_num = ? and school_cd = ?"
            );

            statement.setString(1, class_num);
            statement.setString(2, school.getCd());

            rSet = statement.executeQuery();

            if (rSet.next()) {
                classNum = new ClassNum();
                classNum.setClassNum(rSet.getString("class_num"));
                classNum.setSchool(school);
            }

        } catch (Exception e) {
            throw e;

        } finally {
            if (rSet != null) try { rSet.close(); } catch (SQLException e) { throw e; }
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return classNum;
    }

    /**
     * 学校ごとのクラス一覧取得
     */
    public List<String> filter(School school) throws Exception {

        List<String> list = new ArrayList<>();

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            statement = connection.prepareStatement(
                "select class_num from class_num where school_cd=? order by class_num"
            );

            statement.setString(1, school.getCd());

            rSet = statement.executeQuery();

            while (rSet.next()) {
                list.add(rSet.getString("class_num"));
            }

        } catch (Exception e) {
            throw e;

        } finally {
            if (rSet != null) try { rSet.close(); } catch (SQLException e) { throw e; }
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return list;
    }

    /**
     * 新規登録
     */
    public boolean save(ClassNum classNum) throws Exception {

        Connection connection = getConnection();
        PreparedStatement statement = null;

        int count = 0;

        try {
            statement = connection.prepareStatement(
                "insert into class_num(school_cd, class_num) values(?, ?)"
            );

            statement.setString(1, classNum.getSchool().getCd());
            statement.setString(2, classNum.getClassNum());

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;

        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return count > 0;
    }

    /**
     * クラス番号変更（class_num / student / test を一括更新）
     */
    public boolean save(ClassNum classNum, String newClassNum) throws Exception {

        Connection connection = getConnection();
        PreparedStatement statement = null;

        int count = 0;

        try {
            connection.setAutoCommit(false);

            // class_num 更新
            statement = connection.prepareStatement(
                "update class_num set class_num = ? where school_cd = ? and class_num = ?"
            );
            statement.setString(1, newClassNum);
            statement.setString(2, classNum.getSchool().getCd());
            statement.setString(3, classNum.getClassNum());
            count += statement.executeUpdate();
            statement.close();

            // student 更新
            statement = connection.prepareStatement(
                "update student set class_num = ? where school_cd = ? and class_num = ?"
            );
            statement.setString(1, newClassNum);
            statement.setString(2, classNum.getSchool().getCd());
            statement.setString(3, classNum.getClassNum());
            statement.executeUpdate();
            statement.close();

            // test 更新
            statement = connection.prepareStatement(
                "update test set class_num = ? where school_cd = ? and class_num = ?"
            );
            statement.setString(1, newClassNum);
            statement.setString(2, classNum.getSchool().getCd());
            statement.setString(3, classNum.getClassNum());
            statement.executeUpdate();

            connection.commit();

        } catch (Exception e) {

            if (connection != null) {
                try { connection.rollback(); } catch (SQLException sqle) { throw sqle; }
            }

            throw e;

        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return count > 0;
    }

    /**
     * クラス削除（class_num のみ削除）
     */
    public boolean delete(ClassNum classNum) throws Exception {

        Connection connection = getConnection();
        PreparedStatement statement = null;

        int count = 0;

        try {
            statement = connection.prepareStatement(
                "delete from class_num where school_cd=? and class_num=?"
            );

            statement.setString(1, classNum.getSchool().getCd());
            statement.setString(2, classNum.getClassNum());

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;

        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return count > 0;
    }
}
