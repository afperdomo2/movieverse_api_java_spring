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
        UNION ALL
        SELECT '019b8127-6000-7001-8aab-00000100bc8f'::uuid, 'The Godfather Part II', 202, 'Crime', DATE '1974-12-20', 9.00, TRUE
        UNION ALL
        SELECT '019b8127-63e8-7002-9556-00000201791e'::uuid, 'The Lord of the Rings: The Fellowship of the Ring', 178, 'Fantasy', DATE '2001-12-19', 8.90, TRUE
        UNION ALL
        SELECT '019b8127-67d0-7003-a001-0000030235ad'::uuid, 'The Lord of the Rings: The Two Towers', 179, 'Fantasy', DATE '2002-12-18', 8.80, TRUE
        UNION ALL
        SELECT '019b8127-6bb8-7004-aaac-00000402f23c'::uuid, 'The Silence of the Lambs', 118, 'Thriller', DATE '1991-02-14', 8.60, TRUE
        UNION ALL
        SELECT '019b8127-6fa0-7005-b557-00000503aecb'::uuid, 'Schindler''s List', 195, 'Biography', DATE '1993-12-15', 9.00, TRUE
        UNION ALL
        SELECT '019b8127-7388-7006-8002-000006046b5a'::uuid, 'Saving Private Ryan', 169, 'War', DATE '1998-07-24', 8.60, TRUE
        UNION ALL
        SELECT '019b8127-7770-7007-8aad-0000070527e9'::uuid, 'Jurassic Park', 127, 'Adventure', DATE '1993-06-11', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-7b58-7008-9558-00000805e478'::uuid, 'E.T. the Extra-Terrestrial', 115, 'Family', DATE '1982-06-11', 7.90, TRUE
        UNION ALL
        SELECT '019b8127-7f40-7009-a003-00000906a107'::uuid, 'Raiders of the Lost Ark', 115, 'Adventure', DATE '1981-06-12', 8.40, TRUE
        UNION ALL
        SELECT '019b8127-8328-700a-aaae-00000a075d96'::uuid, 'The Departed', 151, 'Crime', DATE '2006-10-06', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-8710-700b-b559-00000b081a25'::uuid, 'Taxi Driver', 114, 'Drama', DATE '1976-02-08', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-8af8-700c-8004-00000c08d6b4'::uuid, 'Raging Bull', 129, 'Biography', DATE '1980-12-19', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-8ee0-700d-8aaf-00000d099343'::uuid, 'The Wolf of Wall Street', 180, 'Biography', DATE '2013-12-25', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-92c8-700e-955a-00000e0a4fd2'::uuid, 'Se7en', 127, 'Thriller', DATE '1995-09-22', 8.60, TRUE
        UNION ALL
        SELECT '019b8127-96b0-700f-a005-00000f0b0c61'::uuid, 'Gone Girl', 149, 'Thriller', DATE '2014-10-03', 8.10, TRUE
        UNION ALL
        SELECT '019b8127-9a98-7010-aab0-0000100bc8f0'::uuid, 'The Social Network', 120, 'Biography', DATE '2010-10-01', 7.80, TRUE
        UNION ALL
        SELECT '019b8127-9e80-7011-b55b-0000110c857f'::uuid, 'Alien', 117, 'Horror', DATE '1979-05-25', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-a268-7012-8006-0000120d420e'::uuid, 'Blade Runner', 117, 'Sci-Fi', DATE '1982-06-25', 8.10, TRUE
        UNION ALL
        SELECT '019b8127-a650-7013-8ab1-0000130dfe9d'::uuid, 'The Martian', 144, 'Sci-Fi', DATE '2015-10-02', 8.00, TRUE
        UNION ALL
        SELECT '019b8127-aa38-7014-955c-0000140ebb2c'::uuid, 'Django Unchained', 165, 'Western', DATE '2012-12-25', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-ae20-7015-a007-0000150f77bb'::uuid, 'Kill Bill: Vol. 1', 111, 'Action', DATE '2003-10-10', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-b208-7016-aab2-00001610344a'::uuid, 'Inglourious Basterds', 153, 'War', DATE '2009-08-21', 8.40, TRUE
        UNION ALL
        SELECT '019b8127-b5f0-7017-b55d-00001710f0d9'::uuid, 'Once Upon a Time in Hollywood', 161, 'Comedy', DATE '2019-07-26', 7.60, TRUE
        UNION ALL
        SELECT '019b8127-b9d8-7018-8008-00001811ad68'::uuid, 'The Prestige', 130, 'Mystery', DATE '2006-10-20', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-bdc0-7019-8ab3-0000191269f7'::uuid, 'Memento', 113, 'Mystery', DATE '2000-09-05', 8.40, TRUE
        UNION ALL
        SELECT '019b8127-c1a8-701a-955e-00001a132686'::uuid, 'Dunkirk', 106, 'War', DATE '2017-07-21', 7.80, TRUE
        UNION ALL
        SELECT '019b8127-c590-701b-a009-00001b13e315'::uuid, 'Oppenheimer', 180, 'Biography', DATE '2023-07-21', 8.30, TRUE
        UNION ALL
        SELECT '019b8127-c978-701c-aab4-00001c149fa4'::uuid, 'Batman Begins', 140, 'Action', DATE '2005-06-15', 8.20, TRUE
        UNION ALL
        SELECT '019b8127-cd60-701d-b55f-00001d155c33'::uuid, 'The Grand Budapest Hotel', 99, 'Comedy', DATE '2014-03-28', 8.10, TRUE
        UNION ALL
        SELECT '019b8127-d148-701e-800a-00001e1618c2'::uuid, 'Moonrise Kingdom', 94, 'Comedy', DATE '2012-06-29', 7.80, TRUE
        UNION ALL
        SELECT '019b8127-d530-701f-8ab5-00001f16d551'::uuid, 'The Royal Tenenbaums', 110, 'Comedy', DATE '2001-12-14', 7.60, TRUE
        UNION ALL
        SELECT '019b8127-d918-7020-9560-0000201791e0'::uuid, 'La La Land', 128, 'Musical', DATE '2016-12-09', 8.00, TRUE
        UNION ALL
        SELECT '019b8127-dd00-7021-a00b-000021184e6f'::uuid, 'Whiplash', 106, 'Drama', DATE '2014-10-10', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-e0e8-7022-aab6-000022190afe'::uuid, 'Arrival', 116, 'Sci-Fi', DATE '2016-11-11', 7.90, TRUE
        UNION ALL
        SELECT '019b8127-e4d0-7023-b561-00002319c78d'::uuid, 'Blade Runner 2049', 164, 'Sci-Fi', DATE '2017-10-06', 8.00, TRUE
        UNION ALL
        SELECT '019b8127-e8b8-7024-800c-0000241a841c'::uuid, 'Dune', 155, 'Sci-Fi', DATE '2021-10-22', 8.00, TRUE
        UNION ALL
        SELECT '019b8127-eca0-7025-8ab7-0000251b40ab'::uuid, 'Dune: Part Two', 166, 'Sci-Fi', DATE '2024-03-01', 8.50, TRUE
        UNION ALL
        SELECT '019b8127-f088-7026-9562-0000261bfd3a'::uuid, 'Sicario', 121, 'Thriller', DATE '2015-10-02', 7.60, TRUE
        UNION ALL
        SELECT '019b8127-f470-7027-a00d-0000271cb9c9'::uuid, 'Mad Max: Fury Road', 120, 'Action', DATE '2015-05-15', 8.10, TRUE
        UNION ALL
        SELECT '019b8127-f858-7028-aab8-0000281d7658'::uuid, 'The Big Lebowski', 117, 'Comedy', DATE '1998-03-06', 8.10, TRUE
        UNION ALL
        SELECT '019b8127-fc40-7029-b563-0000291e32e7'::uuid, 'No Country for Old Men', 122, 'Thriller', DATE '2007-11-21', 8.20, TRUE
        UNION ALL
        SELECT '019b8128-0028-702a-800e-00002a1eef76'::uuid, 'Fargo', 98, 'Crime', DATE '1996-04-05', 8.10, TRUE
        UNION ALL
        SELECT '019b8128-0410-702b-8ab9-00002b1fac05'::uuid, 'The Green Mile', 189, 'Fantasy', DATE '1999-12-10', 8.60, TRUE
        UNION ALL
        SELECT '019b8128-07f8-702c-9564-00002c206894'::uuid, 'The Truman Show', 103, 'Comedy', DATE '1998-06-05', 8.20, TRUE
        UNION ALL
        SELECT '019b8128-0be0-702d-a00f-00002d212523'::uuid, 'Dead Poets Society', 128, 'Drama', DATE '1989-06-09', 8.10, TRUE
        UNION ALL
        SELECT '019b8128-0fc8-702e-aaba-00002e21e1b2'::uuid, 'The Sixth Sense', 107, 'Thriller', DATE '1999-08-06', 8.20, TRUE
        UNION ALL
        SELECT '019b8128-13b0-702f-b565-00002f229e41'::uuid, 'Unbreakable', 106, 'Thriller', DATE '2000-11-22', 7.30, TRUE
        UNION ALL
        SELECT '019b8128-1798-7030-8010-000030235ad0'::uuid, 'Split', 117, 'Thriller', DATE '2017-01-20', 7.30, TRUE
        UNION ALL
        SELECT '019b8128-1b80-7031-8abb-00003124175f'::uuid, 'Get Out', 104, 'Horror', DATE '2017-02-24', 7.80, TRUE
        UNION ALL
        SELECT '019b8128-1f68-7032-9566-00003224d3ee'::uuid, 'Nope', 130, 'Horror', DATE '2022-07-22', 6.80, TRUE
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
        UNION ALL
        SELECT '019b864d-bc00-7001-8aab-00000100bc8f'::uuid, 'Jonathan', 'Demme', 'American', DATE '1944-02-22', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-bfe8-7002-9556-00000201791e'::uuid, 'Steven', 'Spielberg', 'American', DATE '1946-12-18', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-c3d0-7003-a001-0000030235ad'::uuid, 'Wes', 'Anderson', 'American', DATE '1969-05-01', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-c7b8-7004-aaac-00000402f23c'::uuid, 'Damien', 'Chazelle', 'American', DATE '1985-01-19', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b864d-cba0-7005-b557-00000503aecb'::uuid, 'Denis', 'Villeneuve', 'Canadian', DATE '1967-10-03', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b864d-cf88-7006-8002-000006046b5a'::uuid, 'George', 'Miller', 'Australian', DATE '1945-03-03', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-d370-7007-8aad-0000070527e9'::uuid, 'Joel', 'Coen', 'American', DATE '1954-11-29', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-d758-7008-9558-00000805e478'::uuid, 'Peter', 'Weir', 'Australian', DATE '1944-08-21', 'Director y guionista.', TRUE
        UNION ALL
        SELECT '019b864d-db40-7009-a003-00000906a107'::uuid, 'M. Night', 'Shyamalan', 'American', DATE '1970-08-06', 'Director, guionista y productor.', TRUE
        UNION ALL
        SELECT '019b864d-df28-700a-aaae-00000a075d96'::uuid, 'Jordan', 'Peele', 'American', DATE '1979-02-21', 'Director y guionista.', TRUE
    ) AS temp_data
