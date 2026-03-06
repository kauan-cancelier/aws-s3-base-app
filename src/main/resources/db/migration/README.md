# Database Migration Scripts

## Overview
This directory contains Flyway database migration scripts for creating default roles, permissions, and users.

## Migration Files

### V1__Create_Default_Roles_And_Permissions.sql
Creates the default permission and role structure:

**Default Permissions:**
- `USER_READ` - Visualizar usuários
- `USER_WRITE` - Criar e editar usuários  
- `USER_DELETE` - Excluir usuários
- `ROLE_READ` - Visualizar papéis
- `ROLE_WRITE` - Criar e editar papéis
- `ROLE_DELETE` - Excluir papéis
- `FILE_READ` - Visualizar arquivos
- `FILE_WRITE` - Criar e editar arquivos
- `FILE_DELETE` - Excluir arquivos
- `ADMIN_PANEL` - Acessar painel administrativo

**Default Roles:**
- `ADMIN` - Todas as permissões
- `MANAGER` - Permissões limitadas (usuários e arquivos)
- `USER` - Permissões básicas (apenas arquivos)

### V2__Create_Default_Admin_User.sql
Creates a default admin user and assigns the ADMIN role.

**Note:** You need to:
1. Update the table/column names to match your actual User entity
2. Replace `$2a$10$YourHashedPasswordHere` with a properly hashed password
3. Update the email and other fields as needed

## Usage

To enable these migrations, update your `application.yaml`:

```yaml
spring:
  sql:
    init:
      mode: always
  flyway:
    enabled: true
    locations: classpath:db/migration
```

Or if you prefer using JPA's schema generation, you can run these scripts manually:

```bash
psql -h localhost -p 5435 -U baseappuser -d basedb -f src/main/resources/db/migration/V1__Create_Default_Roles_And_Permissions.sql
psql -h localhost -p 5435 -U baseappuser -d basedb -f src/main/resources/db/migration/V2__Create_Default_Admin_User.sql
```

## Security Notes
- Change the default admin password immediately after first login
- Consider using environment variables for sensitive data
- Review and customize permissions according to your application's specific needs
