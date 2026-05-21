# SnowflakeLearing
For Snowflake Learing
<p align="center">
  <img src="https://www.vectorlogo.zone/logos/snowflake/snowflake-ar21.svg" alt="Snowflake Logo" width="360"/>
</p>

<p align="center">
  <a href="https://docs.snowflake.com/en/"><img src="https://img.shields.io/badge/Snowflake-Enterprise-blue?style=for-the-badge&logo=snowflake&logoColor=white" alt="Snowflake Enterprise"/></a>
  <a href="https://github.com/dbt-labs/dbt-core"><img src="https://img.shields.io/badge/dbt-1.9+-orange?style=for-the-badge&logo=dbt&logoColor=white" alt="dbt 1.9+"/></a>
  <a href="https://github.com/features/actions"><img src="https://img.shields.io/badge/CI/CD-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions"/></a>
  <a href="https://www.python.org/"><img src="https://img.shields.io/badge/Python-3.10+-3776AB?style=for-the-badge&logo=python&logoColor=white" alt="Python 3.10+"/></a>
</p>

<p align="center">
  <b>⚡ Modern Data Warehouse — クラウドネイティブ · dbt Transformation · CI/CD Automation</b>
</p>

---

##  Architecture Overview

```mermaid
graph TB
    subgraph Sources[" Data Sources"]
        OLTP[(OLTP<br/>Databases)]
        API[("REST / gRPC<br/>APIs")]
        Files[("CSV · JSON<br/>Parquet")]
        Stream[("Kafka / Kinesis<br/>Event Streams")]
    end

    subgraph Ingest[" Ingest"]
        Pipe["Snowpipe<br/>Auto-Ingest"]
        Stage["External Stage<br/>S3 / GCS / Azure Blob"]
        Connector["SDK & Connectors<br/>Python · Spark · Kafka"]
    end

    subgraph Snowflake["❄ Snowflake Cloud Data Platform"]
        subgraph Compute["Compute Layer"]
            VWH["Virtual Warehouse<br/>(Elastic Compute)"]
            Serverless["Serverless Compute<br/>(Tasks · Pipes · UDFs)"]
        end
        subgraph Storage["Storage Layer"]
            CStore["Centralized Storage<br/>(Columnar · Compressed)"]
        end
        subgraph Services["Services Layer"]
            Auth["Authentication<br/>OAuth · SAML · Key-Pair"]
            Meta["Metadata<br/>Query Optimizer · Stats"]
            Security["Security<br/>RBAC · Dynamic Masking"]
        end
    end

    subgraph Transform["⚙️ Transformation"]
        dbt["dbt Core<br/>Models · Tests · Docs"]
        SP["Stored Procedures<br/>Snowpark Python"]
        Jinja["Jinja Templates<br/>Macros · Hooks"]
    end

    subgraph Output[" Output"]
        BI["BI Tools<br/>Tableau · Looker · Metabase"]
        DS["Data Science<br/>Python · R · ML"]
        ReverseETL["Reverse ETL<br/>Hightouch · Census"]
    end

    Sources --> Ingest
    Ingest --> Compute
    Compute --> Storage
    Services --> Compute
    Storage --> Transform
    Transform --> Output

    style Snowflake fill:#29B5E8,stroke:#1A7BA0,color:#fff
    style Compute fill:#115584,color:#fff
    style Storage fill:#115584,color:#fff
    style Services fill:#115584,color:#fff
    style dbt fill:#FF694B,color:#fff
```

##  Key Features

| Category | Capability |
|----------|------------|
|  **Separation** | Compute & storage scale independently — pay only for what you use |
|  **Zero-Copy Cloning** | Instant database/schema/table clones without extra storage |
|  **Time Travel** | Roll back up to 90 days; recover from accidental DROP / DELETE |
|  **Semi-structured Data** | Native VARIANT type — query JSON / Avro / Parquet with SQL |
|  **Secure Data Sharing** | Share live data across accounts without copying |
|  **Multi-Cloud** | AWS · Azure · GCP — same experience everywhere |
|  **Dynamic Masking** | Column-level security policy; no view boilerplate |
|  **Search Optimization** | Sub-second point lookups via pruning indexes |

## Repository Knowledge Base

This repository contains hands-on technical documentation covering Snowflake + dbt full-stack development. Navigate by role or topic below.

###  Start Here

