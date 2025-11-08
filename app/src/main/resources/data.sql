-- Clear existing data
DELETE FROM update_logs;
SHOW TABLES;
DELETE FROM user_devices;
DELETE FROM app_versions;
ALTER TABLE update_logs DROP CONSTRAINT update_logs_chk_1;
SHOW CREATE TABLE update_logs;

SELECT * FROM update_logs;

-- Insert sample data for app_versions table
INSERT INTO app_versions (version, platform, release_date, change_log, update_type, is_active, download_url, file_size, checksum, created_at, updated_at) VALUES
('1.0.0', 'ANDROID', '2024-01-15 10:00:00', 'Initial app release with basic features', 'OPTIONAL', true, 'https://cdn.example.com/app-v1.0.0.apk', 5242880, 'a1b2c3d4e5f6g7h8', NOW(), NOW()),
('1.1.0', 'ANDROID', '2024-02-20 14:30:00', 'New features and bug fixes', 'OPTIONAL', true, 'https://cdn.example.com/app-v1.1.0.apk', 6291456, 'b2c3d4e5f6g7h8i9', NOW(), NOW()),
('1.2.0', 'ANDROID', '2024-03-25 09:15:00', 'Added multi-language support and performance improvements', 'MANDATORY', true, 'https://cdn.example.com/app-v1.2.0.apk', 7340032, 'c3d4e5f6g7h8i9j0', NOW(), NOW()),
('2.0.0', 'ANDROID', '2024-05-10 16:45:00', 'Complete UI redesign and new payment system', 'MANDATORY', true, 'https://cdn.example.com/app-v2.0.0.apk', 10485760, 'd4e5f6g7h8i9j0k1', NOW(), NOW()),
('1.0.0', 'IOS', '2024-01-20 11:00:00', 'Initial iOS release', 'OPTIONAL', true, 'https://apps.apple.com/app/id123456', 4718592, 'e5f6g7h8i9j0k1l2', NOW(), NOW()),
('1.1.0', 'IOS', '2024-03-05 13:20:00', 'Performance optimizations and bug fixes', 'OPTIONAL', true, 'https://apps.apple.com/app/id123456', 5242880, 'f6g7h8i9j0k1l2m3', NOW(), NOW()),
('1.2.0', 'IOS', '2024-04-15 15:10:00', 'iPad support and new app icons', 'MANDATORY', true, 'https://apps.apple.com/app/id123456', 5767168, 'g7h8i9j0k1l2m3n4', NOW(), NOW()),
('1.0.0', 'WINDOWS', '2024-02-10 12:00:00', 'Initial Windows desktop version', 'OPTIONAL', true, 'https://cdn.example.com/app-setup-v1.0.0.exe', 15728640, 'h8i9j0k1l2m3n4o5', NOW(), NOW()),
('1.1.0', 'WINDOWS', '2024-04-01 10:30:00', 'Stability improvements and security updates', 'OPTIONAL', true, 'https://cdn.example.com/app-setup-v1.1.0.exe', 16777216, 'i9j0k1l2m3n4o5p6', NOW(), NOW()),
('1.0.0', 'MACOS', '2024-02-15 14:00:00', 'Initial macOS release', 'OPTIONAL', true, 'https://cdn.example.com/app-v1.0.0.dmg', 17825792, 'j0k1l2m3n4o5p6q7', NOW(), NOW()),
('1.1.0', 'MACOS', '2024-04-20 16:20:00', 'Apple Silicon support and dark mode', 'MANDATORY', true, 'https://cdn.example.com/app-v1.1.0.dmg', 18874368, 'k1l2m3n4o5p6q7r8', NOW(), NOW()),
('0.9.0', 'ANDROID', '2023-12-01 09:00:00', 'Beta version for testing', 'DEPRECATED', false, 'https://cdn.example.com/app-v0.9.0.apk', 4194304, 'l2m3n4o5p6q7r8s9', NOW(), NOW()),
('0.8.0', 'IOS', '2023-12-05 10:30:00', 'iOS beta version', 'DEPRECATED', false, 'https://cdn.example.com/app-v0.8.0.ipa', 4456448, 'm3n4o5p6q7r8s9t0', NOW(), NOW());

