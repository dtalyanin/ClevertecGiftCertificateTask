package ru.clevertec.ecl.dao.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.dao.TagsDAO;
import ru.clevertec.ecl.models.Tag;

import java.util.List;
import java.util.Optional;

import static ru.clevertec.ecl.utils.constants.TagsParams.TAGE_ID;
import static ru.clevertec.ecl.utils.constants.TagsParams.TAGE_NAME;
import static ru.clevertec.ecl.utils.constants.TagsSQL.*;

@Repository
public class TagsDAOImpl implements TagsDAO {

    private final SessionFactory factory;

    @Autowired
    public TagsDAOImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Tag> getAllTags() {
        return factory.getCurrentSession().createQuery(SELECT_ALL_TAGS, Tag.class).list();
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        return Optional.ofNullable(factory.getCurrentSession().get(Tag.class, id));
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        Tag tag;
        Query query = factory.getCurrentSession()
                .createQuery(SELECT_TAG_BY_NAME, Tag.class)
                .setParameter(TAGE_NAME, name);
        try {
            tag = (Tag) query.getSingleResult();
        } catch (NoResultException e) {
            tag = null;
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public Tag addTag(Tag tag) {
        factory.getCurrentSession().persist(tag);
        return tag;

    }

    @Override
    public boolean updateTag(long id, Tag tag) {
        Optional<Tag> currentTag = getTagById(id);
        if (currentTag.isPresent()) {
            tag.setId(id);
            factory.getCurrentSession().merge(tag);
            factory.getCurrentSession().flush();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTag(long id) {
        return factory.getCurrentSession()
                .createMutationQuery(DELETE_TAG_BY_ID)
                .setParameter(TAGE_ID, id)
                .executeUpdate() != 0;
    }
}
