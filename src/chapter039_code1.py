class User(BaseModel):
    id: UUID
    username: str          # 唯一登录名
    email: str             # 邮箱，作为密码重置的凭证
    mobile: Optional[str]  # 手机号，可选
    password_hash: str     # 密码的bcrypt哈希值
    status: UserStatus     # 枚举：ACTIVE/INACTIVE/LOCKED
    created_at: datetime
    last_login_at: Optional[datetime]