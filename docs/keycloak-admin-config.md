# Configuração de Credenciais Admin Keycloak

## Problema Resolvido
O erro `401 Unauthorized` ocorria porque estávamos usando as credenciais do cliente da aplicação (`base-app-realm`) para obter um token de administrador, mas esse cliente não tem permissões de admin.

## Solução Implementada
Foi criada uma configuração separada para usar as credenciais do administrador do Keycloak:

### Nova Configuração (application.yaml)
```yaml
keycloak:
  auth-server-url: http://localhost:8081/realms/base-app-realm
  admin-server-url: http://localhost:8081/admin/realms/base-app-realm
  master-server-url: http://localhost:8081/realms/master
  client-id: base-app-realm
  client-secret: NFTIiTo6RB0Q892xg5nmtPehFRxW7iQr
  admin-username: admin
  admin-password: admin
```

### Novo Cliente Feign
- `KeycloakMasterTokenClient`: Cliente para obter token do master realm usando credenciais de admin

### Fluxo de Autenticação
1. **Obter Token Admin**: Usa `admin-cli` com credenciais `admin:admin` no master realm
2. **Criar Usuário**: Usa o token de admin para criar usuário no realm `base-app-realm`

## Requisitos
- Usuário `admin` deve existir no Keycloak
- Senha do admin deve ser `admin` (configurável no application.yaml)
- Cliente `admin-cli` deve estar habilitado no master realm

## Teste
```bash
POST http://localhost:8100/auth/register
Content-Type: application/json

{
  "nomeCompleto": "Test User",
  "email": "test@example.com",
  "senha": "password123"
}
```
