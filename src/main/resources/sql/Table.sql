-- Table for currencies
        CREATE TABLE IF NOT EXISTS "currency" (
            id serial PRIMARY KEY,
            name VARCHAR(255),
            code VARCHAR(4)
        );

        -- Inserting currencies
        INSERT INTO "currency" ( name, code) VALUES
        ('EURO', 'EUR'),
        ( 'ARIARY', 'AR');

        -- Table for accounts
        CREATE TABLE IF NOT EXISTS "account" (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255),
            balance FLOAT,
            last_update_date_time TIMESTAMP,
            currency_id INT,
            account_type VARCHAR(20),
            FOREIGN KEY ( currency_id ) REFERENCES "currency"(id) ON DELETE CASCADE ON UPDATE CASCADE
        );

        -- Inserting account types
        INSERT INTO "account" (name, balance, last_update_date_time, currency_id, account_type) VALUES
        ( 'CURRENT_ACCOUNT', 19.00, NOW(), 2, 'BANK'),
        ('SAVINGS_ACCOUNT', 12.00, NOW(), 2, 'BANK'),
        ('SAVINGS_ACCOUNT', 15.00, NOW(), 2, 'CASH'),
        ('SAVINGS_ACCOUNT', 188.00, NOW(), 2, 'MOBILE_MONEY');

        -- Table for transactions
        CREATE TABLE IF NOT EXISTS "transaction" (
            id serial PRIMARY KEY,
            label VARCHAR(255),
            amount FLOAT,
            date_time TIMESTAMP,
            transaction_type VARCHAR(10),
            category_id INT,
            account_id INT,
            FOREIGN KEY (category_id) REFERENCES "category"(id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (account_id) REFERENCES "account"(id) ON DELETE CASCADE ON UPDATE CASCADE
        );

        -- Inserting a transaction
        INSERT INTO "transaction" (label, amount, date_time, transaction_type, account_id, category_id)
        VALUES ('Salary', 100000, '2023-12-01 12:15 AM', 'CREDIT', 1, 1),
        ('Christmas gift', 50000, '2023-12-02 2:00 PM', 'DEBIT', 4, 1),
        ('New shoe', 20000, '2023-12-06 4:00 PM', 'DEBIT', 2, 2),
        ('Salary', 40000, '2023-12-03 8:00 PM', 'CREDIT', 3, 2);

      -- Inserting a transaction transfer history
        CREATE TABLE  IF NOT EXISTS "transfer_history" (
            id SERIAL PRIMARY KEY,
            debitTransaction_id INT ,
            creditTransaction_id INT,
            transferDate TIMESTAMP,
            FOREIGN KEY (debitTransaction_id) REFERENCES "account"(id) ON DELETE CASCADE ON UPDATE CASCADE,
            FOREIGN KEY (creditTransaction_id) REFERENCES "account"(id) ON DELETE CASCADE ON UPDATE CASCADE
        );

     CREATE TABLE IF NOT EXISTS "category" (
            id SERIAL PRIMARY KEY,
            name VARCHAR(299),
            type_category VARCHAR(200)
        );

    INSERT INTO "category" (name, type_category) VALUES
     ('Restaurant', 'FOOD'),
     ('Téléphone et Multimédia', 'ENTERTAINMENT');




