# Quarkus Social

> Uma API RESTful para simular funcionalidades de uma rede social, desenvolvida com Quarkus e Java.

## Visão Geral

O **Quarkus Social** é um projeto backend que permite o gerenciamento de usuários, seguidores e postagens, simulando operações comuns de redes sociais. O sistema foi desenvolvido utilizando Quarkus, Hibernate ORM Panache, validação de dados, testes automatizados e documentação interativa via Swagger.

## Funcionalidades

- Cadastro, listagem, atualização e remoção de usuários
- Seguir e deixar de seguir outros usuários
- Listar seguidores de um usuário
- Criar e listar postagens (posts)
- Restrições de acesso a posts (apenas seguidores podem visualizar)

## Tecnologias Utilizadas

- [Quarkus](https://quarkus.io/) 2.15.3.Final
- Java 11
- Hibernate ORM Panache
- Bean Validation
- Banco de Dados: PostgreSQL (produção) e H2 (testes)
- Swagger UI (OpenAPI)
- JUnit 5 e Rest-Assured (testes)
- Docker (execução em container)

## Estrutura do Projeto

```
src/
  main/
    java/com/github/luccassantos4/
      resource/    # Controllers REST
      entities/     # Entidades JPA
      repository/   # Repositórios Panache
      dto/          # Data Transfer Objects
    resources/
      application.properties  # Configurações
      META-INF/resources/     # Recursos estáticos (ex: index.html)
  test/
    java/com/github/luccassantos4/test/resources/ # Testes automatizados
```

## Endpoints Principais

### Usuários
- `POST /users` — Cria um novo usuário
- `GET /users` — Lista todos os usuários
- `PUT /users/{id}` — Atualiza um usuário
- `DELETE /users/{id}` — Remove um usuário

### Seguidores
- `PUT /users/{userId}/followers` — Seguir um usuário
- `GET /users/{userId}/followers` — Listar seguidores de um usuário
- `DELETE /users/{userId}/followers?followerId={id}` — Deixar de seguir

### Postagens
- `POST /users/{userId}/posts` — Criar post
- `GET /users/{userId}/posts` — Listar posts (apenas para seguidores)

## Documentação da API

Acesse a documentação interativa via Swagger UI após iniciar a aplicação:

```
http://localhost:8080/q/swagger-ui/
```

## Configuração

O arquivo `application.properties` define as configurações de banco de dados para desenvolvimento e testes. Por padrão, utiliza PostgreSQL em desenvolvimento e H2 em testes.

## Como Executar Localmente

1. **Pré-requisitos:**
   - Java 11+
   - Maven
   - PostgreSQL rodando em `localhost:5432` com banco `teste1`, usuário `postgres` e senha `1234` (ajuste em `application.properties` se necessário)

2. **Build do projeto:**
   ```sh
   ./mvnw clean package
   ```

3. **Executar aplicação:**
   ```sh
   ./mvnw quarkus:dev
   ```

4. **Acessar endpoints:**
   - API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/q/swagger-ui/`

## Testes Automatizados

Execute os testes com:
```sh
./mvnw test
```

## Executando com Docker

1. Gere o pacote:
   ```sh
   ./mvnw package
   ```
2. Construa a imagem Docker:
   ```sh
   docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quarkus-social-jvm .
   ```
3. Rode o container:
   ```sh
   docker run -i --rm -p 8080:8080 quarkus/quarkus-social-jvm
   ```

---

Projeto para fins de estudo e demonstração de backend com Quarkus.
