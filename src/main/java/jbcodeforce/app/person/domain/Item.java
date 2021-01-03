package jbcodeforce.app.person.domain;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Indexed
public class Item {
    
    public int id;
    @FullTextField(analyzer = "english")
    public String text;

    public Item(){}

	public Item(int i, String inText) {
        this.id = i;
        this.text = inText;
	}
}
