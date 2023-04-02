package ru.clevertec.ecl.dao.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    private final SessionFactory factory;

    @Autowired
    public TagsDAOImpl(SessionFactory factory) {
        this.template = null;
        this.factory = factory;
    }

    @Override
    public List<Tag> getAllTags() {
        return factory.getCurrentSession().createQuery("SELECT t FROM Tag t", Tag.class).list();
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        return Optional.ofNullable(factory.getCurrentSession().get(Tag.class, id));
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        Tag tag;
        Query query = factory.getCurrentSession()
                .createQuery("SELECT t FROM Tag t WHERE name = :name", Tag.class)
                .setParameter("name", name);
        try {
            tag = (Tag) query.getSingleResult();
        } catch (NoResultException e) {
            tag = null;
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public long addTag(Tag tag) {
        factory.getCurrentSession().persist(tag);
        return tag.getId();

    }

    @Override
    public int updateTag(long id, Tag tag) {
        tag.setId(id);
        return template.update(UPDATE_TAG, tag.getName(), id);
    }

    @Override
    public int deleteTag(long id) {
        Tag tag = factory.getCurrentSession().get(Tag.class, id);
        factory.getCurrentSession().remove(tag);
//        Tag tag = new Tag(id, null);
//        factory.getCurrentSession().persist(tag);
//        Query query = factory.getCurrentSession().
//                createQuery("DELETE Tag WHERE id = :id", Tag.class);
//        query.setParameter("id", id);
        return 1;
    }
}
