-- Insert initial users
-- Insert initial users
INSERT INTO users (name, email, password, role)
VALUES ('Joy Osayi', 'admin@musulasoft.com', '$2a$12$Sid1WJ.PNB8gOu7.OtRHTu5Ylhsaxz1xsbYOM.5lQRISPHqfIG/D2', 'ROLE_ADMIN'),
       ('Joy Osayi', 'osayijoy63@gmail.com', '$2a$12$Sid1WJ.PNB8gOu7.OtRHTu5Ylhsaxz1xsbYOM.5lQRISPHqfIG/D2', 'ROLE_USER');

-- Insert initial events
INSERT INTO events (name, date, available_attendees_count, description, category)
VALUES ('Spring Conference', '2024-07-10 10:00:00', 100, 'A conference about Spring Framework', 'Conference'),
       ('Music Concert', '2024-08-15 19:00:00', 500, 'A live music concert', 'Concert');

-- Insert initial reservations
INSERT INTO reservations (event_id, user_id, attendees_count)
VALUES (1, 1, 2),  -- user_id 1 corresponds to admin@musulasoft.com
       (2, 2, 3);  -- user_id 2 corresponds to osayijoy63@gmail.com

