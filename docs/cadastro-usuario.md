# Endpoint de Cadastro de Usuários

Foi criado um endpoint público para cadastro de usuários integrado com Keycloak:

## Endpoint
```
POST /auth/register
```

**Acesso**: Público (não requer autenticação)

## Corpo da Requisição
```json
{
  "nomeCompleto": "João Silva",
  "email": "joao.silva@example.com",
  "senha": "senha123"
}
```

## Validações
- **nomeCompleto**: Obrigatório, entre 3 e 100 caracteres
- **email**: Obrigatório, deve ser um email válido
- **senha**: Obrigatório, mínimo 6 caracteres

## Respostas

### Sucesso (200)
```json
"Usuário criado com sucesso no Keycloak"
```

### Erro (400)
```json
"Erro ao criar usuário: [mensagem de erro]"
```

## Como Funciona

1. **Obtenção do Token de Admin**: O endpoint obtém um token de administrador do Keycloak usando as credenciais do cliente configuradas no `application.yaml`

2. **Criação do Usuário**: Com o token de admin, o usuário é criado diretamente no Keycloak através do endpoint de administração

3. **Configuração Automática**: O usuário é criado com:
   - Username igual ao email
   - Email verificado automaticamente
   - Conta habilitada
   - Senha temporária definida como false

## Tecnologias Utilizadas

- **KeycloakAdminClient**: Cliente Feign para comunicação com a API de administração do Keycloak
- **KeycloakTokenClient**: Cliente Feign para obtenção de tokens
- **KeycloakUserRequestDTO**: DTO com a estrutura esperada pelo Keycloak
- **UsuarioRegisterDto**: DTO com validações para a requisição de cadastro

O endpoint está pronto para uso e segue as melhores práticas de segurança e validação.
