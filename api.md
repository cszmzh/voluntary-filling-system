## 一. API接口

### 1.1 志愿

#### 1.1.1 志愿报名

[POST] /report/insert

| 参数名称     | 是否必须 | 说明                               |
| ------------ | -------- | ---------------------------------- |
| stuId        | Y        | 学生学号 8位 2021开头              |
| stdName      | Y        | 学生姓名                           |
| majorId      | Y        | 专业编号                           |
| classNum     | Y        | 班级号 1-99                        |
| stdQQ        | Y        | 学生的QQ号                         |
| stdPhone     | Y        | 学生的手机号                       |
| firstWill    | Y        | 第一志愿编号（分支编号）           |
| firstReason  | Y        | 第一志愿理由                       |
| secondWill   | N        | 第二志愿编号（分支编号）           |
| secondReason | N        | 第二志愿理由                       |
| isDispensing | Y        | 是否接受调剂 传入0不接受 传入1接受 |
| code         | Y        | 验证码                             |

返回

```json
{
	"code":0,
	"msg":"success"
}

{
	"code":1,
	"msg":"验证码错误"
}

{
	"code":2,
	"msg":"第一志愿信息不完整"
}

{
	"code":3,
	"msg":"志愿已填写"
}

{
	"code":4,
	"msg":"姓名为空"
}

{
	"code":5,
	"msg":"专业信息为空"
}

{
	"code":6,
	"msg":"验证码生成失败"
}
```

#### 1.1.2 获取所有志愿

需要鉴权 >=1

[GET] /report/getAll

| 参数名称 | 是否必须 | 说明           |
| -------- | -------- | -------------- |
| page     | Y        | 页数（1开始）  |
| pageSize | Y        | 一页显示多少条 |

#### 1.1.3 根据学号获取志愿

需要鉴权 >=1

[POST] /report/getByStuId

| 参数名称 | 是否必须 | 说明 |
| -------- | -------- | ---- |
| stuId    | Y        | 学号 |

#### 1.1.4 更新志愿信息（面试官）

需要鉴权 >=1

[POST] /report/update_ADMIN

| 参数名称     | 是否必须 | 说明           |
| ------------ | -------- | -------------- |
| stuId        | Y        | 学号           |
| majorId      | Y        | 专业编号       |
| classNum     | Y        | 班级           |
| stdQQ        | Y        | QQ号           |
| stdPhone     | Y        | 手机号         |
| firstWill    | Y        | 一志愿分支编号 |
| firstReason  | Y        | 一志愿理由     |
| secondWill   | N        | 二志愿分支编号 |
| secondReason | N        | 二志愿理由     |

#### 1.1.5 更新志愿信息（超级管理员）

需要鉴权 =2

[POST] /report/update_ROOT

同上，多个一个字段"isDispensing"（是否调剂），布尔类型。

#### 1.1.6 录取志愿

需要鉴权

[POST] /report/update_ADMIN

[POST] /report/update_ROOT

| 参数名称 | 是否必须 | 说明         |
| -------- | -------- | ------------ |
| stuId    | Y        | 学号         |
| willId   | Y        | 录取分支编号 |

#### 1.1.7 删除志愿

需要鉴权 =2

[POST] /report/delete

| 参数名称 | 是否必须 | 说明 |
| -------- | -------- | ---- |
| stuId    | Y        | 学号 |



### 1.2 用户

#### 1.2.1 登录

[POST] /user/login

| 参数名称 | 是否必须 | 说明   |
| -------- | -------- | ------ |
| username | Y        | 用户名 |
| password | Y        | 密码   |

返回

```json
{
	"code":0,
	"msg":"登录成功",
	"data":{
		"username":"用户名",
		"status":"用户权限状态,1为面试官,2为超级管理员"
	}
}

{
	"code":1,
	"msg":"登录失败"
}
```

#### 1.2.2 获取所有面试官账号

需要鉴权 =2

[GET] /user/getAll

#### 1.2.3 创建面试官账号

需要鉴权 =2

