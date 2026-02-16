package com.cibertec.logistics.client;

import com.cibertec.logistics.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-users")
public interface UsersClient {

    @GetMapping("/users")
    List<UserDto> findUsers(@RequestParam("id") String id);
}
