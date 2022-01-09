DROP TABLE IF EXISTS QueueItem;
DROP TABLE IF EXISTS Queue;

CREATE TABLE Queue (
                       id serial primary key,
                       ownerId varchar(150) not null,
                       title varchar(100) not null
);

CREATE TABLE QueueItem (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(100) NOT NULL,
                           description TEXT,
                           url VARCHAR(255),
                           queueId INTEGER NOT NULL
);

ALTER TABLE QueueItem ADD FOREIGN KEY (queueId)
REFERENCES Queue(id) ON DELETE CASCADE;