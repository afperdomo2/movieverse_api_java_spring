-- Datos de ejemplo. Cada bloque inserta solo si su tabla está vacía.
-- Los identificadores son UUID v7 (RFC 9562) literales y ordenables por fecha.

INSERT INTO
    movies (
        id,
        title,
        duration,
        genre,
        release_date,
        rating,
        is_active
    )
SELECT
    id,
    title,
    duration,
    genre,
    release_date,
    rating,
    is_active
FROM (
        SELECT
            '019b76da-a800-7001-8aab-00000100bc8f'::uuid as id, 'The Shawshank Redemption' as title, 142 as duration, 'Drama' as genre, DATE '1994-09-23' as release_date, 9.30 as rating, TRUE as is_active
        UNION ALL
        SELECT '019b76da-abe8-7002-9556-00000201791e'::uuid, 'The Godfather', 175, 'Crime', DATE '1972-03-24', 9.20, TRUE
        UNION ALL
        SELECT '019b76da-afd0-7003-a001-0000030235ad'::uuid, 'The Dark Knight', 152, 'Action', DATE '2008-07-18', 9.00, TRUE
        UNION ALL
        SELECT '019b76da-b3b8-7004-aaac-00000402f23c'::uuid, 'Pulp Fiction', 154, 'Crime', DATE '1994-10-14', 8.90, TRUE
        UNION ALL
        SELECT '019b76da-b7a0-7005-b557-00000503aecb'::uuid, 'Forrest Gump', 142, 'Drama', DATE '1994-07-06', 8.80, TRUE
        UNION ALL
        SELECT '019b76da-bb88-7006-8002-000006046b5a'::uuid, 'Inception', 148, 'Sci-Fi', DATE '2010-07-16', 8.80, TRUE
        UNION ALL
        SELECT '019b76da-bf70-7007-8aad-0000070527e9'::uuid, 'Fight Club', 139, 'Drama', DATE '1999-10-15', 8.80, TRUE
        UNION ALL
        SELECT '019b76da-c358-7008-9558-00000805e478'::uuid, 'The Matrix', 136, 'Sci-Fi', DATE '1999-03-31', 8.70, TRUE
        UNION ALL
        SELECT '019b76da-c740-7009-a003-00000906a107'::uuid, 'Goodfellas', 146, 'Crime', DATE '1990-09-19', 8.70, TRUE
        UNION ALL
        SELECT '019b76da-cb28-700a-aaae-00000a075d96'::uuid, 'The Lord of the Rings: The Return of the King', 201, 'Fantasy', DATE '2003-12-17', 8.90, TRUE
        UNION ALL
        SELECT '019b76da-cf10-700b-b559-00000b081a25'::uuid, 'Star Wars: Episode V - The Empire Strikes Back', 124, 'Sci-Fi', DATE '1980-05-21', 8.70, TRUE
        UNION ALL
        SELECT '019b76da-d2f8-700c-8004-00000c08d6b4'::uuid, 'Interstellar', 169, 'Sci-Fi', DATE '2014-11-07', 8.60, TRUE
        UNION ALL
        SELECT '019b76da-d6e0-700d-8aaf-00000d099343'::uuid, 'Parasite', 132, 'Thriller', DATE '2019-05-30', 8.60, TRUE
        UNION ALL
        SELECT '019b76da-dac8-700e-955a-00000e0a4fd2'::uuid, 'Joker', 122, 'Crime', DATE '2019-10-04', 8.50, TRUE
        UNION ALL
        SELECT '019b76da-deb0-700f-a005-00000f0b0c61'::uuid, 'Avengers: Endgame', 181, 'Action', DATE '2019-04-26', 8.40, TRUE
        UNION ALL
        SELECT '019b76da-e298-7010-aab0-0000100bc8f0'::uuid, 'Titanic', 195, 'Romance', DATE '1997-12-19', 7.80, TRUE
        UNION ALL
        SELECT '019b76da-e680-7011-b55b-0000110c857f'::uuid, 'Toy Story', 81, 'Animation', DATE '1995-11-22', 8.30, TRUE
        UNION ALL
        SELECT '019b76da-ea68-7012-8006-0000120d420e'::uuid, 'Finding Nemo', 100, 'Animation', DATE '2003-05-30', 8.10, TRUE
        UNION ALL
        SELECT '019b76da-ee50-7013-8ab1-0000130dfe9d'::uuid, 'Gladiator', 155, 'Action', DATE '2000-05-05', 8.50, TRUE
    ) AS temp_data
