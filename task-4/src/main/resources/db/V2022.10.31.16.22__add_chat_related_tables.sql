CREATE TABLE chats (
id BIGSERIAL NOT NULL,
name VARCHAR (128) NOT NULL,
chat_type SMALLINT NOT NULL,
owner_id bigint REFERENCES users(id) ON DELETE NO ACTION,
CONSTRAINT chats_pk PRIMARY KEY (id)
);

CREATE TABLE messages (
id BIGSERIAL NOT NULL,
sender_id bigint  NOT NULL REFERENCES users(id) ON DELETE NO ACTION,
message_text VARCHAR (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
chat_id bigint NOT NULL REFERENCES chats(id) ON DELETE CASCADE,
CONSTRAINT messages_pk PRIMARY KEY (id)
);


CREATE TABLE chat_participations (
id BIGSERIAL NOT NULL,
user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
chat_id bigint NOT NULL REFERENCES chats(id) ON DELETE CASCADE,
CONSTRAINT chat_participations_pk PRIMARY KEY (id)
);




