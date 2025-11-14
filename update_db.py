from app import db, app
from datetime import datetime
from werkzeug.security import generate_password_hash

def init_db():
    with app.app_context():
        db.drop_all()
        db.create_all()
        
        # 创建示例用户
        from app import User
        
        # 创建上传者用户
        uploader = User(
            username='uploader1',
            email='uploader1@example.com',
            role='uploader'
        )
        uploader.set_password('password123')
        db.session.add(uploader)
        
        # 创建管理员用户
        admin = User(
            username='admin1',
            email='admin1@example.com',
            role='admin'
        )
        admin.set_password('password123')
        db.session.add(admin)
        
        # 创建超级管理员用户
        superadmin = User(
            username='superadmin',
            email='superadmin@example.com',
            role='superadmin'
        )
        superadmin.set_password('superadmin123')
        db.session.add(superadmin)
        
        db.session.commit()
        print("数据库表结构和示例用户已重新创建")
        print("演示账户信息：")
        print("- 上传者账户: uploader1 / password123")
        print("- 管理员账户: admin1 / password123") 
        print("- 超级管理员账户: superadmin / superadmin123")

if __name__ == '__main__':
    init_db()
