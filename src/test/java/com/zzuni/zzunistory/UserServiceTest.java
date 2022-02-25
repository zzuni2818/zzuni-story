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

    private static UserDto.ReqSignUpDto reqSignUpDto1;
    private static UserDto.ReqSignUpDto reqSignUpDto2;
    private static UserDto.ReqSignUpDto reqSignUpDto3;
    private static UserDto.ReqSignUpDto reqSignUpDto4;

    @BeforeAll
    public static void setUp() {
        reqSignUpDto1 = new UserDto.ReqSignUpDto();
        reqSignUpDto1.setUsername("User1_username");
        reqSignUpDto1.setPassword("User1_password");

        reqSignUpDto2 = new UserDto.ReqSignUpDto();
        reqSignUpDto2.setUsername("User2_username");
        reqSignUpDto2.setPassword("User2_password");

        reqSignUpDto3 = new UserDto.ReqSignUpDto();
        reqSignUpDto3.setUsername("User3_username");
        reqSignUpDto3.setPassword("User3_password");

        reqSignUpDto4 = new UserDto.ReqSignUpDto();
        reqSignUpDto4.setUsername(reqSignUpDto1.getUsername());
        reqSignUpDto4.setPassword(reqSignUpDto1.getPassword());
    }

    @Test
    public void count() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);
        userService.add(reqSignUpDto1);
        assertThat(userService.getCount()).isEqualTo(1);
        userService.add(reqSignUpDto2);
        assertThat(userService.getCount()).isEqualTo(2);
        userService.add(reqSignUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);
    }

    @Test
    public void addAndGet() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);

        Long user1_id = userService.add(reqSignUpDto1);
        assertThat(userService.get(user1_id).get().getUsername()).isEqualTo(reqSignUpDto1.getUsername());
        assertThat(userService.get(user1_id).get().getUsername()).isNotEqualTo(reqSignUpDto2.getUsername());
        assertThat(userService.get(user1_id).get().getUsername()).isNotEqualTo(reqSignUpDto3.getUsername());

        Long user2_id = userService.add(reqSignUpDto2);
        assertThat(userService.get(user2_id).get().getUsername()).isNotEqualTo(reqSignUpDto1.getUsername());
        assertThat(userService.get(user2_id).get().getUsername()).isEqualTo(reqSignUpDto2.getUsername());
        assertThat(userService.get(user2_id).get().getUsername()).isNotEqualTo(reqSignUpDto3.getUsername());

        userService.add(reqSignUpDto3);
        assertThat(userService.loadUserByUsername(reqSignUpDto3.getUsername()).getUsername()).isNotEqualTo(reqSignUpDto1.getUsername());
        assertThat(userService.loadUserByUsername(reqSignUpDto3.getUsername()).getUsername()).isNotEqualTo(reqSignUpDto2.getUsername());
        assertThat(userService.loadUserByUsername(reqSignUpDto3.getUsername()).getUsername()).isEqualTo(reqSignUpDto3.getUsername());
    }

    @Test
    public void addAndRemove() {
        userService.removeAll();
        assertThat(userService.getCount()).isEqualTo(0);
        Long user1_id = userService.add(reqSignUpDto1);
        Long user2_id = userService.add(reqSignUpDto2);
        Long user3_id = userService.add(reqSignUpDto3);
        assertThat(userService.getCount()).isEqualTo(3);

        userService.remove(user1_id);
        assertThat(userService.getCount()).isEqualTo(2);
        assertThat(userService.get(user1_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user2_id).get().getUsername()).isEqualTo(reqSignUpDto2.getUsername());
        assertThat(userService.get(user3_id).get().getUsername()).isEqualTo(reqSignUpDto3.getUsername());

        userService.remove(user2_id);
        assertThat(userService.getCount()).isEqualTo(1);
        assertThat(userService.get(user1_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user2_id).isEmpty()).isEqualTo(true);
        assertThat(userService.get(user3_id).get().getUsername()).isEqualTo(reqSignUpDto3.getUsername());

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

        userService.add(reqSignUpDto1);
        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.add(reqSignUpDto4);
        });
    }
}
