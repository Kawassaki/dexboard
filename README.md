# Dexboard
Indicadores de projetos da Dextra

[![CircleCI](https://circleci.com/gh/dextra/dexboard/tree/master.svg?style=svg)](https://circleci.com/gh/dextra/dexboard/tree/master)


# Dependências
Para rodar o projeto você precisar ter as seguintes dependências instaladas:

* [Java - 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven - 3.6](https://maven.apache.org/download.cgi)


# Rodando projeto
Na raiz do projeto, execute o seguinte comando:


`mvn clean install`


Depois execute

`mvn appengine:devserver`

Se tudo estiver ok, você poderá acessar a URL:


[localhost:8080/index.html](http://localhost:8080/index.html)


Irá aparecer uma tela de login, onde é necessário utilizar um email `@dextra-sw.com` válido.


A princípio os projetos não serão listados. Para isso você precisa atualizar os projetos.
Isso pode ser feito chamando a URL: [localhost:8080/reload/projetos](http://localhost:8080/reload/projetos)

## Crons

Uma vez ao dia o Dexboard roda três crons. Um para atualizar o dashboard com informações da planilha de importação (`dxb.spreadsheet`), um para exportar todos os dados para outra planilha (´dxb.export.spreadsheet´) e um para enviar os emails com notificações a cerca de atrasos de preenchimento.

Para rodar a importação imediatamente acesse:
https://dexboard-reload.appspot.com/cron/reload/projetos

Para rodar a exportação imediatamente acesse:
https://dexboard-reload.appspot.com/cron/export

## Deploy

Há dois ambientes, um para a Dextra e outro para a Mutant. Você também pode forkar e criar seus próprios ambientes se quiser!

Basta adicionar um profile do maven conforme os que já existem lá.

Para deployar, rode:

```bash
  mvn appengine:update -P<profile>
```

Por exemplo, `-Pdextra`.

Para homolog, rode `mvn appengine:update -Pdextra-homolog`.
