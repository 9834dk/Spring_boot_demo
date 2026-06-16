package com.example.Spring_boot_demo.dto;

/**
 * 学生数据传输对象 (Data Transfer Object)
 * 用于在 Controller 层与 Service 层之间传递数据，
 * 或者用于返回给前端，从而避免直接暴露底层数据库实体（Entity）。
 */
public class StudentDTO {
    /**
     * 学生的唯一标识 ID
     */
    private long id;

    /**
     * 学生的姓名
     */
    private String name;

    /**
     * 学生的电子邮件地址
     */
    private String email;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
