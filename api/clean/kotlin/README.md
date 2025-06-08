# Kotlin Clean Architecture API

KotlinとSpring Bootを使用したクリーンアーキテクチャのRESTful APIプロジェクトです。
初心者にも分かりやすく、クリーンアーキテクチャの概念と実装方法を学ぶことができます。

## 📚 目次

- [プロジェクト概要](#プロジェクト概要)
- [クリーンアーキテクチャとは](#クリーンアーキテクチャとは)
- [プロジェクト構造](#プロジェクト構造)
- [各層の詳細解説](#各層の詳細解説)
- [依存関係の流れ](#依存関係の流れ)
- [実装例：ユーザー管理機能](#実装例ユーザー管理機能)
- [セットアップ & 実行方法](#セットアップ--実行方法)
- [API使用方法](#api使用方法)
- [テスト](#テスト)
- [拡張方法](#拡張方法)

## 🎯 プロジェクト概要

このプロジェクトは、ロバート・C・マーティン（Uncle Bob）が提唱したクリーンアーキテクチャパターンを、KotlinとSpring Bootで実装したサンプルアプリケーションです。

### 🛠️ 使用技術

- **言語**: Kotlin 1.9.10
- **フレームワーク**: Spring Boot 3.1.5
- **データベース**: H2 Database (インメモリ)
- **ORM**: JPA/Hibernate
- **ビルドツール**: Gradle (Kotlin DSL)
- **テスト**: JUnit 5 + MockK

### 🎯 学習目標

- クリーンアーキテクチャの基本概念を理解する
- 依存関係逆転の原則を学ぶ
- テスト駆動開発（TDD）のアプローチを体験する
- 実際のプロジェクトでの実装方法を習得する

## 🏗️ クリーンアーキテクチャとは

クリーンアーキテクチャは、ソフトウェアを**4つの層**に分けて設計するアーキテクチャパターンです。

### 🌟 なぜクリーンアーキテクチャを使うのか？

1. **保守性**: コードの変更が他の部分に影響しにくい
2. **テスタビリティ**: 各層を独立してテストできる
3. **独立性**: フレームワークやデータベースに依存しない
4. **可読性**: 責任が明確に分離されている

### 📊 4つの層

```
┌─────────────────────────┐
│    Presentation Layer   │  ← 外側（フレームワーク依存）
├─────────────────────────┤
│    Application Layer    │
├─────────────────────────┤
│    Infrastructure Layer │
├─────────────────────────┤
│      Domain Layer       │  ← 内側（ビジネスロジック）
└─────────────────────────┘
```

#### 🎯 Domain Layer（ドメイン層）
- **役割**: ビジネスロジックとルールを定義
- **内容**: エンティティ、ビジネスルール、リポジトリインターフェース
- **依存**: 他の層に依存しない（最も内側）

#### 🔄 Application Layer（アプリケーション層）
- **役割**: ユースケースとアプリケーション固有のロジック
- **内容**: ユースケース、アプリケーションサービス
- **依存**: Domain層のみに依存

#### 🗄️ Infrastructure Layer（インフラストラクチャ層）
- **役割**: 外部システムとの連携
- **内容**: データベースアクセス、外部API、リポジトリ実装
- **依存**: Domain層とApplication層に依存

#### 🖥️ Presentation Layer（プレゼンテーション層）
- **役割**: ユーザーインターフェース（REST API）
- **内容**: コントローラー、DTO、リクエスト/レスポンス
- **依存**: Application層に依存

## 📁 プロジェクト構造

```
src/main/kotlin/com/example/api/
├── 📂 domain/                      # Domain Layer
│   ├── 📂 model/
│   │   └── 📄 User.kt             # ユーザーエンティティ
│   └── 📂 repository/
│       └── 📄 UserRepository.kt    # リポジトリインターフェース
│
├── 📂 application/                 # Application Layer
│   ├── 📂 usecase/
│   │   ├── 📄 CreateUserUseCase.kt # ユーザー作成ユースケース
│   │   ├── 📄 GetUserUseCase.kt    # ユーザー取得ユースケース
│   │   └── 📄 GetAllUsersUseCase.kt # 全ユーザー取得ユースケース
│   └── 📂 service/
│       └── 📄 UserService.kt       # アプリケーションサービス
│
├── 📂 infrastructure/              # Infrastructure Layer
│   ├── 📂 persistence/
│   │   └── 📄 UserJpaRepository.kt # JPA リポジトリ
│   ├── 📂 repository/
│   │   └── 📄 UserRepositoryImpl.kt # リポジトリ実装
│   └── 📂 config/
│       └── 📄 ApplicationConfig.kt  # DI設定
│
├── 📂 presentation/                # Presentation Layer
│   ├── 📂 controller/
│   │   └── 📄 UserController.kt    # REST コントローラー
│   └── 📂 dto/
│       ├── 📄 CreateUserRequest.kt  # リクエストDTO
│       └── 📄 UserResponse.kt       # レスポンスDTO
│
└── 📄 Application.kt               # Spring Boot メインクラス
```

## 🔍 各層の詳細解説

### 🎯 Domain Layer

#### User.kt - エンティティ
```kotlin
@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    val name: String,
    // ...タイムスタンプ
)
```

**ポイント**:
- ビジネスの中核となるデータ構造
- フレームワークに依存しない（JPAアノテーションは実装上の都合）
- 不変性を保つためのdata class

#### UserRepository.kt - リポジトリインターフェース
```kotlin
interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun deleteById(id: Long)
}
```

**ポイント**:
- データアクセスの抽象化
- 実装に依存しないインターフェース
- ドメイン層で定義、インフラ層で実装

### 🔄 Application Layer

#### CreateUserUseCase.kt - ユースケース
```kotlin
@Service
class CreateUserUseCase(private val userRepository: UserRepository) {
    fun execute(email: String, name: String): User {
        // ビジネスルール: 重複チェック
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User with email $email already exists")
        }
        
        val user = User(email = email, name = name)
        return userRepository.save(user)
    }
}
```

**ポイント**:
- 1つの具体的なユースケースを実装
- ビジネスルールを含む
- リポジトリインターフェースに依存

#### UserService.kt - アプリケーションサービス
```kotlin
@Service
class UserService(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) {
    fun createUser(email: String, name: String): User = 
        createUserUseCase.execute(email, name)
    // ...
}
```

**ポイント**:
- 複数のユースケースを組み合わせ
- プレゼンテーション層との窓口
- 薄いレイヤー（主にデリゲート）

### 🗄️ Infrastructure Layer

#### UserRepositoryImpl.kt - リポジトリ実装
```kotlin
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    
    override fun save(user: User): User = userJpaRepository.save(user)
    override fun findById(id: Long): User? = 
        userJpaRepository.findById(id).orElse(null)
    // ...
}
```

**ポイント**:
- ドメイン層のインターフェースを実装
- 具体的なデータアクセス技術（JPA）を使用
- ドメインオブジェクトと外部システムの橋渡し

### 🖥️ Presentation Layer

#### UserController.kt - RESTコントローラー
```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return try {
            val user = userService.createUser(request.email, request.name)
            ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
```

**ポイント**:
- HTTP リクエスト/レスポンスの処理
- DTOとドメインオブジェクトの変換
- エラーハンドリング

## 🔄 依存関係の流れ

### 📊 依存関係図

```
Presentation ─────➤ Application ─────➤ Domain
     │                    │               ↑
     │                    │               │
     └────────➤ Infrastructure ──────────┘
```

### 🔄 依存関係逆転の原則

**従来の問題**:
```
ビジネスロジック → データベース
```
ビジネスロジックがデータベースに依存してしまう

**クリーンアーキテクチャの解決**:
```
ビジネスロジック ← インターフェース ← データベース実装
```
データベースがビジネスロジックに依存する（逆転）

### 💡 メリット

1. **ビジネスロジックの独立性**: データベースが変わってもビジネスロジックは影響を受けない
2. **テスタビリティ**: モックを使って単体テストが簡単
3. **フレームワーク独立**: Spring以外のフレームワークにも移行可能

## 🎯 実装例：ユーザー管理機能

### 📝 ユーザー作成フローの詳細

```
1. クライアント → POST /api/users
   ├── Content-Type: application/json
   └── Body: {"email": "user@example.com", "name": "太郎"}

2. UserController.createUser()
   ├── CreateUserRequest DTOで受信
   └── UserService.createUser()を呼び出し

3. UserService.createUser()
   └── CreateUserUseCase.execute()を呼び出し

4. CreateUserUseCase.execute()
   ├── UserRepository.findByEmail()で重複チェック
   ├── 重複がなければ新しいUserオブジェクト作成
   └── UserRepository.save()でユーザー保存

5. UserRepositoryImpl.save()
   ├── UserJpaRepository.save()を呼び出し
   └── H2データベースに保存

6. レスポンス
   ├── Userオブジェクト → UserResponse DTOに変換
   └── 201 Created ステータスで返却
```

### 🧪 テストでの動作確認

#### ユニットテスト例（CreateUserUseCaseTest）
```kotlin
@Test
fun `should create user when email is unique`() {
    // Given
    val email = "test@example.com"
    val name = "Test User"
    
    every { userRepository.findByEmail(email) } returns null
    every { userRepository.save(any()) } returns savedUser
    
    // When
    val result = createUserUseCase.execute(email, name)
    
    // Then
    assertEquals(savedUser, result)
    verify { userRepository.findByEmail(email) }
    verify { userRepository.save(any()) }
}
```

**テストのポイント**:
- モックを使ってリポジトリをシミュレート
- ビジネスロジックのみをテスト
- 外部依存なしで高速実行

## 🚀 セットアップ & 実行方法

### 📋 前提条件

- Java 17以上
- Git

### 🔧 インストール手順

1. **リポジトリクローン**
```bash
git clone <repository-url>
cd kotlin-clean-api
```

2. **Java 17インストール（Ubuntu/WSL）**
```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

3. **ビルド**
```bash
./gradlew build
```

4. **アプリケーション起動**
```bash
./gradlew bootRun
```

### ✅ 起動確認

アプリケーションが正常に起動すると、以下のメッセージが表示されます：
```
Tomcat started on port(s): 8080 (http) with context path ''
```

## 🌐 API使用方法

### 📍 ベースURL
```
http://localhost:8080
```

### 📋 エンドポイント一覧

#### 1. ユーザー作成
```http
POST /api/users
Content-Type: application/json

{
  "email": "john@example.com",
  "name": "John Doe"
}
```

**レスポンス例（201 Created）**:
```json
{
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "createdAt": "2024-01-08T10:30:00",
  "updatedAt": "2024-01-08T10:30:00"
}
```

#### 2. ユーザー取得
```http
GET /api/users/1
```

**レスポンス例（200 OK）**:
```json
{
  "id": 1,
  "email": "john@example.com",
  "name": "John Doe",
  "createdAt": "2024-01-08T10:30:00",
  "updatedAt": "2024-01-08T10:30:00"
}
```

#### 3. 全ユーザー取得
```http
GET /api/users
```

**レスポンス例（200 OK）**:
```json
[
  {
    "id": 1,
    "email": "john@example.com",
    "name": "John Doe",
    "createdAt": "2024-01-08T10:30:00",
    "updatedAt": "2024-01-08T10:30:00"
  },
  {
    "id": 2,
    "email": "jane@example.com",
    "name": "Jane Smith",
    "createdAt": "2024-01-08T10:31:00",
    "updatedAt": "2024-01-08T10:31:00"
  }
]
```

### 🧪 cURLサンプル

#### ユーザー作成
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "name": "テストユーザー"
  }'
```

#### ユーザー取得
```bash
curl http://localhost:8080/api/users/1
```

#### 全ユーザー取得
```bash
curl http://localhost:8080/api/users
```

### 🗄️ H2データベースコンソール

開発時のデータ確認用：
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (空白)
```

## 🧪 テスト

### 🎯 テスト戦略

このプロジェクトでは**テスト駆動開発（TDD）**を採用しています：

1. **Unit Tests**: 各ユースケースの個別テスト
2. **Integration Tests**: コントローラーレベルのテスト
3. **Mock使用**: 外部依存をモックして高速テスト

### ⚡ テスト実行

```bash
# 全テスト実行
./gradlew test

# テスト結果の確認
./gradlew test --info
```

### 📊 テストカバレッジ確認

```bash
./gradlew jacocoTestReport
# build/reports/jacoco/test/html/index.html を確認
```

### 🔍 テスト例

#### ユニットテスト例
```kotlin
@Test
fun `should throw exception when email already exists`() {
    // Given
    val email = "test@example.com"
    val existingUser = User(1L, email, "Existing User", ...)
    every { userRepository.findByEmail(email) } returns existingUser

    // When & Then
    val exception = assertThrows<IllegalArgumentException> {
        createUserUseCase.execute(email, "New User")
    }
    assertEquals("User with email $email already exists", exception.message)
}
```

#### コントローラーテスト例
```kotlin
@Test
fun `should create user successfully`() {
    // Given
    val request = CreateUserRequest("test@example.com", "Test User")
    val user = User(1L, "test@example.com", "Test User", ...)
    every { userService.createUser(request.email, request.name) } returns user

    // When & Then
    mockMvc.perform(
        post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    )
        .andExpect(status().isCreated)
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("test@example.com"))
}
```

## 🔧 拡張方法

### 📚 新機能の追加手順（例：商品管理機能）

#### 1. Domain層の追加
```kotlin
// domain/model/Product.kt
data class Product(
    val id: Long? = null,
    val name: String,
    val price: BigDecimal,
    val description: String
)

// domain/repository/ProductRepository.kt
interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: Long): Product?
    fun findAll(): List<Product>
}
```

#### 2. Application層の追加
```kotlin
// application/usecase/CreateProductUseCase.kt
@Service
class CreateProductUseCase(private val productRepository: ProductRepository) {
    fun execute(name: String, price: BigDecimal, description: String): Product {
        // ビジネスルール
        require(price > BigDecimal.ZERO) { "Price must be positive" }
        
        val product = Product(name = name, price = price, description = description)
        return productRepository.save(product)
    }
}
```

#### 3. Infrastructure層の追加
```kotlin
// infrastructure/persistence/ProductJpaRepository.kt
@Repository
interface ProductJpaRepository : JpaRepository<Product, Long>

// infrastructure/repository/ProductRepositoryImpl.kt
@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun save(product: Product): Product = productJpaRepository.save(product)
    // ...
}
```

#### 4. Presentation層の追加
```kotlin
// presentation/dto/CreateProductRequest.kt
data class CreateProductRequest(
    val name: String,
    val price: BigDecimal,
    val description: String
)

// presentation/controller/ProductController.kt
@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {
    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> {
        // 実装
    }
}
```

#### 5. テストの追加
```kotlin
// test/.../CreateProductUseCaseTest.kt
class CreateProductUseCaseTest {
    @Test
    fun `should create product with valid data`() {
        // テスト実装
    }
    
    @Test
    fun `should throw exception when price is negative`() {
        // テスト実装
    }
}
```

### 🎯 ベストプラクティス

#### 1. 依存関係を正しく守る
```kotlin
// ❌ 悪い例：Domain層がInfrastructure層に依存
class User {
    fun save() {
        val repository = UserJpaRepository() // NG
        repository.save(this)
    }
}

// ✅ 良い例：依存関係逆転の原則
class CreateUserUseCase(private val userRepository: UserRepository) {
    fun execute(...): User {
        // userRepository はインターフェース
        return userRepository.save(user)
    }
}
```

#### 2. ビジネスロジックは Domain/Application 層に
```kotlin
// ❌ 悪い例：Controller にビジネスロジック
@PostMapping
fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
    if (request.email.isBlank()) { // ビジネスルールがControllerに
        return ResponseEntity.badRequest().build()
    }
    // ...
}

// ✅ 良い例：UseCase にビジネスロジック
class CreateUserUseCase {
    fun execute(email: String, name: String): User {
        require(email.isNotBlank()) { "Email is required" } // ビジネスルール
        require(name.isNotBlank()) { "Name is required" }
        // ...
    }
}
```

#### 3. DTOとエンティティの分離
```kotlin
// ✅ 適切な分離
data class CreateUserRequest(val email: String, val name: String) // API用DTO
data class User(val id: Long?, val email: String, val name: String) // ドメインエンティティ
data class UserResponse(val id: Long, val email: String, val name: String) // レスポンス用DTO
```

### 🚀 さらなる拡張のアイデア

1. **認証・認可の追加**
   - JWT トークン認証
   - Spring Security の導入

2. **バリデーション強化**
   - Bean Validation の活用
   - カスタムバリデーター

3. **ロギング・監視**
   - 構造化ログ
   - メトリクス収集

4. **データベース変更**
   - PostgreSQL への変更
   - マイグレーション（Flyway）

5. **外部API連携**
   - メール送信サービス
   - 外部認証プロバイダー

## 🤝 貢献

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 ライセンス

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📚 参考資料

- [The Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [JPA Documentation](https://spring.io/projects/spring-data-jpa)

---

🎉 **Happy Coding!** 

このプロジェクトでクリーンアーキテクチャを楽しく学んでください！質問や改善提案があれば、お気軽にIssueを作成してください。