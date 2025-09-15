Meu Analista Financeiro

Este é um projeto full-stack que permite aos utilizadores fazer o upload de extratos bancários em formato PDF, analisar as transações e visualizar um resumo financeiro completo através de uma interface web interativa.
Funcionalidades

    Upload de Extrato: Envie o seu extrato bancário em PDF.

    Extração de Dados: A aplicação lê o PDF e extrai automaticamente as transações (data, descrição, valor).

    Categorização Inteligente: As transações são categorizadas automaticamente em Receitas, Moradia, Alimentação, Lazer, etc.

    Análise Visual: Visualize as suas despesas em gráficos de pizza ou de barras.

    Resumo Financeiro: Veja o total de receitas, despesas e o seu saldo final.

    Exportação: Exporte os dados do resumo para uma tabela em Excel ou baixe os gráficos como um ficheiro PDF.

Tecnologias Utilizadas
Backend

    Java 17 & Spring Boot 3: Para a criação da API REST.

    Spring Data JPA: Para a comunicação com a base de dados.

    H2 Database: Uma base de dados em memória, ótima para desenvolvimento.

    Apache PDFBox: Para a leitura e extração de texto de ficheiros PDF.

    Maven: Para a gestão de dependências e construção do projeto.

    Docker: Para a containerização da aplicação.

Frontend

    HTML5 & React.js: Para a criação da interface de utilizador dinâmica.

    Recharts: Para a renderização dos gráficos.

    Tailwind CSS: Para a estilização da interface.

    Docker & Nginx: Para a containerização e para servir a aplicação estática.

Como Executar o Projeto

Existem duas formas de executar a aplicação: com Docker (recomendado) ou manualmente.
Método 1: Execução com Docker (Recomendado)

Esta é a forma mais simples e fiável de executar o projeto, pois configura todo o ambiente automaticamente.

Pré-requisitos:

    Docker e Docker Compose instalados e em execução.

Passos:

    Abra um terminal na pasta raiz do projeto.

    Construa as imagens do backend e do frontend com o comando:

    docker compose build

    Inicie a aplicação completa em segundo plano:

    docker compose up -d

    Aceda à interface no seu navegador em http://localhost:5500.

Para parar a aplicação, execute: docker compose down.
Método 2: Execução Manual

Se preferir executar cada parte separadamente sem o Docker.

1. Executar o Backend (API Java)

    Pré-requisitos: Ter o Java 17 (ou superior) e o Maven instalados.

    Abra um terminal e navegue até à pasta backend.

    cd backend

    Execute a aplicação Spring Boot:

    ./mvnw spring-boot:run

    O servidor do backend estará a ser executado em http://localhost:8080.

2. Executar o Frontend (Interface Web)

    Pré-requisitos: Ter o VS Code com a extensão Live Server instalada.

    Abra a pasta frontend/meu-analista-financeiro-ui no VS Code.

    Clique com o botão direito do rato no ficheiro index.html e selecione "Open with Live Server".

    O seu navegador será aberto automaticamente no endereço correto (geralmente http://127.0.0.1:5500).
