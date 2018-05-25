package com.xlzhen.cathouse.entity;

/**
 * Created by xlzhen on 2/20 0020.
 * 图片 实体类
 */

public class PictureEntity {
    private int pin_id;
    private int user_id;
    private int board_id;
    private int file_id;
    private FileEntity file;
    private BoardEntity board;
    private int media_type;
    private String source;
    private String link;
    private String raw_text;
    private int via;
    private int via_user_id;
    private int original;
    private int created_at;
    private int like_count;
    private int comment_count;
    private int repin_count;
    private int is_private;
    private boolean hide_origin;

    public int getPin_id() {
        return pin_id;
    }

    public void setPin_id(int pin_id) {
        this.pin_id = pin_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public int getMedia_type() {
        return media_type;
    }

    public void setMedia_type(int media_type) {
        this.media_type = media_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRaw_text() {
        return raw_text;
    }

    public void setRaw_text(String raw_text) {
        this.raw_text = raw_text;
    }

    public int getVia() {
        return via;
    }

    public void setVia(int via) {
        this.via = via;
    }

    public int getVia_user_id() {
        return via_user_id;
    }

    public void setVia_user_id(int via_user_id) {
        this.via_user_id = via_user_id;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getRepin_count() {
        return repin_count;
    }

    public void setRepin_count(int repin_count) {
        this.repin_count = repin_count;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public boolean isHide_origin() {
        return hide_origin;
    }

    public void setHide_origin(boolean hide_origin) {
        this.hide_origin = hide_origin;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "PictureEntity{" +
                "pin_id=" + pin_id +
                ", user_id=" + user_id +
                ", board_id=" + board_id +
                ", file_id=" + file_id +
                ", file=" + file +
                ", board=" + board +
                ", media_type=" + media_type +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                ", raw_text='" + raw_text + '\'' +
                ", via=" + via +
                ", via_user_id=" + via_user_id +
                ", original=" + original +
                ", created_at=" + created_at +
                ", like_count=" + like_count +
                ", comment_count=" + comment_count +
                ", repin_count=" + repin_count +
                ", is_private=" + is_private +
                ", hide_origin=" + hide_origin +
                '}';
    }
}
