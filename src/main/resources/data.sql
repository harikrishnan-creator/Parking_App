INSERT INTO users
(
 username,
 password,
 role,
 enabled
)
VALUES
(
 'admin',
 '$2a$10$DowJones7Jj5XEhMZ2Qv8MOLJXBh44kq6B9vOqG4P2w0fzhwdLAF6W',
 'ADMIN',
 true
)
ON CONFLICT(username)
DO NOTHING;
