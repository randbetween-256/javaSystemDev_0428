# その他の機能 マニュアル

得点管理システムのその他の機能についての説明書です。学生管理・クラス管理ほど詳しくなく、コンパクトにまとめています。

---

## 目次

1. [Subject Bean & Subject 管理機能](#subject-bean--subject-管理機能)
2. [Test Bean & Test 関連機能](#test-bean--test-関連機能)
3. [MenuAction](#menuaction)
4. [LogoutAction](#logoutaction)

---

## Subject Bean & Subject 管理機能

### Subject Bean

**役割**: 科目情報のデータモデル

**プロパティ**:
- `id`: 科目コード（String） - 例："001"
- `name`: 科目名（String） - 例："数学"
- `school`: 学校（School オブジェクト）

**特徴**:
- テスト登録用の特別なコンストラクタあり：`Subject(String id, String name)`
- `toString()` メソッドでデバッグ情報を出力

---

### SubjectListAction

**機能**: 科目一覧表示

**処理内容**:
1. ログインチェック
2. 学校に紐づく科目一覧を取得
3. subject_list.jsp に表示

---

### SubjectCreateAction

**機能**: 科目登録画面表示

**処理内容**: ログインチェック後、subject_create.jsp を表示

---

### SubjectCreateExecuteAction

**機能**: 科目新規登録実行

**バリデーション**:
- ✅ 科目コード未入力チェック
- ✅ 科目名未入力チェック
- ✅ 科目コード形式チェック（英数字のみ）
- ✅ 科目コード文字数チェック（**3文字固定**）
- ✅ 科目コード重複チェック

**エラー時**: 入力画面に戻す（エラーメッセージと入力値を保持）

---

### SubjectUpdateAction

**機能**: 科目更新画面表示

**処理内容**:
1. ログインチェック
2. パラメータから科目コードを取得
3. 科目が存在するか確認
4. 科目情報を JSP に渡す

**エラー処理**: 科目が存在しない場合は一覧画面へ戻す

---

### SubjectUpdateExecuteAction

**機能**: 科目情報更新実行

**バリデーション**:
- ✅ 科目コード未入力チェック
- ✅ 科目名未入力チェック
- ✅ 科目の存在確認

**処理**: 問題なければ科目情報を更新

---

### SubjectDeleteAction

**機能**: 科目削除確認画面表示

**処理内容**:
1. ログインチェック
2. パラメータから科目コードを取得
3. 科目が存在するか確認
4. subject_delete.jsp（確認画面）に表示

---

### SubjectDeleteExecuteAction

**機能**: 科目削除実行

**処理内容**:
1. ログインチェック
2. 科目コードを取得して検証
3. 科目を削除
4. 完了画面へリダイレクト

---

## Test Bean & Test 関連機能

### Test Bean

**役割**: テスト成績のデータモデル

**プロパティ**:
- `student`: 学生（Student オブジェクト）
- `subject`: 科目（Subject オブジェクト）
- `school`: 学校（School オブジェクト）
- `classNum`: クラス番号（String）
- `no`: 回数（int） - 例：1回目、2回目…
- `point`: 得点（Integer） - null の場合は未登録

**特徴**:
- `count` という互換性用のメソッド：`getCount()` / `setCount()`

---

### TestListAction

**機能**: テスト一覧検索画面表示

**処理内容**:
1. ログインチェック
2. 入学年度リスト作成（10年前〜今年）
3. クラス一覧を取得
4. 科目一覧を取得
5. JSP に渡す

**渡されるデータ**:
- `entYearSet`: 入学年度のリスト
- `cNumlist`: クラス一覧
- `list`: 科目一覧

**画面**: test_list.jsp

---

### TestListSubjectExecuteAction

**機能**: 科目別の成績一覧表示

**処理内容**:
1. ログインチェック
2. パラメータ取得（入学年度、クラス、科目）
3. 条件不足チェック（3つすべて必須）
4. DAO から成績一覧を取得
5. test_list_subject.jsp に表示

**パラメータ**:
- `f1`: 入学年度
- `f2`: クラス番号
- `f3`: 科目コード

**エラー**: 条件不足の場合はエラーメッセージを表示

**特徴**:
- 科目名の自動取得
- マルチテーブル（Student, Subject, TestListSubject）の連携

---

## MenuAction

### 機能

メニュー画面を表示するシンプルな Action

### 処理内容

1. ログインチェック
2. menu.jsp をフォワード

### 特徴

- メイン画面への入り口
- ログイン確認のみ

### コード例

```java
// セッションからログイン中の Teacher を取得
Teacher teacher = (Teacher) session.getAttribute("user");

// ログイン済みの場合はメニュー画面へフォワード
req.getRequestDispatcher("/scoremanager/main/menu.jsp")
   .forward(req, res);
```

---

## LogoutAction

### 機能

ログアウト処理を実行

### 処理内容

1. セッションを取得（存在しない場合は null）
2. セッション破棄（`session.invalidate()`）
3. logout.jsp へリダイレクト

### 特徴

- セッション破棄により完全なログアウトを実現
- ユーザー情報はメモリから削除される

### コード例

```java
HttpSession session = req.getSession(false);

// セッションが存在する場合は破棄
if (session != null) {
    session.invalidate();
}

// logout.jsp へリダイレクト
res.sendRedirect(req.getContextPath() + "/scoremanager/main/logout.jsp");
```

---

## 機能別処理フロー一覧

### 科目管理（Subject）

```
一覧表示
  ↓
SubjectListAction
  ↓
subject_list.jsp
```

```
新規登録
  ↓
SubjectCreateAction → subject_create.jsp
  ↓
SubjectCreateExecuteAction
  ↓
subject_create_done.jsp
```

```
情報更新
  ↓
SubjectUpdateAction → subject_update.jsp
  ↓
SubjectUpdateExecuteAction
  ↓
subject_update_done.jsp
```

```
削除
  ↓
SubjectDeleteAction → subject_delete.jsp（確認画面）
  ↓
SubjectDeleteExecuteAction
  ↓
subject_delete_done.jsp
```

### テスト管理（Test）

```
成績一覧検索
  ↓
TestListAction → test_list.jsp
  ↓
TestListSubjectExecuteAction → test_list_subject.jsp
```

---

## セキュリティ対策

すべての Action で共通して実装されている対策：

✅ **ログインチェック**: `session.getSession(false)` でセッション確認  
✅ **マルチテナント対応**: `teacher.getSchool()` で学校を指定  
✅ **入力値バリデーション**: 必須項目、形式チェック、重複チェック  
✅ **POST-Redirect-Get**: 再送信を防止  

---

## データ流れ図

### 科目コード管理

```
入力（科目コード）
  ↓
▼ バリデーション
  ├─ 未入力チェック
  ├─ 形式チェック（英数字のみ）
  ├─ 文字数チェック（3文字）
  └─ 重複チェック
  ↓
エラーあり → 入力画面に戻す
  ↓
エラーなし → DB に登録
  ↓
完了画面へ
```

### 成績一覧検索

```
検索条件入力（入学年度、クラス、科目）
  ↓
条件チェック（3つすべて必須）
  ↓
条件不足 → エラーメッセージ表示
  ↓
OK → DB から成績データ取得
  ↓
test_list_subject.jsp に表示
```

---

## よくある使用シーン

### シーン1: 科目を新規作成

```
ユーザー入力
  科目コード: "001"
  科目名: "数学"
    ↓
SubjectCreateExecuteAction でバリデーション
    ↓
問題なし → Subject オブジェクト作成 → DB 登録
    ↓
subject_create_done.jsp に表示
```

### シーン2: 成績を検索・表示

```
TestListAction で検索条件を準備
    ↓
ユーザーが「入学年度:2024, クラス:A, 科目:001」を選択
    ↓
TestListSubjectExecuteAction で検索実行
    ↓
TestListSubjectDao から成績一覧を取得
    ↓
test_list_subject.jsp に表示
```

### シーン3: ログアウト

```
ユーザーが「ログアウト」をクリック
    ↓
LogoutAction 実行
    ↓
session.invalidate() でセッション破棄
    ↓
logout.jsp へリダイレクト
    ↓
ユーザーはログアウト状態に
```

---

## トラブルシューティング

| 症状 | 原因 | 対処方法 |
|------|------|--------|
| ログイン画面にリダイレクトされる | セッションがない または Teacher がnull | ログインし直してください |
| 「科目が見つかりません」エラー | 削除されたか、データベース接続エラー | リストを更新して確認 |
| 検索結果が空 | 条件に合うデータがない | 検索条件を変更してください |
| 「科目コードが重複」エラー | 同じコードが既に登録済み | 別のコードを使用してください |

---

## まとめ

このマニュアルでカバーした機能：

✅ **Subject 管理**（一覧・作成・更新・削除）  
✅ **Test 表示**（検索・一覧表示）  
✅ **Menu 画面**（メイン画面表示）  
✅ **Logout**（ログアウト）  

これらの機能により、学生管理・クラス管理に加えて、科目管理とテスト成績管理が実現されます。

全機能が **MVC パターン**で統一された設計となっており、セキュリティ対策も共通で実装されています。
