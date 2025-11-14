from flask import Flask, render_template, request, jsonify, redirect, url_for, flash, send_file
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager, UserMixin, login_user, logout_user, login_required, current_user
from werkzeug.security import generate_password_hash, check_password_hash
from werkzeug.utils import secure_filename
import os
import io
from datetime import datetime

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your-secret-key-here'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///invoices.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['UPLOAD_FOLDER'] = 'uploads'
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 16MB max file size

# 确保上传目录存在
os.makedirs(app.config['UPLOAD_FOLDER'], exist_ok=True)

db = SQLAlchemy(app)
login_manager = LoginManager()
login_manager.init_app(app)
login_manager.login_view = 'login'

# 数据库模型
class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password_hash = db.Column(db.String(128))
    role = db.Column(db.String(20), nullable=False)  # 'uploader', 'admin' 或 'superadmin'
    created_at = db.Column(db.DateTime, default=datetime.utcnow)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

class Invoice(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    invoice_number = db.Column(db.String(100), nullable=False)
    supplier_name = db.Column(db.String(200), nullable=False)
    purchase_date = db.Column(db.DateTime, nullable=False)
    amount = db.Column(db.Float, nullable=False)
    description = db.Column(db.Text)
    file_path = db.Column(db.String(500))  # 存储PDF文件路径
    file_url = db.Column(db.String(500))  # 存储PDF文件URL链接
    file_type = db.Column(db.String(20), default='upload')  # upload: 上传文件, url: URL链接
    status = db.Column(db.String(20), default='pending')  # pending, approved, rejected
    reimbursement_status = db.Column(db.String(20), default='unreimbursed')  # unreimbursed, reimbursed
    tags = db.Column(db.String(500))
    rejection_reason = db.Column(db.Text)  # 拒绝原因
    uploader_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    admin_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    uploader = db.relationship('User', foreign_keys=[uploader_id], backref='uploaded_invoices')
    admin = db.relationship('User', foreign_keys=[admin_id], backref='managed_invoices')

@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))

