alter TABLE user_infos add column open_profile boolean;

create TABLE friend_requests (
id BIGSERIAL NOT NULL,
sender_id bigint REFERENCES users(id),
recipient_id bigint REFERENCES users(id),
is_accepted boolean NOT NULL,
CONSTRAINT friend_requests_pk PRIMARY KEY (id)
);


create TABLE chats (
id BIGSERIAL NOT NULL,
name varchar (64) NOT NULL,
chat_type varchar (32) NOT NULL,
owner_id bigint REFERENCES users(id),
CONSTRAINT chats_pk PRIMARY KEY (id)
);

create TABLE messages (
id BIGSERIAL NOT NULL,
sender_id bigint  NOT NULL REFERENCES users(id),
text varchar (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
chat_id bigint REFERENCES chats(id),
CONSTRAINT messages_pk PRIMARY KEY (id)
);


create TABLE chat_participations (
id BIGSERIAL NOT NULL,
user_id bigint REFERENCES users(id),
chat_id bigint REFERENCES chats(id),
CONSTRAINT chat_participations_pk PRIMARY KEY (id)
);

create TABLE table_groups (
id BIGSERIAL NOT NULL,
name varchar (64) NOT NULL,
info varchar (255) NOT NULL,
wall_type varchar (32) NOT NULL,
open_to_join boolean NOT NULL,
CONSTRAINT table_groups_pk PRIMARY KEY (id)
);

create TABLE group_memberships (
id BIGSERIAL NOT NULL,
user_id bigint REFERENCES users(id),
group_id bigint REFERENCES table_groups(id),
group_role varchar (32) NOT NULL,
CONSTRAINT group_memberships_pk PRIMARY KEY (id)
);

create TABLE posts (
id BIGSERIAL NOT NULL,
text varchar (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
author_id bigint REFERENCES users(id),
group_id bigint REFERENCES table_groups(id),
wall_id bigint REFERENCES walls(id),
is_published boolean NOT NULL DEFAULT true,
CONSTRAINT posts_pk PRIMARY KEY (id)
);


create TABLE comments (
id BIGSERIAL NOT NULL,
author_id bigint REFERENCES users(id),
text varchar (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
post_id bigint REFERENCES posts(id),
CONSTRAINT comments_pk PRIMARY KEY (id)
);

create TABLE likes (
id BIGSERIAL NOT NULL,
user_id bigint REFERENCES users(id),
sending_time TIMESTAMP NOT NULL,
comment_id bigint REFERENCES comments(id),
post_id bigint REFERENCES posts(id),
CONSTRAINT likes_pk PRIMARY KEY (id)
);