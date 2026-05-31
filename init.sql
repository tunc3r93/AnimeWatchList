-- ============================================
-- Complete Database Schema - Anime Watchlist
-- ============================================

-- Tenants Table
CREATE TABLE tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_tenant_id ON users(tenant_id);

-- Anime Table
CREATE TABLE anime (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    cover_url VARCHAR(500),
    genre VARCHAR(500),
    episode_count INTEGER,
    status VARCHAR(50) NOT NULL,
    release_year INTEGER,
    avg_rating DECIMAL(3, 2),
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_anime_tenant_id ON anime(tenant_id);

-- Watchlist Entries
CREATE TABLE watchlist_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    anime_id UUID NOT NULL REFERENCES anime(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, anime_id)
);

CREATE INDEX idx_watchlist_entries_user_id ON watchlist_entries(user_id);
CREATE INDEX idx_watchlist_entries_anime_id ON watchlist_entries(anime_id);

-- Ratings
CREATE TABLE ratings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    anime_id UUID NOT NULL REFERENCES anime(id) ON DELETE CASCADE,
    score INTEGER NOT NULL CHECK (score >= 1 AND score <= 10),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, anime_id)
);

CREATE INDEX idx_ratings_user_id ON ratings(user_id);
CREATE INDEX idx_ratings_anime_id ON ratings(anime_id);

-- Invitations
CREATE TABLE invitations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(255) UNIQUE NOT NULL,
    invited_email VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    created_by_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    used_by_id UUID REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_invitations_code ON invitations(code);
CREATE INDEX idx_invitations_tenant_id ON invitations(tenant_id);

-- ============================================
-- Sample Data
-- ============================================

INSERT INTO tenants (name, slug) VALUES ('Anime Fans JP', 'anime-fans-jp');

-- Default admin user (password: admin123)
INSERT INTO users (email, password, first_name, last_name, role, tenant_id) VALUES (
    'admin@example.com',
    'admin123',
    'Admin',
    'User',
    'ADMIN',
    (SELECT id FROM tenants LIMIT 1)
);

-- Sample Anime
INSERT INTO anime (title, description, cover_url, genre, episode_count, status, release_year, tenant_id) VALUES
('Attack on Titan', 'Humans fight giant monsters in a walled city', 'https://via.placeholder.com/300x400', 'Action, Fantasy, Shounen', 139, 'COMPLETED', 2013, (SELECT id FROM tenants LIMIT 1)),
('Death Note', 'A genius student finds a notebook that can kill anyone', 'https://via.placeholder.com/300x400', 'Psychological, Thriller', 37, 'COMPLETED', 2006, (SELECT id FROM tenants LIMIT 1)),
('My Hero Academia', 'Students train to become superheroes', 'https://via.placeholder.com/300x400', 'Action, School, Shounen', 139, 'AIRING', 2016, (SELECT id FROM tenants LIMIT 1)),
('Demon Slayer', 'A young man fights demons to save his sister', 'https://via.placeholder.com/300x400', 'Action, Supernatural', 50, 'AIRING', 2019, (SELECT id FROM tenants LIMIT 1)),
('Steins;Gate', 'A group discovers time travel', 'https://via.placeholder.com/300x400', 'Sci-Fi, Thriller', 24, 'COMPLETED', 2011, (SELECT id FROM tenants LIMIT 1));

-- ============================================
-- Sample Invitation Code for Testing
-- ============================================

INSERT INTO invitations (code, invited_email, role, tenant_id, created_by_id, expires_at) VALUES
('TEST123CODE', 'newuser@example.com', 'USER', (SELECT id FROM tenants LIMIT 1),
 (SELECT id FROM users WHERE email = 'admin@example.com' LIMIT 1),
 CURRENT_TIMESTAMP + INTERVAL '30 days');
