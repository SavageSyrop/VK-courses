CREATE TABLE friend_requests (
id BIGSERIAL NOT NULL,
sender_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
recipient_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
is_accepted boolean NOT NULL,
CONSTRAINT friend_requests_pk PRIMARY KEY (id)
);