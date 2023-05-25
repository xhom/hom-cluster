-- used in tests that use MYSQL
CREATE TABLE oauth_client_details
(
    client_id               VARCHAR(128) PRIMARY KEY,
    resource_ids            VARCHAR(128),
    client_secret           VARCHAR(128),
    scope                   VARCHAR(128),
    authorized_grant_types  VARCHAR(128),
    web_server_redirect_uri VARCHAR(128),
    authorities             VARCHAR(128),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(128)
);

CREATE TABLE oauth_client_token
(
    token_id          VARCHAR(128),
    token             BLOB,
    authentication_id VARCHAR(128) PRIMARY KEY,
    user_name         VARCHAR(128),
    client_id         VARCHAR(128)
);

CREATE TABLE oauth_access_token
(
    token_id          VARCHAR(128),
    token             BLOB,
    authentication_id VARCHAR(128) PRIMARY KEY,
    user_name         VARCHAR(128),
    client_id         VARCHAR(128),
    authentication    BLOB,
    refresh_token     VARCHAR(128)
);


CREATE TABLE oauth_refresh_token
(
    token_id       VARCHAR(128),
    token          BLOB,
    authentication BLOB
);


CREATE TABLE oauth_code
(
    CODE           VARCHAR(128),
    authentication BLOB
);

CREATE TABLE oauth_approvals
(
    userId         VARCHAR(128),
    clientId       VARCHAR(128),
    scope          VARCHAR(128),
    STATUS         VARCHAR(10),
    expiresAt      TIMESTAMP,
    lastModifiedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- customized oauth_client_details table
CREATE TABLE ClientDetails
(
    appId                  VARCHAR(128) PRIMARY KEY,
    resourceIds            VARCHAR(128),
    appSecret              VARCHAR(128),
    scope                  VARCHAR(128),
    grantTypes             VARCHAR(128),
    redirectUrl            VARCHAR(128),
    authorities            VARCHAR(128),
    access_token_validity  INTEGER,
    refresh_token_validity INTEGER,
    additionalInformation  VARCHAR(4096),
    autoApproveScopes      VARCHAR(128)
);