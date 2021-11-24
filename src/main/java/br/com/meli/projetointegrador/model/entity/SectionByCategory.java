package br.com.meli.projetointegrador.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "sectionbycategory")
@CompoundIndexes({
        @CompoundIndex(name = "section_category", def = "{'section' : 1, 'category': 1}", unique = true)
})
public class SectionByCategory {

    @MongoId(FieldType.OBJECT_ID)
    @Setter(AccessLevel.NONE)
    private String id;

    @DBRef
    private Section section;

    @DBRef
    private SectionCategory category;

    public SectionByCategory id(String id) {
        this.id = id;
        return this;
    }

    public SectionByCategory section(Section section) {
        this.section = section;
        return this;
    }

    public SectionByCategory category(SectionCategory category) {
        this.category = category;
        return this;
    }

    public SectionByCategory build() {
        return this;
    }

}