-- Insert sample data for user_devices table
INSERT INTO user_devices (user_id, platform, current_version, last_seen, created_at) VALUES
('user_001', 'ANDROID', '2.0.0', '2024-11-08 10:15:00', '2024-01-20 14:30:00'),
('user_002', 'ANDROID', '1.2.0', '2024-11-07 16:45:00', '2024-02-15 09:20:00'),
('user_003', 'ANDROID', '1.1.0', '2024-11-06 11:30:00', '2024-03-10 13:15:00'),
('user_004', 'ANDROID', '2.0.0', '2024-11-08 14:20:00', '2024-01-25 16:40:00'),
('user_005', 'ANDROID', '1.2.0', '2024-11-05 08:50:00', '2024-04-05 10:25:00'),
('user_101', 'IOS', '1.2.0', '2024-11-08 09:30:00', '2024-02-01 11:45:00'),
('user_102', 'IOS', '1.1.0', '2024-11-07 15:20:00', '2024-03-15 14:30:00'),
('user_103', 'IOS', '1.0.0', '2024-11-04 12:10:00', '2024-01-30 08:15:00'),
('user_104', 'IOS', '1.2.0', '2024-11-08 17:40:00', '2024-02-20 16:50:00'),
('user_105', 'IOS', '1.2.0', '2024-11-06 13:25:00', '2024-03-25 10:35:00'),
('user_201', 'WINDOWS', '1.1.0', '2024-11-07 10:45:00', '2024-02-28 12:20:00'),
('user_202', 'WINDOWS', '1.0.0', '2024-11-05 14:30:00', '2024-03-20 15:10:00'),
('user_203', 'WINDOWS', '1.1.0', '2024-11-08 11:15:00', '2024-04-10 09:40:00'),
('user_301', 'MACOS', '1.1.0', '2024-11-08 16:25:00', '2024-03-05 14:55:00'),
('user_302', 'MACOS', '1.0.0', '2024-11-06 09:20:00', '2024-02-25 11:30:00'),
('user_303', 'MACOS', '1.1.0', '2024-11-07 13:50:00', '2024-04-15 10:05:00'),
('user_test_001', 'ANDROID', '1.0.0', '2024-11-08 08:30:00', '2024-01-15 12:00:00'),
('user_test_002', 'IOS', '1.0.0', '2024-11-07 10:20:00', '2024-01-20 14:00:00'),
('user_test_003', 'WINDOWS', '1.0.0', '2024-11-06 15:45:00', '2024-02-10 16:30:00'),
('user_test_004', 'MACOS', '1.0.0', '2024-11-05 12:10:00', '2024-02-15 13:45:00');


INSERT INTO update_logs (user_id, platform, from_version, to_version, update_date, status, error_message, created_at, updated_at) VALUES
('user_001', 'ANDROID', '1.2.0', '2.0.0', '2024-05-11 09:30:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_002', 'ANDROID', '1.1.0', '1.2.0', '2024-03-26 14:15:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_003', 'ANDROID', '1.0.0', '1.1.0', '2024-02-21 11:20:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_004', 'ANDROID', '1.2.0', '2.0.0', '2024-05-12 16:45:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_101', 'IOS', '1.1.0', '1.2.0', '2024-04-16 10:30:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_102', 'IOS', '1.0.0', '1.1.0', '2024-03-06 15:45:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_104', 'IOS', '1.1.0', '1.2.0', '2024-04-17 08:20:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_201', 'WINDOWS', '1.0.0', '1.1.0', '2024-04-02 11:30:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_203', 'WINDOWS', '1.0.0', '1.1.0', '2024-04-03 14:25:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_301', 'MACOS', '1.0.0', '1.1.0', '2024-04-21 09:15:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_303', 'MACOS', '1.0.0', '1.1.0', '2024-04-22 13:40:00', 'SUCCESS', NULL, NOW(), NOW()),
('user_test_001', 'ANDROID', '1.0.0', '1.1.0', '2024-02-21 16:40:00', 'FAILED', 'Network connection lost during download', NOW(), NOW()),
('user_test_002', 'IOS', '1.0.0', '1.1.0', '2024-03-06 12:15:00', 'FAILED', 'Insufficient storage space', NOW(), NOW()),
('user_test_003', 'WINDOWS', '1.0.0', '1.1.0', '2024-04-02 10:20:00', 'FAILED', 'Installation interrupted by user', NOW(), NOW()),
('user_005', 'ANDROID', '1.1.0', '1.2.0', '2024-11-07 09:00:00', 'PENDING', NULL, NOW(), NOW()),
('user_103', 'IOS', '1.0.0', '1.1.0', '2024-11-06 14:30:00', 'PENDING', NULL, NOW(), NOW()),
('user_202', 'WINDOWS', '1.0.0', '1.1.0', '2024-11-05 16:15:00', 'PENDING', NULL, NOW(), NOW()),
('user_302', 'MACOS', '1.0.0', '1.1.0', '2024-11-04 11:45:00', 'PENDING', NULL, NOW(), NOW()),
('user_test_004', 'MACOS', '1.0.0', '1.1.0', '2024-04-21 15:30:00', 'CANCELLED', 'User cancelled the update process', NOW(), NOW());