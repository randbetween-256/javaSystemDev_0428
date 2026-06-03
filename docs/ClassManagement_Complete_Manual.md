# クラス管理機能 完全マニュアル

---

## 📖 目次

1. [ClassNum Bean クラス](#classnum-bean-クラス説明書)
2. [ClassNumDao クラス](#classnumdao-クラス説明書)
3. [ClassListAction クラス](#classlistaction-クラス説明書)
4. [ClassCreateAction クラス](#classcreateaction-クラス説明書)
5. [ClassCreateExecuteAction クラス](#classcreateexecuteaction-クラス説明書)
6. [ClassUpdateAction クラス](#classupdateaction-クラス説明書)
7. [ClassUpdateExecuteAction クラス](#classupdateexecuteaction-クラス説明書)
8. [ClassDeleteAction クラス](#classdeleteaction-クラス説明書)
9. [ClassDeleteExecuteAction クラス](#classdeleteexecuteaction-クラス説明書)

---

<div style="page-break-after: always;"></div>

# ClassNum Bean クラス説明書

## 📋 概要

`ClassNum` クラスは、得点管理システムにおける**クラス情報を表すデータモデル**です。クラスの基本情報（クラス番号、学校）を保持し、データベースとのやり取りに使用されます。

## 🏗️ クラス構成

```
package: bean
class: ClassNum implements Serializable
```

**特徴**: `Serializable` インターフェイスを実装しているため、HttpSession に保存できます。

## 📦 プロパティ（メンバ変数）

| プロパティ | 型 | 説明 | 例 |
|-----------|-----|------|-----|
| `classNum` | String | クラス番号 | "A", "1組" |
| `school` | School | 所属学校 | School オブジェクト |

## 🔧 主要メソッド

### Getter/Setter メソッド

```java
// クラス番号
public String getClassNum() {
    return classNum;
}

public void setClassNum(String classNum) {
    this.classNum = classNum;
}

// 学校
public School getSchool() {
    return school;
}

public void setSchool(School school) {
    this.school = school;
}
```

### toString メソッド

```java
@Override
public String toString() {
    return "ClassNum [classNum=" + classNum + ", school=" + school + "]";
}
```

**用途**: デバッグ時にコンソール出力でクラス情報を確認

## 💾 使用シーン

### 新規登録時
```java
ClassNum cn = new ClassNum();
cn.setClassNum("A");
cn.setSchool(teacher.getSchool());
classNumDao.save(cn);
```

### データベースから取得時
```java
ClassNum classNum = new ClassNum();
classNum.setClassNum(rSet.getString("class_num"));
classNum.setSchool(school);
```

## 📊 データベーススキーマとの対応

| プロパティ | DBカラム | 型 |
|-----------|---------|-----|
| classNum | class_num | VARCHAR(10) |
| school.getCd() | school_cd | VARCHAR(10) |

## 🎯 まとめ

✅ クラス情報の一元管理  
✅ DAO ↔ Action 間のデータ受け渡し  
✅ JSP への画面表示データ提供  
✅ セッションへの一時保存対応  

---

<div style="page-break-after: always;"></div>

# ClassNumDao クラス説明書

## 📋 概要

`ClassNumDao`（Data Access Object）は、クラス情報に関するすべてのデータベース操作を担当するクラスです。SQLクエリの実行、結果のマッピング、トランザクション管理を統一的に行います。

## 🏗️ クラス構成

```
package: dao
class: ClassNumDao extends Dao
```

## 🔍 主要メソッド一覧

| メソッド | 機能 | 戻り値 |
|---------|------|--------|
| `get(classNum, school)` | クラス1件取得 | ClassNum / null |
| `filter(school)` | 学校のクラス一覧取得 | List<String> |
| `save(classNum)` | 新規登録 | boolean |
| `save(classNum, newClassNum)` | クラス番号変更（一括更新） | boolean |
| `delete(classNum)` | クラス削除 | boolean |

## 📖 メソッド詳細

### 1. get（クラス1件取得）

```java
public ClassNum get(String class_num, School school) throws Exception
```

**機能**: クラス番号と学校コードでクラス情報を1件取得

**SQL**: `SELECT * FROM class_num WHERE class_num = ? AND school_cd = ?`

**使用例**:
```java
ClassNum classNum = classNumDao.get("A", teacher.getSchool());
if (classNum != null) {
    System.out.println("クラス: " + classNum.getClassNum());
}
```

---

### 2. filter（学校のクラス一覧取得）

```java
public List<String> filter(School school) throws Exception
```

**機能**: 指定された学校のすべてのクラス番号を取得

**SQL**: `SELECT class_num FROM class_num WHERE school_cd=? ORDER BY class_num`

**戻り値**: クラス番号のリスト（String）

**使用例**:
```java
List<String> classList = classNumDao.filter(teacher.getSchool());
// 結果例: ["A", "B", "C"]
```

**特徴**:
- クラス番号でソート済み
- ドロップダウンメニュー用データに最適

---

### 3. save（新規登録）

```java
public boolean save(ClassNum classNum) throws Exception
```

**機能**: クラス情報をデータベースに新規登録

**SQL**: `INSERT INTO class_num(school_cd, class_num) VALUES(?, ?)`

**戻り値**: 成功時 true / 失敗時 false

**使用例**:
```java
ClassNum cn = new ClassNum();
cn.setClassNum("A");
cn.setSchool(teacher.getSchool());

boolean result = classNumDao.save(cn);
if (result) {
    System.out.println("クラスを登録しました");
}
```

---

### 4. save（クラス番号変更 - 一括更新）

```java
public boolean save(ClassNum classNum, String newClassNum) throws Exception
```

**機能**: クラス番号を変更し、それに伴うすべての関連テーブルを更新

**トランザクション処理**:

このメソッドは **3つのテーブルを一括更新** します：

```java
// 1. class_num テーブル更新
UPDATE class_num SET class_num = ? WHERE school_cd = ? AND class_num = ?

// 2. student テーブル更新
UPDATE student SET class_num = ? WHERE school_cd = ? AND class_num = ?

// 3. test テーブル更新
UPDATE test SET class_num = ? WHERE school_cd = ? AND class_num = ?
```

**実装詳細**:
```java
// オートコミット無効化 → トランザクション開始
connection.setAutoCommit(false);

try {
    // 3つの UPDATE を実行
    // ...
    
    connection.commit();  // 成功時にコミット
    
} catch (Exception e) {
    connection.rollback();  // 失敗時にロールバック
    throw e;
}
```

**重要な特徴**:
- ✅ **トランザクション管理** → 3つの更新がすべて成功するか、すべて失敗するか
- ✅ **データ整合性を保証** → クラス名変更時のデータ矛盾を防止
- ✅ **ロールバック機能** → エラー時に全変更を取り消し

**使用例**:
```java
ClassNum oldClass = classNumDao.get("A", teacher.getSchool());

// クラス A → クラス B に変更
boolean result = classNumDao.save(oldClass, "B");

// 結果: class_num, student, test 全てで "A" が "B" に変更される
```

---

### 5. delete（クラス削除）

```java
public boolean delete(ClassNum classNum) throws Exception
```

**機能**: クラスをデータベースから削除

**SQL**: `DELETE FROM class_num WHERE school_cd=? AND class_num=?`

**戻り値**: 成功時 true / 失敗時 false

**使用例**:
```java
ClassNum classNum = classNumDao.get("A", teacher.getSchool());
boolean result = classNumDao.delete(classNum);

if (result) {
    System.out.println("クラスを削除しました");
}
```

**注意点**:
- ⚠️ `class_num` テーブルのみ削除
- ⚠️ `student` や `test` テーブルは削除されない（参照保持）

---

## 🔐 セキュリティ機能

✅ **PreparedStatement の使用** → SQL インジェクション完全防止  
✅ **マルチテナント対応** → すべてのクエリで school_cd をチェック  
✅ **トランザクション管理** → クラス番号変更時のデータ整合性保証  
✅ **リソース管理** → try-finally でコネクション確実にクローズ  

## 🎯 まとめ

✅ SQL クエリの安全な実行  
✅ クラス一覧の効率的な取得  
✅ クラス番号変更時のトランザクション管理  
✅ データ整合性の保証  

---

<div style="page-break-after: always;"></div>

# ClassListAction クラス説明書

## 📋 概要

`ClassListAction` は、クラス情報を**一覧表示**するための Servlet Action クラスです。学校に紐づくすべてのクラスをデータベースから取得し、JSP に渡して画面を表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassListAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「クラス管理」をクリック
    ↓
① ログインチェック
    ↓
② ClassNumDao を初期化
    ↓
③ クラス一覧を取得
    ↓
④ JSP にデータを渡す
    ↓
⑤ class_list.jsp を表示
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

**目的**: ログインしていないユーザーのアクセスを防止

---

### ② DAO 初期化とクラス一覧取得

```java
ClassNumDao classNumDao = new ClassNumDao();

// 学校に紐づくクラス一覧を取得
List<String> classList = classNumDao.filter(teacher.getSchool());
```

**戻り値**: クラス番号のリスト（String）

**例**: `["A", "B", "C"]`

---

### ③ JSP へデータを渡す

```java
req.setAttribute("classList", classList);

req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
   .forward(req, res);
```

| 属性名 | 型 | 説明 |
|--------|-----|------|
| `classList` | `List<String>` | クラス一覧 |

## 📊 実行シナリオ

### シナリオ1: 通常時

```
URL: /scoremanager/ClassList.action
    ↓
学校 A001 に紐づくすべてのクラスを取得
    ↓
class_list.jsp に表示（クラス A, B, C を表示）
```

### シナリオ2: クラスがない場合

```
URL: /scoremanager/ClassList.action
    ↓
クラス一覧が空
    ↓
classList = [] （空のリスト）
    ↓
class_list.jsp に表示（「クラスがありません」と表示）
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  

## 🎯 まとめ

✅ ログイン状態の確認  
✅ クラス一覧の効率的な取得  
✅ JSP への適切なデータ受け渡し  

---

<div style="page-break-after: always;"></div>

# ClassCreateAction クラス説明書

## 📋 概要

`ClassCreateAction` は、クラス**新規登録画面を表示**するための Servlet Action クラスです。ログイン確認後、登録フォームを表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassCreateAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「クラス新規登録」をクリック
    ↓
① ログインチェック
    ↓
② class_create.jsp へフォワード
    ↓
③ クラス登録フォームを表示
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

**目的**: ログインしていないユーザーを登録画面にアクセスさせない

---

### ② クラス登録画面へフォワード

```java
req.getRequestDispatcher("/scoremanager/main/class_create.jsp")
   .forward(req, res);
```

**処理内容**:
- 入力フォームが表示される
- ユーザーはクラス番号を入力

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  

## 🎯 まとめ

✅ ログイン状態の確認  
✅ 登録画面の表示  

---

<div style="page-break-after: always;"></div>

# ClassCreateExecuteAction クラス説明書

## 📋 概要

`ClassCreateExecuteAction` は、クラス**新規登録を実行**するための Servlet Action クラスです。フォームから受け取ったクラス番号を検証し、問題なければデータベースに登録します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassCreateExecuteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「登録」ボタンをクリック
    ↓
① ログインチェック
    ↓
② クラス番号パラメータを取得
    ↓
③ 入力値のバリデーション
    ↓
├─ エラーあり → JSP に戻す（エラーメッセージ表示）
│
└─ エラーなし → ④ ClassNum オブジェクト作成
    ↓
⑤ DB に登録
    ↓
⑥ 完了画面へリダイレクト
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

---

### ② パラメータ取得

```java
String classNumStr = req.getParameter("classNum");

if (classNumStr != null) classNumStr = classNumStr.trim();
```

---

### ③ バリデーション（入力値チェック）

```java
Map<String, String> errors = new HashMap<>();

// 未入力チェック
if (classNumStr == null || classNumStr.isEmpty()) {
    errors.put("classNum", "クラス番号を入力してください");
}

// 英数字チェック
if (!errors.containsKey("classNum")
        && !classNumStr.matches("^[A-Za-z0-9]+$")) {
    errors.put("classNum", "クラス番号は英数字で入力してください");
}

// 重複チェック
if (!errors.containsKey("classNum")
        && classNumDao.get(classNumStr, teacher.getSchool()) != null) {
    errors.put("classNum", "このクラス番号は既に登録されています");
}
```

| チェック項目 | エラー条件 | エラーメッセージ |
|-----------|----------|----------------|
| 未入力 | null または空文字列 | "クラス番号を入力してください" |
| 英数字 | 英数字以外を含む | "クラス番号は英数字で入力してください" |
| 重複 | 同じクラス番号が存在 | "このクラス番号は既に登録されています" |

---

### ④ ClassNum オブジェクト作成と DB 登録

```java
if (errors.isEmpty()) {
    
    ClassNum cn = new ClassNum();
    cn.setClassNum(classNumStr);
    cn.setSchool(teacher.getSchool());
    
    classNumDao.save(cn);
    
    // 完了画面へリダイレクト
    res.sendRedirect(
        req.getContextPath() + "/scoremanager/main/class_create_done.jsp"
    );
    return;
}
```

---

### ⑤ エラー時の処理

```java
req.setAttribute("errors", errors);
req.setAttribute("classNum", classNumStr);

req.getRequestDispatcher("ClassCreate.action")
   .forward(req, res);
```

## 📊 実行シナリオ

### シナリオ1: 正常系（入力が正しい）

```
クラス番号: "A"
    ↓
バリデーション OK → 重複チェック OK
    ↓
ClassNum オブジェクトを作成 → DB に登録
    ↓
完了画面へリダイレクト
```

---

### シナリオ2: 未入力

```
クラス番号: （空白）
    ↓
バリデーション NG（未入力）
    ↓
errors に「クラス番号を入力してください」を追加
    ↓
入力画面に戻す（エラーメッセージ表示）
```

---

### シナリオ3: クラス番号が重複している

```
クラス番号: "A"（既に存在）
    ↓
バリデーション OK → 重複チェック NG
    ↓
errors に「このクラス番号は既に登録されています」を追加
    ↓
入力画面に戻す（エラーメッセージ表示）
```

---

### シナリオ4: 英数字以外を含む

```
クラス番号: "あ"（日本語）
    ↓
バリデーション NG（英数字チェック失敗）
    ↓
errors に「クラス番号は英数字で入力してください」を追加
    ↓
入力画面に戻す（エラーメッセージ表示）
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **入力値バリデーション** → 不正な値の登録を防止  
✅ **重複チェック** → 同じクラス番号の二重登録を防止  
✅ **マルチテナント対応** → 他校のクラスと混在しない  
✅ **POST-Redirect-Get** → 再送信による重複登録を防止  

## 🎯 まとめ

✅ 入力値の厳密なバリデーション  
✅ クラス番号の重複チェック  
✅ ClassNum オブジェクトの作成と DB 登録  
✅ エラー時の適切なフィードバック  

---

<div style="page-break-after: always;"></div>

# ClassUpdateAction クラス説明書

## 📋 概要

`ClassUpdateAction` は、クラス**更新画面を表示**するための Servlet Action クラスです。クラス番号から既存のクラス情報を取得し、編集フォームに表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassUpdateAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「クラスを選択して編集」をクリック
    ↓
① ログインチェック
    ↓
② クラス番号パラメータを取得
    ↓
③ クラスが存在するか確認
    ↓
├─ 存在しない → エラーメッセージを表示
│
└─ 存在する → ④ JSP にデータを渡す
    ↓
⑤ class_update.jsp を表示
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

---

### ② クラス番号パラメータを取得

```java
String classNumStr = req.getParameter("classNum");

if (classNumStr != null) {
    classNumStr = classNumStr.trim();
}
```

**パラメータ**: `classNum` = クラス番号

**例**: `ClassUpdate.action?classNum=A`

---

### ③ クラス番号の妥当性チェック

```java
if (classNumStr == null || classNumStr.isEmpty()) {
    
    req.setAttribute("error", "クラス番号が指定されていません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー条件**:
- `classNumStr == null` → パラメータが渡されていない
- `classNumStr.isEmpty()` → パラメータが空文字列

**処理**: 一覧画面にエラーメッセージを表示

---

### ④ クラス情報を取得

```java
ClassNumDao classNumDao = new ClassNumDao();

ClassNum classNum = classNumDao.get(classNumStr, teacher.getSchool());

if (classNum == null) {
    
    req.setAttribute("error", "対象のクラスが存在しません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー処理**:
- DB からクラスが見つからない → 一覧画面にエラー表示

---

### ⑤ JSP へデータを渡す

```java
req.setAttribute("oldClassNum", classNumStr);
req.setAttribute("newClassNum", classNumStr);

req.getRequestDispatcher("/scoremanager/main/class_update.jsp")
   .forward(req, res);
```

| 属性名 | 説明 |
|--------|------|
| `oldClassNum` | 元のクラス番号 |
| `newClassNum` | 新しいクラス番号（初期値=元のクラス番号） |

## 📊 実行シナリオ

### シナリオ1: 正常系（クラスが存在）

```
URL: /scoremanager/ClassUpdate.action?classNum=A
    ↓
クラス A を DB から取得
    ↓
class_update.jsp に表示（oldClassNum=A, newClassNum=A）
```

---

### シナリオ2: クラスが存在しない

```
URL: /scoremanager/ClassUpdate.action?classNum=Z
    ↓
クラス Z を DB から検索 → 見つからない
    ↓
エラーメッセージ「対象のクラスが存在しません」
    ↓
class_list.jsp へフォワード
```

---

### シナリオ3: クラス番号が指定されていない

```
URL: /scoremanager/ClassUpdate.action
    ↓
パラメータ classNum が null
    ↓
エラーメッセージ「クラス番号が指定されていません」
    ↓
class_list.jsp へフォワード
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **パラメータ検証** → 不正なクラス番号の検出  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  
✅ **存在確認** → DB から削除されたクラスへのアクセスを防止  

## 🎯 まとめ

✅ 更新画面表示前の準備処理  
✅ 既存クラス情報の取得と検証  
✅ エラーハンドリング  

---

<div style="page-break-after: always;"></div>

# ClassUpdateExecuteAction クラス説明書

## 📋 概要

`ClassUpdateExecuteAction` は、クラス**情報更新を実行**するための Servlet Action クラスです。フォームから受け取った新しいクラス番号を検証し、問題なければデータベースを更新します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassUpdateExecuteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「更新」ボタンをクリック
    ↓
① ログインチェック
    ↓
② パラメータを取得（旧クラス番号、新クラス番号）
    ↓
③ 入力値のバリデーション
    ↓
④ 旧クラスが存在するか確認
    ↓
⑤ 新クラス番号が重複していないか確認
    ↓
├─ エラーあり → JSP に戻す（エラーメッセージ表示）
│
└─ エラーなし → ⑥ ClassNumDao.save() で一括更新
    ↓
⑦ 完了画面へリダイレクト
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

---

### ② パラメータ取得

```java
String oldClassNum = req.getParameter("oldClassNum");    // 元のクラス番号
String newClassNum = req.getParameter("newClassNum");    // 新しいクラス番号

if (oldClassNum != null) oldClassNum = oldClassNum.trim();
if (newClassNum != null) newClassNum = newClassNum.trim();
```

---

### ③ バリデーション（入力値チェック）

```java
Map<String, String> errors = new HashMap<>();

// 未入力チェック
if (oldClassNum == null || oldClassNum.isEmpty()) {
    errors.put("oldClassNum", "クラス番号が取得できませんでした");
}

if (newClassNum == null || newClassNum.isEmpty()) {
    errors.put("newClassNum", "新しいクラス番号を入力してください");
}

// 英数字チェック
if (!errors.containsKey("newClassNum")
        && !newClassNum.matches("^[A-Za-z0-9]+$")) {
    errors.put("newClassNum", "クラス番号は英数字で入力してください");
}
```

| チェック項目 | エラー条件 | エラーメッセージ |
|-----------|----------|----------------|
| 旧クラス番号 | null または空文字列 | "クラス番号が取得できませんでした" |
| 新クラス番号 | null または空文字列 | "新しいクラス番号を入力してください" |
| 英数字 | 英数字以外を含む | "クラス番号は英数字で入力してください" |

---

### ④ 旧クラスが存在するか確認

```java
ClassNumDao classNumDao = new ClassNumDao();

ClassNum oldClass = classNumDao.get(oldClassNum, teacher.getSchool());

if (!errors.containsKey("oldClassNum") && oldClass == null) {
    errors.put("oldClassNum", "対象のクラスが存在しません");
}
```

**確認内容**:
- 指定された旧クラス番号が DB に存在するか
- 削除されていないか

---

### ⑤ 新クラス番号が重複していないか確認

```java
if (!errors.containsKey("newClassNum")
        && classNumDao.get(newClassNum, teacher.getSchool()) != null) {
    errors.put("newClassNum", "このクラス番号は既に使用されています");
}
```

**チェック内容**:
- 新クラス番号が既に別のクラスとして登録されていないか

---

### ⑥ エラーなし → DB 更新

```java
if (!errors.isEmpty()) {
    // エラーあり → 後述
    return;
}

// クラス番号を変更（class_num, student, test を一括更新）
classNumDao.save(oldClass, newClassNum);

// 完了画面へリダイレクト
res.sendRedirect(
    req.getContextPath()
    + "/scoremanager/main/class_update_done.jsp"
);
```

**重要**: `save(oldClass, newClassNum)` はトランザクション処理で以下を一括更新：
- `class_num` テーブル
- `student` テーブル（クラス番号）
- `test` テーブル（クラス番号）

---

### ⑦ エラー時の処理

```java
req.setAttribute("errors", errors);
req.setAttribute("oldClassNum", oldClassNum);
req.setAttribute("newClassNum", newClassNum);

req.getRequestDispatcher("ClassUpdate.action")
   .forward(req, res);
```

## 📊 実行シナリオ

### シナリオ1: 正常系（すべての入力が正しい）

```
旧クラス番号: A, 新クラス番号: B
    ↓
バリデーション OK → 存在確認 OK → 重複チェック OK
    ↓
classNumDao.save(oldClass, "B") を実行
    ↓
3つのテーブルで A → B に変更
    ↓
完了画面へリダイレクト
```

---

### シナリオ2: 新クラス番号が未入力

```
新クラス番号: （空白）
    ↓
バリデーション NG
    ↓
編集画面に戻す（「新しいクラス番号を入力してください」エラー表示）
```

---

### シナリオ3: 更新対象のクラスが存在しない

```
旧クラス番号: Z（存在しない）
    ↓
バリデーション OK → 存在確認 NG
    ↓
編集画面に戻す（「対象のクラスが存在しません」エラー表示）
```

---

### シナリオ4: 新クラス番号が重複している

```
旧クラス番号: A, 新クラス番号: C（既に存在）
    ↓
バリデーション OK → 存在確認 OK → 重複チェック NG
    ↓
編集画面に戻す（「このクラス番号は既に使用されています」エラー表示）
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **入力値バリデーション** → 不正な値の更新を防止  
✅ **存在確認** → 削除済みクラスの更新を防止  
✅ **重複チェック** → クラス番号の重複を防止  
✅ **トランザクション** → 3つのテーブルの更新を一括管理  
✅ **POST-Redirect-Get** → 再送信による二重更新を防止  
✅ **マルチテナント対応** → 他校のクラスと混在しない  

## 🎯 まとめ

✅ 入力値の厳密なバリデーション  
✅ 更新対象のクラスが存在することを確認  
✅ 新クラス番号が重複していないことを確認  
✅ トランザクション処理による一括更新  
✅ エラー時の適切なフィードバック  

---

<div style="page-break-after: always;"></div>

# ClassDeleteAction クラス説明書

## 📋 概要

`ClassDeleteAction` は、クラス**削除確認画面を表示**するための Servlet Action クラスです。削除対象のクラスが存在することを確認し、削除確認画面に表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassDeleteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「クラスを削除」をクリック
    ↓
① ログインチェック
    ↓
② クラス番号パラメータを取得
    ↓
③ クラスが存在するか確認
    ↓
├─ 存在しない → エラーメッセージを表示
│
└─ 存在する → ④ JSP にデータを渡す
    ↓
⑤ class_delete.jsp（削除確認画面）を表示
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

---

### ② クラス番号パラメータを取得

```java
String classNumStr = req.getParameter("classNum");

if (classNumStr != null) {
    classNumStr = classNumStr.trim();
}
```

**パラメータ**: `classNum` = クラス番号

**例**: `ClassDelete.action?classNum=A`

---

### ③ クラス番号の妥当性チェック

```java
if (classNumStr == null || classNumStr.isEmpty()) {
    
    req.setAttribute("error", "クラス番号が指定されていません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー条件**:
- `classNumStr == null` → パラメータが渡されていない
- `classNumStr.isEmpty()` → パラメータが空文字列

**処理**: 一覧画面にエラーメッセージを表示

---

### ④ クラス情報を取得

```java
ClassNumDao classNumDao = new ClassNumDao();

ClassNum classNum = classNumDao.get(classNumStr, teacher.getSchool());

if (classNum == null) {
    
    req.setAttribute("error", "対象のクラスが存在しません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー処理**:
- DB からクラスが見つからない → 一覧画面にエラー表示

---

### ⑤ JSP へデータを渡す

```java
req.setAttribute("classNum", classNumStr);

req.getRequestDispatcher("/scoremanager/main/class_delete.jsp")
   .forward(req, res);
```

| 属性名 | 説明 |
|--------|------|
| `classNum` | 削除対象のクラス番号 |

**画面表示**: 削除確認画面に「このクラスを削除してもよろしいですか？」と表示

## 📊 実行シナリオ

### シナリオ1: 正常系（クラスが存在）

```
URL: /scoremanager/ClassDelete.action?classNum=A
    ↓
クラス A を DB から取得
    ↓
class_delete.jsp に表示（削除確認画面）
    ↓
ユーザーが「削除」または「キャンセル」を選択
```

---

### シナリオ2: クラスが存在しない

```
URL: /scoremanager/ClassDelete.action?classNum=Z
    ↓
クラス Z を DB から検索 → 見つからない
    ↓
エラーメッセージ「対象のクラスが存在しません」
    ↓
class_list.jsp へフォワード
```

---

### シナリオ3: クラス番号が指定されていない

```
URL: /scoremanager/ClassDelete.action
    ↓
パラメータ classNum が null
    ↓
エラーメッセージ「クラス番号が指定されていません」
    ↓
class_list.jsp へフォワード
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **パラメータ検証** → 不正なクラス番号の検出  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  
✅ **存在確認** → DB から削除されたクラスへのアクセスを防止  
✅ **削除確認画面** → 誤削除を防止  

## 🎯 まとめ

✅ 削除画面表示前の準備処理  
✅ 削除対象のクラスが存在することを確認  
✅ エラーハンドリング  

---

<div style="page-break-after: always;"></div>

# ClassDeleteExecuteAction クラス説明書

## 📋 概要

`ClassDeleteExecuteAction` は、クラス**削除を実行**するための Servlet Action クラスです。ユーザーの削除確認後、データベースからクラスを削除します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: ClassDeleteExecuteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「削除確認画面」で「削除」ボタンをクリック
    ↓
① ログインチェック
    ↓
② クラス番号パラメータを取得
    ↓
③ クラスが存在するか確認
    ↓
├─ 存在しない → エラーメッセージを表示
│
└─ 存在する → ④ クラスを削除
    ↓
⑤ 完了画面へリダイレクト
```

## 🔧 処理の詳細

### ① ログインチェック

```java
HttpSession session = req.getSession(false);

if (session == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}

Teacher teacher = (Teacher) session.getAttribute("user");

if (teacher == null) {
    res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    return;
}
```

---

### ② クラス番号パラメータを取得

```java
String classNumStr = req.getParameter("classNum");

if (classNumStr != null) {
    classNumStr = classNumStr.trim();
}
```

---

### ③ クラス番号の妥当性チェック

```java
if (classNumStr == null || classNumStr.isEmpty()) {
    
    req.setAttribute("error", "クラス番号が指定されていません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}
```

---

### ④ クラス情報を取得して削除

```java
ClassNumDao classNumDao = new ClassNumDao();

ClassNum classNum = classNumDao.get(classNumStr, teacher.getSchool());

if (classNum == null) {
    
    req.setAttribute("error", "対象のクラスが存在しません");
    
    req.getRequestDispatcher("/scoremanager/main/class_list.jsp")
       .forward(req, res);
    
    return;
}

// ===== 削除処理 =====
classNumDao.delete(classNum);
```

**実行される SQL**:
```sql
DELETE FROM class_num WHERE school_cd=? AND class_num=?
```

**注意点**:
- ⚠️ `class_num` テーブルのみ削除
- ⚠️ `student` や `test` テーブルのデータは残される

---

### ⑤ 完了画面へリダイレクト

```java
res.sendRedirect(
    req.getContextPath()
    + "/scoremanager/main/class_delete_done.jsp"
);
```

**POST-Redirect-Get パターン** → 再送信による二重削除を防止

## 📊 実行シナリオ

### シナリオ1: 正常系（クラスが存在）

```
URL: /scoremanager/ClassDeleteExecute.action（POST）
パラメータ: classNum=A
    ↓
クラス A を DB から取得
    ↓
classNumDao.delete() を実行
    ↓
class_num テーブルからクラス A を削除
    ↓
完了画面へリダイレクト
```

---

### シナリオ2: クラスが存在しない

```
パラメータ: classNum=Z
    ↓
クラス Z を DB から検索 → 見つからない
    ↓
エラーメッセージ「対象のクラスが存在しません」
    ↓
class_list.jsp へフォワード
```

---

### シナリオ3: クラス番号が指定されていない

```
パラメータ: classNum=（空白）
    ↓
エラーメッセージ「クラス番号が指定されていません」
    ↓
class_list.jsp へフォワード
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **パラメータ検証** → 不正なクラス番号の検出  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  
✅ **存在確認** → DB から削除済みクラスの削除を防止  
✅ **POST-Redirect-Get** → 再送信による二重削除を防止  

## ⚠️ 重要な注意点

### クラス削除時の動作

```
削除対象: クラス A

処理結果:
┌─────────────────┐
│  class_num 削除  │ ← クラス A が削除される
│  student 保持   │ ← クラス A に属する学生は残る
│  test 保持      │ ← クラス A のテストは残る
└─────────────────┘
```

**推奨事項**:
- クラス削除前に、関連する学生やテストの確認を促す
- または、学生やテストがある場合は削除を禁止する

---

## 🎯 まとめ

✅ 削除前の確認処理  
✅ 削除対象のクラスが存在することを確認  
✅ クラスの安全な削除  
✅ エラー時の適切なフィードバック  

---

## 📚 まとめ

本マニュアルでは、クラス管理機能の以下の**9つのクラス**を詳しく解説しました：

1. **ClassNum Bean** - クラス情報のデータモデル
2. **ClassNumDao** - DB アクセス層（トランザクション管理含む）
3. **ClassListAction** - 一覧表示
4. **ClassCreateAction** - 登録画面表示
5. **ClassCreateExecuteAction** - 登録実行
6. **ClassUpdateAction** - 更新画面表示
7. **ClassUpdateExecuteAction** - 更新実行（一括更新）
8. **ClassDeleteAction** - 削除確認画面表示
9. **ClassDeleteExecuteAction** - 削除実行

これらのクラスが連携することで、クラス管理機能全体が実現されています。

✅ **MVC パターンの実装**  
✅ **セキュリティ対策**  
✅ **トランザクション管理**  
✅ **エラーハンドリング**  
✅ **マルチテナント対応**  

各処理を理解することで、得点管理システムの開発・保守が容易になります。
