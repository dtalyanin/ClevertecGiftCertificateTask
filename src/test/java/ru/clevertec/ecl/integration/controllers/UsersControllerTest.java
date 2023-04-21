package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import generators.factories.users.UserDtoFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dto.UserDto;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;

import java.util.Collections;
import java.util.List;

import static generators.factories.responses.ErrorResponseFactory.getIncorrectIdResponse;
import static generators.factories.responses.ErrorResponseFactory.getUserIdNotFoundResponse;
import static generators.factories.users.UserDtoFactory.getSimpleUserDto;
import static generators.factories.users.UserDtoFactory.getSimpleUsersDtos;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UsersControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturnUserDtosWithDefaultPagination() {
        List<UserDto> userDtos = getSimpleUsersDtos();
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturn1UserDtoWithPageSize1() {
        List<UserDto> userDtos = List.of(getSimpleUserDto());
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturn2UserDtosWithFirstPage() {
        List<UserDto> userDtos = getSimpleUsersDtos();
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturnUserDtosEmptyListOutOfPageRange() {
        List<UserDto> userDtos = Collections.emptyList();
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturn1UserDtoWithPageSize1AngIncludeFirstPage() {
        List<UserDto> userDtos = List.of(getSimpleUserDto());
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("size", "1")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturn2UserDtosWithDefaultPaginationWhenNegativePageSize() {
        List<UserDto> userDtos = getSimpleUsersDtos();
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllUsersShouldReturnUserDtosWithDefaultPaginationWhenNegativePage() {
        List<UserDto> userDtos = getSimpleUsersDtos();
        String jsonUserDtos = mapper.writeValueAsString(userDtos);

        mvc.perform(get("/users")
                        .param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDtos));
    }

    @Test
    @SneakyThrows
    void checkGetUserByIdShouldReturnUserDtoWithSpecifiedId() {
        UserDto userDto = UserDtoFactory.getSimpleUserDto();
        String jsonUserDto = mapper.writeValueAsString(userDto);

        mvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonUserDto));
    }

    @Test
    @SneakyThrows
    void checkGetTagByIdShouldReturnErrorResponseWithIncorrectId() {
        ErrorResponse response = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(response);

        mvc.perform(get("/users/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkGetUserByIdShouldReturnErrorResponseWithIdNotFound() {
        ErrorResponse errorResponse = getUserIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(get("/users/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }
}