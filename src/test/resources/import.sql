-- ============================================
-- TRAINING TYPES
-- ============================================
INSERT INTO training_type (id, name) VALUES (1, 'FITNESS');
INSERT INTO training_type (id, name) VALUES (2, 'YOGA');
INSERT INTO training_type (id, name) VALUES (3, 'ZUMBA');
INSERT INTO training_type (id, name) VALUES (4, 'STRETCHING');
INSERT INTO training_type (id, name) VALUES (5, 'RESISTANCE');

-- ============================================
-- USERS
-- ============================================
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('John.Doe', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'John', 'Doe', true);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Jane.Smith', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'Jane', 'Smith', true);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Alex.Johnson', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'Alex', 'Johnson', true);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Emma.Brown', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'Emma', 'Brown', true);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Mike.Wilson', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'Mike', 'Wilson', true);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Sarah.Lee', '$2a$10$oW2GC2uy4P814Q2TFFCcr.Yq/A82jXfUKc2DhpdU8nJrtV7feJKeG', 'Sarah', 'Lee', true);

-- ============================================
-- TRAINERS
-- ============================================
INSERT INTO trainer (user_id, specialization_id) VALUES (1, 1);
INSERT INTO trainer (user_id, specialization_id) VALUES (2, 2);
INSERT INTO trainer (user_id, specialization_id) VALUES (3, 3);

-- ============================================
-- TRAINEES
-- ============================================
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (4, '1990-05-12', '123 Main St');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (5, '1985-08-22', '456 Oak Ave');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (6, '2000-01-15', '789 Pine Rd');

-- ============================================
-- TRAININGS
-- ============================================
INSERT INTO training (name, training_date, duration_in_hours, trainee_id, trainer_id, training_type_id) VALUES ('Morning Fitness', '2026-01-10', 1, 1, 1, 1);
INSERT INTO training (name, training_date, duration_in_hours, trainee_id, trainer_id, training_type_id) VALUES ('Yoga Session', '2026-01-11', 2, 1, 2, 2);
INSERT INTO training (name, training_date, duration_in_hours, trainee_id, trainer_id, training_type_id) VALUES ('Zumba Fun', '2026-01-12', 1, 1, 3, 3);
