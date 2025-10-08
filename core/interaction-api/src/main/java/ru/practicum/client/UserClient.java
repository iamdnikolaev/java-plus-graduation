package ru.practicum.client;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.user.UserDto;

@FeignClient(name = "user-service", path = "/admin/users")
public interface UserClient {
    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable @NotNull @Positive Long userId);
}
