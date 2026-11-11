1. Instruções de execução

Na pasta raiz do projeto, execute:

.\mvnw.cmd spring-boot:run

A API estará disponível em:

http://localhost:8080

2. Documentação das rotas

| POST | `/api/itens` | Cadastrar um item | 201 Created |
| GET | `/api/itens` | Listar todos os itens | 200 OK |
| GET | `/api/itens/{id}` | Buscar item pelo ID | 200 OK ou 404 Not Found |
| PUT | `/api/itens/{id}` | Atualizar item pelo ID | 200 OK ou 404 Not Found |
| DELETE | `/api/itens/{id}` | Excluir item pelo ID | 204 No Content ou 404 Not Found |

3. Exemplos funcionais de requisições

Todas as requisições que enviam dados devem utilizar o formato `Body → raw → JSON` e o cabeçalho `Content-Type: application/json`.

3.1 Cadastrar item — POST

Body:
{
    "nome": "Espada ",
    "raridade": "Comum",
    "preco": 10.00,
    "nivelMinimo": 1
}

Resposta esperada: 201 Created

3.2 Listar itens — GET
Resposta esperada: 200 OK

3.3 Buscar item por ID — GET

http://localhost:8080/api/itens/1 (precisa colocar o id após itens/)

Resposta esperada: 200 OK

3.4 Atualizar item — PUT


http://localhost:8080/api/itens/1 (precisa ter uma referencia de id após itens/)

{
    "nome": "Espada Lendária ",
    "raridade": "Lendária",
    "preco": 1500.00,
    "nivelMinimo": 15
}


Resposta esperada: 200 OK

3.5 Excluir item — DELETE

http://localhost:8080/api/itens/1 (precisa do id do item que deseja excluir)

Resposta esperada: 204 No Content