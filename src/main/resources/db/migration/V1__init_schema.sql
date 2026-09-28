-- =========================================================
-- Cogniva Softwares - initial schema
-- =========================================================

CREATE TABLE admin_users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(120) NOT NULL,
    role          VARCHAR(30)  NOT NULL DEFAULT 'ADMIN',
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE contact_enquiries (
    id           BIGSERIAL PRIMARY KEY,
    full_name    VARCHAR(120)  NOT NULL,
    email        VARCHAR(160)  NOT NULL,
    phone        VARCHAR(30),
    company      VARCHAR(160),
    project_type VARCHAR(80)   NOT NULL,
    message      VARCHAR(5000) NOT NULL,
    status       VARCHAR(20)   NOT NULL DEFAULT 'NEW',
    admin_notes  VARCHAR(2000),
    source_ip    VARCHAR(64),
    user_agent   VARCHAR(512),
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_enquiry_status CHECK (status IN ('NEW', 'IN_PROGRESS', 'CONTACTED', 'CLOSED', 'SPAM'))
);

CREATE INDEX idx_enquiries_status     ON contact_enquiries (status);
CREATE INDEX idx_enquiries_created_at ON contact_enquiries (created_at DESC);
CREATE INDEX idx_enquiries_email      ON contact_enquiries (LOWER(email));

CREATE TABLE service_offerings (
    id            BIGSERIAL PRIMARY KEY,
    slug          VARCHAR(120) NOT NULL UNIQUE,
    icon          VARCHAR(60)  NOT NULL,
    accent        VARCHAR(30)  NOT NULL DEFAULT 'sky',
    title         VARCHAR(160) NOT NULL,
    tagline       VARCHAR(255),
    description   TEXT         NOT NULL,
    highlights    JSONB        NOT NULL DEFAULT '[]'::jsonb,
    tech_stack    JSONB        NOT NULL DEFAULT '[]'::jsonb,
    ideal_for     JSONB        NOT NULL DEFAULT '[]'::jsonb,
    display_order INT          NOT NULL DEFAULT 0,
    published     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_services_published_order ON service_offerings (published, display_order);

CREATE TABLE projects (
    id            BIGSERIAL PRIMARY KEY,
    slug          VARCHAR(160) NOT NULL UNIQUE,
    title         VARCHAR(200) NOT NULL,
    industry      VARCHAR(160),
    scope         VARCHAR(500),
    description   TEXT         NOT NULL,
    tags          JSONB        NOT NULL DEFAULT '[]'::jsonb,
    accent        VARCHAR(30)  NOT NULL DEFAULT 'sky',
    live_url      VARCHAR(255),
    stat1         JSONB,
    stat2         JSONB,
    summary       TEXT,
    challenge     TEXT,
    approach      TEXT,
    solution      JSONB        NOT NULL DEFAULT '[]'::jsonb,
    display_order INT          NOT NULL DEFAULT 0,
    published     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_projects_published_order ON projects (published, display_order);