WHERE
    NOT EXISTS (
        SELECT 1
        FROM movies
        LIMIT 1
    );

INSERT INTO
    authors (
        id,
        name,
        last_name,
        nationality,
        birth_date,
        biography,
        is_active
    )
SELECT
    id,
    name,
    last_name,
    nationality,
    birth_date,
    biography,
    is_active
FROM (
        SELECT
            '019b7c01-0400-7001-8aab-00000100bc8f'::uuid as id, 'Frank' as name, 'Darabont' as last_name, 'American' as nationality, DATE '1959-01-28' as birth_date, 'Director y guionista.' as biography, TRUE as is_active
        UNION ALL
        SELECT '019b7c01-07e8-7002-9556-00000201791e'::uuid, 'Francis Ford', 'Coppola', 'American', DATE '1939-04-07', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-0bd0-7003-a001-0000030235ad'::uuid, 'Christopher', 'Nolan', 'British-American', DATE '1970-07-30', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid, 'Quentin', 'Tarantino', 'American', DATE '1963-03-27', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b7c01-13a0-7005-b557-00000503aecb'::uuid, 'Robert', 'Zemeckis', 'American', DATE '1952-05-14', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-1788-7006-8002-000006046b5a'::uuid, 'David', 'Fincher', 'American', DATE '1962-08-28', 'Director y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-1b70-7007-8aad-0000070527e9'::uuid, 'Lana', 'Wachowski', 'American', DATE '1965-06-21', 'Directora, guionista y productora.', TRUE
        UNION ALL
        SELECT '019b7c01-1f58-7008-9558-00000805e478'::uuid, 'Lilly', 'Wachowski', 'American', DATE '1967-12-29', 'Directora, guionista y productora.', TRUE
        UNION ALL
        SELECT '019b7c01-2340-7009-a003-00000906a107'::uuid, 'Martin', 'Scorsese', 'American', DATE '1942-11-17', 'Director y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-2728-700a-aaae-00000a075d96'::uuid, 'Peter', 'Jackson', 'New Zealander', DATE '1961-10-31', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-2b10-700b-b559-00000b081a25'::uuid, 'Irvin', 'Kershner', 'American', DATE '1923-04-29', 'Director de cine.', TRUE
        UNION ALL
        SELECT '019b7c01-2ef8-700c-8004-00000c08d6b4'::uuid, 'Bong', 'Joon-ho', 'South Korean', DATE '1969-09-14', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b7c01-32e0-700d-8aaf-00000d099343'::uuid, 'Todd', 'Phillips', 'American', DATE '1970-12-20', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-36c8-700e-955a-00000e0a4fd2'::uuid, 'Anthony', 'Russo', 'American', DATE '1970-02-03', 'Director y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-3ab0-700f-a005-00000f0b0c61'::uuid, 'Joe', 'Russo', 'American', DATE '1971-07-18', 'Director y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-3e98-7010-aab0-0000100bc8f0'::uuid, 'James', 'Cameron', 'Canadian', DATE '1954-08-16', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b7c01-4280-7011-b55b-0000110c857f'::uuid, 'John', 'Lasseter', 'American', DATE '1957-01-12', 'Director y animador.', TRUE
        UNION ALL
        SELECT '019b7c01-4668-7012-8006-0000120d420e'::uuid, 'Andrew', 'Stanton', 'American', DATE '1965-12-03', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b7c01-4a50-7013-8ab1-0000130dfe9d'::uuid, 'Ridley', 'Scott', 'British', DATE '1937-11-30', 'Director y productor.', TRUE
    ) AS temp_data
WHERE
    NOT EXISTS (
        SELECT 1
        FROM authors
        LIMIT 1
    );
