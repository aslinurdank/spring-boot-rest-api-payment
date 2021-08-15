DROP table accounts IF EXISTS ;
CREATE TABLE accounts (
  account_Id INT AUTO_INCREMENT  PRIMARY KEY,
  balance DECIMAL(10,2) NOT NULL
);

INSERT INTO accounts (account_Id, balance) VALUES
  (4755, 1001.88),
  (9834, 456.45),
  (7735, 89.36);