# システム開発で意識した点・工夫した点

得点管理システム開発で**実装の際に意識した点**と**設計上の工夫**をまとめました。  
面接や発表で聞かれた際の回答ポイントとなります。

---

## 目次

1. [MVC アーキテクチャの実装](#mvc-アーキテクチャの実装)
2. [セキュリティ対策](#セキュリティ対策)
3. [バリデーション・エラーハンドリング](#バリデーションエラーハンドリング)
4. [データベース設計の工夫](#データベース設計の工夫)
5. [DAO パターンの活用](#dao-パターンの活用)
6. [画面設計・UX の工夫](#画面設計ux-の工夫)
7. [チーム開発での工夫](#チーム開発での工夫)
8. [生成AI を活用した開発](#生成ai-を活用した開発)

---

## MVC アーキテクチャの実装

### 🎯 意識した点

**MVC パターンを徹底的に分離**することで、**可読性**と**保守性**を確保しました。

### 📊 実装構成

```
Model（Bean）     : データ構造
  ↓
Action（Controller）: ビジネスロジック
  ↓
JSP（View）        : 画面表示
  ↓
DAO（DataAccess）  : DB 操作
```

### ✅ 具体的な分離

| レイヤー | 責任 | 例 |
|---------|------|-----|
| **Bean** | データ保持のみ | Student.java（getter/setter） |
| **Action** | ロジック実行 | StudentCreateExecuteAction（登録処理） |
| **DAO** | DB 操作 | StudentDao.java（SQL 実行） |
| **JSP** | 画面表示 | student_create.jsp（フォーム表示） |

### 💡 メリット

✅ **コードの再利用性向上** - 同じ Bean を複数の Action で使用可能  
✅ **テストが容易** - 各レイヤーを独立してテスト可能  
✅ **修正箇所が限定される** - 仕様変更時の影響範囲が明確  

### 📝 回答例

> 「MVC パターンに従うことで、Model・View・Controller を完全に分離しました。
> 例えば、学生情報を新規登録する場合、Bean で学生データを保持し、Action で入力値チェックと登録処理を行い、JSP で画面を表示しています。
> この分離により、後から画面デザインを変更する場合、JSP だけを修正すれば済むようになっています。」

---

## セキュリティ対策

### 🎯 意識した点

**4層のセキュリティ対策**を実装しました。

### 1️⃣ セッション管理（ログイン認証）

**すべての Action で共通して実装**

```java
// 正しい実装
HttpSession session = req.getSession(false);  // ★ false が重要
if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

// セッションから Teacher を取得
Teacher teacher = (Teacher) session.getAttribute("user");
if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

**重要ポイント**:
- `getSession(false)` → 既存セッションのみ取得（新規作成しない）
- ダブルチェック → セッションの存在と Teacher の存在を確認
- セッション再生成 → ログイン時に古いセッションを破棄

### 2️⃣ マルチテナント対応

```java
// 学校を指定することで、他校のデータアクセスを防止
StudentDao dao = new StudentDao();
List<Student> students = dao.filter(teacher.getSchool());
```

**効果**: ログイン先の学校のみのデータが表示される

### 3️⃣ 入力値バリデーション

```java
Map<String, String> errors = new HashMap<>();

// 必須項目チェック
if (studentNo == null || studentNo.isEmpty()) {
    errors.put("student_no", "学生番号を入力してください");
}

// 形式チェック（英数字のみ）
if (!classNum.matches("^[A-Za-z0-9]+$")) {
    errors.put("class_num", "英数字で入力してください");
}

// 重複チェック
if (studentDao.get(studentNo, teacher.getSchool()) != null) {
    errors.put("student_no", "この学生番号は既に登録されています");
}
```

**多層的なチェック**:
1. ✅ null / 空文字列チェック
2. ✅ 形式チェック（正規表現）
3. ✅ 文字数チェック
4. ✅ 重複チェック（DB 確認）

### 4️⃣ POST-Redirect-Get パターン

```
ユーザーが「登録」ボタン
  ↓
POST リクエスト（StudentCreateExecuteAction）
  ↓
DB に登録
  ↓
REDIRECT リダイレクト（完了画面へ）← ★重要
  ↓
GET リクエスト（student_create_done.jsp）
```

**メリット**: 
- ブラウザの戻るボタンでも再送信されない
- 二重登録を防止

### 📝 回答例

> 「セキュリティ対策は 4段階で実装しています。
> 1 つ目は**セッション管理**で、すべての Action でログイン確認をしています。
> 2 つ目は**マルチテナント対応**で、teacher.getSchool() でアクセス可能な学校を制限しています。
> 3 つ目は**入力値バリデーション**で、必須項目・形式・重複の 3 点をチェックしています。
> 4 つ目は**POST-Redirect-Get**で、ブラウザ再送信による二重登録を防いでいます。」

---

## バリデーション・エラーハンドリング

### 🎯 意識した点

**ユーザーフレンドリーなエラーメッセージ**を表示し、ユーザーが迷わないようにしました。

### エラーの分類と対応

```
入力値エラー
  ├─ 未入力エラー       → 「〇〇を入力してください」
  ├─ 形式エラー         → 「〇〇は英数字で入力してください」
  ├─ 文字数エラー       → 「〇〇は 3 文字で入力してください」
  └─ 重複エラー         → 「この〇〇は既に登録されています」

システムエラー
  └─ DB 接続失敗       → 「システムエラーが発生しました」
```

### 実装パターン

```java
Map<String, String> errors = new HashMap<>();

// バリデーション
if (entYear == 0) {
    errors.put("ent_year", "入学年度を選択してください");
}
if (studentNo == null || studentNo.isEmpty()) {
    errors.put("student_no", "学生番号を入力してください");
}

// エラーがあれば入力画面に戻す
if (!errors.isEmpty()) {
    req.setAttribute("errors", errors);           // エラーメッセージ
    req.setAttribute("ent_year", entYear);        // 入力値を保持
    req.setAttribute("student_no", studentNo);
    req.getRequestDispatcher(
        "/scoremanager/main/student_create.jsp"
    ).forward(req, res);
    return;
}

// エラーなければ登録
Student student = new Student();
student.setNo(studentNo);
studentDao.save(student);
```

### JSP での表示例

```jsp
<!-- エラーメッセージ表示 -->
<c:if test="${not empty errors}">
    <div class="alert alert-danger">
        <c:forEach var="error" items="${errors.values()}">
            <p>${error}</p>
        </c:forEach>
    </div>
</c:if>

<!-- 入力値を保持 -->
<input type="text" name="student_no" 
       value="${student_no}" class="form-control">
```

### ✅ 工夫のポイント

| 工夫 | 効果 |
|------|------|
| **複数項目のエラー同時表示** | ユーザーが一度にすべてを確認できる |
| **入力値の保持** | ユーザーが修正時に再入力の手間を省ける |
| **わかりやすいメッセージ** | ユーザーが何を修正すべきか明確 |
| **色分け（赤）** | 視覚的にエラーが目立つ |

### 📝 回答例

> 「エラーハンドリングでは、ユーザーの使いやすさを重視しました。
> 複数のエラーがある場合は、すべてを同時に表示して、ユーザーが 1 回の修正で対応できるようにしました。
> また、エラー時にも入力値を保持することで、ユーザーの手間を減らしています。
> メッセージも『学生番号を入力してください』など、何をすべきかが明確になるように工夫しました。」

---

## データベース設計の工夫

### 🎯 意識した点

**マルチテナント対応のデータベース設計**を意識しました。

### 設計の特徴

#### 1️⃣ 学校 ID による分離

すべてのテーブルに `school_cd`（学校コード）を含める

```
STUDENT テーブル
  - student.no（学生番号）
  - student.name（学生名）
  - student.school_cd ← 学校コード（重要！）

CLASS_NUM テーブル
  - class_num.cd（クラス番号）
  - class_num.school_cd ← 学校コード
```

**効果**:
```sql
-- 学校 A の学生のみを取得
SELECT * FROM STUDENT 
WHERE school_cd = 'A' AND name LIKE '%田中%';

-- 他校のデータは絶対に見えない
```

#### 2️⃣ 外部キー制約

```
CLASS_NUM → STUDENT に対して外部キー制約
```

**効果**:
- クラスを削除したとき、自動的に関連する学生情報も削除
- 不整合なデータの登録を防止

#### 3️⃣ クラス変更時の対応

クラス番号が「A」から「B」に変更された場合：

```java
public boolean save(ClassNum classNum, String newClassNum) throws Exception {
    // 3つのテーブルを一括更新
    // ① CLASS_NUM テーブル更新
    // ② STUDENT テーブル更新（class_num = 'B'）
    // ③ TEST テーブル更新（class_num = 'B'）
}
```

**メリット**: トランザクション内で整合性を保証

### 📝 回答例

> 「データベース設計では、マルチテナント対応を重視しました。
> すべてのテーブルに学校コードを含めることで、複数の学校が同一システムを使用する場合も、データが混在しないようにしています。
> また、クラス番号が変更された場合、STUDENT と TEST テーブルも同時に更新することで、データの整合性を保つようにしました。」

---

## DAO パターンの活用

### 🎯 意識した点

**DAO パターン**を導入することで、データベース操作を統一管理しました。

### DAO の役割

```java
public class StudentDao extends Dao {
    
    // データ取得系
    public Student get(String no, School school) { }
    public List<Student> filter(School school) { }
    public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) { }
    
    // データ更新系
    public boolean save(Student student) { }          // 新規登録
    public boolean update(Student student) { }        // 更新
    public boolean delete(Student student) { }        // 削除
}
```

### データ接続プールの活用

```java
public class Dao {
    static DataSource ds;  // クラス変数で保持
    
    public Connection getConnection() throws Exception {
        // 最初の 1 回だけ JNDI から取得
        if (ds == null) {
            InitialContext ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:/comp/env/jdbc/exam");
        }
        // 以降は同じ DataSource を使い回す
        return ds.getConnection();
    }
}
```

### ✅ メリット

| メリット | 説明 |
|---------|------|
| **コード再利用** | 複数の Action から同じ DAO メソッドを呼び出し可能 |
| **接続プール管理** | DB 接続を効率的に管理、パフォーマンス向上 |
| **保守性向上** | SQL の変更が必要な場合、DAO だけを修正 |
| **テスト容易性** | Mock DAO を用いたテストが可能 |

### try-with-resources による自動クローズ

```java
try (Connection connection = getConnection();
     PreparedStatement statement = connection.prepareStatement(sql)) {
    
    statement.setString(1, no);
    statement.setString(2, school.getCd());
    
    try (ResultSet rSet = statement.executeQuery()) {
        if (rSet.next()) {
            student = new Student();
            student.setNo(rSet.getString("no"));
        }
    }
} catch (SQLException e) {
    throw e;
}
```

**メリット**: 例外が発生しても自動的にリソースがクローズされる

### 📝 回答例

> 「DAO パターンを導入することで、データベース操作を一元管理しました。
> 例えば、StudentDao では学生情報に関するすべての SQL 操作（取得・登録・更新・削除）が集約されています。
> これにより、複数の Action から同じメソッドを再利用でき、SQL の変更が必要な場合も DAO だけを修正すれば済むようになっています。
> また、接続プールの管理も DAO に集約することで、パフォーマンスと保守性が向上しています。」

---

## 画面設計・UX の工夫

### 🎯 意識した点

**ユーザーが迷わない画面設計**と**効率的な入力フロー**を目指しました。

### 1️⃣ 一覧画面での検索機能

```
入学年度  ▼ [2024年]
クラス    ▼ [A]
在籍状態  ▼ [在籍中]

[検索] ボタン
```

**工夫**:
- ✅ ドロップダウンで選択肢を限定（入力ミスを防止）
- ✅ 複合検索対応（入学年度 + クラス + 在籍状態）
- ✅ 検索結果に該当なしの場合も表示

### 2️⃣ 登録画面での入力補助

```
入学年度  ▼ [2024年]          ← プルダウン（選択肢から選ぶ）
学生番号  [___________]      ← テキスト入力
学生名    [___________]      ← テキスト入力
クラス    ▼ [A]              ← プルダウン（登録済みクラスのみ表示）

[登録] [キャンセル]
```

**工夫**:
- ✅ 手動入力と選択肢の適切な分け方
- ✅ クラスは登録済みのもののみ表示（整合性確保）
- ✅ キャンセルボタンで元に戻す

### 3️⃣ 確認画面の実装

```
登録内容確認
─────────────────
入学年度: 2024年
学生番号: 001
学生名:   田中太郎
クラス:   A

[確認] [修正に戻る]
```

**メリット**:
- ✅ ユーザーが登録前に内容確認できる
- ✅ 送信ボタンの誤クリック防止

### 4️⃣ エラー表示

```
エラーが発生しました

✗ 学生番号を入力してください
✗ この学生番号は既に登録されています

[修正画面に戻る]
```

**工夫**:
- ✅ 赤色で目立たせる
- ✅ 複数エラーは箇条書き
- ✅ 原因が明確なメッセージ

### 5️⃣ Bootstrap による UI 統一

```html
<div class="alert alert-danger">エラーメッセージ</div>
<button class="btn btn-primary">登録</button>
<button class="btn btn-secondary">キャンセル</button>
```

**効果**:
- ✅ 統一された色使い
- ✅ ボタンの大きさ・色で優先度を表現
- ✅ レスポンシブ対応（スマートフォンでも見やすい）

### 📝 回答例

> 「画面設計では、ユーザーの迷いを減らすことを重視しました。
> 例えば、入学年度やクラスはプルダウンにして、選択肢を限定することで入力ミスを防ぎました。
> また、確認画面を設けることで、ユーザーが登録前に内容を確認できるようにしました。
> さらに Bootstrap を使用して画面全体の統一感を出し、ボタンの色で『登録』『キャンセル』の優先度を表現しています。」

---

## チーム開発での工夫

### 🎯 意識した点

**4 人チームで効率的に開発するため、役割分担と統一基準を重視しました。**

### WBS（Work Breakdown Structure）による役割分担

```
プロジェクト
├─ 学生管理機能        ← 自分が担当
│  ├─ StudentDao
│  ├─ StudentBean
│  ├─ StudentListAction
│  ├─ StudentCreateAction
│  ├─ StudentUpdateAction
│  └─ JSP (student_*.jsp)
│
├─ クラス管理機能      ← 自分が追加機能として実装
│  └─ ClassNum 関連 (DAO / Bean / Action / JSP)
│
├─ 科目管理機能
└─ 成績登録機能
```

### 統一基準の設定

#### 1️⃣ ファイル名・パッケージ規則

```
Bean:     bean/StudentBeans/ClassNum.java
Action:   scoremanager/main/StudentCreateAction.java
DAO:      dao/StudentDao.java
JSP:      scoremanager/main/student_create.jsp
```

**効果**: ファイルの場所が予測可能

#### 2️⃣ メソッド命名規則

```java
// 一覧取得
public List<Student> filter(School school) { }

// 1件取得
public Student get(String no, School school) { }

// 新規登録
public boolean save(Student student) { }

// 更新
public boolean update(Student student) { }

// 削除
public boolean delete(Student student) { }
```

**効果**: どの DAO でも同じメソッド名で操作できる

#### 3️⃣ エラーメッセージの統一

```
▼ 学生管理
「学生番号を入力してください」
「学生名を入力してください」

▼ クラス管理
「クラス番号を入力してください」
「クラス名を入力してください」

パターン: 「〇〇を入力してください」
```

**効果**: ユーザーが統一感を感じ、使いやすくなる

#### 4️⃣ Action での処理フロー統一

```java
// すべての Action で同じ流れ
1. ログインチェック
2. パラメータ取得
3. バリデーション
4. 存在確認
5. DB 操作
6. 完了画面へ
```

**効果**: コードレビューが容易、保守性向上

### ドキュメント作成による情報共有

```
Student_Management_Complete_Manual.md
├─ StudentDao の API 仕様
├─ bean.Student の使い方
├─ Action での処理フロー
├─ エラーメッセージ一覧
└─ 画面遷移図
```

**メリット**:
- ✅ チームメンバーが仕様を即座に確認可能
- ✅ 新しいメンバーのオンボーディングが早い
- ✅ 修正時の影響範囲が明確

### 📝 回答例

> 「チーム開発では、WBS を使用して役割分担を明確にしました。
> 自分は学生管理機能を担当し、クラス管理を追加機能として実装しました。
> チーム内で統一基準を設けることで、各自が実装した機能が自然と統合できるようにしました。
> 例えば、メソッド名や処理フロー、エラーメッセージなどを統一することで、
> 後からコードを読むときの混乱を減らしています。」

---

## 生成AI を活用した開発

### 🎯 意識した点

**生成AI を効果的に活用して、開発効率と品質を向上させました。**

### 活用シーン 1: エラーコードの調査

**状況**: 実行時に`SQLException`が発生

```
例外メッセージ:
java.sql.SQLException: 
Syntax error in SQL statement 
"SELECT * FROM STUDENT WHERE student_no = ?"
```

**活用**:
```
生成AI へ質問:
「このエラーが出ています。原因は何ですか？」

→ AI の回答:
「カラム名や SQL の構文に誤りがある可能性があります。
テーブル定義とクエリを確認してください。」

→ 原因特定:
テーブルの実際のカラム名は「NO」だった（大文字）
```

**効果**: 
- ✅ デバッグ時間を大幅短縮
- ✅ 原因特定の精度向上

### 活用シーン 2: バリデーションコードの生成

**依頼内容**:
```
正規表現を使って「英数字のみ」をチェックするコードを書いてください
```

**生成 AI の出力**:
```java
if (!input.matches("^[A-Za-z0-9]+$")) {
    errors.put("key", "英数字で入力してください");
}
```

**活用方法**:
- ✅ 出力されたコードを確認・修正
- ✅ 複数パターンのコードから最適なものを選択

**効果**:
- ✅ コーディング時間短縮
- ✅ バグの可能性低減

### 活用シーン 3: JSP コンポーネントの生成

**依頼内容**:
```
Bootstrap を使ったエラー表示部を作成してください。
複数エラーを赤色で箇条書き表示してください。
```

**生成 AI の出力**:
```jsp
<c:if test="${not empty errors}">
    <div class="alert alert-danger" role="alert">
        <h5 class="alert-heading">エラーが発生しました</h5>
        <ul>
        <c:forEach var="error" items="${errors.values()}">
            <li>${error}</li>
        </c:forEach>
        </ul>
    </div>
</c:if>
```

**活用方法**:
- ✅ デザインの参考にする
- ✅ 出力されたコードを微調整

### 活用シーン 4: ドキュメント作成の効率化

**活用内容**:
```
ActionClass の説明
  ↓
生成 AI で骨組み生成
  ↓
詳細は手動で修正・追加
```

**例**:
- 自動生成: クラスの責任、メソッド一覧
- 手動編集: 実装上の工夫、トラブル時の対応

**効果**:
- ✅ ドキュメント作成時間を 50% 短縮
- ✅ 品質を保ったままスピードアップ

### ❌ AI を使わなかった理由

以下は AI に頼らず**自分で実装**しました：

| 項目 | 理由 |
|------|------|
| **ビジネスロジック** | システムの仕様理解が必須 |
| **バリデーションルール決定** | 要件定義段階での検討が必要 |
| **ファイル構成・アーキテクチャ** | チーム全体で統一基準が必要 |
| **セキュリティ実装** | 脆弱性の責任は自分たちにある |

### 📝 回答例

> 「生成AI は、ルーチン的な作業効率化のために活用しました。
> 例えば、エラーが出たときの原因特定や、バリデーションコードの生成などです。
> これにより開発時間を短縮しつつ、品質も維持できました。
> 一方、ビジネスロジックやセキュリティに関わる部分は、自分たちで仕様を理解した上で実装しています。
> AI は『効率化の道具』として使い、『最終的な責任は自分たち』という意識を持ちながら開発しました。」

---

## まとめ・面接対策チェックリスト

### 🎯 聞かれやすい質問と回答ポイント

| 質問 | 回答ポイント | 参考箇所 |
|------|-----------|--------|
| このシステムの特徴は？ | MVC パターン、セキュリティ対策、バリデーション | [MVC アーキテクチャ](#mvc-アーキテクチャの実装) |
| セキュリティで工夫した点は？ | ログイン認証、マルチテナント、バリデーション、POST-Redirect-Get | [セキュリティ対策](#セキュリティ対策) |
| エラーハンドリングはどう実装した？ | 複数エラー同時表示、入力値保持、わかりやすいメッセージ | [バリデーション](#バリデーションエラーハンドリング) |
| DB 設計で工夫した点は？ | マルチテナント対応、学校 ID による分離、外部キー制約 | [DB 設計](#データベース設計の工夫) |
| DAO パターンを使った理由は？ | コード再利用、保守性向上、接続プール管理 | [DAO パターン](#dao-パターンの活用) |
| 画面設計で工夫した点は？ | 検索機能、確認画面、エラー表示、Bootstrap 統一 | [画面設計](#画面設計ux-の工夫) |
| チーム開発で工夫した点は？ | WBS、統一基準、ドキュメント作成 | [チーム開発](#チーム開発での工夫) |
| AI をどう活用した？ | エラー調査、コード生成、効率化（責任は自分たち） | [生成AI](#生成ai-を活用した開発) |

### ✅ 回答時の心得

1. **具体例を挙げる** - 「〇〇の機能で△△を工夫しました」
2. **技術用語を正確に** - MVC、DAO、セッション管理など
3. **なぜそうしたのか理由を述べる** - 「保守性が向上するため」
4. **困ったことと解決方法を話す** - 成長の姿勢をアピール
5. **生成 AI は効率化の道具として** - 自分の理解・責任を強調

---

## 参考資料

- 📄 `Student_Management_Complete_Manual.md` - 学生管理機能の詳細
- 📄 `ClassManagement_Complete_Manual.md` - クラス管理機能の詳細
- 📄 `Other_Features_Manual.md` - その他機能（科目・テスト）の詳細
- 📁 `src/main/java/dao/` - DAO の実装例
- 📁 `src/main/java/scoremanager/` - Action の実装例
- 🌐 `README.md` - プロジェクト概要
