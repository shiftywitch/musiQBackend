DROP TABLE IF EXISTS QueueItem;
DROP TABLE IF EXISTS Queue;

CREATE TABLE Queue (
                       id serial primary key,
                       title varchar(100)
);

CREATE TABLE QueueItem (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(100) NOT NULL,
                           description TEXT,
                           urls TEXT [],
                           queueId INTEGER REFERENCES Queue(id)
);