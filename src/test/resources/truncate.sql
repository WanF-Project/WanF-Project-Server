SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE comment;
TRUNCATE TABLE course;
TRUNCATE TABLE major;
TRUNCATE TABLE post;
TRUNCATE TABLE profile;
TRUNCATE TABLE profile_goals;
TRUNCATE TABLE profile_personalities;
TRUNCATE TABLE user;
TRUNCATE table post_comments;

INSERT INTO major (name) VALUES ('컴퓨터공학과');

SET
REFERENTIAL_INTEGRITY TRUE

