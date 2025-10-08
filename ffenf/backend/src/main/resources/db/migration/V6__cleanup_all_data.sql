-- Cleanup script to clear all data while keeping table structure
-- This will give a fresh start while maintaining all functionality

-- Clear all data tables (in dependency order to avoid foreign key issues)
TRUNCATE TABLE answers CASCADE;
TRUNCATE TABLE questions CASCADE;
TRUNCATE TABLE reviews CASCADE;
TRUNCATE TABLE material_bookmarks CASCADE;
TRUNCATE TABLE course_bookmarks CASCADE;
TRUNCATE TABLE ai_jobs CASCADE;
TRUNCATE TABLE coin_transactions CASCADE;
TRUNCATE TABLE materials CASCADE;

-- Reset any sequences if they exist
-- PostgreSQL will automatically reset sequences when truncating

-- Update user statistics to 0
UPDATE users SET 
    coins = 0,
    uploads_count = 0,
    reviews_count = 0,
    questions_count = 0,
    answers_count = 0;

-- Log the cleanup
INSERT INTO ai_jobs (id, material_id, type, status, created_at, updated_at, error) 
VALUES (
    gen_random_uuid(), 
    NULL, 
    'CLEANUP', 
    'COMPLETED', 
    NOW(), 
    NOW(), 
    'Database cleanup completed - all data cleared successfully'
);
