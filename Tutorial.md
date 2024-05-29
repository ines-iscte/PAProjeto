# Biblioteca de Manipulação de XML - Tutorial de uso
### Projeto com objetivo de desenvolver uma biblioteca em Kotlin que permita ao utilizador gerar e manipular ficheiros do tipo XML.

## Índice

1. **Introdução**
2. **Classes**
    - Entity
    - Attribute
    - Document
3. **Funcionalidades**
    - VisitorFunctions
    - ClassMapping
    - DSL

### 1. Introdução

Os documentos XML são compostos por objetos que representam entidades ou atributos. As entidades poderão ter entidades aninhadas e por conseguinte atributos a elas associados.
Com esta aplicação, o utilizador:
- Terá flexibilidade em criar e alterar documentos XML que seguem uma estrutura semelhante à descrita;
- Obter a lista de entidades ou o seu conteúdo em formato de string, presentes no XML, ao introduzir expressões _XPath_;
- Fazer o mapeamento das classes de forma automática a partir de objetos, tendo por base a estrutura dessas classes e aplicando anotações;
- Permitir, no mapeamento, a personalização do conteúdo do ficheiro. Como exemplos, implementamos a adição de um símbolo de % ao valor da componente de avaliações, e a modificação dos objetos da classe fuc, ao adicionar um novo atributo.


### 2. Classes

As classes _Entity_, _Attribute_ e _Document_ contêm as operações de manipulação para os documentos XML.
Desta forma, o utilizador tem a flexibilidade de:
- Criar documentos, através da inserção de uma entidade raiz e de uma string de encoding

**Exemplo:**

`val plano = Entity(name ="plano")`

`val doc_plano = Document(child = plano,  encoding="?xml version=\"1.0\" encoding=\"UTF-8\"?")`

- Criar, alterar e remover entidades a documentos;

**Exemplo:**

`val curso = Entity(name ="curso", text ="Mestrado em Engenharia Informática", parent = plano)`

`doc_plano.addEntity(curso)`

- Alterar e remover entidades globalmente ao documento;

**Exemplo:**

`doc_plano.removeGlobalEntities("curso")`

- Criar, alterar e remover atributos a entidades;

**Exemplo:**

`curso.addAttribute(Attribute(name ="codigo", value ="M4310"))`

- Criar, alterar e remover atributos globalmente ao documento;

**Exemplo:**

`doc_plano.removeGlobalAttributes(curso, codigo)`

- Alterar ou Remover texto associado a entidades (que não tenham entidades filhas);

**Exemplo:**

`curso.removeEntityText()`

- Obter partes do XML, ao introduzir o caminho das entidades cujo conteúdo quer visualizar;

**Exemplo:**

`doc_plano.getEntityXmlWithXPath("curso")`

- Obter uma string ou ficheiro associado ao pretty_print de um documento.

**Exemplo:**

`doc_plano.prettyPrint()`

### 3. Funcionalidades

#### Funções Visitor
O ficheiro **_VisitorFunctions.kt_** tem as mesmas funcionalidades que a classe _Document_, mas aplicando o método _Visitor_. 
Estas funções verificam todas as entidades pertencentes ao documento, desde a que é a raíz da estrutura do XML até à sua última entidade descendente.
Assim o utilizador poderá procurar pela:

- entidade-pai - se não tiver é a entidade-raíz do documento;

**Exemplo:**

`doc_plano.getParentVis(curso)`

- entidades filhas da entidade; 

**Exemplo:**

`doc_plano.getChildrenVis(curso)`

- pai e entidades filhas da entidade.

**Exemplo:**

`doc_plano.getParentAndChildrenVis(curso)`

O utilizador poderá ainda percorrer as entidades do documento e procurar:
- pelo atributo que pretende adicionar, alterar ou remover globalmente do documento e fazê-lo;

**Exemplo:**

`doc_plano.addGlobalAttributeVis(entity_name: curso, attribute_name: "Attribute", attribute_value: "Value")`

- pela entidade que pretende adicionar, alterar ou remover globalmente do documento e fazê-lo.

**Exemplo:**

`doc_plano.renameGlobalEntityVis((old_name: "curso", new_name:"unidade"))`

#### Mapeamento de Classes XML
O ficheiro _**ClassMapping.kt**_ serve para traduzir o conteúdo das classes para objetos de modo a gerar automaticamente um ficheiro XML, recorrendo a 
anotações, transformadores e adaptadores que facilitam na flexibilidade de operações para o utilizador.
Deste modo, o utilizador poderá criar novas funções para manipulação de objetos e usá-las através das anotações _StringAdapter_ e _XmlAdapter_.

**Exemplo:**

`val c = ComponenteAvaliacao("Quizzes", 20)`
`translate(c, encoding = "utf-8")`

Estas funcionalidades permitem, como exemplos:
- Adicionar um símbolo de percentagem (%) ao valor das componentes de uma entidade;
- Adicionar um ponto final ao nome da entidade;
- Alterar o nome de uma entidade através de um adaptador;
- Adicionar, através do adaptador, um atributo a uma entidade em específico (FUC);


#### DSL Interna 
O ficheiro _**DSL.kt**_ é um ficheiro que disponibiliza a instanciação de modelos XML através de uma DLS interna. 
Assim, o utilizador tem uma visualização mais clara e limpa do conteúdo do ficheiro XML.
Neste ficheiro, é possível:
- Criar uma nova entidade;

**Exemplo:**

`val curso = entity("curso") {}`

- Adicionar um atributo a uma entidade;

**Exemplo:**

`val curso = entity("curso") { attribute("Att", "1") }`

- Adicionar uma entidade filha a uma entidade;

**Exemplo:**

`val curso = entity("curso") { entity("semestre", text = "1") {} }`

- Obter entidades filhas ou atributos associados a uma determinada entidade, através do seu nome;

**Exemplo:**

`curso.get("semestre") {}`

- Criar entidades e seus constituintes através de uma estrutura em árvore;

**Exemplo:**

`val curso = entity("curso") {
attribute("Att", "1")
entity("semestre", text = "1") { }
}`

- Transformar a estrutura da entidade e seus constituites numa estrutura em árvore, facilitanto a sua leitura.

**Exemplo:**

`curso.toTree()`
