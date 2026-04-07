import os

entity_dir = "src/main/java/com/rabbiter/association/entity"

# 1. delete SysRole and SysUserRole
for file in ["SysRole.java", "SysUserRole.java"]:
    path = os.path.join(entity_dir, file)
    if os.path.exists(path):
        os.remove(path)

# 2. Update existing entities: change @TableId(type = IdType.AUTO) to @TableId(value = "xxx_id", type = IdType.AUTO)
def update_table_id(file_name, id_col_name):
    path = os.path.join(entity_dir, file_name)
    if not os.path.exists(path): return
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    import re
    content = re.sub(r'@TableId\(type = IdType\.AUTO\)', f'@TableId(value = "{id_col_name}", type = IdType.AUTO)', content)
    
    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

update_table_id("SysUser.java", "user_id")
update_table_id("ClubType.java", "type_id")
update_table_id("ClubInfo.java", "club_id")
update_table_id("ClubApplication.java", "application_id")
update_table_id("ClubMember.java", "member_id")
update_table_id("ActivityInfo.java", "activity_id")
update_table_id("ActivityParticipant.java", "participant_id")
update_table_id("ActivityFeedback.java", "feedback_id")
update_table_id("NotificationInfo.java", "notification_id")
update_table_id("PaymentNotice.java", "notice_id")
update_table_id("PaymentRecord.java", "record_id")
update_table_id("WithdrawalRecord.java", "record_id")

# Also add memberStatus to SysUser.java
sys_user_path = os.path.join(entity_dir, "SysUser.java")
if os.path.exists(sys_user_path):
    with open(sys_user_path, 'r', encoding='utf-8') as f:
        content = f.read()
    if "memberStatus" not in content:
        content = content.replace("private Integer status;", "private Integer status;\n\n    @TableField(value = \"member_status\")\n    private Integer memberStatus;\n\n    public Integer getMemberStatus() { return memberStatus; }\n    public void setMemberStatus(Integer memberStatus) { this.memberStatus = memberStatus; }")
    with open(sys_user_path, 'w', encoding='utf-8') as f:
        f.write(content)

# Remove signInTime and signOutTime from ActivityParticipant.java
ap_path = os.path.join(entity_dir, "ActivityParticipant.java")
if os.path.exists(ap_path):
    with open(ap_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    with open(ap_path, 'w', encoding='utf-8') as f:
        skip = False
        for line in lines:
            if "signInTime" in line or "signOutTime" in line:
                continue
            if "getSignInTime" in line or "setSignInTime" in line or "getSignOutTime" in line or "setSignOutTime" in line:
                continue
            if "@JsonFormat" in line and ("sign_in_time" in ''.join(lines) or "sign_out_time" in ''.join(lines)):
                # Hacky: we might accidentally remove other JsonFormats. Let's just be careful.
                pass
            f.write(line)

# 3. Create new entities
new_entities = {
    "SysAdmin.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("sys_admin")
public class SysAdmin {
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    @TableField(value = "real_name")
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}""",
    "SysPresident.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("sys_president")
public class SysPresident {
    @TableId(value = "president_id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    @TableField(value = "real_name")
    private String realName;
    private String phone;
    private String email;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}""",
    "ActivitySignIn.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("activity_sign_in")
public class ActivitySignIn {
    @TableId(value = "sign_in_id", type = IdType.AUTO)
    private Long id;
    @TableField("activity_id")
    private Long activityId;
    @TableField("user_id")
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("sign_in_time")
    private Date signInTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Date getSignInTime() { return signInTime; }
    public void setSignInTime(Date signInTime) { this.signInTime = signInTime; }
}""",
    "ActivitySignOut.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("activity_sign_out")
public class ActivitySignOut {
    @TableId(value = "sign_out_id", type = IdType.AUTO)
    private Long id;
    @TableField("activity_id")
    private Long activityId;
    @TableField("user_id")
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("sign_out_time")
    private Date signOutTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Date getSignOutTime() { return signOutTime; }
    public void setSignOutTime(Date signOutTime) { this.signOutTime = signOutTime; }
}""",
    "FeedbackReply.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("feedback_reply")
public class FeedbackReply {
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Long id;
    @TableField("feedback_id")
    private Long feedbackId;
    @TableField("user_id")
    private Long userId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}""",
    "ActivityTweet.java": """package com.rabbiter.association.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
@TableName("activity_tweet")
public class ActivityTweet {
    @TableId(value = "tweet_id", type = IdType.AUTO)
    private Long id;
    @TableField("activity_id")
    private Long activityId;
    @TableField("publisher_id")
    private Long publisherId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}"""
}

for name, content in new_entities.items():
    with open(os.path.join(entity_dir, name), 'w', encoding='utf-8') as f:
        f.write(content)
