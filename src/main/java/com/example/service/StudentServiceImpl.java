package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.Spring_boot_demo.dao.Student;
import com.example.Spring_boot_demo.dao.StudentRepository;
import com.example.Spring_boot_demo.dto.StudentDTO;
import com.example.converter.StudentConverter;

import org.springframework.util.StringUtils;

/**
 * 学生服务实现类 (Service 层)
 * 处理具体业务逻辑，调用 DAO (Repository) 进行数据库操作
 */
@Service
public class StudentServiceImpl implements StudentService {
    
    // 注入数据库操作的 Repository
    @Autowired
    private StudentRepository studentRepository;

    /**
     * 查询指定 ID 的学生
     */
    @Override
    public StudentDTO getStudentById(long id) {
        // 在数据库中查找，如果找不到抛出运行时异常
        Student student = studentRepository.findById(id).orElseThrow(RuntimeException::new);
        // 将 Entity 转为 DTO 返回给 Controller
        return StudentConverter.convertStudent(student);
    }

    /**
     * 添加一个新学生
     */
    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        // 校验邮箱是否已经被占用
        List<Student> studentList = studentRepository.findByEmail(studentDTO.getEmail());
        if (!CollectionUtils.isEmpty(studentList)) {
            throw new IllegalStateException("email:" + studentDTO.getEmail() + "has been taken");
        }
        // 将传入的 DTO 转换为 Entity 然后保存到数据库
        Student student = studentRepository.save(StudentConverter.convertStudentDTO(studentDTO));
        return student.getId(); // 返回生成的数据库主键 ID
    }

    /**
     * 根据 ID 删除学生
     */
    @Override
    public void deleteStudentById(long id) {
        // 先检查是否存在该学生，不存在则抛出异常
        studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id:" + id + "doesn’t exist!"));
        // 调用 Repository 的删除方法
        studentRepository.deleteById(id);
    }

    /**
     * 更新指定学生的信息
     */
    @Override
    @Transactional // 开启事务保证数据库操作的一致性
    public StudentDTO updateStudentById(long id, String name, String email) {
        // 先从数据库查出该学生
        Student studentInDB = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id:" + id + "doesn’t exist!"));

        // 如果传入了 name 参数且与数据库中的不同，则更新名字
        if (StringUtils.hasLength(name) && !studentInDB.getName().equals(name)) {
            studentInDB.setName(name);
        }
        // 如果传入了 email 参数且与数据库中的不同，则更新邮箱
        if (StringUtils.hasLength(email) && !studentInDB.getEmail().equals(email)) {
            studentInDB.setEmail(email);
        }
        
        // 保存修改后的实体
        Student student = studentRepository.save(studentInDB);
        return StudentConverter.convertStudent(student);
    }
}
