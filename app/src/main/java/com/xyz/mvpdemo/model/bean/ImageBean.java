package com.xyz.mvpdemo.model.bean;

import java.util.List;

/**
 * 作者：xy_z on 2016/6/14 09:54
 * 邮箱：xyz@163.com
 */
public class ImageBean {

    public List<PinsEntity> pins;

    public class PinsEntity {
        public int pin_id;
        public int user_id;
        public int board_id;
        public int file_id;
        public FileEntity file;
        public int media_type;
        public Object source;
        public Object link;
        public String raw_text;
        public TextMetaEntity text_meta;
        public int via;
        public int via_user_id;
        public Object original;
        public int created_at;
        public int like_count;
        public int comment_count;
        public int repin_count;
        public int is_private;
        public Object orig_source;
        public UserEntity user;
        public BoardEntity board;

        public class FileEntity {
            public String farm;
            public String bucket;
            public String key;
            public String type;
            public int height;
            public int width;
            public String frames;
        }

        public class TextMetaEntity {
        }

        public class UserEntity {
            public int user_id;
            public String username;
            public String urlname;
            public int created_at;
            public AvatarEntity avatar;

            public class AvatarEntity {
                public int id;
                public String farm;
                public String bucket;
                public String key;
                public String type;
                public int width;
                public int height;
                public int frames;
            }
        }

        public class BoardEntity {
            public int board_id;
            public int user_id;
            public String title;
            public String description;
            public String category_id;
            public int seq;
            public int pin_count;
            public int follow_count;
            public int like_count;
            public int created_at;
            public int updated_at;
            public int deleting;
            public int is_private;
            public Object extra;
        }
    }
}
