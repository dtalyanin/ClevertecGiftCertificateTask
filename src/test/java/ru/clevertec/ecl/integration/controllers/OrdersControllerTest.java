package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.dto.orders.CreateOrderDto;
import ru.clevertec.ecl.dto.orders.OrderDto;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.models.responses.ModificationResponse;
import ru.clevertec.ecl.models.responses.errors.ErrorResponse;
import ru.clevertec.ecl.models.responses.errors.SingleFieldValidationErrorResponse;

import java.util.List;

import static generators.factories.orders.CreateOrderDtoFactory.*;
import static generators.factories.orders.OrderDtoFactory.*;
import static generators.factories.responses.ErrorResponseFactory.*;
import static generators.factories.responses.ModificationResponseFactory.getOrderAddedResponse;
import static generators.factories.responses.ValidationErrorResponseFactory.getZeroOrderQuantityResponse;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class OrdersControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithDefaultPagination() {
        List<OrderDto> orderDtos = getDifferentOrderDtos();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn1OrderDtoWithPageSize1() {
        List<OrderDto> orderDtos = getOrderDtosWithSize1();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithFirstPage() {
        List<OrderDto> orderDtos = getDifferentOrderDtos();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturnEmptyListOutOfPageRange() {
        List<OrderDto> orderDtos = getEmptyListOrderDtos();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn1OrderDtoWithPageSize1AngIncludeFirstPage() {
        List<OrderDto> orderDtos = getOrderDtosWithSize1();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("size", "1")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithDefaultPaginationWhenNegativeSize() {
        List<OrderDto> orderDtos = getDifferentOrderDtos();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetAllOrdersByUserIdShouldReturn2OrderDtosWithDefaultPaginationWhenNegativePage() {
        List<OrderDto> orderDtos = getDifferentOrderDtos();
        String jsonOrderDtos = mapper.writeValueAsString(orderDtos);

        mvc.perform(get("/users/{userId}/orders", 1L)
                        .param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDtos));
    }

    @Test
    @SneakyThrows
    void checkGetOrderByOrderIdAndUserIdShouldReturnOrderWithSpecifiedId() {
        OrderDto orderDto = getSimpleOrderDto();
        String jsonOrderDto = mapper.writeValueAsString(orderDto);

        mvc.perform(get("/users/{userId}/orders/{orderId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonOrderDto));
    }

    @Test
    @SneakyThrows
    void checkGetOrderByOrderIdAndUserIdShouldReturnResponseUserIdNotFound() {
        ErrorResponse errorResponse = getUserForOrderIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(get("/users/{userId}/orders/{orderId}", 10L, 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkGetOrderByOrderIdAndUserIdShouldReturnResponseOrderIdNotFound() {
        ErrorResponse errorResponse = getOrderIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);

        mvc.perform(get("/users/{userId}/orders/{orderId}", 1L, 10L))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseWithGeneratedId() {
        ModificationResponse modificationResponse = getOrderAddedResponse();
        String jsonModificationResponse = mapper.writeValueAsString(modificationResponse);
        CreateOrderDto createOrderDto = getSimpleCreateOrderDto();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonModificationResponse))
                .andExpect(header().string("Location", containsString("users/1/orders/3")));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseUserNotFound() {
        ErrorResponse errorResponse = getOrderCannotAddUserIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);
        CreateOrderDto createOrderDto = getSimpleCreateOrderDto();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseCertificateNotFound() {
        ErrorResponse errorResponse = getOrderCannotAddCertificateIdNotFoundResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);
        CreateOrderDto createOrderDto = getCreateOrderDtoWithNotExistingCertificateId();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isNotFound())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseIncorrectQuantity() {
        SingleFieldValidationErrorResponse errorResponse = getZeroOrderQuantityResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);
        CreateOrderDto createOrderDto = getCreateOrderDtoWithInvalidQuantity();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseIncorrectUserId() {
        ErrorResponse errorResponse = getIncorrectIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);
        CreateOrderDto createOrderDto = getSimpleCreateOrderDto();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }

    @Test
    @SneakyThrows
    void checkAddOrderShouldReturnResponseIncorrectCertificateId() {
        ErrorResponse errorResponse = getIncorrectCertificateIdResponse();
        String jsonErrorResponse = mapper.writeValueAsString(errorResponse);
        CreateOrderDto createOrderDto = getCreateOrderDtoWithNegativeCertificateId();
        String jsonCreateOrderDto = mapper.writeValueAsString(createOrderDto);

        mvc.perform(post("/users/{userId}/orders", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateOrderDto))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonErrorResponse));
    }
}