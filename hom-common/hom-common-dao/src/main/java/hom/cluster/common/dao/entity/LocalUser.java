package hom.cluster.common.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author visy.wang
 * @description: 本地用户
 * @date 2023/5/25 16:31
 */
@Data
@TableName("local_user")
public class LocalUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date createDt;
    private Date updateDt;
    private Date lastLoginDt;
}
