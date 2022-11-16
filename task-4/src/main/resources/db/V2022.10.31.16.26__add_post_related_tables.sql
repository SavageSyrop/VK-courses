CREATE TABLE posts (
id BIGSERIAL NOT NULL,
post_text VARCHAR (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
author_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
group_id bigint REFERENCES user_groups(id) ON DELETE CASCADE,
wall_id bigint REFERENCES walls(id) ON DELETE CASCADE,
is_published boolean NOT NULL DEFAULT true,
CONSTRAINT posts_pk PRIMARY KEY (id),
CONSTRAINT ck_mutuallyExclusiveFK CHECK ((group_id is null or wall_id is null) and not (wall_id is null and group_id is null))
);


CREATE TABLE comments (
id BIGSERIAL NOT NULL,
author_id bigint NOT NULL REFERENCES users(id) ON DELETE NO ACTION,
comment_text VARCHAR (255) NOT NULL,
sending_time TIMESTAMP NOT NULL,
post_id bigint NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
CONSTRAINT comments_pk PRIMARY KEY (id)
);

CREATE TABLE likes (
id BIGSERIAL NOT NULL,
user_id bigint NOT NULL REFERENCES users(id) ON DELETE NO ACTION,
sending_time TIMESTAMP NOT NULL,
comment_id bigint REFERENCES comments(id) ON DELETE CASCADE,
post_id bigint REFERENCES posts(id) ON DELETE CASCADE,
CONSTRAINT likes_pk PRIMARY KEY (id),
CONSTRAINT ck_mutuallyExclusiveFK CHECK ((comment_id is null or post_id is null) and not (comment_id is null and post_id is null))
);