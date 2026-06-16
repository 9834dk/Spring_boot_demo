package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Response;
import com.example.Spring_boot_demo.dao.Student;
import com.example.Spring_boot_demo.dto.StudentDTO;
import com.example.service.StudentService;

/**
 * 学生控制器 (Controller 层)
 * 负责接收和处理所有关于学生（Student）的 HTTP 请求，并返回统一的 Response 结果
 */
@RestController
public class StudentController {
    
    // 注入 Service 层的依赖
    @Autowired
    private StudentService studentService;

    /**
     * 根据学生 ID 查询学生信息
     * @param id 学生的唯一标识 (通过 URL 路径传递)
     * @return 包含学生数据 (StudentDTO) 的统一响应对象
     */
    @GetMapping("/student/{id}")
    public Response<StudentDTO> getStudentById(@PathVariable long id) {
        return Response.newSuccess(studentService.getStudentById(id));
    }

    /**
     * 新增一名学生
     * @param studentDTO 包含学生基本信息的数据传输对象 (通过请求体 JSON 传递)
     * @return 成功后返回新创建学生的 ID
     */
    @PostMapping("/student")
    public Response<Long> addNewStudent(@RequestBody StudentDTO studentDTO) {
        return Response.newSuccess(studentService.addNewStudent(studentDTO));
    }

    /**
     * 根据学生 ID 删除一名学生
     * @param id 待删除学生的唯一标识
     * @return 删除成功返回空的成功响应
     */
    @DeleteMapping("/student/{id}")
    public Response<Void> deleteStudentById(@PathVariable long id) {
        studentService.deleteStudentById(id);
        return Response.newSuccess(null);
    }

    /**
     * 更新学生信息 (名字或邮箱)
     * @param id 需要更新的学生的 ID
     * @param name 新的名字 (可选参数)
     * @param email 新的邮箱 (可选参数)
     * @return 返回更新后的学生信息
     */
    @PutMapping("/student/{id}")
    public Response<StudentDTO> updateStudentById(@PathVariable long id, 
                                                  @RequestParam(required = false) String name, 
                                                  @RequestParam(required = false) String email) {
        return Response.newSuccess(studentService.updateStudentById(id, name, email));
    }
}
