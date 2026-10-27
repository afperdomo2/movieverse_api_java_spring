-- Solo insertar datos si la tabla movies está vacía
INSERT INTO
    movies (
        title,
        duration,
        genre,
        release_date,
        rating,
        is_active
    )
SELECT
    title,
    duration,
    genre,
    release_date,
    rating,
    is_active
FROM (
        SELECT
            'The Shawshank Redemption' as title, 142 as duration, 'Drama' as genre, DATE '1994-09-23' as release_date, 9.30 as rating, TRUE as is_active
        UNION ALL
        SELECT 'The Godfather', 175, 'Crime', DATE '1972-03-24', 9.20, TRUE
        UNION ALL
        SELECT 'The Dark Knight', 152, 'Action', DATE '2008-07-18', 9.00, TRUE
        UNION ALL
        SELECT 'Pulp Fiction', 154, 'Crime', DATE '1994-10-14', 8.90, TRUE
        UNION ALL
        SELECT 'Forrest Gump', 142, 'Drama', DATE '1994-07-06', 8.80, TRUE
        UNION ALL
        SELECT 'Inception', 148, 'Sci-Fi', DATE '2010-07-16', 8.80, TRUE
        UNION ALL
        SELECT 'Fight Club', 139, 'Drama', DATE '1999-10-15', 8.80, TRUE
        UNION ALL
        SELECT 'The Matrix', 136, 'Sci-Fi', DATE '1999-03-31', 8.70, TRUE
        UNION ALL
        SELECT 'Goodfellas', 146, 'Crime', DATE '1990-09-19', 8.70, TRUE
        UNION ALL
        SELECT 'The Lord of the Rings: The Return of the King', 201, 'Fantasy', DATE '2003-12-17', 8.90, TRUE
        UNION ALL
        SELECT 'Star Wars: Episode V - The Empire Strikes Back', 124, 'Sci-Fi', DATE '1980-05-21', 8.70, TRUE
        UNION ALL
        SELECT 'Interstellar', 169, 'Sci-Fi', DATE '2014-11-07', 8.60, TRUE
        UNION ALL
        SELECT 'Parasite', 132, 'Thriller', DATE '2019-05-30', 8.60, TRUE
        UNION ALL
        SELECT 'Joker', 122, 'Crime', DATE '2019-10-04', 8.50, TRUE
        UNION ALL
        SELECT 'Avengers: Endgame', 181, 'Action', DATE '2019-04-26', 8.40, TRUE
        UNION ALL
        SELECT 'Titanic', 195, 'Romance', DATE '1997-12-19', 7.80, TRUE
        UNION ALL
        SELECT 'Toy Story', 81, 'Animation', DATE '1995-11-22', 8.30, TRUE
        UNION ALL
        SELECT 'Finding Nemo', 100, 'Animation', DATE '2003-05-30', 8.10, TRUE
        UNION ALL
        SELECT 'Gladiator', 155, 'Action', DATE '2000-05-05', 8.50, TRUE
    ) AS temp_data
WHERE
    NOT EXISTS (
        SELECT 1
        FROM movies
        LIMIT 1
    );
