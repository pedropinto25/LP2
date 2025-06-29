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
	inativo BIT,
	vendido BIT
);
GO
-- Criar a tabela Cliente
CREATE TABLE Cliente (
    id INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255),
    morada TEXT,
    dataNascimento DATE,
    email VARCHAR(255),
    senha VARCHAR(255),
    encrypted BIT DEFAULT 0,
    approved BIT DEFAULT 0
	);
GO

-- Criar a tabela Users
CREATE TABLE Users (
    id INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    encrypted BIT DEFAULT 0,
    role VARCHAR(50) DEFAULT 'cliente',
    approved BIT DEFAULT 0,
    ultimoLogin DATETIME NULL 
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

--Criar a tabela para participar em leil�es
CREATE TABLE LeilaoParticipacao (
    id INT PRIMARY KEY IDENTITY(1,1),
    leilao_id INT NOT NULL,
    cliente_id INT NOT NULL,
    data_participacao TIMESTAMP,
    valor_lance DECIMAL(10, 2),
    FOREIGN KEY (leilao_id) REFERENCES Leilao(id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);
GO

-- Criar a tabela Pontos
CREATE TABLE Pontos (
    id INT PRIMARY KEY IDENTITY(1,1),
    cliente_id INT NOT NULL,
    pontos INT NOT NULL,
	approved BIT DEFAULT 0
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    );
GO

-- Criar a tabela Agente para registar agentes de licitação automática
CREATE TABLE Agente (
    id INT PRIMARY KEY IDENTITY(1,1),
    clienteId INT NOT NULL,
    leilaoId INT NOT NULL,
    ordem INT NOT NULL,
    incremento DECIMAL(10, 2) NOT NULL,
    limite DECIMAL(10, 2) NOT NULL,
    ativo BIT DEFAULT 1,
    FOREIGN KEY (clienteId) REFERENCES Cliente(id),
    FOREIGN KEY (leilaoId) REFERENCES Leilao(id)
);
GO

-- Criar tabela para classificacao de leiloes
CREATE TABLE LeilaoClassificacao (
    id INT PRIMARY KEY IDENTITY(1,1),
    classificacao INT CHECK (classificacao BETWEEN 1 AND 5),
    comentario TEXT NULL,
    data_classificacao DATE,
    cliente_id INT NOT NULL,
    leilao_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    FOREIGN KEY (leilao_id) REFERENCES Leilao(id),
    CONSTRAINT UC_LeilaoClassificacao UNIQUE (cliente_id, leilao_id)
);
GO

-- Criar tabela de categorias
CREATE TABLE Categoria (
    id INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(255) NOT NULL UNIQUE
);
GO

-- Tabela de associacao entre leiloes e categorias
CREATE TABLE LeilaoCategoria (
    leilao_id INT NOT NULL,
    categoria_id INT NOT NULL,
    PRIMARY KEY (leilao_id, categoria_id),
    FOREIGN KEY (leilao_id) REFERENCES Leilao(id) ,
    FOREIGN KEY (categoria_id) REFERENCES Categoria(id) 
);
GO

-- Categorias base
INSERT INTO Categoria (nome) VALUES
('Arte'),
('Antiguidade'),
('Mobiliário'),
('Ourivesaria'),
('Relojoaria'),
('Veículos'),
('Têxteis'),
('Imóveis');
GO
-- Tabela para propostas de negociação
CREATE TABLE NegociacaoProposta (
    id INT PRIMARY KEY IDENTITY(1,1),
    leilao_id INT NOT NULL,
    cliente_id INT NOT NULL,
    valor DECIMAL(10,2),
    estado VARCHAR(20),
    data TIMESTAMP,
    FOREIGN KEY (leilao_id) REFERENCES Leilao(id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);
GO
-- Criar o trigger para popular a tabela Users
CREATE TRIGGER trg_InsertUsers
ON Cliente
AFTER INSERT
AS
BEGIN
    INSERT INTO Users (id, email, password_hash)
    SELECT id, email, senha
    FROM inserted;
END;
GO

insert into Cliente (nome, morada, dataNascimento, email, senha)
VALUES('Pedro','rua 2',null, 'pintop2003@gmail.com', '123');

GO

Update Users
set approved = 1;

GO

Update Cliente
set approved = 1;

