# voluntary-filling-system
## 一. 项目说明

[项目Demo体验](https://www.515code.com/posts/t6po20qr/)

这是一个大学生社团或组织的新生志愿填报管理系统，后端使用 SpringBoot 开发，前端使用 HTML5 / CSS / JavaScript，数据库使用 MySQL。

它功能齐全，界面美观（至少我这样认为），你可以将它用于课程设计、开发学习等。

具体开发环境：IntelliJ IDEA 2020.2.3 / JDK 1.8.0_181 / MySQL 5.7.32 / SpringBoot 2.3.3 / Tomcat 8.6.5

为了部署方便，我将项目原型合并成了一个 SpringBoot 项目（加入了 Thymeleaf 模板，实质上还是前后端分离的，有必要可以自己拆分出来）。

## 二. 数据库设计

先在 Navicat 中新建数据库：`volunteer`，Character Set 选择 `utf8mb4`，Collation 选择 `utf8mb4_general_ci`

运行项目前，**请按顺序创建表**。

1.专业表 (major)

```sql
CREATE TABLE `major` (
  `major_id` int(5) NOT NULL COMMENT '专业编号',
  `major_name` varchar(15) NOT NULL COMMENT '专业名称',
  `class_num` smallint(2) NOT NULL COMMENT '班级个数',
  PRIMARY KEY (`major_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

2.组织表 (organization)

````sql
CREATE TABLE `organization` (
  `org_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '组织编号',
  `org_name` varchar(20) NOT NULL COMMENT '组织名称',
  `org_des` varchar(100) DEFAULT NULL COMMENT '组织描述/介绍',
  `manager_name` varchar(20) DEFAULT NULL COMMENT '组织负责人名字',
  PRIMARY KEY (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
````

3.分支表 (branch)

```sql
CREATE TABLE `branch` (
  `org_id` int(5) NOT NULL COMMENT '组织编号',
  `branch_id` int(5) NOT NULL COMMENT '下属组织编号',
  `branch_name` varchar(20) DEFAULT NULL COMMENT '下属组织名称',
  `branch_des` varchar(20) DEFAULT NULL COMMENT '下属组织描述',
  `manager_name` varchar(20) DEFAULT NULL COMMENT '下属组织负责人',
  PRIMARY KEY (`branch_id`) USING BTREE,
  KEY `branch_ibfk1` (`org_id`) USING BTREE,
  CONSTRAINT `branch_ibfk1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`org_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

4.学生表 (student)

```sql
CREATE TABLE `student` (
  `stu_id` int(8) NOT NULL COMMENT '学生学号',
  `stu_name` varchar(12) NOT NULL COMMENT '学生姓名',
  `major_id` int(5) NOT NULL COMMENT '所属专业id',
  `stu_class` smallint(2) NOT NULL COMMENT '学生班级 用整形数字表示',
  `stu_phone` varchar(11) NOT NULL COMMENT '手机号',
  `stu_qq` varchar(11) DEFAULT NULL COMMENT 'qq号',
  `branch_id` int(5) DEFAULT NULL COMMENT '所属组织id',
  PRIMARY KEY (`stu_id`) USING BTREE,
  KEY `student_ibfk1` (`major_id`) USING BTREE,
  KEY `student_ibfk2` (`branch_id`) USING BTREE,
  CONSTRAINT `student_ibfk1` FOREIGN KEY (`major_id`) REFERENCES `major` (`major_id`) ON UPDATE CASCADE,
  CONSTRAINT `student_ibfk2` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

5.志愿表 (report)

```sql
CREATE TABLE `report` (
  `report_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '志愿编号',
  `student_id` int(8) NOT NULL COMMENT '学生学号',
  `vol_first` int(5) NOT NULL COMMENT '下属组织编号1',
  `vol_second` int(5) DEFAULT NULL COMMENT '下属组织编号2',
  `reason_first` varchar(500) NOT NULL,
  `reason_second` varchar(500) DEFAULT NULL,
  `is_dispensing` smallint(1) NOT NULL COMMENT '是否调剂 0/1',
  `status` smallint(1) DEFAULT '0' COMMENT '录取状态 0/1/2',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(300) DEFAULT NULL COMMENT '面试官的评价',
  PRIMARY KEY (`report_id`) USING BTREE,
  UNIQUE KEY `student_id` (`student_id`) USING BTREE,
  KEY `report_ibfk1` (`vol_first`) USING BTREE,
  KEY `report_ibfk2` (`vol_second`) USING BTREE,
  CONSTRAINT `report_ibfk1` FOREIGN KEY (`vol_first`) REFERENCES `branch` (`branch_id`) ON UPDATE CASCADE,
  CONSTRAINT `report_ibfk2` FOREIGN KEY (`vol_second`) REFERENCES `branch` (`branch_id`) ON UPDATE CASCADE,
  CONSTRAINT `report_ibfk3` FOREIGN KEY (`student_id`) REFERENCES `student` (`stu_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

6.用户表 (user)

```sql
CREATE TABLE `user` (
  `user_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `user_name` varchar(12) NOT NULL COMMENT '用户名',
  `real_name` varchar(12) NOT NULL COMMENT '使用者真实姓名',
  `user_password` varchar(16) NOT NULL COMMENT '密码',
  `branch_id` int(5) NOT NULL,
  `user_status` smallint(1) NOT NULL COMMENT '用户权限 1为管理员 2为超级管理员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `user_ibfk1` (`branch_id`) USING BTREE,
  CONSTRAINT `user_ibfk1` FOREIGN KEY (`branch_id`) REFERENCES `branch` (`branch_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
```

7.志愿视图 (report_view)

在 Navicat 中选择 volunteer 数据库，在 Views 中用以下 SQL 新建视图：

```sql
SELECT `a`.`stu_id` AS `stu_id`, `a`.`stu_name` AS `stu_name`, `b`.`major_name` AS `major_name`, `a`.`stu_class` AS `stu_class`, `a`.`stu_phone` AS `stu_phone`
	, `a`.`stu_qq` AS `stu_qq`, `e`.`org_name` AS `org_first`, `d`.`branch_name` AS `bra_first`, `g`.`org_name` AS `org_second`, `f`.`branch_name` AS `bra_second`
	, `c`.`reason_first` AS `reason_first`, `c`.`reason_second` AS `reason_second`, `c`.`is_dispensing` AS `is_dispensing`, `c`.`status` AS `status`, `c`.`update_time` AS `update_time`
	, `c`.`create_time` AS `create_time`, `c`.`remark` AS `remark`
FROM `student` `a`
	LEFT JOIN `major` `b` ON `a`.`major_id` = `b`.`major_id`
	LEFT JOIN `report` `c` ON `a`.`stu_id` = `c`.`student_id`
	LEFT JOIN `branch` `d` ON `c`.`vol_first` = `d`.`branch_id`
	LEFT JOIN `organization` `e` ON `d`.`org_id` = `e`.`org_id`
	LEFT JOIN `branch` `f` ON `c`.`vol_second` = `f`.`branch_id`
	LEFT JOIN `organization` `g` ON `f`.`org_id` = `g`.`org_id`
```

## 三. API接口

详见 [api.md](./api.md) 文件。

## 四. 目录结构

后端文件 src -> main -> java

前端文件 src -> main -> resources -> web/static

配置文件 src -> main -> resources -> application.yml

## 五. 使用方法

1.添加数据到数据库中（执行sql文件夹中的脚本）。

2.在 application.yml 中配置好相应的数据库信息。

3.启动项目。

## 六. 注意事项

1.每添加一个 HTML 界面，要在 PageController 中添加相应接口。项目代码可能存在不规范问题（毕竟只是一个信息系统设计），欢迎留言讨论。

2.项目中存在部分在线引用 CSS / JS 等文件，若失效则手动替换链接即可。

