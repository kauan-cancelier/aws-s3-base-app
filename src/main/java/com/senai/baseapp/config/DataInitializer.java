package com.senai.baseapp.config;

import com.senai.baseapp.domain.permission.Permission;
import com.senai.baseapp.domain.permission.PermissionRepository;
import com.senai.baseapp.domain.permission.Role;
import com.senai.baseapp.domain.permission.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public DataInitializer(RoleRepository roleRepository, 
                          PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializePermissions();
        initializeRoles();
    }

    private void initializePermissions() {
        if (permissionRepository.count() == 0) {
            Permission createUser = Permission.builder()
                    .name("CREATE_USER")
                    .description("Permissão para criar usuários")
                    .build();

            Permission readUser = Permission.builder()
                    .name("READ_USER")
                    .description("Permissão para ler usuários")
                    .build();

            Permission updateUser = Permission.builder()
                    .name("UPDATE_USER")
                    .description("Permissão para atualizar usuários")
                    .build();

            Permission deleteUser = Permission.builder()
                    .name("DELETE_USER")
                    .description("Permissão para deletar usuários")
                    .build();

            Permission readOwnUser = Permission.builder()
                    .name("READ_OWN_USER")
                    .description("Permissão para ler próprio usuário")
                    .build();

            Permission updateOwnUser = Permission.builder()
                    .name("UPDATE_OWN_USER")
                    .description("Permissão para atualizar próprio usuário")
                    .build();

            permissionRepository.saveAll(Set.of(createUser, readUser, updateUser, deleteUser, readOwnUser, updateOwnUser));
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            Set<Permission> adminPermissions = new HashSet<>();
            permissionRepository.findAll().forEach(adminPermissions::add);

            Role adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .description("Administrador do sistema")
                    .permissions(adminPermissions)
                    .build();

            Set<Permission> userPermissions = new HashSet<>();
            userPermissions.add(permissionRepository.findByName("READ_OWN_USER").orElseThrow());
            userPermissions.add(permissionRepository.findByName("UPDATE_OWN_USER").orElseThrow());

            Role userRole = Role.builder()
                    .name("ROLE_USUARIO")
                    .description("Usuário comum")
                    .permissions(userPermissions)
                    .build();

            roleRepository.saveAll(Set.of(adminRole, userRole));
        }
    }
}
