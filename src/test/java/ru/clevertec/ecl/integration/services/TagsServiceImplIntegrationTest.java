package ru.clevertec.ecl.integration.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.services.impl.TagsServiceImpl;

class TagsServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TagsServiceImpl service;

    @Test
    void checkGetAllTagsWithFilteringShouldReturnListOfDtos() {

    }
}
