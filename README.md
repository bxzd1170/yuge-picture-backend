# 宇阁云图库 · 后端

多空间视觉资产管理系统的服务端，提供图片存储与检索、空间与成员权限、AI 图像处理、多人实时协同编辑等能力。

前端仓库：[yuge-gallery-frontend](https://github.com/bxzd1170/yuge-gallery-frontend) · 在线体验：<http://39.104.13.97/>

---

## 功能

- **图片管理**：上传（本地文件 / URL 导入）、覆盖更新、批量编辑、审核流转、按关键词 / 分类 / 标签 / 颜色检索
- **空间体系**：公共图库、私有空间、团队空间；三级角色（viewer / editor / admin）与配额限制
- **团队协作**：成员管理、基于 WebSocket 的实时协同编辑（缩放、旋转、进入 / 退出通知）
- **AI 能力**：阿里云通义万相 AI 扩图、以图搜图
- **运营分析**：按分类 / 标签 / 体积 / 用户 / 使用量多维统计与排行
- **对象存储**：腾讯云 COS 直传，一次 PUT 派生 webp 主图与缩略图

## 技术栈

| 分类 | 选型 |
| --- | --- |
| 框架 | Spring Boot 2.7 · JDK 17 |
| 持久层 | MyBatis-Plus · MySQL 5.7 |
| 缓存 / 会话 | Redis · Caffeine · Spring Session |
| 权限 | Sa-Token（多账号体系）· AOP 注解鉴权 |
| 实时通信 | WebSocket · Disruptor 无锁队列 |
| 对象存储 | 腾讯云 COS + 数据万象 |
| 分库分表 | ShardingSphere（按空间分表，可选启用） |
| 接口文档 | Knife4j |

## 项目结构

```
src/main/java/com/yuge
├── annotation      自定义注解（@AuthCheck、@SaSpaceCheckPermission）
├── aop             全局权限拦截器
├── api             外部服务封装（AI 扩图、以图搜图）
├── common          统一响应体、分页基类、工具
├── config          CORS、COS、JSON、MyBatis-Plus、请求包装
├── controller      接口层
├── exception       业务异常与全局异常处理
├── manager
│   ├── auth        Sa-Token 多账号体系与空间权限计算
│   ├── sharding    动态分表策略
│   ├── upload      上传模板方法（文件 / URL 两种实现）
│   └── websocket   协同编辑与 Disruptor 事件分发
├── mapper          数据访问层
├── model           entity / dto / vo / enums
├── service         业务层
└── utils           颜色相似度、格式转换等工具
```

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p < sql/create_table.sql
```

### 2. 准备配置

仓库内不含任何密钥。复制模板并填写本地值：

```bash
cp application-local.example.yml application-local.yml
```

| 配置项 | 说明 |
| --- | --- |
| `spring.datasource.*` | MySQL 连接信息 |
| `spring.redis.*` | Redis 连接信息 |
| `cos.client.*` | 腾讯云 COS 的 secretId / secretKey / bucket / region / host |
| `aliYunAi.apiKey` | 阿里云百炼 API Key（扩图功能） |

`application-local.yml` 已被 `.gitignore` 忽略，不会入库。也可改用环境变量注入（见 `application.yml` 顶部注释）。

### 3. 启动

```bash
mvn spring-boot:run
```

默认端口 `8123`，上下文路径 `/api`。

### 4. 接口文档

启动后访问 <http://localhost:8123/api/doc.html>

## 权限模型

三层身份互相独立，不可混用：

| 层级 | 取值 | 存储位置 |
| --- | --- | --- |
| 系统角色 | `admin` / `user` | `user.userRole` |
| 空间角色 | `viewer` / `editor` / `admin` | `space_user.spaceRole` |
| 空间归属 | 创建者 | `space.userId` |

空间内可执行的操作由 `SpaceUserAuthManager` 依据 `biz/spaceUserAuthConfig.json` 计算得出，随 `GET /space/get/vo` 的 `permissionList` 一起下发，前端据此渲染按钮，后端在接口入口二次校验。

## 部署

- 打包：`mvn clean package -DskipTests`
- 生产配置：通过 `--spring.profiles.active=prod` 加载外部配置文件或环境变量
- 反向代理需放行 `/api`，WebSocket 需带 `Upgrade` 与 `Connection` 请求头

## 说明

- 仓库不包含任何数据库密码、对象存储密钥或生产数据
- `application-local.yml`、`application-prod.yml`、`deploy/data_export.sql` 均已在 `.gitignore` 中排除
