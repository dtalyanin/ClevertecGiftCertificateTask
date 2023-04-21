package ru.clevertec.ecl.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.ecl.integration.BaseIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
class OrdersControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
}