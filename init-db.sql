-- Create databases for microservices
CREATE DATABASE orderdb;
CREATE DATABASE userdb;
CREATE DATABASE productdb;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE orderdb TO postgres;
GRANT ALL PRIVILEGES ON DATABASE userdb TO postgres;
GRANT ALL PRIVILEGES ON DATABASE productdb TO postgres;

-- Enable UUID extension for all databases
\c userdb;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c productdb;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c orderdb;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";