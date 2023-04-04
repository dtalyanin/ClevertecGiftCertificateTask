package ru.clevertec.ecl.utils.constants;

public class TagsSQL {
    public static final String SELECT_ALL_TAGS = "SELECT t FROM Tag t LEFT JOIN t.giftCertificates";
    public static final String SELECT_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name = :name";
    public static final String DELETE_TAG_BY_ID = "DELETE Tag WHERE id = :id";
}
