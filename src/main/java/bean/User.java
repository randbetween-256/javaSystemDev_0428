package bean;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 認証済みフラグ
     * true : 認証済み
     * false : 未認証
     */
    private boolean authenticated;

    // ===== getter / setter =====

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * デバッグ用に User の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "User [authenticated=" + authenticated + "]";
    }
}
