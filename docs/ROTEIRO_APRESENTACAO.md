# 🎬 Roteiro de Apresentação — Projeto 1 (2ª Etapa): FrameStack
**IFPB / Campus João Pessoa — Programação Orientada a Objetos 2026.2**  
**Professor:** Fausto Ayres  
**Tema:** FrameStack (Videoteca e Catálogo de Gêneros)  
**Banco de Dados:** Db4o (`framestack.db4o`)  

---

## 📌 Sumário da Apresentação
1. **Introdução e Contexto (1 minuto)**
2. **Modelo de Domínio e Relacionamentos (2 minutos)**
3. **Persistência Db4o, Utilitários e IDs Automáticos (2 minutos)**
4. **Demonstração Prática do CRUD no Console (5 a 7 minutos)**
   - `Cadastrar.java`
   - `Listar.java`
   - `Alterar.java` (Remover Relacionamento)
   - `Apagar.java` (Excluir Objeto com Relacionamento)
   - *Bônus:* `Visualizar.java` (Visualizador Gráfico em Árvore)
5. **Perguntas Frequentes do Professor e Respostas Técnicas (2 minutos)**
6. **Conclusão**

---

## 🗣️ Guia de Falas Passo a Passo

### Bloco 1: Abertura e Introdução
> **Quem fala:** Integrante 1  
> **Objetivo:** Cumprimentar a banca/professor, contextualizar o projeto e apresentar o objetivo da 2ª etapa.

> **Fala sugerida:**  
> *"Boa noite, professor Fausto e colegas. Nós somos o grupo responsável pelo projeto **FrameStack**, que é um sistema de videoteca para catálogo e avaliação de filmes e produções audiovisuais.*  
> *Na 1ª etapa, modelamos e validamos as regras de negócio puramente em memória orientada a objetos. Agora, na 2ª etapa, o nosso objetivo foi integrar a persistência real utilizando o banco de dados orientado a objetos **Db4o**.*  
> *Implementamos todas as quatro operações fundamentais do CRUD em console solicitadas: cadastro com relacionamentos, listagem completa, alteração com desassociação de vínculo e exclusão segura de um objeto associado, respeitando as boas práticas de integridade referencial que o senhor ensinou em aula."*

---

### Bloco 2: Modelo de Domínio e Arquitetura
> **Quem fala:** Integrante 2 (ou Integrante 1)  
> **Apoio visual:** Mostrar o arquivo `docs/img/Videoteca.UML.v1.drawio.png` ou o código das classes em `modelo`.

> **Fala sugerida:**  
> *"O nosso modelo de negócio é composto por duas entidades principais, localizadas no pacote `modelo`:*  
> 1. *A classe **`Video`**, que possui atributos como `id`, `titulo`, `data` (armazenada como texto `dd/MM/yyyy`, atendendo à especificação do projeto), `linkSite`, `nota` (de 0 a 10) e uma coleção de gêneros associados.*  
> 2. *A classe **`Genero`**, que possui `id`, `nome` e a lista de vídeos que pertencem a esse gênero.*  
>  
> *O relacionamento entre `Video` e `Genero` é **Muitos-para-Muitos (N:N)** e **bidirecional**: um filme como 'Matrix' pode pertencer simultaneamente a 'Ação' e 'Ficção Científica', e um gênero armazena todos os seus filmes associados.*  
> *Garantimos que os métodos `adicionarGenero`, `removerGenero` e `removerVideo` sincronizem automaticamente os dois lados da associação, mantendo os objetos consistentes tanto em memória quanto no banco."*

---

### Bloco 3: Configuração do Db4o e GeradorID
> **Quem fala:** Integrante 1 ou 2  
> **Apoio visual:** Abrir `src/util/Util.java` e `src/util/GeradorID.java`.

> **Fala sugerida:**  
> *"No pacote `util`, utilizamos as classes `Util` e `GeradorID` fornecidas na disciplina:*  
> * No `Util.java`, configuramos o arquivo do banco com o nome do projeto: **`framestack.db4o`**.*  
> * Configuramos `cascadeOnUpdate = true` para que, ao atualizar um vídeo ou gênero, as alterações nas coleções associadas sejam persistidas automaticamente.*  
> * Mantivemos **`cascadeOnDelete = false`**. Isso é fundamental: ao apagar um vídeo, não queremos que o Db4o delete em cascata os gêneros associados, pois outros vídeos ainda dependem deles.*  
> * O `GeradorID` monitora eventos de persistência e gera chaves primárias sequenciais automáticas (1, 2, 3...) para cada classe através de um arquivo auxiliar `sequencia.db4o`, eliminando a necessidade de setar IDs manualmente no construtor."*

---

### Bloco 4: Demonstração Prática do CRUD (Console)

