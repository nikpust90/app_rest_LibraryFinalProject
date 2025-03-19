package app_rest_libraryfinalproject.mappers;

import app_rest_libraryfinalproject.dto.BookDto;
import app_rest_libraryfinalproject.model.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedPerson", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removedPerson", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdPerson", source = "createdPerson")
    @Mapping(target = "ownerId", source = "owner.id") // Маппинг owner -> ownerId
    BookDto toDtoForCreate(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdPerson", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "removedPerson", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    @Mapping(target = "updatedPerson", source = "updatedPerson")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "ownerId", source = "owner.id")
    BookDto toDtoForUpdate(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdPerson", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedPerson", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "removedPerson", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    @Mapping(target = "ownerId", source = "owner.id")
    BookDto toDtoForGet(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedPerson", ignore = true)
    @Mapping(target = "removedPerson", ignore = true)
    @Mapping(target = "removedAt", ignore = true)
    @Mapping(target = "owner", ignore = true) // Игнорируем, если не маппим напрямую
    Book toEntity(BookDto bookDto);
}


