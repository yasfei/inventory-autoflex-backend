# ⚡ Inventory Autoflex – Backend (Quarkus)

## 🧭 Descrição

Este projeto é o **backend** da aplicação Inventory Autoflex, responsável por fornecer **API REST** para gerenciar produtos e matérias-primas, incluindo relacionamentos entre eles.  

O backend foi desenvolvido com **Node.js**, **Express** e **Prisma ORM**, com foco em **boas práticas de API**, validação de dados e integração com o frontend.

Este backend deve ser usado em conjunto com o frontend do Inventory Autoflex, que consome suas rotas para exibir e manipular produtos e matérias-primas.

---

## 📊 Funcionalidades

### 🏭 Gestão de Produtos
* CRUD completo de produtos.
* Validação de campos obrigatórios: código, nome e valor.
* Associação de matérias-primas a produtos com quantidade necessária.
* Atualização e remoção de associações de matérias-primas.

### 🧱 Gestão de Matérias-Primas
* CRUD completo de matérias-primas.
* Controle de quantidade disponível.
* Exclusão de matérias-primas apenas se não estiverem associadas a produtos.

### 🔗 Relacionamento
* Cada produto pode ter múltiplas matérias-primas associadas.
* Cada matéria-prima pode ser associada a múltiplos produtos.

---

## 🧩 Tecnologias Utilizadas

* **Quarkus 3+** – framework Java moderno, rápido e eficiente.
* **RESTEasy / JAX-RS** – criação de endpoints RESTful.
* **Node.js 20+** – runtime JavaScript moderno.
* **Express.js** – criação de API REST.
* **Hibernate ORM** – persistência de dados.
* **Prisma ORM** – modelagem e manipulação do banco de dados.
* **PostgreSQL** – banco de dados relacional.
* **Java 17+** – runtime moderno com suporte a features recentes.

---

## ⚙️ Setup

### 1. Clonar o projeto
```
git clone <URL_DO_REPOSITORIO_BACKEND>
cd inventory-autoflex-backend
```

### 2. Instalar dependências e plugins Maven
```
./mvnw clean install
```


### 3. Configurar variáveis de ambiente
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=<DB_USER>
quarkus.datasource.password=<DB_PASSWORD>
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/<DB_NAME>
quarkus.http.port=8080


### 4. Rodar o backend
```
./mvnw quarkus:dev
```

---

## ☁️ Deploy (opcional)

- Banco de dados: PostgreSQL (local ou na nuvem)
- Servidor: pode ser hospedado em Render, Railway ou Heroku.

---

## ✨ Observações

- Todas as rotas seguem padrão RESTful.
- Endpoints retornam erros padronizados em caso de validação ou falha.
- Produtos e matérias-primas podem ser manipulados via frontend conectado a este backend.
- A porta padrão é 8080, compatível com o frontend para desenvolvimento local.