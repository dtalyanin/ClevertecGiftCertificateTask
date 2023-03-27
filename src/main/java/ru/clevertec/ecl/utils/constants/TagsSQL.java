package ru.clevertec.ecl.utils.constants;

public class TagsSQL {
    public static final String SELECT_ALL_TAGS = "SELECT id, name FROM tags";
    public static final String SELECT_TAG_BY_ID = "SELECT id, name FROM tags WHERE id = ?";
    public static final String SELECT_TAG_BY_NAME = "SELECT id, name FROM tags WHERE name = ?";
    public static final String INSERT_NEW_TAG = "INSERT INTO tags (name) VALUES (?)";
    public static final String UPDATE_TAG = "UPDATE tags SET name = ? WHERE id = ?";
    public static final String DELETE_TAG_BY_ID = "DELETE FROM tags WHERE id = ?";
}