WHERE
    NOT EXISTS (
        SELECT 1
        FROM authors
        LIMIT 1
    );

INSERT INTO movie_authors (movie_id, author_id)
SELECT movie_id, author_id
FROM (VALUES
        ('019b76da-a800-7001-8aab-00000100bc8f'::uuid, '019b7c01-0400-7001-8aab-00000100bc8f'::uuid),
        ('019b76da-abe8-7002-9556-00000201791e'::uuid, '019b7c01-07e8-7002-9556-00000201791e'::uuid),
        ('019b76da-afd0-7003-a001-0000030235ad'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b76da-b3b8-7004-aaac-00000402f23c'::uuid, '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid),
        ('019b76da-b7a0-7005-b557-00000503aecb'::uuid, '019b7c01-13a0-7005-b557-00000503aecb'::uuid),
        ('019b76da-bb88-7006-8002-000006046b5a'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b76da-bf70-7007-8aad-0000070527e9'::uuid, '019b7c01-1788-7006-8002-000006046b5a'::uuid),
        ('019b76da-c358-7008-9558-00000805e478'::uuid, '019b7c01-1b70-7007-8aad-0000070527e9'::uuid),
        ('019b76da-c358-7008-9558-00000805e478'::uuid, '019b7c01-1f58-7008-9558-00000805e478'::uuid),
        ('019b76da-c740-7009-a003-00000906a107'::uuid, '019b7c01-2340-7009-a003-00000906a107'::uuid),
        ('019b76da-cb28-700a-aaae-00000a075d96'::uuid, '019b7c01-2728-700a-aaae-00000a075d96'::uuid),
        ('019b76da-cf10-700b-b559-00000b081a25'::uuid, '019b7c01-2b10-700b-b559-00000b081a25'::uuid),
        ('019b76da-d2f8-700c-8004-00000c08d6b4'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b76da-d6e0-700d-8aaf-00000d099343'::uuid, '019b7c01-2ef8-700c-8004-00000c08d6b4'::uuid),
        ('019b76da-dac8-700e-955a-00000e0a4fd2'::uuid, '019b7c01-32e0-700d-8aaf-00000d099343'::uuid),
        ('019b76da-deb0-700f-a005-00000f0b0c61'::uuid, '019b7c01-36c8-700e-955a-00000e0a4fd2'::uuid),
        ('019b76da-deb0-700f-a005-00000f0b0c61'::uuid, '019b7c01-3ab0-700f-a005-00000f0b0c61'::uuid),
        ('019b76da-e298-7010-aab0-0000100bc8f0'::uuid, '019b7c01-3e98-7010-aab0-0000100bc8f0'::uuid),
        ('019b76da-e680-7011-b55b-0000110c857f'::uuid, '019b7c01-4280-7011-b55b-0000110c857f'::uuid),
        ('019b76da-ea68-7012-8006-0000120d420e'::uuid, '019b7c01-4668-7012-8006-0000120d420e'::uuid),
        ('019b76da-ee50-7013-8ab1-0000130dfe9d'::uuid, '019b7c01-4a50-7013-8ab1-0000130dfe9d'::uuid),
        ('019b8127-6000-7001-8aab-00000100bc8f'::uuid, '019b7c01-07e8-7002-9556-00000201791e'::uuid),
        ('019b8127-63e8-7002-9556-00000201791e'::uuid, '019b7c01-2728-700a-aaae-00000a075d96'::uuid),
        ('019b8127-67d0-7003-a001-0000030235ad'::uuid, '019b7c01-2728-700a-aaae-00000a075d96'::uuid),
        ('019b8127-6bb8-7004-aaac-00000402f23c'::uuid, '019b864d-bc00-7001-8aab-00000100bc8f'::uuid),
        ('019b8127-6fa0-7005-b557-00000503aecb'::uuid, '019b864d-bfe8-7002-9556-00000201791e'::uuid),
        ('019b8127-7388-7006-8002-000006046b5a'::uuid, '019b864d-bfe8-7002-9556-00000201791e'::uuid),
        ('019b8127-7770-7007-8aad-0000070527e9'::uuid, '019b864d-bfe8-7002-9556-00000201791e'::uuid),
        ('019b8127-7b58-7008-9558-00000805e478'::uuid, '019b864d-bfe8-7002-9556-00000201791e'::uuid),
        ('019b8127-7f40-7009-a003-00000906a107'::uuid, '019b864d-bfe8-7002-9556-00000201791e'::uuid),
        ('019b8127-8328-700a-aaae-00000a075d96'::uuid, '019b7c01-2340-7009-a003-00000906a107'::uuid),
        ('019b8127-8710-700b-b559-00000b081a25'::uuid, '019b7c01-2340-7009-a003-00000906a107'::uuid),
        ('019b8127-8af8-700c-8004-00000c08d6b4'::uuid, '019b7c01-2340-7009-a003-00000906a107'::uuid),
        ('019b8127-8ee0-700d-8aaf-00000d099343'::uuid, '019b7c01-2340-7009-a003-00000906a107'::uuid),
        ('019b8127-92c8-700e-955a-00000e0a4fd2'::uuid, '019b7c01-1788-7006-8002-000006046b5a'::uuid),
        ('019b8127-96b0-700f-a005-00000f0b0c61'::uuid, '019b7c01-1788-7006-8002-000006046b5a'::uuid),
        ('019b8127-9a98-7010-aab0-0000100bc8f0'::uuid, '019b7c01-1788-7006-8002-000006046b5a'::uuid),
        ('019b8127-9e80-7011-b55b-0000110c857f'::uuid, '019b7c01-4a50-7013-8ab1-0000130dfe9d'::uuid),
        ('019b8127-a268-7012-8006-0000120d420e'::uuid, '019b7c01-4a50-7013-8ab1-0000130dfe9d'::uuid),
        ('019b8127-a650-7013-8ab1-0000130dfe9d'::uuid, '019b7c01-4a50-7013-8ab1-0000130dfe9d'::uuid),
        ('019b8127-aa38-7014-955c-0000140ebb2c'::uuid, '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-ae20-7015-a007-0000150f77bb'::uuid, '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-b208-7016-aab2-00001610344a'::uuid, '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-b5f0-7017-b55d-00001710f0d9'::uuid, '019b7c01-0fb8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-b9d8-7018-8008-00001811ad68'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b8127-bdc0-7019-8ab3-0000191269f7'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b8127-c1a8-701a-955e-00001a132686'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b8127-c590-701b-a009-00001b13e315'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b8127-c978-701c-aab4-00001c149fa4'::uuid, '019b7c01-0bd0-7003-a001-0000030235ad'::uuid),
        ('019b8127-cd60-701d-b55f-00001d155c33'::uuid, '019b864d-c3d0-7003-a001-0000030235ad'::uuid),
        ('019b8127-d148-701e-800a-00001e1618c2'::uuid, '019b864d-c3d0-7003-a001-0000030235ad'::uuid),
        ('019b8127-d530-701f-8ab5-00001f16d551'::uuid, '019b864d-c3d0-7003-a001-0000030235ad'::uuid),
        ('019b8127-d918-7020-9560-0000201791e0'::uuid, '019b864d-c7b8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-dd00-7021-a00b-000021184e6f'::uuid, '019b864d-c7b8-7004-aaac-00000402f23c'::uuid),
        ('019b8127-e0e8-7022-aab6-000022190afe'::uuid, '019b864d-cba0-7005-b557-00000503aecb'::uuid),
        ('019b8127-e4d0-7023-b561-00002319c78d'::uuid, '019b864d-cba0-7005-b557-00000503aecb'::uuid),
        ('019b8127-e8b8-7024-800c-0000241a841c'::uuid, '019b864d-cba0-7005-b557-00000503aecb'::uuid),
        ('019b8127-eca0-7025-8ab7-0000251b40ab'::uuid, '019b864d-cba0-7005-b557-00000503aecb'::uuid),
        ('019b8127-f088-7026-9562-0000261bfd3a'::uuid, '019b864d-cba0-7005-b557-00000503aecb'::uuid),
        ('019b8127-f470-7027-a00d-0000271cb9c9'::uuid, '019b864d-cf88-7006-8002-000006046b5a'::uuid),
        ('019b8127-f858-7028-aab8-0000281d7658'::uuid, '019b864d-d370-7007-8aad-0000070527e9'::uuid),
        ('019b8127-fc40-7029-b563-0000291e32e7'::uuid, '019b864d-d370-7007-8aad-0000070527e9'::uuid),
        ('019b8128-0028-702a-800e-00002a1eef76'::uuid, '019b864d-d370-7007-8aad-0000070527e9'::uuid),
        ('019b8128-0410-702b-8ab9-00002b1fac05'::uuid, '019b7c01-0400-7001-8aab-00000100bc8f'::uuid),
        ('019b8128-07f8-702c-9564-00002c206894'::uuid, '019b864d-d758-7008-9558-00000805e478'::uuid),
        ('019b8128-0be0-702d-a00f-00002d212523'::uuid, '019b864d-d758-7008-9558-00000805e478'::uuid),
        ('019b8128-0fc8-702e-aaba-00002e21e1b2'::uuid, '019b864d-db40-7009-a003-00000906a107'::uuid),
        ('019b8128-13b0-702f-b565-00002f229e41'::uuid, '019b864d-db40-7009-a003-00000906a107'::uuid),
        ('019b8128-1798-7030-8010-000030235ad0'::uuid, '019b864d-db40-7009-a003-00000906a107'::uuid),
        ('019b8128-1b80-7031-8abb-00003124175f'::uuid, '019b864d-df28-700a-aaae-00000a075d96'::uuid),
        ('019b8128-1f68-7032-9566-00003224d3ee'::uuid, '019b864d-df28-700a-aaae-00000a075d96'::uuid)
    ) AS seed(movie_id, author_id)
WHERE
    NOT EXISTS (
        SELECT 1
        FROM movie_authors
        LIMIT 1
    );
