#!/usr/bin/env python3
"""
发票管理系统 - 环境自动安装脚本
自动检测环境并安装所需依赖
"""

import sys
import os
import subprocess
import platform
from pathlib import Path

def check_python_version():
    """检查Python版本"""
    print("🔍 检查Python版本...")
    if sys.version_info < (3, 8):
        print(f"❌ Python版本过低: {sys.version}")
        print("请安装Python 3.8或更高版本")
        return False
    print(f"✅ Python版本符合要求: {sys.version}")
    return True

def check_requirements():
    """检查requirements.txt文件是否存在"""
    print("🔍 检查依赖文件...")
    if not os.path.exists("requirements.txt"):
        print("❌ requirements.txt文件不存在")
        return False
    print("✅ requirements.txt文件存在")
    return True

def install_dependencies():
    """安装项目依赖"""
    print("📦 安装项目依赖...")
    
    # 使用国内镜像源加速下载
    mirrors = [
        "https://pypi.tuna.tsinghua.edu.cn/simple",
        "https://mirrors.aliyun.com/pypi/simple",
        "https://pypi.douban.com/simple"
