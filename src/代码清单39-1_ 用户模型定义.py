class User(BaseModel):
    id: UUID
    username: str          # 唯一登录名
    email: str             # 邮箱
    password_hash: str     # 密码的bcrypt哈希值
    status: UserStatus     # 枚举：ACTIVE/INACTIVE/LOCKED