| Document | Description | Audience |
|----------|-------------|----------|
| [**Snowflake Training Manual**](snowflake-training-manual.md) | ゼロから学ぶ Snowflake コア技術 | 新規メンバー / 入門者 |
| [**dbt 全面指南**](snowflake_dbt.md) | dbt Core 全貌 — Models · Tests · Snapshots · Seeds | Data Engineers |
| [**Jinja 过滤器 & 函数参考**](snowflake_dbt_jinja_reference.md) | dbt Jinja 完整リファレンス | Analytics Engineers |
| [**CI/CD 实施指南**](snowflake-dbt_cicd_guide.md) | dbt + Snowflake CI/CD 实战 (GitHub Actions) | DevOps / Platform |

###  Learning Path

```mermaid
flowchart LR
    A[" Snowflake<br/>Training Manual"] --> B[" dbt<br/>全面指南"]
    B --> C[" Jinja<br/>Reference"]
    B --> D[" CI/CD<br/>Guide"]

    style A fill:#29B5E8,stroke:#1A7BA0,color:#fff
    style B fill:#FF694B,stroke:#B8472E,color:#fff
    style C fill:#47D7AC,stroke:#2EA87F,color:#000
    style D fill:#FEC84B,stroke:#C7992A,color:#000
```

###  Core Stack

<p align="center">
  <table>
    <tr>
      <td align="center" width="33%">
        <img src="https://www.vectorlogo.zone/logos/snowflake/snowflake-icon.svg" width="80"/><br/>
        <b>Snowflake</b><br/>
        Cloud Data Platform<br/>
        <sub>Enterprise Edition ↑</sub>
      </td>
      <td align="center" width="33%">
        <img src="https://www.vectorlogo.zone/logos/getdbt/getdbt-icon.svg" width="80"/><br/>
        <b>dbt Core</b><br/>
        Data Transformation<br/>
        <sub>v1.9+ · dbt-snowflake</sub>
      </td>
      <td align="center" width="33%">
        <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/120px-Python-logo-notext.svg.png" width="80"/><br/>
        <b>Python Ecosystem</b><br/>
        Snowpark · Pandas · Jinja<br/>
        <sub>3.10+</sub>
      </td>
    </tr>
  </table>
</p>

##  Quick Start

### Prerequisites

```bash
# Python 3.10+
python --version

# dbt-snowflake
pip install dbt-snowflake

# SnowSQL CLI (optional)
curl -O https://sfc-repo.snowflakecomputing.com/snowsql/bootstrap/1.2/linux_x86_64/snowsql-1.2.33-linux_x86_64.bash
```

###  Project Structure (dbt)

```
dbt_project/
├── models/
│   ├── staging/        # Source-normalized views
│   ├── intermediate/   # Business logic composition
│   └── marts/          # Business-facing tables
├── tests/              # Custom data tests
├── seeds/              # Static CSV reference data
├── snapshots/          # Slowly Changing Dimensions
├── macros/             # Reusable Jinja blocks
├── analyses/           # Ad-hoc SQL
├── dbt_project.yml     # Project config
└── profiles.yml        # Connection config
```

###  CI/CD Pipeline

```
PR Open → dbt build (dev) → dbt test → Lint / Format
    ↓
Merge → dbt build (prod) → Run dbt snapshots → Notify
```

##  Commands Quick Reference

| Command | Purpose |
|---------|---------|
| `snowsql -c <connection>` | Connect via CLI |
| `put file://data.csv @my_stage` | Upload to internal stage |
| `copy into tbl from @my_stage` | Bulk load from stage |
| `create or replace table t clone t_prod` | Zero-copy clone |
| `undrop table t` | Recover dropped table |
| `select * from t at(timestamp => ...)` | Time Travel query |
| `dbt run` | Execute all models |
| `dbt test` | Run all tests |
| `dbt build` | run + test + seed + snapshot |
| `dbt docs generate && dbt docs serve` | Documentation portal |

## 📖 External Resources

- [Snowflake Documentation](https://docs.snowflake.com/)
- [dbt Documentation](https://docs.getdbt.com/)
- [Jinja Template Designer Docs](https://jinja.palletsprojects.com/en/3.1.x/templates/)
- [Snowflake Architecture Whitepaper](https://www.snowflake.com/resource/sigmod-2016-the-snowflake-elastic-data-warehouse/)

---

<p align="center">
  <sub>Built with ❄ for fast-paced data teams · Updated 2026-05-21</sub>
</p>