[POST] /user/createAdmin

| 参数名称 | 是否必须 | 说明         |
| -------- | -------- | ------------ |
| username | Y        | 用户名       |
| password | Y        | 密码         |
| realName | Y        | 持有人姓名   |
| branchId | Y        | 所属分支编号 |

#### 1.2.4 删除面试官账号

需要鉴权 =2

[POST] /user/delete

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| userId   | Y        | 用户编号 |

#### 1.2.5 修改面试官账号

需要鉴权 =2

[POST] /user/update

| 参数名称 | 是否必须 | 说明               |
| -------- | -------- | ------------------ |
| userId   | Y        | 用户编号           |
| userName | Y        | 修改的用户名       |
| realName | Y        | 修改的持有人       |
| password | Y        | 修改的新密码       |
| branchId | Y        | 修改的所属分支编号 |



### 1.3 专业

#### 1.3.1 获取所有专业

[GET] /major/getAll

#### 1.3.2 创建专业

需要鉴权 =2

[POST] /major/create

| 参数名称  | 是否必须 | 说明                |
| --------- | -------- | ------------------- |
| majorId   | Y        | 专业编号（5位数字） |
| majorName | Y        | 专业名              |
| classNum  | Y        | 班级数              |

#### 1.3.3 修改专业

需要鉴权 =2

[POST] /major/update

| 参数名称  | 是否必须 | 说明         |
| --------- | -------- | ------------ |
| majorId   | Y        | 专业编号     |
| majorName | Y        | 修改的专业名 |
| classNum  | Y        | 修改的班级数 |

#### 1.3.4 删除专业

需要鉴权 =2

[POST] /major/delete

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| majorId  | Y        | 专业编号 |



### 1.4 组织

#### 1.4.1 获取所有组织

[GET] /org/getMain

#### 1.4.2 删除组织

需要鉴权 =2

[POST] /org/deleteOrg

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| orgId    | Y        | 组织编号 |

#### 1.4.3 更新组织

需要鉴权 =2

[POST] /org/updateOrg

| 参数名称 | 是否必须 | 说明       |
| -------- | -------- | ---------- |
| orgId    | Y        | 组织编号   |
| orgName  | Y        | 新组织名   |
| orgDes   | Y        | 新组织描述 |
| orgDes   | Y        | 新负责人   |

#### 1.4.4 创建组织

需要鉴权 =2

[POST] /org/createOrg

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| orgId    | Y        | 组织编号 |
| orgName  | Y        | 组织名   |
| orgDes   | Y        | 组织描述 |
| orgDes   | Y        | 负责人   |



### 1.5 分支

#### 1.5.1 根据组织编号获取分支

[GET] /org/getBranch

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| orgId    | Y        | 组织编号 |

#### 1.5.2 获取所有分支

[GET] /org/getBranchResult

#### 1.5.3 删除分支

[POST] /org/deleteBranch

| 参数名称 | 是否必须 | 说明     |
| -------- | -------- | -------- |
| branchId | Y        | 分支编号 |

#### 1.5.4 更新分支

[POST] /org/updateBranch

| 参数名称    | 是否必须 | 说明           |
| ----------- | -------- | -------------- |
| branchId    | Y        | 分支编号       |
| orgId       | Y        | 新所属组织编号 |
| branchName  | Y        | 新分支名       |
| branchDes   | Y        | 新分支描述     |
| managerName | Y        | 新负责人       |

#### 1.5.5 创建分支

[POST] /org/createBranch

| 参数名称    | 是否必须 | 说明         |
| ----------- | -------- | ------------ |
| branchId    | Y        | 分支编号     |
| orgId       | Y        | 所属组织编号 |
| branchName  | Y        | 分支名       |
| branchDes   | Y        | 分支描述     |
| managerName | Y        | 负责人       |



### 1.6 数据分析

#### 1.6.1 获取填报信息情况

[GET] /data/get



### 1.7 验证码

#### 1.7.1 获取验证码图片

[GET] /img/code