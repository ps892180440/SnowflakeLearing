-- ============================================================
-- Workspace 连接 Git 仓库（用于推送）所需的准备 SQL
-- 执行完成后，在 Workspace UI 中选择 "From Git repository" 创建 Git Workspace
-- ============================================================

-- Step 1: 创建 Secret（存储 GitHub Personal Access Token）
-- 注意：需要将 'your_github_username' 和 'ghp_xxxx' 替换为你的实际值
-- GitHub PAT 需要 repo 权限（Settings > Developer settings > Personal access tokens）
CREATE OR REPLACE SECRET TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.GIT_SECRET_FOR_PUSH
  TYPE = PASSWORD
  USERNAME = 'ps892180440'
  PASSWORD = '***********************************';

-- Step 2: 创建支持推送的 API Integration
CREATE OR REPLACE API INTEGRATION GITHUB_PUSH_INTEGRATION
  API_PROVIDER = git_https_api
  API_ALLOWED_PREFIXES = ('https://github.com/ps892180440/')
  ALLOWED_AUTHENTICATION_SECRETS = (TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.GIT_SECRET_FOR_PUSH)
  ENABLED = TRUE;

-- Step 3: 创建完成后，在 Workspace 界面操作：
-- 1. Projects > Workspaces > 选择 "From Git repository"
-- 2. Repository URL 填写: https://github.com/ps892180440/SnowflakeLearing.git
-- 3. API Integration 选择: GITHUB_PUSH_INTEGRATION
-- 4. Authentication 选择: Personal access token
-- 5. Secret 选择: GIT_SECRET_FOR_PUSH
-- 6. 点击 Create
--
-- 连接成功后：
-- 1. 将 TEST_SNOWFLAKE_LEANING.sql 等文件复制到该 Git Workspace
-- 2. 点击 Changes 标签
-- 3. 写 commit message 然后点击 Push
