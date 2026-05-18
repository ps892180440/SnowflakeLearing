
# Snowflake 新项目成员培训手册

> **适用版本**: Snowflake Enterprise Edition 及以上
> **手册目的**: 帮助新项目成员从零基础快速掌握 Snowflake 核心技术栈，能够独立完成数据开发与运维工作
> **最后更新**: 2026-05-12

---

## 目录

- [1. Snowflake 概述](#1-snowflake-概述)
  - [1.1 什么是 Snowflake](#11-什么是-snowflake)
  - [1.2 核心架构](#12-核心架构)
  - [1.3 与传统数据库对比](#13-与传统数据库对比)
  - [1.4 支持的云平台与区域](#14-支持的云平台与区域)
- [2. 快速入门](#2-快速入门)
  - [2.1 访问方式概览](#21-访问方式概览)
  - [2.2 Snowsight (Web UI) 操作入门](#22-snowsight-web-ui-操作入门)
  - [2.3 SnowSQL (CLI) 安装与配置](#23-snowsql-cli-安装与配置)
  - [2.4 SDK & Connector 连接](#24-sdk--connector-连接)
- [3. 核心对象层级结构](#3-核心对象层级结构)
  - [3.1 对象层级总览](#31-对象层级总览)
  - [3.2 Database](#32-database-数据库)
  - [3.3 Schema](#33-schema-模式)
  - [3.4 Table](#34-table-表)
  - [3.5 View](#35-view-视图)
  - [3.6 Materialized View](#36-materialized-view-物化视图)
  - [3.7 Stage](#37-stage-暂存区)
  - [3.8 File Format](#38-file-format-文件格式)
  - [3.9 Pipe](#39-pipe-管道)
  - [3.10 Sequence](#310-sequence-序列)
  - [3.11 Stored Procedure](#311-stored-procedure-存储过程)
  - [3.12 UDF / UDTF](#312-udf--udtf-用户自定义函数)
- [4. 数据类型详解](#4-数据类型详解)
  - [4.1 数值类型](#41-数值类型)
  - [4.2 字符串类型](#42-字符串类型)
  - [4.3 日期与时间类型](#43-日期与时间类型)
  - [4.4 半结构化数据类型](#44-半结构化数据类型)
  - [4.5 其他类型](#45-其他类型)
  - [4.6 类型转换](#46-类型转换)
- [5. DDL 完整语法参考](#5-ddl-完整语法参考)
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
- [6. DML 完整语法参考](#6-dml-完整语法参考)
  - [6.1 INSERT](#61-insert)
  - [6.2 UPDATE](#62-update)
  - [6.3 DELETE](#63-delete)
  - [6.4 MERGE](#64-merge)
  - [6.5 COPY INTO <table>](#65-copy-into-table-数据加载)
  - [6.6 COPY INTO <location>](#66-copy-into-location-数据卸载)
- [7. SELECT 查询完整语法](#7-select-查询完整语法)
  - [7.1 SELECT 完整语法](#71-select-完整语法)
  - [7.2 JOIN 类型详解](#72-join-类型详解)
  - [7.3 子查询与 CTE](#73-子查询与-cte)
  - [7.4 窗口函数](#74-窗口函数)
  - [7.5 集合操作](#75-集合操作-union--intersect--except)
  - [7.6 半结构化数据查询](#76-半结构化数据查询-flatten--lateral)
  - [7.7 QUALIFY 子句](#77-qualify-子句)
  - [7.8 SAMPLE / TABLESAMPLE](#78-sample--tablesample-抽样查询)
  - [7.9 PIVOT / UNPIVOT](#79-pivot--unpivot)
  - [7.10 CONNECT BY 层次查询](#710-connect-by-层次查询)
- [8. 数据加载与卸载](#8-数据加载与卸载)
  - [8.1 Stage 详解](#81-stage-暂存区-详解)
  - [8.2 PUT 命令](#82-put-命令上传文件)
  - [8.3 COPY INTO 加载](#83-copy-into-加载数据)
  - [8.4 VALIDATION_MODE](#84-validation_mode-数据校验)
  - [8.5 Snowpipe 自动加载](#85-snowpipe-自动加载)
  - [8.6 数据卸载 (UNLOAD)](#86-数据卸载-unload)
- [9. Time Travel 与 Fail-safe](#9-time-travel-时间旅行-与-fail-safe-故障安全)
  - [9.1 Time Travel 概述](#91-time-travel-概述)
  - [9.2 Time Travel 操作手顺](#92-time-travel-操作手顺)
  - [9.3 UNDROP 恢复对象](#93-undrop-恢复对象)
  - [9.4 Fail-safe 概述](#94-fail-safe-概述)
  - [9.5 数据保留策略配置](#95-数据保留策略配置)
- [10. Zero-Copy Cloning](#10-zero-copy-cloning-零拷贝克隆)
  - [10.1 克隆原理](#101-克隆原理)
  - [10.2 克隆操作手顺](#102-克隆操作手顺)
  - [10.3 克隆权限与注意事项](#103-克隆权限与注意事项)
- [11. Data Sharing](#11-data-sharing-数据共享)
  - [11.1 数据共享架构](#111-数据共享架构)
  - [11.2 创建与配置 Share](#112-创建与配置-share)
  - [11.3 Reader Account](#113-创建-reader-account)
  - [11.4 从 Share 创建数据库](#114-从-share-创建数据库)
  - [11.5 Data Marketplace](#115-data-marketplace)
- [12. 安全与访问控制 (RBAC)](#12-安全与访问控制-rbac)
  - [12.1 RBAC 模型概述](#121-rbac-模型概述)
  - [12.2 用户管理](#122-用户管理操作手顺)
  - [12.3 角色管理](#123-角色管理操作手顺)
  - [12.4 权限授予与撤销](#124-权限授予与撤销)
  - [12.5 网络策略](#125-网络策略)
  - [12.6 数据加密](#126-数据加密)
  - [12.7 Column-Level Security](#127-column-level-security-列级安全)
  - [12.8 Row-Level Security](#128-row-level-security-行级安全)
- [13. Virtual Warehouse 与性能](#13-virtual-warehouse-虚拟仓库-与性能)
  - [13.1 Warehouse 概述](#131-warehouse-概述)
  - [13.2 Warehouse 创建与配置](#132-warehouse-创建与配置)
  - [13.3 Multi-cluster Warehouse](#133-multi-cluster-warehouse)
  - [13.4 查询性能优化](#134-查询性能优化)
  - [13.5 缓存机制](#135-结果缓存--元数据缓存--数据缓存)
  - [13.6 Micro-partition 与 Clustering](#136-micro-partition-与-clustering)
  - [13.7 物化视图加速](#137-物化视图与查询加速)
  - [13.8 Search Optimization Service](#138-search-optimization-service)
  - [13.9 Query Profile](#139-query-profile-性能分析)
- [14. 半结构化数据处理](#14-半结构化数据处理)
  - [14.1 VARIANT 类型操作](#141-variant-类型操作)
  - [14.2 FLATTEN 函数详解](#142-flatten-函数详解)
  - [14.3 PARSE_JSON / PARSE_XML](#143-parse_json--parse_xml)
  - [14.4 加载 JSON / Parquet / Avro / XML](#144-加载-json--parquet--avro--xml-数据)
  - [14.5 半结构化数据优化](#145-半结构化数据优化)
- [15. UDF / UDTF / 存储过程](#15-udf--udtf--存储过程)
  - [15.1 SQL UDF](#151-sql-udf-创建与使用)
  - [15.2 JavaScript UDF](#152-javascript-udf-创建与使用)
  - [15.3 Python UDF](#153-python-udf-创建与使用)
  - [15.4 Java UDF](#154-java-udf-创建与使用)
  - [15.5 UDTF](#155-udtf-表函数-创建与使用)
  - [15.6 JavaScript 存储过程](#156-javascript-存储过程)
  - [15.7 Python 存储过程](#157-python-存储过程)
  - [15.8 SQL 存储过程 (Snowflake Scripting)](#158-sql-存储过程-snowflake-scripting)
- [16. Streams & Tasks](#16-streams--tasks-流与任务)
  - [16.1 Stream 概述与类型](#161-stream-概述与类型)
  - [16.2 Standard Stream](#162-standard-stream-操作手顺)
  - [16.3 Append-Only Stream](#163-append-only-stream-操作手顺)
  - [16.4 Task 创建与管理](#164-task-创建与管理)
  - [16.5 Stream + Task 增量 ETL](#165-stream--task-构建增量-etl-管道)
- [17. 账户与资源管理](#17-账户与资源管理)
  - [17.1 账户结构](#171-账户结构)
  - [17.2 Resource Monitor](#172-resource-monitor-创建与使用)
  - [17.3 信息模式与 Account Usage](#173-信息模式与-account-usage)
  - [17.4 成本管理](#174-成本管理)
- [18. 最佳实践与常见问题](#18-最佳实践与常见问题)
  - [18.1 表设计最佳实践](#181-表设计最佳实践)
  - [18.2 查询优化最佳实践](#182-查询优化最佳实践)
  - [18.3 数据加载最佳实践](#183-数据加载最佳实践)
  - [18.4 安全最佳实践](#184-安全最佳实践)
  - [18.5 成本控制最佳实践](#185-成本控制最佳实践)
  - [18.6 常见问题排查](#186-常见问题排查)

---

## 1. Snowflake 概述

### 1.1 什么是 Snowflake

Snowflake 是一个**完全托管 (Fully Managed)** 的**云原生数据平台**，基于 **SaaS (Software-as-a-Service)** 模式交付。它提供数据仓库、数据湖、数据工程、数据科学、数据共享和市场等解决方案，核心产品是 Snowflake 数据云 (Data Cloud)。

**核心特性：**

| 特性 | 描述 |
|------|------|
| **存算分离** | 存储层和计算层完全独立，可独立扩缩容 |
| **弹性伸缩** | 计算资源（Warehouse）可动态扩缩，支持自动暂停/恢复 |
| **零管理** | 无需管理硬件、操作系统、数据库软件、补丁、索引调配 |
| **多云支持** | 支持 AWS、Azure、GCP 三大云平台 |
| **跨云数据共享** | 不同云平台间的 Snowflake 账户可安全共享数据 |
| **Time Travel** | 数据可回溯到过去某个时间点（最多 90 天） |
| **Zero-Copy Cloning** | 秒级创建完整副本，不占用额外存储 |
| **安全合规** | IP 白名单、MFA、SOC 2 Type II、HIPAA、PCI DSS、FedRAMP |
| **半结构化数据** | 原生支持 JSON、Avro、Parquet、ORC、XML 的查询与优化存储 |

---

### 1.2 核心架构

Snowflake 采用混合架构，包含三个独立的层：

```
+---------------------------------------------------+
|              Cloud Services Layer                  |
|  Authentication  |  Query Optimizer  |  Metadata   |
|  Access Control  |  Infra Manager    |  Tx Mgmt    |
+---------------------------------------------------+
|              Compute Layer                         |
|  Virtual Warehouse (XS / S / M / L / XL / 2XL~6XL)|
|  Serverless Compute                                |
+---------------------------------------------------+
|              Storage Layer                         |
|  Blob Storage (S3 / Azure Blob / GCS)             |
|  列式压缩 | AES-256 加密 | Micro-partitions       |
+---------------------------------------------------+
```

**三层详解：**

| 层 | 职责 | 收费方式 |
|----|------|----------|
| **Cloud Services** | 认证授权、查询解析优化、元数据管理、事务管理 | 按 credits 使用量 |
| **Compute** | Virtual Warehouse 执行查询和 DML | 按 Warehouse 运行时间 (credits/小时) |
| **Storage** | 列式压缩存储所有数据 | 按压缩后数据量 (TB/月) |

**关键理解：**
- 存储和计算完全分离——可以关闭所有 Warehouse 而数据仍然安全存储，此时仅需支付存储费用
- 每个 Warehouse 是独立的计算集群，互不影响（工作负载隔离）
- 查询从存储层读取数据到计算层执行，结果返回后计算层释放内存

---

### 1.3 与传统数据库对比

| 特性 | Snowflake | 传统数据库 (Oracle/PostgreSQL/SQL Server) |
|------|-----------|------------------------------------------|
| 部署方式 | SaaS，无需自行管理 | 需安装、配置、打补丁、维护 |
| 存算 | 完全分离，独立扩缩容 | 耦合在一起 |
| 弹性 | 按需扩缩容，秒级响应 | 固定资源，扩容需停机 |
| 并发 | 多 Warehouse 隔离工作负载 | 共享资源池，资源争抢 |
| 索引 | 无需手动创建索引 | 需手动创建和维护索引 |
| 半结构化 | 原生 VARIANT 类型 | 需 JSON/XML 扩展 |
| 数据共享 | 原生支持，零拷贝 | 需通过 ETL、API 或文件导出 |
| 零拷贝克隆 | 支持，秒级完成 | 不支持 |
| Time Travel | 支持，最多 90 天 | 需自行实现 |
| 自动暂停 | 支持，空闲后自动暂停 | 不支持 |

---

### 1.4 支持的云平台与区域

| 云平台 | 主要可用区域 |
|--------|-------------|
| **AWS** | us-east-1, us-west-2, eu-central-1, eu-west-1, ap-southeast-1, ap-southeast-2, ap-northeast-1, ca-central-1, ap-south-1, sa-east-1, us-east-2, eu-west-2 等 |
| **Azure** | eastus2, westeurope, southeastasia, canadacentral, australiaeast 等 |
| **GCP** | us-central1, europe-west4 等 |

---

## 2. 快速入门

### 2.1 访问方式概览

| 方式 | 适用场景 | 说明 |
|------|----------|------|
| **Snowsight (Web UI)** | 查询开发、管理、可视化 | `https://app.snowflake.com` |
| **Classic Console** | 传统查询开发 | 旧版 Web 界面 |
| **SnowSQL (CLI)** | 脚本、自动化、批处理 | 命令行客户端 |
| **Python Connector** | Python 应用 | `pip install snowflake-connector-python` |
| **JDBC Driver** | Java 应用 | Maven/Gradle 依赖 |
| **ODBC Driver** | .NET / BI 工具 | 系统级驱动 |
| **Node.js Driver** | Node.js 应用 | npm 安装 |
| **Go Driver** | Go 应用 | `go get github.com/snowflakedb/gosnowflake` |
| **Spark Connector** | Apache Spark 集成 | Spark 包 |
| **Kafka Connector** | Kafka 实时流 | Confluent Hub |

---

### 2.2 Snowsight (Web UI) 操作手顺

**步骤 1: 登录**
1. 打开浏览器，访问 `https://app.snowflake.com`
2. 输入账户标识符 (Account Identifier)

   格式：`<orgname>-<account_name>` 或 `<account_locator>.<region>.<cloud>`

   示例：`myorg-myaccount` 或 `xy12345.us-east-1.aws`

3. 输入用户名和密码
4. （如启用 MFA）输入 Duo Mobile / Microsoft Authenticator 验证码

**步骤 2: 创建 SQL Worksheet**
1. 左侧导航栏点击 **Projects** -> **Worksheets**
2. 点击右上角 **+ Worksheet** 按钮
3. 选择 **SQL Worksheet**

**步骤 3: 执行第一个查询**

```sql
-- 查看当前会话上下文
SELECT CURRENT_ROLE();
SELECT CURRENT_WAREHOUSE();
SELECT CURRENT_DATABASE();
SELECT CURRENT_SCHEMA();
SELECT CURRENT_USER();

-- 查看所有可见数据库
SHOW DATABASES;

-- 查看所有 Warehouse
SHOW WAREHOUSES;
```

**步骤 4: 浏览数据库对象**
1. 左侧导航栏点击 **Data** -> **Databases**
2. 展开 `SNOWFLAKE` -> `ACCOUNT_USAGE`，可查看系统共享的账户使用数据
3. 点击任意表名即可预览数据

**步骤 5: 设置会话上下文**

```sql
USE ROLE SYSADMIN;
USE WAREHOUSE COMPUTE_WH;
USE DATABASE MY_DB;
USE SCHEMA MY_SCHEMA;
```

> 也可以在 Worksheet 顶部的上下文选择器中直接选择 Role / Warehouse / Database / Schema。

---

### 2.3 SnowSQL (CLI) 安装与配置

**步骤 1: 下载安装**

| 平台 | 方式 |
|------|------|
| **Windows** | 下载 [SnowSQL MSI Installer](https://developers.snowflake.com/snowsql/) |
| **macOS** | `brew install --cask snowflake-snowsql` |
| **Linux** | `curl -O https://sfc-repo.snowflakecomputing.com/snowsql/bootstrap/1.3/linux_x86_64/snowsql-1.3.3-linux_x86_64.bash` |

**步骤 2: 配置连接**

创建/编辑 `~/.snowsql/config`：

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

**Key Pair 认证（推荐生产环境）：**

```ini
[connections.my_account_keypair]
accountname      = myorg-myaccount
username         = myuser
private_key_path = /path/to/rsa_key.p8
authenticator    = snowflake_jwt
role             = SYSADMIN
warehouse        = COMPUTE_WH
```

**步骤 3: 连接与测试**

```bash
# 使用配置文件连接
snowsql -c my_account

# 直接指定参数
snowsql -a myorg-myaccount -u myuser

# 执行单条 SQL 并退出
snowsql -c my_account -q "SELECT CURRENT_TIMESTAMP();"

# 执行 SQL 文件
snowsql -c my_account -f my_script.sql

# 使用变量
snowsql -c my_account -D table_name=MY_TABLE -q "SELECT COUNT(*) FROM &{table_name};"

# 非交互式输出（适合脚本）
snowsql -c my_account -o friendly=false -o header=false -o timing=false \
    -q "SELECT COUNT(*) FROM my_table;"
```

**步骤 4: SnowSQL 交互式命令**

```
!help                    -- 查看帮助
!options                 -- 查看当前选项
!set variable_name=value -- 设置会话变量
!source file.sql         -- 执行 SQL 文件
!load file.csv           -- 快速加载 CSV 到表
!output file.csv         -- 将结果输出为 CSV 文件
!abort                   -- 取消当前查询
!exit                    -- 退出
```

---

### 2.4 SDK & Connector 连接

#### Python Connector

```bash
pip install snowflake-connector-python[pandas]
```

```python
import snowflake.connector
import pandas as pd

# 基础连接
conn = snowflake.connector.connect(
    user='myuser',
    password='mypassword',
    account='myorg-myaccount',
    warehouse='COMPUTE_WH',
    database='MY_DB',
    schema='MY_SCHEMA',
    role='SYSADMIN'
)

# 查询并获取 DataFrame
cur = conn.cursor()
cur.execute("SELECT * FROM my_table LIMIT 10")
df = cur.fetch_pandas_all()
print(df)

# 使用 with 语句（自动管理事务）
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

## 3. 核心对象层级结构

### 3.1 对象层级总览

```
Organization (组织)
 +-- Account (账户)
      +-- User (用户)
      +-- Role (角色)
      +-- Warehouse (虚拟仓库)
      +-- Resource Monitor (资源监控器)
      +-- Database (数据库)
      |    +-- Schema (模式)
      |         +-- Table (表)
      |         +-- View (视图)
      |         +-- Materialized View (物化视图)
      |         +-- Stage (暂存区)
      |         +-- File Format (文件格式)
      |         +-- Pipe (管道)
      |         +-- Sequence (序列)
      |         +-- Stream (流)
      |         +-- Task (任务)
      |         +-- Stored Procedure (存储过程)
      |         +-- Function / UDF / UDTF (函数)
      |         +-- External Function (外部函数)
      |         +-- Masking Policy (脱敏策略)
      |         +-- Row Access Policy (行级访问策略)
      |         +-- Tag (标签)
      +-- Share (共享)
      +-- Integration (集成: Storage / Notification / API)
      +-- Network Policy (网络策略)
```

**Fully Qualified Name (完全限定名称)：**

```sql
-- 格式
<database>.<schema>.<object>

-- 示例
MY_DB.PUBLIC.EMPLOYEES
MY_DB.HR.VW_ACTIVE_STAFF
```

---

### 3.2 Database (数据库)

数据库是最顶层的命名空间容器。

```sql
-- === 创建数据库 ===

-- 最简单的创建
CREATE DATABASE my_database;

-- 带所有参数的创建
CREATE OR REPLACE DATABASE my_database
    COMMENT = 'Training database for new hires'
    DATA_RETENTION_TIME_IN_DAYS = 7
    MAX_DATA_EXTENSION_TIME_IN_DAYS = 14
    DEFAULT_DDL_COLLATION = 'en_US';

-- 从 Share 创建（接收共享数据）
CREATE DATABASE shared_db FROM SHARE provider_account.share_name;

-- 克隆数据库（零拷贝）
CREATE DATABASE my_database_clone CLONE my_database;

-- 克隆到历史时间点
CREATE DATABASE my_database_restore CLONE my_database
    AT (TIMESTAMP => '2026-05-10 12:00:00'::TIMESTAMP);

-- === 查看数据库 ===
SHOW DATABASES;
SHOW DATABASES LIKE 'my_database';
DESC DATABASE my_database;

-- === 切换数据库 ===
USE DATABASE my_database;
SELECT CURRENT_DATABASE();

-- === 修改数据库 ===
ALTER DATABASE my_database SET DATA_RETENTION_TIME_IN_DAYS = 14;
ALTER DATABASE my_database RENAME TO my_database_v2;

-- === 删除与恢复 ===
DROP DATABASE my_database;
UNDROP DATABASE my_database;  -- Time Travel 范围内可恢复
```

---

### 3.3 Schema (模式)

```sql
-- 创建
CREATE SCHEMA my_database.hr_schema
    COMMENT = 'HR department schema';

-- Managed Access Schema（权限集中管控）
CREATE SCHEMA my_database.managed_hr WITH MANAGED ACCESS;

-- 克隆
CREATE SCHEMA my_database.hr_clone CLONE my_database.hr_schema;

-- 查看
SHOW SCHEMAS IN DATABASE my_database;
DESC SCHEMA my_database.hr_schema;

-- 切换
USE SCHEMA my_database.hr_schema;
SELECT CURRENT_SCHEMA();

-- 修改
ALTER SCHEMA my_database.hr_schema
    SET DATA_RETENTION_TIME_IN_DAYS = 14;

-- 删除/恢复
DROP SCHEMA my_database.hr_schema;
UNDROP SCHEMA my_database.hr_schema;
```

---

### 3.4 Table (表)

**表类型对比：**

| 类型 | Time Travel | Fail-safe | 生命周期 |
|------|-------------|-----------|----------|
| **Permanent (永久表)** | 0-90天(可配置) | 7天 | 手动删除 |
| **Temporary (临时表)** | 无 | 无 | 会话结束自动删除 |
| **Transient (瞬态表)** | 1天(固定) | 无 | 手动删除 |
| **External (外部表)** | N/A | N/A | N/A |

```sql
-- 永久表（默认类型）
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

-- 临时表（会话结束自动删除）
CREATE TEMPORARY TABLE temp_calc_results (
    id    INTEGER,
    value NUMBER
);

-- 瞬态表（无 Fail-safe 保护，有 1 天 Time Travel）
CREATE TRANSIENT TABLE staging_raw_data (
    id       INTEGER,
    raw_json VARIANT,
    load_ts  TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

-- CTAS (CREATE TABLE AS SELECT)
CREATE TABLE high_salary_employees AS
SELECT * FROM employees WHERE salary > 100000;

-- CREATE TABLE LIKE（仅复制列结构，不含数据）
CREATE TABLE employees_backup LIKE employees;

-- 从 Stage 文件推断 Schema 自动建表
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

### 3.5 View (视图)

```sql
-- 标准视图
CREATE VIEW vw_active_employees AS
SELECT employee_id, first_name, last_name, email, department_id
FROM employees WHERE is_active = TRUE;

-- 安全视图（隐藏定义细节，适合数据共享场景）
CREATE SECURE VIEW vw_salary_summary AS
SELECT department_id,
       COUNT(*) AS emp_count,
       AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id;

-- 修改视图
CREATE OR REPLACE VIEW vw_active_employees AS
SELECT employee_id, first_name, last_name, email, department_id, hire_date
FROM employees WHERE is_active = TRUE;

-- 查看视图定义
SELECT GET_DDL('VIEW', 'MY_DB.HR_SCHEMA.VW_ACTIVE_EMPLOYEES');

-- 删除
DROP VIEW vw_active_employees;
```

---

### 3.6 Materialized View (物化视图)

物化视图**预计算并存储查询结果**。基表数据变更时，后台自动增量刷新（由 Snowflake 管理，费用含在 Cloud Services 中）。

**限制：** 仅支持聚合函数 + GROUP BY，不支持 JOIN、窗口函数、UNION、子查询。

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

### 3.7 Stage (暂存区)

| 类型 | 作用域 | 引用方式 | 用途 |
|------|--------|----------|------|
| **User Stage** | 用户 | `@~` | 用户私有文件暂存 |
| **Table Stage** | 表 | `@%table_name` | 单表数据文件暂存 |
| **Internal Named Stage** | Schema | `@stage_name` | Schema 内共享暂存 |
| **External Stage** | Schema | `@ext_stage` | 指向外部云存储 (S3/Azure/GCS) |

```sql
-- 创建内部 Stage（开启目录表功能）
CREATE STAGE my_internal_stage
    ENCRYPTION = (TYPE = 'SNOWFLAKE_SSE')
    DIRECTORY = (ENABLE = TRUE)
    COMMENT = 'Internal stage for ETL files';

-- 创建外部 Stage (AWS S3)
CREATE STAGE my_s3_stage
    URL = 's3://my-bucket/data/'
    STORAGE_INTEGRATION = my_storage_integration
    FILE_FORMAT = my_csv_format;

-- 查看 Stage 中的文件
LIST @my_internal_stage;
LIST @my_s3_stage PATTERN='.*\\.csv$';

-- 从 Stage 删除文件
REMOVE @my_internal_stage/old_data.csv;

-- 删除 Stage
DROP STAGE my_internal_stage;
```

---

### 3.8 File Format (文件格式)

定义数据文件的格式规范，供 COPY INTO 使用。

```sql
-- CSV 格式
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

-- JSON 格式
CREATE FILE FORMAT my_json_format
    TYPE = 'JSON'
    COMPRESSION = 'AUTO'
    STRIP_OUTER_ARRAY = TRUE
    ENABLE_OCTAL = FALSE
    ALLOW_DUPLICATE = FALSE;

-- Parquet 格式
CREATE FILE FORMAT my_parquet_format
    TYPE = 'PARQUET'
    COMPRESSION = 'AUTO'
    BINARY_AS_TEXT = TRUE;

-- Avro 格式
CREATE FILE FORMAT my_avro_format
    TYPE = 'AVRO'
    COMPRESSION = 'AUTO';

-- ORC 格式
CREATE FILE FORMAT my_orc_format
    TYPE = 'ORC';

-- 查看与删除
SHOW FILE FORMATS IN SCHEMA MY_DB.PUBLIC;
DESC FILE FORMAT my_csv_format;
DROP FILE FORMAT my_csv_format;
```

---

### 3.9 Pipe (管道)

Pipe 用于 Snowpipe 自动连续数据加载。

```sql
-- 创建 Pipe (AWS S3 示例)
CREATE PIPE my_snowpipe
    AUTO_INGEST = TRUE
    AWS_SNS_TOPIC = 'arn:aws:sns:us-east-1:1234567890:snowpipe-topic'
    AS
    COPY INTO employees
    FROM @my_s3_stage/employees/
    FILE_FORMAT = (FORMAT_NAME = my_csv_format)
    ON_ERROR = 'SKIP_FILE';

-- 查看 Pipe
SHOW PIPES;
SELECT SYSTEM$PIPE_STATUS('my_snowpipe');

-- 暂停/恢复 Pipe
ALTER PIPE my_snowpipe SET PIPE_EXECUTION_PAUSED = TRUE;
ALTER PIPE my_snowpipe SET PIPE_EXECUTION_PAUSED = FALSE;

-- 刷新 Pipe（手动加载已存在的文件）
ALTER PIPE my_snowpipe REFRESH;

-- 查看 Pipe 加载历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.PIPE_USAGE_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    PIPE_NAME => 'my_snowpipe'
));

-- 删除
DROP PIPE my_snowpipe;
```

---

### 3.10 Sequence (序列)

```sql
-- 创建
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

-- 作为列默认值
CREATE TABLE orders (
    order_id   INTEGER DEFAULT employee_id_seq.NEXTVAL,
    product    VARCHAR(100),
    order_date DATE DEFAULT CURRENT_DATE()
);

-- 查看
SHOW SEQUENCES;
DESC SEQUENCE employee_id_seq;

-- 修改
ALTER SEQUENCE employee_id_seq SET INCREMENT BY 10;

-- 删除
DROP SEQUENCE employee_id_seq;
```

> **注意：** Snowflake 序列**不保证无间隙**（大型多节点 Warehouse 中可能跳号）。如果业务需要严格连续编号，应在应用层实现。

---

### 3.11 Stored Procedure (存储过程)

```sql
-- SQL 存储过程（Snowflake Scripting）
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

-- 调用
CALL calculate_bonus(1001, 0.1);
```

---

### 3.12 UDF / UDTF (用户自定义函数)

```sql
-- SQL UDF (标量函数)
CREATE FUNCTION celsius_to_fahrenheit(celsius FLOAT)
    RETURNS FLOAT
AS $$
    celsius * 9/5 + 32
$$;

SELECT celsius_to_fahrenheit(37);  -- 98.6

-- SQL UDTF (表函数)
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

## 4. 数据类型详解

### 4.1 数值类型

| 类型 | 描述 | 范围/精度 |
|------|------|-----------|
| **NUMBER / DECIMAL / NUMERIC** | 固定精度数值 | P: 1-38, S: 0-P |
| **INT / INTEGER / BIGINT / SMALLINT** | 整数 (NUMBER(38,0) 别名) | -10^38+1 ~ 10^38-1 |
| **TINYINT** | 微小整数 | 0 ~ 255 |
| **FLOAT / FLOAT4 / FLOAT8** | 浮点数 | ~15-17 位有效数字 |
| **DOUBLE / DOUBLE PRECISION / REAL** | 双精度浮点 | ~15-17 位有效数字 |

```sql
CREATE TABLE numeric_examples (
    col_num   NUMBER,           -- NUMBER(38, 0)
    col_dec   NUMBER(10, 2),    -- 精度10，小数2位
    col_int   INTEGER,          -- NUMBER(38, 0)
    col_float FLOAT,
    col_auto  INTEGER AUTOINCREMENT
);
```

### 4.2 字符串类型

| 类型 | 最大长度 |
|------|----------|
| **VARCHAR / STRING / TEXT / NVARCHAR / NVARCHAR2** | 16,777,216 bytes (16 MB) |
| **CHAR / CHARACTER / NCHAR** | 16,777,216 bytes (16 MB) |
| **BINARY / VARBINARY** | 8,388,608 bytes (8 MB) |

```sql
CREATE TABLE string_examples (
    col_vc  VARCHAR(100),
    col_vc2 VARCHAR,          -- 默认 16MB 上限
    col_ch  CHAR(10),         -- 定长 10，不足补空格
    col_txt TEXT,
    col_bin BINARY(100)
);
```

### 4.3 日期与时间类型

| 类型 | 描述 | 示例 |
|------|------|------|
| **DATE** | 日期 (天精度) | `'2026-05-12'` |
| **TIME(n)** | 时间 (n=0~9 纳秒精度) | `'14:30:00.123456789'` |
| **TIMESTAMP_NTZ** | 无时区时间戳 | `'2026-05-12 14:30:00'` |
| **TIMESTAMP_LTZ** | 本地时区输入->UTC存储 | **推荐跨时区场景** |
| **TIMESTAMP_TZ** | 保留原始时区偏移 | 需保留来源时区场景 |

```sql
-- 常用日期函数
SELECT CURRENT_DATE();                    -- 当前日期
SELECT CURRENT_TIME();                    -- 当前时间
SELECT CURRENT_TIMESTAMP();               -- 当前时间戳
SELECT SYSDATE();                        -- 查询开始时间
SELECT DATEADD(day, 7, '2026-05-12');    -- 加7天
SELECT DATEDIFF(day, '2026-01-01', CURRENT_DATE());
SELECT DATE_TRUNC('MONTH', CURRENT_DATE());  -- 当月第一天
SELECT EXTRACT(YEAR FROM CURRENT_DATE());    -- 提取年份
SELECT LAST_DAY(CURRENT_DATE());             -- 当月最后一天
SELECT TO_DATE('2026-05-12', 'YYYY-MM-DD');
SELECT TO_TIMESTAMP('2026-05-12 14:30:00', 'YYYY-MM-DD HH24:MI:SS');
```

### 4.4 半结构化数据类型

| 类型 | 描述 | 最大 |
|------|------|------|
| **VARIANT** | 通用半结构化 (JSON/Avro/Parquet/ORC) | 压缩后 16 MB |
| **OBJECT** | 键值对 (类似 JSON 对象) | 压缩后 16 MB |
| **ARRAY** | 有序值列表 (类似 JSON 数组) | 压缩后 16 MB |
| **GEOGRAPHY** | 地理空间数据 (WGS84) | N/A |

```sql
SELECT PARSE_JSON('{"name":"John","age":30,"skills":["SQL","Python"]}');
SELECT OBJECT_CONSTRUCT('name', 'John', 'age', 30);
SELECT ARRAY_CONSTRUCT('a', 'b', 'c');
SELECT ARRAY_SIZE(['a', 'b', 'c']);  -- 3
```

### 4.5 其他类型

| 类型 | 描述 |
|------|------|
| **BOOLEAN** | TRUE / FALSE / NULL |

### 4.6 类型转换

```sql
-- CAST / :: 显式转换
SELECT CAST('123' AS INTEGER);         -- 123
SELECT '3.14159'::FLOAT;               -- 3.14159
SELECT '2026-05-12'::DATE;

-- 安全转换（失败返回 NULL）
SELECT TRY_CAST('abc' AS INTEGER);     -- NULL
SELECT TRY_TO_NUMBER('$1,234.56', '$999,999.99');

-- 格式化
SELECT TO_CHAR(1234567.89, '$9,999,999.99');   -- '$1,234,567.89'
SELECT TO_NUMBER('$1,234.56', '$999,999.99');   -- 1234.56
SELECT TO_BOOLEAN('true'), TO_BOOLEAN(1);        -- TRUE, TRUE
```

---


## 5. DDL 完整语法参考

本章提供 Snowflake 核心 DDL 语句的**完整官方语法**，每个入参均有详细说明。

---

### 5.1 CREATE DATABASE

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `OR REPLACE` | 如果同名数据库已存在，先用 DROP 替换（需有 DROP 权限）。旧数据库进入 Time Travel 状态 |
| `IF NOT EXISTS` | 如果同名数据库已存在，不报错，静默跳过 |
| `<name>` | 数据库名称。必须满足 Snowflake 标识符规则：以字母开头，可含字母/数字/下划线，最大 255 字符 |
| `CLONE <source>` | 零拷贝克隆指定数据库。不复制实际数据，仅复制元数据指针 |
| `AT (TIMESTAMP => ...)` | 克隆到指定时间点（精确到秒）。格式：`'2026-05-10 12:00:00'::TIMESTAMP` |
| `AT (OFFSET => ...)` | 克隆到指定秒数之前的时间点。例如 `OFFSET => 3600` 表示 1 小时前 |
| `AT (STATEMENT => ...)` | 克隆到指定查询执行之前的状态。`<query_id>` 可从 Query History 获取 |
| `BEFORE (...)` | 功能同 AT，但在指定时间点/查询之前 |
| `FROM SHARE <provider>.<share>` | 从数据共享创建数据库。provider_account 是提供方账户名，share_name 是 Share 名称 |
| `DATA_RETENTION_TIME_IN_DAYS` | Time Travel 保留天数。范围 0-90 (Enterprise Edition)。设为 0 表示禁用 Time Travel。默认继承账户级别设置 |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | 存储扩展天数。如果账户到期或数据库被删除，数据在此额外天数内仍可恢复。最大 90 天 |
| `DEFAULT_DDL_COLLATION` | 默认排序规则。例如 `'en_US'`、`'utf8'` 等。影响字符串比较和排序行为 |
| `COMMENT` | 数据库注释/描述。最大 255 字符 |

#### 操作示例

```sql
-- 基础创建
CREATE DATABASE training_db;

-- 完整配置
CREATE OR REPLACE DATABASE training_db
    DATA_RETENTION_TIME_IN_DAYS = 14
    MAX_DATA_EXTENSION_TIME_IN_DAYS = 30
    DEFAULT_DDL_COLLATION = 'en_US'
    COMMENT = 'Training database for new team members';

-- 克隆当前状态
CREATE DATABASE training_db_dev CLONE training_db;

-- 克隆到1小时前的状态
CREATE DATABASE training_db_restore CLONE training_db
    AT (OFFSET => -3600);

-- 克隆到指定时间点
CREATE DATABASE training_db_timepoint CLONE training_db
    AT (TIMESTAMP => '2026-05-11 08:00:00'::TIMESTAMP);

-- 从共享创建
CREATE DATABASE shared_partner_data FROM SHARE partner_acct.market_data_share;
```

---

### 5.2 ALTER DATABASE

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `IF EXISTS` | 数据库不存在时不报错 |
| `RENAME TO <new_name>` | 重命名数据库。注意：不能在事务中执行 |
| `SWAP WITH <target>` | 交换两个数据库的名称。原子操作，无数据复制 |
| `SET DATA_RETENTION_TIME_IN_DAYS` | 修改 Time Travel 保留天数 |
| `SET MAX_DATA_EXTENSION_TIME_IN_DAYS` | 修改存储扩展天数 |
| `SET DEFAULT_DDL_COLLATION` | 修改默认排序规则 |
| `UNSET` | 恢复参数为账户默认值 |
| `ENABLE REPLICATION` | 启用数据库复制到指定账户 |
| `DISABLE REPLICATION` | 禁用数据库复制 |

#### 操作示例

```sql
ALTER DATABASE training_db RENAME TO training_db_v2;
ALTER DATABASE training_db SWAP WITH training_db_old;
ALTER DATABASE training_db SET DATA_RETENTION_TIME_IN_DAYS = 30;
ALTER DATABASE training_db SET COMMENT = 'Updated training database';
ALTER DATABASE training_db UNSET DATA_RETENTION_TIME_IN_DAYS;  -- 恢复默认
```

---

### 5.3 DROP DATABASE

#### 完整语法

```sql
DROP DATABASE [ IF EXISTS ] <name> [ CASCADE | RESTRICT ]
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |
| `CASCADE` | 级联删除数据库下所有 Schema 和对象 |
| `RESTRICT` | (默认) 如果数据库非空（包含 Schema），拒绝删除并报错 |

#### 操作示例

```sql
DROP DATABASE training_db;
DROP DATABASE IF EXISTS training_db;
DROP DATABASE training_db CASCADE;       -- 强制删除所有子对象
UNDROP DATABASE training_db;             -- 恢复已删除的数据库
```

---

### 5.4 CREATE SCHEMA

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `OR REPLACE` | 替换已存在的同名 Schema，旧 Schema 进入 Time Travel |
| `IF NOT EXISTS` | 存在时不报错 |
| `[<database>.]<name>` | Schema 名称。可指定数据库前缀。如 `my_db.hr_schema` |
| `CLONE` | 零拷贝克隆指定 Schema |
| `WITH MANAGED ACCESS` | 托管访问模式：仅 Schema 所有者可授予对象权限，简化权限管理 |
| `DATA_RETENTION_TIME_IN_DAYS` | Time Travel 保留天数，覆盖数据库级别设置 |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | 存储扩展天数 |
| `DEFAULT_DDL_COLLATION` | 默认排序规则 |

#### 操作示例

```sql
CREATE SCHEMA training_db.hr_schema;
CREATE SCHEMA hr_schema;  -- 在当前数据库中创建

CREATE SCHEMA training_db.hr_dev CLONE training_db.hr_schema;
CREATE SCHEMA training_db.managed_schema WITH MANAGED ACCESS;
```

---

### 5.5 CREATE TABLE

#### 完整语法

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

#### 参数说明

**表类型参数：**

| 参数 | 说明 |
|------|------|
| `TEMPORARY / TEMP` | 临时表。会话内可见，会话结束自动删除。无 Time Travel / Fail-safe。不能克隆 |
| `TRANSIENT` | 瞬态表。持久存在但无 Fail-safe 保护 (仅 1 天 Time Travel)。存储成本比永久表低 |
| `VOLATILE` | 同 TRANSIENT（已弃用别名，建议用 TRANSIENT） |
| (无类型指定) | 永久表。享受完整的 Time Travel + Fail-safe 保护 |

**列定义参数：**

| 参数 | 说明 |
|------|------|
| `<column_name>` | 列名。必须以字母开头，可含字母/数字/下划线 |
| `<column_type>` | 数据类型（参见第 4 章） |
| `COLLATE` | 列的排序规则。覆盖数据库/Schema 级别设置 |
| `DEFAULT <expr>` | 默认值表达式。如 `CURRENT_DATE()`、`0`、`'N/A'` |
| `AUTOINCREMENT / IDENTITY` | 自增列。`<start>` 起始值（默认 1），`<increment>` 步长（默认 1） |
| `ORDER / NOORDER` | IDENTITY 列行为。`ORDER` 尽量保证递增，`NOORDER` 不保证。默认 `NOORDER` |
| `NOT NULL` | 非空约束 |
| `NULL` | 允许空值（默认） |
| `PRIMARY KEY` | 主键约束。**文档目的**，不强制唯一性（除非启用 RELY） |
| `UNIQUE` | 唯一约束。**文档目的**，不强制唯一性 |
| `REFERENCES <table>` | 外键约束。**文档目的**，不强制参照完整性 |
| `WITH MASKING POLICY` | 列级数据脱敏策略。详见 [12.7](#127-column-level-security-列级安全) |
| `WITH TAG` | 列级标签。用于治理和跟踪 |

**表级参数：**

| 参数 | 说明 |
|------|------|
| `STAGE_FILE_FORMAT` | 表 Stage 的默认文件格式。影响 COPY INTO 到此表时的行为 |
| `STAGE_COPY_OPTIONS` | 表 Stage 的 COPY 选项 |
| `DATA_RETENTION_TIME_IN_DAYS` | 表级别 Time Travel 保留天数。覆盖数据库/Schema 设置 |
| `MAX_DATA_EXTENSION_TIME_IN_DAYS` | 表级别存储扩展天数 |
| `CHANGE_TRACKING` | 变更跟踪。为 TRUE 时 Stream 可跟踪该表的所有变更 |
| `DEFAULT_DDL_COLLATION` | 表级默认排序规则 |
| `COPY GRANTS` | 使用 CTAS/LIKE 创建时，复制源表的权限授予 |
| `CLUSTER BY (<expr>)` | 聚类键。按指定表达式重新组织 Micro-partition 布局，提升查询性能 |
| `ENABLE_SCHEMA_EVOLUTION` | 启用 Schema 演化。自动根据数据文件新增列，适用于自动加载场景 |
| `WITH TAG` | 表级标签 |

#### 操作示例

```sql
-- 标准永久表
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

-- 集群表
CREATE TABLE sales_fact (
    sale_date  DATE,
    product_id INTEGER,
    region     VARCHAR(50),
    amount     NUMBER(12, 2)
) CLUSTER BY (sale_date, region);

-- CTAS
CREATE TABLE high_value_employees AS
SELECT * FROM employees WHERE salary > 150000;

-- CTAS 带列定义
CREATE TABLE employee_summary (dept_id, avg_salary, headcount)
    COMMENT = 'Department-level summary'
AS
SELECT department_id, AVG(salary), COUNT(*)
FROM employees GROUP BY department_id;

-- LIKE 结构
CREATE TABLE employees_staging LIKE employees;

-- 从 Stage 自动推断 Schema
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

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `RENAME TO` | 重命名表 |
| `SWAP WITH` | 原子交换两表名称 |
| `ALTER [COLUMN]` | 修改列属性 |
| `DROP DEFAULT` | 删除列的默认值 |
| `SET DEFAULT <expr>` | 设置列默认值 |
| `SET [NOT] NULL` | 修改可为空/非空约束 |
| `SET DATA TYPE <type>` | 修改列数据类型。**限制：** 仅可将 VARCHAR 扩展长度或将 NUMBER 扩展精度/标度 |
| `ADD [COLUMN]` | 新增列。新增列在已有行中为 NULL 或默认值 |
| `RENAME COLUMN` | 重命名列 |
| `DROP [COLUMN]` | 删除列。`CASCADE` 级联删除依赖对象，`RESTRICT` 有依赖时报错 |
| `SET CLUSTER BY` | 设置/修改聚类键 |
| `DROP CLUSTERING KEY` | 删除聚类键 |
| `ENABLE/DISABLE SCHEMA_EVOLUTION` | 启用/禁用自动 Schema 演化 |

#### 操作示例

```sql
-- 重命名
ALTER TABLE employees RENAME TO staff;

-- 交换
ALTER TABLE employees SWAP WITH employees_backup;

-- 添加列
ALTER TABLE employees ADD COLUMN phone VARCHAR(20);
ALTER TABLE employees ADD COLUMN address VARCHAR(200) DEFAULT 'Unknown';

-- 修改列
ALTER TABLE employees ALTER COLUMN phone SET DATA TYPE VARCHAR(30);
ALTER TABLE employees ALTER COLUMN phone DROP DEFAULT;
ALTER TABLE employees ALTER COLUMN email SET NOT NULL;

-- 重命名列
ALTER TABLE employees RENAME COLUMN phone TO contact_phone;

-- 删除列
ALTER TABLE employees DROP COLUMN address;

-- 设置聚类键
ALTER TABLE employees SET CLUSTER BY (department_id, hire_date);

-- 删除聚类键
ALTER TABLE employees DROP CLUSTERING KEY;

-- 修改 Time Travel
ALTER TABLE employees SET DATA_RETENTION_TIME_IN_DAYS = 30;

-- 启用变更跟踪
ALTER TABLE employees SET CHANGE_TRACKING = TRUE;
```

---

### 5.7 DROP TABLE

#### 完整语法

```sql
DROP TABLE [ IF EXISTS ] <table_name> [ CASCADE | RESTRICT ]
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |
| `CASCADE` | 级联删除依赖对象（如视图、Stream） |
| `RESTRICT` | (默认) 有依赖对象时报错拒绝删除 |

#### 操作示例

```sql
DROP TABLE employees;
DROP TABLE IF EXISTS employees CASCADE;
UNDROP TABLE employees;  -- Time Travel 范围内恢复
```

---

### 5.8 TRUNCATE TABLE

#### 完整语法

```sql
TRUNCATE [ TABLE ] [ IF EXISTS ] <table_name>
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `IF EXISTS` | 不存在时不报错 |

> **注意：** TRUNCATE 删除表中所有行，但保留表结构和权限。操作是**元数据操作**（非逐行删除），非常快速。TRUNCATE 的变更可被 Stream 捕获。

#### 操作示例

```sql
TRUNCATE TABLE staging_data;
TRUNCATE TABLE IF EXISTS temp_logs;
```

---

### 5.9 CREATE VIEW

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `OR REPLACE` | 替换已存在的同名视图 |
| `SECURE` | 创建安全视图。隐藏视图定义和内部细节。适用于数据共享场景，防止接收方通过 SHOW VIEWS / GET_DDL 查看定义 |
| `RECURSIVE` | 使用递归 CTE 的视图。依赖自身以支持层次查询 |
| `IF NOT EXISTS` | 已存在时不报错 |
| `<column_list>` | 为视图列指定名称。数量和顺序需与 SELECT 列一致 |
| `COPY GRANTS` | 使用 CREATE OR REPLACE 时，保留旧视图的权限授予 |
| `COMMENT` | 视图注释 |

#### 操作示例

```sql
-- 标准视图
CREATE VIEW vw_active_staff AS
SELECT employee_id, first_name, last_name, email, department_id
FROM employees WHERE is_active = TRUE;

-- 带列别名
CREATE VIEW vw_salary_stats(dept, total_payroll, headcount) AS
SELECT department_id, SUM(salary), COUNT(*) FROM employees GROUP BY department_id;

-- 安全视图（用于数据共享）
CREATE SECURE VIEW vw_public_employee AS
SELECT first_name, last_name, department_id FROM employees;

-- 递归视图（组织架构树）
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

#### 完整语法

```sql
CREATE [ OR REPLACE ]
    [ SECURE ]
    MATERIALIZED VIEW [ IF NOT EXISTS ] <mv_name>
    [ COPY GRANTS ]
    [ COMMENT = '<string>' ]
    AS <select_statement>
```

> **select_statement 限制：** 仅支持聚合函数 (SUM, COUNT, AVG, MIN, MAX, STDDEV 等) 和 GROUP BY。不支持 JOIN、窗口函数、UNION、子查询（WHERE 子句中除外）、HAVING、ORDER BY、LIMIT。只能查询单张基表。

#### 操作示例

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

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `URL` | 外部云存储 URL。如 `'s3://my-bucket/data/'`、`'azure://myaccount.blob.core.windows.net/container/'`、`'gcs://my-bucket/'`。内部 Stage 不指定 |
| `STORAGE_INTEGRATION` | 存储集成对象名称，用于安全访问外部云存储（推荐，避免使用明文凭据） |
| `CREDENTIALS` | 明文云存储凭据。不推荐生产环境使用，优先用 STORAGE_INTEGRATION |
| `ENCRYPTION` | 文件加密选项。`SNOWFLAKE_SSE` 默认服务端加密。`AWS_CSE`/`AZURE_CSE` 客户端加密 |
| `FILE_FORMAT` | 引用已创建的文件格式，或内联定义 |
| `COPY_OPTIONS` | COPY INTO 操作的默认选项 |
| `DIRECTORY` | 启用目录表功能。启用后可通过查询 `DIRECTORY(@stage)` 查看 Stage 文件列表 |

#### 操作示例

```sql
-- 内部 Stage
CREATE STAGE my_internal_stage;

-- 外部 Stage (AWS S3 + Storage Integration)
CREATE STAGE my_s3_stage
    URL = 's3://my-data-lake/incoming/'
    STORAGE_INTEGRATION = my_s3_integration
    FILE_FORMAT = my_csv_ff;

-- 带目录表的内部 Stage
CREATE STAGE my_dir_stage
    DIRECTORY = (ENABLE = TRUE REFRESH_ON_CREATE = TRUE);

-- 查询目录表
SELECT * FROM DIRECTORY(@my_dir_stage);
```

---

### 5.12 CREATE FILE FORMAT

#### 完整语法

```sql
CREATE [ OR REPLACE ] FILE FORMAT [ IF NOT EXISTS ] <name>
    TYPE = { CSV | JSON | AVRO | ORC | PARQUET | XML }
    [ formatTypeOptions ]
    [ COMMENT = '<string>' ]
```

#### CSV 选项

| 参数 | 可选值 | 默认值 | 说明 |
|------|--------|--------|------|
| `COMPRESSION` | AUTO / GZIP / BZ2 / BROTLI / ZSTD / DEFLATE / RAW_DEFLATE / NONE | AUTO | 文件压缩类型 |
| `RECORD_DELIMITER` | 字符串 (如 `\n`, `\r\n`) | `\n` (Unix) / `\r\n` (Windows) | 行分隔符 |
| `FIELD_DELIMITER` | 字符串 | `,` | 字段分隔符 |
| `FILE_EXTENSION` | 字符串 | (由压缩决定) | 文件扩展名，用于匹配 Stage 文件 |
| `SKIP_HEADER` | 整数 | 0 | 跳过文件开头行数 |
| `SKIP_BLANK_LINES` | TRUE / FALSE | FALSE | 是否跳过空行 |
| `DATE_FORMAT` | 格式字符串或 AUTO | AUTO | 日期格式，如 `'YYYY-MM-DD'` |
| `TIME_FORMAT` | 格式字符串或 AUTO | AUTO | 时间格式 |
| `TIMESTAMP_FORMAT` | 格式字符串或 AUTO | AUTO | 时间戳格式 |
| `BINARY_FORMAT` | HEX / BASE64 / UTF8 | HEX | 二进制数据格式 |
| `ESCAPE` | 字符 | NONE | 转义字符 |
| `ESCAPE_UNENCLOSED_FIELD` | 字符 | `\` | 非引号包围字段的转义字符 |
| `TRIM_SPACE` | TRUE / FALSE | FALSE | 是否去除字段首尾空格 |
| `FIELD_OPTIONALLY_ENCLOSED_BY` | 字符 | NONE | 字段可选的包围字符，如 `"` |
| `NULL_IF` | 字符串列表 | `('\\N', '', 'NULL', 'NUL')` | 视为 NULL 的值列表 |
| `EMPTY_FIELD_AS_NULL` | TRUE / FALSE | TRUE | 空字段是否视为 NULL |
| `ERROR_ON_COLUMN_COUNT_MISMATCH` | TRUE / FALSE | TRUE | 列数不匹配时是否报错 |
| `REPLACE_INVALID_CHARACTERS` | TRUE / FALSE | FALSE | 是否用 Unicode 替代字符替换无效 UTF-8 |
| `VALIDATE_UTF8` | TRUE / FALSE | TRUE | 是否验证 UTF-8 编码 |
| `ENCODING` | UTF8 / ISO-8859-1 等 | UTF8 | 字符编码 |

#### JSON 选项

| 参数 | 说明 |
|------|------|
| `COMPRESSION` | 同 CSV |
| `DATE_FORMAT` | 同 CSV |
| `TIME_FORMAT` | 同 CSV |
| `TIMESTAMP_FORMAT` | 同 CSV |
| `BINARY_FORMAT` | 同 CSV |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |
| `STRIP_OUTER_ARRAY` | TRUE 时剥离最外层数组，将数组元素作为独立行加载 |
| `ENABLE_OCTAL` | 是否允许八进制数字表示 |
| `ALLOW_DUPLICATE` | 是否允许 JSON 对象中的重复键 |
| `STRIP_NULL_VALUES` | 是否从 JSON 对象中移除值为 null 的键 |
| `IGNORE_UTF8_ERRORS` | 是否忽略 UTF-8 编码错误 |
| `SKIP_BYTE_ORDER_MARK` | 是否跳过 BOM 标记 |

#### Parquet 选项

| 参数 | 说明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `BINARY_AS_TEXT` | TRUE 时将二进制列解释为 UTF-8 文本 |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |

#### Avro 选项

| 参数 | 说明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `TRIM_SPACE` | 同 CSV |
| `NULL_IF` | 同 CSV |
| `VALIDATE_UTF8` | 同 CSV |

#### XML 选项

| 参数 | 说明 |
|------|------|
| `COMPRESSION` | 默认 AUTO |
| `IGNORE_UTF8_ERRORS` | 同 JSON |
| `PRESERVE_SPACE` | 是否保留 XML 元素间的空白 |
| `STRIP_OUTER_ELEMENT` | TRUE 时剥离最外层 XML 元素 |
| `DISABLE_SNOWFLAKE_DATA` | TRUE 时禁用特殊 Snowflake 元数据标记 |
| `DISABLE_AUTO_CONVERT` | TRUE 时禁用自动类型转换 |
| `SKIP_BYTE_ORDER_MARK` | 同 JSON |

#### 操作示例

```sql
-- CSV 格式（完整配置）
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

-- JSON 格式
CREATE FILE FORMAT my_json_ff
    TYPE = 'JSON'
    STRIP_OUTER_ARRAY = TRUE
    ENABLE_OCTAL = FALSE
    ALLOW_DUPLICATE = FALSE
    IGNORE_UTF8_ERRORS = TRUE;

-- Parquet 格式
CREATE FILE FORMAT my_parquet_ff
    TYPE = 'PARQUET'
    BINARY_AS_TEXT = TRUE;
```

---

### 5.13 CREATE PIPE

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `AUTO_INGEST` | TRUE 时，当 Stage 有新文件时自动触发 COPY。需配置云存储事件通知 |
| `AWS_SNS_TOPIC` | AWS S3 事件通知的 SNS 主题 ARN |
| `INTEGRATION` | Azure Event Grid 或 GCP Pub/Sub 的通知集成名称 |
| `ERROR_INTEGRATION` | 出错时的错误通知集成 |
| `<copy_statement>` | 定义数据从 Stage 加载到目标表的 COPY INTO 语句 |

#### 操作示例

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

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `START WITH` | 序列起始值，默认 1 |
| `INCREMENT BY` | 步长，可为负数表示递减。默认 1 |
| `MINVALUE / NO MINVALUE` | 最小值/无最小值 |
| `MAXVALUE / NO MAXVALUE` | 最大值/无最大值 |
| `CYCLE / NO CYCLE` | 到达边界后是否循环。默认 NO CYCLE |
| `ORDER / NOORDER` | 是否保证递增顺序。默认 NOORDER。ORDER 会影响性能 |

---

### 5.15 CREATE FUNCTION (UDF)

#### 完整语法 (SQL UDF)

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `TEMP/TEMPORARY` | 创建临时函数，会话结束后自动删除 |
| `SECURE` | 创建安全函数。隐藏定义细节 |
| `RETURNS <type>` | SQL UDF: 返回标量类型。UDTF: `RETURNS TABLE (col type, ...)` |
| `NOT NULL` | (SQL UDF) 声明函数返回值永远不为 NULL |
| `VOLATILE / IMMUTABLE` | (SQL UDF) VOLATILE: 相同输入可能返回不同值。IMMUTABLE: 相同输入总是返回相同值（可被优化器缓存） |
| `CALLED ON NULL INPUT` | (默认) 即使输入为 NULL 也调用函数 |
| `RETURNS NULL ON NULL INPUT / STRICT` | 输入为 NULL 时直接返回 NULL，不调用函数 |
| `RUNTIME_VERSION` | Python/Java UDF 的运行时版本 |
| `IMPORTS` | Python/Java UDF 的导入文件路径（Stage 路径） |
| `PACKAGES` | Python UDF 的 Python 包列表 |
| `HANDLER` | Python/Java UDF 的处理函数/方法名 |
| `LANGUAGE` | 函数实现语言 |

---

### 5.16 CREATE PROCEDURE

#### 完整语法 (SQL)

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

#### 关键参数说明

| 参数 | 说明 |
|------|------|
| `EXECUTE AS CALLER` | (默认) 以调用者权限执行 |
| `EXECUTE AS OWNER` | 以存储过程所有者权限执行。适合让低权限用户通过存储过程执行高权限操作 |
| `RETURNS` | SQL 存储过程必须声明 RETURNS。JavaScript 存储过程可选返回类型 |

---

### 5.17 CREATE TASK

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `WAREHOUSE` | 执行任务使用的 Warehouse |
| `USER_TASK_MANAGED_INITIAL_WAREHOUSE_SIZE` | Serverless Task 的初始计算规模 |
| `SCHEDULE` | 调度方式。`<minutes> MINUTE` 或 `USING CRON <expr> <tz>` |
| `ALLOW_OVERLAPPING_EXECUTION` | 是否允许并发执行（默认 FALSE，同时间只运行一个实例） |
| `SESSION_PARAMETER` | 任务执行时的会话参数 |
| `USER_TASK_TIMEOUT_MS` | Serverless Task 超时时间（毫秒） |
| `SUSPEND_TASK_AFTER_NUM_FAILURES` | 连续失败多少次后自动暂停任务 |
| `ERROR_INTEGRATION` | 错误通知集成 |
| `AFTER` | 指定前置任务，构成 DAG（有向无环图） |
| `WHEN` | 条件表达式。仅当为 TRUE 时才执行 SQL |

---

### 5.18 CREATE STREAM

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `ON TABLE <table_name>` | Stream 跟踪的目标表（外部表、视图、目录表也可） |
| `AT/BEFORE` | 创建 Stream 的时间点偏移 |
| `APPEND_ONLY = TRUE` | 仅跟踪 INSERT 操作，忽略 UPDATE/DELETE。适用于追加式数据源 |
| `SHOW_INITIAL_ROWS = TRUE` | 首次查询 Stream 时包含创建前已存在的所有行 |
| `INSERT_ONLY = TRUE` | 同 APPEND_ONLY 别名 |

---

### 5.19 CREATE USER

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `PASSWORD` | 用户密码。受账户密码策略约束 |
| `LOGIN_NAME` | 登录名。区别于显示名，可用于 SSO 集成 |
| `DISPLAY_NAME` | 界面显示名称 |
| `MUST_CHANGE_PASSWORD` | 首次登录是否强制修改密码 |
| `DISABLED` | 是否禁用用户 |
| `DAYS_TO_EXPIRY` | 密码过期天数 |
| `MINS_TO_UNLOCK` | 账户锁定时长（分钟） |
| `DEFAULT_WAREHOUSE` | 用户默认 Warehouse |
| `DEFAULT_NAMESPACE` | 用户默认 数据库.Schema |
| `DEFAULT_ROLE` | 用户默认角色 |
| `DEFAULT_SECONDARY_ROLES` | 默认次要角色，`('ALL')` 表示所有被授予的角色 |
| `MINS_TO_BYPASS_MFA` | MFA 验证缓存时长（分钟） |
| `RSA_PUBLIC_KEY` | RSA 公钥（用于 Key Pair 认证） |

---

### 5.20 CREATE ROLE

#### 完整语法

```sql
CREATE [ OR REPLACE ] ROLE [ IF NOT EXISTS ] <name>
    [ COMMENT = '<string>' ]
```

#### 操作示例

```sql
CREATE ROLE data_analyst;
CREATE ROLE data_engineer COMMENT = 'ETL pipeline role';
CREATE ROLE data_scientist COMMENT = 'ML and analytics role';
```

---

### 5.21 CREATE WAREHOUSE

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `WAREHOUSE_SIZE` | Warehouse 大小。每个 Size 翻倍计算资源。XSMALL=1 node, SMALL=2, MEDIUM=4, LARGE=8, XLARGE=16, 2XLARGE=32, 3XLARGE=64, 4XLARGE=128 |
| `WAREHOUSE_TYPE` | `STANDARD` 通用型；`SNOWPARK_OPTIMIZED` 针对 Snowpark 内存密集型工作负载优化 |
| `MAX_CLUSTER_COUNT` | 最大集群数 (1-10)。>1 启用 Multi-cluster Warehouse |
| `MIN_CLUSTER_COUNT` | 最小集群数。≤ MAX_CLUSTER_COUNT |
| `SCALING_POLICY` | `STANDARD` 快速扩缩(更快响应)；`ECONOMY` 保守扩缩(节约成本) |
| `AUTO_SUSPEND` | 空闲自动暂停秒数。NULL 表示不自动暂停。默认 600 秒。Warehouse 暂停后不收费 |
| `AUTO_RESUME` | 有查询时自动恢复。默认 TRUE |
| `INITIALLY_SUSPENDED` | 创建后是否立即挂起。默认 FALSE |
| `RESOURCE_MONITOR` | 关联的资源监控器 |
| `ENABLE_QUERY_ACCELERATION` | 启用查询加速服务 |
| `QUERY_ACCELERATION_MAX_SCALE_FACTOR` | 查询加速最大倍数 |
| `MAX_CONCURRENCY_LEVEL` | 最大并发查询数。默认 8 |
| `STATEMENT_QUEUED_TIMEOUT_IN_SECONDS` | 队列等待超时（秒） |
| `STATEMENT_TIMEOUT_IN_SECONDS` | 语句执行超时（秒） |

---

## 6. DML 完整语法参考

### 6.1 INSERT

#### 完整语法

```sql
INSERT [ OVERWRITE ] INTO <target_table>
    [ ( <target_column_list> ) ]
    {
        VALUES ( { <expr> | DEFAULT | NULL } [ , ... ] ) [ , ( ... ) , ... ]
        | <query>
    }
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `OVERWRITE` | 覆盖写入。先 TRUNCATE 表再插入。比 DELETE+INSERT 更高效 |
| `<target_table>` | 目标表名 |
| `<target_column_list>` | 指定要插入的列。未指定的列使用默认值或 NULL |
| `VALUES` | 值列表，支持多行插入 |
| `DEFAULT` | 使用列的默认值 |
| `<query>` | 子查询结果插入 |

#### 操作示例

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

-- 从查询插入
INSERT INTO high_salary_employees
SELECT * FROM employees WHERE salary > 100000;

-- OVERWRITE (覆盖写入)
INSERT OVERWRITE INTO staging_table
SELECT * FROM source_table WHERE load_date = CURRENT_DATE();
```

---

### 6.2 UPDATE

#### 完整语法

```sql
UPDATE <target_table>
    SET <column_name> = <expr> [ , <column_name> = <expr> , ... ]
    [ FROM <additional_tables> ]
    [ WHERE <condition> ]
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `SET` | 列=值 对。可一次更新多列 |
| `FROM` | 额外的源表，用于关联更新 |
| `WHERE` | 过滤条件。**如果省略 WHERE，所有行都会被更新** |

#### 操作示例

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

#### 完整语法

```sql
DELETE FROM <table_name>
    [ USING <additional_tables> ]
    [ WHERE <condition> ]
```

#### 参数说明

| 参数 | 说明 |
|------|------|
| `USING` | 关联其他表来过滤删除条件 |
| `WHERE` | 过滤条件。**如果省略 WHERE，删除所有行** |

#### 操作示例

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

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `<target_table>` | 目标表（被合并到的表） |
| `<source>` | 源表或子查询（提供合并数据的来源） |
| `ON <join_condition>` | 匹配条件 |
| `WHEN MATCHED` | 当行匹配时的操作：UPDATE 或 DELETE |
| `WHEN NOT MATCHED` | 当行不匹配时的操作：INSERT |
| `AND <case_predicate>` | 额外的条件分支过滤 |
| `UPDATE SET` | 匹配时更新列值 |
| `DELETE` | 匹配时删除行 |
| `INSERT` | 不匹配时插入新行 |

> **注意：** 一个 MERGE 语句可有多个 WHEN MATCHED 子句（带不同条件），但至多一个 WHEN NOT MATCHED。

#### 操作示例

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

### 6.5 COPY INTO <table> (数据加载)

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `<column_list>` | 指定要加载的列和顺序 |
| `FROM` | 数据源位置（Stage 或外部位置） |
| `FILES` | 指定具体文件名列表 |
| `PATTERN` | 正则表达式过滤文件名 |
| `FILE_FORMAT` | 文件格式定义 |
| `VALIDATION_MODE` | 验证模式：返回 N 行数据预览或错误信息 |
| `ON_ERROR` | 错误处理策略 |
| `SIZE_LIMIT` | 加载数据量限制（字节） |
| `PURGE` | 加载成功后是否删除 Stage 中的文件 |
| `MATCH_BY_COLUMN_NAME` | 按列名匹配而非按顺序。适用于 Schema 演化场景 |
| `ENFORCE_LENGTH` | 列长度超限是否截断 |
| `TRUNCATECOLUMNS` | 超出目标列长度时是否截断 |
| `FORCE` | 强制加载所有文件（即使已加载过） |
| `LOAD_UNCERTAIN_FILES` | 加载上次未完成的文件 |

#### 操作示例

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

### 6.6 COPY INTO <location> (数据卸载)

#### 完整语法

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

#### 参数说明

| 参数 | 说明 |
|------|------|
| `FROM <table> / (<query>)` | 卸载的数据源 |
| `PARTITION BY <expr>` | 按表达式分区输出文件 |
| `HEADER` | 输出 CSV 时是否包含列名头行 |
| `MAX_FILE_SIZE` | 单个文件最大大小（字节）。默认 16000000 (16 MB) |
| `SINGLE` | 是否输出为单个文件（默认 FALSE，可能输出多个文件） |
| `OVERWRITE` | 是否覆盖已存在的文件 |
| `INCLUDE_QUERY_ID` | 文件名中是否包含查询 ID |

#### 操作示例

```sql
-- 卸载表到 Stage
COPY INTO @my_s3_stage/export/
FROM employees
FILE_FORMAT = (TYPE = CSV COMPRESSION = GZIP)
HEADER = TRUE
MAX_FILE_SIZE = 50000000;

-- 卸载查询结果
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


## 7. SELECT 查询完整语法

### 7.1 SELECT 完整语法

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
| `FROM` | 数据源。支持表、视图、表函数、子查询 |
| `AT / BEFORE` | Time Travel 时间点查询 |
| `SAMPLE / TABLESAMPLE` | 抽样查询 |
| `FLATTEN` | 展开半结构化数据 |
| `PIVOT / UNPIVOT` | 行列转换 |
| `WHERE` | 行过滤 |
| `GROUP BY` | 分组。支持 GROUPING SETS / CUBE / ROLLUP |
| `HAVING` | 分组后过滤 |
| `QUALIFY` | 窗口函数结果过滤 |
| `ORDER BY` | 排序。NULLS FIRST / LAST 控制 NULL 排序位置 |
| `LIMIT / OFFSET` | 限制返回行数和偏移量 |

---

### 7.2 JOIN 类型详解

| JOIN 类型 | 描述 |
|-----------|------|
| **INNER JOIN** | 仅返回两表都匹配的行 |
| **LEFT [OUTER] JOIN** | 返回左表全部行 + 右表匹配行，不匹配时右表列为 NULL |
| **RIGHT [OUTER] JOIN** | 返回右表全部行 + 左表匹配行，不匹配时左表列为 NULL |
| **FULL [OUTER] JOIN** | 返回两表全部行，不匹配时对应列为 NULL |
| **CROSS JOIN** | 笛卡尔积（所有行的组合） |
| **NATURAL JOIN** | 自动按同名列进行 INNER/LEFT/RIGHT/FULL JOIN |
| **LATERAL JOIN** | 允许子查询/表函数引用左侧表的列。常用于 FLATTEN |

#### 操作示例

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

### 7.3 子查询与 CTE

#### 子查询

```sql
-- 标量子查询（返回单个值）
SELECT employee_id, first_name, salary,
    (SELECT AVG(salary) FROM employees) AS avg_salary,
    salary - (SELECT AVG(salary) FROM employees) AS salary_diff
FROM employees;

-- IN / NOT IN 子查询
SELECT * FROM employees
WHERE department_id IN (SELECT department_id FROM departments WHERE location = 'New York');

-- EXISTS / NOT EXISTS
SELECT * FROM employees e
WHERE EXISTS (SELECT 1 FROM sales s WHERE s.employee_id = e.employee_id);

-- 派生表（FROM 子句中的子查询）
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

### 7.4 窗口函数

Snowflake 支持的窗口函数分为以下几类：

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

**聚合窗口函数：**
`SUM()`, `AVG()`, `COUNT()`, `MIN()`, `MAX()`, `STDDEV()`, `VARIANCE()` 等，配合 OVER() 子句

#### 操作示例

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

### 7.5 集合操作 (UNION / INTERSECT / EXCEPT)

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

### 7.6 半结构化数据查询 (FLATTEN / LATERAL)

```sql
-- 基础 FLATTEN
SELECT *
FROM TABLE(FLATTEN(input => PARSE_JSON('[{"a":1},{"a":2},{"a":3}]')));

-- 从表列 FLATTEN
SELECT e.id, f.value::STRING AS skill
FROM employees e,
LATERAL FLATTEN(input => e.skills_array) f;

-- FLATTEN 详解参数
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

`QUALIFY` 是 Snowflake 特有的子句，用于过滤窗口函数结果。它等同于在 `WHERE` 中使用窗口函数的子查询。

```sql
-- 每个部门薪资最高的员工（用 QUALIFY 替代子查询）
SELECT employee_id, first_name, department_id, salary
FROM employees
QUALIFY ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) = 1;

-- 等价于：
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

### 7.8 SAMPLE / TABLESAMPLE 抽样查询

```sql
-- 抽样 10% 的数据（BERNOULLI: 按行随机抽样）
SELECT * FROM employees SAMPLE BERNOULLI (10);

-- 抽样 10%（SYSTEM: 按 block/micro-partition 抽样，更快但分布可能不均）
SELECT * FROM employees SAMPLE SYSTEM (10);

-- 可重复抽样（相同 seed 返回相同结果）
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

### 7.10 CONNECT BY 层次查询

```sql
-- 组织架构层次查询
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

## 8. 数据加载与卸载

### 8.1 Stage (暂存区) 详解

**Stage 类型对比：**

| 类型 | 创建方式 | 引用 | 生命周期 |
|------|----------|------|----------|
| **User Stage** | 自动 | `@~` | 跟随用户 |
| **Table Stage** | 自动 | `@%table_name` | 跟随表 |
| **Internal Named Stage** | 手动创建 | `@stage_name` | 独立管理 |
| **External Stage** | 手动创建 | `@ext_stage` | 独立管理 |

**Stage 常用操作：**

```sql
-- 列出 Stage 内容
LIST @my_stage;
LIST @my_stage PATTERN='.*\\.csv$';
LIST @my_stage PATTERN='.*2026-05-.*\\.json';

-- 查看 Stage 详情
DESC STAGE my_stage;

-- 查询目录表（需启用 DIRECTORY）
SELECT * FROM DIRECTORY(@my_stage);

-- 从 Stage 删除文件
REMOVE @my_stage/old_file.csv;
REMOVE @my_stage PATTERN='.*\\.tmp$';
```

---

### 8.2 PUT 命令上传文件

> **重要：** PUT 命令仅在 SnowSQL CLI 交互模式中可用，SQL Worksheet 中无法使用。

```bash
# SnowSQL 交互模式中：

-- 上传单个文件到内部 Stage
PUT file:///local/path/data.csv @my_internal_stage;

-- 上传多个文件（通配符）
PUT file:///local/path/*.csv @my_internal_stage/;

-- 压缩上传
PUT file:///local/path/data.csv @my_internal_stage/ AUTO_COMPRESS=TRUE;

-- 覆盖已存在文件
PUT file:///local/path/data.csv @my_internal_stage/ OVERWRITE=TRUE;

-- 上传到用户 Stage
PUT file:///local/path/data.csv @~/;

-- 上传到表 Stage
PUT file:///local/path/data.csv @%employees/;

-- 指定源压缩
PUT file:///local/path/data.csv @my_stage/ SOURCE_COMPRESSION=GZIP;
```

**PUT 命令参数说明：**

| 参数 | 说明 |
|------|------|
| `AUTO_COMPRESS` | TRUE(默认): 自动 gzip 压缩上传 |
| `OVERWRITE` | TRUE: 覆盖已存在文件。FALSE(默认): 跳过 |
| `SOURCE_COMPRESSION` | 声明源文件已使用的压缩格式 (GZIP/BZIP2 等)，上传时不解压 |
| `PARALLEL` | 并行上传线程数，默认 4 |

---

### 8.3 COPY INTO 加载数据

#### 完整语法见 [6.5 COPY INTO <table>](#65-copy-into-table-数据加载)

#### 典型加载流程操作手顺：

**步骤 1: 准备数据文件**
```
CSV 示例 (employees.csv):
employee_id,first_name,last_name,email,hire_date,salary,department_id
1,John,Doe,john@example.com,2025-01-15,75000,10
2,Jane,Smith,jane@example.com,2025-03-20,80000,20
3,Bob,Johnson,bob@example.com,2025-06-01,65000,10
```

**步骤 2: 创建 File Format**

```sql
CREATE FILE FORMAT emp_csv_ff
    TYPE = 'CSV'
    SKIP_HEADER = 1
    FIELD_OPTIONALLY_ENCLOSED_BY = '"'
    NULL_IF = ('NULL', '', 'null');
```

**步骤 3: 创建目标表**

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

**步骤 4: 上传文件到 Stage**
```bash
# SnowSQL CLI
PUT file:///local/path/employees.csv @my_stage/;
```

**步骤 5: 执行 COPY INTO**

```sql
COPY INTO employees
FROM @my_stage/
FILES = ('employees.csv.gz')
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
ON_ERROR = 'ABORT_STATEMENT';
```

**步骤 6: 验证结果**

```sql
-- 查看加载的 COPY 历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('hours', -24, CURRENT_TIMESTAMP())
));

-- 验证行数
SELECT COUNT(*) FROM employees;
```

---

### 8.4 VALIDATION_MODE 数据校验

```sql
-- 预览前 10 行（不实际加载）
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_10_ROWS;

-- 预览前 100 行
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_100_ROWS;

-- 返回所有错误（不加载数据）
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
VALIDATION_MODE = RETURN_ALL_ERRORS;

-- 仅返回失败的行
COPY INTO employees
FROM @my_stage/
FILE_FORMAT = (FORMAT_NAME = emp_csv_ff)
RETURN_FAILED_ONLY = TRUE;
```

---

### 8.5 Snowpipe 自动加载

Snowpipe 实现文件到达即自动加载。

**步骤 1: 创建 Pipe**

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

**步骤 2: 配置云存储事件通知**
- AWS: S3 Bucket -> SNS Topic -> Snowpipe
- Azure: Storage Account -> Event Grid -> Snowpipe
- GCP: GCS -> Pub/Sub -> Snowpipe

**步骤 3: 监控 Pipe**

```sql
-- 查看 Pipe 状态
SELECT SYSTEM$PIPE_STATUS('employee_pipe');

-- 查看加载历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.PIPE_USAGE_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    PIPE_NAME => 'EMPLOYEE_PIPE'
));

-- 查看 COPY 历史（所有来源包括 Pipe）
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('day', -1, CURRENT_TIMESTAMP())
));
```

---

### 8.6 数据卸载 (UNLOAD)

```sql
-- 卸载为 CSV
COPY INTO @my_s3_stage/export/employees/
FROM employees
FILE_FORMAT = (TYPE = CSV COMPRESSION = GZIP)
HEADER = TRUE
MAX_FILE_SIZE = 50000000
OVERWRITE = TRUE;

-- 卸载为 Parquet
COPY INTO @my_s3_stage/export/employees_parquet/
FROM employees
FILE_FORMAT = (TYPE = PARQUET COMPRESSION = SNAPPY)
OVERWRITE = TRUE;

-- 卸载为 JSON
COPY INTO @my_s3_stage/export/employees_json/
FROM employees
FILE_FORMAT = (TYPE = JSON COMPRESSION = GZIP)
OVERWRITE = TRUE;

-- 分区卸载（按部门）
COPY INTO @my_s3_stage/export/employees/
FROM employees
PARTITION BY department_id
FILE_FORMAT = (TYPE = CSV)
HEADER = TRUE;

-- 下载文件到本地（SnowSQL CLI）
-- GET @my_s3_stage/export/employees/ file:///local/download/;
```

---

## 9. Time Travel (时间旅行) 与 Fail-safe (故障安全)

### 9.1 Time Travel 概述

Time Travel 允许在数据保留期内访问、查询和恢复历史数据。

```
数据变更时间轴:
<--- 过去                              现在              未来 --->
|...|------ Time Travel 保留期 -----|------ Fail-safe ------|
     (0-90天，可配置，可查询/恢复)   (7天固定，不可查询，需 Support 恢复)
```

**关键特性：**

| 特性 | Time Travel | Fail-safe |
|------|-------------|-----------|
| **可查询** | 是 | 否 |
| **可克隆** | 是 | 否 |
| **可 UNDROP** | 是 | 否 |
| **保留期** | 0-90 天 (可配置) | 7 天 (固定) |
| **费用** | 包含在存储费用中 | 额外存储费用 |
| **恢复方式** | 用户自助 | 需联系 Snowflake Support |

---

### 9.2 Time Travel 操作手顺

**三种时间定位方式：**

```sql
-- 1. TIMESTAMP: 指定精确时间点
SELECT * FROM employees
    AT (TIMESTAMP => '2026-05-12 08:00:00'::TIMESTAMP);

-- 2. OFFSET: 相对当前时间的偏移（秒）
SELECT * FROM employees
    AT (OFFSET => -3600);  -- 1 小时前

-- 3. STATEMENT: 指定查询 ID 之前的状态
SELECT * FROM employees
    BEFORE (STATEMENT => '01a2b3c4-0506-0708-090a-0b0c0d0e0f00');
```

**查询历史数据：**

```sql
-- 查询 5 分钟前的表状态
SELECT * FROM employees AT (OFFSET => -300);

-- 查询昨天的表状态
SELECT * FROM employees AT (TIMESTAMP => DATEADD('day', -1, CURRENT_TIMESTAMP())::TIMESTAMP);

-- 查询特定时间点
SELECT * FROM employees AT (TIMESTAMP => '2026-05-11 14:30:00'::TIMESTAMP);

-- 克隆到历史时间点
CREATE TABLE employees_restored CLONE employees
    AT (TIMESTAMP => '2026-05-10 12:00:00'::TIMESTAMP);

-- 使用 BEFORE（在指定时间点之前，语义同 AT）
SELECT * FROM employees BEFORE (OFFSET => -3600);
```

**获取查询 ID：**

```sql
-- 从 Query History 获取已执行查询的 ID
SELECT QUERY_ID, QUERY_TEXT, START_TIME
FROM TABLE(INFORMATION_SCHEMA.QUERY_HISTORY(
    DATE_RANGE_START => DATEADD('day', -1, CURRENT_TIMESTAMP()),
    DATE_RANGE_END => CURRENT_TIMESTAMP()
))
WHERE QUERY_TEXT ILIKE '%INSERT INTO employees%'
ORDER BY START_TIME DESC;
```

---

### 9.3 UNDROP 恢复对象

可恢复的对象类型：DATABASE、SCHEMA、TABLE、VIEW、FUNCTION、PROCEDURE、STAGE、FILE FORMAT、PIPE、SEQUENCE、STREAM、TASK。

```sql
-- 恢复已删除的表
UNDROP TABLE employees;

-- 恢复已删除的数据库
UNDROP DATABASE training_db;

-- 恢复已删除的 Schema
UNDROP SCHEMA training_db.hr_schema;

-- 如果创建了同名新表再恢复旧表，需先重命名新表
ALTER TABLE employees RENAME TO employees_new;
UNDROP TABLE employees;  -- 恢复旧的 employees
```

---

### 9.4 Fail-safe 概述

Fail-safe 在 Time Travel 过期后提供额外 7 天的数据保护。此期间数据对用户不可见，不可查询。如需恢复，必须联系 Snowflake 技术支持。

---

### 9.5 数据保留策略配置

```sql
-- 账户级别（需 ACCOUNTADMIN）
ALTER ACCOUNT SET DATA_RETENTION_TIME_IN_DAYS = 14;

-- 数据库级别（覆盖账户设置）
ALTER DATABASE training_db SET DATA_RETENTION_TIME_IN_DAYS = 7;

-- Schema 级别（覆盖数据库设置）
ALTER SCHEMA training_db.hr_schema SET DATA_RETENTION_TIME_IN_DAYS = 3;

-- 表级别（覆盖 Schema 设置）
ALTER TABLE employees SET DATA_RETENTION_TIME_IN_DAYS = 1;

-- 创建时指定
CREATE TABLE critical_data (...) DATA_RETENTION_TIME_IN_DAYS = 90;
```

> **最佳实践：** 生产表建议至少 7 天，关键业务表建议 14-30 天。瞬态表固定为 1 天。

---

## 10. Zero-Copy Cloning (零拷贝克隆)

### 10.1 克隆原理

克隆不复制实际数据，仅复制元数据指针，因此：
- **极快：** 无论数据量多大，克隆几乎瞬时完成
- **零额外存储：** 克隆后不产生额外的存储成本（只存储后续变更的数据）
- **独立可写：** 克隆后的对象可独立修改，修改只影响该克隆

```
克隆前:                 克隆后:
Table_A                 Table_A          Table_A_CLONE
  |                      |                 |
  +-- Micro-partitions --+ (共享) ---------+
  |                      |
  +-- 元数据             +-- 独立元数据    +-- 独立元数据
```

---

### 10.2 克隆操作手顺

```sql
-- 克隆数据库
CREATE DATABASE prod_clone CLONE prod_db;

-- 克隆 Schema
CREATE SCHEMA training_db.hr_clone CLONE training_db.hr_schema;

-- 克隆表
CREATE TABLE employees_clone CLONE employees;

-- 克隆 Time Travel 时间点的表
CREATE TABLE employees_yesterday CLONE employees
    AT (OFFSET => -86400);

CREATE TABLE employees_snapshot CLONE employees
    AT (TIMESTAMP => '2026-05-01 00:00:00'::TIMESTAMP);

-- 克隆 + 覆盖（如果目标已存在）
CREATE OR REPLACE TABLE employees_clone CLONE employees;

-- 克隆带复制权限
CREATE TABLE employees_clone CLONE employees COPY GRANTS;
```

---

### 10.3 克隆权限与注意事项

**权限要求：**
- 克隆数据库：需源数据库的 USAGE 权限 + 目标数据库的 CREATE DATABASE 权限
- 克隆 Schema：需源 Schema 的 USAGE 权限 + 目标数据库的 CREATE SCHEMA 权限
- 克隆表：需源表的 SELECT 权限 + 目标 Schema 的 CREATE TABLE 权限

**注意事项：**
1. 克隆是**浅拷贝**——克隆对象共享源对象的存储（直到数据被修改）
2. 克隆不复制 Load History（源表的 COPY 历史不会带到克隆中）
3. 克隆的 Pipes 默认是暂停状态
4. 克隆包含源对象的权限授予（如果使用 COPY GRANTS）
5. 克隆会影响源对象的 Time Travel 保留——只要克隆存在，被引用的 micro-partition 就不会被物理删除

**典型使用场景：**
- 创建生产环境的即时开发/测试副本
- 数据备份和快照
- 数据科学实验（不影响生产数据）
- 问题调试（回到特定时间点分析数据状态）

---


## 11. Data Sharing (数据共享)

### 11.1 数据共享架构

Snowflake 数据共享允许在不同账户间安全地共享数据——**无需复制数据**。数据消费者可直接查询提供者共享的实时数据。

```
Provider Account (提供者)              Consumer Account (消费者)
+-----------------------+            +------------------------+
| Database / Schema     |  Share    | Database from Share     |
| + Table / View / UDF  | ========> | (只读，零拷贝)          |
+-----------------------+            +------------------------+
```

**关键概念：**

| 概念 | 说明 |
|------|------|
| **Share** | 共享容器，定义哪些对象共享给哪些消费者 |
| **Provider (提供者)** | 拥有数据并创建 Share 的账户 |
| **Consumer (消费者)** | 从 Share 创建只读数据库的账户 |
| **Reader Account** | 特殊的消费者账户，为没有 Snowflake 账户的组织创建 |
| **Data Marketplace** | Snowflake 的数据市场，可公开或私密共享数据 |

---

### 11.2 创建与配置 Share

**步骤 1: 创建 Share**

```sql
-- 创建空的 Share
CREATE SHARE employee_data_share
    COMMENT = 'Employee data share for HR analytics';
```

**步骤 2: 向 Share 添加对象**

```sql
-- 授予数据库使用权（Consumer 需要此权限才能看到数据库结构）
GRANT USAGE ON DATABASE training_db TO SHARE employee_data_share;

-- 授予 Schema 使用权
GRANT USAGE ON SCHEMA training_db.hr_schema TO SHARE employee_data_share;

-- 授予表只读权限
GRANT SELECT ON TABLE training_db.hr_schema.employees TO SHARE employee_data_share;

-- 授予视图
GRANT SELECT ON VIEW training_db.hr_schema.vw_salary_summary TO SHARE employee_data_share;

-- 授予安全 UDF
GRANT USAGE ON FUNCTION training_db.hr_schema.calculate_bonus(INTEGER, FLOAT)
    TO SHARE employee_data_share;
```

**步骤 3: 添加消费者账户**

```sql
-- 添加一个消费者账户
ALTER SHARE employee_data_share
    ADD ACCOUNTS = consumer_org.consumer_account;

-- 添加多个消费者账户
ALTER SHARE employee_data_share
    ADD ACCOUNTS = consumer1_org.consumer1_account,
                   consumer2_org.consumer2_account;

-- 移除消费者
ALTER SHARE employee_data_share
    REMOVE ACCOUNTS = consumer_org.old_account;
```

**步骤 4: 验证 Share 配置**

```sql
-- 查看 Share 详情
SHOW SHARES;
DESC SHARE employee_data_share;

-- 查看 Share 中包含的对象
SHOW GRANTS TO SHARE employee_data_share;

-- 查看 Share 的消费者
SHOW GRANTS OF SHARE employee_data_share;
```

---

### 11.3 创建 Reader Account

当接收方没有 Snowflake 账户时，提供者可为其创建 Reader Account。

```sql
-- 创建 Reader Account
CREATE MANAGED ACCOUNT reader_account_name
    ADMIN_NAME = 'reader_admin'
    ADMIN_PASSWORD = '<strong_password>'
    TYPE = READER
    COMMENT = 'Reader account for partner XYZ';

-- 查看 Reader Account 的状态
SHOW MANAGED ACCOUNTS;

-- 将 Reader Account 添加到 Share
ALTER SHARE employee_data_share
    ADD ACCOUNTS = reader_account_locator;
```

---

### 11.4 从 Share 创建数据库 (消费者侧)

```sql
-- 查看收到的 Share（需使用 ACCOUNTADMIN）
SHOW SHARES;
-- 查看收到的入站 Share
SELECT * FROM TABLE(INFORMATION_SCHEMA.INBOUND_SHARES());

-- 从 Share 创建数据库
CREATE DATABASE shared_employee_data
    FROM SHARE provider_org.provider_account.employee_data_share;

-- 验证可访问的表
SELECT * FROM shared_employee_data.hr_schema.employees LIMIT 10;

-- 如果直接共享（Direct Share），消费者无需额外权限
-- 创建新角色并授予权限（消费者侧）
CREATE ROLE share_reader;
GRANT ROLE share_reader TO USER analyst_user;
GRANT IMPORTED PRIVILEGES ON DATABASE shared_employee_data TO ROLE share_reader;
```

---

### 11.5 Data Marketplace

```sql
-- 查看可用的 Marketplace Listing
SHOW LISTINGS;

-- 获取 Marketplace 数据集
-- 1. 在 Snowsight -> Data -> Marketplace 中浏览和获取
-- 2. 或者通过 SQL:
CREATE DATABASE marketplace_db FROM SHARE provider_org.provider_account.listing_share;

-- 创建私有 Listing（仅指定消费者可见）
CREATE LISTING my_private_listing
    FOR SHARE employee_data_share
    COMMENT = 'Private HR data listing'
    AS PRIVATELISTING;
```

---

## 12. 安全与访问控制 (RBAC)

### 12.1 RBAC 模型概述

Snowflake 使用**基于角色的访问控制 (RBAC)** 和**自主访问控制 (DAC)** 的混合模型。

**核心概念：**

```
ACCOUNTADMIN (最顶级，管理一切)
  |
  +-- SECURITYADMIN (管理用户和角色)
  |     |
  |     +-- USERADMIN (创建用户和角色)
  |
  +-- SYSADMIN (管理所有数据库对象)
        |
        +-- CUSTOM_ROLE_A (自定义角色)
              |
              +-- CUSTOM_ROLE_B (角色继承)
```

**预定义系统角色：**

| 角色 | 职责 |
|------|------|
| **ACCOUNTADMIN** | 最高权限角色。包含 SECURITYADMIN + SYSADMIN 的所有权限 |
| **SECURITYADMIN** | 管理用户、角色、权限授予。可创建/管理用户和角色 |
| **USERADMIN** | 创建用户和角色（SECURITYADMIN 的受限子集） |
| **SYSADMIN** | 管理所有数据库对象（Warehouse、数据库、表、视图等） |
| **PUBLIC** | 每个用户默认拥有。最小权限 |

**权限类型：**

| 权限类别 | 示例权限 |
|----------|----------|
| **账户权限** | CREATE DATABASE, CREATE WAREHOUSE, CREATE USER, CREATE ROLE |
| **数据库权限** | USAGE, MODIFY, MONITOR, CREATE SCHEMA |
| **Schema 权限** | USAGE, MODIFY, MONITOR, CREATE TABLE, CREATE VIEW, CREATE STAGE |
| **表权限** | SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES |
| **视图权限** | SELECT, REFERENCES |
| **Stage 权限** | READ, WRITE |
| **函数/过程权限** | USAGE |

---

### 12.2 用户管理操作手顺

**创建用户：**

```sql
-- 基础创建
CREATE USER john_doe
    PASSWORD = 'TempPass123!'
    DEFAULT_ROLE = data_analyst
    DEFAULT_WAREHOUSE = analyst_wh
    DEFAULT_NAMESPACE = training_db.public
    MUST_CHANGE_PASSWORD = TRUE
    COMMENT = 'Data Analyst - John Doe';

-- 完整创建
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

**管理用户：**

```sql
-- 修改用户
ALTER USER john_doe SET PASSWORD = 'NewPass789!';
ALTER USER john_doe SET DISABLED = TRUE;         -- 禁用用户
ALTER USER john_doe SET DISABLED = FALSE;        -- 启用用户
ALTER USER john_doe RESET PASSWORD;               -- 重置密码
ALTER USER john_doe SET DEFAULT_ROLE = data_engineer;
ALTER USER john_doe SET DEFAULT_WAREHOUSE = etl_wh;

-- 查看用户
SHOW USERS;
SHOW USERS LIKE 'john_doe';
DESC USER john_doe;

-- 删除用户
DROP USER john_doe;
```

---

### 12.3 角色管理操作手顺

```sql
-- 创建角色
CREATE ROLE data_analyst;
CREATE ROLE data_engineer COMMENT = 'ETL pipeline operators';
CREATE ROLE data_scientist COMMENT = 'ML and analytics';

-- 角色继承（GRANT ROLE ... TO ROLE）
GRANT ROLE data_analyst TO ROLE SYSADMIN;
GRANT ROLE data_engineer TO ROLE SYSADMIN;

-- 将角色分配给用户
GRANT ROLE data_analyst TO USER john_doe;
GRANT ROLE data_analyst TO USER jane_smith;

-- 查看角色
SHOW ROLES;
SHOW GRANTS TO ROLE data_analyst;
SHOW GRANTS OF ROLE data_analyst;

-- 切换当前角色
USE ROLE data_analyst;
SELECT CURRENT_ROLE();

-- 查看用户的角色
SHOW GRANTS TO USER john_doe;

-- 撤销角色
REVOKE ROLE data_analyst FROM USER john_doe;

-- 删除角色
DROP ROLE data_analyst;
```

---

### 12.4 权限授予与撤销

**操作手顺：**

```sql
-- === 步骤 1: 使用 SYSADMIN 创建对象 ===
USE ROLE SYSADMIN;
CREATE DATABASE company_data;
CREATE SCHEMA company_data.hr;
CREATE TABLE company_data.hr.employees (...);

-- === 步骤 2: 创建功能角色 ===
USE ROLE SECURITYADMIN;
CREATE ROLE hr_analyst;
CREATE ROLE hr_manager;

-- === 步骤 3: 授予数据库和 Schema 权限 ===
USE ROLE SYSADMIN;
GRANT USAGE ON DATABASE company_data TO ROLE hr_analyst;
GRANT USAGE ON SCHEMA company_data.hr TO ROLE hr_analyst;

-- === 步骤 4: 授予表权限 ===
GRANT SELECT ON company_data.hr.employees TO ROLE hr_analyst;
GRANT SELECT, INSERT, UPDATE, DELETE ON company_data.hr.employees TO ROLE hr_manager;

-- === 步骤 5: 授予视图权限 ===
GRANT SELECT ON VIEW company_data.hr.vw_salary_summary TO ROLE hr_manager;

-- === 步骤 6: 授予 Warehouse 权限 ===
GRANT USAGE ON WAREHOUSE analyst_wh TO ROLE hr_analyst;

-- === 步骤 7: 将角色授权给用户 ===
USE ROLE SECURITYADMIN;
GRANT ROLE hr_analyst TO USER john_doe;

-- === 撤销权限 ===
REVOKE SELECT ON company_data.hr.employees FROM ROLE hr_analyst;
REVOKE ALL ON company_data.hr.employees FROM ROLE hr_manager;

-- === 查看所有授权 ===
SHOW GRANTS ON DATABASE company_data;
SHOW GRANTS ON SCHEMA company_data.hr;
SHOW GRANTS ON TABLE company_data.hr.employees;
SHOW GRANTS TO ROLE hr_analyst;
SHOW GRANTS TO USER john_doe;
```

---

### 12.5 网络策略

网络策略控制允许连接 Snowflake 的 IP 地址范围。

```sql
-- 创建网络策略
CREATE NETWORK POLICY corp_network_policy
    ALLOWED_IP_LIST = ('192.168.1.0/24', '10.0.0.0/8')
    BLOCKED_IP_LIST = ('192.168.1.100')
    COMMENT = 'Corporate office and VPN network';

-- 应用到账户
ALTER ACCOUNT SET NETWORK_POLICY = corp_network_policy;

-- 应用到用户
ALTER USER john_doe SET NETWORK_POLICY = restricted_policy;

-- 查看
SHOW NETWORK POLICIES;
DESC NETWORK POLICY corp_network_policy;

-- 移除
ALTER ACCOUNT UNSET NETWORK_POLICY;
```

---

### 12.6 数据加密

Snowflake 自动加密所有数据：
- **传输中加密：** TLS 1.2+
- **静态数据加密：** AES-256 自动加密所有存储的数据
- **客户端加密：** 使用客户端提供的密钥进行额外的加密层（上传到 Stage 时）

```sql
-- 创建客户端加密的 Stage
CREATE STAGE encrypted_stage
    ENCRYPTION = (TYPE = 'AWS_CSE' MASTER_KEY = '<your_master_key>')
    URL = 's3://my-bucket/encrypted/'
    STORAGE_INTEGRATION = my_s3_integration;
```

---

### 12.7 Column-Level Security (列级安全)

使用 **Masking Policy** 实现列级数据脱敏。

```sql
-- 创建脱敏策略
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

-- 应用脱敏策略到列
ALTER TABLE employees MODIFY COLUMN ssn
    SET MASKING POLICY mask_ssn;

ALTER TABLE employees MODIFY COLUMN salary
    SET MASKING POLICY mask_salary;

-- 查看脱敏策略
SHOW MASKING POLICIES;

-- 移除脱敏策略
ALTER TABLE employees MODIFY COLUMN salary
    UNSET MASKING POLICY;
```

---

### 12.8 Row-Level Security (行级安全)

使用 **Row Access Policy** 实现行级访问控制。

```sql
-- 创建行访问策略
CREATE ROW ACCESS POLICY dept_access_policy AS (dept_id INTEGER) RETURNS BOOLEAN ->
    CASE
        WHEN CURRENT_ROLE() = 'HR_MANAGER' THEN TRUE
        WHEN CURRENT_ROLE() = 'HR_DEPT10' AND dept_id = 10 THEN TRUE
        WHEN CURRENT_ROLE() = 'HR_DEPT20' AND dept_id = 20 THEN TRUE
        ELSE FALSE
    END;

-- 应用行访问策略
ALTER TABLE employees
    ADD ROW ACCESS POLICY dept_access_policy ON (department_id);

-- 查看
SHOW ROW ACCESS POLICIES;

-- 移除
ALTER TABLE employees DROP ROW ACCESS POLICY dept_access_policy;
ALTER TABLE employees DROP ALL ROW ACCESS POLICIES;
```

---

## 13. Virtual Warehouse (虚拟仓库) 与性能

### 13.1 Warehouse 概述

Virtual Warehouse 是 Snowflake 的计算资源单元。每个 Warehouse 是独立的计算集群。

**Warehouse Size 对应资源：**

| Size | 节点数 | Credits/小时 | 适用场景 |
|------|--------|-------------|----------|
| X-Small | 1 | 1 | 开发/测试/轻量查询 |
| Small | 2 | 2 | 标准查询 |
| Medium | 4 | 4 | 中量 ETL/报表 |
| Large | 8 | 8 | 重量 ETL/大表扫描 |
| X-Large | 16 | 16 | 大型转换/聚合 |
| 2X-Large | 32 | 32 | 超大规模计算 |
| 3X-Large | 64 | 64 | 极大规模计算 |
| 4X-Large | 128 | 128 | 最大规模计算 |
| 5X-Large | 256 | 256 | (需联系 Support) |
| 6X-Large | 512 | 512 | (需联系 Support) |

> **经验法则：** 每个 Size 翻倍 = 性能约翻倍 = 成本翻倍。关键原则：**用更大的 Warehouse 更短时间完成查询 = 不增加成本**（credits = 时间 x 节点数）。

---

### 13.2 Warehouse 创建与配置

```sql
-- 创建 Warehouse
CREATE WAREHOUSE analyst_wh
    WAREHOUSE_SIZE = 'SMALL'
    AUTO_SUSPEND = 300           -- 空闲 5 分钟后自动暂停
    AUTO_RESUME = TRUE           -- 有查询时自动恢复
    INITIALLY_SUSPENDED = TRUE   -- 创建后立即挂起
    MIN_CLUSTER_COUNT = 1
    MAX_CLUSTER_COUNT = 1
    SCALING_POLICY = 'STANDARD'
    MAX_CONCURRENCY_LEVEL = 8
    STATEMENT_TIMEOUT_IN_SECONDS = 3600
    COMMENT = 'Analyst warehouse - business hours';

-- 修改 Warehouse
ALTER WAREHOUSE analyst_wh SET WAREHOUSE_SIZE = 'MEDIUM';
ALTER WAREHOUSE analyst_wh SET AUTO_SUSPEND = 120;  -- 2 分钟

-- 手动操作
ALTER WAREHOUSE analyst_wh SUSPEND;     -- 暂停
ALTER WAREHOUSE analyst_wh RESUME;      -- 恢复
ALTER WAREHOUSE analyst_wh RESUME IF SUSPENDED;

-- 查看 Warehouse
SHOW WAREHOUSES;
SHOW WAREHOUSES LIKE 'analyst_wh';
DESC WAREHOUSE analyst_wh;

-- 查看 Warehouse 使用情况
SELECT * FROM TABLE(INFORMATION_SCHEMA.WAREHOUSE_METERING_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE())
));

-- 删除
DROP WAREHOUSE analyst_wh;
```

---

### 13.3 Multi-cluster Warehouse

当需要高并发处理时，启用多集群 Warehouse。

```sql
-- 创建多集群 Warehouse
CREATE WAREHOUSE report_wh
    WAREHOUSE_SIZE = 'MEDIUM'
    MIN_CLUSTER_COUNT = 1
    MAX_CLUSTER_COUNT = 3
    SCALING_POLICY = 'STANDARD'    -- 快速扩缩
    AUTO_SUSPEND = 300
    AUTO_RESUME = TRUE;

-- ECONOMY 模式（更保守，适合预算敏感）
ALTER WAREHOUSE report_wh SET SCALING_POLICY = 'ECONOMY';

-- 查看集群状态
SHOW WAREHOUSES;
-- 查看当前活动集群数
SELECT * FROM TABLE(INFORMATION_SCHEMA.WAREHOUSE_LOAD_HISTORY(
    DATE_RANGE_START => DATEADD('hour', -1, CURRENT_TIMESTAMP()),
    WAREHOUSE_NAME => 'REPORT_WH'
));
```

**STANDARD vs ECONOMY 扩缩策略：**

| 策略 | 扩缩速度 | 缩容速度 | 适合场景 |
|------|----------|----------|----------|
| STANDARD | 快速响应 | 按需缩容 | 用户交互式查询，需快速响应 |
| ECONOMY | 保守，队列到达一定阈值才扩展 | 保守缩容 | 批处理，预算敏感，可接受短暂排队 |

---

### 13.4 查询性能优化

**核心优化策略：**

```sql
-- 1. 选择合适的 Warehouse Size
-- 小查询用小 WH，大查询用大 WH

-- 2. 使用 DATE/TIMESTAMP 过滤（利用 Micro-partition Pruning）
SELECT * FROM sales_fact
WHERE sale_date BETWEEN '2026-05-01' AND '2026-05-12';

-- 3. 避免 SELECT *（只选择需要的列）
SELECT employee_id, first_name, last_name FROM employees;  -- 好
SELECT * FROM employees;  -- 避免

-- 4. 使用 LIMIT 限制结果
SELECT * FROM large_table LIMIT 1000;

-- 5. 使用结果缓存（24小时内相同查询直接从缓存返回）
-- 相同查询、相同结果集、底层数据未变 -> 0 秒返回，不消耗 credits

-- 6. 利用 Clustering 优化大表扫描
ALTER TABLE sales_fact CLUSTER BY (sale_date, region);
```

---

### 13.5 结果缓存 / 元数据缓存 / 数据缓存

| 缓存类型 | 存储位置 | 有效期 | 说明 |
|----------|----------|--------|------|
| **结果缓存** | Cloud Services | 24 小时 | 完全相同查询直接返回缓存结果。底层数据变更后自动失效。0 credits |
| **元数据缓存** | Cloud Services | 长期 | 行数、MIN/MAX 值等统计信息。用于查询优化和分区裁剪 |
| **数据缓存** | Warehouse SSD | 查询期间 + 暂停前 | Warehouse 本地 SSD 缓存的 Micro-partition 数据，加速后续扫描 |

```sql
-- 查看是否使用了结果缓存
SELECT * FROM employees WHERE department_id = 10;  -- 第一次：执行查询
SELECT * FROM employees WHERE department_id = 10;  -- 第二次：结果缓存命中

-- 禁用结果缓存（用于性能测试对比）
ALTER SESSION SET USE_CACHED_RESULT = FALSE;
```

---

### 13.6 Micro-partition 与 Clustering

**Micro-partition (微分区):**
- Snowflake 自动将数据划分为连续的微分区（默认 50-500 MB/个，未压缩）
- 每个微分区存储列式数据，包含 MIN/MAX 统计信息
- 查询时自动进行 **Partition Pruning**：只扫描相关微分区

**Clustering (聚类):**
当自然聚类（按插入时间）不再有效时，可定义聚类键重新组织数据。

```sql
-- 创建带聚类键的表
CREATE TABLE sales_fact (
    sale_date  DATE,
    product_id INTEGER,
    region     VARCHAR,
    amount     NUMBER(12, 2)
) CLUSTER BY (sale_date, region);

-- 添加/修改聚类键
ALTER TABLE sales_fact CLUSTER BY (sale_date, product_id);

-- 删除聚类键
ALTER TABLE sales_fact DROP CLUSTERING KEY;

-- 手动触发重聚类
ALTER TABLE sales_fact RECLUSTER;

-- 查看聚类信息
SELECT * FROM TABLE(INFORMATION_SCHEMA.AUTOMATIC_CLUSTERING_HISTORY(
    DATE_RANGE_START => DATEADD('day', -7, CURRENT_DATE()),
    TABLE_NAME => 'SALES_FACT'
));

-- 查看表的聚类深度
SELECT SYSTEM$CLUSTERING_INFORMATION('sales_fact');
```

**何时需要 Clustering：**
- 表非常大（TB 级别）
- 查询过滤条件与数据插入顺序不相关
- 查询性能明显下降（扫描大量微分区但返回少量行）
- 不建议在小于 1TB 的表上使用

---

### 13.7 物化视图与查询加速

**物化视图：** 预计算聚合结果，查询时直接读取而非重新计算。

```sql
CREATE MATERIALIZED VIEW mv_hourly_sales AS
SELECT DATE_TRUNC('HOUR', sale_date) AS hour,
       region, product_id,
       SUM(amount) AS total_amount,
       COUNT(*) AS txn_count
FROM sales_fact
GROUP BY 1, 2, 3;
```

**查询加速服务 (Query Acceleration Service)：**
无需创建物化视图，自动识别查询中可卸载的扫描/过滤/部分聚合操作。

```sql
-- 启用查询加速（Warehouse 级别）
ALTER WAREHOUSE analyst_wh SET
    ENABLE_QUERY_ACCELERATION = TRUE
    QUERY_ACCELERATION_MAX_SCALE_FACTOR = 8;
```

---

### 13.8 Search Optimization Service

为特定类型的查询（点查、子串搜索、GEO 搜索等）加速。

```sql
-- 添加搜索优化
ALTER TABLE employees
    ADD SEARCH OPTIMIZATION ON EQUALITY(employee_id, email);

ALTER TABLE large_logs
    ADD SEARCH OPTIMIZATION ON SUBSTRING(message);

ALTER TABLE customer_locations
    ADD SEARCH OPTIMIZATION ON GEO(location);

-- 查看搜索优化状态
SELECT SYSTEM$ESTIMATE_SEARCH_OPTIMIZATION_COSTS('employees');

-- 删除搜索优化
ALTER TABLE employees DROP SEARCH OPTIMIZATION;
```

---

### 13.9 Query Profile 性能分析

在 Snowsight 中查看 Query Profile：
1. 打开 **Activity -> Query History**
2. 找到目标查询，点击 **Query ID**
3. 查看 **Query Profile** 标签页

**分析要点：**

| 指标 | 含义 | 优化方向 |
|------|------|----------|
| **Partitions Scanned** | 扫描的微分区数 | 高值 => 添加 Clustering |
| **Bytes Scanned** | 扫描字节数 | 高值 => 加 WHERE 条件或 Clustering |
| **Spilling to Local Storage** | 本地溢出 | Medium 大小不够 => 加大 Warehouse |
| **Spilling to Remote Storage** | 远程溢出 | Warehouse 严重不足 => 加大 Warehouse |
| **Percentage Scanned from Cache** | 缓存命中率 | 低 => 检查查询模式，增大 Warehouse 有助数据缓存 |
| **Total Execution Time** | 总执行时间 | 对比各步骤找瓶颈 |

```sql
-- 直接获取查询统计
SELECT * FROM TABLE(INFORMATION_SCHEMA.QUERY_HISTORY())
WHERE QUERY_ID = '<query_id>';

-- 查看最近的慢查询
SELECT QUERY_ID, QUERY_TEXT, EXECUTION_TIME / 1000 AS seconds,
       BYTES_SCANNED / 1024 / 1024 / 1024 AS gb_scanned,
       PARTITIONS_SCANNED,
       PARTITIONS_TOTAL,
       WAREHOUSE_SIZE
FROM TABLE(SNOWFLAKE.ACCOUNT_USAGE.QUERY_HISTORY)
WHERE EXECUTION_STATUS = 'SUCCESS'
  AND START_TIME >= DATEADD('day', -1, CURRENT_TIMESTAMP())
  AND EXECUTION_TIME > 60000  -- 超过 60 秒
ORDER BY EXECUTION_TIME DESC
LIMIT 20;
```

---

## 14. 半结构化数据处理

### 14.1 VARIANT 类型操作

```sql
-- 创建包含 VARIANT 的表
CREATE TABLE event_logs (
    event_id   INTEGER AUTOINCREMENT,
    event_data VARIANT,
    event_ts   TIMESTAMP_LTZ DEFAULT CURRENT_TIMESTAMP()
);

-- 插入 JSON 数据
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

-- 查询 VARIANT 字段
SELECT
    event_data:user.name::STRING AS user_name,
    event_data:user.id::INTEGER AS user_id,
    event_data:action::STRING AS action,
    event_data:total::FLOAT AS total,
    event_data:items[0].product_id::STRING AS first_product,
    event_data:items[0].price::FLOAT AS first_price,
    ARRAY_SIZE(event_data:items) AS item_count
FROM event_logs;

-- 检查类型
SELECT TYPEOF(event_data) FROM event_logs;                              -- VARIANT
SELECT TYPEOF(event_data:user.name) FROM event_logs;                    -- VARIANT
SELECT TYPEOF(event_data:user.name::STRING) FROM event_logs;            -- VARCHAR
SELECT IS_ARRAY(event_data:items) FROM event_logs;                      -- TRUE

-- 更新 VARIANT
UPDATE event_logs
SET event_data = OBJECT_INSERT(event_data, 'status', 'completed', TRUE)
WHERE event_id = 1;
```

---

### 14.2 FLATTEN 函数详解

```sql
-- FLATTEN 语法
FLATTEN(
    INPUT => <expr>,
    PATH => <constant_path>,
    OUTER => TRUE | FALSE,
    RECURSIVE => TRUE | FALSE,
    MODE => OBJECT | ARRAY | BOTH
)

-- 展开 ARRAY
SELECT
    e.event_id,
    f.index,
    f.value AS item,
    f.value:product_id::STRING AS product_id,
    f.value:quantity::INTEGER AS quantity,
    f.value:price::FLOAT AS price
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:items) f;

-- 展开 OBJECT 的键值对
SELECT
    e.event_id,
    f.key,
    f.value
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:user) f;

-- OUTER => TRUE（保留空数组/NULL 的行）
SELECT e.event_id, f.value
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data:items, OUTER => TRUE) f;

-- RECURSIVE => TRUE（递归展开嵌套结构）
SELECT f.*
FROM event_logs e,
LATERAL FLATTEN(input => e.event_data, RECURSIVE => TRUE) f;
```

**FLATTEN 输出列：**

| 列 | 类型 | 说明 |
|----|------|------|
| SEQ | INTEGER | 序列号 |
| KEY | VARCHAR | 键(OBJECT)或索引(ARRAY) |
| PATH | VARCHAR | 到该元素的路径 |
| INDEX | INTEGER | 元素在数组中的索引(0-based) |
| VALUE | VARIANT | 元素的值 |
| THIS | VARIANT | 被展开的元素本身 |

---

### 14.3 PARSE_JSON / PARSE_XML

```sql
-- PARSE_JSON
SELECT PARSE_JSON('{"name": "John", "age": 30}');
SELECT TRY_PARSE_JSON('invalid json');  -- 出错返回 NULL

-- OBJECT_CONSTRUCT (创建对象)
SELECT OBJECT_CONSTRUCT('name', 'John', 'age', 30, 'active', TRUE);

-- OBJECT_KEYS (获取所有键)
SELECT OBJECT_KEYS(PARSE_JSON('{"name":"John","age":30}'));  -- ["name","age"]

-- OBJECT_DELETE / OBJECT_INSERT
SELECT OBJECT_DELETE(PARSE_JSON('{"name":"John","age":30}'), 'age');
SELECT OBJECT_INSERT(PARSE_JSON('{"name":"John"}'), 'age', 30);

-- ARRAY_CONSTRUCT (创建数组)
SELECT ARRAY_CONSTRUCT('a', 'b', 'c');
SELECT ARRAY_CONSTRUCT(1, 2, 3);

-- ARRAY_AGG (聚合为数组)
SELECT department_id, ARRAY_AGG(employee_id) AS emp_ids
FROM employees GROUP BY department_id;

-- 数组操作
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

### 14.4 加载 JSON / Parquet / Avro / XML 数据

**加载 JSON 数据：**

```sql
-- 1. 创建 File Format
CREATE FILE FORMAT json_ff
    TYPE = 'JSON'
    STRIP_OUTER_ARRAY = TRUE    -- 剥离外层数组，每个元素成为独立行
    ALLOW_DUPLICATE = FALSE
    ENABLE_OCTAL = FALSE;

-- 2. 创建目标表
CREATE TABLE json_raw (data VARIANT);

-- 3. 加载
COPY INTO json_raw
FROM @my_stage/json_data/
FILE_FORMAT = (FORMAT_NAME = json_ff);

-- 4. 创建结构化视图
CREATE VIEW vw_parsed_events AS
SELECT
    data:user.id::INTEGER AS user_id,
    data:user.name::STRING AS user_name,
    data:action::STRING AS action,
    data:total::FLOAT AS total,
    data:timestamp::TIMESTAMP AS event_time
FROM json_raw;
```

**加载 Parquet 数据：**

```sql
-- Parquet 直接加载到结构化列
COPY INTO sales_structured (sale_date, product_id, region, amount)
FROM (
    SELECT $1:sale_date::DATE,
           $1:product_id::INTEGER,
           $1:region::STRING,
           $1:amount::NUMBER(12,2)
    FROM @my_stage/parquet_data/
)
FILE_FORMAT = (TYPE = PARQUET);

-- 或加载为 VARIANT
COPY INTO parquet_raw (data)
FROM @my_stage/parquet_data/
FILE_FORMAT = (TYPE = PARQUET);
```

**加载 Avro 数据：**

```sql
COPY INTO avro_raw (data)
FROM @my_stage/avro_data/
FILE_FORMAT = (TYPE = AVRO);
```

**加载 XML 数据：**

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

### 14.5 半结构化数据优化

```sql
-- 1. 提取常查询路径到独立列（物化）
CREATE TABLE events_optimized AS
SELECT
    event_data,
    event_data:user.id::INTEGER AS user_id,
    event_data:action::STRING AS action,
    event_data:total::FLOAT AS total,
    event_data:timestamp::TIMESTAMP AS event_time
FROM event_logs;

-- 2. 在提取列上创建 Clustering
ALTER TABLE events_optimized CLUSTER BY (event_time);

-- 3. 在 VARIANT 列路径上创建 Search Optimization
ALTER TABLE event_logs
    ADD SEARCH OPTIMIZATION ON EQUALITY(event_data:user.id),
    ADD SEARCH OPTIMIZATION ON EQUALITY(event_data:action);

-- 4. 使用 GET_PATH 代替链式访问
SELECT GET_PATH(event_data, 'user.name')::STRING FROM event_logs;
-- 等价于
SELECT event_data:user.name::STRING FROM event_logs;

-- 5. 避免在 VARIANT 上频繁类型转换
-- 不好
SELECT * FROM event_logs WHERE event_data:user.id::INTEGER = 123;
-- 好（如果已在提取列上优化）
SELECT * FROM events_optimized WHERE user_id = 123;
```

---


## 15. UDF / UDTF / 存储过程

### 15.1 SQL UDF 创建与使用

#### 完整语法

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

#### 操作示例

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

### 15.2 JavaScript UDF 创建与使用

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

### 15.3 Python UDF 创建与使用

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

### 15.4 Java UDF 创建与使用

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

### 15.5 UDTF (表函数) 创建与使用

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

### 15.6 JavaScript 存储过程

```sql
CREATE PROCEDURE js_etl_process(table_name VARCHAR, source_stage VARCHAR)
    RETURNS VARCHAR
    LANGUAGE JAVASCRIPT
    EXECUTE AS CALLER
AS $$
    // 执行数据加载
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

-- JavaScript 存储过程中执行查询并遍历结果
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

### 15.7 Python 存储过程

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

### 15.8 SQL 存储过程 (Snowflake Scripting)

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

## 16. Streams & Tasks (流与任务)

### 16.1 Stream 概述与类型

Stream 跟踪表的**增量变更**（INSERT、UPDATE、DELETE），记录自上次消费后所有数据变化。

**三种类型：**

| 类型 | 跟踪内容 | 适用场景 |
|------|----------|----------|
| **Standard (Delta)** | INSERT + UPDATE + DELETE | 完整 CDC（变更数据捕获） |
| **Append-Only** | 仅 INSERT | 追加式数据源（日志、事件） |
| **Insert-Only** | 同 Append-Only（别名） | 同 Append-Only |

**Stream 工作原理：**
- 创建 Stream 时不复制数据，仅记录偏移量（Offset）
- 查询 Stream 时返回自上次偏移量以来变更的行
- 每次 DML 操作后，Stream 自动推进（如果在事务中消费）
- Stream 的偏移量只在显式消费（SELECT/DML 中使用）后才推进

**Stream 输出列：**

| 列 | 说明 |
|----|------|
| `METADATA$ACTION` | 变更类型: INSERT / DELETE |
| `METADATA$ISUPDATE` | 是否 UPDATE（TRUE/FALSE）|
| `METADATA$ROW_ID` | 行的唯一标识 |

---

### 16.2 Standard Stream 操作手顺

**步骤 1: 创建 Stream**

```sql
-- 前提：表需启用 CHANGE_TRACKING
ALTER TABLE employees SET CHANGE_TRACKING = TRUE;

-- 创建 Stream
CREATE STREAM employees_stream ON TABLE employees
    COMMENT = 'Tracks all DML changes on employees';

-- 查看 Stream 状态
SHOW STREAMS;
DESC STREAM employees_stream;
```

**步骤 2: 产生变更（模拟操作）**

```sql
-- 我们会模拟一些变更来看 Stream 的行为
-- INSERT
INSERT INTO employees VALUES (100, 'Alice', 'Wang', 'alice@test.com', '2026-05-12', 85000, 10, TRUE, CURRENT_TIMESTAMP());

-- UPDATE
UPDATE employees SET salary = 90000 WHERE employee_id = 100;

-- DELETE
DELETE FROM employees WHERE employee_id = 100;
```

**步骤 3: 查询 Stream 内容**

```sql
-- 首次查询（在变更后）
SELECT
    employee_id,
    first_name,
    salary,
    METADATA$ACTION,
    METADATA$ISUPDATE,
    METADATA$ROW_ID
FROM employees_stream;
-- 结果包含: INSERT(新版本) + DELETE(旧版本) 各一行（对应 UPDATE）
-- + INSERT 行（INSERT 操作）
-- + DELETE 行（DELETE 操作）
```

**步骤 4: 消费 Stream（执行增量 ETL）**

```sql
-- 增量插入/更新
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

**步骤 5: 验证 Stream 是否清空**

```sql
-- 消费后 Stream 应为空
SELECT COUNT(*) FROM employees_stream;  -- 应为 0
```

---

### 16.3 Append-Only Stream 操作手顺

```sql
-- 创建仅追加 Stream（只跟踪 INSERT，不跟踪 UPDATE/DELETE）
CREATE STREAM employees_append_stream ON TABLE employees
    APPEND_ONLY = TRUE;

-- 仅返回 INSERT 的行，无 METADATA$ISUPDATE
SELECT * FROM employees_append_stream;
```

---

### 16.4 Task 创建与管理

Task 是定期执行的调度单元，可用于构建定时 ETL 管道。

**Serverless Task vs Warehouse Task：**

| 特性 | Warehouse Task | Serverless Task |
|------|---------------|-----------------|
| 计算资源 | 指定 Warehouse | Snowflake 管理 |
| 计费 | Warehouse credits | Cloud Services credits |
| 适用 | 长时间/重量级任务 | 短/轻量级任务 |

```sql
-- 创建 Warehouse Task
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

-- 创建 Serverless Task
CREATE TASK serverless_cleanup
    USER_TASK_MANAGED_INITIAL_WAREHOUSE_SIZE = 'XSMALL'
    SCHEDULE = 'USING CRON 0 2 * * * UTC'  -- 每天凌晨 2 点 UTC
AS
    DELETE FROM temp_logs
    WHERE created_at < DATEADD('day', -7, CURRENT_TIMESTAMP());

-- 创建 Task DAG（依赖链）
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

-- 管理 Task
ALTER TASK hourly_employee_sync SUSPEND;
ALTER TASK hourly_employee_sync RESUME;
ALTER TASK hourly_employee_sync SET SCHEDULE = '30 MINUTE';

-- 手动执行 Task
EXECUTE TASK hourly_employee_sync;

-- 查看 Task 执行历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.TASK_HISTORY(
    TASK_NAME => 'HOURLY_EMPLOYEE_SYNC',
    SCHEDULED_TIME_RANGE_START => DATEADD('day', -7, CURRENT_TIMESTAMP())
));

-- 查看 Task DAG 依赖
SELECT * FROM TABLE(INFORMATION_SCHEMA.TASK_DEPENDENTS(
    TASK_NAME => 'TASK_STEP1',
    RECURSIVE => TRUE
));

-- 删除 Task
DROP TASK hourly_employee_sync;
```

---

### 16.5 Stream + Task 构建增量 ETL 管道

**完整示例：构建 CDC 管道**

```sql
-- === 步骤 1: 环境准备 ===
CREATE DATABASE cdc_demo;
CREATE SCHEMA cdc_demo.etl;
USE SCHEMA cdc_demo.etl;

-- === 步骤 2: 创建源表和目标表 ===
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

-- 启用源表变更跟踪
ALTER TABLE source_orders SET CHANGE_TRACKING = TRUE;

-- === 步骤 3: 创建 Stream ===
CREATE STREAM orders_stream ON TABLE source_orders;

-- === 步骤 4: 创建增量加载存储过程 ===
CREATE PROCEDURE incremental_load()
    RETURNS VARCHAR
    LANGUAGE SQL
    EXECUTE AS OWNER
AS
$$
DECLARE
    processed_count INTEGER;
BEGIN
    -- 关闭现有记录
    UPDATE orders_history h
    SET valid_to = CURRENT_TIMESTAMP(), is_current = FALSE
    FROM orders_stream s
    WHERE h.order_id = s.order_id
      AND h.is_current = TRUE
      AND s.METADATA$ACTION IN ('INSERT', 'DELETE');

    -- 插入新版本（INSERT 操作 + UPDATE 的新版本）
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

    -- 标记已删除的记录（排除 UPDATE 产生的 DELETE 行）
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

-- === 步骤 5: 创建调度 Task ===
CREATE TASK cdc_orders_sync
    WAREHOUSE = etl_wh
    SCHEDULE = '5 MINUTE'
    WHEN SYSTEM$STREAM_HAS_DATA('orders_stream')
AS
    CALL incremental_load();

-- 激活 Task
ALTER TASK cdc_orders_sync RESUME;

-- === 步骤 6: 验证 ===
-- 插入测试数据
INSERT INTO source_orders VALUES (1, 100, 99.99, 'PENDING', CURRENT_TIMESTAMP());
INSERT INTO source_orders VALUES (2, 200, 149.99, 'PENDING', CURRENT_TIMESTAMP());

-- 手动触发 Task 执行（测试用）
EXECUTE TASK cdc_orders_sync;

-- 验证目标表
SELECT * FROM orders_history;

-- 模拟 UPDATE 和 DELETE
UPDATE source_orders SET status = 'COMPLETED' WHERE order_id = 1;
DELETE FROM source_orders WHERE order_id = 2;

EXECUTE TASK cdc_orders_sync;

-- 查看完整的变更历史
SELECT * FROM orders_history
ORDER BY order_id, valid_from;
```

---

## 17. 账户与资源管理

### 17.1 账户结构

Snowflake 的账户结构概览：

```
Organization
 +-- Account 1 (例如: prod)
 |    +-- Database / Schema / Table ...
 |    +-- User / Role / Warehouse ...
 +-- Account 2 (例如: dev)
 |    +-- Database / Schema / Table ...
 +-- Account 3 (例如: analytics)
      +-- ...
```

---

### 17.2 Resource Monitor 创建与使用

Resource Monitor 用于控制 Warehouse 的 Credit 消耗。

```sql
-- 创建 Resource Monitor
CREATE RESOURCE MONITOR monthly_budget
    WITH
        CREDIT_QUOTA = 1000                 -- 月度 credit 配额
        FREQUENCY = MONTHLY                 -- 评估频率
        START_TIMESTAMP = IMMEDIATELY       -- 立即开始
        TRIGGERS
            ON 80 PERCENT DO NOTIFY         -- 80% 时通知
            ON 90 PERCENT DO NOTIFY         -- 90% 时通知
            ON 100 PERCENT DO SUSPEND       -- 100% 时暂停所有关联 WH
            ON 110 PERCENT DO SUSPEND_IMMEDIATE;  -- 110% 立即暂停

-- 关联到 Warehouse
ALTER WAREHOUSE analyst_wh SET RESOURCE_MONITOR = monthly_budget;
ALTER WAREHOUSE etl_wh SET RESOURCE_MONITOR = monthly_budget;

-- 查看 Resource Monitor
SHOW RESOURCE MONITORS;
DESC RESOURCE MONITOR monthly_budget;

-- 查看 credit 使用情况
SELECT * FROM SNOWFLAKE.ACCOUNT_USAGE.METERING_HISTORY
WHERE START_TIME >= DATE_TRUNC('MONTH', CURRENT_DATE());

-- 修改
ALTER RESOURCE MONITOR monthly_budget SET CREDIT_QUOTA = 2000;

-- 删除
DROP RESOURCE MONITOR monthly_budget;
```

---

### 17.3 信息模式与 Account Usage

**INFORMATION_SCHEMA (当前数据库)：**

```sql
-- 查看当前数据库下的表
SELECT * FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'PUBLIC';

-- 查看列信息
SELECT * FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'EMPLOYEES';

-- 查看 COPY 历史
SELECT * FROM TABLE(INFORMATION_SCHEMA.COPY_HISTORY(
    TABLE_NAME => 'EMPLOYEES',
    START_TIME => DATEADD('day', -1, CURRENT_TIMESTAMP())
));

-- 查看表存储大小
SELECT * FROM INFORMATION_SCHEMA.TABLE_STORAGE_METRICS
WHERE TABLE_NAME = 'EMPLOYEES';
```

**ACCOUNT_USAGE (跨数据库，1-3小时延迟)：**

```sql
-- Warehouse 使用统计
SELECT WAREHOUSE_NAME,
       SUM(CREDITS_USED) AS total_credits,
       SUM(CREDITS_USED_COMPUTE) AS compute_credits,
       SUM(CREDITS_USED_CLOUD_SERVICES) AS cloud_credits
FROM SNOWFLAKE.ACCOUNT_USAGE.WAREHOUSE_METERING_HISTORY
WHERE START_TIME >= DATEADD('day', -30, CURRENT_DATE())
GROUP BY 1
ORDER BY 2 DESC;

-- 查询历史（找慢查询）
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

-- 存储使用
SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME,
       ACTIVE_BYTES / 1024 / 1024 / 1024 AS active_gb,
       TIME_TRAVEL_BYTES / 1024 / 1024 / 1024 AS tt_gb,
       FAILSAFE_BYTES / 1024 / 1024 / 1024 AS fs_gb
FROM SNOWFLAKE.ACCOUNT_USAGE.TABLE_STORAGE_METRICS
WHERE TABLE_CATALOG = 'TRAINING_DB'
ORDER BY ACTIVE_BYTES DESC;

-- 登录历史
SELECT USER_NAME, EVENT_TIMESTAMP, FIRST_AUTHENTICATION_FACTOR,
       CLIENT_IP, REPORTED_CLIENT_TYPE
FROM SNOWFLAKE.ACCOUNT_USAGE.LOGIN_HISTORY
WHERE EVENT_TIMESTAMP >= DATEADD('day', -7, CURRENT_DATE())
ORDER BY EVENT_TIMESTAMP DESC;

-- 权限审计
SELECT * FROM SNOWFLAKE.ACCOUNT_USAGE.GRANTS_TO_USERS
WHERE GRANTEE_NAME = 'JOHN_DOE'
  AND DELETED_ON IS NULL;
```

---

### 17.4 成本管理

**成本优化策略：**

| 策略 | 操作 | 预期节省 |
|------|------|----------|
| **AUTO_SUSPEND** | 设置合理的自动暂停时间 (60-300s) | 空闲时间 100% 节省 |
| **AUTO_RESUME** | 保持 TRUE | 无需手动管理 |
| **Warehouse Size** | 选择合适大小 | 大 WH 更短时间 = 相同 cost |
| **结果缓存** | 利用 24h 结果缓存 | 重复查询 0 成本 |
| **Resource Monitor** | 设置预算告警和自动暂停 | 防止意外超支 |
| **Transient 表** | 临时/中转数据用瞬态表 | 省 7 天 Fail-safe 存储费 |
| **克隆** | 开发/测试环境用克隆 | 不额外占用存储 |

---

## 18. 最佳实践与常见问题

### 18.1 表设计最佳实践

1. **选择合适的表类型：**
   - 生产数据 -> 永久表
   - 临时计算 -> 临时表
   - ETL 中间数据 -> 瞬态表（省钱）

2. **选择合适的数据类型：**
   - 优先使用 `VARCHAR` 而非 `CHAR`（存储效率相同，灵活度更高）
   - 日期时间优先使用 `TIMESTAMP_LTZ`（跨时区友好）
   - 半结构化数据用 `VARIANT`（无需预先定义 Schema）

3. **Clustering 策略：**
   - 仅对 TB 级以上且查询模式不匹配自然排序的表
   - 选择基数适中的列（不高不低）
   - 高基数列在前（如 timestamp），低基数列在后（如 region）

4. **约束使用：**
   - PRIMARY KEY / UNIQUE / FOREIGN KEY 仅作文档用途，不强制
   - 如需强制唯一性，需在应用层实现

---

### 18.2 查询优化最佳实践

1. **利用 WHERE 过滤减少扫描量**
2. **避免 SELECT \*** ，只选需要的列
3. **使用 LIMIT 限制结果集**
4. **优先使用 CTE 而非重复子查询**
5. **大表 JOIN 时先过滤再 JOIN**
6. **利用 QUALIFY 替代子查询处理窗口函数**
7. **对重复查询利用结果缓存**

```sql
-- 不好
SELECT * FROM sales_fact;
SELECT * FROM sales_fact WHERE YEAR(sale_date) = 2026;

-- 好
SELECT sale_date, amount FROM sales_fact LIMIT 1000;
SELECT * FROM sales_fact WHERE sale_date BETWEEN '2026-01-01' AND '2026-12-31';
```

---

### 18.3 数据加载最佳实践

1. **文件大小：** 推荐 100-250 MB（压缩后）的文件。避免过小（增加元数据开销）或过大（无法并行）
2. **并行加载：** 使用多个 COPY 语句加载不同文件
3. **先校验：** 使用 `VALIDATION_MODE` 预览数据再实际加载
4. **增量加载：** 使用 Snowpipe 或 Stream+Task 实现增量
5. **Schema 演化：** 使用 `MATCH_BY_COLUMN_NAME` 和 `ENABLE_SCHEMA_EVOLUTION` 自动处理列变更

---

### 18.4 安全最佳实践

1. **最小权限原则：** 只授予必需的权限
2. **使用角色继承：** 建立清晰的角色层级
3. **默认禁用用户：** 新用户创建后 `DISABLED = TRUE`，审批后启用
4. **Key Pair 认证：** 生产系统使用 Key Pair 替代密码
5. **网络策略：** 限制 IP 白名单
6. **启用 MFA：** 所有人类用户启用多因素认证
7. **敏感数据脱敏：** 使用 Masking Policy
8. **审计查询：** 定期审查 `LOGIN_HISTORY` 和 `QUERY_HISTORY`

---

### 18.5 成本控制最佳实践

1. **设置合适的 AUTO_SUSPEND：** 开发 WH 60-120s，ETL WH 300-600s
2. **使用 Resource Monitor：** 设置多级告警（80%/90%/100%）
3. **选择合适的 Warehouse Size：** 不要在 X-Small 上跑大数据量
4. **监控 Cloud Services 成本：** 大量小查询会推高 Cloud Services 费用
5. **清理无用对象：** 定期清理旧数据、临时表、过期克隆

---

### 18.6 常见问题排查

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| **查询慢** | 扫描过多微分区 | 添加 WHERE 过滤或 Clustering |
| **查询排队** | Warehouse 并发达到上限 | 增加 MAX_CONCURRENCY_LEVEL 或使用 Multi-cluster |
| **Spilling** | Warehouse 太小无法容纳操作 | 增大 Warehouse Size |
| **COPY 加载失败** | 文件格式不匹配 | 使用 VALIDATION_MODE 校验，检查 NULL_IF 设置 |
| **数据丢失** | 误删表/数据库 | 使用 UNDROP 或 Time Travel 恢复 |
| **权限不足** | 缺少必要权限 | 检查角色和权限授予 |
| **Snowpipe 不加载** | Pipe 暂停或 SNS 通知失效 | 检查 PIPE_EXECUTION_PAUSED 和 SNS 配置 |
| **Stream 为空** | Stream 已被消费或表无变更 | 检查 CHANGE_TRACKING 设置 |
| **结果缓存不命中** | 底层数据变更或查询不完全相同 | 检查 USE_CACHED_RESULT 设置 |
| **费用过高** | Warehouse 未自动暂停或过大 | 检查 AUTO_SUSPEND 设置和使用模式 |

---

> **手册结束**
>
> 本手册涵盖了 Snowflake 的核心知识点，从架构概述到详细的 DDL/DML 语法，从数据加载到性能优化，从安全管理到最佳实践。建议新成员按章节顺序学习，并在 Snowflake 环境中动手实践每个示例。
>
> **推荐后续学习资源：**
> - [Snowflake 官方文档](https://docs.snowflake.com/)
> - [Snowflake Hands-On Labs](https://quickstarts.snowflake.com/)
> - [Snowflake Community](https://community.snowflake.com/)
