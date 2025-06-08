# Kotlin Hexagonal Architecture API

KotlinとSpring Bootを使用したヘキサゴナルアーキテクチャ（ポート&アダプターパターン）のRESTful APIプロジェクトです。
初心者にも分かりやすく、ヘキサゴナルアーキテクチャの概念と実装方法を学ぶことができます。

## 📚 目次

- [プロジェクト概要](#プロジェクト概要)
- [ヘキサゴナルアーキテクチャとは](#ヘキサゴナルアーキテクチャとは)
- [プロジェクト構造](#プロジェクト構造)
- [各層の詳細解説](#各層の詳細解説)
- [ポート&アダプターの関係](#ポートアダプターの関係)
- [実装例：ユーザー管理機能](#実装例ユーザー管理機能)
- [セットアップ & 実行方法](#セットアップ--実行方法)
- [API使用方法](#api使用方法)
- [テスト](#テスト)
- [拡張方法](#拡張方法)

## 🎯 プロジェクト概要

このプロジェクトは、Alistair Cockburnが提唱したヘキサゴナルアーキテクチャ（別名：ポート&アダプターパターン）を、KotlinとSpring Bootで実装したサンプルアプリケーションです。

### 🛠️ 使用技術

- **言語**: Kotlin 1.9.10
- **フレームワーク**: Spring Boot 3.2
- **データベース**: H2 Database (インメモリ)
- **ORM**: JPA/Hibernate  
- **ビルドツール**: Gradle (Kotlin DSL)
- **テスト**: JUnit 5 + MockK
- **コンテナ**: Docker & Docker Compose

### 🎯 学習目標

- ヘキサゴナルアーキテクチャの基本概念を理解する
- ポート&アダプターパターンを学ぶ
- ドメイン駆動設計（DDD）のアプローチを体験する
- 外部依存の分離とテスタビリティを習得する

## 🏗️ ヘキサゴナルアーキテクチャとは

ヘキサゴナルアーキテクチャは、ソフトウェアを**中心から外側**へと層を分けて設計するアーキテクチャパターンです。
別名「ポート&アダプターパターン」とも呼ばれ、ビジネスロジックを外部の詳細から完全に分離することを目的としています。

### 🌟 なぜヘキサゴナルアーキテクチャを使うのか？

1. **外部依存からの独立性**: データベース、フレームワーク、外部APIに依存しない
2. **高いテスタビリティ**: 外部システムなしでビジネスロジックをテストできる
3. **柔軟性**: 外部システムを簡単に変更・置換できる
4. **保守性**: ビジネスロジックの変更が外部システムに影響しない

### 🔧 基本概念：ポート&アダプター

ヘキサゴナルアーキテクチャの核心は**ポート（Port）**と**アダプター（Adapter）**の概念です：

#### 🔌 ポート（Port）
- **定義**: アプリケーションとの契約を定義するインターフェース
- **役割**: 外部世界とのやり取りを抽象化
- **種類**: 入力ポート（Primary Port）と出力ポート（Secondary Port）

#### 🔗 アダプター（Adapter）
- **定義**: ポートの具体的な実装
- **役割**: 外部システムとアプリケーションを繋ぐ橋渡し
- **種類**: 入力アダプター（Driving Adapter）と出力アダプター（Driven Adapter）

### 📊 アーキテクチャ図

```
        入力アダプター                        出力アダプター
    ┌─────────────────────┐              ┌─────────────────────┐
    │   REST Controller   │              │  Database Adapter   │
    │   (Web API)         │              │  (JPA Repository)   │
    └──────────┬──────────┘              └──────────┬──────────┘
               │                                    │
               │ 入力ポート                          │ 出力ポート
    ┌──────────▼──────────┐              ┌──────────▼──────────┐
    │                     │              │                     │
    │                     │              │                     │
    │    Application      │◄────────────►│     Domain          │
    │    (Use Cases)      │              │   (Business Logic)  │
    │                     │              │                     │
    │                     │              │                     │
    └─────────────────────┘              └─────────────────────┘
            中心層                              中心層
```

### 🎯 レイヤーの説明

#### 🏛️ Domain Layer（ドメイン層）
- **役割**: ビジネスロジックとビジネスルールを定義
- **内容**: エンティティ、値オブジェクト、ドメインサービス
- **依存**: 他の層に依存しない（最も内側）

#### 🔄 Application Layer（アプリケーション層）
- **役割**: ユースケースとアプリケーション固有のロジック
- **内容**: ユースケース（Application Services）
- **依存**: Domain層のみに依存

#### 🔌 Adapter Layer（アダプター層）
- **役割**: 外部システムとアプリケーションの橋渡し
- **内容**: 
  - **入力アダプター**: REST Controller、CLI、GUI
  - **出力アダプター**: Database Repository、外部API Client
- **依存**: ApplicationとDomain層に依存

## 📁 プロジェクト構造

```
src/main/kotlin/com/example/hexagonal/
├── 📂 domain/                          # Domain Layer (Core)
│   ├── 📂 model/
│   │   ├── 📄 User.kt                 # ユーザーエンティティ
│   │   └── 📄 UserId.kt               # ユーザーID値オブジェクト
│   └── 📂 port/
│       └── 📄 UserRepository.kt        # 出力ポート（リポジトリ）
│
├── 📂 application/                     # Application Layer
│   └── 📂 usecase/
│       ├── 📄 CreateUserUseCase.kt    # ユーザー作成ユースケース
│       ├── 📄 GetUserUseCase.kt       # ユーザー取得ユースケース
│       └── 📄 GetAllUsersUseCase.kt   # 全ユーザー取得ユースケース
│
├── 📂 adapter/                        # Adapter Layer
│   ├── 📂 input/                      # 入力アダプター（Driving）
│   │   └── 📄 UserController.kt       # REST API アダプター
│   └── 📂 output/                     # 出力アダプター（Driven）
│       ├── 📄 UserEntity.kt           # JPA エンティティ
│       ├── 📄 UserJpaRepository.kt    # JPA リポジトリ
│       └── 📄 UserRepositoryImpl.kt   # リポジトリ実装
│
└── 📄 HexagonalApiApplication.kt       # Spring Boot メインクラス
```

### 🔄 依存関係の流れ

```
入力アダプター ─────➤ Application ─────➤ Domain
                      │               ↑
                      │               │
                      └─➤ 出力アダプター ──┘
```

**ポイント**:
- ドメイン層は他の層に依存しない
- アプリケーション層はドメイン層のみに依存
- アダプター層はアプリケーション層とドメイン層に依存
- 外部システム（DB、Web）はアダプター層でのみ扱う

## 🔍 各層の詳細解説

### 🏛️ Domain Layer

#### User.kt - エンティティ
```kotlin
data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val age: Int
) {
    companion object {
        fun create(name: String, email: String, age: Int): User {
            validateName(name)
            validateEmail(email) 
            validateAge(age)
            
            return User(
                id = UserId.generate(),
                name = name,
                email = email,
                age = age
            )
        }
    }
}
```

**ポイント**:
- ビジネスルールを内包（バリデーション）
- フレームワークに依存しない純粋なKotlinクラス
- ファクトリーメソッドでオブジェクト作成を制御

#### UserId.kt - 値オブジェクト
```kotlin
@JvmInline
value class UserId(val value: String) {
    companion object {
        fun generate(): UserId = UserId(UUID.randomUUID().toString())
        fun of(value: String): UserId = UserId(value)
    }
}
```

**ポイント**:
- 型安全性を提供（String の誤用を防ぐ）
- ビジネス的な意味を持つ値を表現
- value class でメモリ効率を向上

#### UserRepository.kt - 出力ポート
```kotlin
interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findAll(): List<User>
    fun deleteById(id: UserId): Boolean
}
```

**ポイント**:
- データアクセスの抽象化
- 実装に依存しないインターフェース
- ドメイン層で定義、アダプター層で実装

### 🔄 Application Layer

#### CreateUserUseCase.kt - ユースケース
```kotlin
@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(request: CreateUserRequest): CreateUserResponse {
        val user = User.create(
            name = request.name,
            email = request.email,
            age = request.age
        )
        
        val savedUser = userRepository.save(user)
        
        return CreateUserResponse(
            id = savedUser.id.value,
            name = savedUser.name,
            email = savedUser.email,
            age = savedUser.age
        )
    }
}
```

**ポイント**:
- 1つの具体的なユースケースを実装
- ドメインオブジェクトを使用してビジネスロジックを実行
- 入出力データの変換を行う

### 🔗 Adapter Layer

#### 入力アダプター: UserController.kt
```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) {
    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val useCaseRequest = CreateUserUseCase.CreateUserRequest(
            name = request.name,
            email = request.email,
            age = request.age
        )
        
        val response = createUserUseCase.execute(useCaseRequest)
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreateUserResponse(
                id = response.id,
                name = response.name,
                email = response.email,
                age = response.age
            )
        )
    }
}
```

**ポイント**:
- HTTP プロトコルの詳細を隠蔽
- Web DTOとユースケースDTOの変換
- Spring Boot 固有のアノテーションを使用

#### 出力アダプター: UserRepositoryImpl.kt
```kotlin
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    
    override fun save(user: User): User {
        val userEntity = UserEntity.fromDomain(user)
        val savedEntity = userJpaRepository.save(userEntity)
        return savedEntity.toDomain()
    }
    
    override fun findById(id: UserId): User? {
        return userJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }
}
```

**ポイント**:
- ドメイン層のインターフェースを実装
- ドメインオブジェクトとJPAエンティティの相互変換
- データベースの詳細を隠蔽

#### UserEntity.kt - JPA エンティティ
```kotlin
@Entity
@Table(name = "users")
data class UserEntity(
    @Id val id: String,
    @Column(nullable = false) val name: String,
    @Column(nullable = false, unique = true) val email: String,
    @Column(nullable = false) val age: Int
) {
    fun toDomain(): User {
        return User(
            id = UserId.of(id),
            name = name,
            email = email,
            age = age
        )
    }

    companion object {
        fun fromDomain(user: User): UserEntity {
            return UserEntity(
                id = user.id.value,
                name = user.name,
                email = user.email,
                age = user.age
            )
        }
    }
}
```

**ポイント**:
- データベース永続化のためのエンティティ
- ドメインオブジェクトとの変換メソッドを提供
- JPAアノテーションでマッピング定義

## 🔌 ポート&アダプターの関係

### 📥 入力側（Primary Side）

```
HTTP Request → UserController → CreateUserUseCase → UserRepository
    ↓             ↓                 ↓                 ↓
  外部世界      入力アダプター      アプリケーション      出力ポート
```

**フロー**:
1. **HTTP Request**: クライアントからのリクエスト
2. **UserController**: REST APIアダプター（入力アダプター）
3. **CreateUserUseCase**: ビジネスロジック実行
4. **UserRepository**: データ永続化の抽象化（出力ポート）

### 📤 出力側（Secondary Side）

```
UserRepository → UserRepositoryImpl → UserJpaRepository → Database
    ↓               ↓                    ↓                  ↓
  出力ポート      出力アダプター         JPA実装           外部世界
```

**フロー**:
1. **UserRepository**: インターフェース（出力ポート）
2. **UserRepositoryImpl**: 具体的な実装（出力アダプター）
3. **UserJpaRepository**: Spring Data JPA
4. **Database**: 実際のデータベース

### 💡 メリット

1. **テスタビリティ**: モックで外部依存を置換可能
2. **置換可能性**: データベースやWebフレームワークを簡単に変更
3. **独立性**: ビジネスロジックが外部システムに依存しない

## 🎯 実装例：ユーザー管理機能

### 📝 ユーザー作成フローの詳細

```
1. クライアント → POST /api/users
   ├── Content-Type: application/json
   └── Body: {"name": "太郎", "email": "taro@example.com", "age": 25}

2. UserController.createUser() [入力アダプター]
   ├── Web DTOで受信
   ├── ユースケースDTOに変換
   └── CreateUserUseCase.execute()を呼び出し

3. CreateUserUseCase.execute() [アプリケーション層]
   ├── User.create()でドメインオブジェクト作成
   │   ├── バリデーション実行（名前、メール、年齢）
   │   └── UserId.generate()でID生成
   └── UserRepository.save()でユーザー保存

4. UserRepositoryImpl.save() [出力アダプター]
   ├── User → UserEntity に変換
   ├── UserJpaRepository.save()を呼び出し
   ├── H2データベースに保存
   └── UserEntity → User に変換して返却

5. レスポンス
   ├── User → ユースケースレスポンスに変換
   ├── ユースケースレスポンス → Web DTOに変換
   └── 201 Created ステータスで返却
```

### 🧪 テストでの動作確認

#### ユニットテスト例（CreateUserUseCaseTest）
```kotlin
@Test
fun `should create user successfully`() {
    // Given
    val request = CreateUserUseCase.CreateUserRequest(
        name = "John Doe",
        email = "john.doe@example.com",
        age = 30
    )

    val expectedUser = User.create(request.name, request.email, request.age)
    every { userRepository.save(any()) } returns expectedUser

    // When
    val result = createUserUseCase.execute(request)

    // Then
    assertEquals(expectedUser.name, result.name)
    assertEquals(expectedUser.email, result.email)
    assertEquals(expectedUser.age, result.age)
    verify { userRepository.save(any()) }
}
```

**テストのポイント**:
- モックを使ってリポジトリ（出力ポート）をシミュレート
- ビジネスロジックのみをテスト
- 外部依存なしで高速実行

## 🚀 セットアップ & 実行方法

### 📋 前提条件

- Java 17以上
- Docker & Docker Compose（オプション）
- Git

### 🔧 インストール手順

1. **リポジトリクローン**
```bash
git clone <repository-url>
cd hexagonal-kotlin-api
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

### 🐳 Docker実行

```bash
# Docker Compose で実行
docker-compose up --build

# 個別ビルド&実行
docker build -t hexagonal-api .
docker run -p 8080:8080 hexagonal-api
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
  "name": "John Doe",
  "email": "john.doe@example.com", 
  "age": 30
}
```

**レスポンス例（201 Created）**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 30
}
```

#### 2. ユーザー取得
```http
GET /api/users/{id}
```

**レスポンス例（200 OK）**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe", 
  "email": "john.doe@example.com",
  "age": 30
}
```

#### 3. 全ユーザー取得
```http
GET /api/users
```

**レスポンス例（200 OK）**:
```json
{
  "users": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "age": 30
    },
    {
      "id": "6ba7b810-9dad-11d1-80b4-00c04fd430c8",
      "name": "Jane Smith",
      "email": "jane.smith@example.com",
      "age": 25
    }
  ]
}
```

### 🧪 cURLサンプル

#### ユーザー作成
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "テストユーザー",
    "email": "test@example.com",
    "age": 28
  }'
```

#### ユーザー取得
```bash
curl http://localhost:8080/api/users/550e8400-e29b-41d4-a716-446655440000
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
Password: password
```

## 🧪 テスト

### 🎯 テスト戦略

このプロジェクトでは**ヘキサゴナルアーキテクチャの利点**を活かしたテストを採用しています：

1. **Unit Tests**: ドメインロジックとユースケースの個別テスト
2. **Integration Tests**: アダプター層のテスト
3. **Contract Tests**: ポートの契約テスト

### ⚡ テスト実行

```bash
# 全テスト実行
./gradlew test

# テスト結果の確認
./gradlew test --info

# Docker環境でのテスト
docker-compose -f docker-compose.yml -f Dockerfile.test run --rm test
```

### 📊 テストカバレッジ確認

```bash
./gradlew jacocoTestReport
# build/reports/jacoco/test/html/index.html を確認
```

### 🔍 テスト例

#### ドメインテスト例
```kotlin
@Test
fun `should create user with valid data`() {
    // When
    val user = User.create("John Doe", "john@example.com", 30)
    
    // Then
    assertEquals("John Doe", user.name)
    assertEquals("john@example.com", user.email)
    assertEquals(30, user.age)
    assertNotNull(user.id)
}

@Test  
fun `should throw exception when email is invalid`() {
    // When & Then
    assertThrows<IllegalArgumentException> {
        User.create("John Doe", "invalid-email", 30)
    }
}
```

#### ユースケーステスト例
```kotlin
@Test
fun `should create user successfully`() {
    // Given
    val request = CreateUserUseCase.CreateUserRequest(
        name = "John Doe",
        email = "john.doe@example.com",
        age = 30
    )

    val expectedUser = User.create(request.name, request.email, request.age)
    every { userRepository.save(any()) } returns expectedUser

    // When
    val result = createUserUseCase.execute(request)

    // Then
    assertEquals(expectedUser.name, result.name)
    verify { userRepository.save(any()) }
}
```

## 🔧 拡張方法

### 📚 新機能の追加手順（例：商品管理機能）

#### 1. Domain層の追加
```kotlin
// domain/model/Product.kt
data class Product(
    val id: ProductId,
    val name: String,
    val price: BigDecimal,
    val description: String
) {
    companion object {
        fun create(name: String, price: BigDecimal, description: String): Product {
            require(name.isNotBlank()) { "Product name cannot be empty" }
            require(price > BigDecimal.ZERO) { "Price must be positive" }
            
            return Product(
                id = ProductId.generate(),
                name = name,
                price = price,
                description = description
            )
        }
    }
}

// domain/port/ProductRepository.kt  
interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: ProductId): Product?
    fun findAll(): List<Product>
}
```

#### 2. Application層の追加
```kotlin
// application/usecase/CreateProductUseCase.kt
@Service
class CreateProductUseCase(
    private val productRepository: ProductRepository
) {
    fun execute(request: CreateProductRequest): CreateProductResponse {
        val product = Product.create(
            name = request.name,
            price = request.price,
            description = request.description
        )
        
        val savedProduct = productRepository.save(product)
        
        return CreateProductResponse(
            id = savedProduct.id.value,
            name = savedProduct.name,
            price = savedProduct.price,
            description = savedProduct.description
        )
    }
}
```

#### 3. Adapter層の追加
```kotlin
// adapter/input/ProductController.kt
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val createProductUseCase: CreateProductUseCase
) {
    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<CreateProductResponse> {
        // 実装
    }
}

// adapter/output/ProductRepositoryImpl.kt
@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun save(product: Product): Product {
        val entity = ProductEntity.fromDomain(product)
        val savedEntity = productJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}
```

### 🎯 ベストプラクティス

#### 1. ポートとアダプターの分離
```kotlin
// ❌ 悪い例：ドメイン層でSpringに依存
@Entity  // JPA依存
data class User(...) 

// ✅ 良い例：ドメイン層は純粋なKotlin
data class User(...) // フレームワーク非依存

@Entity  // アダプター層でJPA使用
data class UserEntity(...)
```

#### 2. 依存関係の方向を守る
```kotlin
// ❌ 悪い例：ドメインがアダプターに依存
class User {
    fun save() {
        UserJpaRepository().save(this) // NG
    }
}

// ✅ 良い例：アダプターがドメインに依存
class CreateUserUseCase(private val userRepository: UserRepository) {
    fun execute(...): User {
        return userRepository.save(user) // OK
    }
}
```

#### 3. ビジネスロジックはドメイン層に
```kotlin
// ❌ 悪い例：コントローラーにビジネスロジック
@PostMapping
fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<*> {
    if (request.age < 0) { // ビジネスルールがコントローラーに
        return ResponseEntity.badRequest().build()
    }
    // ...
}

// ✅ 良い例：ドメインにビジネスロジック
data class User(...) {
    companion object {
        fun create(name: String, email: String, age: Int): User {
            require(age >= 0) { "Age cannot be negative" } // ビジネスルール
            // ...
        }
    }
}
```

### 🚀 さらなる拡張のアイデア

1. **異なる入力アダプター**
   - GraphQL API
   - メッセージキュー（RabbitMQ、Kafka）
   - コマンドラインインターフェース

2. **異なる出力アダプター**
   - MongoDB Repository
   - Redis Cache
   - 外部API Client

3. **ドメインの強化**
   - イベント駆動アーキテクチャ
   - ドメインイベント
   - 集約（Aggregate）パターン

4. **品質向上**
   - アーキテクチャテスト（ArchUnit）
   - API契約テスト（Spring Cloud Contract）
   - パフォーマンステスト

## 🤝 貢献

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 ライセンス

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📚 参考資料

- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Ports & Adapters Pattern](https://jmgarridopaz.github.io/content/hexagonalarchitecture.html)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

---

🎉 **Happy Coding!** 

このプロジェクトでヘキサゴナルアーキテクチャを楽しく学んでください！質問や改善提案があれば、お気軽にIssueを作成してください。