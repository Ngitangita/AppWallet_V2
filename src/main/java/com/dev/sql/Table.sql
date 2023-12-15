
-- Table for currencies
        CREATE TABLE IF NOT EXISTS "currency" (
        id serial PRIMARY KEY,
        name VARCHAR(255),
        code VARCHAR(4)
        );

-- Inserting currencies
        INSERT INTO "currency" ( name, code) VALUES
        ('Euro', 'EUR'),
        ( 'Ariary', 'AR');

-- Table for accounts
        CREATE TABLE IF NOT EXISTS "account" (
        id serial PRIMARY KEY,
        name VARCHAR(255),
        balance float,
        last_update_date_time timestamp,
        currency_id INT,
        account_type VARCHAR(20),
        FOREIGN KEY ( currency_id ) REFERENCES "currency"(id)
        );

-- Inserting account types
        INSERT INTO "account" (name, balance, last_update_date_time, currency_id, account_type) VALUES
        ( 'Current Account', 0.00, NOW(), 2, 'Bank'),
        ('Savings Account', 0.00, NOW(), 2, 'Bank'),
        ('Cash Wallet', 0.00, NOW(), 2, 'Cash'),
        ('Mobile Money', 0.00, NOW(), 2, 'Mobile Money');

-- Table for transactions
        CREATE TABLE IF NOT EXISTS "transaction" (
        id serial PRIMARY KEY,
        label VARCHAR(255),
        amount float,
        date_time timestamp,
        transaction_type VARCHAR(10),
        account_id INT,
        FOREIGN KEY (account_id) REFERENCES "account"(id)
        );

-- Inserting a transaction
        INSERT INTO "transaction" (label, amount, date_time, transaction_type, account_id)
        VALUES ('Salary', 100000, '2023-12-01 12:15 AM', 'Credit', 1),
        ('Christmas gift', 50000, '2023-12-02 2:00 PM', 'Debit', 4),
        ('New shoe', 20000, '2023-12-06 4:00 PM', 'Debit', 2),
        ('Salary', 40000, '2023-12-03 8:00 PM', 'Credit', 3);




