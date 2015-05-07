# Projeto SearchPapers
- Nome: SearchPapers
- Participantes: Camila Zacché de Aguiar e Daisy Ferreira Brito
- Descrição: O Search Paper é um projeto colaborativo de cadastramento de paper para facilitar a organização e aprendizado de pesquisadores. Sua funcionalidade principal é realizar o cadastramento e gerenciamento de papers considerando informações como dados do paper, keyords e autores.


# Configuração
- Para configurar o projeto vc deve ter instalado e configurado o Eclipse com WildFly e MySql

+ Configurar MySql
- Crie um schema com o nome searchpapers
- Crie um usuário searchpapers e senha searchpapers
- Configure o privilégio desse usuário para acessar o schema searchpapers

+ Configurar WildFly
- Acesse o arquivo standalone.xml no diretorio do WildFly. Caminho: wildfly-8.2.0.Final ▸ standalone ▸ configuration
- Inclua dentro da tag datasources as configurações de acesso ao banco de dados do projeto SearchPapers


<datasource jta="true" jndi-name="java:jboss/datasources/SearchPapers" pool-name="SearchPapersPool" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost:3306/searchpapers</connection-url>
        <driver>mysql</driver>
        <security>
            <user-name>searchpapers</user-name>
            <password>searchpapers</password>
        </security>
</datasource>


+ Importar projeto no Eclipse
- Baixe o projeto do repositório github
- Importe o projeto como um projeto Geral em existente projeto
- Verifique se as propriedades do projeto estão corretas. Em Projects Facets deve utilizar:
	- Dynamic Web Modules
	- Java
	- JavaServer Faces
	- JPA
- Adicione o projeto no WildFly e inicie o servidor


# Acessar Projeto
- Para acessar digite: localhost:8080/SearchPapers
- Será exibido a tela inicial contendo login e cadastro
- Para utilizar as funcionalidades do sistema é necessário se cadastrar antes, incluindo nome e senha de usuário
- Após cadastrar, entre com os dados de usuário e senha para realizar o login
- O usuário logado terá acesso ao cadastro e gerenciamento de papers.
