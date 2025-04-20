<p align="center">
  <img src="https://iili.io/3FFO5cF.png" alt="Universidade Católica de Brasília">
</p>

## PROJETO PESSOAL - MICROSSERVIÇO  📚:

#### 📖 Descrição:
Microsserviço responsável por **gerar comprovantes de compras realizadas** por clientes. O comprovante é criado em formato PDF contendo as informações dos produtos adquiridos e o e-mail do comprador. Este serviço faz parte de uma arquitetura integrada com os microsserviços `ms-catalogo` (gerenciamento de produtos) e `ms-notificacao` (processamento e envio de notificações).

#### ⚡ Funcionalidades:
1. 🧾 Receber requisição de compra contendo e-mail do cliente e lista de produtos;
2. 📄 Gerar comprovante em PDF com os dados da compra;
3. 📤 Disparar envio do comprovante (via integração com ms-notificacao);
4. 🔗 Integração com `ms-catalogo` para validar/obter detalhes dos produtos adquiridos.

#### Métodos de execução:

### 🖥️ **1️⃣ Rodar Localmente**
Para executar o projeto localmente, basta iniciar a aplicação com o Spring Boot:

```sh
./gradlew bootRun
```

> 💡 Este projeto **não utiliza banco de dados** nem possui configuração Docker.

---

## 📌 Endpoints e exemplos de uso:

### 🧾 1 - Gerar Comprovante de Compra:
```cmd
curl --request POST \
  --url http://localhost:8083/v1/comprovantes \
  --header 'Content-Type: application/json' \
  --data '{
  "emailCliente": "cliente@email.com",
  "produtos": [
    {
      "title": "Camiseta Estampada",
      "price": 59.90
    },
    {
      "title": "Calça Jeans",
      "price": 120.00
    }
  ]
}'
```
> ✅ Ao executar essa chamada, um PDF será gerado no diretório `comprovantes/` com os detalhes da compra.

---

#### 🛠️ Tecnologias utilizadas:
- ☕ Java 17;
- 🍃 Spring Boot;
- 📄 iText (geração de PDF);
- 🛠️ Gradle;

---

## 🛺 Autor

<table>
  <tr>
    <td align="center">
      <a href="https://www.linkedin.com/in/wesley-lima-244405251/" title="Wesley Lima">
        <img src="https://media.licdn.com/dms/image/v2/D4D03AQEVAsL2UL6A0w/profile-displayphoto-shrink_400_400/profile-displayphoto-shrink_400_400/0/1721323972268?e=1746662400&v=beta&t=4_2RDPgz5FqJ2G-yRQk3y0vWMVRpSeAPKMAO7IOFXeE" width="100px;" alt="Foto do Wesley Lima"/><br>
        <sub>
          <b>Wesley Lima</b>
        </sub>
      </a>
    </td>
  </tr>
</table>