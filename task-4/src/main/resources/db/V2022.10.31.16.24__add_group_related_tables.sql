CREATE TABLE user_groups (
id BIGSERIAL NOT NULL,
name VARCHAR (128) NOT NULL,
info VARCHAR (255) NOT NULL,
wall_type VARCHAR (128) NOT NULL,
open_to_join boolean NOT NULL,
CONSTRAINT user_groups_pk PRIMARY KEY (id)
);

CREATE TABLE group_memberships (
id BIGSERIAL NOT NULL,
user_id bigint NOT NULL REFERENCES users(id) ON DELETE CASCADE,
group_id bigint NOT NULL REFERENCES user_groups(id) ON DELETE CASCADE,
group_role VARCHAR (128) NOT NULL,
CONSTRAINT group_memberships_pk PRIMARY KEY (id)
);