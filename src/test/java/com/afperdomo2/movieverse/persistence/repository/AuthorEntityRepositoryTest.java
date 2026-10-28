package com.afperdomo2.movieverse.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.afperdomo2.movieverse.domain.dto.AuthorDto;
import com.afperdomo2.movieverse.domain.dto.CreateAuthorDto;
import com.afperdomo2.movieverse.domain.dto.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchAuthorDto;
import com.afperdomo2.movieverse.domain.dto.UpdateAuthorDto;
import com.afperdomo2.movieverse.domain.exception.AuthorAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.AuthorNotFoundException;
import com.afperdomo2.movieverse.persistence.crud.CrudAuthorEntity;
import com.afperdomo2.movieverse.persistence.entity.AuthorEntity;
import com.afperdomo2.movieverse.persistence.mapper.AuthorMapper;

@ExtendWith(MockitoExtension.class)
class AuthorEntityRepositoryTest {

    private static final LocalDate BIRTH_DATE = LocalDate.of(1970, 7, 30);
    private static final UUID AUTHOR_ID = UUID.fromString("019b7c01-0bd0-7003-a001-0000030235ad");
    private static final UUID OTHER_AUTHOR_ID = UUID.fromString("019b7c01-04e0-7001-8aab-00000100bc8f");
    private static final UUID UNKNOWN_AUTHOR_ID = UUID.fromString("019b7c01-4a50-7013-8ab1-0000130dfe9d");

    @Mock
    private CrudAuthorEntity crudAuthorEntity;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorEntityRepository repository;

    @Captor
    private ArgumentCaptor<AuthorEntity> entityCaptor;

