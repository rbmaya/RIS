package model;

import generated.Tag;

import java.util.List;

public interface TagDao {
    boolean insertTag(Tag tag);
    boolean insertTagStringQuery(Tag tag);
    boolean insertTagBatchQuery(List<Tag> tagList);
}
