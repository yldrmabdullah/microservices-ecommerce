-- Create databases for microservices
CREATE DATABASE orderdb;
CREATE DATABASE userdb;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE orderdb TO postgres;
GRANT ALL PRIVILEGES ON DATABASE userdb TO postgres;