package com.example.service;

import java.util.List;
import com.example.Spring_boot_demo.dto.StudentDTO;

/**
 * 学生服务层接口
 * 定义了针对学生的业务操作规范
 */
public interface StudentService {

    /**
     * 查询所有学生记录
     * @return 返回所有学生的列表
     */
    List<StudentDTO> getAllStudents();

    /**
     * 根据学生 ID 查询学生详细信息
     * @param id 学生的数据库主键
     * @return 返回封装好的学生 DTO 对象
     */
    StudentDTO getStudentById(long id);

    /**
     * 新增一名学生，并在内部校验邮箱是否重复
     * @param studentDTO 包含新生信息的传输对象
     * @return 返回成功插入数据库后的新学生 ID
     */
    Long addNewStudent(StudentDTO studentDTO);

    /**
     * 根据 ID 删除指定的学生记录
     * @param id 需要删除的学生的主键 ID
     */
    void deleteStudentById(long id);

    /**
     * 根据 ID 更新学生的部分信息（例如姓名和邮箱）
     * @param id 学生的主键 ID
     * @param name 新的姓名（如果传空或与原来相同则不更新）
     * @param email 新的邮箱（如果传空或与原来相同则不更新）
     * @param age 新的年龄（如果传空或与原来相同则不更新）
     * @param gender 新的性别
     * @param hobby 新的爱好
     * @param grade 新的年级
     * @return 返回更新成功后的学生 DTO 对象
     */
    StudentDTO updateStudentById(long id, String name, String email, Integer age, String gender, String hobby, String grade);
}