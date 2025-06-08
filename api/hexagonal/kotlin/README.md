# Hexagonal Architecture API (Kotlin)

KotlinとSpring Bootを使用したヘキサゴナルアーキテクチャのAPIテンプレートです。

## アーキテクチャ

```
src/main/kotlin/
├── domain/           # ドメイン層（中心）
│   ├── model/       # エンティティ・値オブジェクト
│   └── port/        # ポート（インターフェース）
├── application/      # アプリケーション層
│   └── usecase/     # ユースケース
├── adapter/         # アダプター層（外側）
│   ├── input/       # 入力アダプター（REST API）
│   └── output/      # 出力アダプター（DB）
└── infrastructure/   # インフラ層
    └── config/      # 設定クラス
```

## 技術スタック

- **言語**: Kotlin
- **フレームワーク**: Spring Boot 3.2
- **データベース**: H2（インメモリ）
- **テスト**: JUnit 5, MockK
- **ビルドツール**: Gradle (Kotlin DSL)

## 機能

- User CRUD API
  - `POST /api/users` - ユーザー作成
  - `GET /api/users/{id}` - ユーザー取得
  - `GET /api/users` - 全ユーザー取得

## 実行方法

### ローカル実行

```bash
./gradlew bootRun
```

### Docker実行

```bash
docker-compose up --build
```

### テスト実行

```bash
./gradlew test
```

## API使用例

### ユーザー作成
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 30
  }'
```

### ユーザー取得
```bash
curl http://localhost:8080/api/users/{id}
```

### 全ユーザー取得
```bash
curl http://localhost:8080/api/users
```

## H2コンソール

開発時はH2コンソールにアクセス可能です：
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password