---

#### 1️⃣ Cadastrar.java — Inserção com Relacionamentos
> **Ação:** Executar a classe `appconsole.Cadastrar`.  
> **Comando de terminal (se aplicável):**
> ```powershell
> java @add_opens.txt -cp "lib/db4o-all-8.0.249.jar;bin" appconsole.Cadastrar
> ```

> **Fala sugerida:**  
> *"Vamos iniciar a demonstração executando a aplicação `Cadastrar.java`.*  
> *(Executa a classe)*  
> *Aqui criamos 4 gêneros ('Ação', 'Ficção Científica', 'Comédia', 'Drama') e 4 filmes clássicos ('Matrix', 'O Exterminador do Futuro 2', 'De Volta para o Futuro' e 'Forrest Gump').*  
> *Cada filme é associado bidirecionalmente a múltiplos gêneros. Por exemplo: 'Matrix' é Ação e Ficção; 'De Volta para o Futuro' é Ficção e Comédia; 'Forrest Gump' é Drama e Comédia.*  
> *Ao chamar `manager.store(video)` e comitar, o Db4o persiste os vídeos, aciona os triggers do `GeradorID` e grava também os gêneros interligados.*  
> *Além disso, implementamos uma trava de segurança que detecta se o banco já foi populado, evitando duplicação acidental se o programa for executado mais de uma vez."*

---

#### 2️⃣ Listar.java — Listagem Completa com Relacionamentos
> **Ação:** Executar a classe `appconsole.Listar`.  
> **Comando de terminal:**
> ```powershell
> java @add_opens.txt -cp "lib/db4o-all-8.0.249.jar;bin" appconsole.Listar
> ```

> **Fala sugerida:**  
> *"Agora vamos executar a classe `Listar.java` para inspecionar o estado do banco.*  
> *(Executa a classe)*  
> *Observem como a listagem traz todos os objetos e seus respectivos relacionamentos em ambas as direções:*  
> * Na seção de **Vídeos**, cada vídeo exibe seus dados completos e a lista de nomes dos seus gêneros: `Matrix` tem `[Ação, Ficção Científica]`, `Forrest Gump` tem `[Drama, Comédia]`.*  
> * Na seção de **Gêneros**, cada gênero exibe o total de vídeos e os títulos associados: `Ficção Científica` possui 3 filmes ('Matrix', 'De Volta para o Futuro', 'O Exterminador do Futuro 2'); `Drama` possui 1 filme ('Forrest Gump').*  
> *Isso demonstra que a profundidade de ativação (`activationDepth`) e a bidirecionalidade estão funcionando perfeitamente."*

---

#### 3️⃣ Alterar.java — Remoção de um Relacionamento Existente
> **Ação:** Executar a classe `appconsole.Alterar`.  
> **Comando de terminal:**
> ```powershell
> java @add_opens.txt -cp "lib/db4o-all-8.0.249.jar;bin" appconsole.Alterar
> ```

> **Fala sugerida:**  
> *"O terceiro requisito do projeto é a classe `Alterar.java`, que deve remover um relacionamento existente entre dois objetos do banco.*  
> *No nosso caso, escolhemos remover o vínculo entre o filme **'Matrix'** e o gênero **'Ficção Científica'**.*  
> *(Executa a classe)*  
> *Vejam a saída detalhada no terminal:*  
> * O programa localiza 'Matrix' via consulta SODA e identifica o gênero 'Ficção Científica' associado.*  
> * Ele desfaz a associação chamando `matrix.removerGenero(ficcao)`, que remove dos dois lados.*  
> * Em seguida, persiste ambos com `manager.store()` e `manager.commit()`.*  
> *Agora podemos ver no log: 'Matrix' passa a ter apenas o gênero 'Ação', e 'Ficção Científica' passa de 3 para 2 filmes ('De Volta para o Futuro' e 'T2')."*

*(Dica: Executar `Listar.java` rapidamente para provar que a alteração foi persistida no arquivo do banco).*

---

#### 4️⃣ Apagar.java — Exclusão de Objeto com Relacionamento
> **Ação:** Executar a classe `appconsole.Apagar`.  
> **Comando de terminal:**
> ```powershell
> java @add_opens.txt -cp "lib/db4o-all-8.0.249.jar;bin" appconsole.Apagar
> ```

