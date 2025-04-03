-- Criar a base de dados
CREATE DATABASE Leiloeira;
GO

-- Usar a base de dados criada
USE Leiloeira;
GO

-- Criar a tabela Leilao
CREATE TABLE Leilao (
    id INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255),
    descricao TEXT,
    tipo VARCHAR(50),
    dataInicio DATE,
    dataFim DATE,
    valorMinimo DECIMAL(10, 2),
    valorMaximo DECIMAL(10, 2),
    multiploLance DECIMAL(10, 2),
	inativo BIT
);
GO
-- Criar a tabela Cliente
CREATE TABLE Cliente (
    id INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255),
    morada TEXT,
    dataNascimento DATE,
    email VARCHAR(255),
    senha VARCHAR(255)
);
GO

-- Criar a tabela Lance
CREATE TABLE Lance (
    id INT PRIMARY KEY IDENTITY(1,1),
    valor DECIMAL(10, 2),
    dataHora DATETIME,
    clienteId INT,
    leilaoId INT,
    FOREIGN KEY (clienteId) REFERENCES Cliente(id),
    FOREIGN KEY (leilaoId) REFERENCES Leilao(id)
);
GO