package ru.clevertec.ecl.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.models.Tag;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.ecl.utils.constants.TagsSQL.*;

@Repository
public class TagsDAOImpl implements TagsDAO {

    private final JdbcTemplate template;

    @Autowired
    public TagsDAOImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Tag> getAllTags() {
        return template.query(SELECT_ALL_TAGS, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        Tag tag;
        try {
            tag = template.queryForObject(SELECT_TAG_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id);
        } catch (EmptyResultDataAccessException e) {
            tag = null;
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        Tag tag;
        try {
            tag = template.queryForObject(SELECT_TAG_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name);
        } catch (EmptyResultDataAccessException e) {
            tag = null;
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public long addTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_NEW_TAG, new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();

    }

    @Override
    public int updateTag(long id, Tag tag) {
        return template.update(UPDATE_TAG, tag.getName(), id);
    }

    @Override
    public int deleteTag(long id) {
        return template.update(DELETE_TAG_BY_ID, id);
    }
}
