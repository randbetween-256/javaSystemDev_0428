# 学生管理機能 完全マニュアル

---

## 📖 目次

1. [Student Bean クラス](#student-bean-クラス説明書)
2. [StudentDao クラス](#studentdao-クラス説明書)
3. [StudentListAction クラス](#studentlistaction-クラス説明書)
4. [StudentCreateAction クラス](#studentcreateaction-クラス説明書)
5. [StudentCreateExecuteAction クラス](#studentcreateexecuteaction-クラス説明書)
6. [StudentUpdateAction クラス](#studentupdateaction-クラス説明書)
7. [StudentUpdateExecuteAction クラス](#studentupdateexecuteaction-クラス説明書)

---

<div style="page-break-after: always;"></div>

# Student Bean クラス説明書

## 📋 概要

`Student` クラスは、得点管理システムにおける**学生情報を表すデータモデル**です。学生の基本情報（学生番号、氏名、入学年度、クラス番号、在学状態）を保持し、データベースとのやり取りに使用されます。

## 🏗️ クラス構成

```
package: bean
class: Student implements Serializable
```

**特徴**: `Serializable` インターフェイスを実装しているため、HttpSession に保存できます。

## 📦 プロパティ（メンバ変数）

| プロパティ | 型 | 説明 | 例 |
|-----------|-----|------|-----|
| `no` | String | 学生番号（主キー） | "2401" |
| `name` | String | 学生の氏名 | "山田太郎" |
| `entYear` | int | 入学年度 | 2024 |
| `classNum` | String | クラス番号 | "A" |
| `isAttend` | boolean | 在学フラグ | true/false |
| `school` | School | 所属学校 | School オブジェクト |

## 🔧 主要メソッド

### Getter/Setter メソッド

```java
// 学生番号
public String getNo() { return no; }
public void setNo(String no) { this.no = no; }

// 学生名
public String getName() { return name; }
public void setName(String name) { this.name = name; }

// 入学年度
public int getEntYear() { return entYear; }
public void setEntYear(int entYear) { this.entYear = entYear; }

// クラス番号
public String getClassNum() { return classNum; }
public void setClassNum(String classNum) { this.classNum = classNum; }

// 在学フラグ
public boolean isAttend() { return isAttend; }
public void setAttend(boolean isAttend) { this.isAttend = isAttend; }

// 学校
public School getSchool() { return school; }
public void setSchool(School school) { this.school = school; }
```

### toString メソッド

```java
@Override
public String toString() {
    return "Student [no=" + no + ", name=" + name + ", entYear=" + entYear 
            + ", classNum=" + classNum + ", isAttend=" + isAttend + "]";
}
```

**用途**: デバッグ時にコンソール出力で学生情報を確認

## 💾 使用シーン

### 新規登録時
```java
Student student = new Student();
student.setNo("2401");
student.setName("山田太郎");
student.setEntYear(2024);
student.setClassNum("A");
student.setAttend(true);
student.setSchool(teacher.getSchool());
studentDao.save(student);
```

### データベースから取得時
```java
Student student = new Student();
student.setNo(rSet.getString("no"));
student.setName(rSet.getString("name"));
student.setEntYear(rSet.getInt("ent_year"));
student.setClassNum(rSet.getString("class_num"));
student.setAttend(rSet.getBoolean("is_attend"));
student.setSchool(school);
```

## 📊 データベーススキーマとの対応

| プロパティ | DBカラム | 型 |
|-----------|---------|-----|
| no | no | VARCHAR(10) |
| name | name | VARCHAR(50) |
| entYear | ent_year | INT |
| classNum | class_num | VARCHAR(10) |
| isAttend | is_attend | BOOLEAN |
| school.getCd() | school_cd | VARCHAR(10) |

## 🎯 まとめ

✅ 学生情報の一元管理  
✅ DAO ↔ Action 間のデータ受け渡し  
✅ JSP への画面表示データ提供  
✅ セッションへの一時保存対応  

---

<div style="page-break-after: always;"></div>

# StudentDao クラス説明書

## 📋 概要

`StudentDao`（Data Access Object）は、学生情報に関するすべてのデータベース操作を担当するクラスです。SQLクエリの実行、結果のマッピング、トランザクション管理を統一的に行います。

## 🏗️ クラス構成

```
package: dao
class: StudentDao extends Dao
```

## 🔍 主要メソッド一覧

| メソッド | 機能 | 戻り値 |
|---------|------|--------|
| `get(no, school)` | 学生1件取得 | Student / null |
| `filter(school)` | 全学生取得 | List<Student> |
| `filter(school, isAttend)` | 在学状態でフィルタ | List<Student> |
| `filter(school, entYear, classNum, isAttend)` | 複雑条件で検索 | List<Student> |
| `filter(school, entYear, isAttend)` | 入学年度+在学でフィルタ | List<Student> |
| `save(student)` | 新規登録 | boolean |
| `update(student)` | 情報更新 | boolean |
| `getEntYearSet(school)` | 入学年度一覧取得 | List<Integer> |

## 📖 メソッド詳細

### 1. get（学生1件取得）

```java
public Student get(String no, School school) throws Exception
```

**機能**: 学生番号と学校コードで学生情報を1件取得

**SQL**: `SELECT * FROM student WHERE no = ? AND school_cd = ?`

**使用例**:
```java
Student student = studentDao.get("2401", teacher.getSchool());
if (student != null) {
    System.out.println("学生名: " + student.getName());
}
```

---

### 2. filter（学校単位で全学生取得）

```java
public List<Student> filter(School school) throws Exception
```

**機能**: 指定された学校のすべての学生を取得

**SQL**: `SELECT * FROM student WHERE school_cd = ? ORDER BY no`

**使用例**:
```java
List<Student> students = studentDao.filter(teacher.getSchool());
```

---

### 3. filter（在学状態でフィルタ）

```java
public List<Student> filter(School school, boolean isAttend) throws Exception
```

**機能**: 学校と在学状態を指定して学生を検索

**SQL**: `SELECT * FROM student WHERE school_cd = ? AND is_attend = ? ORDER BY no`

**使用例**:
```java
// 在学中の学生のみ
List<Student> attending = studentDao.filter(teacher.getSchool(), true);
```

---

### 4. filter（複雑条件での検索）

```java
public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception
```

**機能**: 入学年度、クラス、在学状態を組み合わせて検索

**SQL**: `SELECT * FROM student WHERE school_cd = ? AND ent_year = ? AND class_num = ? AND is_attend = ? ORDER BY no`

**使用例**:
```java
List<Student> results = studentDao.filter(teacher.getSchool(), 2024, "A", true);
```

---

### 5. filter（入学年度+在学状態）

```java
public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception
```

**機能**: 入学年度と在学状態でフィルタ（クラス指定なし）

**SQL**: `SELECT * FROM student WHERE school_cd = ? AND ent_year = ? AND is_attend = ? ORDER BY no`

---

### 6. save（新規登録）

```java
public boolean save(Student student) throws Exception
```

**機能**: 学生情報をデータベースに新規登録

**SQL**: `INSERT INTO student(no, name, ent_year, class_num, is_attend, school_cd) VALUES(?, ?, ?, ?, ?, ?)`

**戻り値**: 成功時 true / 失敗時 false

**使用例**:
```java
Student student = new Student();
student.setNo("2401");
student.setName("山田太郎");
student.setEntYear(2024);
student.setClassNum("A");
student.setAttend(true);
student.setSchool(teacher.getSchool());

boolean result = studentDao.save(student);
```

---

### 7. update（学生情報更新）

```java
public boolean update(Student student) throws Exception
```

**機能**: 既存の学生情報を更新

**SQL**: `UPDATE student SET name=?, ent_year=?, class_num=?, is_attend=? WHERE no=? AND school_cd=?`

**戻り値**: 更新成功時 true / 失敗時 false

**使用例**:
```java
Student student = studentDao.get("2401", teacher.getSchool());
student.setName("新しい名前");
student.setClassNum("B");
boolean result = studentDao.update(student);
```

**注意**: 学生番号は変更できません

---

### 8. getEntYearSet（入学年度一覧取得）

```java
public List<Integer> getEntYearSet(School school) throws Exception
```

**機能**: 学校に在籍する学生の入学年度を重複なしで取得

**SQL**: `SELECT DISTINCT ent_year FROM student WHERE school_cd = ? ORDER BY ent_year`

**使用例**:
```java
List<Integer> entYears = studentDao.getEntYearSet(teacher.getSchool());
// 結果例: [2020, 2021, 2022, 2023, 2024]
```

## 🔐 セキュリティ機能

✅ **PreparedStatement の使用** → SQL インジェクション完全防止  
✅ **Try-with-Resources** → リソース自動クローズ  
✅ **マルチテナント対応** → すべてのクエリで school_cd をチェック  

## 🎯 まとめ

✅ SQL クエリの安全な実行  
✅ 検索条件に応じた柔軟なデータ取得  
✅ INSERT・UPDATE によるデータ操作  
✅ ResultSet → Student オブジェクトへの変換  

---

<div style="page-break-after: always;"></div>

# StudentListAction クラス説明書

## 📋 概要

`StudentListAction` は、学生情報を**一覧表示**するための Servlet Action クラスです。検索条件に応じて学生データを取得し、JSP に渡して画面を表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: StudentListAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「学生一覧」をクリック
    ↓
① ログインチェック
    ↓
② 検索パラメータ取得（f1=入学年度, f2=クラス, f3=在学フラグ）
    ↓
③ 入学年度を数値に変換
    ↓
④ 検索条件の判定
    ↓
⑤ StudentDao で学生データ取得
    ↓
⑥ ドロップダウン用リスト作成
    ↓
⑦ JSP にデータを渡す
    ↓
⑧ student_list.jsp を表示
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

### ② パラメータ取得

```java
String entYearStr = req.getParameter("f1");    // 入学年度
String classNum = req.getParameter("f2");      // クラス番号
String isAttendStr = req.getParameter("f3");   // 在学フラグ

if (entYearStr != null) entYearStr = entYearStr.trim();
if (classNum != null) classNum = classNum.trim();
```

| パラメータ | 説明 |
|-----------|------|
| f1 | 入学年度（ドロップダウンから） |
| f2 | クラス番号（ドロップダウンから） |
| f3 | 在学フラグ（チェックボックス） |

---

### ③ 検索条件の判定

**初期表示（パラメータなし）**:
```java
if (entYearStr == null && classNum == null && isAttendStr == null) {
    students = studentDao.filter(teacher.getSchool());
}
```

**検索パターン**:

| 条件 | 処理 |
|------|------|
| 入学年度 ✅ + クラス ✅ | 3つの条件で検索 |
| 入学年度 ✅ + クラス ❌ | 入学年度+在学で検索 |
| 入学年度 ❌ + クラス ❌ | 在学フラグのみで検索 |
| 入学年度 ❌ + クラス ✅ | **エラー** → 在学フラグのみで検索 |

---

### ④ ドロップダウン用リスト作成

```java
// 入学年度リスト（10年前〜今年）
LocalDate today = LocalDate.now();
int year = today.getYear();
List<Integer> entYearList = new ArrayList<>();

for (int i = year - 10; i <= year; i++) {
    entYearList.add(i);
}

// クラス一覧取得
List<String> classNumList = classNumDao.filter(teacher.getSchool());
```

---

### ⑤ JSP へデータを渡す

```java
req.setAttribute("students", students);
req.setAttribute("ent_year_set", entYearList);
req.setAttribute("class_num_set", classNumList);

req.getRequestDispatcher("/scoremanager/main/student_list.jsp")
   .forward(req, res);
```

## 📊 実行シナリオ

### シナリオ1: 初回アクセス
```
URL: /scoremanager/StudentList.action
→ 学校のすべての学生を表示
```

### シナリオ2: 入学年度指定
```
URL: /scoremanager/StudentList.action?f1=2024
→ 2024年度の学生を表示
```

### シナリオ3: 入学年度 + クラス指定
```
URL: /scoremanager/StudentList.action?f1=2024&f2=A
→ 2024年度、クラスA の学生を表示
```

### シナリオ4: 在学フラグのみ
```
URL: /scoremanager/StudentList.action?f3=on
→ 在学中のすべての学生を表示
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **セッション管理** → `getSession(false)` で新規セッション作成を防止  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  

## 🎯 まとめ

✅ ログイン状態の確認  
✅ 検索パラメータの取得と検証  
✅ 複数の検索パターンに対応  
✅ ドロップダウン用リストの作成  

---

<div style="page-break-after: always;"></div>

# StudentCreateAction クラス説明書

## 📋 概要

`StudentCreateAction` は、学生**新規登録画面を表示**するための Servlet Action クラスです。登録フォームに必要なドロップダウンデータ（入学年度、クラス）を準備し、JSP に渡します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: StudentCreateAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「新規登録」をクリック
    ↓
① ログインチェック
    ↓
② 入学年度リスト作成（10年前〜今年）
    ↓
③ クラス一覧取得
    ↓
④ JSP にデータを渡す
    ↓
⑤ student_create.jsp を表示
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

### ② 入学年度リスト作成

```java
// 今日の日付を取得
LocalDate today = LocalDate.now();
int year = today.getYear();

// 入学年度のリストを作成（10年前〜今年まで）
List<Integer> entYearList = new ArrayList<>();

for (int i = year - 10; i <= year; i++) {
    entYearList.add(i);
}

// 例（2024年の場合）: [2014, 2015, ..., 2023, 2024]
```

**用途**: 登録フォームのドロップダウンメニューに使用

---

### ③ クラス一覧取得

```java
ClassNumDao classNumDao = new ClassNumDao();
List<String> classNumList = classNumDao.filter(teacher.getSchool());
```

**用途**: 登録フォームのクラス選択ドロップダウンに使用

---

### ④ JSP へデータを渡す

```java
req.setAttribute("ent_year_set", entYearList);
req.setAttribute("class_num_set", classNumList);

req.getRequestDispatcher("/scoremanager/main/student_create.jsp")
   .forward(req, res);
```

| 属性名 | 型 | 説明 |
|--------|-----|------|
| `ent_year_set` | `List<Integer>` | 入学年度ドロップダウン用 |
| `class_num_set` | `List<String>` | クラスドロップダウン用 |

## 📊 JSP での使用例

```jsp
<!-- 入学年度ドロップダウン -->
<select name="ent_year">
    <option value="0">選択してください</option>
    <c:forEach var="year" items="${ent_year_set}">
        <option value="${year}">${year}年</option>
    </c:forEach>
</select>

<!-- クラスドロップダウン -->
<select name="class_num">
    <option value="">選択してください</option>
    <c:forEach var="classNum" items="${class_num_set}">
        <option value="${classNum}">${classNum}</option>
    </c:forEach>
</select>
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **学校指定** → `teacher.getSchool()` で学校を指定  

## 🎯 まとめ

✅ 登録フォーム表示前の準備処理  
✅ ドロップダウンデータの取得  
✅ JSP への適切なデータ受け渡し  

---

<div style="page-break-after: always;"></div>

# StudentCreateExecuteAction クラス説明書

## 📋 概要

`StudentCreateExecuteAction` は、学生**新規登録を実行**するための Servlet Action クラスです。フォームから受け取った入力値を検証し、問題なければデータベースに登録します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: StudentCreateExecuteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「登録」ボタンをクリック
    ↓
① ログインチェック
    ↓
② フォーム入力値を取得
    ↓
③ 入力値のバリデーション
    ↓
④ 学生番号の重複チェック
    ↓
├─ エラーあり → JSP に戻す（エラーメッセージ表示）
│
└─ エラーなし → ⑤ Student オブジェクト作成
    ↓
⑥ DB に登録
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

### ② フォーム入力値の取得

```java
String entYearStr = req.getParameter("ent_year");    // 入学年度（文字列）
String studentNo = req.getParameter("no");           // 学生番号
String studentName = req.getParameter("name");       // 学生名
String classNum = req.getParameter("class_num");     // クラス番号
String isAttendStr = req.getParameter("is_attend");  // 在学チェックボックス

// 前後の空白を削除
if (studentNo != null) studentNo = studentNo.trim();
if (studentName != null) studentName = studentName.trim();
if (classNum != null) classNum = classNum.trim();
```

---

### ③ バリデーション（入力値チェック）

```java
Map<String, String> errors = new HashMap<>();

// 入学年度チェック
if (entYear == 0) {
    errors.put("ent_year", "入学年度を選択してください");
}

// 学生番号チェック
if (studentNo == null || studentNo.isEmpty()) {
    errors.put("student_no", "学生番号を入力してください");
}

// 学生名チェック
if (studentName == null || studentName.isEmpty()) {
    errors.put("student_name", "学生名を入力してください");
}

// クラス番号チェック
if (classNum == null || classNum.isEmpty()) {
    errors.put("class_num", "クラスを選択してください");
}
```

| チェック項目 | エラー条件 | エラーメッセージ |
|-----------|----------|----------------|
| 入学年度 | `entYear == 0` | "入学年度を選択してください" |
| 学生番号 | null または空文字列 | "学生番号を入力してください" |
| 学生名 | null または空文字列 | "学生名を入力してください" |
| クラス番号 | null または空文字列 | "クラスを選択してください" |

---

### ④ 学生番号の重複チェック

```java
if (!errors.containsKey("student_no")
        && studentDao.get(studentNo, teacher.getSchool()) != null) {
    
    errors.put("student_no", "学生番号が重複しています");
}
```

**処理内容**:
1. 学生番号のバリデーションエラーがない場合のみ実行
2. DB から同じ学生番号の学生を検索
3. 見つかった場合（`!= null`）→ エラーメッセージを追加

---

### ⑤ エラーチェック

```java
if (errors.isEmpty()) {
    // エラーなし → 登録処理へ進む
} else {
    // エラーあり → JSP に戻す
}
```

---

### ⑥ Student オブジェクト作成と DB 登録

```java
// Student オブジェクトを作成
Student student = new Student();
student.setNo(studentNo);
student.setName(studentName);
student.setEntYear(entYear);
student.setClassNum(classNum);

// チェックボックスは null でなければ true
student.setAttend(isAttendStr != null);

// 学校情報をセット
student.setSchool(teacher.getSchool());

// DB に登録
studentDao.save(student);
```

---

### ⑦ 完了画面へリダイレクト

```java
res.sendRedirect(req.getContextPath() 
        + "/scoremanager/main/student_create_done.jsp");
```

**POST-Redirect-Get パターン**を採用 → F5キーでの再送信を防止

---

### エラー時の処理

```java
// エラーメッセージをリクエスト属性に保存
req.setAttribute("errors", errors);

// 入力値を再表示するために保存
req.setAttribute("ent_year", entYear);
req.setAttribute("no", studentNo);
req.setAttribute("name", studentName);
req.setAttribute("class_num", classNum);
req.setAttribute("is_attend", isAttendStr != null);

// 入力画面に戻す（Action を再実行）
req.getRequestDispatcher("StudentCreate.action")
   .forward(req, res);
```

## 📊 実行シナリオ

### シナリオ1: 正常系（すべての入力が正しい）

```
入学年度: 2024, 学生番号: 2401, 学生名: 山田太郎, クラス: A, 在学: チェック
    ↓
バリデーション OK → 重複チェック OK
    ↓
Student オブジェクトを作成 → DB に登録
    ↓
完了画面へリダイレクト
```

---

### シナリオ2: 入力値が不足している

```
入学年度: (未選択), 学生番号: 2401, 学生名: 山田太郎, クラス: A
    ↓
バリデーション NG（入学年度が未選択）
    ↓
errors に「入学年度を選択してください」を追加
    ↓
入力画面に戻す（エラーメッセージ表示、入力値を保持）
```

---

### シナリオ3: 学生番号が重複している

```
学生番号: 2401（既に存在）
    ↓
バリデーション OK → 重複チェック NG
    ↓
errors に「学生番号が重複しています」を追加
    ↓
入力画面に戻す（エラーメッセージ表示、入力値を保持）
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **入力値バリデーション** → 不正な値の登録を防止  
✅ **重複チェック** → 同じ学生番号の二重登録を防止  
✅ **POST-Redirect-Get** → 再送信による重複登録を防止  
✅ **マルチテナント対応** → 他校の学生と混在しない  

## 🎯 まとめ

✅ 入力値の厳密なバリデーション  
✅ 学生番号の重複チェック  
✅ Student オブジェクトの作成と DB 登録  
✅ エラー時の適切なフィードバック  

---

<div style="page-break-after: always;"></div>

# StudentUpdateAction クラス説明書

## 📋 概要

`StudentUpdateAction` は、学生**更新画面を表示**するための Servlet Action クラスです。学生番号から既存の学生情報を取得し、編集フォームに表示します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: StudentUpdateAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「学生を選択して編集」をクリック
    ↓
① ログインチェック
    ↓
② 学生番号パラメータを取得
    ↓
③ 学生が存在するか確認
    ↓
├─ 存在しない → エラーメッセージを表示
│
└─ 存在する → ④ 学生情報を取得
    ↓
⑤ クラス一覧を取得
    ↓
⑥ JSP にデータを渡す
    ↓
⑦ student_update.jsp を表示
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

### ② 学生番号パラメータを取得

```java
String no = req.getParameter("no");

if (no != null) {
    no = no.trim();
}
```

**パラメータ**: `no` = 学生番号

**例**: `StudentUpdate.action?no=2401`

---

### ③ 学生番号の妥当性チェック

```java
if (no == null || no.isEmpty()) {
    
    req.setAttribute("error", "学生番号が指定されていません");
    
    req.getRequestDispatcher("/scoremanager/main/student_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー条件**:
- `no == null` → パラメータが渡されていない
- `no.isEmpty()` → パラメータが空文字列

**処理**: 一覧画面にエラーメッセージを表示

---

### ④ 学生情報を取得

```java
StudentDao studentDao = new StudentDao();
ClassNumDao classNumDao = new ClassNumDao();

// 指定された学生番号の学生情報を取得
Student student = studentDao.get(no, teacher.getSchool());

// 学生が存在しない場合はエラー
if (student == null) {
    
    req.setAttribute("error", "対象の学生が存在しません");
    
    req.getRequestDispatcher("/scoremanager/main/student_list.jsp")
       .forward(req, res);
    
    return;
}
```

**エラー処理**:
- DB から学生が見つからない → 一覧画面にエラー表示

---

### ⑤ クラス一覧を取得

```java
List<String> classNumList = classNumDao.filter(teacher.getSchool());
```

**用途**: 編集フォームのクラス選択ドロップダウン用

---

### ⑥ JSP へデータを渡す

```java
req.setAttribute("ent_year", student.getEntYear());
req.setAttribute("no", student.getNo());
req.setAttribute("name", student.getName());
req.setAttribute("class_num", student.getClassNum());
req.setAttribute("class_num_set", classNumList);
req.setAttribute("is_attend", student.isAttend());
```

| 属性名 | 説明 |
|--------|------|
| `ent_year` | 入学年度 |
| `no` | 学生番号 |
| `name` | 学生名 |
| `class_num` | クラス番号 |
| `class_num_set` | クラスドロップダウン用 |
| `is_attend` | 在学フラグ |

---

### ⑦ JSP を表示

```java
req.getRequestDispatcher("/scoremanager/main/student_update.jsp")
   .forward(req, res);
```

## 📊 実行シナリオ

### シナリオ1: 正常系（学生が存在）

```
URL: /scoremanager/StudentUpdate.action?no=2401
    ↓
学生 2401 を DB から取得
    ↓
student_update.jsp に表示（フォームに既存値を埋める）
```

---

### シナリオ2: 学生が存在しない

```
URL: /scoremanager/StudentUpdate.action?no=9999
    ↓
学生 9999 を DB から検索 → 見つからない
    ↓
エラーメッセージ「対象の学生が存在しません」
    ↓
student_list.jsp へフォワード
```

---

### シナリオ3: 学生番号が指定されていない

```
URL: /scoremanager/StudentUpdate.action
    ↓
パラメータ no が null
    ↓
エラーメッセージ「学生番号が指定されていません」
    ↓
student_list.jsp へフォワード
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **パラメータ検証** → 不正な学生番号の検出  
✅ **マルチテナント対応** → `teacher.getSchool()` で学校を指定  
✅ **存在確認** → DB から削除された学生へのアクセスを防止  

## 🎯 まとめ

✅ 更新画面表示前の準備処理  
✅ 既存学生情報の取得と検証  
✅ エラーハンドリング  

---

<div style="page-break-after: always;"></div>

# StudentUpdateExecuteAction クラス説明書

## 📋 概要

`StudentUpdateExecuteAction` は、学生**情報更新を実行**するための Servlet Action クラスです。フォームから受け取った入力値を検証し、問題なければデータベースを更新します。

## 🏗️ クラス構成

```
package: scoremanager.main
class: StudentUpdateExecuteAction extends Action
```

## 🔄 処理フロー

```
ユーザーが「更新」ボタンをクリック
    ↓
① ログインチェック
    ↓
② フォーム入力値を取得
    ↓
③ 入力値のバリデーション
    ↓
④ 入学年度を数値に変換
    ↓
⑤ 更新対象の学生が存在するか確認
    ↓
├─ エラーあり → JSP に戻す（エラーメッセージ表示）
│
└─ エラーなし → ⑥ Student オブジェクト作成
    ↓
⑦ DB を更新
    ↓
⑧ 完了画面へリダイレクト
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

### ② フォーム入力値の取得

```java
String entYearStr = req.getParameter("ent_year");   // 入学年度（文字列）
String no = req.getParameter("no");                 // 学生番号
String name = req.getParameter("name");             // 学生名
String classNum = req.getParameter("class_num");    // クラス番号
String isAttendStr = req.getParameter("is_attend");  // 在学チェックボックス

// 前後の空白を削除
if (no != null) no = no.trim();
if (name != null) name = name.trim();
if (classNum != null) classNum = classNum.trim();
```

---

### ③ バリデーション（入力値チェック）

```java
if (entYearStr == null || entYearStr.isEmpty()
        || no == null || no.isEmpty()
        || name == null || name.isEmpty()
        || classNum == null || classNum.isEmpty()) {
    
    req.setAttribute("error", "未入力の項目があります");
    
    // 入力値を保持して画面に戻す
    req.setAttribute("ent_year", entYearStr);
    req.setAttribute("no", no);
    req.setAttribute("name", name);
    req.setAttribute("class_num", classNum);
    req.setAttribute("is_attend", isAttendStr != null);
    
    req.getRequestDispatcher("/StudentUpdate.action")
       .forward(req, res);
    
    return;
}
```

**チェック項目**:
- ✅ 入学年度が空でない
- ✅ 学生番号が空でない
- ✅ 学生名が空でない
- ✅ クラス番号が空でない

**エラー時**: 編集画面に戻す（入力値を保持）

---

### ④ 入学年度を数値に変換

```java
int entYear;

try {
    entYear = Integer.parseInt(entYearStr);
} catch (NumberFormatException e) {
    
    req.setAttribute("error", "入学年度の形式が不正です");
    
    req.getRequestDispatcher("StudentUpdate.action")
       .forward(req, res);
    
    return;
}
```

**エラー処理**: 数値に変換できない → エラーメッセージを表示

---

### ⑤ 更新対象の学生が存在するか確認

```java
StudentDao studentDao = new StudentDao();

Student oldStudent = studentDao.get(no, teacher.getSchool());

if (oldStudent == null) {
    
    req.setAttribute("error", "対象の学生が存在しません");
    
    req.getRequestDispatcher("/scoremanager/main/student_list.jsp")
       .forward(req, res);
    
    return;
}
```

**確認内容**:
- 指定された学生番号の学生が DB に存在するか
- 削除されていないか

---

### ⑥ Student オブジェクト作成

```java
boolean isAttend = (isAttendStr != null);

Student student = new Student();

student.setNo(no);
student.setName(name);
student.setEntYear(entYear);
student.setClassNum(classNum);
student.setAttend(isAttend);

student.setSchool(teacher.getSchool());
```

---

### ⑦ DB を更新

```java
studentDao.update(student);
```

---

### ⑧ 完了画面へリダイレクト

```java
res.sendRedirect(req.getContextPath() 
        + "/scoremanager/main/student_update_done.jsp");
```

**POST-Redirect-Get パターン** → 再送信による二重更新を防止

---

## 📊 実行シナリオ

### シナリオ1: 正常系（すべての入力が正しい）

```
学生番号: 2401, 学生名: 山田太郎（変更後）, クラス: B
    ↓
バリデーション OK → 存在確認 OK
    ↓
Student オブジェクトを作成 → DB を更新
    ↓
完了画面へリダイレクト
```

---

### シナリオ2: 入力値が不足している

```
学生名: （未入力）, その他: 入力済み
    ↓
バリデーション NG
    ↓
編集画面に戻す（「未入力の項目があります」エラー表示）
```

---

### シナリオ3: 更新対象の学生が存在しない

```
学生番号: 9999（存在しない）
    ↓
バリデーション OK → 存在確認 NG
    ↓
一覧画面へフォワード（「対象の学生が存在しません」エラー表示）
```

## 🔐 セキュリティ機能

✅ **ログインチェック** → ログイン前のアクセスを防止  
✅ **入力値バリデーション** → 不正な値の更新を防止  
✅ **存在確認** → 削除済み学生の更新を防止  
✅ **POST-Redirect-Get** → 再送信による二重更新を防止  
✅ **マルチテナント対応** → 他校の学生と混在しない  

## 🎯 まとめ

✅ 入力値の厳密なバリデーション  
✅ 更新対象の学生が存在することを確認  
✅ Student オブジェクトの作成と DB 更新  
✅ エラー時の適切なフィードバック  

---

## 📚 まとめ

本マニュアルでは、学生管理機能の以下の7つのクラスを詳しく解説しました：

1. **Student Bean** - 学生情報のデータモデル
2. **StudentDao** - DB アクセス層
3. **StudentListAction** - 一覧表示
4. **StudentCreateAction** - 登録画面表示
5. **StudentCreateExecuteAction** - 登録実行
6. **StudentUpdateAction** - 更新画面表示
7. **StudentUpdateExecuteAction** - 更新実行

これらのクラスが連携することで、学生管理機能全体が実現されています。

✅ **MVC パターンの実装**  
✅ **セキュリティ対策**  
✅ **エラーハンドリング**  
✅ **マルチテナント対応**  

各処理を理解することで、得点管理システムの開発・保守が容易になります。