> **Fala sugerida:**  
> *"O quarto requisito é a classe `Apagar.java`: apagar um objeto que possua relacionamentos no banco.*  
> *Aqui selecionamos o filme **'Forrest Gump'**, que está relacionado aos gêneros 'Drama' e 'Comédia'.*  
> *(Executa a classe)*  
> *Professor, aqui está um ponto crucial da disciplina:*  
> *Como `cascadeOnDelete` é **falso**, não podemos simplesmente dar `manager.delete(forrest)`. Se fizéssemos isso, as listas internas de 'Drama' e 'Comédia' ficariam com referências a um objeto deletado.*  
> *Por isso, o nosso código primeiro percorre os gêneros associados ao 'Forrest Gump', remove o filme de cada um deles (`g.removerVideo(forrest)`), salva os gêneros atualizados no banco com `manager.store(g)` e, por fim, executa `manager.delete(forrest)` e `manager.commit()`.*  
> *O resultado é impecável:*  
> * 'Forrest Gump' foi completamente removido do banco.*  
> * O gênero 'Drama' continua existindo normalmente, agora com 0 vídeos.*  
> * O gênero 'Comédia' continua existindo com 1 vídeo remanescente ('De Volta para o Futuro')."*

*(Dica: Executar `Listar.java` novamente para mostrar a base final: 3 vídeos cadastrados e todos os gêneros preservados).*

---

#### 5️⃣ Bônus: Visualizar.java — Visualizador Gráfico em Árvore
> **Ação:** Executar a classe `appconsole.Visualizar`.  
> **Comando de terminal:**
> ```powershell
> java @add_opens.txt -cp "lib/db4o-all-8.0.249.jar;bin" appconsole.Visualizar
> ```

> **Fala sugerida:**  
> *"Para fechar com chave de ouro, criamos também a classe `Visualizar.java`, que abre uma interface gráfica Swing tipo OME (Object Manager Enterprise) do Eclipse.*  
> *Nela conseguimos expandir em tempo real a árvore de objetos do banco `framestack.db4o`, inspecionar atributos, listas de referências, IDs gerados e os nós do Db4o, provando visualmente a integridade física do arquivo."*

---

## 🎯 Perguntas Prováveis do Professor Fausto Ayres (e como responder)

### ❓ 1. "Por que a data foi armazenada como String e não LocalDate / Date?"
> **Resposta:**  
> *"Conforme a instrução da especificação do projeto da 2ª etapa ('Armazenar datas no banco como texto (string)'), padronizamos a data no formato brasileiro `dd/MM/yyyy` como String. Além de atender ao requisito do documento, isso simplifica a persistência sem necessidade de adaptadores adicionais no motor de tipos do Db4o."*

### ❓ 2. "Como funciona o GeradorID e onde os IDs são salvos?"
> **Resposta:**  
> *"O `GeradorID` registra listeners de eventos (triggers) no `ObjectContainer` do Db4o. No evento `creating()` (disparado antes do `store`), ele inspeciona via Reflection se a classe tem o campo `id`. Se tiver, ele consulta o banco auxiliar `sequencia.db4o`, lê o último ID gerado para aquela classe, incrementa sequencialmente, injeta no objeto e persiste o registro de controle no `sequencia.db4o`. No commit, ele limpa o cache via `purge()`."*

### ❓ 3. "Por que você não ativou `cascadeOnDelete(true)`?"
> **Resposta:**  
> *"Porque o relacionamento é N:N entre entidades independentes. Se `cascadeOnDelete` estivesse ativo em `Video`, ao deletar 'Matrix', o Db4o deletaria os gêneros 'Ação' e 'Ficção Científica'. E se `Genero` também estivesse com cascade ativo, todos os outros vídeos associados a 'Ação' e 'Ficção' seriam apagados em efeito dominó, destruindo o banco de dados inteiro! Por isso, a boa prática ensinada em aula é `cascadeOnDelete(false)` e a limpeza manual das referências antes da exclusão."*

### ❓ 4. "Por que é necessário o arquivo `add_opens.txt` na execução?"
> **Resposta:**  
> *"O Db4o é uma biblioteca desenvolvida originalmente para Java 5/6, que faz uso intensivo de introspecção e Reflection profunda (`setAccessible(true)`) em campos privados e coleções internas do JDK. A partir do Java 9 com o sistema de módulos (JPMS), o encapsulamento forte do Java bloqueia esse acesso reflexivo por padrão. O arquivo `add_opens.txt` passa os parâmetros `--add-opens` necessários para que a JVM permita ao Db4o inspecionar os objetos sem exceções de runtime."*

---

## 📋 Checklist Rápido Pré-Apresentação
- [ ] O arquivo `framestack.db4o` e `sequencia.db4o` foram deletados para iniciar a apresentação do zero?
- [ ] O terminal ou Eclipse está aberto na pasta do projeto?
- [ ] A ordem de execução está decorada:
  1. `Cadastrar.java`
  2. `Listar.java`
  3. `Alterar.java`
  4. `Listar.java`
  5. `Apagar.java`
  6. `Listar.java`
  7. `Visualizar.java`
