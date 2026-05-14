USE mysql;

CREATE USER IF NOT EXISTS 'rapizz'@'localhost' IDENTIFIED BY '';
CREATE USER IF NOT EXISTS 'rappiz'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON RaPizz.* TO 'rapizz'@'localhost';
GRANT ALL PRIVILEGES ON RaPizz.* TO 'rappiz'@'localhost';
FLUSH PRIVILEGES;
