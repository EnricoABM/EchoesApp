## 1. O Ciclo de Trabalho (Fluxo Diário)

Estes são os comandos que você usará toda vez que escrever uma nova linha de código ou seção da sua monografia.

* **Verifica o estado atual (quais arquivos foram modificados ou adicionados)**

```bash
git status
```

* **Adiciona um arquivo específico para a área de preparação (staging)**

```bash
git add <nome-do-arquivo>
```

* **Adiciona todos os arquivos modificados de uma vez**

```bash
git add .
```

* **Grava as alterações permanentemente com uma mensagem (seguindo o padrão que discutimos)**

```bash
git commit -m "tipo: descrição da alteração"
```

* **Envia seus commits locais para o servidor remoto (nuvem)**

```bash
git push origin <nome-da-branch>
```

---

## 2. Gerenciamento de Ramificações (Branches)

Essencial para testar funcionalidades novas sem quebrar o que já está pronto no seu TCC.

* **Lista todas as branches locais (a branch atual terá um asterisco)**

```bash
git branch
```

* **Cria uma nova branch para uma tarefa específica**

```bash
git branch <nome-da-branch>
```

* **Troca para uma branch existente**

```bash
git checkout <nome-da-branch>
```

* **Cria uma nova branch e já troca para ela imediatamente**

```bash
git checkout -b <nome-da-branch>
```

---

## 3. Integração e Atualização

Comandos para unir o trabalho e manter seu código sincronizado.

* **Mescla as alterações de outra branch na branch atual**

```bash
git merge <nome-da-outra-branch>
```

* **Busca e baixa as atualizações do repositório remoto para o seu computador**

```bash
git pull origin <nome-da-branch>
```

* **Mostra o histórico de commits realizados**

```bash
git log --oneline
```