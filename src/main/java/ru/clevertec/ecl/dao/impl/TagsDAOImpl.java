package ru.clevertec.ecl.dao.impl;

//import jakarta.persistence.NoResultException;
//import jakarta.persistence.Query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.exceptions.ItemExistException;
import ru.clevertec.ecl.models.Tag;
import ru.clevertec.ecl.models.codes.ErrorCode;

import java.util.List;
import java.util.Optional;

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
    public boolean updateTag(long id, Tag tag) {
        Optional<Tag> currentTag = getTagById(id);
        if (currentTag.isPresent()) {
            try {
                tag.setId(id);
                factory.getCurrentSession().merge(tag);
                return true;
            } catch (DataIntegrityViolationException e) {
                System.out.println(e.getClass());
                throw new ItemExistException("Cannot update: tag with name '" + tag.getName() +
                        "' already exist in database", ErrorCode.TAG_NAME_EXIST);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTag(long id) {
        return factory.getCurrentSession()
                .createQuery("DELETE Tag WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate() != 0;
    }
}
