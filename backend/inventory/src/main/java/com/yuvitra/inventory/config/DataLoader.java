package com.yuvitra.inventory.config;

import com.yuvitra.inventory.entity.Role;
import com.yuvitra.inventory.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        if (roleRepository.findByRoleName("ADMIN").isEmpty()) {

            Role admin = new Role();
            admin.setRoleName("ADMIN");
            admin.setDescription("System Administrator");

            roleRepository.save(admin);
        }

        if (roleRepository.findByRoleName("MANAGER").isEmpty()) {

            Role manager = new Role();
            manager.setRoleName("MANAGER");
            manager.setDescription("Inventory Manager");

            roleRepository.save(manager);
        }

        if (roleRepository.findByRoleName("STAFF").isEmpty()) {

            Role staff = new Role();
            staff.setRoleName("STAFF");
            staff.setDescription("Inventory Staff");

            roleRepository.save(staff);
        }
    }
}