    @Test
    @DisplayName("findPage mapea la página, los metadatos y ordena por apellido y nombre")
    void findPage_mapsPageAndMetadata() {
        List<AuthorEntity> entities = List.of(authorEntity(AUTHOR_ID, "Christopher", "Nolan"));
        List<AuthorDto> dtos = List.of(authorDto(AUTHOR_ID, "Christopher", "Nolan"));
        PageImpl<AuthorEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), 19);
        when(crudAuthorEntity.findAll(any(Pageable.class))).thenReturn(page);
        when(authorMapper.toDto(entities)).thenReturn(dtos);

        PageResponse<AuthorDto> response = repository.findPage(1, 10);

        assertThat(response.items()).isEqualTo(dtos);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.limit()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(19);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.hasPrevious()).isFalse();

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(crudAuthorEntity).findAll(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("lastName").getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("findById devuelve el DTO cuando el autor existe")
    void findById_returnsDto_whenEntityExists() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        AuthorDto dto = authorDto(AUTHOR_ID, "Christopher", "Nolan");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(authorMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.findById(AUTHOR_ID)).isEqualTo(dto);
        verify(crudAuthorEntity).findById(AUTHOR_ID);
    }

    @Test
    @DisplayName("findById lanza AuthorNotFoundException cuando el autor no existe")
    void findById_throwsAuthorNotFound_whenEntityDoesNotExist() {
        when(crudAuthorEntity.findById(UNKNOWN_AUTHOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findById(UNKNOWN_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining(UNKNOWN_AUTHOR_ID.toString());
        verify(authorMapper, never()).toDto(any(AuthorEntity.class));
    }

    @Test
    @DisplayName("create genera un UUID v7, guarda el autor activo y devuelve el DTO")
    void create_savesActiveEntityWithUuidV7() {
        CreateAuthorDto request = createRequest("Christopher", "Nolan");
        AuthorDto created = authorDto(AUTHOR_ID, "Christopher", "Nolan");
        doAnswer(invocation -> {
            AuthorEntity entity = invocation.getArgument(1);
            entity.setName(request.name());
            entity.setLastName(request.lastName());
            entity.setNationality(request.nationality());
            entity.setBirthDate(request.birthDate());
            entity.setBiography(request.biography());
            return null;
        }).when(authorMapper).createEntityFromDto(eq(request), any(AuthorEntity.class));
        when(authorMapper.toDto(any(AuthorEntity.class))).thenReturn(created);

        AuthorDto result = repository.create(request);

        assertThat(result).isEqualTo(created);
        verify(crudAuthorEntity).findByNameAndLastName("Christopher", "Nolan");
        verify(crudAuthorEntity).save(entityCaptor.capture());
        AuthorEntity saved = entityCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId().version()).isEqualTo(7);
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getName()).isEqualTo("Christopher");
        assertThat(saved.getLastName()).isEqualTo("Nolan");
        assertThat(saved.getBirthDate()).isEqualTo(BIRTH_DATE);
    }

    @Test
    @DisplayName("create lanza AuthorAlreadyExistsException cuando el autor ya existe")
    void create_throwsAuthorAlreadyExists_whenAuthorExists() {
        CreateAuthorDto request = createRequest("Christopher", "Nolan");
        when(crudAuthorEntity.findByNameAndLastName("Christopher", "Nolan"))
                .thenReturn(authorEntity(AUTHOR_ID, "Christopher", "Nolan"));

        assertThatThrownBy(() -> repository.create(request))
                .isInstanceOf(AuthorAlreadyExistsException.class)
                .hasMessageContaining("Christopher Nolan");
        verify(crudAuthorEntity, never()).save(any(AuthorEntity.class));
        verify(authorMapper, never()).createEntityFromDto(any(CreateAuthorDto.class), any(AuthorEntity.class));
    }

    @Test
    @DisplayName("update actualiza el autor existente y devuelve el DTO")
    void update_updatesExistingEntity() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        UpdateAuthorDto changes = new UpdateAuthorDto("Christopher", "Nolan", "British",
                BIRTH_DATE, "Director.");
        AuthorDto updated = authorDto(AUTHOR_ID, "Christopher", "Nolan");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(crudAuthorEntity.findByNameAndLastName("Christopher", "Nolan")).thenReturn(entity);
        when(authorMapper.toDto(entity)).thenReturn(updated);

        assertThat(repository.update(AUTHOR_ID, changes)).isEqualTo(updated);
        verify(authorMapper).updateEntityFromDto(changes, entity);
        verify(crudAuthorEntity).save(entity);
    }

    @Test
    @DisplayName("update lanza AuthorNotFoundException cuando el autor no existe")
    void update_throwsAuthorNotFound_whenEntityDoesNotExist() {
        UpdateAuthorDto changes = new UpdateAuthorDto("Christopher", "Nolan", "British",
                BIRTH_DATE, "Director.");
        when(crudAuthorEntity.findById(UNKNOWN_AUTHOR_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(UNKNOWN_AUTHOR_ID, changes))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining(UNKNOWN_AUTHOR_ID.toString());
        verify(crudAuthorEntity, never()).save(any(AuthorEntity.class));
    }

    @Test
    @DisplayName("update lanza AuthorAlreadyExistsException cuando el nombre pertenece a otro autor")
    void update_throwsAuthorAlreadyExists_whenNameBelongsToAnotherAuthor() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        UpdateAuthorDto changes = new UpdateAuthorDto("Frank", "Darabont", "American",
                BIRTH_DATE, "Director.");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(crudAuthorEntity.findByNameAndLastName("Frank", "Darabont"))
                .thenReturn(authorEntity(OTHER_AUTHOR_ID, "Frank", "Darabont"));

        assertThatThrownBy(() -> repository.update(AUTHOR_ID, changes))
                .isInstanceOf(AuthorAlreadyExistsException.class)
                .hasMessageContaining("Frank Darabont");
        verify(authorMapper, never()).updateEntityFromDto(any(UpdateAuthorDto.class), any(AuthorEntity.class));
        verify(crudAuthorEntity, never()).save(any(AuthorEntity.class));
    }

    @Test
    @DisplayName("patch actualiza solo los campos enviados y conserva el resto")
    void patch_updatesOnlyProvidedFields() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        PatchAuthorDto changes = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.of("British"), Optional.empty(), Optional.empty(), Optional.of(false));
        AuthorDto dto = authorDto(AUTHOR_ID, "Christopher", "Nolan");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(crudAuthorEntity.findByNameAndLastName("Christopher", "Nolan")).thenReturn(entity);
        when(authorMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(AUTHOR_ID, changes)).isEqualTo(dto);
        assertThat(entity.getNationality()).isEqualTo("British");
        assertThat(entity.isActive()).isFalse();
        assertThat(entity.getName()).isEqualTo("Christopher");
        assertThat(entity.getLastName()).isEqualTo("Nolan");
        verify(crudAuthorEntity).save(entity);
    }

    @Test
    @DisplayName("patch permite cambiar nombre y apellido")
    void patch_updatesNameAndLastName() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        PatchAuthorDto changes = new PatchAuthorDto(Optional.of("Chris"), Optional.of("Nolan"),
                Optional.empty(), Optional.empty(), Optional.empty());
        AuthorDto dto = authorDto(AUTHOR_ID, "Chris", "Nolan");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(crudAuthorEntity.findByNameAndLastName("Chris", "Nolan")).thenReturn(null);
        when(authorMapper.toDto(entity)).thenReturn(dto);

        repository.patch(AUTHOR_ID, changes);

        assertThat(entity.getName()).isEqualTo("Chris");
        assertThat(entity.getLastName()).isEqualTo("Nolan");
        verify(crudAuthorEntity).save(entity);
    }

    @Test
    @DisplayName("patch lanza AuthorAlreadyExistsException cuando el nuevo nombre ya existe")
    void patch_throwsAuthorAlreadyExists_whenNewNameExists() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        PatchAuthorDto changes = new PatchAuthorDto(Optional.empty(), Optional.of("Darabont"),
                Optional.empty(), Optional.empty(), Optional.empty());
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(crudAuthorEntity.findByNameAndLastName("Christopher", "Darabont"))
                .thenReturn(authorEntity(OTHER_AUTHOR_ID, "Christopher", "Darabont"));

        assertThatThrownBy(() -> repository.patch(AUTHOR_ID, changes))
                .isInstanceOf(AuthorAlreadyExistsException.class)
                .hasMessageContaining("Christopher Darabont");
        assertThat(entity.getLastName()).isEqualTo("Nolan");
        verify(crudAuthorEntity, never()).save(any(AuthorEntity.class));
    }

    @Test
    @DisplayName("patch sin campos no guarda cambios")
    void patch_withEmptyChanges_doesNotSave() {
        AuthorEntity entity = authorEntity(AUTHOR_ID, "Christopher", "Nolan");
        PatchAuthorDto changes = new PatchAuthorDto(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());
        AuthorDto dto = authorDto(AUTHOR_ID, "Christopher", "Nolan");
        when(crudAuthorEntity.findById(AUTHOR_ID)).thenReturn(Optional.of(entity));
        when(authorMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(AUTHOR_ID, changes)).isEqualTo(dto);
        verify(crudAuthorEntity, never()).save(any(AuthorEntity.class));
        verify(crudAuthorEntity, never()).findByNameAndLastName(any(), any());
    }

    @Test
    @DisplayName("delete elimina el autor y devuelve true")
    void delete_returnsTrue_whenAuthorExists() {
        when(crudAuthorEntity.existsById(AUTHOR_ID)).thenReturn(true);

        assertThat(repository.delete(AUTHOR_ID)).isTrue();
        verify(crudAuthorEntity).deleteById(AUTHOR_ID);
    }

    @Test
    @DisplayName("delete lanza AuthorNotFoundException cuando el autor no existe")
    void delete_throwsAuthorNotFound_whenAuthorDoesNotExist() {
        when(crudAuthorEntity.existsById(UNKNOWN_AUTHOR_ID)).thenReturn(false);

        assertThatThrownBy(() -> repository.delete(UNKNOWN_AUTHOR_ID))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessageContaining(UNKNOWN_AUTHOR_ID.toString());
        verify(crudAuthorEntity, never()).deleteById(UNKNOWN_AUTHOR_ID);
    }

    private static AuthorEntity authorEntity(UUID id, String name, String lastName) {
        AuthorEntity entity = new AuthorEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setLastName(lastName);
        entity.setNationality("British-American");
        entity.setBirthDate(BIRTH_DATE);
        entity.setBiography("Director.");
        entity.setActive(true);
        return entity;
    }

    private static AuthorDto authorDto(UUID id, String name, String lastName) {
        return new AuthorDto(id, name, lastName, "British-American", BIRTH_DATE, "Director.", true);
    }

    private static CreateAuthorDto createRequest(String name, String lastName) {
        return new CreateAuthorDto(name, lastName, "British-American", BIRTH_DATE, "Director.");
    }
}
