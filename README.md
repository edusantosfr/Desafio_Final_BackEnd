Desafio Final do curso 3035Teach, Módulo 7 (BackEnd).

**Descrição**:
- O presente projeto foi feito com base na descrição do Desafio Final do módulo 7 de BackEnd do curso 3035Teach.
Link para o Acesso: <https://3035teach.s3.us-east-2.amazonaws.com/documents/57Desafio%20Final%20Fullstack140.pdf>
Atualmente é a API da Rede Social TeachGram. Que conta com as funções básicas para o funcionamento como
- Usuário: Cadastro, Exclusão e Login.
- Posts: Criação, Edição e Exclusão.

**Status do Projeto**:
- O atual status do projeto se encontra como "terminado", mas pode seguir em frente se for precisa adição de mais funcionalidades.

🚀 **Tecnologias Utilizadas**:
  Java 21v;
  Spring Boot;
  Spring Security; 
  PostgreSQL; 
  JWT.

📁 **Como inciar**
- Faça a criação do Banco de Dados usando o PgAdmin 4 e PostGreSql com o nome de 'teach-gram'.
- Inicie o Projeto com Intellij IDEA e entre na pasta 'teach-gram'.

Dentro de src/main/resources crie um arquivo .env e dentro dele defina:

- FRONT_END=http://localhost:5173 //o local do seu front
- JWT-SECRET=Kf93GfL!a%9dRz0# //uma senha para o token JWT
- URL=jdbc:postgresql://localhost:5432/teach-gram //a url da API
- USERNAME=postgres //o nome do seu banco
- PASSWORD=root //senha do seu banco

Logo após esses passos, inicie a API com o arquivo TeachGramApplication.

⚠️ Se estiver com alguma tarefa sendo executada na porta 8080.
Inicie o CMD como Administrador, use 'netstat -ano | findstr :8080' para descobrir que tarefa está sendo executada.
Então use o comando 'taskkill /PID 1234 /F' com o número da tarefa, para terminar ela.
