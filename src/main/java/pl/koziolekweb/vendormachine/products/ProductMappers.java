package pl.koziolekweb.vendormachine.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.koziolekweb.vendormachine.persons.User;

@Mapper(componentModel = "spring")
public interface ProductMappers {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", source = "user")
    Product fromCreate(CreateProductRequest source, User user);
}
