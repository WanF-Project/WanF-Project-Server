SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE user_fcm_tokens;
TRUNCATE TABLE user;
TRUNCATE TABLE club;
TRUNCATE TABLE club_auth;
TRUNCATE TABLE club_post;
TRUNCATE TABLE club_posts;
TRUNCATE TABLE image;
TRUNCATE TABLE major;
TRUNCATE TABLE course;
TRUNCATE TABLE message;
TRUNCATE table post_comments;
TRUNCATE TABLE comment;
TRUNCATE TABLE post;
TRUNCATE TABLE profile_personalities;
TRUNCATE TABLE profile_goals;
TRUNCATE TABLE profile;

INSERT INTO major (name)
VALUES ('컴퓨터공학과');

INSERT INTO IMAGE (convert_image_name, directory, image_url)
values ('default_cover_image.png', 0,
        'https://s3.ap-northeast-2.amazonaws.com/woowahan-courses/default/default_cover_image.png');


SET
REFERENTIAL_INTEGRITY TRUE