# 路由
@app.route('/')
def index():
    if current_user.is_authenticated:
        if current_user.role == 'uploader':
            return redirect(url_for('uploader_dashboard'))
        elif current_user.role == 'admin':
            return redirect(url_for('admin_dashboard'))
        elif current_user.role == 'superadmin':
            return redirect(url_for('superadmin_dashboard'))
    return redirect(url_for('login'))

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form.get('username')
        password = request.form.get('password')
        user = User.query.filter_by(username=username).first()
        
        if user and user.check_password(password):
            login_user(user)
            flash('登录成功！', 'success')
            return redirect(url_for('index'))
        else:
            flash('用户名或密码错误！', 'error')
    
    return render_template('login.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    flash('您已成功退出登录。', 'success')
    return redirect(url_for('login'))

@app.route('/uploader/dashboard')
@login_required
def uploader_dashboard():
    if current_user.role != 'uploader':
        flash('您没有权限访问此页面。', 'error')
        return redirect(url_for('index'))
    
    invoices = Invoice.query.filter_by(uploader_id=current_user.id).all()
    admins = User.query.filter_by(role='admin').all()
    return render_template('uploader_dashboard.html', invoices=invoices, admins=admins)

@app.route('/admin/dashboard')
@login_required
def admin_dashboard():
    if current_user.role != 'admin':
        flash('您没有权限访问此页面。', 'error')
        return redirect(url_for('index'))
    
    pending_invoices = Invoice.query.filter_by(admin_id=current_user.id, status='pending').all()
    approved_invoices = Invoice.query.filter_by(admin_id=current_user.id, status='approved').all()
    rejected_invoices = Invoice.query.filter_by(admin_id=current_user.id, status='rejected').all()
    
    # 获取汇总数据（所有已通过的发票）
    summary_invoices = Invoice.query.filter_by(admin_id=current_user.id, status='approved').all()
    
    # 获取所有上传者用于筛选
    uploaders = User.query.filter_by(role='uploader').all()
    
    return render_template('admin_dashboard.html', 
                         pending_invoices=pending_invoices,
                         approved_invoices=approved_invoices,
                         rejected_invoices=rejected_invoices,
                         summary_invoices=summary_invoices,
                         uploaders=uploaders)

@app.route('/admin/summary_filter', methods=['POST'])
@login_required
def admin_summary_filter():
    if current_user.role != 'admin':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        data = request.json
        uploader_id = data.get('uploader_id')
        start_date = data.get('start_date')
        end_date = data.get('end_date')
        tags = data.get('tags')
        reimbursement_status = data.get('reimbursement_status')
        
        # 基础查询：当前管理员且已通过的发票
        query = Invoice.query.filter_by(admin_id=current_user.id, status='approved')
        
        # 应用筛选条件
        if uploader_id and uploader_id != 'all':
            query = query.filter_by(uploader_id=uploader_id)
        
        if start_date:
            start_date_obj = datetime.strptime(start_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at >= start_date_obj)
        
        if end_date:
            end_date_obj = datetime.strptime(end_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at <= end_date_obj)
        
        if tags:
            query = query.filter(Invoice.tags.contains(tags))
        
        if reimbursement_status and reimbursement_status != 'all':
            query = query.filter_by(reimbursement_status=reimbursement_status)
        
        filtered_invoices = query.all()
        
        # 转换为JSON格式
        invoices_data = []
        for invoice in filtered_invoices:
            invoices_data.append({
                'id': invoice.id,
                'invoice_number': invoice.invoice_number,
                'supplier_name': invoice.supplier_name,
                'purchase_date': invoice.purchase_date.strftime('%Y-%m-%d'),
                'amount': invoice.amount,
                'description': invoice.description,
                'status': invoice.status,
                'reimbursement_status': invoice.reimbursement_status,
                'tags': invoice.tags,
                'uploader': invoice.uploader.username,
                'created_at': invoice.created_at.strftime('%Y-%m-%d %H:%M'),
                'updated_at': invoice.updated_at.strftime('%Y-%m-%d %H:%M'),
                'file_path': invoice.file_path
            })
        
        return jsonify(invoices_data)
    
    except Exception as e:
        return jsonify({'error': f'筛选失败: {str(e)}'}), 500

@app.route('/uploader/summary_filter', methods=['POST'])
@login_required
def uploader_summary_filter():
    if current_user.role != 'uploader':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        data = request.json
        start_date = data.get('start_date')
        end_date = data.get('end_date')
        status = data.get('status')
        reimbursement_status = data.get('reimbursement_status')
        
        # 基础查询：当前上传者的所有发票
        query = Invoice.query.filter_by(uploader_id=current_user.id)
        
        # 应用筛选条件
        if start_date:
            start_date_obj = datetime.strptime(start_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at >= start_date_obj)
        
        if end_date:
            end_date_obj = datetime.strptime(end_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at <= end_date_obj)
        
        if status and status != 'all':
            query = query.filter_by(status=status)
        
        if reimbursement_status and reimbursement_status != 'all':
            query = query.filter_by(reimbursement_status=reimbursement_status)
        
        filtered_invoices = query.all()
        
        # 转换为JSON格式
        invoices_data = []
        for invoice in filtered_invoices:
            invoices_data.append({
                'id': invoice.id,
                'invoice_number': invoice.invoice_number,
                'supplier_name': invoice.supplier_name,
                'purchase_date': invoice.purchase_date.strftime('%Y-%m-%d'),
                'amount': invoice.amount,
                'description': invoice.description,
                'status': invoice.status,
                'reimbursement_status': invoice.reimbursement_status,
                'tags': invoice.tags,
                'created_at': invoice.created_at.strftime('%Y-%m-%d %H:%M'),
                'updated_at': invoice.updated_at.strftime('%Y-%m-%d %H:%M'),
                'file_path': invoice.file_path
            })
        
        return jsonify(invoices_data)
    
    except Exception as e:
        return jsonify({'error': f'筛选失败: {str(e)}'}), 500

@app.route('/upload_invoice', methods=['POST'])
@login_required
def upload_invoice():
    if current_user.role != 'uploader':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        invoice_number = request.form.get('invoice_number')
        supplier_name = request.form.get('supplier_name')
        purchase_date = datetime.strptime(request.form.get('purchase_date'), '%Y-%m-%d')
        amount = float(request.form.get('amount'))
        description = request.form.get('description')
        admin_id = int(request.form.get('admin_id'))
        file_type = request.form.get('file_type', 'upload')  # upload 或 url
        
        # 根据文件类型处理
        if file_type == 'upload':
            # 文件上传方式
            if 'invoice_file' not in request.files:
                return jsonify({'error': '请选择发票文件'}), 400
            
            file = request.files['invoice_file']
            if file.filename == '':
                return jsonify({'error': '请选择发票文件'}), 400
            
            if file:
                # 生成唯一文件名
                timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
                original_filename = secure_filename(file.filename)
                filename = f"{timestamp}_{original_filename}"
                file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
                
                # 保存文件到uploads目录
                file.save(file_path)
                
                # 创建发票记录
                invoice = Invoice(
                    invoice_number=invoice_number,
                    supplier_name=supplier_name,
                    purchase_date=purchase_date,
                    amount=amount,
                    description=description,
                    file_path=file_path,
                    file_type='upload',
                    uploader_id=current_user.id,
                    admin_id=admin_id
                )
                
                db.session.add(invoice)
                db.session.commit()
                
                return jsonify({'success': '发票上传成功！'})
        
        elif file_type == 'url':
            # URL链接方式
            file_url = request.form.get('file_url')
            if not file_url:
                return jsonify({'error': '请输入PDF文件URL链接'}), 400
            
            # 验证URL格式
            if not (file_url.startswith('http://') or file_url.startswith('https://')):
                return jsonify({'error': '请输入有效的URL链接'}), 400
            
            # 创建发票记录
            invoice = Invoice(
                invoice_number=invoice_number,
                supplier_name=supplier_name,
                purchase_date=purchase_date,
                amount=amount,
                description=description,
                file_url=file_url,
                file_type='url',
                uploader_id=current_user.id,
                admin_id=admin_id
            )
            
            db.session.add(invoice)
            db.session.commit()
            
            return jsonify({'success': '发票链接提交成功！'})
        
        else:
            return jsonify({'error': '不支持的提交方式'}), 400
    
    except Exception as e:
        return jsonify({'error': f'提交失败: {str(e)}'}), 500

@app.route('/resubmit_invoice', methods=['POST'])
@login_required
def resubmit_invoice():
    if current_user.role != 'uploader':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        invoice_id = request.form.get('invoice_id')
        invoice_number = request.form.get('invoice_number')
        supplier_name = request.form.get('supplier_name')
        purchase_date = datetime.strptime(request.form.get('purchase_date'), '%Y-%m-%d')
        amount = float(request.form.get('amount'))
        description = request.form.get('description')
        admin_id = int(request.form.get('admin_id'))
        
        # 获取现有发票记录
        invoice = Invoice.query.get_or_404(invoice_id)
        
        # 检查权限
        if invoice.uploader_id != current_user.id:
            return jsonify({'error': '没有权限操作此发票'}), 403
        
        # 检查文件上传
        if 'invoice_file' not in request.files:
            return jsonify({'error': '请选择发票文件'}), 400
        
        file = request.files['invoice_file']
        if file.filename == '':
            return jsonify({'error': '请选择发票文件'}), 400
        
        if file:
            # 生成唯一文件名
            timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
            original_filename = secure_filename(file.filename)
            filename = f"{timestamp}_{original_filename}"
            file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
            
            # 保存文件到uploads目录
            file.save(file_path)
            
            # 删除旧文件（如果存在）
            if invoice.file_path and os.path.exists(invoice.file_path):
                try:
                    os.remove(invoice.file_path)
                except:
                    pass  # 如果删除失败，继续执行
            
            # 更新发票记录
            invoice.invoice_number = invoice_number
            invoice.supplier_name = supplier_name
            invoice.purchase_date = purchase_date
            invoice.amount = amount
            invoice.description = description
            invoice.file_path = file_path
            invoice.admin_id = admin_id
            invoice.status = 'pending'  # 重置为待审核状态
            invoice.rejection_reason = None  # 清除拒绝理由
            invoice.tags = ''  # 清除标签
            invoice.reimbursement_status = 'unreimbursed'  # 重置报销状态
            
            db.session.commit()
            
            return jsonify({'success': '发票重新提交成功！'})
    
    except Exception as e:
        return jsonify({'error': f'重新提交失败: {str(e)}'}), 500

@app.route('/update_invoice_status/<int:invoice_id>', methods=['POST'])
@login_required
def update_invoice_status(invoice_id):
    if current_user.role != 'admin':
        return jsonify({'error': '没有权限'}), 403
    
    invoice = Invoice.query.get_or_404(invoice_id)
    if invoice.admin_id != current_user.id:
        return jsonify({'error': '没有权限操作此发票'}), 403
    
    try:
        status = request.json.get('status')
        tags = request.json.get('tags', '')
        reimbursement_status = request.json.get('reimbursement_status')
        rejection_reason = request.json.get('rejection_reason', '')
        
        invoice.status = status
        invoice.tags = tags
        if reimbursement_status:
            invoice.reimbursement_status = reimbursement_status
        
        # 如果是拒绝状态，保存拒绝原因
        if status == 'rejected':
            invoice.rejection_reason = rejection_reason
        
        db.session.commit()
        
        return jsonify({'success': '发票状态更新成功！'})
    
    except Exception as e:
        return jsonify({'error': f'更新失败: {str(e)}'}), 500

@app.route('/invoice_details/<int:invoice_id>')
@login_required
def invoice_details(invoice_id):
    invoice = Invoice.query.get_or_404(invoice_id)
    
    # 检查权限
    if current_user.role == 'uploader' and invoice.uploader_id != current_user.id:
        return jsonify({'error': '没有权限查看此发票'}), 403
    elif current_user.role == 'admin' and invoice.admin_id != current_user.id:
        return jsonify({'error': '没有权限查看此发票'}), 403
    
    invoice_data = {
        'id': invoice.id,
        'invoice_number': invoice.invoice_number,
        'supplier_name': invoice.supplier_name,
        'purchase_date': invoice.purchase_date.strftime('%Y-%m-%d'),
        'amount': invoice.amount,
        'description': invoice.description,
        'status': invoice.status,
        'reimbursement_status': invoice.reimbursement_status,
        'tags': invoice.tags,
        'rejection_reason': invoice.rejection_reason,
        'uploader': invoice.uploader.username,
        'created_at': invoice.created_at.strftime('%Y-%m-%d %H:%M'),
        'updated_at': invoice.updated_at.strftime('%Y-%m-%d %H:%M'),
        'file_path': invoice.file_path
    }
    
    return jsonify(invoice_data)

@app.route('/download_invoice/<int:invoice_id>')
@login_required
def download_invoice(invoice_id):
    invoice = Invoice.query.get_or_404(invoice_id)
    
    # 检查权限
    if current_user.role == 'uploader' and invoice.uploader_id != current_user.id:
        return jsonify({'error': '没有权限下载此发票'}), 403
    elif current_user.role == 'admin' and invoice.admin_id != current_user.id:
        return jsonify({'error': '没有权限下载此发票'}), 403
    
    # 根据文件类型处理
    if invoice.file_type == 'upload':
        if not invoice.file_path or not os.path.exists(invoice.file_path):
            return jsonify({'error': '发票文件不存在'}), 404
        
        # 获取文件名
        filename = os.path.basename(invoice.file_path)
        return send_file(invoice.file_path, as_attachment=True, download_name=filename)
    
    elif invoice.file_type == 'url':
        if not invoice.file_url:
            return jsonify({'error': '发票链接不存在'}), 404
        
        # 重定向到外部URL
        return redirect(invoice.file_url)
    
    else:
        return jsonify({'error': '不支持的发票类型'}), 400

# 超级管理员路由
@app.route('/superadmin/dashboard')
@login_required
def superadmin_dashboard():
    if current_user.role != 'superadmin':
        flash('您没有权限访问此页面。', 'error')
        return redirect(url_for('index'))
    
    # 获取所有用户
    users = User.query.all()
    # 获取所有发票
    invoices = Invoice.query.all()
    
    return render_template('superadmin_dashboard.html', users=users, invoices=invoices)

@app.route('/superadmin/create_user', methods=['POST'])
@login_required
def create_user():
    if current_user.role != 'superadmin':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        username = request.json.get('username')
        email = request.json.get('email')
        password = request.json.get('password')
        role = request.json.get('role')  # 'uploader' 或 'admin'
        
        # 验证输入
        if not username or not email or not password or not role:
            return jsonify({'error': '请填写所有字段'}), 400
        
        if role not in ['uploader', 'admin']:
            return jsonify({'error': '角色必须是 uploader 或 admin'}), 400
        
        # 检查用户名和邮箱是否已存在
        if User.query.filter_by(username=username).first():
            return jsonify({'error': '用户名已存在'}), 400
        
        if User.query.filter_by(email=email).first():
            return jsonify({'error': '邮箱已存在'}), 400
        
        # 创建用户
        user = User(username=username, email=email, role=role)
        user.set_password(password)
        
        db.session.add(user)
        db.session.commit()
        
        return jsonify({'success': '用户创建成功！'})
    
    except Exception as e:
        return jsonify({'error': f'创建用户失败: {str(e)}'}), 500

@app.route('/superadmin/delete_user/<int:user_id>', methods=['POST'])
@login_required
def delete_user(user_id):
    if current_user.role != 'superadmin':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        user = User.query.get_or_404(user_id)
        
        # 不能删除自己
        if user.id == current_user.id:
            return jsonify({'error': '不能删除自己的账户'}), 400
        
        # 检查用户是否有相关发票
        if user.role == 'uploader':
            uploaded_invoices = Invoice.query.filter_by(uploader_id=user.id).all()
            if uploaded_invoices:
                return jsonify({'error': '该用户有上传的发票，无法删除'}), 400
        
        if user.role == 'admin':
            managed_invoices = Invoice.query.filter_by(admin_id=user.id).all()
            if managed_invoices:
                return jsonify({'error': '该管理员有管理的发票，无法删除'}), 400
        
        db.session.delete(user)
        db.session.commit()
        
        return jsonify({'success': '用户删除成功！'})
    
    except Exception as e:
        return jsonify({'error': f'删除用户失败: {str(e)}'}), 500

@app.route('/superadmin/all_invoices')
@login_required
def all_invoices():
    if current_user.role != 'superadmin':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        # 获取所有发票
        invoices = Invoice.query.all()
        
        invoices_data = []
        for invoice in invoices:
            invoices_data.append({
                'id': invoice.id,
                'invoice_number': invoice.invoice_number,
                'supplier_name': invoice.supplier_name,
                'purchase_date': invoice.purchase_date.strftime('%Y-%m-%d'),
                'amount': invoice.amount,
                'description': invoice.description,
                'status': invoice.status,
                'reimbursement_status': invoice.reimbursement_status,
                'tags': invoice.tags,
                'rejection_reason': invoice.rejection_reason,
                'uploader': invoice.uploader.username,
                'admin': invoice.admin.username,
                'created_at': invoice.created_at.strftime('%Y-%m-%d %H:%M'),
                'updated_at': invoice.updated_at.strftime('%Y-%m-%d %H:%M'),
                'file_path': invoice.file_path
            })
        
        return jsonify(invoices_data)
    
    except Exception as e:
        return jsonify({'error': f'获取发票数据失败: {str(e)}'}), 500

@app.route('/superadmin/filter_invoices', methods=['POST'])
@login_required
def filter_invoices():
    if current_user.role != 'superadmin':
        return jsonify({'error': '没有权限'}), 403
    
    try:
        data = request.json
        uploader_id = data.get('uploader_id')
        admin_id = data.get('admin_id')
        start_date = data.get('start_date')
        end_date = data.get('end_date')
        status = data.get('status')
        reimbursement_status = data.get('reimbursement_status')
        
        # 基础查询：所有发票
        query = Invoice.query
        
        # 应用筛选条件
        if uploader_id and uploader_id != 'all':
            query = query.filter_by(uploader_id=uploader_id)
        
        if admin_id and admin_id != 'all':
            query = query.filter_by(admin_id=admin_id)
        
        if start_date:
            start_date_obj = datetime.strptime(start_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at >= start_date_obj)
        
        if end_date:
            end_date_obj = datetime.strptime(end_date, '%Y-%m-%d')
            query = query.filter(Invoice.created_at <= end_date_obj)
        
        if status and status != 'all':
            query = query.filter_by(status=status)
        
        if reimbursement_status and reimbursement_status != 'all':
            query = query.filter_by(reimbursement_status=reimbursement_status)
        
        filtered_invoices = query.all()
        
        # 转换为JSON格式
        invoices_data = []
        for invoice in filtered_invoices:
            invoices_data.append({
                'id': invoice.id,
                'invoice_number': invoice.invoice_number,
                'supplier_name': invoice.supplier_name,
                'purchase_date': invoice.purchase_date.strftime('%Y-%m-%d'),
                'amount': invoice.amount,
                'description': invoice.description,
                'status': invoice.status,
                'reimbursement_status': invoice.reimbursement_status,
                'tags': invoice.tags,
                'rejection_reason': invoice.rejection_reason,
                'uploader': invoice.uploader.username,
                'admin': invoice.admin.username,
                'created_at': invoice.created_at.strftime('%Y-%m-%d %H:%M'),
                'updated_at': invoice.updated_at.strftime('%Y-%m-%d %H:%M'),
                'file_path': invoice.file_path
            })
        
        return jsonify(invoices_data)
    
    except Exception as e:
        return jsonify({'error': f'筛选失败: {str(e)}'}), 500

# 初始化数据库和示例数据
def init_db():
    with app.app_context():
        db.create_all()
        
        # 创建示例用户（如果不存在）
        if not User.query.filter_by(username='uploader1').first():
            uploader = User(username='uploader1', email='uploader1@example.com', role='uploader')
            uploader.set_password('password123')
            db.session.add(uploader)
        
        if not User.query.filter_by(username='admin1').first():
            admin = User(username='admin1', email='admin1@example.com', role='admin')
            admin.set_password('password123')
            db.session.add(admin)
        
        # 创建超级管理员（如果不存在）
        if not User.query.filter_by(username='superadmin').first():
            superadmin = User(username='superadmin', email='superadmin@example.com', role='superadmin')
            superadmin.set_password('superadmin123')
            db.session.add(superadmin)
        
        db.session.commit()
        print("数据库初始化完成！")

if __name__ == '__main__':
    init_db()
    app.run(debug=True, host="0.0.0.0")
