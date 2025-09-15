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

    Java 17

    Spring Boot 3: Para a criação da API REST.

    Spring Data JPA: Para a comunicação com a base de dados.

    H2 Database: Uma base de dados em memória, ótima para desenvolvimento.

    Apache PDFBox: Para a leitura e extração de texto de ficheiros PDF.

    Maven: Para a gestão de dependências e construção do projeto.

Frontend

    HTML5

    React.js: Para a criação da interface de utilizador dinâmica.

    Recharts: Para a renderização dos gráficos.

    Tailwind CSS: Para a estilização da interface.

Como Executar o Projeto

Com a nova estrutura de pastas, precisará de dois terminais para executar a aplicação completa.
1. Executar o Backend (API Java)

    Pré-requisitos: Ter o Java 17 (ou superior) e o Maven instalados.

    Abra um terminal e navegue até à pasta backend.

# Navegue para a pasta do backend
cd backend

# Execute a aplicação Spring Boot
./mvnw spring-boot:run

O servidor do backend estará a ser executado em http://localhost:8080.
2. Executar o Frontend (Interface Web)

    Pré-requisitos: Ter o VS Code com a extensão Live Server instalada.

    Abra um segundo terminal e navegue até à pasta frontend.

# Navegue para a pasta do frontend
cd frontend

    No VS Code, clique com o botão direito do rato no ficheiro index.html e selecione "Open with Live Server".

O seu navegador será aberto automaticamente no endereço http://127.0.0.1:5500 (ou uma porta semelhante) com a interface da aplicação pronta a ser utilizada.
