-- TRAINING TYPES
INSERT INTO training_type (id, name) VALUES (1, 'FITNESS');
INSERT INTO training_type (id, name) VALUES (2, 'YOGA');
INSERT INTO training_type (id, name) VALUES (3, 'ZUMBA');
INSERT INTO training_type (id, name) VALUES (4, 'STRETCHING');
INSERT INTO training_type (id, name) VALUES (5, 'RESISTANCE');

-- USERS
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('John.Smith', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'John', 'Smith', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Anna.White', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Anna', 'White', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Mark.Brown', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Mark', 'Brown', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Sophia.Davis', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Sophia', 'Davis', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Liam.Wilson', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Liam', 'Wilson', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Mike.Black', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Mike', 'Black', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Olivia.Green', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Olivia', 'Green', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('James.Taylor', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'James', 'Taylor', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Emily.Johnson', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Emily', 'Johnson', TRUE);
INSERT INTO user_profile (username, password, first_name, last_name, is_active) VALUES ('Daniel.Lee', '$2a$10$Q.gcp0uslbX1sspyEHhI1eFLxpOsenlvZx4/99wFGWmZXbROFqqa2', 'Daniel', 'Lee', TRUE);

-- TRAINEES
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (1, '1998-03-15', '123 Main St, London');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (2, '1995-06-21', '56 Oxford St, London');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (3, '2000-11-05', '14 Queen Ave, Manchester');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (4, '1997-02-19', '9 Green Rd, Bristol');
INSERT INTO trainee (user_id, date_of_birth, address) VALUES (5, '1999-09-30', '77 King St, Leeds');

-- TRAINERS
INSERT INTO trainer (user_id, specialization_id) VALUES (6, 1);
INSERT INTO trainer (user_id, specialization_id) VALUES (7, 2);
INSERT INTO trainer (user_id, specialization_id) VALUES (8, 3);
INSERT INTO trainer (user_id, specialization_id) VALUES (9, 4);
INSERT INTO trainer (user_id, specialization_id) VALUES (10, 5);

-- TRAINEE ↔ TRAINER
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 1);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 2);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (3, 3);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (4, 4);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (5, 5);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 3);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 1);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (3, 2);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (4, 5);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (5, 4);

-- TRAININGS
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (1, 1, 'Morning Cardio', 1, '2025-10-10', 60);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (2, 2, 'Evening Yoga', 2, '2025-10-11', 90);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (3, 3, 'Zumba Energy', 3, '2025-10-12', 75);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (4, 4, 'Deep Stretch', 4, '2025-10-13', 50);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (5, 5, 'Power Resistance', 5, '2025-10-14', 70);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (3, 1, 'Zumba Blast', 3, '2025-10-15', 80);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (1, 2, 'Full Body Strength', 1, '2025-10-16', 60);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (2, 3, 'Morning Yoga Flow', 2, '2025-10-17', 90);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (5, 4, 'Core Resistance', 5, '2025-10-18', 70);
INSERT INTO training (trainer_id, trainee_id, name, training_type_id, training_date, duration) VALUES (4, 5, 'Evening Stretch', 4, '2025-10-19', 50);
