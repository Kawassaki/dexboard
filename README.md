# Dexboard
Indicadores de projetos da Dextra

[![Build Status](https://snap-ci.com/dextra/dexboard/branch/master/build_image)](https://snap-ci.com/dextra/dexboard/branch/master)


# Dependências
Para rodar o projeto você precisar ter as seguintes dependências instaladas:

* [Java - 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven - 3.6](https://maven.apache.org/download.cgi) 


# Rodando projeto
Na raiz do projeto, execute o seguinte comando:


`mvn clean install`


Depois execute

` mvn appengine:devserver `

Se tudo estiver ok, você poderá acessar a URL:


[localhost:8080/index.html](http://localhost:8080/index.html) 


Irá aparecer uma tela de login, onde é necessário utilizar um email `@dextra-sw.com` válido.


A princípio os projetos não serão listados. Para isso você precisa atualizar os projetos. 
Isso pode ser feito chamando a URL: [localhost:8080/reload/projetos](http://localhost:8080/reload/projetos) 


