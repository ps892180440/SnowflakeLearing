
# Snowflake 新規プロジェクトメンバー研修マニュアル

> **対象バージョン**: Snowflake Enterprise Edition 以上
> **マニュアルの目的**: 新規プロジェクトメンバーがゼロから Snowflake のコア技術スタックを迅速に習得し、データ開発と運用保守を独力で遂行できるようにする
> **最終更新**: 2026-05-12

---

## 目次

- [1. Snowflake 概要](#1-snowflake-概要)
  - [1.1 Snowflake とは](#11-snowflake-とは)
  - [1.2 コアアーキテクチャ](#12-コアアーキテクチャ)
  - [1.3 従来のデータベースとの比較](#13-従来のデータベースとの比較)
  - [1.4 対応クラウドプラットフォームとリージョン](#14-対応クラウドプラットフォームとリージョン)
- [2. クイックスタート](#2-クイックスタート)
  - [2.1 アクセス方法の概要](#21-アクセス方法の概要)
  - [2.2 Snowsight (Web UI) 操作ガイド](#22-snowsight-web-ui-操作ガイド)
  - [2.3 SnowSQL (CLI) インストールと設定](#23-snowsql-cli-インストールと設定)
  - [2.4 SDK & コネクター接続](#24-sdk--コネクター接続)
- [3. コアオブジェクト階層構造](#3-コアオブジェクト階層構造)
  - [3.1 オブジェクト階層の概要](#31-オブジェクト階層の概要)
  - [3.2 Database](#32-database-データベース)
  - [3.3 Schema](#33-schema-スキーマ)
  - [3.4 Table](#34-table-テーブル)
  - [3.5 View](#35-view-ビュー)
  - [3.6 Materialized View](#36-materialized-view-マテリアライズドビュー)
  - [3.7 Stage](#37-stage-ステージ)
  - [3.8 File Format](#38-file-format-ファイル形式)
  - [3.9 Pipe](#39-pipe-パイプ)
  - [3.10 Sequence](#310-sequence-シーケンス)
  - [3.11 Stored Procedure](#311-stored-procedure-ストアドプロシージャ)
  - [3.12 UDF / UDTF](#312-udf--udtf-ユーザー定義関数)
- [4. データ型詳細](#4-データ型詳細)
  - [4.1 数値型](#41-数値型)
  - [4.2 文字列型](#42-文字列型)
  - [4.3 日付・時刻型](#43-日付・時刻型)
  - [4.4 半構造化データ型](#44-半構造化データ型)
  - [4.5 その他の型](#45-その他の型)
  - [4.6 型変換](#46-型変換)
- [5. DDL 完全構文リファレンス](#5-ddl-完全構文リファレンス)
  - [5.1 CREATE DATABASE](#51-create-database)
  - [5.2 ALTER DATABASE](#52-alter-database)
  - [5.3 DROP DATABASE](#53-drop-database)
  - [5.4 CREATE SCHEMA](#54-create-schema)
  - [5.5 CREATE TABLE](#55-create-table)
  - [5.6 ALTER TABLE](#56-alter-table)
  - [5.7 DROP TABLE](#57-drop-table)
  - [5.8 TRUNCATE TABLE](#58-truncate-table)
  - [5.9 CREATE VIEW](#59-create-view)
  - [5.10 CREATE MATERIALIZED VIEW](#510-create-materialized-view)
  - [5.11 CREATE STAGE](#511-create-stage)
  - [5.12 CREATE FILE FORMAT](#512-create-file-format)
  - [5.13 CREATE PIPE](#513-create-pipe)
  - [5.14 CREATE SEQUENCE](#514-create-sequence)
  - [5.15 CREATE FUNCTION (UDF)](#515-create-function-udf)
  - [5.16 CREATE PROCEDURE](#516-create-procedure)
  - [5.17 CREATE TASK](#517-create-task)
  - [5.18 CREATE STREAM](#518-create-stream)
  - [5.19 CREATE USER](#519-create-user)
  - [5.20 CREATE ROLE](#520-create-role)
  - [5.21 CREATE WAREHOUSE](#521-create-warehouse)
- [6. DML 完全構文リファレンス](#6-dml-完全構文リファレンス)
  - [6.1 INSERT](#61-insert)
  - [6.2 UPDATE](#62-update)
  - [6.3 DELETE](#63-delete)
  - [6.4 MERGE](#64-merge)
  - [6.5 COPY INTO <table>](#65-copy-into-table-データロード)
  - [6.6 COPY INTO <location>](#66-copy-into-location-データアンロード)
- [7. SELECT クエリ完全構文](#7-select-クエリ完全構文)
  - [7.1 SELECT 完全構文](#71-select-完全構文)
  - [7.2 JOIN 種類の詳細](#72-join-種類の詳細)
  - [7.3 サブクエリと CTE](#73-サブクエリと-cte)
  - [7.4 ウィンドウ関数](#74-ウィンドウ関数)
  - [7.5 集合演算](#75-集合演算-union--intersect--except)
  - [7.6 半構造化データクエリ](#76-半構造化データクエリ-flatten--lateral)
  - [7.7 QUALIFY 子句](#77-qualify-子句)
  - [7.8 SAMPLE / TABLESAMPLE](#78-sample--tablesample-サンプリングクエリ)
  - [7.9 PIVOT / UNPIVOT](#79-pivot--unpivot)
  - [7.10 CONNECT BY 階層クエリ](#710-connect-by-階層クエリ)
- [8. データロードとアンロード](#8-データロードとアンロード)
  - [8.1 Stage 詳細](#81-stage-詳細)
  - [8.2 PUT コマンド](#82-put-コマンド-ファイルアップロード)
  - [8.3 COPY INTO ロード](#83-copy-into-ロード)
  - [8.4 VALIDATION_MODE](#84-validation_mode-データ検証)
  - [8.5 Snowpipe 自動ロード](#85-snowpipe-自動ロード)
  - [8.6 データアンロード (UNLOAD)](#86-データアンロード-unload)
- [9. Time Travel と Fail-safe](#9-time-travel-と-fail-safe)
  - [9.1 Time Travel 概述](#91-time-travel-概述)
  - [9.2 Time Travel 操作手順](#92-time-travel-操作手順)
  - [9.3 UNDROP 恢复对象](#93-undrop-恢复对象)
  - [9.4 Fail-safe 概述](#94-fail-safe-概述)
  - [9.5 数据保留策略配置](#95-数据保留策略配置)
- [10. Zero-Copy Cloning](#10-zero-copy-cloning-ゼロコピークローン)
  - [10.1 克隆原理](#101-克隆原理)
  - [10.2 クローン操作手順](#102-クローン操作手順)
  - [10.3 クローン権限と注意事項](#103-クローン権限と注意事項)
- [11. Data Sharing](#11-data-sharing-データ共有)
  - [11.1 データ共有アーキテクチャ](#111-データ共有アーキテクチャ)
  - [11.2 创建与配置 Share](#112-创建与配置-share)
  - [11.3 Reader Account](#113-创建-reader-account)
  - [11.4 从 Share 创建数据库](#114-从-share-创建数据库)
  - [11.5 Data Marketplace](#115-data-marketplace)
- [12. セキュリティとアクセス制御 (RBAC)](#12-セキュリティとアクセス制御-rbac)
  - [12.1 RBAC 模型概述](#121-rbac-模型概述)
  - [12.2 ユーザー管理](#122-ユーザー管理操作手順)
  - [12.3 ロール管理](#123-ロール管理操作手順)
  - [12.4 権限付与と取り消し](#124-権限付与と取り消し)
  - [12.5 ネットワークポリシー](#125-ネットワークポリシー)
  - [12.6 データ暗号化](#126-データ暗号化)
  - [12.7 Column-Level Security](#127-column-level-security-列レベルセキュリティ)
  - [12.8 Row-Level Security](#128-row-level-security-行レベルセキュリティ)
- [13. Virtual Warehouse とパフォーマンス](#13-virtual-warehouse-とパフォーマンス)
  - [13.1 Warehouse 概要](#131-warehouse-概要)
  - [13.2 Warehouse 作成と設定](#132-warehouse-作成と設定)
  - [13.3 Multi-cluster Warehouse](#133-multi-cluster-warehouse)
  - [13.4 クエリパフォーマンス最適化](#134-クエリパフォーマンス最適化)
  - [13.5 キャッシュ機構](#135-結果キャッシュ--メタデータキャッシュ--データキャッシュ)
  - [13.6 Micro-partition 与 Clustering](#136-micro-partition-与-clustering)
  - [13.7 マテリアライズドビュー高速化](#137-マテリアライズドビューとクエリ高速化)
  - [13.8 Search Optimization Service](#138-search-optimization-service)
  - [13.9 Query Profile](#139-query-profile-パフォーマンス分析)
- [14. 半構造化データ処理](#14-半構造化データ処理)
  - [14.1 VARIANT 型操作](#141-variant-型操作)
  - [14.2 FLATTEN 関数詳細](#142-flatten-関数詳細)
  - [14.3 PARSE_JSON / PARSE_XML](#143-parse_json--parse_xml)
  - [14.4 JSON / Parquet / Avro / XML のロード](#144-json--parquet--avro--xml-データのロード)
  - [14.5 半構造化データ最適化](#145-半構造化データ最適化)
- [15. UDF / UDTF / ストアドプロシージャ](#15-udf--udtf--ストアドプロシージャ)
  - [15.1 SQL UDF 作成と使用](#151-sql-udf-作成と使用)
  - [15.2 JavaScript UDF 作成と使用](#152-javascript-udf-作成と使用)
  - [15.3 Python UDF 作成と使用](#153-python-udf-作成と使用)
  - [15.4 Java UDF 作成と使用](#154-java-udf-作成と使用)
  - [15.5 UDTF (テーブル関数) 作成と使用](#155-udtf-テーブル関数-作成と使用)
  - [15.6 JavaScript ストアドプロシージャ](#156-javascript-ストアドプロシージャ)
  - [15.7 Python ストアドプロシージャ](#157-python-ストアドプロシージャ)
  - [15.8 SQL ストアドプロシージャ (Snowflake Scripting)](#158-sql-ストアドプロシージャ-snowflake-scripting)
- [16. Streams & Tasks](#16-streams--tasks-ストリームとタスク)
  - [16.1 Stream 概要と種類](#161-stream-概要と種類)
  - [16.2 Standard Stream 操作手順](#162-standard-stream-操作手順)
  - [16.3 Append-Only Stream 操作手順](#163-append-only-stream-操作手順)
  - [16.4 Task 作成と管理](#164-task-作成と管理)
  - [16.5 Stream + Task 増分 ETL](#165-stream--task-増分-etl-パイプライン構築)
- [17. アカウントとリソース管理](#17-アカウントとリソース管理)
  - [17.1 アカウント構造](#171-アカウント構造)
  - [17.2 Resource Monitor 作成と使用](#172-resource-monitor-作成と使用)
  - [17.3 情報スキーマと Account Usage](#173-情報スキーマと-account-usage)
  - [17.4 コスト管理](#174-コスト管理)
- [18. ベストプラクティスとよくある問題](#18-ベストプラクティスとよくある問題)
  - [18.1 テーブル設計ベストプラクティス](#181-テーブル設計ベストプラクティス)
  - [18.2 クエリ最適化ベストプラクティス](#182-クエリ最適化ベストプラクティス)
  - [18.3 データロードベストプラクティス](#183-データロードベストプラクティス)
  - [18.4 セキュリティベストプラクティス](#184-セキュリティベストプラクティス)
  - [18.5 コスト管理ベストプラクティス](#185-コスト管理ベストプラクティス)
  - [18.6 よくある問題と解決策](#186-よくある問題と解決策)

---

## 1. Snowflake 概要

### 1.1 Snowflake とは

Snowflake は**完全マネージド (Fully Managed)** の**クラウドネイティブデータプラットフォーム**であり、**SaaS (Software-as-a-Service)** モデルで提供されます。データウェアハウス、データレイク、データエンジニアリング、データサイエンス、データ共有、マーケットプレイスなどのソリューションを提供し、中核製品は Snowflake Data Cloud です。

**主な特徴：**

| 特性 | 描述 |
|------|------|
| **ストレージとコンピュートの分離** | ストレージ層とコンピュート層が完全に独立しており、個別にスケール可能 |
| **弾力的なスケーリング** | コンピュートリソース（Warehouse）を動的に拡大縮小可能、自動停止/再開をサポート |
| **ゼロ管理** | ハードウェア、OS、データベースソフトウェア、パッチ、インデックス管理が不要 |
| **マルチクラウド対応** | AWS、Azure、GCP の3大クラウドプラットフォームをサポート |
| **クロスクラウドデータ共有** | 異なるクラウドプラットフォーム間の Snowflake アカウントで安全にデータ共有が可能 |
| **Time Travel** | データを過去の任意の時点に遡って参照可能（最大90日） |
| **Zero-Copy Cloning** | 完全なコピーを秒単位で作成、追加のストレージを消費しない |
| **セキュリティとコンプライアンス** | IP許可リスト、MFA、SOC 2 Type II、HIPAA、PCI DSS、FedRAMP |
| **半構造化データ** | JSON、Avro、Parquet、ORC、XML のクエリと最適化ストレージをネイティブサポート |

---

### 1.2 コアアーキテクチャ

Snowflake はハイブリッドアーキテクチャを採用し、3つの独立した層で構成されています：

```
+---------------------------------------------------+
|              Cloud Services 層                     |
|  認証              |  クエリ最適化       |  メタデータ  |
|  アクセス制御      |  インフラ管理       |  トランザクション管理 |
+---------------------------------------------------+
|              コンピュート層                        |
|  Virtual Warehouse (XS / S / M / L / XL / 2XL~6XL)|
|  サーバーレスコンピュート                          |
+---------------------------------------------------+
|              ストレージ層                          |
|  ブロブストレージ (S3 / Azure Blob / GCS)          |
|  列指向圧縮 | AES-256 暗号化 | マイクロパーティション |
+---------------------------------------------------+
```

**3層の詳細：**

| 层 | 职责 | 收费方式 |
|----|------|----------|
| **Cloud Services** | 認証・認可、クエリ解析最適化、メタデータ管理、トランザクション管理 | クレジット使用量に応じて課金 |
| **Compute** | Virtual Warehouse がクエリとDMLを実行 | Warehouse 稼働時間に応じて課金 (クレジット/時間) |
| **Storage** | 列指向圧縮ですべてのデータを格納 | 圧縮後のデータ量に応じて課金 (TB/月) |

**重要なポイント：**
- ストレージとコンピュートは完全に分離——すべての Warehouse を停止してもデータは安全に保存され、その間はストレージ費用のみが発生
- 各 Warehouse は独立したコンピュートクラスターであり、互いに影響しない（ワークロード分離）
- クエリはストレージ層からデータを読み取りコンピュート層で実行し、結果を返した後コンピュート層のメモリを解放

---

### 1.3 従来のデータベースとの比較

| 特性 | Snowflake | 従来のデータベース (Oracle/PostgreSQL/SQL Server) |
|------|-----------|------------------------------------------|
| デプロイ方式 | SaaS、自己管理不要 | インストール、設定、パッチ適用、メンテナンスが必要 |
| ストレージとコンピュート | 完全分離、独立してスケール | 密結合 |
| 弾力性 | オンデマンドでスケール、秒単位で応答 | 固定リソース、拡張にダウンタイムが必要 |
| 同時実行 | 複数 Warehouse でワークロードを分離 | 共有リソースプール、リソース競合 |
| インデックス | 手動作成不要 | 手動作成・メンテナンスが必要 |
| 半構造化 | ネイティブ VARIANT 型 | JSON/XML 拡張が必要 |
| データ共有 | ネイティブサポート、ゼロコピー | ETL、API、ファイルエクスポートが必要 |
| ゼロコピークローン | サポート、秒単位で完了 | 非サポート |
| Time Travel | サポート、最大90日 | 独自実装が必要 |
| 自動停止 | サポート、アイドル時に自動停止 | 非サポート |

---

### 1.4 対応クラウドプラットフォームとリージョン

| クラウドプラットフォーム | 主な利用可能リージョン |
|--------|-------------|
| **AWS** | us-east-1, us-west-2, eu-central-1, eu-west-1, ap-southeast-1, ap-southeast-2, ap-northeast-1, ca-central-1, ap-south-1, sa-east-1, us-east-2, eu-west-2 他 |
| **Azure** | eastus2, westeurope, southeastasia, canadacentral, australiaeast 他 |
| **GCP** | us-central1, europe-west4 他 |

---

## 2. クイックスタート

### 2.1 アクセス方法の概要

| 方式 | ユースケース | 説明 |
|------|----------|------|
| **Snowsight (Web UI)** | クエリ開発、管理、可視化 | `https://app.snowflake.com` |
| **Classic Console** | 従来のクエリ開発 | 旧バージョン Web インターフェース |
| **SnowSQL (CLI)** | スクリプト、自動化、バッチ処理 | コマンドラインクライアント |
| **Python Connector** | Python アプリケーション | `pip install snowflake-connector-python` |
| **JDBC Driver** | Java アプリケーション | Maven/Gradle 依存関係 |
| **ODBC Driver** | .NET / BI ツール | システムレベルドライバー |
| **Node.js Driver** | Node.js アプリケーション | npm インストール |
| **Go Driver** | Go アプリケーション | `go get github.com/snowflakedb/gosnowflake` |
| **Spark Connector** | Apache Spark 統合 | Spark パッケージ |
| **Kafka Connector** | Kafka リアルタイムストリーム | Confluent Hub |

---

### 2.2 Snowsight (Web UI) 操作手順

**ステップ 1: ログイン**
1. ブラウザで `https://app.snowflake.com` にアクセス
2. アカウント識別子 (Account Identifier) を入力

   形式：`<orgname>-<account_name>` または `<account_locator>.<region>.<cloud>`

   例：`myorg-myaccount` または `xy12345.us-east-1.aws`

3. ユーザー名とパスワードを入力
4. （MFA 有効時）Duo Mobile / Microsoft Authenticator の確認コードを入力

**ステップ 2: SQL Worksheet の作成**
1. 左側ナビゲーションから **Projects** -> **Worksheets** をクリック
2. 右上の **+ Worksheet** ボタンをクリック
3. **SQL Worksheet** を選択

**ステップ 3: 最初のクエリを実行**

```sql
-- 現在のセッションコンテキストを表示
SELECT CURRENT_ROLE();
SELECT CURRENT_WAREHOUSE();
SELECT CURRENT_DATABASE();
SELECT CURRENT_SCHEMA();
SELECT CURRENT_USER();

-- 表示可能なすべてのデータベースを表示
SHOW DATABASES;

-- すべての Warehouse を表示
SHOW WAREHOUSES;
```

**ステップ 4: データベースオブジェクトの参照**
1. 左側ナビゲーションから **Data** -> **Databases** をクリック
2. `SNOWFLAKE` -> `ACCOUNT_USAGE` を展開し、システム共有のアカウント使用データを表示
3. 任意のテーブル名をクリックするとデータをプレビュー可能

**ステップ 5: セッションコンテキストの設定**

```sql
USE ROLE SYSADMIN;
USE WAREHOUSE COMPUTE_WH;
USE DATABASE MY_DB;
USE SCHEMA MY_SCHEMA;
```

> Worksheet 上部のコンテキストセレクターから直接 Role / Warehouse / Database / Schema を選択することも可能です。

---

### 2.3 SnowSQL (CLI) インストールと設定

**ステップ 1: ダウンロードとインストール**

| プラットフォーム | 方法 |
|------|------|
| **Windows** | [SnowSQL MSI Installer](https://developers.snowflake.com/snowsql/) をダウンロード |
| **macOS** | `brew install --cask snowflake-snowsql` |
| **Linux** | `curl -O https://sfc-repo.snowflakecomputing.com/snowsql/bootstrap/1.3/linux_x86_64/snowsql-1.3.3-linux_x86_64.bash` |

**ステップ 2: 接続設定**

`~/.snowsql/config` の作成/編集：

```ini
[connections.my_account]
accountname = myorg-myaccount
username    = myuser
password    = mypassword
role        = SYSADMIN
warehouse   = COMPUTE_WH
database    = MY_DB
schema      = MY_SCHEMA
```

**Key Pair 認証（本番環境推奨）：**

```ini
[connections.my_account_keypair]
accountname      = myorg-myaccount
username         = myuser
private_key_path = /path/to/rsa_key.p8
authenticator    = snowflake_jwt
role             = SYSADMIN
warehouse        = COMPUTE_WH
```

**ステップ 3: 接続とテスト**

```bash
# 設定ファイルを使用して接続
snowsql -c my_account

# パラメータを直接指定
snowsql -a myorg-myaccount -u myuser

# 単一SQLを実行して終了
snowsql -c my_account -q "SELECT CURRENT_TIMESTAMP();"

# SQLファイルを実行
snowsql -c my_account -f my_script.sql

# 変数を使用
snowsql -c my_account -D table_name=MY_TABLE -q "SELECT COUNT(*) FROM &{table_name};"

# 非対話的出力（スクリプト向け）
snowsql -c my_account -o friendly=false -o header=false -o timing=false \
    -q "SELECT COUNT(*) FROM my_table;"
```

**ステップ 4: SnowSQL 対話的コマンド**

```
!help                    -- ヘルプを表示
!options                 -- 現在のオプションを表示
!set variable_name=value -- セッション変数を設定
!source file.sql         -- SQLファイルを実行
!load file.csv           -- CSVをテーブルに高速ロード
!output file.csv         -- 結果をCSVファイルとして出力
!abort                   -- 現在のクエリをキャンセル
!exit                    -- 終了
```

---

### 2.4 SDK & コネクター接続

#### Python コネクター

```bash
pip install snowflake-connector-python[pandas]
```

```python
import snowflake.connector
import pandas as pd

# 基本接続
conn = snowflake.connector.connect(
    user='myuser',
    password='mypassword',
    account='myorg-myaccount',
    warehouse='COMPUTE_WH',
    database='MY_DB',
    schema='MY_SCHEMA',
    role='SYSADMIN'
)

# クエリを実行して DataFrame を取得
cur = conn.cursor()
cur.execute("SELECT * FROM my_table LIMIT 10")
df = cur.fetch_pandas_all()
print(df)

# with ステートメントを使用（トランザクション自動管理）
with conn.cursor() as cur:
    cur.execute("INSERT INTO my_table VALUES (1, 'test')")

# Snowpark Python (DataFrame API)
from snowflake.snowpark import Session
from snowflake.snowpark.functions import col, sum as sum_

session = Session.builder.configs({
    "account": "myorg-myaccount",
    "user": "myuser",
    "password": "mypassword",
    "role": "SYSADMIN",
    "warehouse": "COMPUTE_WH",
    "database": "MY_DB",
    "schema": "MY_SCHEMA"
}).create()

df = (session.table("employees")
      .filter(col("status") == "active")
      .group_by("department_id")
      .agg(sum_("salary").alias("total_salary"))
      .to_pandas())
```

#### JDBC (Java)

```xml
<!-- pom.xml -->
<dependency>
    <groupId>net.snowflake</groupId>
    <artifactId>snowflake-jdbc</artifactId>
    <version>3.14.4</version>
</dependency>
```

```java
import java.sql.*;
import java.util.Properties;

public class SnowflakeExample {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("user", "myuser");
        props.put("password", "mypassword");
        props.put("role", "SYSADMIN");
        props.put("warehouse", "COMPUTE_WH");
        props.put("db", "MY_DB");
        props.put("schema", "MY_SCHEMA");

        String url = "jdbc:snowflake://myorg-myaccount.snowflakecomputing.com";
        try (Connection conn = DriverManager.getConnection(url, props);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CURRENT_TIMESTAMP()")) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        }
    }
}
```

---

## 3. コアオブジェクト階層構造

### 3.1 オブジェクト階層の概要

```
Organization (組織)
 +-- Account (アカウント)
      +-- User (ユーザー)
      +-- Role (ロール)
      +-- Warehouse (仮想ウェアハウス)
      +-- Resource Monitor (リソースモニター)
      +-- Database (データベース)
      |    +-- Schema (模式)
      |         +-- Table (表)
      |         +-- View (视图)
      |         +-- Materialized View (マテリアライズドビュー)
      |         +-- Stage (ステージ)
      |         +-- File Format (文件格式)
      |         +-- Pipe (管道)
      |         +-- Sequence (序列)
      |         +-- Stream (流)
      |         +-- Task (任务)
      |         +-- Stored Procedure (ストアドプロシージャ)
      |         +-- Function / UDF / UDTF (函数)
      |         +-- External Function (外部函数)
      |         +-- Masking Policy (脱敏策略)
      |         +-- Row Access Policy (行级访问策略)
      |         +-- Tag (标签)
      +-- Share (共有)
      +-- Integration (統合: Storage / Notification / API)
      +-- Network Policy (ネットワークポリシー)
```

**Fully Qualified Name (完全修飾名)：**

```sql
-- 形式
<database>.<schema>.<object>

-- 示例
MY_DB.PUBLIC.EMPLOYEES
MY_DB.HR.VW_ACTIVE_STAFF
```

---

### 3.2 Database (データベース)

データベースは最上位の名前空間コンテナです。

```sql
-- === データベースの作成 ===

-- 最もシンプルな作成
CREATE DATABASE my_database;

-- 全パラメータを指定した作成
CREATE OR REPLACE DATABASE my_database
    COMMENT = 'Training database for new hires'
    DATA_RETENTION_TIME_IN_DAYS = 7
    MAX_DATA_EXTENSION_TIME_IN_DAYS = 14
    DEFAULT_DDL_COLLATION = 'en_US';

-- Share から作成（共有データの受信）
CREATE DATABASE shared_db FROM SHARE provider_account.share_name;

-- データベースのクローン（ゼロコピー）
CREATE DATABASE my_database_clone CLONE my_database;

-- 過去の時点にクローン
CREATE DATABASE my_database_restore CLONE my_database
    AT (TIMESTAMP => '2026-05-10 12:00:00'::TIMESTAMP);

-- === データベースの表示 ===
SHOW DATABASES;
SHOW DATABASES LIKE 'my_database';
DESC DATABASE my_database;

-- === データベースの切り替え ===
USE DATABASE my_database;
SELECT CURRENT_DATABASE();

-- === データベースの変更 ===
ALTER DATABASE my_database SET DATA_RETENTION_TIME_IN_DAYS = 14;
ALTER DATABASE my_database RENAME TO my_database_v2;

-- === 削除と復旧 ===
DROP DATABASE my_database;
UNDROP DATABASE my_database;  -- Time Travel 期間内であれば復旧可能
```

---

### 3.3 Schema (スキーマ)

```sql
-- 作成
CREATE SCHEMA my_database.hr_schema
    COMMENT = 'HR department schema';

-- Managed Access Schema（権限の一元管理）
CREATE SCHEMA my_database.managed_hr WITH MANAGED ACCESS;

-- クローン
CREATE SCHEMA my_database.hr_clone CLONE my_database.hr_schema;

-- 表示
SHOW SCHEMAS IN DATABASE my_database;
DESC SCHEMA my_database.hr_schema;

-- 切り替え
USE SCHEMA my_database.hr_schema;
SELECT CURRENT_SCHEMA();

-- 変更
ALTER SCHEMA my_database.hr_schema
    SET DATA_RETENTION_TIME_IN_DAYS = 14;

-- 削除/復旧
DROP SCHEMA my_database.hr_schema;
UNDROP SCHEMA my_database.hr_schema;
```

---

### 3.4 Table (テーブル)

**テーブル種類の比較：**

| 種類 | Time Travel | Fail-safe | ライフサイクル |
|------|-------------|-----------|----------|
| **Permanent (永続テーブル)** | 0-90日(設定可) | 7日 | 手動削除 |
| **Temporary (一時テーブル)** | なし | なし | セッション終了時に自動削除 |
| **Transient (一時的テーブル)** | 1日(固定) | なし | 手動削除 |
| **External (外部テーブル)** | N/A | N/A | N/A |

```sql
-- 永続テーブル（デフォルトタイプ）
CREATE TABLE employees (
    employee_id   INTEGER PRIMARY KEY,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50) NOT NULL,
    email         VARCHAR(100) UNIQUE,
    hire_date     DATE DEFAULT CURRENT_DATE(),
    salary        NUMBER(10, 2),
    department_id INTEGER,
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

-- 一時テーブル（セッション終了時に自動削除）
CREATE TEMPORARY TABLE temp_calc_results (
    id    INTEGER,
    value NUMBER
);

-- 一時的テーブル（Fail-safe 保護なし、1日間の Time Travel あり）
CREATE TRANSIENT TABLE staging_raw_data (
    id       INTEGER,
    raw_json VARIANT,
    load_ts  TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

-- CTAS (CREATE TABLE AS SELECT)
CREATE TABLE high_salary_employees AS
SELECT * FROM employees WHERE salary > 100000;

-- CREATE TABLE LIKE（列構造のみコピー、データは含まない）
CREATE TABLE employees_backup LIKE employees;

-- Stage ファイルからスキーマを推測して自動テーブル作成
CREATE TABLE json_data
    USING TEMPLATE (
        SELECT ARRAY_AGG(OBJECT_CONSTRUCT(*))
        FROM TABLE(INFER_SCHEMA(
            LOCATION => '@my_stage/json_files/',
            FILE_FORMAT => 'my_json_format'
        ))
    );
```

---

### 3.5 View (ビュー)

```sql
-- 標準ビュー
CREATE VIEW vw_active_employees AS
SELECT employee_id, first_name, last_name, email, department_id
FROM employees WHERE is_active = TRUE;

-- 安全ビュー（定義の詳細を隠蔽、データ共有シナリオに最適）
CREATE SECURE VIEW vw_salary_summary AS
SELECT department_id,
       COUNT(*) AS emp_count,
       AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id;

-- 変更视图
CREATE OR REPLACE VIEW vw_active_employees AS
SELECT employee_id, first_name, last_name, email, department_id, hire_date
FROM employees WHERE is_active = TRUE;

-- 表示视图定义
SELECT GET_DDL('VIEW', 'MY_DB.HR_SCHEMA.VW_ACTIVE_EMPLOYEES');

-- 削除
DROP VIEW vw_active_employees;
```

---

### 3.6 Materialized View (マテリアライズドビュー)

マテリアライズドビューは**クエリ結果を事前計算して格納**します。基テーブルのデータが変更されると、バックグラウンドで自動的に増分更新されます（Snowflake が管理し、費用は Cloud Services に含まれます）。

**制限：** 集約関数 + GROUP BY のみサポート。JOIN、ウィンドウ関数、UNION、サブクエリは非サポート。

```sql
CREATE MATERIALIZED VIEW mv_dept_salary_summary AS
SELECT department_id,
       COUNT(*) AS employee_count,
       SUM(salary) AS total_salary,
       AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id;

ALTER MATERIALIZED VIEW mv_dept_salary_summary
    RENAME TO mv_dept_salary_summary_v2;

DROP MATERIALIZED VIEW mv_dept_salary_summary;
```

---

### 3.7 Stage (ステージ)

| 種類 | スコープ | 参照方法 | 用途 |
|------|--------|----------|------|
| **User Stage** | ユーザー | `@~` | ユーザー私有ファイルの一時保存 |
| **Table Stage** | テーブル | `@%table_name` | 単一テーブルのデータファイル一時保存 |
| **Internal Named Stage** | Schema | `@stage_name` | Schema 内で共有する一時保存 |
| **External Stage** | Schema | `@ext_stage` | 外部クラウドストレージを参照 (S3/Azure/GCS) |

```sql
-- 作成内部 Stage（开启目录表功能）
CREATE STAGE my_internal_stage
    ENCRYPTION = (TYPE = 'SNOWFLAKE_SSE')
    DIRECTORY = (ENABLE = TRUE)
    COMMENT = 'Internal stage for ETL files';

-- 作成外部 Stage (AWS S3)
CREATE STAGE my_s3_stage
    URL = 's3://my-bucket/data/'
    STORAGE_INTEGRATION = my_storage_integration
    FILE_FORMAT = my_csv_format;

-- 表示 Stage 中的文件
LIST @my_internal_stage;
LIST @my_s3_stage PATTERN='.*\\.csv$';

-- Stage からファイルを削除
REMOVE @my_internal_stage/old_data.csv;

-- 削除 Stage
DROP STAGE my_internal_stage;
```

---

### 3.8 File Format (ファイル形式)

データファイルの形式仕様を定義し、COPY INTO で使用します。

```sql
-- CSV 形式
CREATE FILE FORMAT my_csv_format
    TYPE = 'CSV'
    COMPRESSION = 'AUTO'
    RECORD_DELIMITER = '\n'
    FIELD_DELIMITER = ','
    SKIP_HEADER = 1
    FIELD_OPTIONALLY_ENCLOSED_BY = '"'
    NULL_IF = ('NULL', 'null', '')
    EMPTY_FIELD_AS_NULL = TRUE
    TRIM_SPACE = TRUE;

-- JSON 形式
CREATE FILE FORMAT my_json_format
    TYPE = 'JSON'
    COMPRESSION = 'AUTO'
    STRIP_OUTER_ARRAY = TRUE
    ENABLE_OCTAL = FALSE
    ALLOW_DUPLICATE = FALSE;

-- Parquet 形式
CREATE FILE FORMAT my_parquet_format
    TYPE = 'PARQUET'
    COMPRESSION = 'AUTO'
    BINARY_AS_TEXT = TRUE;

-- Avro 形式
CREATE FILE FORMAT my_avro_format
    TYPE = 'AVRO'
    COMPRESSION = 'AUTO';

-- ORC 形式
CREATE FILE FORMAT my_orc_format
    TYPE = 'ORC';

-- 表示与删除
SHOW FILE FORMATS IN SCHEMA MY_DB.PUBLIC;
DESC FILE FORMAT my_csv_format;
DROP FILE FORMAT my_csv_format;
```

---

### 3.9 Pipe (パイプ)

Pipe は Snowpipe による自動連続データロードに使用します。

```sql
-- 作成 Pipe (AWS S3 示例)
CREATE PIPE my_snowpipe
    AUTO_INGEST = TRUE
    AWS_SNS_TOPIC = 'arn:aws:sns:us-east-1:1234567890:snowpipe-topic'
    AS
    COPY INTO employees
    FROM @my_s3_stage/employees/
    FILE_FORMAT = (FORMAT_NAME = my_csv_format)
    ON_ERROR = 'SKIP_FILE';

-- 表示 Pipe
SHOW PIPES;
SELECT SYSTEM$PIPE_STATUS('my_snowpipe');

-- 停止/恢复 Pipe
ALTER PIPE my_snowpipe SET PIPE_EXECUTION_PAUSED = TRUE;
ALTER PIPE my_snowpipe SET PIPE_EXECUTION_PAUSED = FALSE;

-- Pipe のリフレッシュ（既存ファイルの手動ロード）
ALTER PIPE my_snowpipe REFRESH;

-- 表示 Pipe 加载历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.PIPE_USAGE_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    PIPE_NAME => 'my_snowpipe'
));

-- 削除
DROP PIPE my_snowpipe;
```

---

### 3.10 Sequence (シーケンス)

```sql
-- 作成
CREATE SEQUENCE employee_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 999999999
    COMMENT = 'Employee ID auto-increment sequence';

-- 使用
SELECT employee_id_seq.NEXTVAL;

INSERT INTO employees (employee_id, first_name, last_name)
VALUES (employee_id_seq.NEXTVAL, 'John', 'Doe');

-- 列のデフォルト値として使用
CREATE TABLE orders (
    order_id   INTEGER DEFAULT employee_id_seq.NEXTVAL,
    product    VARCHAR(100),
    order_date DATE DEFAULT CURRENT_DATE()
);

-- 表示
SHOW SEQUENCES;
DESC SEQUENCE employee_id_seq;

-- 変更
ALTER SEQUENCE employee_id_seq SET INCREMENT BY 10;

-- 削除
DROP SEQUENCE employee_id_seq;
```

> **注意：** Snowflake シーケンスは**ギャップがないことを保証しません**（大規模マルチノード Warehouse では番号が飛ぶことがあります）。厳密な連続番号が必要な場合は、アプリケーション層で実装してください。

---

### 3.11 Stored Procedure (ストアドプロシージャ)

```sql
-- SQL ストアドプロシージャ（Snowflake Scripting）
CREATE PROCEDURE calculate_bonus(emp_id INTEGER, bonus_rate FLOAT)
    RETURNS FLOAT
    LANGUAGE SQL
    EXECUTE AS CALLER
AS
$$
DECLARE
    current_salary FLOAT;
    bonus_amount   FLOAT;
BEGIN
    SELECT salary INTO :current_salary
    FROM employees
    WHERE employee_id = :emp_id;

    bonus_amount := :current_salary * :bonus_rate;

    RETURN :bonus_amount;
END;
$$;

-- 呼び出し
CALL calculate_bonus(1001, 0.1);
```

---

### 3.12 UDF / UDTF (ユーザー定義関数)

```sql
-- SQL UDF (スカラー関数)
CREATE FUNCTION celsius_to_fahrenheit(celsius FLOAT)
    RETURNS FLOAT
AS $$
    celsius * 9/5 + 32
$$;

SELECT celsius_to_fahrenheit(37);  -- 98.6

-- SQL UDTF (テーブル関数)
CREATE FUNCTION get_dept_employees(dept_id INTEGER)
    RETURNS TABLE (emp_id INTEGER, name VARCHAR, salary NUMBER)
AS $$
    SELECT employee_id, first_name || ' ' || last_name, salary
    FROM employees
    WHERE department_id = dept_id
$$;

SELECT * FROM TABLE(get_dept_employees(10));
```

---

## 4. データ型詳細

### 4.1 数値型

| 型 | 説明 | 範囲/精度 |
|------|------|-----------|
| **NUMBER / DECIMAL / NUMERIC** | 固定精度数値 | P: 1-38, S: 0-P |
| **INT / INTEGER / BIGINT / SMALLINT** | 整数 (NUMBER(38,0) のエイリアス) | -10^38+1 ~ 10^38-1 |
| **TINYINT** | 微小整数 | 0 ~ 255 |
| **FLOAT / FLOAT4 / FLOAT8** | 浮動小数点数 | 約15-17桁の有効数字 |
| **DOUBLE / DOUBLE PRECISION / REAL** | 倍精度浮動小数点 | 約15-17桁の有効数字 |

```sql
CREATE TABLE numeric_examples (
    col_num   NUMBER,           -- NUMBER(38, 0)
    col_dec   NUMBER(10, 2),    -- 精度10，小数2位
    col_int   INTEGER,          -- NUMBER(38, 0)
    col_float FLOAT,
    col_auto  INTEGER AUTOINCREMENT
);
```

### 4.2 文字列型

| 型 | 最大長 |
|------|----------|
| **VARCHAR / STRING / TEXT / NVARCHAR / NVARCHAR2** | 16,777,216 バイト (16 MB) |
| **CHAR / CHARACTER / NCHAR** | 16,777,216 バイト (16 MB) |
| **BINARY / VARBINARY** | 8,388,608 バイト (8 MB) |

```sql
CREATE TABLE string_examples (
    col_vc  VARCHAR(100),
    col_vc2 VARCHAR,          -- デフォルト上限 16MB
    col_ch  CHAR(10),         -- 固定長 10、不足分はスペースで埋める
    col_txt TEXT,
    col_bin BINARY(100)
);
```

### 4.3 日付・時刻型

| 型 | 説明 | 例 |
|------|------|------|
| **DATE** | 日付 (日単位の精度) | `'2026-05-12'` |
| **TIME(n)** | 時刻 (n=0~9 ナノ秒精度) | `'14:30:00.123456789'` |
| **TIMESTAMP_NTZ** | タイムゾーンなしタイムスタンプ | `'2026-05-12 14:30:00'` |
| **TIMESTAMP_LTZ** | ローカルタイムゾーン入力→UTC格納 | **クロスタイムゾーンシナリオに推奨** |
| **TIMESTAMP_TZ** | 元のタイムゾーンオフセットを保持 | ソースタイムゾーン保持が必要なシナリオ |

```sql
-- よく使う日付関数
SELECT CURRENT_DATE();                    -- 現在の日付
SELECT CURRENT_TIME();                    -- 現在の時刻
SELECT CURRENT_TIMESTAMP();               -- 現在の時刻戳
SELECT SYSDATE();                        -- クエリ開始時刻
SELECT DATEADD(day, 7, '2026-05-12');    -- 7日加算
SELECT DATEDIFF(day, '2026-01-01', CURRENT_DATE());
SELECT DATE_TRUNC('MONTH', CURRENT_DATE());  -- 当月の初日
SELECT EXTRACT(YEAR FROM CURRENT_DATE());    -- 年を抽出
SELECT LAST_DAY(CURRENT_DATE());             -- 当月の最終日
SELECT TO_DATE('2026-05-12', 'YYYY-MM-DD');
SELECT TO_TIMESTAMP('2026-05-12 14:30:00', 'YYYY-MM-DD HH24:MI:SS');
```

### 4.4 半構造化データ型

| 型 | 説明 | 最大 |
|------|------|------|
| **VARIANT** | 汎用半構造化 (JSON/Avro/Parquet/ORC) | 圧縮後 16 MB |
| **OBJECT** | キー・バリュー形式 (JSON オブジェクト類似) | 圧縮後 16 MB |
| **ARRAY** | 順序付き値リスト (JSON 配列類似) | 圧縮後 16 MB |
| **GEOGRAPHY** | 地理空間データ (WGS84) | N/A |

```sql
SELECT PARSE_JSON('{"name":"John","age":30,"skills":["SQL","Python"]}');
SELECT OBJECT_CONSTRUCT('name', 'John', 'age', 30);
SELECT ARRAY_CONSTRUCT('a', 'b', 'c');
SELECT ARRAY_SIZE(['a', 'b', 'c']);  -- 3
```

### 4.5 その他の型

| 类型 | 描述 |
|------|------|
| **BOOLEAN** | TRUE / FALSE / NULL |

### 4.6 型変換

```sql
-- CAST / :: 明示的変換
SELECT CAST('123' AS INTEGER);         -- 123
SELECT '3.14159'::FLOAT;               -- 3.14159
SELECT '2026-05-12'::DATE;

-- 安全な変換（失敗時は NULL を返す）
SELECT TRY_CAST('abc' AS INTEGER);     -- NULL
SELECT TRY_TO_NUMBER('$1,234.56', '$999,999.99');

-- 形式化
SELECT TO_CHAR(1234567.89, '$9,999,999.99');   -- '$1,234,567.89'
SELECT TO_NUMBER('$1,234.56', '$999,999.99');   -- 1234.56
SELECT TO_BOOLEAN('true'), TO_BOOLEAN(1);        -- TRUE, TRUE
```

---


## 5. DDL 完全構文リファレンス

本章では Snowflake コア DDL ステートメントの**完全な公式構文**を提供し、各パラメータについて詳細に説明します。

---

### 5.1 CREATE DATABASE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] DATABASE [ IF NOT EXISTS ] <name>
    [ CLONE <source_database>
        [ AT | BEFORE ( TIMESTAMP => <timestamp> | OFFSET => <offset_seconds> | STATEMENT => <query_id> ) ]
    ]
    [ FROM SHARE <provider_account>.<share_name> ]
    [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
    [ MAX_DATA_EXTENSION_TIME_IN_DAYS = <integer> ]
    [ DEFAULT_DDL_COLLATION = '<collation_spec>' ]
    [ COMMENT = '<string>' ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `OR REPLACE` | 同名データベースが既存の場合、先に DROP して置き換え（DROP 権限が必要）。旧データベースは Time Travel 状態に移行 |
| `IF NOT EXISTS` | 同名データベースが既存の場合、エラーを出さずにスキップ |
| `<name>` | データベース名。Snowflake 識別子ルールに準拠：英字で始まり、英数字/アンダースコアを使用可能、最大255文字 |
| `CLONE <source>` | 指定データベースをゼロコピークローン。実際のデータはコピーせず、メタデータポインタのみ複製 |
| `AT (TIMESTAMP => ...)` | 指定時刻にクローン（秒精度）。形式：`'2026-05-10 12:00:00'::TIMESTAMP` |
| `AT (OFFSET => ...)` | 指定秒数前の時点にクローン。例：`OFFSET => 3600` は1時間前 |
| `AT (STATEMENT => ...)` | 指定クエリ実行前の状態にクローン。`<query_id>` は Query History から取得可能 |
| `BEFORE (...)` | AT と同機能だが、指定時刻/クエリの前の状態 |
| `FROM SHARE <provider>.<share>` | データ共有からデータベースを作成。provider_account は提供者アカウント名、share_name は Share 名 |
| `DATA_RETENTION_TIME_IN_DAYS` | Time Travel 保持日数。範囲 0-90 (Enterprise Edition)。0 に設定すると Time Travel 無効。デフォルトはアカウントレベル設定を継承 |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | ストレージ延長日数。アカウント期限切れやデータベース削除時、この追加日数内はデータ復旧可能。最大90日 |
| `DEFAULT_DDL_COLLATION` | デフォルト照合順序。例：`'en_US'`、`'utf8'` 他。文字列比較・ソート動作に影響 |
| `COMMENT` | データベースのコメント/説明。最大255文字 |

#### 操作例

```sql
-- 基本作成
CREATE DATABASE training_db;

-- フル設定
CREATE OR REPLACE DATABASE training_db
    DATA_RETENTION_TIME_IN_DAYS = 14
    MAX_DATA_EXTENSION_TIME_IN_DAYS = 30
    DEFAULT_DDL_COLLATION = 'en_US'
    COMMENT = 'Training database for new team members';

-- クローン当前状态
CREATE DATABASE training_db_dev CLONE training_db;

-- クローン到1小时前的状态
CREATE DATABASE training_db_restore CLONE training_db
    AT (OFFSET => -3600);

-- 指定時点にクローン
CREATE DATABASE training_db_timepoint CLONE training_db
    AT (TIMESTAMP => '2026-05-11 08:00:00'::TIMESTAMP);

-- 共有から作成
CREATE DATABASE shared_partner_data FROM SHARE partner_acct.market_data_share;
```

---

### 5.2 ALTER DATABASE

#### 完全な構文

```sql
ALTER DATABASE [ IF EXISTS ] <name>
    RENAME TO <new_name>
    | SWAP WITH <target_database>
    | SET
        [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
        [ MAX_DATA_EXTENSION_TIME_IN_DAYS = <integer> ]
        [ DEFAULT_DDL_COLLATION = '<collation_spec>' ]
        [ COMMENT = '<string>' ]
    | UNSET
        [ DATA_RETENTION_TIME_IN_DAYS ]
        [ MAX_DATA_EXTENSION_TIME_IN_DAYS ]
        [ DEFAULT_DDL_COLLATION ]
        [ COMMENT ]
    | ENABLE REPLICATION TO ACCOUNTS <account_identifier>
        IGNORE EDITION CHECK
    | DISABLE REPLICATION TO ACCOUNTS <account_identifier>
    | REFRESH REPLICATION
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `IF EXISTS` | データベースが存在しない場合エラーを出さない |
| `RENAME TO <new_name>` | データベース名の変更。注意：トランザクション内では実行不可 |
| `SWAP WITH <target>` | 2つのデータベース名を交換。アトミック操作、データコピーなし |
| `SET DATA_RETENTION_TIME_IN_DAYS` | Time Travel 保持日数の変更 |
| `SET MAX_DATA_EXTENSION_TIME_IN_DAYS` | ストレージ延長日数の変更 |
| `SET DEFAULT_DDL_COLLATION` | デフォルト照合順序の変更 |
| `UNSET` | パラメータをアカウントデフォルト値に戻す |
| `ENABLE REPLICATION` | 指定アカウントへのデータベース複製を有効化 |
| `DISABLE REPLICATION` | データベース複製を無効化 |

#### 操作例

```sql
ALTER DATABASE training_db RENAME TO training_db_v2;
ALTER DATABASE training_db SWAP WITH training_db_old;
ALTER DATABASE training_db SET DATA_RETENTION_TIME_IN_DAYS = 30;
ALTER DATABASE training_db SET COMMENT = 'Updated training database';
ALTER DATABASE training_db UNSET DATA_RETENTION_TIME_IN_DAYS;  -- 再開默认
```

---

### 5.3 DROP DATABASE

#### 完全な構文

```sql
DROP DATABASE [ IF EXISTS ] <name> [ CASCADE | RESTRICT ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |
| `CASCADE` | データベース配下の全 Schema とオブジェクトを連鎖削除 |
| `RESTRICT` | (デフォルト) データベースが空でない場合（Schema を含む）、削除を拒否しエラー |

#### 操作例

```sql
DROP DATABASE training_db;
DROP DATABASE IF EXISTS training_db;
DROP DATABASE training_db CASCADE;       -- 全子オブジェクトを強制削除
UNDROP DATABASE training_db;             -- 削除されたデータベースを復旧
```

---

### 5.4 CREATE SCHEMA

#### 完全な構文

```sql
CREATE [ OR REPLACE ] SCHEMA [ IF NOT EXISTS ] [ <database>.]<name>
    [ CLONE <source_schema>
        [ AT | BEFORE ( TIMESTAMP => <timestamp> | OFFSET => <offset_seconds> | STATEMENT => <query_id> ) ]
    ]
    [ WITH MANAGED ACCESS ]
    [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
    [ MAX_DATA_EXTENSION_TIME_IN_DAYS = <integer> ]
    [ DEFAULT_DDL_COLLATION = '<collation_spec>' ]
    [ COMMENT = '<string>' ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `OR REPLACE` | 既存の同名 Schema を置き換え、旧 Schema は Time Travel に移行 |
| `IF NOT EXISTS` | 存在する場合エラーを出さない |
| `[<database>.]<name>` | Schema 名。データベース接頭辞を指定可能。例：`my_db.hr_schema` |
| `CLONE` | 指定 Schema をゼロコピークローン |
| `WITH MANAGED ACCESS` | 管理アクセスモード：Schema 所有者のみがオブジェクト権限を付与可能、権限管理を簡素化 |
| `DATA_RETENTION_TIME_IN_DAYS` | Time Travel 保持日数、データベースレベル設定を上書き |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | ストレージ延長日数 |
| `DEFAULT_DDL_COLLATION` | デフォルト照合順序 |

#### 操作例

```sql
CREATE SCHEMA training_db.hr_schema;
CREATE SCHEMA hr_schema;  -- 現在のデータベース内に作成

CREATE SCHEMA training_db.hr_dev CLONE training_db.hr_schema;
CREATE SCHEMA training_db.managed_schema WITH MANAGED ACCESS;
```

---

### 5.5 CREATE TABLE

#### 完全な構文

```sql
CREATE [ OR REPLACE ]
    [ { LOCAL | GLOBAL } ] { TEMPORARY | TEMP } | TRANSIENT | VOLATILE
    TABLE [ IF NOT EXISTS ] <table_name>
    (
        <column_name> <column_type>
            [ COLLATE '<collation_spec>' ]
            [ DEFAULT <default_expr> ]
            [ { AUTOINCREMENT | IDENTITY }
                [ ( <start>, <increment> ) ]
                [ ORDER | NOORDER ]
            ]
            [ [ NOT ] NULL ]
            [ [ UNIQUE ] PRIMARY KEY ]
            [ UNIQUE ]
            [ REFERENCES <ref_table> [ ( <ref_column> ) ] ]
            [ WITH MASKING POLICY <policy_name> [ USING ( <col1>, <cond1> ) ] ]
            [ WITH TAG ( <tag_name> = '<tag_value>', ... ) ]
            [ COMMENT '<string>' ]
        [ , ... ]
        [ , <out_of_line_constraint> [ , ... ] ]
    )
    [ STAGE_FILE_FORMAT = ( FORMAT_NAME = '<file_format_name>' | TYPE = '<type>' ... ) ]
    [ STAGE_COPY_OPTIONS = ( ON_ERROR = { CONTINUE | SKIP_FILE | ABORT_STATEMENT } ... ) ]
    [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
    [ MAX_DATA_EXTENSION_TIME_IN_DAYS = <integer> ]
    [ CHANGE_TRACKING = { TRUE | FALSE } ]
    [ DEFAULT_DDL_COLLATION = '<collation_spec>' ]
    [ COPY GRANTS ]
    [ CLUSTER BY ( <expr> [ , <expr> , ... ] ) ]
    [ ENABLE_SCHEMA_EVOLUTION = { TRUE | FALSE } ]
    [ WITH TAG ( <tag_name> = '<tag_value>' [ , ... ] ) ]
    [ COMMENT = '<string>' ]

-- 或使用 CTAS 语法
CREATE [ OR REPLACE ]
    [ { LOCAL | GLOBAL } ] { TEMPORARY | TEMP } | TRANSIENT | VOLATILE
    TABLE [ IF NOT EXISTS ] <table_name>
    [ ( <column_name> [ WITH MASKING POLICY <policy_name> ] ... ) ]
    [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
    [ ... ]
    AS <query>

-- 或使用 LIKE 语法
CREATE [ OR REPLACE ]
    [ { LOCAL | GLOBAL } ] { TEMPORARY | TEMP } | TRANSIENT | VOLATILE
    TABLE [ IF NOT EXISTS ] <table_name>
    LIKE <source_table>
    [ COPY GRANTS ]
    [ ... ]

-- 或从 Stage 推断 Schema
CREATE [ OR REPLACE ]
    TABLE [ IF NOT EXISTS ] <table_name>
    USING TEMPLATE <subquery>
    [ ... ]
```

#### パラメータ説明

**テーブル種類パラメータ：**

| パラメータ | 説明 |
|------|------|
| `TEMPORARY / TEMP` | 一時テーブル。セッション内のみ参照可能、セッション終了時に自動削除。Time Travel / Fail-safe なし。クローン不可 |
| `TRANSIENT` | 一時的テーブル。永続的に存在するが Fail-safe 保護なし（1日間の Time Travel のみ）。永続テーブルよりストレージコストが低い |
| `VOLATILE` | TRANSIENT と同じ（非推奨エイリアス、TRANSIENT の使用を推奨） |
| (種類指定なし) | 永続テーブル。完全な Time Travel + Fail-safe 保護が適用 |

**列定義パラメータ：**

| パラメータ | 説明 |
|------|------|
| `<column_name>` | 列名。英字で始まり、英数字/アンダースコアを使用可能 |
| `<column_type>` | データ型（第4章参照） |
| `COLLATE` | 列の照合順序。データベース/Schema レベル設定を上書き |
| `DEFAULT <expr>` | デフォルト値式。例：`CURRENT_DATE()`、`0`、`'N/A'` |
| `AUTOINCREMENT / IDENTITY` | 自動採番列。`<start>` 開始値（デフォルト 1）、`<increment>` 増分（デフォルト 1） |
| `ORDER / NOORDER` | IDENTITY 列の動作。`ORDER` は可能な限り増加を保証、`NOORDER` は保証しない。デフォルト `NOORDER` |
| `NOT NULL` | NOT NULL 制約 |
| `NULL` | NULL 値を許可（デフォルト） |
| `PRIMARY KEY` | 主キー制約。**ドキュメント目的**、一意性は強制されない（RELY 有効時を除く） |
| `UNIQUE` | 一意制約。**ドキュメント目的**、一意性は強制されない |
| `REFERENCES <table>` | 外部キー制約。**ドキュメント目的**、参照整合性は強制されない |
| `WITH MASKING POLICY` | 列レベルデータマスキングポリシー。詳細は [12.7](#127-column-level-security-カラムレベルセキュリティ) 参照 |
| `WITH TAG` | 列レベルタグ。ガバナンスと追跡に使用 |

**テーブルレベルパラメータ：**

| パラメータ | 説明 |
|------|------|
| `STAGE_FILE_FORMAT` | テーブル Stage のデフォルトファイル形式。COPY INTO の動作に影響 |
| `STAGE_COPY_OPTIONS` | テーブル Stage の COPY オプション |
| `DATA_RETENTION_TIME_IN_DAYS` | テーブルレベル Time Travel 保持日数。データベース/Schema 設定を上書き |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | テーブルレベルストレージ延長日数 |
| `CHANGE_TRACKING` | 変更追跡。TRUE の場合、Stream がテーブルの全変更を追跡可能 |
| `DEFAULT_DDL_COLLATION` | テーブルレベルデフォルト照合順序 |
| `COPY GRANTS` | CTAS/LIKE 作成時、ソーステーブルの権限付与をコピー |
| `CLUSTER BY (<expr>)` | クラスタリングキー。指定式に従ってマイクロパーティション配置を再構成し、クエリパフォーマンスを向上 |
| `ENABLE_SCHEMA_EVOLUTION` | スキーマ進化を有効化。データファイルに基づいて自動的に列を追加、自動ロードシナリオに最適 |
| `WITH TAG` | テーブルレベルタグ |

#### 操作例

```sql
-- 標準永続テーブル
CREATE TABLE employees (
    employee_id   INTEGER AUTOINCREMENT (1, 1) ORDER PRIMARY KEY,
    first_name    VARCHAR(50) NOT NULL,
    last_name     VARCHAR(50) NOT NULL,
    email         VARCHAR(100),
    hire_date     DATE DEFAULT CURRENT_DATE(),
    salary        NUMBER(10, 2) DEFAULT 0 CHECK (salary >= 0),
    department_id INTEGER,
    manager_id    INTEGER REFERENCES employees(employee_id),
    is_active     BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP(),
    CONSTRAINT chk_salary CHECK (salary >= 0)
)
COMMENT = 'Employee master data'
DATA_RETENTION_TIME_IN_DAYS = 14
CHANGE_TRACKING = TRUE;

-- クラスタリングテーブル
CREATE TABLE sales_fact (
    sale_date  DATE,
    product_id INTEGER,
    region     VARCHAR(50),
    amount     NUMBER(12, 2)
) CLUSTER BY (sale_date, region);

-- CTAS
CREATE TABLE high_value_employees AS
SELECT * FROM employees WHERE salary > 150000;

-- 列定義付き CTAS
CREATE TABLE employee_summary (dept_id, avg_salary, headcount)
    COMMENT = 'Department-level summary'
AS
SELECT department_id, AVG(salary), COUNT(*)
FROM employees GROUP BY department_id;

-- LIKE 構造
CREATE TABLE employees_staging LIKE employees;

-- Stage からスキーマを自動推測
CREATE TABLE json_auto_table
    USING TEMPLATE (
        SELECT ARRAY_AGG(OBJECT_CONSTRUCT(*))
        FROM TABLE(INFER_SCHEMA(
            LOCATION => '@my_stage/json_data/',
            FILE_FORMAT => 'my_json_ff'
        ))
    );
```

---

### 5.6 ALTER TABLE

#### 完全な構文

```sql
ALTER TABLE [ IF EXISTS ] <table_name>
    RENAME TO <new_table_name>
    | SWAP WITH <target_table>
    | ALTER [ COLUMN ] <column_name>
        DROP DEFAULT
        | SET DEFAULT <expr>
        | SET [ NOT ] NULL
        | DROP NOT NULL
        | SET DATA TYPE <type>
        | { SET | DROP } COMMENT '<string>'
        | { SET | DROP } MASKING POLICY <policy_name>
        | UNSET TAG <tag_name>
        | SET TAG <tag_name> = '<tag_value>'
    | ADD [ COLUMN ] <column_name> <type> [ ... ]
    | RENAME COLUMN <old_name> TO <new_name>
    | DROP [ COLUMN ] <column_name> [ CASCADE | RESTRICT ]
    | SET
        [ DATA_RETENTION_TIME_IN_DAYS = <integer> ]
        [ MAX_DATA_EXTENSION_TIME_IN_DAYS = <integer> ]
        [ CHANGE_TRACKING = { TRUE | FALSE } ]
        [ DEFAULT_DDL_COLLATION = '<spec>' ]
        [ CLUSTER BY ( <expr> [ , ... ] ) ]
        [ ENABLE_SCHEMA_EVOLUTION = { TRUE | FALSE } ]
        [ STAGE_FILE_FORMAT = (...) ]
        [ COMMENT = '<string>' ]
    | UNSET
        [ DATA_RETENTION_TIME_IN_DAYS ]
        [ ... ]
    | { ADD | DROP } CLUSTERING KEY
    | { ENABLE | DISABLE } SCHEMA_EVOLUTION
    | ADD CONSTRAINT <name> ...
    | DROP CONSTRAINT <name> [ CASCADE | RESTRICT ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `RENAME TO` | テーブル名の変更 |
| `SWAP WITH` | 2つのテーブル名をアトミックに交換 |
| `ALTER [COLUMN]` | 列属性の変更 |
| `DROP DEFAULT` | 列のデフォルト値削除 |
| `SET DEFAULT <expr>` | 列のデフォルト値設定 |
| `SET [NOT] NULL` | NULL 許可/非許可制約の変更 |
| `SET DATA TYPE <type>` | 列データ型の変更。**制限：** VARCHAR の長さ拡張または NUMBER の精度/スケール拡張のみ |
| `ADD [COLUMN]` | 列の追加。追加列は既存行で NULL またはデフォルト値 |
| `RENAME COLUMN` | 列名の変更 |
| `DROP [COLUMN]` | 列の削除。`CASCADE` は依存オブジェクトを連鎖削除、`RESTRICT` は依存がある場合エラー |
| `SET CLUSTER BY` | クラスタリングキーの設定/変更 |
| `DROP CLUSTERING KEY` | クラスタリングキーの削除 |
| `ENABLE/DISABLE SCHEMA_EVOLUTION` | 自動スキーマ進化の有効/無効 |

#### 操作例

```sql
-- 名前変更
ALTER TABLE employees RENAME TO staff;

-- 交換
ALTER TABLE employees SWAP WITH employees_backup;

-- 列の追加
ALTER TABLE employees ADD COLUMN phone VARCHAR(20);
ALTER TABLE employees ADD COLUMN address VARCHAR(200) DEFAULT 'Unknown';

-- 変更列
ALTER TABLE employees ALTER COLUMN phone SET DATA TYPE VARCHAR(30);
ALTER TABLE employees ALTER COLUMN phone DROP DEFAULT;
ALTER TABLE employees ALTER COLUMN email SET NOT NULL;

-- 名前変更列
ALTER TABLE employees RENAME COLUMN phone TO contact_phone;

-- 削除列
ALTER TABLE employees DROP COLUMN address;

-- クラスタリングキーの設定
ALTER TABLE employees SET CLUSTER BY (department_id, hire_date);

-- 削除聚类键
ALTER TABLE employees DROP CLUSTERING KEY;

-- 変更 Time Travel
ALTER TABLE employees SET DATA_RETENTION_TIME_IN_DAYS = 30;

-- 変更追跡の有効化
ALTER TABLE employees SET CHANGE_TRACKING = TRUE;
```

---

### 5.7 DROP TABLE

#### 完全な構文

```sql
DROP TABLE [ IF EXISTS ] <table_name> [ CASCADE | RESTRICT ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |
| `CASCADE` | 依存オブジェクト（ビュー、Stream 等）の連鎖削除 |
| `RESTRICT` | (デフォルト) 依存オブジェクトがある場合エラーで削除拒否 |

#### 操作例

```sql
DROP TABLE employees;
DROP TABLE IF EXISTS employees CASCADE;
UNDROP TABLE employees;  -- Time Travel 期間内なら復旧可能
```

---

### 5.8 TRUNCATE TABLE

#### 完全な構文

```sql
TRUNCATE [ TABLE ] [ IF EXISTS ] <table_name>
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |

> **注意：** TRUNCATE はテーブル内の全行を削除しますが、テーブル構造と権限は保持されます。この操作は**メタデータ操作**（行ごとの削除ではない）であり、非常に高速です。TRUNCATE の変更は Stream でキャプチャ可能です。

#### 操作例

```sql
TRUNCATE TABLE staging_data;
TRUNCATE TABLE IF EXISTS temp_logs;
```

---

### 5.9 CREATE VIEW

#### 完全な構文

```sql
CREATE [ OR REPLACE ]
    [ SECURE ]
    [ RECURSIVE ]
    VIEW [ IF NOT EXISTS ] <view_name>
    [ ( <column_list> ) ]
    [ COPY GRANTS ]
    [ COMMENT = '<string>' ]
    AS <query>
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `OR REPLACE` | 既存の同名ビューを置き換え |
| `SECURE` | 安全ビューを作成。ビュー定義と内部詳細を隠蔽。データ共有シナリオで受信者による SHOW VIEWS / GET_DDL での定義閲覧を防止 |
| `RECURSIVE` | 再帰 CTE を使用するビュー。階層クエリをサポートするために自己参照 |
| `IF NOT EXISTS` | 既存の場合エラーを出さない |
| `<column_list>` | ビュー列の名前を指定。数と順序は SELECT 列と一致が必要 |
| `COPY GRANTS` | CREATE OR REPLACE 使用時、旧ビューの権限付与を保持 |
| `COMMENT` | ビューのコメント |

#### 操作例

```sql
-- 標準ビュー
CREATE VIEW vw_active_staff AS
SELECT employee_id, first_name, last_name, email, department_id
FROM employees WHERE is_active = TRUE;

-- 列エイリアス付き
CREATE VIEW vw_salary_stats(dept, total_payroll, headcount) AS
SELECT department_id, SUM(salary), COUNT(*) FROM employees GROUP BY department_id;

-- 安全ビュー（データ共有用）
CREATE SECURE VIEW vw_public_employee AS
SELECT first_name, last_name, department_id FROM employees;

-- 再帰ビュー（組織階層ツリー）
CREATE RECURSIVE VIEW vw_org_tree (employee_id, manager_id, level, path) AS
SELECT employee_id, manager_id, 1, employee_id::VARCHAR
FROM employees WHERE manager_id IS NULL
UNION ALL
SELECT e.employee_id, e.manager_id, t.level + 1, t.path || '.' || e.employee_id
FROM employees e
JOIN vw_org_tree t ON e.manager_id = t.employee_id;
```

---

### 5.10 CREATE MATERIALIZED VIEW

#### 完全な構文

```sql
CREATE [ OR REPLACE ]
    [ SECURE ]
    MATERIALIZED VIEW [ IF NOT EXISTS ] <mv_name>
    [ COPY GRANTS ]
    [ COMMENT = '<string>' ]
    AS <select_statement>
```

> **select_statement 制限：** 集約関数 (SUM, COUNT, AVG, MIN, MAX, STDDEV 他) と GROUP BY のみサポート。JOIN、ウィンドウ関数、UNION、サブクエリ（WHERE 句内を除く）、HAVING、ORDER BY、LIMIT は非サポート。単一の基テーブルのみクエリ可能。

#### 操作例

```sql
CREATE MATERIALIZED VIEW mv_monthly_sales AS
SELECT DATE_TRUNC('MONTH', sale_date) AS sale_month,
       product_id,
       SUM(amount) AS total_amount,
       COUNT(*) AS transaction_count
FROM sales_fact
GROUP BY 1, 2;
```

---

### 5.11 CREATE STAGE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] STAGE [ IF NOT EXISTS ] [ <schema>.]<stage_name>
    [ URL = '<cloud_storage_url>' ]
    [ {
        STORAGE_INTEGRATION = <integration_name>
        | CREDENTIALS = ( { AWS_KEY_ID = '<key>' AWS_SECRET_KEY = '<secret>' [ AWS_TOKEN = '<token>' ] }
                         | { AZURE_SAS_TOKEN = '<sas_token>' } )
    } ]
    [ ENCRYPTION = (
          [ TYPE = 'SNOWFLAKE_SSE' ]
          | [ TYPE = 'NONE' ]
          | [ TYPE = 'AWS_CSE' MASTER_KEY = '<key>' ]
          | [ TYPE = 'AWS_SSE_S3' ]
          | [ TYPE = 'AWS_SSE_KMS' [ KMS_KEY_ID = '<kms_key>' ] ]
          | [ TYPE = 'AZURE_CSE' MASTER_KEY = '<key>' ]
          | [ TYPE = 'GCS_SSE_KMS' [ KMS_KEY_ID = '<key>' ] ]
      ) ]
    [ FILE_FORMAT = ( FORMAT_NAME = '<ff_name>' | TYPE = '<type>' ... ) ]
    [ COPY_OPTIONS = ( ON_ERROR = { CONTINUE | SKIP_FILE | ABORT_STATEMENT } ... ) ]
    [ COMMENT = '<string>' ]
    [ DIRECTORY = ( ENABLE = { TRUE | FALSE } [ REFRESH_ON_CREATE = { TRUE | FALSE } ] ) ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `URL` | 外部クラウドストレージ URL。例：`'s3://my-bucket/data/'`、`'azure://myaccount.blob.core.windows.net/container/'`、`'gcs://my-bucket/'`。内部 Stage は指定不要 |
| `STORAGE_INTEGRATION` | ストレージ統合オブジェクト名。外部クラウドストレージへの安全なアクセスに使用（推奨、平文認証情報の使用を回避） |
| `CREDENTIALS` | 平文クラウドストレージ認証情報。本番環境での使用は非推奨、STORAGE_INTEGRATION を優先 |
| `ENCRYPTION` | ファイル暗号化オプション。`SNOWFLAKE_SSE` デフォルトサーバーサイド暗号化。`AWS_CSE`/`AZURE_CSE` クライアントサイド暗号化 |
| `FILE_FORMAT` | 作成済みファイル形式の参照、またはインライン定義 |
| `COPY_OPTIONS` | COPY INTO 操作のデフォルトオプション |
| `DIRECTORY` | ディレクトリテーブル機能を有効化。有効後、`DIRECTORY(@stage)` クエリで Stage ファイル一覧を表示可能 |

#### 操作例

```sql
-- 内部 Stage
CREATE STAGE my_internal_stage;

-- 外部 Stage (AWS S3 + Storage Integration)
CREATE STAGE my_s3_stage
    URL = 's3://my-data-lake/incoming/'
    STORAGE_INTEGRATION = my_s3_integration
    FILE_FORMAT = my_csv_ff;

-- ディレクトリテーブル付き内部 Stage
CREATE STAGE my_dir_stage
    DIRECTORY = (ENABLE = TRUE REFRESH_ON_CREATE = TRUE);

-- ディレクトリテーブルのクエリ
SELECT * FROM DIRECTORY(@my_dir_stage);
```

---

### 5.12 CREATE FILE FORMAT

#### 完全な構文

```sql
CREATE [ OR REPLACE ] FILE FORMAT [ IF NOT EXISTS ] <name>
    TYPE = { CSV | JSON | AVRO | ORC | PARQUET | XML }
    [ formatTypeOptions ]
    [ COMMENT = '<string>' ]
```

#### CSV オプション

| パラメータ | 選択可能な値 | デフォルト値 | 説明 |
|------|--------|--------|------|
| `COMPRESSION` | AUTO / GZIP / BZ2 / BROTLI / ZSTD / DEFLATE / RAW_DEFLATE / NONE | AUTO | ファイル圧縮タイプ |
| `RECORD_DELIMITER` | 文字列 (例: `\n`, `\r\n`) | `\n` (Unix) / `\r\n` (Windows) | 行区切り文字 |
| `FIELD_DELIMITER` | 文字列 | `,` | フィールド区切り文字 |
| `FILE_EXTENSION` | 文字列 | (圧縮形式により決定) | ファイル拡張子、Stage ファイルのマッチングに使用 |
| `SKIP_HEADER` | 整数 | 0 | ファイル先頭のスキップ行数 |
| `SKIP_BLANK_LINES` | TRUE / FALSE | FALSE | 空行をスキップするか |
| `DATE_FORMAT` | フォーマット文字列 または AUTO | AUTO | 日付形式、例: `'YYYY-MM-DD'` |
| `TIME_FORMAT` | フォーマット文字列 または AUTO | AUTO | 時刻形式 |
| `TIMESTAMP_FORMAT` | フォーマット文字列 または AUTO | AUTO | タイムスタンプ形式 |
| `BINARY_FORMAT` | HEX / BASE64 / UTF8 | HEX | バイナリデータ形式 |
| `ESCAPE` | 文字 | NONE | エスケープ文字 |
| `ESCAPE_UNENCLOSED_FIELD` | 文字 | `\` | 引用符で囲まれていないフィールドのエスケープ文字 |
| `TRIM_SPACE` | TRUE / FALSE | FALSE | フィールド前後の空白を除去するか |
| `FIELD_OPTIONALLY_ENCLOSED_BY` | 文字 | NONE | フィールドを囲むオプションの文字、例: `"` |
| `NULL_IF` | 文字列リスト | `('\\N', '', 'NULL', 'NUL')` | NULL と見なす値のリスト |
| `EMPTY_FIELD_AS_NULL` | TRUE / FALSE | TRUE | 空フィールドを NULL と見なすか |
| `ERROR_ON_COLUMN_COUNT_MISMATCH` | TRUE / FALSE | TRUE | 列数不一致時にエラーとするか |
| `REPLACE_INVALID_CHARACTERS` | TRUE / FALSE | FALSE | 無効な UTF-8 を Unicode 代替文字で置換するか |
| `VALIDATE_UTF8` | TRUE / FALSE | TRUE | UTF-8 エンコーディングを検証するか |
| `ENCODING` | UTF8 / ISO-8859-1 他 | UTF8 | 文字エンコーディング |

#### JSON オプション

| パラメータ | 説明 |
|------|------|
| `COMPRESSION` | 同 CSV |
| `DATE_FORMAT` | 同 CSV |
| `TIME_FORMAT` | 同 CSV |
| `TIMESTAMP_FORMAT` | 同 CSV |
| `BINARY_FORMAT` | 同 CSV |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |
| `STRIP_OUTER_ARRAY` | TRUE の場合、最外層配列を剥がし、配列要素を独立行としてロード |
| `ENABLE_OCTAL` | 8進数表現を許可するか |
| `ALLOW_DUPLICATE` | JSON オブジェクト内の重複キーを許可するか |
| `STRIP_NULL_VALUES` | JSON オブジェクトから null 値のキーを削除するか |
| `IGNORE_UTF8_ERRORS` | UTF-8 エンコーディングエラーを無視するか |
| `SKIP_BYTE_ORDER_MARK` | BOM マークをスキップするか |

#### Parquet オプション

| パラメータ | 説明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `BINARY_AS_TEXT` | TRUE の場合、バイナリ列を UTF-8 テキストとして解釈 |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |

#### Avro オプション

| パラメータ | 説明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |
| `VALIDATE_UTF8` | 同 CSV |

#### XML オプション

| パラメータ | 説明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `IGNORE_UTF8_ERRORS` | 同 JSON |
| `PRESERVE_SPACE` | XML 要素間の空白を保持するか |
| `STRIP_OUTER_ELEMENT` | TRUE の場合、最外層 XML 要素を剥がす |
| `DISABLE_SNOWFLAKE_DATA` | TRUE の場合、特殊 Snowflake メタデータマークを無効化 |
| `DISABLE_AUTO_CONVERT` | TRUE の場合、自動型変換を無効化 |
| `SKIP_BYTE_ORDER_MARK` | 同 JSON |

#### 操作例

```sql
-- CSV 形式（完整配置）
CREATE FILE FORMAT my_csv_ff
    TYPE = 'CSV'
    COMPRESSION = 'AUTO'
    RECORD_DELIMITER = '\n'
    FIELD_DELIMITER = ','
    FILE_EXTENSION = 'csv'
    SKIP_HEADER = 1
    SKIP_BLANK_LINES = TRUE
    DATE_FORMAT = 'YYYY-MM-DD'
    TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF3'
    BINARY_FORMAT = 'HEX'
    ESCAPE = '\\'
    ESCAPE_UNENCLOSED_FIELD = '\\'
    TRIM_SPACE = TRUE
    FIELD_OPTIONALLY_ENCLOSED_BY = '"'
    NULL_IF = ('NULL', 'null', '', '\N')
    EMPTY_FIELD_AS_NULL = TRUE
    ERROR_ON_COLUMN_COUNT_MISMATCH = TRUE
    ENCODING = 'UTF8';

-- JSON 形式
CREATE FILE FORMAT my_json_ff
    TYPE = 'JSON'
    STRIP_OUTER_ARRAY = TRUE
    ENABLE_OCTAL = FALSE
    ALLOW_DUPLICATE = FALSE
    IGNORE_UTF8_ERRORS = TRUE;

-- Parquet 形式
CREATE FILE FORMAT my_parquet_ff
    TYPE = 'PARQUET'
    BINARY_AS_TEXT = TRUE;
```

---

### 5.13 CREATE PIPE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] PIPE [ IF NOT EXISTS ] <name>
    [ AUTO_INGEST = { TRUE | FALSE } ]
    [
        AWS_SNS_TOPIC = '<sns_topic_arn>'      -- 仅 AWS
      | INTEGRATION = '<notification_integration>'  -- Azure / GCP
    ]
    [ ERROR_INTEGRATION = '<notification_integration>' ]
    [ COMMENT = '<string>' ]
    AS <copy_statement>
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `AUTO_INGEST` | TRUE の場合、Stage に新しいファイルがあると自動的に COPY をトリガー。クラウドストレージイベント通知の設定が必要 |
| `AWS_SNS_TOPIC` | AWS S3 イベント通知の SNS トピック ARN |
| `INTEGRATION` | Azure Event Grid または GCP Pub/Sub の通知統合名 |
| `ERROR_INTEGRATION` | エラー発生時のエラー通知統合 |
| `<copy_statement>` | Stage からターゲットテーブルへのデータロードを定義する COPY INTO 文 |

#### 操作例

```sql
CREATE PIPE my_snowpipe
    AUTO_INGEST = TRUE
    AWS_SNS_TOPIC = 'arn:aws:sns:us-east-1:1234567890:snowpipe-topic'
    COMMENT = 'Automatic employee data ingestion'
AS
    COPY INTO employees
    FROM @my_s3_stage/employees/
    FILE_FORMAT = (FORMAT_NAME = my_csv_ff)
    ON_ERROR = 'SKIP_FILE';
```

---

### 5.14 CREATE SEQUENCE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] SEQUENCE [ IF NOT EXISTS ] <name>
    [ WITH ] START [ WITH ] <start_value>
    [ INCREMENT [ BY ] <increment_value> ]
    [ MINVALUE <min_value> | NO MINVALUE ]
    [ MAXVALUE <max_value> | NO MAXVALUE ]
    [ CYCLE | NO CYCLE ]
    [ ORDER | NOORDER ]
    [ COMMENT = '<string>' ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `START WITH` | シーケンス開始値、デフォルト 1 |
| `INCREMENT BY` | 増分、負数で減少。デフォルト 1 |
| `MINVALUE / NO MINVALUE` | 最小値/最小値なし |
| `MAXVALUE / NO MAXVALUE` | 最大値/最大値なし |
| `CYCLE / NO CYCLE` | 境界到達時に循環するか。デフォルト NO CYCLE |
| `ORDER / NOORDER` | 増加順序を保証するか。デフォルト NOORDER。ORDER はパフォーマンスに影響 |

---

### 5.15 CREATE FUNCTION (UDF)

#### 完全な構文 (SQL UDF)

```sql
CREATE [ OR REPLACE ] [ { TEMP | TEMPORARY } ]
    [ SECURE ] FUNCTION [ IF NOT EXISTS ] <name>
    ( [ <arg_name> <arg_type> [ DEFAULT <expr> ] [ , ... ] ] )
    RETURNS { <result_type> | TABLE ( <col_name> <col_type> [ , ... ] ) }
    [ NOT NULL ]
    [ VOLATILE | IMMUTABLE ]
    [ COMMENT = '<string>' ]
    [ [ CALLED ON NULL INPUT | { RETURNS NULL ON NULL INPUT | STRICT } ] ]
    [ RUNTIME_VERSION = <version> ]      -- Python / Java
    [ IMPORTS = ( '<path>', ... ) ]       -- Python / Java
    [ PACKAGES = ( '<package>', ... ) ]   -- Python
    [ HANDLER = '<handler>' ]             -- Python / Java
    LANGUAGE { SQL | JAVASCRIPT | PYTHON | JAVA | SCALA }
    AS { $$<body>$$ | '<body>' }
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `TEMP/TEMPORARY` | 一時関数を作成、セッション終了後に自動削除 |
| `SECURE` | 安全関数を作成。定義詳細を隠蔽 |
| `RETURNS <type>` | SQL UDF: スカラー型を返す。UDTF: `RETURNS TABLE (col type, ...)` |
| `NOT NULL` | (SQL UDF) 関数の戻り値が常に NULL でないことを宣言 |
| `VOLATILE / IMMUTABLE` | (SQL UDF) VOLATILE: 同じ入力でも異なる値を返す可能性がある。IMMUTABLE: 同じ入力は常に同じ値を返す（オプティマイザにキャッシュ可能） |
| `CALLED ON NULL INPUT` | (デフォルト) 入力が NULL でも関数を呼び出す |
| `RETURNS NULL ON NULL INPUT / STRICT` | 入力が NULL の場合直接 NULL を返し、関数を呼び出さない |
| `RUNTIME_VERSION` | Python/Java UDF のランタイムバージョン |
| `IMPORTS` | Python/Java UDF のインポートファイルパス（Stage パス） |
| `PACKAGES` | Python UDF の Python パッケージリスト |
| `HANDLER` | Python/Java UDF のハンドラー関数/メソッド名 |
| `LANGUAGE` | 関数の実装言語 |

---

### 5.16 CREATE PROCEDURE

#### 完全な構文 (SQL)

```sql
CREATE [ OR REPLACE ] [ { TEMP | TEMPORARY } ]
    [ SECURE ] PROCEDURE [ IF NOT EXISTS ] <name>
    ( [ <arg_name> <arg_type> [ DEFAULT <expr> ] [ , ... ] ] )
    RETURNS { <result_type> | TABLE ( <col_name> <col_type> [ , ... ] ) }
    [ NOT NULL ]
    [ [ CALLED ON NULL INPUT | { RETURNS NULL ON NULL INPUT | STRICT } ] ]
    [ VOLATILE | IMMUTABLE ]
    [ COMMENT = '<string>' ]
    [ EXECUTE AS { CALLER | OWNER } ]
    [ RUNTIME_VERSION = <version> ]       -- Python / Java / Scala
    [ IMPORTS = ( '<path>', ... ) ]       -- Python / Java / Scala
    [ PACKAGES = ( '<package>', ... ) ]   -- Python / Java
    [ HANDLER = '<handler>' ]             -- Python / Java / Scala
    LANGUAGE { SQL | JAVASCRIPT | PYTHON | JAVA | SCALA }
    AS { $$<body>$$ | '<body>' }
```

#### 主要パラメータ説明

| パラメータ | 説明 |
|------|------|
| `EXECUTE AS CALLER` | (デフォルト) 呼び出し元の権限で実行 |
| `EXECUTE AS OWNER` | ストアドプロシージャ所有者の権限で実行。低権限ユーザーがストアドプロシージャ経由で高権限操作を実行するのに最適 |
| `RETURNS` | SQL ストアドプロシージャは RETURNS の宣言が必須。JavaScript ストアドプロシージャは戻り値型がオプション |

---

### 5.17 CREATE TASK

#### 完全な構文

```sql
CREATE [ OR REPLACE ] TASK [ IF NOT EXISTS ] <name>
    [ { WAREHOUSE = <warehouse_name> | USER_TASK_MANAGED_INITIAL_WAREHOUSE_SIZE = <size> } ]
    [ SCHEDULE = <minutes> MINUTE
        | USING CRON <cron_expression> <timezone> ]
    [ CONFIG = '<config_string>' ]
    [ ALLOW_OVERLAPPING_EXECUTION = { TRUE | FALSE } ]
    [ <session_parameter> = <value> [ , ... ] ]
    [ USER_TASK_TIMEOUT_MS = <num> ]
    [ SUSPEND_TASK_AFTER_NUM_FAILURES = <num> ]
    [ ERROR_INTEGRATION = <integration_name> ]
    [ COPY GRANTS ]
    [ COMMENT = '<string>' ]
    [ AFTER <task_name> [ , <task_name> , ... ] ]
    [ WHEN <boolean_expr> ]
AS
    <sql_statement>
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `WAREHOUSE` | タスク実行に使用する Warehouse |
| `USER_TASK_MANAGED_INITIAL_WAREHOUSE_SIZE` | Serverless Task の初期コンピュート規模 |
| `SCHEDULE` | スケジュール方式。`<minutes> MINUTE` または `USING CRON <expr> <tz>` |
| `ALLOW_OVERLAPPING_EXECUTION` | 同時実行を許可するか（デフォルト FALSE、同時に1インスタンスのみ実行） |
| `SESSION_PARAMETER` | タスク実行時のセッションパラメータ |
| `USER_TASK_TIMEOUT_MS` | Serverless Task タイムアウト時間（ミリ秒） |
| `SUSPEND_TASK_AFTER_NUM_FAILURES` | 連続失敗回数がこの値に達するとタスクを自動停止 |
| `ERROR_INTEGRATION` | エラー通知統合 |
| `AFTER` | 先行タスクを指定、DAG（有向非循環グラフ）を構成 |
| `WHEN` | 条件式。TRUE の場合のみ SQL を実行 |

---

### 5.18 CREATE STREAM

#### 完全な構文

```sql
CREATE [ OR REPLACE ] STREAM [ IF NOT EXISTS ] <name>
    [ COPY GRANTS ]
    [ COMMENT = '<string>' ]
    ON TABLE <table_name>
    [ AT | BEFORE ( TIMESTAMP => <timestamp> | OFFSET => <offset> | STATEMENT => <query_id> ) ]
    [
        APPEND_ONLY = { TRUE | FALSE }
      | SHOW_INITIAL_ROWS = { TRUE | FALSE }
      | INSERT_ONLY = { TRUE | FALSE }
    ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `ON TABLE <table_name>` | Stream が追跡する対象テーブル（外部テーブル、ビュー、ディレクトリテーブルも可） |
| `AT/BEFORE` | Stream 作成の時点オフセット |
| `APPEND_ONLY = TRUE` | INSERT 操作のみ追跡、UPDATE/DELETE は無視。追加式データソースに最適 |
| `SHOW_INITIAL_ROWS = TRUE` | Stream 初回クエリ時に作成前から存在する全行を含める |
| `INSERT_ONLY = TRUE` | APPEND_ONLY のエイリアス |

---

### 5.19 CREATE USER

#### 完全な構文

```sql
CREATE [ OR REPLACE ] USER [ IF NOT EXISTS ] <name>
    [ PASSWORD = '<password>' ]
    [ LOGIN_NAME = '<login_name>' ]
    [ DISPLAY_NAME = '<display_name>' ]
    [ FIRST_NAME = '<first_name>' ]
    [ MIDDLE_NAME = '<middle_name>' ]
    [ LAST_NAME = '<last_name>' ]
    [ EMAIL = '<email>' ]
    [ MUST_CHANGE_PASSWORD = { TRUE | FALSE } ]
    [ DISABLED = { TRUE | FALSE } ]
    [ DAYS_TO_EXPIRY = <integer> ]
    [ MINS_TO_UNLOCK = <integer> ]
    [ DEFAULT_WAREHOUSE = <warehouse_name> ]
    [ DEFAULT_NAMESPACE = <db>.<schema> ]
    [ DEFAULT_ROLE = <role_name> ]
    [ DEFAULT_SECONDARY_ROLES = ( 'ALL' ) ]
    [ MINS_TO_BYPASS_MFA = <integer> ]
    [ RSA_PUBLIC_KEY = '<key>' ]
    [ RSA_PUBLIC_KEY_2 = '<key>' ]
    [ COMMENT = '<string>' ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `PASSWORD` | ユーザーパスワード。アカウントパスワードポリシーの制約を受ける |
| `LOGIN_NAME` | ログイン名。表示名とは別、SSO 統合に使用可能 |
| `DISPLAY_NAME` | インターフェース表示名 |
| `MUST_CHANGE_PASSWORD` | 初回ログイン時にパスワード変更を強制するか |
| `DISABLED` | ユーザーを無効化するか |
| `DAYS_TO_EXPIRY` | パスワード有効期限日数 |
| `MINS_TO_UNLOCK` | アカウントロック期間（分） |
| `DEFAULT_WAREHOUSE` | ユーザーのデフォルト Warehouse |
| `DEFAULT_NAMESPACE` | ユーザーのデフォルト データベース.Schema |
| `DEFAULT_ROLE` | ユーザーのデフォルトロール |
| `DEFAULT_SECONDARY_ROLES` | デフォルト二次ロール、`('ALL')` は付与された全ロールを表す |
| `MINS_TO_BYPASS_MFA` | MFA 認証キャッシュ期間（分） |
| `RSA_PUBLIC_KEY` | RSA 公開鍵（Key Pair 認証に使用） |

---

### 5.20 CREATE ROLE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] ROLE [ IF NOT EXISTS ] <name>
    [ COMMENT = '<string>' ]
```

#### 操作例

```sql
CREATE ROLE data_analyst;
CREATE ROLE data_engineer COMMENT = 'ETL pipeline role';
CREATE ROLE data_scientist COMMENT = 'ML and analytics role';
```

---

### 5.21 CREATE WAREHOUSE

#### 完全な構文

```sql
CREATE [ OR REPLACE ] WAREHOUSE [ IF NOT EXISTS ] <name>
    [ [ WITH ] WAREHOUSE_SIZE = { XSMALL | SMALL | MEDIUM | LARGE | XLARGE | XXLARGE | XXXLARGE | X4LARGE | X5LARGE | X6LARGE } ]
    [ WAREHOUSE_TYPE = { STANDARD | SNOWPARK_OPTIMIZED } ]
    [ MAX_CLUSTER_COUNT = <num> ]
    [ MIN_CLUSTER_COUNT = <num> ]
    [ SCALING_POLICY = { STANDARD | ECONOMY } ]
    [ AUTO_SUSPEND = <num> | NULL ]
    [ AUTO_RESUME = { TRUE | FALSE } ]
    [ INITIALLY_SUSPENDED = { TRUE | FALSE } ]
    [ RESOURCE_MONITOR = <monitor_name> ]
    [ COMMENT = '<string>' ]
    [ ENABLE_QUERY_ACCELERATION = { TRUE | FALSE } ]
    [ QUERY_ACCELERATION_MAX_SCALE_FACTOR = <num> ]
    [ MAX_CONCURRENCY_LEVEL = <num> ]
    [ STATEMENT_QUEUED_TIMEOUT_IN_SECONDS = <num> ]
    [ STATEMENT_TIMEOUT_IN_SECONDS = <num> ]
    [ TAG <tag_name> = '<tag_value>' [ , ... ] ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `WAREHOUSE_SIZE` | Warehouse サイズ。各サイズでコンピュートリソースが倍増。XSMALL=1 ノード, SMALL=2, MEDIUM=4, LARGE=8, XLARGE=16, 2XLARGE=32, 3XLARGE=64, 4XLARGE=128 |
| `WAREHOUSE_TYPE` | `STANDARD` 汎用；`SNOWPARK_OPTIMIZED` Snowpark のメモリ集約型ワークロード向け最適化 |
| `MAX_CLUSTER_COUNT` | 最大クラスター数 (1-10)。>1 で Multi-cluster Warehouse を有効化 |
| `MIN_CLUSTER_COUNT` | 最小クラスター数。≤ MAX_CLUSTER_COUNT |
| `SCALING_POLICY` | `STANDARD` 迅速なスケーリング(より速い応答)；`ECONOMY` 保守的スケーリング(コスト削減) |
| `AUTO_SUSPEND` | アイドル時に自動停止する秒数。NULL は自動停止しない。デフォルト 600 秒。Warehouse 停止後は課金なし |
| `AUTO_RESUME` | クエリ実行時に自動再開。デフォルト TRUE |
| `INITIALLY_SUSPENDED` | 作成後すぐに停止するか。デフォルト FALSE |
| `RESOURCE_MONITOR` | 関連付けられたリソースモニター |
| `ENABLE_QUERY_ACCELERATION` | クエリ高速化サービスを有効化 |
| `QUERY_ACCELERATION_MAX_SCALE_FACTOR` | クエリ高速化の最大倍率 |
| `MAX_CONCURRENCY_LEVEL` | 最大同時クエリ数。デフォルト 8 |
| `STATEMENT_QUEUED_TIMEOUT_IN_SECONDS` | キュー待機タイムアウト（秒） |
| `STATEMENT_TIMEOUT_IN_SECONDS` | ステートメント実行タイムアウト（秒） |

---

## 6. DML 完全構文リファレンス

### 6.1 INSERT

#### 完全な構文

```sql
INSERT [ OVERWRITE ] INTO <target_table>
    [ ( <target_column_list> ) ]
    {
        VALUES ( { <expr> | DEFAULT | NULL } [ , ... ] ) [ , ( ... ) , ... ]
        | <query>
    }
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `OVERWRITE` | 上書き書き込み。TRUNCATE 後に挿入。DELETE+INSERT より効率的 |
| `<target_table>` | 目标表名 |
| `<target_column_list>` | 指定要插入的列。未指定的列使用默认值或 NULL |
| `VALUES` | 值列表，支持多行插入 |
| `DEFAULT` | 使用列的默认值 |
| `<query>` | サブクエリ結果の挿入 |

#### 操作例

```sql
-- 单行插入
INSERT INTO employees (employee_id, first_name, last_name, salary)
VALUES (1, 'John', 'Doe', 75000);

-- 多行插入
INSERT INTO employees (employee_id, first_name, last_name, salary)
VALUES
    (2, 'Jane', 'Smith', 80000),
    (3, 'Bob', 'Johnson', 65000),
    (4, 'Alice', 'Williams', 90000);

-- 使用 DEFAULT
INSERT INTO employees (first_name, last_name, email, salary)
VALUES ('Charlie', 'Brown', 'charlie@example.com', DEFAULT);

-- クエリから挿入
INSERT INTO high_salary_employees
SELECT * FROM employees WHERE salary > 100000;

-- OVERWRITE (覆盖写入)
INSERT OVERWRITE INTO staging_table
SELECT * FROM source_table WHERE load_date = CURRENT_DATE();
```

---

### 6.2 UPDATE

#### 完全な構文

```sql
UPDATE <target_table>
    SET <column_name> = <expr> [ , <column_name> = <expr> , ... ]
    [ FROM <additional_tables> ]
    [ WHERE <condition> ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `SET` | 列=值 对。可一次更新多列 |
| `FROM` | 额外的源表，用于关联更新 |
| `WHERE` | 过滤条件。**如果省略 WHERE，所有行都会被更新** |

#### 操作例

```sql
-- 简单更新
UPDATE employees
SET salary = salary * 1.1
WHERE department_id = 10;

-- 多列更新
UPDATE employees
SET salary = 90000, is_active = TRUE
WHERE employee_id = 5;

-- FROM 子句关联更新
UPDATE employees e
SET e.salary = n.new_salary
FROM salary_adjustments n
WHERE e.employee_id = n.employee_id;
```

---

### 6.3 DELETE

#### 完全な構文

```sql
DELETE FROM <table_name>
    [ USING <additional_tables> ]
    [ WHERE <condition> ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `USING` | 关联其他表来过滤删除条件 |
| `WHERE` | 过滤条件。**如果省略 WHERE，删除所有行** |

#### 操作例

```sql
-- 条件删除
DELETE FROM employees WHERE is_active = FALSE;

-- 使用 USING 关联删除
DELETE FROM employees
USING terminated_employees t
WHERE employees.employee_id = t.employee_id;
```

---

### 6.4 MERGE

#### 完全な構文

```sql
MERGE INTO <target_table>
    USING <source>
    ON <join_condition>
    [ WHEN MATCHED
        [ AND <case_predicate> ]
        THEN
        UPDATE SET <col> = <expr> [ , ... ]
        | DELETE
    ]
    [ WHEN NOT MATCHED
        [ AND <case_predicate> ]
        THEN
        INSERT [ ( <col_list> ) ] VALUES ( <val_list> )
    ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `<target_table>` | 目标表（被合并到的表） |
| `<source>` | ソーステーブルまたはサブクエリ（マージデータのソース） |
| `ON <join_condition>` | 匹配条件 |
| `WHEN MATCHED` | 当行匹配时的操作：UPDATE 或 DELETE |
| `WHEN NOT MATCHED` | 当行不匹配时的操作：INSERT |
| `AND <case_predicate>` | 额外的条件分支过滤 |
| `UPDATE SET` | 匹配时更新列值 |
| `DELETE` | 匹配时删除行 |
| `INSERT` | 不匹配时插入新行 |

> **注意：** 一个 MERGE 语句可有多个 WHEN MATCHED 子句（带不同条件），但至多一个 WHEN NOT MATCHED。

#### 操作例

```sql
-- 基础 Upsert
MERGE INTO employees t
USING employee_updates s
ON t.employee_id = s.employee_id
WHEN MATCHED THEN
    UPDATE SET t.salary = s.salary, t.department_id = s.department_id
WHEN NOT MATCHED THEN
    INSERT (employee_id, first_name, last_name, salary)
    VALUES (s.employee_id, s.first_name, s.last_name, s.salary);

-- 带条件分支的 MERGE
MERGE INTO employees t
USING employee_updates s
ON t.employee_id = s.employee_id
WHEN MATCHED AND s.change_type = 'UPDATE' THEN
    UPDATE SET t.salary = s.new_salary
WHEN MATCHED AND s.change_type = 'DELETE' THEN
    DELETE
WHEN NOT MATCHED THEN
    INSERT (employee_id, first_name, last_name, salary, department_id)
    VALUES (s.employee_id, s.first_name, s.last_name, s.salary, s.department_id);
```

---

### 6.5 COPY INTO <table> (データロード)

#### 完全な構文

```sql
COPY INTO [ <namespace>.]<table_name>
    [ ( <column_list> ) ]
    FROM {
        { <internal_stage> | <external_stage> | <external_location> }
        | ( SELECT ... FROM { <internal_stage> | <external_stage> } )
    }
    [ FILES = ( '<file_name>' [ , '<file_name>' ... ] ) ]
    [ PATTERN = '<regex_pattern>' ]
    [ FILE_FORMAT = ( FORMAT_NAME = '<name>' | TYPE = '<type>' ... ) ]
    [ VALIDATION_MODE = { RETURN_<n>_ROWS | RETURN_ERRORS | RETURN_ALL_ERRORS } ]
    [ COPY_OPTIONS = ( ... ) ]
    [ ON_ERROR = { CONTINUE | SKIP_FILE | ABORT_STATEMENT | SKIP_FILE_<num> | SKIP_FILE_<num>% } ]
    [ SIZE_LIMIT = <num> ]
    [ PURGE = { TRUE | FALSE } ]
    [ RETURN_FAILED_ONLY = { TRUE | FALSE } ]
    [ MATCH_BY_COLUMN_NAME = { CASE_SENSITIVE | CASE_INSENSITIVE | NONE } ]
    [ ENFORCE_LENGTH = { TRUE | FALSE } ]
    [ TRUNCATECOLUMNS = { TRUE | FALSE } ]
    [ FORCE = { TRUE | FALSE } ]
    [ LOAD_UNCERTAIN_FILES = { TRUE | FALSE } ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `<column_list>` | 指定要加载的列和顺序 |
| `FROM` | 数据源位置（Stage 或外部位置） |
| `FILES` | 指定具体文件名列表 |
| `PATTERN` | 正则表达式过滤文件名 |
| `FILE_FORMAT` | 文件格式定义 |
| `VALIDATION_MODE` | 验证模式：返回 N 行数据预览或错误信息 |
| `ON_ERROR` | 错误处理策略 |
| `SIZE_LIMIT` | ロードデータ量制限（バイト） |
| `PURGE` | 加载成功后是否删除 Stage 中的文件 |
| `MATCH_BY_COLUMN_NAME` | 按列名匹配而非按顺序。适用于 Schema 演化场景 |
| `ENFORCE_LENGTH` | 列长度超限是否截断 |
| `TRUNCATECOLUMNS` | 超出目标列长度时是否截断 |
| `FORCE` | 强制加载所有文件（即使已加载过） |
| `LOAD_UNCERTAIN_FILES` | 加载上次未完成的文件 |

#### 操作例

```sql
-- 基础加载
COPY INTO employees
FROM @my_s3_stage/employees/
FILE_FORMAT = (FORMAT_NAME = my_csv_ff);

-- 指定文件
COPY INTO employees
FROM @my_s3_stage
FILES = ('employees_2026-05-12.csv', 'employees_2026-05-11.csv')
FILE_FORMAT = (FORMAT_NAME = my_csv_ff);

-- 按模式匹配
COPY INTO employees
FROM @my_s3_stage
PATTERN = '.*employees.*[.]csv'
FILE_FORMAT = (FORMAT_NAME = my_csv_ff);

-- 指定列+转换
COPY INTO employees (employee_id, first_name, last_name, salary)
FROM (
    SELECT $1, $2, $3, $4::NUMBER(10,2)
    FROM @my_s3_stage
)
FILE_FORMAT = (FORMAT_NAME = my_csv_ff);

-- 验证模式（预览而不实际加载）
COPY INTO employees
FROM @my_s3_stage
FILE_FORMAT = (FORMAT_NAME = my_csv_ff)
VALIDATION_MODE = RETURN_10_ROWS;

-- 处理 JSON 数据
COPY INTO employees
FROM (
    SELECT
        $1:employee_id::INTEGER,
        $1:first_name::STRING,
        $1:last_name::STRING,
        $1:salary::NUMBER(10,2)
    FROM @my_stage/json_data/
)
FILE_FORMAT = (FORMAT_NAME = my_json_ff);

-- 加载成功自动删除文件
COPY INTO employees
FROM @my_internal_stage
FILE_FORMAT = (FORMAT_NAME = my_csv_ff)
PURGE = TRUE;

-- 按列名匹配（支持 Schema 演化）
COPY INTO employees
FROM @my_s3_stage
FILE_FORMAT = (FORMAT_NAME = my_csv_ff)
MATCH_BY_COLUMN_NAME = CASE_INSENSITIVE;
```

---

### 6.6 COPY INTO <location> (データアンロード)

#### 完全な構文

```sql
COPY INTO { <internal_stage> | <external_stage> | <external_location> }
    FROM { [ <namespace>.]<table_name> | ( <query> ) }
    [ PARTITION BY <expr> ]
    [ FILE_FORMAT = ( FORMAT_NAME = '<name>' | TYPE = '<type>' ... ) ]
    [ COPY_OPTIONS = ( ... ) ]
    [ HEADER = { TRUE | FALSE } ]
    [ MAX_FILE_SIZE = <num> ]
    [ SINGLE = { TRUE | FALSE } ]
    [ OVERWRITE = { TRUE | FALSE } ]
    [ INCLUDE_QUERY_ID = { TRUE | FALSE } ]
```

#### パラメータ説明

| パラメータ | 説明 |
|------|------|
| `FROM <table> / (<query>)` | 卸载的数据源 |
| `PARTITION BY <expr>` | 按表达式分区输出文件 |
| `HEADER` | 输出 CSV 时是否包含列名头行 |
| `MAX_FILE_SIZE` | 单个文件最大大小（字节）。默认 16000000 (16 MB) |
| `SINGLE` | 是否输出为单个文件（默认 FALSE，可能输出多个文件） |
| `OVERWRITE` | 是否覆盖已存在的文件 |
| `INCLUDE_QUERY_ID` | ファイル名にクエリ ID を含むかどうか |

#### 操作例

```sql
-- 卸载表到 Stage
COPY INTO @my_s3_stage/export/
FROM employees
FILE_FORMAT = (TYPE = CSV COMPRESSION = GZIP)
HEADER = TRUE
MAX_FILE_SIZE = 50000000;

-- クエリ結果のアンロード
COPY INTO @my_internal_stage/export/high_salary/
FROM (
    SELECT employee_id, first_name, last_name, salary
    FROM employees WHERE salary > 100000
)
FILE_FORMAT = (TYPE = PARQUET COMPRESSION = SNAPPY)
OVERWRITE = TRUE;

-- 分区卸载
COPY INTO @my_s3_stage/export/
FROM employees
PARTITION BY department_id
FILE_FORMAT = (TYPE = CSV)
HEADER = TRUE;

-- 单文件输出
COPY INTO @my_stage/export/result.csv
FROM employees
FILE_FORMAT = (TYPE = CSV)
HEADER = TRUE
SINGLE = TRUE;
```

---


## 7. SELECT クエリ完全構文

### 7.1 SELECT 完全構文

```sql
[ WITH <cte_name> [ ( <cte_column_list> ) ] AS ( <cte_query> ) [ , ... ] ]
SELECT [ ALL | DISTINCT ]
    {
        { <expr> [ [ AS ] <alias> ] }
        | { <table_alias>.* }
        | { * }
    }
    [ , ... ]
    [ EXCLUDE ( <col_name> [ , ... ] ) ]
    [ RENAME ( <col_name> AS <new_name> [ , ... ] ) ]
FROM <table_or_view>
    [ [ AS ] <alias> ]
    [ { INNER | LEFT [ OUTER ] | RIGHT [ OUTER ] | FULL [ OUTER ] | CROSS | NATURAL [ INNER | LEFT | RIGHT | FULL ] } JOIN <table> [ ON <cond> | USING ( <col> [ , ... ] ) ] ]
    [ , ... ]
    [ AT | BEFORE ( TIMESTAMP => <ts> | OFFSET => <sec> | STATEMENT => <qid> ) ]
    [ SAMPLE { BERNOULLI | SYSTEM | BLOCK } ( <frac> ) [ REPEATABLE ( <seed> ) ] ]
    | TABLESAMPLE { BERNOULLI | SYSTEM | BLOCK } ( <frac> ) [ REPEATABLE ( <seed> ) ]
    [ FLATTEN ( INPUT => <expr>, PATH => <path>, OUTER => TRUE | FALSE, RECURSIVE => TRUE | FALSE, MODE => OBJECT | ARRAY | BOTH ) ]
    [ PIVOT | UNPIVOT ... ]
WHERE <search_condition>
    [ AND <condition> ... ]
    [ OR <condition> ... ]
    [ NOT ] <condition>
GROUP BY [ GROUPING SETS | CUBE | ROLLUP ] ( <expr_list> )
    [ , ... ]
HAVING <condition>
QUALIFY <window_function_condition>
ORDER BY <expr> [ ASC | DESC ] [ NULLS FIRST | NULLS LAST ] [ , ... ]
LIMIT <count> [ OFFSET <start> ]
```

#### 关键子句说明

| 子句 | 说明 |
|------|------|
| `WITH` | CTE (公用表表达式)。可递归引用自身 |
| `ALL / DISTINCT` | ALL(默认): 所有行。DISTINCT: 去重 |
| `EXCLUDE` | 在 SELECT * 中排除指定列 |
| `RENAME` | 在 SELECT * 中重命名指定列 |
| `FROM` | データソース。テーブル、ビュー、テーブル関数、サブクエリをサポート |
| `AT / BEFORE` | Time Travel 時点クエリ |
| `SAMPLE / TABLESAMPLE` | サンプリングクエリ |
| `FLATTEN` | 半構造化データを展開 |
| `PIVOT / UNPIVOT` | 行列转换 |
| `WHERE` | 行过滤 |
| `GROUP BY` | 分组。支持 GROUPING SETS / CUBE / ROLLUP |
| `HAVING` | 分组后过滤 |
| `QUALIFY` | ウィンドウ関数の結果フィルタリング |
| `ORDER BY` | 排序。NULLS FIRST / LAST 控制 NULL 排序位置 |
| `LIMIT / OFFSET` | 限制返回行数和偏移量 |

---

### 7.2 JOIN 種類の詳細

| JOIN 类型 | 描述 |
|-----------|------|
| **INNER JOIN** | 仅返回两表都匹配的行 |
| **LEFT [OUTER] JOIN** | 返回左表全部行 + 右表匹配行，不匹配时右表列为 NULL |
| **RIGHT [OUTER] JOIN** | 返回右表全部行 + 左表匹配行，不匹配时左表列为 NULL |
| **FULL [OUTER] JOIN** | 返回两表全部行，不匹配时对应列为 NULL |
| **CROSS JOIN** | 笛卡尔积（所有行的组合） |
| **NATURAL JOIN** | 自动按同名列进行 INNER/LEFT/RIGHT/FULL JOIN |
| **LATERAL JOIN** | サブクエリ/テーブル関数が左側テーブルの列を参照可能。FLATTEN でよく使用 |

#### 操作例

```sql
-- INNER JOIN
SELECT e.employee_id, e.first_name, d.department_name
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id;

-- LEFT JOIN
SELECT e.employee_id, e.first_name, d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id;

-- 多表 JOIN
SELECT e.first_name, d.department_name, l.city
FROM employees e
JOIN departments d ON e.department_id = d.department_id
LEFT JOIN locations l ON d.location_id = l.location_id;

-- CROSS JOIN
SELECT e.first_name, p.project_name
FROM employees e
CROSS JOIN projects p;

-- LATERAL JOIN (与 FLATTEN 结合)
SELECT e.employee_id, f.value::STRING AS skill
FROM employees e,
LATERAL FLATTEN(input => e.skills_array) f;
```

---

### 7.3 サブクエリと CTE

#### サブクエリ

```sql
-- スカラーサブクエリ（単一の値を返す）
SELECT employee_id, first_name, salary,
    (SELECT AVG(salary) FROM employees) AS avg_salary,
    salary - (SELECT AVG(salary) FROM employees) AS salary_diff
FROM employees;

-- IN / NOT IN サブクエリ
SELECT * FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE location = 'New York');

-- EXISTS / NOT EXISTS
SELECT * FROM employees e
WHERE EXISTS (SELECT 1 FROM sales s WHERE s.employee_id = e.employee_id);

-- 派生テーブル（FROM 句のサブクエリ）
SELECT dept_id, avg_sal
FROM (SELECT department_id AS dept_id, AVG(salary) AS avg_sal
      FROM employees GROUP BY department_id)
WHERE avg_sal > 80000;
```

#### CTE (WITH 子句)

```sql
-- 基础 CTE
WITH dept_stats AS (
    SELECT department_id, AVG(salary) AS avg_sal, COUNT(*) AS headcount
    FROM employees GROUP BY department_id
)
SELECT e.employee_id, e.first_name, e.salary, d.avg_sal
FROM employees e
JOIN dept_stats d ON e.department_id = d.department_id
WHERE e.salary > d.avg_sal;

-- 多个 CTE
WITH
    dept_avg AS (
        SELECT department_id, AVG(salary) AS avg_salary
        FROM employees GROUP BY department_id
    ),
    high_earners AS (
        SELECT e.*, d.avg_salary
        FROM employees e
        JOIN dept_avg d ON e.department_id = d.department_id
        WHERE e.salary > d.avg_salary * 1.5
    )
SELECT * FROM high_earners ORDER BY salary DESC;

-- 递归 CTE（组织架构树）
WITH RECURSIVE org_tree AS (
    -- 根节点（顶层管理者）
    SELECT employee_id, first_name, manager_id, 1 AS level,
           first_name AS path
    FROM employees WHERE manager_id IS NULL
    UNION ALL
    -- 递归查找下属
    SELECT e.employee_id, e.first_name, e.manager_id,
           t.level + 1, t.path || ' -> ' || e.first_name
    FROM employees e
    JOIN org_tree t ON e.manager_id = t.employee_id
)
SELECT * FROM org_tree ORDER BY level, employee_id;
```

---

### 7.4 ウィンドウ関数

Snowflake がサポートするウィンドウ関数は以下の種類に分類されます：

**排名函数：**

| 函数 | 描述 |
|------|------|
| `ROW_NUMBER()` | 连续行号 (1, 2, 3, ...) |
| `RANK()` | 跳跃排名 (1, 1, 3, ...) |
| `DENSE_RANK()` | 密集排名 (1, 1, 2, ...) |
| `NTILE(n)` | 分为 n 个桶 |
| `PERCENT_RANK()` | 相对排名 (0-1) |
| `CUME_DIST()` | 累积分布 |

**偏移函数：**

| 函数 | 描述 |
|------|------|
| `LAG(col, n, default)` | 前 n 行的值 |
| `LEAD(col, n, default)` | 后 n 行的值 |
| `FIRST_VALUE(col)` | 窗口内第一行的值 |
| `LAST_VALUE(col)` | 窗口内最后一行的值 |
| `NTH_VALUE(col, n)` | 窗口内第 n 行的值 |

**集約ウィンドウ関数：**
`SUM()`, `AVG()`, `COUNT()`, `MIN()`, `MAX()`, `STDDEV()`, `VARIANCE()` 等，配合 OVER() 子句

#### 操作例

```sql
-- 按部门内薪资排名
SELECT employee_id, first_name, department_id, salary,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rn,
    RANK()       OVER (PARTITION BY department_id ORDER BY salary DESC) AS rnk,
    DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS dense_rnk
FROM employees;

-- LAG / LEAD（与前一行/后一行对比）
SELECT employee_id, hire_date,
    LAG(hire_date)  OVER (ORDER BY hire_date) AS prev_hire,
    LEAD(hire_date) OVER (ORDER BY hire_date) AS next_hire,
    DATEDIFF('day', LAG(hire_date) OVER (ORDER BY hire_date), hire_date) AS days_since_prev
FROM employees;

-- 累计和/移动平均
SELECT sale_date, amount,
    SUM(amount) OVER (ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS cumulative_sum,
    AVG(amount) OVER (ORDER BY sale_date
        ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS moving_avg_7
FROM sales_fact;

-- 窗口框架 (Window Frame)
SELECT sale_date, amount,
    SUM(amount) OVER (ORDER BY sale_date
        RANGE BETWEEN INTERVAL '7' DAY PRECEDING AND CURRENT ROW) AS rolling_7d_sum,
    SUM(amount) OVER (PARTITION BY product_id ORDER BY sale_date
        ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS total_by_product
FROM sales_fact;
```

---

### 7.5 集合演算 (UNION / INTERSECT / EXCEPT)

| 操作符 | 描述 |
|--------|------|
| `UNION [ALL]` | 并集。ALL 保留重复行，不含 ALL 则去重 |
| `INTERSECT [ALL]` | 交集。ALL 保留重复行 |
| `EXCEPT [ALL]` | 差集。ALL 保留重复行 |
| `MINUS [ALL]` | 同 EXCEPT |

```sql
-- UNION (去重)
SELECT employee_id, first_name FROM employees_us
UNION
SELECT employee_id, first_name FROM employees_eu;

-- UNION ALL (保留重复)
SELECT employee_id, first_name FROM employees_us
UNION ALL
SELECT employee_id, first_name FROM employees_eu;

-- INTERSECT (交集)
SELECT employee_id FROM employees_2024
INTERSECT
SELECT employee_id FROM employees_2025;

-- EXCEPT (差集)
SELECT employee_id FROM employees
EXCEPT
SELECT employee_id FROM terminated_employees;
```

---

### 7.6 半構造化データクエリ (FLATTEN / LATERAL)

```sql
-- 基础 FLATTEN
SELECT *
FROM TABLE(FLATTEN(input => PARSE_JSON('[{"a":1},{"a":2},{"a":3}]')));

-- 从表列 FLATTEN
SELECT e.id, f.value::STRING AS skill
FROM employees e,
LATERAL FLATTEN(input => e.skills_array) f;

-- FLATTEN パラメータ詳細
SELECT e.id,
    f.seq,        -- 序列号
    f.key,        -- 键 (OBJECT) 或索引 (ARRAY)
    f.path,       -- 路径
    f.index,      -- 索引
    f.value,      -- 值
    f.this        -- 当前元素
FROM employees e,
LATERAL FLATTEN(input => e.doc:projects, OUTER => TRUE, RECURSIVE => TRUE) f;
```

---

### 7.7 QUALIFY 子句

`QUALIFY` は Snowflake 独自の句で、ウィンドウ関数の結果をフィルタリングします。これは `WHERE` でウィンドウ関数を使用するサブクエリと同等です。

```sql
-- 各部門で最も給与の高い従業員（QUALIFY でサブクエリを置き換え）
SELECT employee_id, first_name, department_id, salary
FROM employees
QUALIFY ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) = 1;

-- 以下と同等：
SELECT * FROM (
    SELECT employee_id, first_name, department_id, salary,
        ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rn
    FROM employees
) WHERE rn = 1;

-- 保留每个部门薪资 TOP 3
SELECT employee_id, first_name, department_id, salary
FROM employees
QUALIFY DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) <= 3;
```

---

### 7.8 SAMPLE / TABLESAMPLE サンプリングクエリ

```sql
-- 10% データをサンプリング（BERNOULLI: 行ごとにランダムサンプリング）
SELECT * FROM employees SAMPLE BERNOULLI (10);

-- 10% をサンプリング（SYSTEM: ブロック/マイクロパーティション単位、高速だが分布が不均一になる可能性あり）
SELECT * FROM employees SAMPLE SYSTEM (10);

-- 再現可能なサンプリング（同じ seed で同じ結果を返す）
SELECT * FROM employees SAMPLE BERNOULLI (10) REPEATABLE (42);

-- 使用 TABLESAMPLE 语法（等价）
SELECT * FROM employees TABLESAMPLE BERNOULLI (10) REPEATABLE (42);
```

---

### 7.9 PIVOT / UNPIVOT

```sql
-- PIVOT 行转列
SELECT *
FROM (SELECT department_id, job_title, salary FROM employees)
PIVOT (AVG(salary) FOR job_title IN ('Engineer', 'Manager', 'Analyst'))
AS p (department_id, avg_eng, avg_mgr, avg_analyst);

-- UNPIVOT 列转行
SELECT employee_id, skill_type, skill_level
FROM employee_skills
UNPIVOT (skill_level FOR skill_type IN (sql_level, python_level, java_level));
```

---

### 7.10 CONNECT BY 階層クエリ

```sql
-- 組織階層クエリ
SELECT employee_id, first_name, manager_id, LEVEL
FROM employees
START WITH manager_id IS NULL
CONNECT BY PRIOR employee_id = manager_id
ORDER BY LEVEL, employee_id;

-- 带层次路径
SELECT employee_id, first_name,
    LEVEL,
    SYS_CONNECT_BY_PATH(first_name, ' -> ') AS path,
    CONNECT_BY_ROOT first_name AS root_manager
FROM employees
START WITH manager_id IS NULL
CONNECT BY PRIOR employee_id = manager_id;
```

---

## 8. データロードとアンロード

### 8.1 Stage (ステージ) 詳細

**Stage 種類の比較：**

| 種類 | 作成方法 | 参照 | ライフサイクル |
|------|----------|------|----------|
| **User Stage** | 自動 | `@~` | ユーザーに追随 |
| **Table Stage** | 自動 | `@%table_name` | テーブルに追随 |
| **Internal Named Stage** | 手動作成 | `@stage_name` | 独立管理 |
| **External Stage** | 手動作成 | `@ext_stage` | 独立管理 |

**Stage のよく使う操作：**

```sql
-- Stage 内容の一覧表示
LIST @my_stage;
LIST @my_stage PATTERN='.*\\.csv$';
LIST @my_stage PATTERN='.*2026-05-.*\\.json';

-- 表示 Stage 详情
DESC STAGE my_stage;

-- ディレクトリテーブルのクエリ（需启用 DIRECTORY）
SELECT * FROM DIRECTORY(@my_stage);

-- Stage からファイルを削除
REMOVE @my_stage/old_file.csv;
REMOVE @my_stage PATTERN='.*\\.tmp$';
```

---

### 8.2 PUT コマンド ファイルアップロード

> **重要：** PUT コマンドは SnowSQL CLI 対話モードでのみ使用可能で、SQL Worksheet では使用できません。

```bash
# SnowSQL 対話モード内：

-- 単一ファイルを内部 Stage にアップロード
PUT file:///local/path/data.csv @my_internal_stage;

-- 複数ファイルのアップロード（ワイルドカード）
PUT file:///local/path/*.csv @my_internal_stage/;

-- 圧縮アップロード
PUT file:///local/path/data.csv @my_internal_stage/ AUTO_COMPRESS=TRUE;

-- 既存ファイルを上書き
PUT file:///local/path/data.csv @my_internal_stage/ OVERWRITE=TRUE;

-- ユーザー Stage にアップロード
PUT file:///local/path/data.csv @~/;

-- テーブル Stage にアップロード
PUT file:///local/path/data.csv @%employees/;

-- ソース圧縮を指定
PUT file:///local/path/data.csv @my_stage/ SOURCE_COMPRESSION=GZIP;
```

**PUT コマンドパラメータ説明：**

| パラメータ | 説明 |
|------|------|
| `AUTO_COMPRESS` | TRUE(デフォルト): 自動 gzip 圧縮アップロード |
| `OVERWRITE` | TRUE: 既存ファイルを上書き。FALSE(デフォルト): スキップ |
| `SOURCE_COMPRESSION` | ソースファイルの圧縮形式を宣言 (GZIP/BZIP2 他)、アップロード時に解凍しない |
| `PARALLEL` | 並列アップロードスレッド数、デフォルト 4 |

---

### 8.3 COPY INTO データロード

#### 完全な構文は [6.5 COPY INTO <table>](#65-copy-into-table-データロード) を参照

#### 典型的なロード手順：

**ステップ 1: データファイルの準備**
```
CSV 例 (employees.csv):
employee_id,first_name,last_name,email,hire_date,salary,department_id
1,John,Doe,john@example.com,2025-01-15,75000,10
2,Jane,Smith,jane@example.com,2025-03-20,80000,20
3,Bob,Johnson,bob@example.com,2025-06-01,65000,10
```

**ステップ 2: File Format の作成**

```sql
CREATE FILE FORMAT emp_csv_ff
    TYPE = 'CSV'
    SKIP_HEADER = 1
    FIELD_OPTIONALLY_ENCLOSED_BY = '"'
    NULL_IF = ('NULL', '', 'null');
```

**ステップ 3: ターゲットテーブルの作成**

```sql
CREATE TABLE employees (
    employee_id   INTEGER,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    email         VARCHAR(100),
    hire_date     DATE,
    salary        NUMBER(10,2),
    department_id INTEGER
);
```

**ステップ 4: Stage へのファイルアップロード**
```bash
# SnowSQL CLI
PUT file:///local/path/employees.csv @my_stage/;
```

**ステップ 5: COPY INTO の実行**

```sql
COPY INTO employees
FROM @my_stage/
FILES = ('employees.csv.gz')
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
ON_ERROR = 'ABORT_STATEMENT';
```

**ステップ 6: 結果の検証**

```sql
-- 表示加载的 COPY 历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('hours', -24, CURRENT_TIMESTAMP())
));

-- 行数の検証
SELECT COUNT(*) FROM employees;
```

---

### 8.4 VALIDATION_MODE データ検証

```sql
-- 先頭10行をプレビュー（実際にはロードしない）
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_10_ROWS;

-- 先頭100行をプレビュー
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_100_ROWS;

-- 全エラーを返す（データはロードしない）
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_ALL_ERRORS;

-- 失敗した行のみ返す
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
RETURN_FAILED_ONLY = TRUE;
```

---

### 8.5 Snowpipe 自動ロード

Snowpipe はファイル到着時に自動ロードを実現します。

**ステップ 1: Pipe の作成**

```sql
CREATE PIPE employee_pipe
    AUTO_INGEST = TRUE
    AWS_SNS_TOPIC = 'arn:aws:sns:us-east-1:1234567890:snowpipe-topic'
    AS
    COPY INTO employees
    FROM @my_s3_stage/employees/
    FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
    ON_ERROR = 'SKIP_FILE';
```

**ステップ 2: クラウドストレージイベント通知の設定**
- AWS: S3 バケット -> SNS トピック -> Snowpipe
- Azure: ストレージアカウント -> Event Grid -> Snowpipe
- GCP: GCS -> Pub/Sub -> Snowpipe

**ステップ 3: Pipe の監視**

```sql
-- 表示 Pipe 状态
SELECT SYSTEM$PIPE_STATUS('employee_pipe');

-- 表示加载历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.PIPE_USAGE_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    PIPE_NAME => 'EMPLOYEE_PIPE'
));

-- 表示 COPY 历史（所有来源包括 Pipe）
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('day', -1, CURRENT_TIMESTAMP())
));
```

---

### 8.6 データアンロード (UNLOAD)

```sql
-- CSV としてアンロード
COPY INTO @my_s3_stage/export/employees/
FROM employees
FILE_FORMAT = (TYPE = CSV COMPRESSION = GZIP)
HEADER = TRUE
MAX_FILE_SIZE = 50000000
OVERWRITE = TRUE;

-- Parquet としてアンロード
COPY INTO @my_s3_stage/export/employees_parquet/
FROM employees
FILE_FORMAT = (TYPE = PARQUET COMPRESSION = SNAPPY)
OVERWRITE = TRUE;

-- JSON としてアンロード
COPY INTO @my_s3_stage/export/employees_json/
FROM employees
FILE_FORMAT = (TYPE = JSON COMPRESSION = GZIP)
OVERWRITE = TRUE;

-- パーティションアンロード（部門別）
COPY INTO @my_s3_stage/export/employees/
FROM employees
PARTITION BY department_id
FILE_FORMAT = (TYPE = CSV)
HEADER = TRUE;

-- ファイルをローカルにダウンロード（SnowSQL CLI）
-- GET @my_s3_stage/export/employees/ file:///local/download/;
```

---

## 9. Time Travel (タイムトラベル) と Fail-safe (フェイルセーフ)

### 9.1 Time Travel 概要

Time Travel はデータ保持期間内に履歴データへのアクセス、クエリ、復旧を可能にします。

```
データ変更タイムライン:
<--- 過去                              現在              未来 --->
|...|------ Time Travel 保持期間 -----|------ Fail-safe ------|
     (0-90日、設定可、クエリ/復旧可)   (7日固定、クエリ不可、サポートによる復旧が必要)
```

**主な特性：**

| 特性 | Time Travel | Fail-safe |
|------|-------------|-----------|
| **クエリ可** | はい | いいえ |
| **クローン可** | はい | いいえ |
| **UNDROP 可** | はい | いいえ |
| **保持期間** | 0-90 日 (設定可) | 7 日 (固定) |
| **費用** | ストレージ費用に含まれる | 追加ストレージ費用 |
| **復旧方法** | ユーザーセルフサービス | Snowflake サポートへの連絡が必要 |

---

### 9.2 Time Travel 操作手順

**3つの時間指定方式：**

```sql
-- 1. TIMESTAMP: 正確な時刻を指定
SELECT * FROM employees
    AT (TIMESTAMP => '2026-05-12 08:00:00'::TIMESTAMP);

-- 2. OFFSET: 現在時刻からのオフセット（秒）
SELECT * FROM employees
    AT (OFFSET => -3600);  -- 1時間前

-- 3. STATEMENT: 指定クエリ ID 前の状態
SELECT * FROM employees
    BEFORE (STATEMENT => '01a2b3c4-0506-0708-090a-0b0c0d0e0f00');
```

**履歴データのクエリ：**

```sql
-- 5分前のテーブル状態をクエリ
SELECT * FROM employees AT (OFFSET => -300);

-- 昨日のテーブル状態をクエリ
SELECT * FROM employees AT (TIMESTAMP => DATEADD('day', -1, CURRENT_TIMESTAMP())::TIMESTAMP);

-- 特定時点をクエリ
SELECT * FROM employees AT (TIMESTAMP => '2026-05-11 14:30:00'::TIMESTAMP);

-- 過去の時点にクローン
CREATE TABLE employees_restored CLONE employees
    AT (TIMESTAMP => '2026-05-10 12:00:00'::TIMESTAMP);

-- BEFORE を使用（指定時点の前、AT と同義）
SELECT * FROM employees BEFORE (OFFSET => -3600);
```

**クエリ ID の取得：**

```sql
-- Query History から実行済みクエリの ID を取得
SELECT QUERY_ID, QUERY_TEXT, START_TIME
FROM TABLE(INFORMATION_SCHEMA.QUERY_HISTORY(
    DATE_RANGE_START => DATEADD('day', -1, CURRENT_TIMESTAMP()),
    DATE_RANGE_END => CURRENT_TIMESTAMP()
))
WHERE QUERY_TEXT ILIKE '%INSERT INTO employees%'
ORDER BY START_TIME DESC;
```

---

### 9.3 UNDROP オブジェクト復旧

復旧可能なオブジェクト種類：DATABASE、SCHEMA、TABLE、VIEW、FUNCTION、PROCEDURE、STAGE、FILE FORMAT、PIPE、SEQUENCE、STREAM、TASK。

```sql
-- 削除されたテーブルを復旧
UNDROP TABLE employees;

-- 削除されたデータベースを復旧
UNDROP DATABASE training_db;

-- 削除された Schema を復旧
UNDROP SCHEMA training_db.hr_schema;

-- 同名の新規テーブル作成後に旧テーブルを復旧する場合、先に新テーブルをリネーム
ALTER TABLE employees RENAME TO employees_new;
UNDROP TABLE employees;  -- 旧 employees を復旧
```

---

### 9.4 Fail-safe 概要

Fail-safe は Time Travel 期限切れ後、追加で7日間のデータ保護を提供します。この期間中データはユーザーに表示されず、クエリも不可です。復旧が必要な場合は Snowflake テクニカルサポートに連絡する必要があります。

---

### 9.5 データ保持ポリシー設定

```sql
-- アカウントレベル（ACCOUNTADMIN が必要）
ALTER ACCOUNT SET DATA_RETENTION_TIME_IN_DAYS = 14;

-- データベースレベル（アカウント設定を上書き）
ALTER DATABASE training_db SET DATA_RETENTION_TIME_IN_DAYS = 7;

-- Schema レベル（データベース設定を上書き）
ALTER SCHEMA training_db.hr_schema SET DATA_RETENTION_TIME_IN_DAYS = 3;

-- テーブルレベル（Schema 設定を上書き）
ALTER TABLE employees SET DATA_RETENTION_TIME_IN_DAYS = 1;

-- 作成时指定
CREATE TABLE critical_data (...) DATA_RETENTION_TIME_IN_DAYS = 90;
```

> **ベストプラクティス：** 本番テーブルは最低7日、重要業務テーブルは14-30日を推奨。一時的テーブルは1日固定。

---

## 10. Zero-Copy Cloning (ゼロコピークローン)

### 10.1 クローンの原理

クローンは実際のデータをコピーせず、メタデータポインタのみを複製します。そのため：
- **非常に高速：** データ量に関わらず、クローンはほぼ瞬時に完了
- **追加ストレージゼロ：** クローン後に追加のストレージコストは発生しない（後続の変更データのみ格納）
- **独立して書込可能：** クローン後のオブジェクトは独立して変更可能、変更はそのクローンのみに影響

```
クローン前:              クローン後:
Table_A                 Table_A          Table_A_CLONE
  |                      |                 |
  +-- Micro-partitions --+ (共有) ---------+
  |                      |
  +-- メタデータ           +-- 独立メタデータ  +-- 独立メタデータ
```

---

### 10.2 クローン操作手順

```sql
-- クローン数据库
CREATE DATABASE prod_clone CLONE prod_db;

-- クローン Schema
CREATE SCHEMA training_db.hr_clone CLONE training_db.hr_schema;

-- クローン表
CREATE TABLE employees_clone CLONE employees;

-- Time Travel 時点のテーブルをクローン
CREATE TABLE employees_yesterday CLONE employees
    AT (OFFSET => -86400);

CREATE TABLE employees_snapshot CLONE employees
    AT (TIMESTAMP => '2026-05-01 00:00:00'::TIMESTAMP);

-- クローン + 覆盖（如果目标已存在）
CREATE OR REPLACE TABLE employees_clone CLONE employees;

-- クローン带复制权限
CREATE TABLE employees_clone CLONE employees COPY GRANTS;
```

---

### 10.3 クローンの権限と注意事項

**必要な権限：**
- データベースのクローン：ソース DB の USAGE 権限 + ターゲット DB の CREATE DATABASE 権限が必要
- Schema のクローン：ソース Schema の USAGE 権限 + ターゲット DB の CREATE SCHEMA 権限が必要
- テーブルのクローン：ソーステーブルの SELECT 権限 + ターゲット Schema の CREATE TABLE 権限が必要

**注意事項：**
1. クローンは**シャローコピー**——クローンオブジェクトはソースオブジェクトのストレージを共有（データが変更されるまで）
2. クローンは Load History をコピーしない（ソーステーブルの COPY 履歴はクローンに引き継がれない）
3. クローンされた Pipe はデフォルトで停止状態
4. クローンはソースオブジェクトの権限付与を含む（COPY GRANTS 使用時）
5. クローンはソースオブジェクトの Time Travel 保持に影響——クローンが存在する限り、参照されているマイクロパーティションは物理削除されない

**一般的なユースケース：**
- 本番環境の即時開発/テストコピーを作成
- データバックアップとスナップショット
- データサイエンス実験（本番データに影響なし）
- 問題デバッグ（特定時点に戻ってデータ状態を分析）

---


## 11. Data Sharing (データ共有)

### 11.1 データ共有アーキテクチャ

Snowflake データ共有は、異なるアカウント間で安全にデータを共有することを可能にします——**データのコピーは不要**です。データ利用者は提供者が共有するリアルタイムデータを直接クエリできます。

```
Provider Account (提供者)              Consumer Account (消费者)
+-----------------------+            +------------------------+
| Database / Schema     |  Share    | Database from Share     |
| + Table / View / UDF  | ========> | (読み取り専用、ゼロコピー)          |
+-----------------------+            +------------------------+
```

**主要概念：**

| 概念 | 説明 |
|------|------|
| **Share** | 共有コンテナ、どのオブジェクトをどの利用者と共有するかを定義 |
| **Provider (提供者)** | データを所有し Share を作成するアカウント |
| **Consumer (消费者)** | 从 Share 创建只读数据库的账户 |
| **Reader Account** | 特別な利用者アカウント、Snowflake アカウントを持たない組織向けに作成 |
| **Data Marketplace** | Snowflake のデータマーケットプレイス、公開または限定共有が可能 |

---

### 11.2 Share の作成と設定

**ステップ 1: Share の作成**

```sql
-- 作成空的 Share
CREATE SHARE employee_data_share
    COMMENT = 'Employee data share for HR analytics';
```

**ステップ 2: Share へのオブジェクト追加**

```sql
-- データベース使用権を付与（利用者がデータベース構造を見るために必要）
GRANT USAGE ON DATABASE training_db TO SHARE employee_data_share;

-- Schema 使用権を付与
GRANT USAGE ON SCHEMA training_db.hr_schema TO SHARE employee_data_share;

-- テーブル読取専用権限を付与
GRANT SELECT ON TABLE training_db.hr_schema.employees TO SHARE employee_data_share;

-- ビューを付与
GRANT SELECT ON VIEW training_db.hr_schema.vw_salary_summary TO SHARE employee_data_share;

-- 安全 UDF を付与
GRANT USAGE ON FUNCTION training_db.hr_schema.calculate_bonus(INTEGER, FLOAT)
    TO SHARE employee_data_share;
```

**ステップ 3: 利用者アカウントの追加**

```sql
-- 1つの利用者アカウントを追加
ALTER SHARE employee_data_share
    ADD ACCOUNTS = consumer_org.consumer_account;

-- 複数の利用者アカウントを追加
ALTER SHARE employee_data_share
    ADD ACCOUNTS = consumer1_org.consumer1_account,
                   consumer2_org.consumer2_account;

-- 利用者を削除
ALTER SHARE employee_data_share
    REMOVE ACCOUNTS = consumer_org.old_account;
```

**ステップ 4: Share 設定の検証**

```sql
-- 表示 Share 详情
SHOW SHARES;
DESC SHARE employee_data_share;

-- 表示 Share 中包含的对象
SHOW GRANTS TO SHARE employee_data_share;

-- 表示 Share 的消费者
SHOW GRANTS OF SHARE employee_data_share;
```

---

### 11.3 Reader Account の作成

受信側が Snowflake アカウントを持っていない場合、提供者は Reader Account を作成できます。

```sql
-- 作成 Reader Account
CREATE MANAGED ACCOUNT reader_account_name
    ADMIN_NAME = 'reader_admin'
    ADMIN_PASSWORD = '<strong_password>'
    TYPE = READER
    COMMENT = 'Reader account for partner XYZ';

-- 表示 Reader Account 的状态
SHOW MANAGED ACCOUNTS;

-- Reader Account を Share に追加
ALTER SHARE employee_data_share
    ADD ACCOUNTS = reader_account_locator;
```

---

### 11.4 Share からデータベースを作成 (利用者側)

```sql
-- 表示收到的 Share（需使用 ACCOUNTADMIN）
SHOW SHARES;
-- 表示收到的入站 Share
SELECT * FROM TABLE(INFORMATION_SCHEMA.INBOUND_SHARES());

-- Share からデータベースを作成
CREATE DATABASE shared_employee_data
    FROM SHARE provider_org.provider_account.employee_data_share;

-- アクセス可能なテーブルの確認
SELECT * FROM shared_employee_data.hr_schema.employees LIMIT 10;

-- 直接共有（Direct Share）の場合、利用者は追加権限不要
-- 作成新角色并授予权限（消费者侧）
CREATE ROLE share_reader;
GRANT ROLE share_reader TO USER analyst_user;
GRANT IMPORTED PRIVILEGES ON DATABASE shared_employee_data TO ROLE share_reader;
```

---

### 11.5 Data Marketplace

```sql
-- 表示可用的 Marketplace Listing
SHOW LISTINGS;

-- Marketplace データセットの取得
-- 1. Snowsight -> Data -> Marketplace で閲覧・取得
-- 2. または SQL で:
CREATE DATABASE marketplace_db FROM SHARE provider_org.provider_account.listing_share;

-- 作成私有 Listing（仅指定消费者可见）
CREATE LISTING my_private_listing
    FOR SHARE employee_data_share
    COMMENT = 'Private HR data listing'
    AS PRIVATELISTING;
```

---

## 12. セキュリティとアクセス制御 (RBAC)

### 12.1 RBAC モデル概要

Snowflake は**ロールベースアクセス制御 (RBAC)** と**任意アクセス制御 (DAC)** のハイブリッドモデルを採用しています。

**中核概念：**

```
ACCOUNTADMIN (最上位、すべてを管理)
  |
  +-- SECURITYADMIN (ユーザーとロールを管理)
  |     |
  |     +-- USERADMIN (ユーザーとロールを作成)
  |
  +-- SYSADMIN (すべてのデータベースオブジェクトを管理)
        |
        +-- CUSTOM_ROLE_A (カスタムロール)
              |
              +-- CUSTOM_ROLE_B (ロール継承)
```

**定義済みシステムロール：**

| ロール | 責務 |
|------|------|
| **ACCOUNTADMIN** | 最高権限ロール。SECURITYADMIN + SYSADMIN の全権限を含む |
| **SECURITYADMIN** | ユーザー、ロール、権限付与を管理。ユーザーとロールの作成/管理が可能 |
| **USERADMIN** | ユーザーとロールの作成（SECURITYADMIN の制限付きサブセット） |
| **SYSADMIN** | 全データベースオブジェクトを管理（Warehouse、データベース、テーブル、ビュー他） |
| **PUBLIC** | 全ユーザーがデフォルトで持つ。最小権限 |

**権限種類：**

| 権限カテゴリ | 権限例 |
|----------|----------|
| **アカウント権限** | CREATE DATABASE, CREATE WAREHOUSE, CREATE USER, CREATE ROLE |
| **データベース権限** | USAGE, MODIFY, MONITOR, CREATE SCHEMA |
| **Schema 権限** | USAGE, MODIFY, MONITOR, CREATE TABLE, CREATE VIEW, CREATE STAGE |
| **テーブル権限** | SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES |
| **ビュー権限** | SELECT, REFERENCES |
| **Stage 権限** | READ, WRITE |
| **関数/プロシージャ権限** | USAGE |

---

### 12.2 ユーザー管理操作手順

**ユーザーの作成：**

```sql
-- 基本作成
CREATE USER john_doe
    PASSWORD = 'TempPass123!'
    DEFAULT_ROLE = data_analyst
    DEFAULT_WAREHOUSE = analyst_wh
    DEFAULT_NAMESPACE = training_db.public
    MUST_CHANGE_PASSWORD = TRUE
    COMMENT = 'Data Analyst - John Doe';

-- フル作成
CREATE USER jane_smith
    PASSWORD = 'SecurePass456!'
    LOGIN_NAME = 'jane.smith@company.com'
    DISPLAY_NAME = 'Jane Smith'
    FIRST_NAME = 'Jane'
    LAST_NAME = 'Smith'
    EMAIL = 'jane.smith@company.com'
    MUST_CHANGE_PASSWORD = TRUE
    DISABLED = FALSE
    DAYS_TO_EXPIRY = 90
    DEFAULT_WAREHOUSE = analyst_wh
    DEFAULT_NAMESPACE = training_db.hr_schema
    DEFAULT_ROLE = data_analyst
    COMMENT = 'Senior Data Analyst';
```

**ユーザーの管理：**

```sql
-- 変更用户
ALTER USER john_doe SET PASSWORD = 'NewPass789!';
ALTER USER john_doe SET DISABLED = TRUE;         -- ユーザーを無効化
ALTER USER john_doe SET DISABLED = FALSE;        -- ユーザーを有効化
ALTER USER john_doe RESET PASSWORD;               -- パスワードをリセット
ALTER USER john_doe SET DEFAULT_ROLE = data_engineer;
ALTER USER john_doe SET DEFAULT_WAREHOUSE = etl_wh;

-- 表示用户
SHOW USERS;
SHOW USERS LIKE 'john_doe';
DESC USER john_doe;

-- 削除用户
DROP USER john_doe;
```

---

### 12.3 ロール管理操作手順

```sql
-- 作成角色
CREATE ROLE data_analyst;
CREATE ROLE data_engineer COMMENT = 'ETL pipeline operators';
CREATE ROLE data_scientist COMMENT = 'ML and analytics';

-- ロール継承（GRANT ROLE ... TO ROLE）
GRANT ROLE data_analyst TO ROLE SYSADMIN;
GRANT ROLE data_engineer TO ROLE SYSADMIN;

-- ロールをユーザーに割り当て
GRANT ROLE data_analyst TO USER john_doe;
GRANT ROLE data_analyst TO USER jane_smith;

-- 表示角色
SHOW ROLES;
SHOW GRANTS TO ROLE data_analyst;
SHOW GRANTS OF ROLE data_analyst;

-- 切り替え当前角色
USE ROLE data_analyst;
SELECT CURRENT_ROLE();

-- 表示用户的角色
SHOW GRANTS TO USER john_doe;

-- ロールの取り消し
REVOKE ROLE data_analyst FROM USER john_doe;

-- 削除角色
DROP ROLE data_analyst;
```

---

### 12.4 権限付与と取り消し

**操作手順：**

```sql
-- === ステップ 1: SYSADMIN でオブジェクト作成 ===
USE ROLE SYSADMIN;
CREATE DATABASE company_data;
CREATE SCHEMA company_data.hr;
CREATE TABLE company_data.hr.employees (...);

-- === ステップ 2: 機能ロールの作成 ===
USE ROLE SECURITYADMIN;
CREATE ROLE hr_analyst;
CREATE ROLE hr_manager;

-- === ステップ 3: データベースと Schema 権限の付与 ===
USE ROLE SYSADMIN;
GRANT USAGE ON DATABASE company_data TO ROLE hr_analyst;
GRANT USAGE ON SCHEMA company_data.hr TO ROLE hr_analyst;

-- === ステップ 4: テーブル権限の付与 ===
GRANT SELECT ON company_data.hr.employees TO ROLE hr_analyst;
GRANT SELECT, INSERT, UPDATE, DELETE ON company_data.hr.employees TO ROLE hr_manager;

-- === ステップ 5: ビュー権限の付与 ===
GRANT SELECT ON VIEW company_data.hr.vw_salary_summary TO ROLE hr_manager;

-- === ステップ 6: Warehouse 権限の付与 ===
GRANT USAGE ON WAREHOUSE analyst_wh TO ROLE hr_analyst;

-- === ステップ 7: ロールをユーザーに付与 ===
USE ROLE SECURITYADMIN;
GRANT ROLE hr_analyst TO USER john_doe;

-- === 権限の取り消し ===
REVOKE SELECT ON company_data.hr.employees FROM ROLE hr_analyst;
REVOKE ALL ON company_data.hr.employees FROM ROLE hr_manager;

-- === 全権限付与の表示 ===
SHOW GRANTS ON DATABASE company_data;
SHOW GRANTS ON SCHEMA company_data.hr;
SHOW GRANTS ON TABLE company_data.hr.employees;
SHOW GRANTS TO ROLE hr_analyst;
SHOW GRANTS TO USER john_doe;
```

---

### 12.5 ネットワークポリシー

ネットワークポリシーは Snowflake への接続を許可する IP アドレス範囲を制御します。

```sql
-- ネットワークポリシーを作成
CREATE NETWORK POLICY corp_network_policy
    ALLOWED_IP_LIST = ('192.168.1.0/24', '10.0.0.0/8')
    BLOCKED_IP_LIST = ('192.168.1.100')
    COMMENT = 'Corporate office and VPN network';

-- アカウントに適用
ALTER ACCOUNT SET NETWORK_POLICY = corp_network_policy;

-- ユーザーに適用
ALTER USER john_doe SET NETWORK_POLICY = restricted_policy;

-- 表示
SHOW NETWORK POLICIES;
DESC NETWORK POLICY corp_network_policy;

-- 解除
ALTER ACCOUNT UNSET NETWORK_POLICY;
```

---

### 12.6 データ暗号化

Snowflake はすべてのデータを自動的に暗号化します：
- **転送中の暗号化：** TLS 1.2+
- **保存データの暗号化：** AES-256 で全保存データを自動暗号化
- **クライアントサイド暗号化：** クライアント提供の鍵で追加の暗号化層（Stage へのアップロード時）

```sql
-- 作成客户端加密的 Stage
CREATE STAGE encrypted_stage
    ENCRYPTION = (TYPE = 'AWS_CSE' MASTER_KEY = '<your_master_key>')
    URL = 's3://my-bucket/encrypted/'
    STORAGE_INTEGRATION = my_s3_integration;
```

---

### 12.7 Column-Level Security (カラムレベルセキュリティ)

**Masking Policy** を使用してカラムレベルデータマスキングを実現します。

```sql
-- 作成脱敏策略
CREATE MASKING POLICY mask_ssn AS (val STRING) RETURNS STRING ->
    CASE
        WHEN CURRENT_ROLE() IN ('HR_MANAGER') THEN val
        ELSE '***-**-' || RIGHT(val, 4)
    END;

CREATE MASKING POLICY mask_salary AS (val NUMBER) RETURNS NUMBER ->
    CASE
        WHEN CURRENT_ROLE() IN ('HR_MANAGER', 'PAYROLL_ADMIN') THEN val
        ELSE NULL
    END;

-- マスキングポリシーを列に適用
ALTER TABLE employees MODIFY COLUMN ssn
    SET MASKING POLICY mask_ssn;

ALTER TABLE employees MODIFY COLUMN salary
    SET MASKING POLICY mask_salary;

-- 表示脱敏策略
SHOW MASKING POLICIES;

-- 解除脱敏策略
ALTER TABLE employees MODIFY COLUMN salary
    UNSET MASKING POLICY;
```

---

### 12.8 Row-Level Security (行レベルセキュリティ)

**Row Access Policy** を使用して行レベルアクセス制御を実現します。

```sql
-- 作成行访问策略
CREATE ROW ACCESS POLICY dept_access_policy AS (dept_id INTEGER) RETURNS BOOLEAN ->
    CASE
        WHEN CURRENT_ROLE() = 'HR_MANAGER' THEN TRUE
        WHEN CURRENT_ROLE() = 'HR_DEPT10' AND dept_id = 10 THEN TRUE
        WHEN CURRENT_ROLE() = 'HR_DEPT20' AND dept_id = 20 THEN TRUE
        ELSE FALSE
    END;

-- 行アクセスポリシーの適用
ALTER TABLE employees
    ADD ROW ACCESS POLICY dept_access_policy ON (department_id);

-- 表示
SHOW ROW ACCESS POLICIES;

-- 解除
ALTER TABLE employees DROP ROW ACCESS POLICY dept_access_policy;
ALTER TABLE employees DROP ALL ROW ACCESS POLICIES;
```

---

## 13. Virtual Warehouse (仮想ウェアハウス) とパフォーマンス

### 13.1 Warehouse 概要

Virtual Warehouse は Snowflake のコンピュートリソースユニットです。各 Warehouse は独立したコンピュートクラスターです。

**Warehouse Size 対応リソース：**

| Size | ノード数 | クレジット/時間 | ユースケース |
|------|--------|-------------|----------|
| X-Small | 1 | 1 | 開発/テスト/軽量クエリ |
| Small | 2 | 2 | 標準クエリ |
| Medium | 4 | 4 | 中量 ETL/レポート |
| Large | 8 | 8 | 重量 ETL/大規模テーブルスキャン |
| X-Large | 16 | 16 | 大規模変換/集約 |
| 2X-Large | 32 | 32 | 超大規模計算 |
| 3X-Large | 64 | 64 | 極大規模計算 |
| 4X-Large | 128 | 128 | 最大規模計算 |
| 5X-Large | 256 | 256 | (サポートへの連絡が必要) |
| 6X-Large | 512 | 512 | (サポートへの連絡が必要) |

> **経験則：** 各 Size 倍増 = パフォーマンス約倍増 = コスト倍増。重要な原則：**より大きな Warehouse でより短時間にクエリを完了 = コスト増加なし**（クレジット = 時間 × ノード数）。

---

### 13.2 Warehouse 作成と設定

```sql
-- 作成 Warehouse
CREATE WAREHOUSE analyst_wh
    WAREHOUSE_SIZE = 'SMALL'
    AUTO_SUSPEND = 300           -- アイドル5分後に自動停止
    AUTO_RESUME = TRUE           -- クエリ実行時に自動再開
    INITIALLY_SUSPENDED = TRUE   -- 作成后立即挂起
    MIN_CLUSTER_COUNT = 1
    MAX_CLUSTER_COUNT = 1
    SCALING_POLICY = 'STANDARD'
    MAX_CONCURRENCY_LEVEL = 8
    STATEMENT_TIMEOUT_IN_SECONDS = 3600
    COMMENT = 'Analyst warehouse - business hours';

-- 変更 Warehouse
ALTER WAREHOUSE analyst_wh SET WAREHOUSE_SIZE = 'MEDIUM';
ALTER WAREHOUSE analyst_wh SET AUTO_SUSPEND = 120;  -- 2分

-- 手動操作
ALTER WAREHOUSE analyst_wh SUSPEND;     -- 停止
ALTER WAREHOUSE analyst_wh RESUME;      -- 再開
ALTER WAREHOUSE analyst_wh RESUME IF SUSPENDED;

-- 表示 Warehouse
SHOW WAREHOUSES;
SHOW WAREHOUSES LIKE 'analyst_wh';
DESC WAREHOUSE analyst_wh;

-- 表示 Warehouse 使用情况
SELECT * FROM TABLE(INFORMATION_SCHEMA.WAREHOUSE_METERING_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE())
));

-- 削除
DROP WAREHOUSE analyst_wh;
```

---

### 13.3 マルチクラスター Warehouse

高い同時実行処理が必要な場合、マルチクラスター Warehouse を有効にします。

```sql
-- 作成多集群 Warehouse
CREATE WAREHOUSE report_wh
    WAREHOUSE_SIZE = 'MEDIUM'
    MIN_CLUSTER_COUNT = 1
    MAX_CLUSTER_COUNT = 3
    SCALING_POLICY = 'STANDARD'    -- 迅速なスケーリング
    AUTO_SUSPEND = 300
    AUTO_RESUME = TRUE;

-- ECONOMY モード（より保守的、予算重視向け）
ALTER WAREHOUSE report_wh SET SCALING_POLICY = 'ECONOMY';

-- 表示集群状态
SHOW WAREHOUSES;
-- 表示当前活动集群数
SELECT * FROM TABLE(INFORMATION_SCHEMA.WAREHOUSE_LOAD_HISTORY(
    DATE_RANGE_START => DATEADD('hour', -1, CURRENT_TIMESTAMP()),
    WAREHOUSE_NAME => 'REPORT_WH'
));
```

**STANDARD vs ECONOMY スケーリング戦略：**

| 戦略 | スケールアップ速度 | スケールダウン速度 | 適したシナリオ |
|------|----------|----------|----------|
| STANDARD | 高速応答 | 必要に応じて縮小 | ユーザー対話型クエリ、迅速な応答が必要 |
| ECONOMY | 保守的、キューが一定閾値に達してから拡張 | 保守的縮小 | バッチ処理、予算重視、短時間のキューイング許容 |

---

### 13.4 クエリパフォーマンス最適化

**コア最適化戦略：**

```sql
-- 1. 適切な Warehouse Size の選択
-- 小クエリは小 WH、大クエリは大 WH

-- 2. DATE/TIMESTAMP フィルターの使用（マイクロパーティションプルーニングを活用）
SELECT * FROM sales_fact
WHERE sale_date BETWEEN '2026-05-01' AND '2026-05-12';

-- 3. SELECT * を避ける（必要な列のみ選択）
SELECT employee_id, first_name, last_name FROM employees;  -- 良い
SELECT * FROM employees;  -- 避ける

-- 4. LIMIT で結果を制限
SELECT * FROM large_table LIMIT 1000;

-- 5. 結果キャッシュの活用（24時間以内の同一クエリはキャッシュから直接返却）
-- 同一クエリ、同一結果セット、基データ未変更 -> 0秒で返却、クレジット消費なし

-- 6. Clustering を活用して大規模テーブルスキャンを最適化
ALTER TABLE sales_fact CLUSTER BY (sale_date, region);
```

---

### 13.5 結果キャッシュ / メタデータキャッシュ / データキャッシュ

| キャッシュ種類 | 格納場所 | 有効期間 | 説明 |
|----------|----------|--------|------|
| **結果キャッシュ** | Cloud Services | 24 時間 | 完全同一クエリはキャッシュ結果を直接返却。基データ変更後自動無効化。0 クレジット |
| **メタデータキャッシュ** | Cloud Services | 長期間 | 行数、MIN/MAX 値等の統計情報。クエリ最適化とパーティションプルーニングに使用 |
| **データキャッシュ** | Warehouse SSD | クエリ中 + 停止前 | Warehouse ローカル SSD にキャッシュされたマイクロパーティションデータ、後続スキャンを高速化 |

```sql
-- 表示是否使用了结果缓存
SELECT * FROM employees WHERE department_id = 10;  -- 1回目：クエリ実行
SELECT * FROM employees WHERE department_id = 10;  -- 2回目：結果キャッシュヒット

-- 結果キャッシュの無効化（パフォーマンステスト比較用）
ALTER SESSION SET USE_CACHED_RESULT = FALSE;
```

---

### 13.6 マイクロパーティションと Clustering

**マイクロパーティション：**
- Snowflake は自動的にデータを連続したマイクロパーティションに分割（デフォルト 50-500 MB/個、非圧縮）
- 各マイクロパーティションは列指向データを格納し、MIN/MAX 統計情報を含む
- クエリ時に自動的に **Partition Pruning**：関連するマイクロパーティションのみスキャン

**Clustering (クラスタリング)：**
自然クラスタリング（挿入時刻順）が有効でなくなった場合、クラスタリングキーを定義してデータを再構成できます。

```sql
-- 作成带聚类键的表
CREATE TABLE sales_fact (
    sale_date  DATE,
    product_id INTEGER,
    region     VARCHAR,
    amount     NUMBER(12, 2)
) CLUSTER BY (sale_date, region);

-- クラスタリングキーの追加/変更
ALTER TABLE sales_fact CLUSTER BY (sale_date, product_id);

-- 削除聚类键
ALTER TABLE sales_fact DROP CLUSTERING KEY;

-- 手動で再クラスタリングをトリガー
ALTER TABLE sales_fact RECLUSTER;

-- 表示聚类信息
SELECT * FROM TABLE(INFORMATION_SCHEMA.AUTOMATIC_CLUSTERING_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    TABLE_NAME => 'SALES_FACT'
));

-- 表示表的聚类深度
SELECT SYSTEM$CLUSTERING_INFORMATION('sales_fact');
```

**Clustering が必要なケース：**
- テーブルが非常に大きい（TB レベル）
- クエリフィルター条件がデータ挿入順序と関連しない
- クエリパフォーマンスが明らかに低下（多数のマイクロパーティションをスキャンするが少数行しか返さない）
- 1TB 未満のテーブルでの使用は推奨しない

---

### 13.7 マテリアライズドビューとクエリ高速化

**マテリアライズドビュー：** 集約結果を事前計算し、クエリ時に再計算せず直接読み取ります。

```sql
CREATE MATERIALIZED VIEW mv_hourly_sales AS
SELECT DATE_TRUNC('HOUR', sale_date) AS hour,
       region, product_id,
       SUM(amount) AS total_amount,
       COUNT(*) AS txn_count
FROM sales_fact
GROUP BY 1, 2, 3;
```

**クエリ高速化サービス (Query Acceleration Service)：**
マテリアライズドビューを作成せずに、クエリ内のオフロード可能なスキャン/フィルター/部分集約操作を自動識別します。

```sql
-- クエリ高速化の有効化（Warehouse レベル）
ALTER WAREHOUSE analyst_wh SET
    ENABLE_QUERY_ACCELERATION = TRUE
    QUERY_ACCELERATION_MAX_SCALE_FACTOR = 8;
```

---

### 13.8 Search Optimization Service

特定の種類のクエリ（ポイント検索、部分文字列検索、GEO 検索など）を高速化します。

```sql
-- 検索最適化の追加
ALTER TABLE employees
    ADD SEARCH OPTIMIZATION ON EQUALITY(employee_id, email);

ALTER TABLE large_logs
    ADD SEARCH OPTIMIZATION ON SUBSTRING(message);

ALTER TABLE customer_locations
    ADD SEARCH OPTIMIZATION ON GEO(location);

-- 表示搜索优化状态
SELECT SYSTEM$ESTIMATE_SEARCH_OPTIMIZATION_COSTS('employees');

-- 削除搜索优化
ALTER TABLE employees DROP SEARCH OPTIMIZATION;
```

---

### 13.9 Query Profile パフォーマンス分析

Snowsight で Query Profile を表示：
1. **Activity -> Query History** を開く
2. 対象クエリを探し、**Query ID** をクリック
3. **Query Profile** タブを表示

**分析のポイント：**

| 指標 | 意味 | 最適化の方向性 |
|------|------|----------|
| **Partitions Scanned** | スキャンされたマイクロパーティション数 | 高値 => Clustering 追加 |
| **Bytes Scanned** | スキャンバイト数 | 高値 => WHERE 条件追加または Clustering |
| **Spilling to Local Storage** | ローカルストレージ溢出 | Medium サイズ不足 => Warehouse 拡大 |
| **Spilling to Remote Storage** | リモートストレージ溢出 | Warehouse 深刻な不足 => Warehouse 拡大 |
| **Percentage Scanned from Cache** | キャッシュヒット率 | 低 => クエリパターン確認、Warehouse 拡大でデータキャッシュ改善 |
| **Total Execution Time** | 総実行時間 | 各ステップを比較してボトルネック特定 |

```sql
-- クエリ統計を直接取得
SELECT * FROM TABLE(INFORMATION_SCHEMA.QUERY_HISTORY())
WHERE QUERY_ID = '<query_id>';

-- 最近のスロークエリを表示
SELECT QUERY_ID, QUERY_TEXT, EXECUTION_TIME / 1000 AS seconds,
       BYTES_SCANNED / 1024 / 1024 / 1024 AS gb_scanned,
       PARTITIONS_SCANNED,
       PARTITIONS_TOTAL,
       WAREHOUSE_SIZE
FROM TABLE(SNOWFLAKE.ACCOUNT_USAGE.QUERY_HISTORY)
WHERE EXECUTION_STATUS = 'SUCCESS'
  AND START_TIME >= DATEADD('day', -1, CURRENT_TIMESTAMP())
  AND EXECUTION_TIME > 60000  -- 60秒超
ORDER BY EXECUTION_TIME DESC
LIMIT 20;
```

---

## 14. 半構造化データ処理

### 14.1 VARIANT 型操作

```sql
-- 作成包含 VARIANT 的表
CREATE TABLE event_logs (
    event_id   INTEGER AUTOINCREMENT,
    event_data VARIANT,
    event_ts   TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

-- JSON データの挿入
INSERT INTO event_logs (event_data)
SELECT PARSE_JSON('{
    "user": {"id": 123, "name": "John Doe", "email": "john@example.com"},
    "action": "purchase",
    "items": [
        {"product_id": "P100", "quantity": 2, "price": 29.99},
        {"product_id": "P200", "quantity": 1, "price": 49.99}
    ],
    "total": 109.97,
    "timestamp": "2026-05-12T14:30:00Z"
}');

-- VARIANT フィールドのクエリ
SELECT
    event_data:user.name::STRING AS user_name,
    event_data:user.id::INTEGER AS user_id,
    event_data:action::STRING AS action,
    event_data:total::FLOAT AS total,
    event_data:items[0].product_id::STRING AS first_product,
    event_data:items[0].price::FLOAT AS first_price,
    ARRAY_SIZE(event_data:items) AS item_count
FROM event_logs;

-- 型の確認
SELECT TYPEOF(event_data) FROM event_logs;                              -- VARIANT
SELECT TYPEOF(event_data:user.name) FROM event_logs;                    -- VARIANT
SELECT TYPEOF(event_data:user.name::STRING) FROM event_logs;            -- VARCHAR
SELECT IS_ARRAY(event_data:items) FROM event_logs;                      -- TRUE

-- VARIANT の更新
UPDATE event_logs
SET event_data = OBJECT_INSERT(event_data, 'status', 'completed', TRUE)
WHERE event_id = 1;
```

---

### 14.2 FLATTEN 関数詳細

```sql
-- FLATTEN 構文
FLATTEN(
    INPUT => <expr>,
    PATH => <constant_path>,
    OUTER => TRUE | FALSE,
    RECURSIVE => TRUE | FALSE,
    MODE => OBJECT | ARRAY | BOTH
)

-- ARRAY の展開
SELECT
    e.event_id,
    f.index,
    f.value AS item,
    f.value:product_id::STRING AS product_id,
    f.value:quantity::INTEGER AS quantity,
    f.value:price::FLOAT AS price
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:items) f;

-- OBJECT のキー・バリュー展開
SELECT
    e.event_id,
    f.key,
    f.value
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:user) f;

-- OUTER => TRUE（空配列/NULL の行を保持）
SELECT e.event_id, f.value
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:items, OUTER => TRUE) f;

-- RECURSIVE => TRUE（ネスト構造を再帰的に展開）
SELECT f.*
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data, RECURSIVE => TRUE) f;
```

**FLATTEN 出力列：**

| 列 | 型 | 説明 |
|----|------|------|
| SEQ | INTEGER | シーケンス番号 |
| KEY | VARCHAR | キー(OBJECT)またはインデックス(ARRAY) |
| PATH | VARCHAR | その要素へのパス |
| INDEX | INTEGER | 配列内の要素インデックス(0ベース) |
| VALUE | VARIANT | 要素の値 |
| THIS | VARIANT | 展開された要素自体 |

---

### 14.3 PARSE_JSON / PARSE_XML

```sql
-- PARSE_JSON
SELECT PARSE_JSON('{"name": "John", "age": 30}');
SELECT TRY_PARSE_JSON('invalid json');  -- エラー時は NULL を返す

-- OBJECT_CONSTRUCT (オブジェクト作成)
SELECT OBJECT_CONSTRUCT('name', 'John', 'age', 30, 'active', TRUE);

-- OBJECT_KEYS (全キー取得)
SELECT OBJECT_KEYS(PARSE_JSON('{"name":"John","age":30}'));  -- ["name","age"]

-- OBJECT_DELETE / OBJECT_INSERT
SELECT OBJECT_DELETE(PARSE_JSON('{"name":"John","age":30}'), 'age');
SELECT OBJECT_INSERT(PARSE_JSON('{"name":"John"}'), 'age', 30);

-- ARRAY_CONSTRUCT (配列作成)
SELECT ARRAY_CONSTRUCT('a', 'b', 'c');
SELECT ARRAY_CONSTRUCT(1, 2, 3);

-- ARRAY_AGG (配列に集約)
SELECT department_id, ARRAY_AGG(employee_id) AS emp_ids
FROM employees GROUP BY department_id;

-- 配列操作
SELECT ARRAY_APPEND(['a','b'], 'c');       -- ['a','b','c']
SELECT ARRAY_PREPEND('a', ['b','c']);     -- ['a','b','c']
SELECT ARRAY_CAT(['a'], ['b','c']);       -- ['a','b','c']
SELECT ARRAY_SIZE(['a','b','c']);         -- 3
SELECT ARRAY_SLICE(['a','b','c','d'], 1, 3); -- ['b','c']
SELECT ARRAY_CONTAINS(1, [1,2,3]);        -- TRUE

-- PARSE_XML
SELECT PARSE_XML('<root><name>John</name><age>30</age></root>');
SELECT XMLGET(PARSE_XML('<root><name>John</name></root>'), 'name');
SELECT GET(XMLGET(PARSE_XML('<root><name>John</name></root>'), 'name'), '$');
```

---

### 14.4 JSON / Parquet / Avro / XML データのロード

**JSON データのロード：**

```sql
-- 1. File Format の作成
CREATE FILE FORMAT json_ff
    TYPE = 'JSON'
    STRIP_OUTER_ARRAY = TRUE    -- 外層配列を剥がし、各要素が独立行に
    ALLOW_DUPLICATE = FALSE
    ENABLE_OCTAL = FALSE;

-- 2. ターゲットテーブルの作成
CREATE TABLE json_raw (data VARIANT);

-- 3. ロード
COPY INTO json_raw
FROM @my_stage/json_data/
FILE_FORMAT = (FORMAT_NAME = json_ff);

-- 4. 構造化ビューの作成
CREATE VIEW vw_parsed_events AS
SELECT
    data:user.id::INTEGER AS user_id,
    data:user.name::STRING AS user_name,
    data:action::STRING AS action,
    data:total::FLOAT AS total,
    data:timestamp::TIMESTAMP AS event_time
FROM json_raw;
```

**Parquet データのロード：**

```sql
-- Parquet を直接構造化列にロード
COPY INTO sales_structured (sale_date, product_id, region, amount)
FROM (
    SELECT $1:sale_date::DATE,
           $1:product_id::INTEGER,
           $1:region::STRING,
           $1:amount::NUMBER(12,2)
    FROM @my_stage/parquet_data/
)
FILE_FORMAT = (TYPE = PARQUET);

-- または VARIANT としてロード
COPY INTO parquet_raw (data)
FROM @my_stage/parquet_data/
FILE_FORMAT = (TYPE = PARQUET);
```

**Avro データのロード：**

```sql
COPY INTO avro_raw (data)
FROM @my_stage/avro_data/
FILE_FORMAT = (TYPE = AVRO);
```

**XML データのロード：**

```sql
CREATE FILE FORMAT xml_ff
    TYPE = 'XML'
    STRIP_OUTER_ELEMENT = TRUE
    PRESERVE_SPACE = FALSE;

COPY INTO xml_raw (data)
FROM @my_stage/xml_data/
FILE_FORMAT = (FORMAT_NAME = xml_ff);
```

---

### 14.5 半構造化データ最適化

```sql
-- 1. 頻繁にクエリされるパスを独立列に抽出（マテリアライズ）
CREATE TABLE events_optimized AS
SELECT
    event_data,
    event_data:user.id::INTEGER AS user_id,
    event_data:action::STRING AS action,
    event_data:total::FLOAT AS total,
    event_data:timestamp::TIMESTAMP AS event_time
FROM event_logs;

-- 2. 抽出列に Clustering を作成
ALTER TABLE events_optimized CLUSTER BY (event_time);

-- 3. VARIANT 列パスに Search Optimization を作成
ALTER TABLE event_logs
    ADD SEARCH OPTIMIZATION ON EQUALITY(event_data:user.id),
    ADD SEARCH OPTIMIZATION ON EQUALITY(event_data:action);

-- 4. チェーンアクセスの代わりに GET_PATH を使用
SELECT GET_PATH(event_data, 'user.name')::STRING FROM event_logs;
-- 以下と同等
SELECT event_data:user.name::STRING FROM event_logs;

-- 5. VARIANT 上での頻繁な型変換を避ける
-- 良くない
SELECT * FROM event_logs WHERE event_data:user.id::INTEGER = 123;
-- 良い（如果已在提取列上优化）
SELECT * FROM events_optimized WHERE user_id = 123;
```

---


## 15. UDF / UDTF / ストアドプロシージャ

### 15.1 SQL UDF 作成と使用

#### 完全な構文

```sql
CREATE [ OR REPLACE ] [ { TEMP | TEMPORARY } ] [ SECURE ]
    FUNCTION [ IF NOT EXISTS ] <name>
    ( [ <arg_name> <arg_type> [ DEFAULT <expr> ] [ , ... ] ] )
    RETURNS <result_type>
    [ NOT NULL ]
    [ VOLATILE | IMMUTABLE ]
    [ CALLED ON NULL INPUT | { RETURNS NULL ON NULL INPUT | STRICT } ]
    [ COMMENT = '<string>' ]
    LANGUAGE SQL
    AS { $$<sql_expression>$$ | '<sql_expression>' }
```

#### 操作例

```sql
-- 简单标量 UDF
CREATE FUNCTION add_tax(price FLOAT, tax_rate FLOAT)
    RETURNS FLOAT
    IMMUTABLE
AS $$
    price * (1 + tax_rate)
$$;

SELECT add_tax(100, 0.08);  -- 108.0

-- 带默认参数的 UDF
CREATE FUNCTION full_name(first VARCHAR, last VARCHAR, title VARCHAR DEFAULT '')
    RETURNS VARCHAR
AS $$
    IFF(title = '', first || ' ' || last, title || ' ' || first || ' ' || last)
$$;

SELECT full_name('John', 'Doe');              -- John Doe
SELECT full_name('Jane', 'Smith', 'Dr.');     -- Dr. Jane Smith

-- STRICT 模式（NULL 输入返回 NULL）
CREATE FUNCTION safe_divide(a FLOAT, b FLOAT)
    RETURNS FLOAT
    RETURNS NULL ON NULL INPUT
AS $$ a / b $$;

SELECT safe_divide(10, 0);       -- NULL (而非报错)
SELECT safe_divide(NULL, 5);     -- NULL

-- 安全 UDF（隐藏实现）
CREATE SECURE FUNCTION hash_email(email VARCHAR)
    RETURNS VARCHAR
    IMMUTABLE
AS $$
    SHA2(email, 256)
$$;
```

---

### 15.2 JavaScript UDF 作成と使用

```sql
CREATE FUNCTION js_reverse_string(input VARCHAR)
    RETURNS VARCHAR
    LANGUAGE JAVASCRIPT
AS $$
    if (INPUT === null) return null;
    return INPUT.split('').reverse().join('');
$$;

SELECT js_reverse_string('Snowflake');  -- ekalfwonS

-- 复杂 JavaScript UDF
CREATE FUNCTION js_json_extract_keys(json_str VARCHAR)
    RETURNS ARRAY
    LANGUAGE JAVASCRIPT
AS $$
    if (JSON_STR === null) return [];
    try {
        var obj = JSON.parse(JSON_STR);
        return Object.keys(obj);
    } catch (e) {
        return [];
    }
$$;

SELECT js_json_extract_keys('{"name":"John","age":30}');  -- ["name","age"]
```

---

### 15.3 Python UDF 作成と使用

```sql
-- 基础 Python UDF
CREATE FUNCTION py_fibonacci(n INTEGER)
    RETURNS INTEGER
    LANGUAGE PYTHON
    RUNTIME_VERSION = '3.10'
    HANDLER = 'fib'
AS $$
def fib(n):
    if n <= 1:
        return n
    a, b = 0, 1
    for _ in range(n - 1):
        a, b = b, a + b
    return b
$$;

SELECT py_fibonacci(10);  -- 55

-- 带第三方包的 Python UDF
CREATE FUNCTION py_sentiment(text VARCHAR)
    RETURNS FLOAT
    LANGUAGE PYTHON
    RUNTIME_VERSION = '3.10'
    PACKAGES = ('textblob',)
    HANDLER = 'analyze'
AS $$
from textblob import TextBlob
def analyze(text):
    if text is None:
        return None
    blob = TextBlob(text)
    return blob.sentiment.polarity
$$;

-- 导入文件的 Python UDF
-- 先从 Stage 导入 helper.py 模块
CREATE FUNCTION py_with_imports(data VARCHAR)
    RETURNS VARCHAR
    LANGUAGE PYTHON
    RUNTIME_VERSION = '3.10'
    IMPORTS = ('@my_stage/python_libs/helpers.py',)
    HANDLER = 'helpers.transform'
AS $$
# helpers.py 中的函数将被调用
$$;
```

---

### 15.4 Java UDF 作成と使用

```sql
CREATE FUNCTION java_base64_decode(encoded VARCHAR)
    RETURNS VARCHAR
    LANGUAGE JAVA
    RUNTIME_VERSION = '11'
    HANDLER = 'Base64Decoder.decode'
AS $$
    import java.util.Base64;
    class Base64Decoder {
        public static String decode(String encoded) {
            if (encoded == null) return null;
            return new String(Base64.getDecoder().decode(encoded));
        }
    }
$$;
```

---

### 15.5 UDTF (テーブル関数) 作成と使用

```sql
-- SQL UDTF
CREATE FUNCTION get_dept_stats(dept_id INTEGER)
    RETURNS TABLE (
        dept_id INTEGER, emp_count INTEGER,
        total_salary NUMBER, avg_salary NUMBER
    )
AS $$
    SELECT department_id, COUNT(*), SUM(salary), AVG(salary)
    FROM employees
    WHERE department_id = dept_id
    GROUP BY department_id
$$;

SELECT * FROM TABLE(get_dept_stats(10));

-- Python UDTF
CREATE FUNCTION py_split_sentences(text VARCHAR)
    RETURNS TABLE (sentence_index INTEGER, sentence VARCHAR)
    LANGUAGE PYTHON
    RUNTIME_VERSION = '3.10'
    HANDLER = 'SplitSentences'
AS $$
class SplitSentences:
    def process(self, text):
        if text is None:
            return []
        import re
        sentences = re.split(r'[.!?]+', text)
        return [(i, s.strip()) for i, s in enumerate(sentences) if s.strip()]
$$;

SELECT * FROM TABLE(py_split_sentences('Hello world. How are you? I am fine!'))
ORDER BY sentence_index;
```

---

### 15.6 JavaScript ストアドプロシージャ

```sql
CREATE PROCEDURE js_etl_process(table_name VARCHAR, source_stage VARCHAR)
    RETURNS VARCHAR
    LANGUAGE JAVASCRIPT
    EXECUTE AS CALLER
AS $$
    // データロードを実行
    var copy_sql = `COPY INTO ${TABLE_NAME} FROM @${SOURCE_STAGE} FILE_FORMAT = (FORMAT_NAME = 'my_csv_ff')`;
    var stmt = snowflake.createStatement({sqlText: copy_sql});
    var result = stmt.execute();

    // 获取加载信息
    result.next();
    var rows_loaded = result.getColumnValue(1);
    var status = result.getColumnValue(3);

    return `Loaded ${rows_loaded} rows from ${SOURCE_STAGE}. Status: ${status}`;
$$;

CALL js_etl_process('EMPLOYEES', 'my_s3_stage');

-- JavaScript ストアドプロシージャでクエリを実行し結果を反復処理
CREATE PROCEDURE js_process_high_salary(threshold NUMBER)
    RETURNS VARCHAR
    LANGUAGE JAVASCRIPT
    EXECUTE AS CALLER
AS $$
    var query = `SELECT employee_id, first_name, last_name, salary
                 FROM employees WHERE salary > ${THRESHOLD}
                 ORDER BY salary DESC`;
    var stmt = snowflake.createStatement({sqlText: query});
    var rs = stmt.execute();

    var result = [];
    while (rs.next()) {
        result.push({
            id: rs.getColumnValue(1),
            name: rs.getColumnValue(2) + ' ' + rs.getColumnValue(3),
            salary: rs.getColumnValue(4)
        });
    }
    return JSON.stringify(result);
$$;
```

---

### 15.7 Python ストアドプロシージャ

```sql
CREATE PROCEDURE py_feature_engineering()
    RETURNS VARCHAR
    LANGUAGE PYTHON
    RUNTIME_VERSION = '3.10'
    PACKAGES = ('snowflake-snowpark-python', 'scikit-learn')
    HANDLER = 'run'
    EXECUTE AS CALLER
AS $$
import numpy as np
from sklearn.preprocessing import StandardScaler
from snowflake.snowpark import Session

def run(session: Session) -> str:
    # 从 Snowflake 读取数据
    df = session.sql("SELECT salary, years_experience, performance_score FROM employees")

    # 转为 Pandas 进行特征工程
    pdf = df.to_pandas()
    scaler = StandardScaler()
    pdf['salary_scaled'] = scaler.fit_transform(pdf[['salary']])

    # 写回 Snowflake
    result_df = session.create_dataframe(pdf)
    result_df.write.mode('overwrite').save_as_table('employee_features')

    return f'Feature engineering complete. Processed {len(pdf)} rows.'
$$;

CALL py_feature_engineering();
```

---

### 15.8 SQL ストアドプロシージャ (Snowflake Scripting)

Snowflake Scripting 支持变量、条件、循环、游标、异常处理等。

```sql
-- 基础示例：条件逻辑
CREATE PROCEDURE update_salary_with_raise(
    emp_id INTEGER,
    raise_percent FLOAT
)
    RETURNS VARCHAR
    LANGUAGE SQL
    EXECUTE AS CALLER
AS
$$
DECLARE
    old_salary NUMBER(10,2);
    new_salary NUMBER(10,2);
BEGIN
    SELECT salary INTO :old_salary FROM employees WHERE employee_id = :emp_id;

    IF (:old_salary IS NULL) THEN
        RETURN 'Employee not found';
    END IF;

    new_salary := :old_salary * (1 + :raise_percent / 100.0);

    UPDATE employees SET salary = :new_salary WHERE employee_id = :emp_id;

    RETURN 'Salary updated: ' || :old_salary || ' -> ' || :new_salary;
END;
$$;

-- FOR 循环示例
CREATE PROCEDURE batch_salary_increase(dept_id INTEGER, raise_pct FLOAT)
    RETURNS INTEGER
    LANGUAGE SQL
AS
$$
DECLARE
    counter INTEGER DEFAULT 0;
    cur CURSOR FOR
        SELECT employee_id FROM employees
        WHERE department_id = :dept_id;
BEGIN
    FOR rec IN cur DO
        UPDATE employees SET salary = salary * (1 + :raise_pct / 100.0)
        WHERE employee_id = rec.employee_id;
        counter := counter + 1;
    END FOR;

    RETURN :counter;
END;
$$;

-- 异常处理
CREATE PROCEDURE safe_insert(emp_id INTEGER, fname VARCHAR, lname VARCHAR, sal NUMBER)
    RETURNS VARCHAR
    LANGUAGE SQL
AS
$$
BEGIN
    INSERT INTO employees (employee_id, first_name, last_name, salary)
    VALUES (:emp_id, :fname, :lname, :sal);
    RETURN 'Insert successful';
EXCEPTION
    WHEN STATEMENT_ERROR THEN
        RETURN 'Error: ' || SQLERRM;
    WHEN OTHER THEN
        RETURN 'Unknown error occurred';
END;
$$;

-- 构建动态 SQL
CREATE PROCEDURE dynamic_count(table_name VARCHAR)
    RETURNS INTEGER
    LANGUAGE SQL
AS
$$
DECLARE
    sql_stmt VARCHAR;
    row_count INTEGER;
BEGIN
    sql_stmt := 'SELECT COUNT(*) FROM ' || :table_name;
    EXECUTE IMMEDIATE :sql_stmt INTO :row_count;
    RETURN :row_count;
END;
$$;
```

---

## 16. Streams & Tasks (ストリームとタスク)

### 16.1 Stream 概要と種類

Stream はテーブルの**増分変更**（INSERT、UPDATE、DELETE）を追跡し、前回消費以降の全データ変更を記録します。

**3つの種類：**

| 種類 | 追跡内容 | ユースケース |
|------|----------|----------|
| **Standard (Delta)** | INSERT + UPDATE + DELETE | 完全 CDC（変更データキャプチャ） |
| **Append-Only** | INSERT のみ | 追加式データソース（ログ、イベント） |
| **Insert-Only** | Append-Only と同じ（エイリアス） | Append-Only と同じ |

**Stream の動作原理：**
- Stream 作成時はデータをコピーせず、オフセットのみ記録
- Stream クエリ時、前回オフセット以降に変更された行を返す
- 各 DML 操作後、Stream は自動的に進行（トランザクション内で消費した場合）
- Stream のオフセットは明示的な消費（SELECT/DML での使用）後にのみ進行

**Stream 出力列：**

| 列 | 説明 |
|----|------|
| `METADATA$ACTION` | 変更種類: INSERT / DELETE |
| `METADATA$ISUPDATE` | UPDATE かどうか（TRUE/FALSE）|
| `METADATA$ROW_ID` | 行の一意識別子 |

---

### 16.2 Standard Stream 操作手順

**ステップ 1: Stream の作成**

```sql
-- 前提：テーブルで CHANGE_TRACKING を有効化
ALTER TABLE employees SET CHANGE_TRACKING = TRUE;

-- 作成 Stream
CREATE STREAM employees_stream ON TABLE employees
    COMMENT = 'Tracks all DML changes on employees';

-- 表示 Stream 状态
SHOW STREAMS;
DESC STREAM employees_stream;
```

**ステップ 2: 変更の発生（シミュレーション）**

```sql
-- Stream の動作を確認するため変更をシミュレーション
-- INSERT
INSERT INTO employees VALUES (100, 'Alice', 'Wang', 'alice@test.com', '2026-05-12', 85000, 10, TRUE, CURRENT_TIMESTAMP());

-- UPDATE
UPDATE employees SET salary = 90000 WHERE employee_id = 100;

-- DELETE
DELETE FROM employees WHERE employee_id = 100;
```

**ステップ 3: Stream 内容のクエリ**

```sql
-- 初回クエリ（変更後）
SELECT
    employee_id,
    first_name,
    salary,
    METADATA$ACTION,
    METADATA$ISUPDATE,
    METADATA$ROW_ID
FROM employees_stream;
-- 結果: INSERT(新バージョン) + DELETE(旧バージョン) 各1行（UPDATE に対応）
-- + INSERT 行（INSERT 操作）
-- + DELETE 行（DELETE 操作）
```

**ステップ 4: Stream の消費（増分 ETL の実行）**

```sql
-- 増分挿入/更新
MERGE INTO employees_history t
USING employees_stream s
ON t.employee_id = s.employee_id
    AND t.valid_from = (
        SELECT MAX(valid_from)
        FROM employees_history
        WHERE employee_id = s.employee_id
    )
WHEN MATCHED AND s.METADATA$ACTION = 'DELETE' THEN
    UPDATE SET t.valid_to = CURRENT_TIMESTAMP(), t.is_current = FALSE
WHEN NOT MATCHED AND s.METADATA$ACTION = 'INSERT' THEN
    INSERT (employee_id, first_name, salary, valid_from, valid_to, is_current)
    VALUES (s.employee_id, s.first_name, s.salary, CURRENT_TIMESTAMP(), NULL, TRUE);
```

**ステップ 5: Stream が空になったか検証**

```sql
-- 消費後 Stream は空になるはず
SELECT COUNT(*) FROM employees_stream;  -- 0 になるはず
```

---

### 16.3 Append-Only Stream 操作手順

```sql
-- 作成仅追加 Stream（只跟踪 INSERT，不跟踪 UPDATE/DELETE）
CREATE STREAM employees_append_stream ON TABLE employees
    APPEND_ONLY = TRUE;

-- INSERT 行のみ返す、METADATA$ISUPDATE なし
SELECT * FROM employees_append_stream;
```

---

### 16.4 Task 作成と管理

Task は定期的に実行されるスケジュール単位で、定時 ETL パイプラインの構築に使用できます。

**Serverless Task vs Warehouse Task：**

| 特性 | Warehouse Task | Serverless Task |
|------|---------------|-----------------|
| コンピュートリソース | Warehouse 指定 | Snowflake 管理 |
| 課金 | Warehouse クレジット | Cloud Services クレジット |
| 用途 | 長時間/重量級タスク | 短時間/軽量級タスク |

```sql
-- 作成 Warehouse Task
CREATE TASK hourly_employee_sync
    WAREHOUSE = etl_wh
    SCHEDULE = '60 MINUTE'
    COMMENT = 'Hourly sync of employee data'
AS
    MERGE INTO employees_report t
    USING employees_stream s ON t.employee_id = s.employee_id
    WHEN MATCHED AND s.METADATA$ACTION = 'DELETE' THEN DELETE
    WHEN MATCHED AND s.METADATA$ACTION = 'INSERT' THEN
        UPDATE SET t.first_name = s.first_name, t.salary = s.salary
    WHEN NOT MATCHED THEN
        INSERT (employee_id, first_name, salary)
        VALUES (s.employee_id, s.first_name, s.salary);

-- 作成 Serverless Task
CREATE TASK serverless_cleanup
    USER_TASK_MANAGED_INITIAL_WAREHOUSE_SIZE = 'XSMALL'
    SCHEDULE = 'USING CRON 0 2 * * * UTC'  -- 毎日午前2時 UTC
AS
    DELETE FROM temp_logs
    WHERE created_at < DATEADD('day', -7, CURRENT_TIMESTAMP());

-- 作成 Task DAG（依赖链）
CREATE TASK task_step1
    WAREHOUSE = etl_wh
    SCHEDULE = '60 MINUTE'
AS
    CALL refresh_staging_data();

CREATE TASK task_step2
    WAREHOUSE = etl_wh
    AFTER task_step1
AS
    CALL transform_and_load();

CREATE TASK task_step3
    WAREHOUSE = etl_wh
    AFTER task_step2
AS
    CALL generate_reports();

-- Task の管理
ALTER TASK hourly_employee_sync SUSPEND;
ALTER TASK hourly_employee_sync RESUME;
ALTER TASK hourly_employee_sync SET SCHEDULE = '30 MINUTE';

-- Task の手動実行
EXECUTE TASK hourly_employee_sync;

-- 表示 Task 执行历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.TASK_HISTORY(
    TASK_NAME => 'HOURLY_EMPLOYEE_SYNC',
    SCHEDULED_TIME_RANGE_START => DATEADD('day', -7, CURRENT_TIMESTAMP())
));

-- 表示 Task DAG 依赖
SELECT * FROM TABLE(INFORMATION_SCHEMA.TASK_DEPENDENTS(
    TASK_NAME => 'TASK_STEP1',
    RECURSIVE => TRUE
));

-- 削除 Task
DROP TASK hourly_employee_sync;
```

---

### 16.5 Stream + Task 増分 ETL パイプライン構築

**完全な例：CDC パイプラインの構築**

```sql
-- === ステップ 1: 環境準備 ===
CREATE DATABASE cdc_demo;
CREATE SCHEMA cdc_demo.etl;
USE SCHEMA cdc_demo.etl;

-- === ステップ 2: ソーステーブルとターゲットテーブルの作成 ===
CREATE TABLE source_orders (
    order_id    INTEGER,
    customer_id INTEGER,
    amount      NUMBER(10,2),
    status      VARCHAR(20),
    updated_at  TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

CREATE TABLE orders_history (
    history_id  INTEGER AUTOINCREMENT,
    order_id    INTEGER,
    customer_id INTEGER,
    amount      NUMBER(10,2),
    status      VARCHAR(20),
    valid_from  TIMESTAMP_LTZ,
    valid_to    TIMESTAMP_LTZ,
    is_current  BOOLEAN,
    change_type VARCHAR(10)  -- INSERT / UPDATE / DELETE
);

-- ソーステーブルの変更追跡を有効化
ALTER TABLE source_orders SET CHANGE_TRACKING = TRUE;

-- === ステップ 3: Stream の作成 ===
CREATE STREAM orders_stream ON TABLE source_orders;

-- === ステップ 4: 増分ロードストアドプロシージャの作成 ===
CREATE PROCEDURE incremental_load()
    RETURNS VARCHAR
    LANGUAGE SQL
    EXECUTE AS OWNER
AS
$$
DECLARE
    processed_count INTEGER;
BEGIN
    -- 既存レコードをクローズ
    UPDATE orders_history h
    SET valid_to = CURRENT_TIMESTAMP(), is_current = FALSE
    FROM orders_stream s
    WHERE h.order_id = s.order_id
      AND h.is_current = TRUE
      AND s.METADATA$ACTION IN ('INSERT', 'DELETE');

    -- 新バージョンの挿入（INSERT 操作 + UPDATE の新バージョン）
    INSERT INTO orders_history (
        order_id, customer_id, amount, status,
        valid_from, valid_to, is_current, change_type
    )
    SELECT
        s.order_id, s.customer_id, s.amount, s.status,
        CURRENT_TIMESTAMP(), NULL, TRUE,
        CASE
            WHEN s.METADATA$ISUPDATE THEN 'UPDATE'
            ELSE s.METADATA$ACTION
        END
    FROM orders_stream s
    WHERE s.METADATA$ACTION = 'INSERT';

    -- 削除済みレコードのマーク（UPDATE による DELETE 行を除外）
    UPDATE orders_history h
    SET valid_to = CURRENT_TIMESTAMP(), is_current = FALSE, change_type = 'DELETE'
    FROM orders_stream s
    WHERE h.order_id = s.order_id
      AND h.is_current = TRUE
      AND s.METADATA$ACTION = 'DELETE'
      AND NOT s.METADATA$ISUPDATE;

    processed_count := (SELECT COUNT(*) FROM orders_stream);
    RETURN 'Processed ' || :processed_count || ' changes';
END;
$$;

-- === ステップ 5: スケジュール Task の作成 ===
CREATE TASK cdc_orders_sync
    WAREHOUSE = etl_wh
    SCHEDULE = '5 MINUTE'
    WHEN SYSTEM$STREAM_HAS_DATA('orders_stream')
AS
    CALL incremental_load();

-- Task の有効化
ALTER TASK cdc_orders_sync RESUME;

-- === ステップ 6: 検証 ===
-- テストデータの挿入
INSERT INTO source_orders VALUES (1, 100, 99.99, 'PENDING', CURRENT_TIMESTAMP());
INSERT INTO source_orders VALUES (2, 200, 149.99, 'PENDING', CURRENT_TIMESTAMP());

-- Task 実行を手動トリガー（テスト用）
EXECUTE TASK cdc_orders_sync;

-- ターゲットテーブルの検証
SELECT * FROM orders_history;

-- UPDATE と DELETE のシミュレーション
UPDATE source_orders SET status = 'COMPLETED' WHERE order_id = 1;
DELETE FROM source_orders WHERE order_id = 2;

EXECUTE TASK cdc_orders_sync;

-- 表示完整的变更历史
SELECT * FROM orders_history
ORDER BY order_id, valid_from;
```

---

## 17. アカウントとリソース管理

### 17.1 アカウント構造

Snowflake のアカウント構造概要：

```
Organization
 +-- Account 1 (例: prod)
 |    +-- Database / Schema / Table ...
 |    +-- User / Role / Warehouse ...
 +-- Account 2 (例: dev)
 |    +-- Database / Schema / Table ...
 +-- Account 3 (例: analytics)
      +-- ...
```

---

### 17.2 Resource Monitor 作成と使用

Resource Monitor は Warehouse のクレジット消費を制御するために使用します。

```sql
-- 作成 Resource Monitor
CREATE RESOURCE MONITOR monthly_budget
    WITH
        CREDIT_QUOTA = 1000                 -- 月間クレジット枠
        FREQUENCY = MONTHLY                 -- 評価頻度
        START_TIMESTAMP = IMMEDIATELY       -- 即時開始
        TRIGGERS
            ON 80 PERCENT DO NOTIFY         -- 80% で通知
            ON 90 PERCENT DO NOTIFY         -- 90% で通知
            ON 100 PERCENT DO SUSPEND       -- 100% で関連 WH を全て停止
            ON 110 PERCENT DO SUSPEND_IMMEDIATE;  -- 110% で即時停止

-- Warehouse に関連付け
ALTER WAREHOUSE analyst_wh SET RESOURCE_MONITOR = monthly_budget;
ALTER WAREHOUSE etl_wh SET RESOURCE_MONITOR = monthly_budget;

-- 表示 Resource Monitor
SHOW RESOURCE MONITORS;
DESC RESOURCE MONITOR monthly_budget;

-- 表示 credit 使用情况
SELECT * FROM SNOWFLAKE.ACCOUNT_USAGE.METERING_HISTORY
WHERE START_TIME >= DATE_TRUNC('MONTH', CURRENT_DATE());

-- 変更
ALTER RESOURCE MONITOR monthly_budget SET CREDIT_QUOTA = 2000;

-- 削除
DROP RESOURCE MONITOR monthly_budget;
```

---

### 17.3 情報スキーマと Account Usage

**INFORMATION_SCHEMA (現在のデータベース)：**

```sql
-- 表示当前数据库下的表
SELECT * FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'PUBLIC';

-- 表示列信息
SELECT * FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'EMPLOYEES';

-- 表示 COPY 历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('day', -1, CURRENT_TIMESTAMP())
));

-- 表示表存储大小
SELECT * FROM INFORMATION_SCHEMA.TABLE_STORAGE_METRICS
WHERE TABLE_NAME = 'EMPLOYEES';
```

**ACCOUNT_USAGE (データベース横断、1-3時間の遅延)：**

```sql
-- Warehouse 使用統計
SELECT WAREHOUSE_NAME,
       SUM(CREDITS_USED) AS total_credits,
       SUM(CREDITS_USED_COMPUTE) AS compute_credits,
       SUM(CREDITS_USED_CLOUD_SERVICES) AS cloud_credits
FROM SNOWFLAKE.ACCOUNT_USAGE.WAREHOUSE_METERING_HISTORY
WHERE START_TIME >= DATEADD('day', -30, CURRENT_DATE())
GROUP BY 1
ORDER BY 2 DESC;

-- クエリ履歴（低速クエリ検索）
SELECT QUERY_ID, QUERY_TEXT,
       EXECUTION_TIME / 1000 AS exec_seconds,
       BYTES_SCANNED / 1024 / 1024 AS mb_scanned,
       ROWS_PRODUCED,
       WAREHOUSE_NAME, WAREHOUSE_SIZE,
       USER_NAME, ROLE_NAME
FROM SNOWFLAKE.ACCOUNT_USAGE.QUERY_HISTORY
WHERE START_TIME >= DATEADD('day', -7, CURRENT_DATE())
  AND EXECUTION_STATUS = 'SUCCESS'
ORDER BY EXECUTION_TIME DESC
LIMIT 50;

-- ストレージ使用
SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME,
       ACTIVE_BYTES / 1024 / 1024 / 1024 AS active_gb,
       TIME_TRAVEL_BYTES / 1024 / 1024 / 1024 AS tt_gb,
       FAILSAFE_BYTES / 1024 / 1024 / 1024 AS fs_gb
FROM SNOWFLAKE.ACCOUNT_USAGE.TABLE_STORAGE_METRICS
WHERE TABLE_CATALOG = 'TRAINING_DB'
ORDER BY ACTIVE_BYTES DESC;

-- ログイン履歴
SELECT USER_NAME, EVENT_TIMESTAMP, FIRST_AUTHENTICATION_FACTOR,
       CLIENT_IP, REPORTED_CLIENT_TYPE
FROM SNOWFLAKE.ACCOUNT_USAGE.LOGIN_HISTORY
WHERE EVENT_TIMESTAMP >= DATEADD('day', -7, CURRENT_DATE())
ORDER BY EVENT_TIMESTAMP DESC;

-- 権限監査
SELECT * FROM SNOWFLAKE.ACCOUNT_USAGE.GRANTS_TO_USERS
WHERE GRANTEE_NAME = 'JOHN_DOE'
  AND DELETED_ON IS NULL;
```

---

### 17.4 コスト管理

**コスト最適化戦略：**

| 戦略 | 操作 | 期待される削減効果 |
|------|------|----------|
| **AUTO_SUSPEND** | 適切な自動停止時間を設定 (60-300s) | アイドル時間 100% 削減 |
| **AUTO_RESUME** | TRUE を維持 | 手動管理不要 |
| **Warehouse Size** | 適切なサイズ選択 | 大 WH 短時間 = 同じコスト |
| **結果キャッシュ** | 24h 結果キャッシュを活用 | 重複クエリ 0 コスト |
| **Resource Monitor** | 予算アラートと自動停止を設定 | 予期せぬ超過を防止 |
| **Transient テーブル** | 一時/中間データに一時的テーブルを使用 | 7日間 Fail-safe ストレージ費削減 |
| **クローン** | 開発/テスト環境にクローン使用 | 追加ストレージ不要 |

---

## 18. ベストプラクティスとよくある問題

### 18.1 テーブル設計ベストプラクティス

1. **適切なテーブル種類の選択：**
   - 本番データ -> 永続テーブル
   - 一時計算 -> 一時テーブル
   - ETL 中間データ -> 一時的テーブル（コスト削減）

2. **適切なデータ型の選択：**
   - `CHAR` より `VARCHAR` を優先（ストレージ効率は同じ、柔軟性が高い）
   - 日付時刻は `TIMESTAMP_LTZ` を優先（クロスタイムゾーンに最適）
   - 半構造化データは `VARIANT` を使用（事前スキーマ定義不要）

3. **Clustering 戦略：**
   - TB 級以上かつクエリパターンが自然ソート順と一致しないテーブルのみ
   - カーディナリティが適度な列を選択（高すぎず低すぎず）
   - 高カーディナリティ列を先に（timestamp 等）、低カーディナリティ列を後に（region 等）

4. **制約の使用：**
   - PRIMARY KEY / UNIQUE / FOREIGN KEY はドキュメント目的のみ、強制されない
   - 一意性の強制が必要な場合はアプリケーション層で実装

---

### 18.2 クエリ最適化ベストプラクティス

1. **WHERE フィルターでスキャン量を削減**
2. **SELECT \* を避け**、必要な列のみ選択
3. **LIMIT で結果セットを制限**
4. **重複サブクエリより CTE を優先**
5. **大規模テーブル JOIN 時は先にフィルターしてから JOIN**
6. **ウィンドウ関数処理はサブクエリの代わりに QUALIFY を活用**
7. **重複クエリは結果キャッシュを活用**

```sql
-- 良くない
SELECT * FROM sales_fact;
SELECT * FROM sales_fact WHERE YEAR(sale_date) = 2026;

-- 良い
SELECT sale_date, amount FROM sales_fact LIMIT 1000;
SELECT * FROM sales_fact WHERE sale_date BETWEEN '2026-01-01' AND '2026-12-31';
```

---

### 18.3 データロードベストプラクティス

1. **ファイルサイズ：** 100-250 MB（圧縮後）のファイルを推奨。小さすぎ（メタデータオーバーヘッド増加）や大きすぎ（並列化不可）を避ける
2. **並列ロード：** 複数の COPY 文で異なるファイルをロード
3. **先に検証：** `VALIDATION_MODE` でデータをプレビューしてから実際にロード
4. **増分ロード：** Snowpipe または Stream+Task で増分を実現
5. **スキーマ進化：** `MATCH_BY_COLUMN_NAME` と `ENABLE_SCHEMA_EVOLUTION` で列変更を自動処理

---

### 18.4 セキュリティベストプラクティス

1. **最小権限の原則：** 必要な権限のみ付与
2. **ロール継承の活用：** 明確なロール階層を構築
3. **デフォルトでユーザー無効：** 新規ユーザー作成後 `DISABLED = TRUE`、承認後に有効化
4. **Key Pair 認証：** 本番システムではパスワードの代わりに Key Pair を使用
5. **ネットワークポリシー：** IP 許可リストを制限
6. **MFA の有効化：** すべての人間ユーザーに多要素認証を有効化
7. **機密データのマスキング：** Masking Policy を使用
8. **クエリ監査：** 定期的に `LOGIN_HISTORY` と `QUERY_HISTORY` を確認

---

### 18.5 コスト管理ベストプラクティス

1. **適切な AUTO_SUSPEND 設定：** 開発 WH 60-120秒、ETL WH 300-600秒
2. **Resource Monitor の使用：** 多段階アラート設定（80%/90%/100%）
3. **適切な Warehouse Size の選択：** X-Small で大量データ処理をしない
4. **Cloud Services コストの監視：** 多数の小クエリは Cloud Services 費用を押し上げる
5. **不要オブジェクトのクリーンアップ：** 古いデータ、一時テーブル、期限切れクローンを定期的に削除

---

### 18.6 よくある問題と解決策

| 問題 | 考えられる原因 | 解決策 |
|------|----------|----------|
| **クエリが遅い** | マイクロパーティションの過剰スキャン | WHERE フィルター追加または Clustering |
| **クエリがキューイング** | Warehouse 同時実行数が上限到達 | MAX_CONCURRENCY_LEVEL 増加または Multi-cluster 使用 |
| **Spilling** | Warehouse が小さすぎて操作を格納不可 | Warehouse Size 拡大 |
| **COPY ロード失敗** | ファイル形式不一致 | VALIDATION_MODE で検証、NULL_IF 設定確認 |
| **データ消失** | テーブル/DB の誤削除 | UNDROP または Time Travel で復旧 |
| **権限不足** | 必要な権限の欠如 | ロールと権限付与を確認 |
| **Snowpipe がロードしない** | Pipe 停止または SNS 通知不調 | PIPE_EXECUTION_PAUSED と SNS 設定を確認 |
| **Stream が空** | Stream 消費済みまたはテーブル変更なし | CHANGE_TRACKING 設定を確認 |
| **結果キャッシュヒットしない** | 基データ変更またはクエリが完全同一でない | USE_CACHED_RESULT 設定を確認 |
| **費用が高すぎる** | Warehouse が自動停止しないまたは大きすぎる | AUTO_SUSPEND 設定と使用パターンを確認 |

---

> **マニュアル終了**
>
> 本マニュアルは Snowflake のコア知識ポイントをカバーしています。アーキテクチャ概要から詳細な DDL/DML 構文、データロードからパフォーマンス最適化、セキュリティ管理からベストプラクティスまで網羅しています。新規メンバーは章順に学習し、Snowflake 環境で各例を実践することを推奨します。
>
> **推奨する後続学習リソース：**
> - [Snowflake 公式ドキュメント](https://docs.snowflake.com/)
> - [Snowflake ハンズオンラボ](https://quickstarts.snowflake.com/)
> - [Snowflake コミュニティ](https://community.snowflake.com/)
