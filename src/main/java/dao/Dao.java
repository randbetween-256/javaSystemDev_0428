package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Dao {

    /**
     * データソースを保持するクラス変数
     *
     * ・最初の1回だけ JNDI から取得し、以降は同じ DataSource を使い回す
     */
    static DataSource ds;

    /**
     * データベースへの接続(Connection) を取得して返すメソッド
     *
     * @return Connection DB への接続オブジェクト
     * @throws Exception
     */
    public Connection getConnection() throws Exception {

        // DataSource がまだ取得されていない場合のみ JNDI から探す
        if (ds == null) {

            // JNDI を利用するためのコンテキストを作成
            InitialContext ic = new InitialContext();

            // context.xml / web.xml に設定された DataSource を JNDI 名で取得
            // ここで DB 接続プールの実体が得られる
            ds = (DataSource) ic.lookup("java:/comp/env/jdbc/exam");
        }

        // DataSource から Connection を取得して返す
        // 実際には接続プールからコネクションを借りてくる動作になる
        return ds.getConnection();
    }
}
