ALTER TABLE users ADD COLUMN password_hash VARCHAR(255);
ALTER TABLE users ADD COLUMN role VARCHAR(32) NOT NULL DEFAULT 'USER';

CREATE INDEX idx_users_email ON users(email);

