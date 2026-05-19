-- ============================================
-- Snowflake 关联 GitLab 公共仓库
-- ============================================

USE DATABASE test_snowflake_leaning;

-- 步骤 1：创建 API Integration
CREATE OR REPLACE API INTEGRATION my_gitlab_integration
  API_PROVIDER = git_https_api
  API_ALLOWED_PREFIXES = ('https://jihulab.com/ps892180440-group')
  ENABLED = TRUE;

-- 步骤 2：创建 Git Repository
CREATE OR REPLACE GIT REPOSITORY my_gitlab_repo
  API_INTEGRATION = my_gitlab_integration
  ORIGIN = 'https://jihulab.com/ps892180440-group/ps892180440-project.git';

-- ============================================
-- 常用 Git Repository 操作
-- ============================================

-- 拉取最新内容
ALTER GIT REPOSITORY my_gitlab_repo FETCH;

-- 查看仓库中的分支和标签
SHOW GIT BRANCHES IN my_gitlab_repo;
SHOW GIT TAGS IN my_gitlab_repo;

-- 列出仓库某分支下的文件
LS @my_gitlab_repo/branches/main/;

-- 查看文件内容（例如查看 README）
SELECT $1 FROM @my_gitlab_repo/branches/main/README.md;

-- ============================================
-- （可选）如果后续仓库改为私有，需添加认证
-- ============================================

-- 创建 Secret（使用 GitLab Personal Access Token）
CREATE OR REPLACE SECRET my_gitlab_secret
  TYPE = password
  USERNAME = 'ps892180440'
  PASSWORD = '1ccPJrmC4sT-x0RJAjoNi286MQp1OjU3aXEK.01.100ldt1oe';

-- 创建带认证的 API Integration（PAT 方式）
CREATE OR REPLACE API INTEGRATION my_gitlab_integration_private
  API_PROVIDER = git_https_api
  API_ALLOWED_PREFIXES = ('https://jihulab.com/ps892180440-group')
  ALLOWED_AUTHENTICATION_SECRETS = (my_gitlab_secret)
  ENABLED = TRUE;

-- 创建带认证的 Git Repository
CREATE OR REPLACE GIT REPOSITORY my_private_repo
  API_INTEGRATION = my_gitlab_integration_private
  GIT_CREDENTIALS = my_gitlab_secret
  ORIGIN = 'https://jihulab.com/ps892180440-group/ps892180440-project.git';

-- ============================================
-- （可选）OAuth2 方式关联 GitLab
-- ============================================

CREATE OR REPLACE API INTEGRATION my_gitlab_oauth_integration
  API_PROVIDER = git_https_api
  API_ALLOWED_PREFIXES = ('https://jihulab.com/ps892180440-group')
  API_USER_AUTHENTICATION = (
    TYPE = OAUTH2
    OAUTH_AUTHORIZATION_ENDPOINT = 'https://gitlab.com/oauth/authorize'
    OAUTH_TOKEN_ENDPOINT = 'https://gitlab.com/oauth/token'
    OAUTH_CLIENT_ID = 'fe1a001d7ef23c961f0f413bfe8fae22e7490adaf0bed61953470a2ef61aa8af'
    OAUTH_CLIENT_SECRET = 'gloas-013c3289e1c3f04b1edcfb938ef5a33dfbdf5faedd5e94d99c454ec83b349bd4'
    OAUTH_ACCESS_TOKEN_VALIDITY = 28800
    OAUTH_ALLOWED_SCOPES = ('read_api', 'read_repository', 'write_repository')
  )
  ENABLED = TRUE;
