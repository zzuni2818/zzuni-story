package com.zzuni.zzunistory;

import com.zzuni.zzunistory.dto.UserDto;
import com.zzuni.zzunistory.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private static UserDto.SignUpDto signUpDto1;
    private static UserDto.SignUpDto signUpDto2;
    private static UserDto.SignUpDto signUpDto3;
    private static UserDto.SignUpDto signUpDto4;

    @BeforeAll
    public static void setUp() {
        signUpDto1 = new UserDto.SignUpDto();
        signUpDto1.setUsername("User1_username");
        signUpDto1.setPassword("User1_password");

        signUpDto2 = new UserDto.SignUpDto();
        signUpDto2.setUsername("User2_username");
        signUpDto2.setPassword("User2_password");

        signUpDto3 = new UserDto.SignUpDto();
        signUpDto3.setUsername("User3_username");
        signUpDto3.setPassword("User3_password");

        signUpDto4 = new UserDto.SignUpDto();
        signUpDto4.setUsername(signUpDto1.getUsername());
        signUpDto4.setPassword(signUpDto1.getPassword());
    }

    @Test
    public void count() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);
        userService.add(signUpDto1);
        assertThat(userService.getCount()).isEqualTo(1);
        userService.add(signUpDto2);
        assertThat(userService.getCount()).isEqualTo(2);
        userService.add(signUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);
    }

    @Test
    public void addAndGet() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);

        Long user1_id = userService.add(signUpDto1);
        assertThat(userService.get(user1_id).get().getUsername()).isEqualTo(signUpDto1.getUsername());
        assertThat(userService.get(user1_id).get().getUsername()).isNotEqualTo(signUpDto2.getUsername());
        assertThat(userService.get(user1_id).get().getUsername()).isNotEqualTo(signUpDto3.getUsername());

        Long user2_id = userService.add(signUpDto2);
        assertThat(userService.get(user2_id).get().getUsername()).isNotEqualTo(signUpDto1.getUsername());
        assertThat(userService.get(user2_id).get().getUsername()).isEqualTo(signUpDto2.getUsername());
        assertThat(userService.get(user2_id).get().getUsername()).isNotEqualTo(signUpDto3.getUsername());

        userService.add(signUpDto3);
        assertThat(userService.loadUserByUsername(signUpDto3.getUsername()).getUsername()).isNotEqualTo(signUpDto1.getUsername());
        assertThat(userService.loadUserByUsername(signUpDto3.getUsername()).getUsername()).isNotEqualTo(signUpDto2.getUsername());
        assertThat(userService.loadUserByUsername(signUpDto3.getUsername()).getUsername()).isEqualTo(signUpDto3.getUsername());
    }

    @Test
    public void addAndRemove() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);
        Long user1_id = userService.add(signUpDto1);
        Long user2_id = userService.add(signUpDto2);
        Long user3_id = userService.add(signUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);

        userService.remove(user1_id);
        assertThat(userService.getCount()).isEqualTo(2);
        assertThat(userService.get(user1_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user2_id).get().getUsername()).isEqualTo(signUpDto2.getUsername());
        assertThat(userService.get(user3_id).get().getUsername()).isEqualTo(signUpDto3.getUsername());

        userService.remove(user2_id);
        assertThat(userService.getCount()).isEqualTo(1);
        assertThat(userService.get(user1_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user2_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user3_id).get().getUsername()).isEqualTo(signUpDto3.getUsername());

        userService.remove(user3_id);
        assertThat(userService.getCount()).isEqualTo(0);
        assertThat(userService.get(user1_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user2_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user3_id).isEmpty()).isEqualTo(true);
    }

    @Test
    public void duplicateUsername() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);

        userService.add(signUpDto1);
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.add(signUpDto4);
        });
    }
}
