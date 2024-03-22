package com.example.imagemanager.admin.dao;

import com.example.imagemanager.admin.model.SysPermission;
import com.example.imagemanager.admin.model.SysRole;
import com.example.imagemanager.admin.model.UserPermissionResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserPermissionMapper {


    @Select("SELECT DISTINCT u.id, ur.role_id " +
            "FROM sys_user u " +
            "JOIN user_role ur ON u.id = ur.user_id " +
            "WHERE u.id = #{uid}")
    @Results({
            @Result(property = "uid", column = "id"),
            @Result(property = "sysRole", column = "role_id", javaType = SysRole.class, one = @One(select = "getSysRole")),
    })
    UserPermissionResult getPermission(@Param("uid") String uid);

    @Select("SELECT r.id as role_id, r.name as role_name, r.description as role_description, " +
            "p.id as permission_id, p.code as permission_code, p.level as permission_level, " +
            "p.name as permission_name, p.description as permission_description, p.url as permission_url " +
            "FROM role r " +
            "JOIN role_permission rp ON r.id = rp.role_id " +
            "JOIN permission p ON rp.permission_id = p.id " +
            "WHERE r.id = #{roleId} LIMIT 1")
    @Results({
            @Result(property = "id", column = "role_id"),
            @Result(property = "name", column = "role_name"),
            @Result(property = "description", column = "role_description"),
            @Result(property = "permissions", column = "role_id", javaType = List.class, many = @Many(select = "getRolePermissions")),
    })
    SysRole getSysRole(@Param("roleId") int roleId);

    @Select("SELECT r.id as role_id, r.name as role_name, r.description as role_description " +
            "FROM role r where  1 = 1")
    @Results({
            @Result(property = "id", column = "role_id"),
            @Result(property = "name", column = "role_name"),
            @Result(property = "description", column = "role_description")
    })
    List<SysRole> getSysRoles();

    @Select("SELECT p.id, p.code , p.level ,p.parent, " +
            "p.name , p.description , p.url " +
            "FROM permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<SysPermission> getRolePermissions(@Param("roleId") int roleId);


    @Insert(" INSERT INTO role (name, description) values (#{name},#{description})")
    void addRole(@Param("name") String name,@Param("description") String description);

    @Delete("delete from role where id = #{id}")
    void deleteRole(@Param("id") int id);

    /**
     * 查询所有的权限
     * @return
     */
    @Select("select * from permission where 1 = 1")
    List<SysPermission> getAllPermission();

    /**
     * 修改权限
     * @param id
     * @param code
     * @param name
     * @param description
     * @param parent
     * @param level
     * @param url
     */
    @Update("UPDATE permission SET code = #{code}, name = #{name}, description = #{description}, parent = #{parent} ,level = #{level},url = #{url} WHERE id = #{id}")
    void updatePermission(int id,int code,String name,String description,int parent,int level,String url);

    /**
     * 删除权限
     * @param id
     */
    @Delete("delete from permission where id = #{id}")
    void deletePermission(int id);

    /**
     * 增加权限
     * @param code
     * @param name
     * @param description
     * @param parent
     * @param level
     * @param url
     */
    @Insert("insert into permission(code,name,description,parent,level,url) values(#{code},#{name},#{description},#{parent},#{level},#{url})")
    void addPermission(int code,String name,String description,int parent,int level,String url);

    @Insert("insert into role_permission(role_id,permission_id) values(#{roleId} , #{pid})")
    void updateRolePerm(int roleId, int pid);

    @Delete("delete from role_permission where role_id =#{roleId}")
    void deleteRolePerm(int roleId);

    @Select("select r.id,r.name,r.description from role r " +
            "left join user_role ur on r.id = ur.role_id where ur.user_id = #{uid}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description")
    })
    SysRole getSysRoleByUid(String uid);


    @Update("update user_role set role_id = #{roleId} where user_id = #{uid}")
    void updateUserRole(String uid, int roleId);

    @Update("update role set name = #{name},description = #{description} where id = #{id}")
    void updateRole(int id, String name, String description);
}