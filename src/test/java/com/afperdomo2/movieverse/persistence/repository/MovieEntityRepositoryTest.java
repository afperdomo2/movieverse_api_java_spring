package com.afperdomo2.movieverse.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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

import com.afperdomo2.movieverse.domain.Genre;
import com.afperdomo2.movieverse.domain.dto.CreateMovieDto;
import com.afperdomo2.movieverse.domain.dto.MovieDto;
import com.afperdomo2.movieverse.domain.dto.PageResponse;
import com.afperdomo2.movieverse.domain.dto.PatchMovieDto;
import com.afperdomo2.movieverse.domain.dto.UpdateMovieDto;
import com.afperdomo2.movieverse.domain.exception.MovieAlreadyExistsException;
import com.afperdomo2.movieverse.domain.exception.MovieNotFoundException;
import com.afperdomo2.movieverse.persistence.crud.CrudMovieEntity;
import com.afperdomo2.movieverse.persistence.entity.MovieEntity;
import com.afperdomo2.movieverse.persistence.mapper.MovieMapper;

@ExtendWith(MockitoExtension.class)
class MovieEntityRepositoryTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(2014, 11, 7);
    private static final UUID MOVIE_ID = UUID.fromString("019b76da-d2f8-700c-8004-00000c08d6b4");
    private static final UUID OTHER_MOVIE_ID = UUID.fromString("019b76da-bb88-7006-8002-000006046b5a");
    private static final UUID UNKNOWN_MOVIE_ID = UUID.fromString("019b76da-ee50-7013-8ab1-0000130dfe9d");

    @Mock
    private CrudMovieEntity crudMovieEntity;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieEntityRepository repository;

    @Captor
    private ArgumentCaptor<MovieEntity> entityCaptor;

    @Test
    @DisplayName("findAll mapea todas las entidades a DTOs")
    void findAll_mapsEntitiesToDtos() {
        List<MovieEntity> entities = List.of(movieEntity(MOVIE_ID, "Interstellar"), movieEntity(OTHER_MOVIE_ID, "Inception"));
        List<MovieDto> dtos = List.of(movieDto(MOVIE_ID, "Interstellar"), movieDto(OTHER_MOVIE_ID, "Inception"));
        when(crudMovieEntity.findAll()).thenReturn(entities);
        when(movieMapper.toDto(entities)).thenReturn(dtos);

        assertThat(repository.findAll()).isEqualTo(dtos);
        verify(crudMovieEntity).findAll();
        verify(movieMapper).toDto(entities);
    }

    @Test
    @DisplayName("findAll devuelve una lista vacía cuando no hay entidades")
    void findAll_returnsEmptyList_whenThereAreNoEntities() {
        when(crudMovieEntity.findAll()).thenReturn(List.of());
        when(movieMapper.toDto(List.<MovieEntity>of())).thenReturn(List.of());

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("findPage mapea la página, los metadatos y ordena por id")
    void findPage_mapsPageAndMetadata() {
        List<MovieEntity> entities = List.of(movieEntity(MOVIE_ID, "Interstellar"));
        List<MovieDto> dtos = List.of(movieDto(MOVIE_ID, "Interstellar"));
        PageImpl<MovieEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10, Sort.by("id").ascending()), 19);
        when(crudMovieEntity.findAll(any(Pageable.class))).thenReturn(page);
        when(movieMapper.toDto(entities)).thenReturn(dtos);

        PageResponse<MovieDto> response = repository.findPage(1, 10);

        assertThat(response.items()).isEqualTo(dtos);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.limit()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(19);
        assertThat(response.totalPages()).isEqualTo(2);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.hasPrevious()).isFalse();

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(crudMovieEntity).findAll(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().getOrderFor("id")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("findPage mapea una página posterior")
    void findPage_mapsSecondPage() {
        List<MovieEntity> entities = List.of(movieEntity(OTHER_MOVIE_ID, "Movie 11"));
        List<MovieDto> dtos = List.of(movieDto(OTHER_MOVIE_ID, "Movie 11"));
        PageImpl<MovieEntity> page = new PageImpl<>(entities, PageRequest.of(1, 10, Sort.by("id").ascending()), 19);
        when(crudMovieEntity.findAll(any(Pageable.class))).thenReturn(page);
        when(movieMapper.toDto(entities)).thenReturn(dtos);

        PageResponse<MovieDto> response = repository.findPage(2, 10);

        assertThat(response.page()).isEqualTo(2);
        assertThat(response.hasPrevious()).isTrue();
        assertThat(response.hasNext()).isFalse();
    }

    @Test
    @DisplayName("findPage devuelve una página vacía cuando no hay resultados")
    void findPage_returnsEmptyPage() {
        List<MovieEntity> entities = List.of();
        PageImpl<MovieEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10, Sort.by("id").ascending()), 0);
        when(crudMovieEntity.findAll(any(Pageable.class))).thenReturn(page);
        when(movieMapper.toDto(entities)).thenReturn(List.of());

        PageResponse<MovieDto> response = repository.findPage(1, 10);

        assertThat(response.items()).isEmpty();
        assertThat(response.totalElements()).isZero();
        assertThat(response.totalPages()).isZero();
        assertThat(response.hasNext()).isFalse();
        assertThat(response.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("findById devuelve el DTO cuando la entidad existe")
    void findById_returnsDto_whenEntityExists() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.findById(MOVIE_ID)).isEqualTo(dto);
        verify(crudMovieEntity).findById(MOVIE_ID);
        verify(movieMapper).toDto(entity);
    }

    @Test
    @DisplayName("findById lanza MovieNotFoundException cuando la entidad no existe")
    void findById_throwsMovieNotFound_whenEntityDoesNotExist() {
        when(crudMovieEntity.findById(UNKNOWN_MOVIE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.findById(UNKNOWN_MOVIE_ID))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(movieMapper, never()).toDto(any(MovieEntity.class));
    }

    @Test
    @DisplayName("create guarda la entidad activa y devuelve el DTO")
    void create_savesActiveEntity() {
        CreateMovieDto request = createRequest("Interstellar");
        MovieDto created = movieDto(MOVIE_ID, "Interstellar");
        doAnswer(invocation -> {
            MovieEntity entity = invocation.getArgument(1);
            entity.setTitle(request.title());
            entity.setDuration(request.duration());
            entity.setGenre(request.genre());
            entity.setReleaseDate(request.releaseDate());
            entity.setRating(BigDecimal.valueOf(request.rating()));
            return null;
        }).when(movieMapper).createEntityFromDto(eq(request), any(MovieEntity.class));
        when(movieMapper.toDto(any(MovieEntity.class))).thenReturn(created);

        MovieDto result = repository.create(request);

        assertThat(result).isEqualTo(created);
        verify(crudMovieEntity).findByTitle("Interstellar");
        verify(crudMovieEntity).save(entityCaptor.capture());
        MovieEntity saved = entityCaptor.getValue();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId().version()).isEqualTo(7);
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getTitle()).isEqualTo("Interstellar");
        assertThat(saved.getRating()).isEqualByComparingTo("8.6");
        verify(movieMapper).createEntityFromDto(request, saved);
    }

    @Test
    @DisplayName("create lanza MovieAlreadyExistsException cuando el título ya existe")
    void create_throwsMovieAlreadyExists_whenTitleExists() {
        CreateMovieDto request = createRequest("Interstellar");
        when(crudMovieEntity.findByTitle("Interstellar")).thenReturn(movieEntity(MOVIE_ID, "Interstellar"));

        assertThatThrownBy(() -> repository.create(request))
                .isInstanceOf(MovieAlreadyExistsException.class)
                .hasMessageContaining("Interstellar");
        verify(crudMovieEntity, never()).save(any(MovieEntity.class));
        verify(movieMapper, never()).createEntityFromDto(any(CreateMovieDto.class), any(MovieEntity.class));
    }

    @Test
    @DisplayName("update actualiza la entidad existente y devuelve el DTO")
    void update_updatesExistingEntity() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar Remastered", RELEASE_DATE, 9.0);
        MovieDto updated = movieDto(MOVIE_ID, "Interstellar Remastered");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(updated);

        assertThat(repository.update(MOVIE_ID, changes)).isEqualTo(updated);
        verify(movieMapper).updateEntityFromDto(changes, entity);
        verify(crudMovieEntity).save(entity);
        assertThat(entity.isActive()).isTrue();
    }

    @Test
    @DisplayName("update lanza MovieNotFoundException cuando la entidad no existe")
    void update_throwsMovieNotFound_whenEntityDoesNotExist() {
        UpdateMovieDto changes = new UpdateMovieDto("Interstellar", RELEASE_DATE, 9.0);
        when(crudMovieEntity.findById(UNKNOWN_MOVIE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.update(UNKNOWN_MOVIE_ID, changes))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(movieMapper, never()).updateEntityFromDto(any(UpdateMovieDto.class), any(MovieEntity.class));
        verify(crudMovieEntity, never()).save(any(MovieEntity.class));
    }

    @Test
    @DisplayName("patch actualiza solo el título y conserva el resto")
    void patch_updatesOnlyTitle() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar Remastered"), Optional.empty(),
                Optional.empty());
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar Remastered");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(MOVIE_ID, changes)).isEqualTo(dto);
        assertThat(entity.getTitle()).isEqualTo("Interstellar Remastered");
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch actualiza solo la fecha y conserva el resto")
    void patch_updatesOnlyReleaseDate() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        LocalDate newReleaseDate = LocalDate.of(2015, 1, 15);
        PatchMovieDto changes = new PatchMovieDto(Optional.empty(), Optional.of(newReleaseDate), Optional.empty());
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(MOVIE_ID, changes)).isEqualTo(dto);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getReleaseDate()).isEqualTo(newReleaseDate);
        assertThat(entity.getRating()).isEqualByComparingTo("8.6");
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch actualiza solo la calificación y conserva el resto")
    void patch_updatesOnlyRating() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.of(9.0));
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(MOVIE_ID, changes)).isEqualTo(dto);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.getReleaseDate()).isEqualTo(RELEASE_DATE);
        assertThat(entity.getRating()).isEqualByComparingTo("9.0");
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch actualiza todos los campos enviados")
    void patch_updatesAllFields() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        LocalDate newReleaseDate = LocalDate.of(2015, 1, 15);
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar Remastered"), Optional.of(newReleaseDate),
                Optional.of(9.0));
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar Remastered");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        repository.patch(MOVIE_ID, changes);

        assertThat(entity.getTitle()).isEqualTo("Interstellar Remastered");
        assertThat(entity.getReleaseDate()).isEqualTo(newReleaseDate);
        assertThat(entity.getRating()).isEqualByComparingTo("9.0");
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch actualiza solo el género y conserva el resto")
    void patch_updatesOnlyGenre() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(Genre.COMEDY), Optional.empty());
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        repository.patch(MOVIE_ID, changes);

        assertThat(entity.getGenre()).isEqualTo(Genre.COMEDY);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        assertThat(entity.isActive()).isTrue();
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch desactiva la película y conserva el resto")
    void patch_updatesOnlyActive() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(false));
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        repository.patch(MOVIE_ID, changes);

        assertThat(entity.isActive()).isFalse();
        assertThat(entity.getGenre()).isEqualTo(Genre.SCI_FI);
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("patch sin campos no guarda cambios")
    void patch_withEmptyChanges_doesNotSave() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.empty(), Optional.empty(), Optional.empty());
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        assertThat(repository.patch(MOVIE_ID, changes)).isEqualTo(dto);
        verify(crudMovieEntity, never()).save(any(MovieEntity.class));
        verify(crudMovieEntity, never()).findByTitle(any());
    }

    @Test
    @DisplayName("patch lanza MovieNotFoundException cuando la película no existe")
    void patch_throwsMovieNotFound_whenEntityDoesNotExist() {
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar"), Optional.empty(), Optional.empty());
        when(crudMovieEntity.findById(UNKNOWN_MOVIE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.patch(UNKNOWN_MOVIE_ID, changes))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(crudMovieEntity, never()).save(any(MovieEntity.class));
    }

    @Test
    @DisplayName("patch lanza MovieAlreadyExistsException cuando el título pertenece a otra película")
    void patch_throwsMovieAlreadyExists_whenTitleBelongsToAnotherMovie() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Inception"), Optional.empty(), Optional.empty());
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(crudMovieEntity.findByTitle("Inception")).thenReturn(movieEntity(OTHER_MOVIE_ID, "Inception"));

        assertThatThrownBy(() -> repository.patch(MOVIE_ID, changes))
                .isInstanceOf(MovieAlreadyExistsException.class)
                .hasMessageContaining("Inception");
        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        verify(crudMovieEntity, never()).save(any(MovieEntity.class));
    }

    @Test
    @DisplayName("patch permite enviar el mismo título sin comprobar duplicados")
    void patch_allowsSameTitle() {
        MovieEntity entity = movieEntity(MOVIE_ID, "Interstellar");
        PatchMovieDto changes = new PatchMovieDto(Optional.of("Interstellar"), Optional.empty(), Optional.empty());
        MovieDto dto = movieDto(MOVIE_ID, "Interstellar");
        when(crudMovieEntity.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(movieMapper.toDto(entity)).thenReturn(dto);

        repository.patch(MOVIE_ID, changes);

        assertThat(entity.getTitle()).isEqualTo("Interstellar");
        verify(crudMovieEntity, never()).findByTitle(any());
        verify(crudMovieEntity).save(entity);
    }

    @Test
    @DisplayName("delete elimina la película y devuelve true")
    void delete_returnsTrue_whenMovieExists() {
        when(crudMovieEntity.existsById(MOVIE_ID)).thenReturn(true);

        assertThat(repository.delete(MOVIE_ID)).isTrue();
        verify(crudMovieEntity).deleteById(MOVIE_ID);
    }

    @Test
    @DisplayName("delete lanza MovieNotFoundException cuando la película no existe")
    void delete_throwsMovieNotFound_whenMovieDoesNotExist() {
        when(crudMovieEntity.existsById(UNKNOWN_MOVIE_ID)).thenReturn(false);

        assertThatThrownBy(() -> repository.delete(UNKNOWN_MOVIE_ID))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessageContaining(UNKNOWN_MOVIE_ID.toString());
        verify(crudMovieEntity, never()).deleteById(UNKNOWN_MOVIE_ID);
    }

    private static MovieEntity movieEntity(UUID id, String title) {
        MovieEntity entity = new MovieEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setDuration(169);
        entity.setGenre(Genre.SCI_FI);
        entity.setReleaseDate(RELEASE_DATE);
        entity.setRating(new BigDecimal("8.6"));
        entity.setActive(true);
        return entity;
    }

    private static MovieDto movieDto(UUID id, String title) {
        return new MovieDto(id, title, 169, Genre.SCI_FI, RELEASE_DATE, true, 8.6, "VERY_GOOD");
    }

    private static CreateMovieDto createRequest(String title) {
        return new CreateMovieDto(title, 169, Genre.SCI_FI, RELEASE_DATE, 8.6);
    }
}
