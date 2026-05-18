# Python 完全学习手册

> 从零基础到企业级/黑客级实战，一本手册全覆盖。

---

## 目录

- [第一部分：快速入门](#第一部分快速入门)
  - [1. Python 简介与特点](#1-python-简介与特点)
  - [2. 环境搭建与第一个程序](#2-环境搭建与第一个程序)
  - [3. 基础语法](#3-基础语法)
  - [4. 变量与数据类型](#4-变量与数据类型)
  - [5. 字符串深度操作](#5-字符串深度操作)
  - [6. 核心数据结构](#6-核心数据结构)
  - [7. 运算符全解](#7-运算符全解)
  - [8. 控制流](#8-控制流)
  - [9. 函数](#9-函数)
  - [10. 模块与包](#10-模块与包)
  - [11. 文件操作](#11-文件操作)
  - [12. 异常处理](#12-异常处理)
  - [13. 面向对象编程](#13-面向对象编程)
  - [14. 常用标准库](#14-常用标准库)
- [第二部分：进阶内容](#第二部分进阶内容)
  - [15. 高级函数式编程](#15-高级函数式编程)
  - [16. 装饰器](#16-装饰器)
  - [17. 生成器与迭代器](#17-生成器与迭代器)
  - [18. 上下文管理器](#18-上下文管理器)
  - [19. 元类与描述符](#19-元类与描述符)
  - [20. 并发与异步编程](#20-并发与异步编程)
  - [21. 网络编程](#21-网络编程)
  - [22. 数据库操作](#22-数据库操作)
  - [23. Web 开发实战](#23-web-开发实战)
  - [24. 测试与质量保障](#24-测试与质量保障)
  - [25. 性能优化](#25-性能优化)
  - [26. 安全编程](#26-安全编程)
  - [27. 黑客级技巧](#27-黑客级技巧)
  - [28. 企业级部署与运维](#28-企业级部署与运维)

---

# 第一部分：快速入门

## 1. Python 简介与特点

### 1.1 什么是 Python

Python 是一门**解释型、动态类型、面向对象**的高级编程语言，由 Guido van Rossum 于 1991 年发布。其设计哲学强调**代码可读性**与**简洁的语法**。

### 1.2 核心特点

| 特点 | 说明 |
|------|------|
| 语法简洁 | 使用缩进定义代码块，减少冗余符号 |
| 动态类型 | 变量无需声明类型，运行时自动推断 |
| 跨平台 | Windows / Linux / macOS 均可运行 |
| 丰富的标准库 | 自带"电池"，涵盖网络、文件、正则等 |
| 生态强大 | PyPI 上超过 40 万个第三方包 |

---

## 2. 环境搭建与第一个程序

### 2.1 安装 Python

**Windows：**
```bash
# 从 https://www.python.org/downloads/ 下载安装包
# 安装时勾选 "Add Python to PATH"
python --version  # 验证安装
```

**Linux / macOS：**
```bash
# macOS (Homebrew)
brew install python3

# Ubuntu/Debian
sudo apt install python3 python3-pip

# 验证
python3 --version
```

### 2.2 虚拟环境

虚拟环境用于隔离项目依赖，避免版本冲突。

```bash
# 创建虚拟环境
python -m venv myproject_env

# 激活虚拟环境
# Windows:
myproject_env\Scripts\activate
# Linux/macOS:
source myproject_env/bin/activate

# 退出虚拟环境
deactivate
```

### 2.3 第一个程序

```python
# hello.py
print("Hello, World!")

# 运行
# python hello.py
```

### 2.4 IDE 推荐

- **VS Code**（轻量，插件丰富）
- **PyCharm**（功能全面，适合大型项目）
- **Jupyter Notebook**（数据科学首选）

---

## 3. 基础语法

### 3.1 缩进规则

Python 使用**缩进**（而非大括号 `{}`）来定义代码块。标准是 **4 个空格**。

```python
# ✅ 正确：统一使用 4 空格缩进
def greet(name):
    if name:
        print(f"Hello, {name}!")
    else:
        print("Hello, World!")

# ❌ 错误：缩进不一致会导致 IndentationError
def bad():
  print("2 spaces")
    print("4 spaces")  # IndentationError!
```

### 3.2 注释

```python
# 这是单行注释

"""
这是多行注释（文档字符串/docstring）
常用于函数、类的说明文档
覆盖多行内容
"""

def add(a, b):
    """返回两个数的和（这是 docstring）"""
    return a + b
```

### 3.3 标识符命名规范

```python
# 变量/函数：snake_case
user_name = "Alice"
def get_user_info():
    pass

# 类名：PascalCase
class UserAccount:
    pass

# 常量：UPPER_SNAKE_CASE
MAX_CONNECTIONS = 100
PI = 3.14159

# 私有成员：单下划线前缀（约定）
_internal_var = "private"

# 特殊方法：双下划线包围（magic methods）
def __init__(self):
    pass
```

### 3.4 语句分隔

```python
# 通常一行一条语句
x = 1
y = 2

# 分号分隔多条语句（不推荐）
x = 1; y = 2

# 反斜杠续行
total = 1 + 2 + 3 + \
        4 + 5 + 6

# 括号内隐式续行（推荐）
total = (1 + 2 + 3 +
         4 + 5 + 6)
```

---

## 4. 变量与数据类型

### 4.1 变量赋值

Python 是**动态类型**语言，变量不需要声明类型。

```python
# 基本赋值
x = 42
name = "Alice"
pi = 3.14

# 多重赋值
a, b, c = 1, 2, 3

# 交换变量（无需临时变量）
a, b = b, a

# 链式赋值
x = y = z = 0

# 解包赋值
data = (1, 2, 3)
first, second, third = data

# 星号解包
head, *tail = [1, 2, 3, 4, 5]
# head = 1, tail = [2, 3, 4, 5]
```

### 4.2 基本数据类型

```python
# --- 整数 (int)：无限精度 ---
a = 10
b = 0xFF        # 十六进制 → 255
c = 0o77        # 八进制 → 63
d = 0b1010      # 二进制 → 10
big = 10**100   # Python 自动处理大整数

# --- 浮点数 (float)：IEEE 754 双精度 ---
pi = 3.14159
sci = 1.5e-3    # 科学计数法 → 0.0015
inf = float('inf')     # 正无穷
neg_inf = float('-inf') # 负无穷
nan = float('nan')      # 非数字

# --- 布尔值 (bool)：True / False ---
is_valid = True
is_empty = False
# bool 是 int 的子类: True == 1, False == 0

# --- None 类型：表示"无" ---
result = None
```

### 4.3 类型转换

```python
# 隐式转换
result = 10 + 3.14     # int + float → float: 13.14
flag = True + 1         # bool + int → int: 2

# 显式转换
int("42")               # → 42
int(3.99)               # → 3（向下取整，非四舍五入）
float("3.14")           # → 3.14
str(123)                # → "123"
bool(0)                 # → False（0、空容器、None 为假）
bool("hello")           # → True
```

### 4.4 类型检查

```python
# type() 返回精确类型
type(42)        # <class 'int'>
type("hello")   # <class 'str'>

# isinstance() 检查继承关系（推荐）
isinstance(42, int)        # True
isinstance(True, int)      # True! (bool 是 int 子类)
isinstance(42, (str, int)) # 检查多个类型
```

---

## 5. 字符串深度操作

### 5.1 字符串创建

```python
# 四种引号方式
s1 = '单引号'
s2 = "双引号"
s3 = '''三单引号
可跨多行'''
s4 = """三双引号
也可跨多行"""

# 转义字符
path = "C:\\Users\\name"  # 双反斜杠
raw = r"C:\Users\name"     # 原始字符串（推荐）
```

### 5.2 f-string 格式化（Python 3.6+，推荐）

```python
name = "Alice"
age = 28

# 基本使用
greeting = f"Hello, {name}! You are {age} years old."

# 表达式嵌入
result = f"2 + 3 = {2 + 3}"

# 格式化数字
pi = 3.14159265
formatted = f"Pi is approximately {pi:.2f}"  # → "Pi is approximately 3.14"
percent = f"{0.8567:.1%}"                     # → "85.7%"
number = f"{1000000:,}"                       # → "1,000,000"

# 对齐与填充
left_aligned = f"{'hello':<10}"      # 左对齐，宽度10
center = f"{'hello':^10}"            # 居中对齐
right_aligned = f"{'hello':>10}"     # 右对齐
padded = f"{42:0>5}"                 # 右对齐，0填充 → "00042"

# 调试输出（Python 3.8+）
var = 3 + 4
print(f"{var = }")                   # → "var = 7"
```

### 5.3 字符串方法

```python
s = "  Hello, Python World!  "

# 大小写转换
s.upper()           # "  HELLO, PYTHON WORLD!  "
s.lower()           # "  hello, python world!  "
s.title()           # "  Hello, Python World!  "
s.capitalize()      # "  hello, python world!  "

# 空白处理
s.strip()           # "Hello, Python World!"（去除两端空白）
s.lstrip()          # 去除左侧空白
s.rstrip()          # 去除右侧空白

# 查找与替换
s.find("Python")    # 9（返回索引，找不到返回-1）
s.index("Python")   # 9（找不到抛出 ValueError）
s.count("o")        # 3（统计出现次数）
s.replace("World", "Universe")  # 替换所有匹配
s.replace("o", "0", 1)          # 只替换第1个

# 判断方法
s.startswith("Hello")  # True
s.endswith("!")        # True
"abc123".isalnum()     # True（字母或数字）
"123".isdigit()        # True
"abc".isalpha()        # True
"   ".isspace()        # True

# 分割与连接
words = "apple,banana,orange".split(",")  # ['apple', 'banana', 'orange']
"apple,banana".split(",", maxsplit=1)      # ['apple', 'banana']
",".join(words)           # "apple,banana,orange"
lines = "a\nb\nc".splitlines()  # ['a', 'b', 'c']
```

### 5.4 字符串切片

```python
s = "Hello, Python!"

# 基本切片 [start:stop:step]
s[0:5]    # "Hello"（索引 0 到 4）
s[7:]     # "Python!"（从索引 7 到末尾）
s[:5]     # "Hello"（从开头到索引 4）
s[-7:-1]  # "Python"（负数索引从末尾开始）
s[::-1]   # "!nohtyP ,olleH"（反转）

# 步长
s[::2]    # "Hlo yhn"（每隔一个字符）
```

---

## 6. 核心数据结构

### 6.1 列表 (list)

列表是**有序、可变、可重复**的容器。

```python
# 创建
fruits = ["apple", "banana", "cherry"]
nested = [[1, 2], [3, 4]]      # 嵌套列表
empty = []
from_range = list(range(5))    # [0, 1, 2, 3, 4]

# 访问
fruits[0]       # "apple"（索引从 0 开始）
fruits[-1]      # "cherry"（最后一个元素）
fruits[1:3]     # ["banana", "cherry"]（切片返回新列表）

# 修改
fruits[0] = "grape"            # 修改元素
fruits.append("orange")        # 末尾添加
fruits.insert(1, "kiwi")       # 指定位置插入
fruits.extend(["mango", "pear"]) # 扩展列表

# 删除
fruits.remove("banana")        # 按值删除（第一个匹配）
popped = fruits.pop()          # 弹出最后一个
popped = fruits.pop(2)         # 弹出索引2的元素
del fruits[0]                  # 按索引删除
fruits.clear()                 # 清空所有元素

# 查找与排序
fruits.index("apple")          # 查找索引（不存在则报错）
fruits.count("apple")          # 计数
fruits.sort()                  # 原地排序
fruits.sort(reverse=True)      # 降序
fruits.sort(key=len)           # 按长度排序
sorted_fruits = sorted(fruits) # 返回新排序列表

# 切片赋值
nums = [1, 2, 3, 4, 5]
nums[1:3] = [20, 30]           # [1, 20, 30, 4, 5]
nums[1:3] = []                 # [1, 4, 5]（删除切片范围）

# 列表推导式（见第15章）
squares = [x**2 for x in range(10)]  # [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
```

### 6.2 元组 (tuple)

元组是**有序、不可变**的序列。

```python
# 创建
point = (3, 4)
single = (1,)           # 单元素元组必须有逗号！
empty = ()
without_parens = 1, 2, 3  # 不加括号也可

# 访问（与列表相同）
point[0]        # 3
point[1]        # 4
x, y = point    # 解包

# 不可变性
# point[0] = 5  # TypeError! 元组不可修改

# 用途
def get_location():
    return (25.0478, 121.5318)  # 返回多个值

# namedtuple：有名字的元组
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(3, 4)
print(p.x, p.y)  # 3 4
```

### 6.3 集合 (set)

集合是**无序、可变、不重复**的容器。

```python
# 创建
colors = {"red", "green", "blue"}
from_list = set([1, 2, 2, 3, 3, 3])  # {1, 2, 3} 自动去重
empty = set()           # 空集合必须用 set()，{} 是空字典

# 基本操作
colors.add("yellow")    # 添加
colors.remove("red")    # 删除（元素不存在则报错）
colors.discard("red")   # 删除（元素不存在不报错）
popped = colors.pop()   # 随机弹出（注意：无序！）

# 集合运算
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

a | b     # 并集: {1, 2, 3, 4, 5, 6}
a & b     # 交集: {3, 4}
a - b     # 差集: {1, 2}
a ^ b     # 对称差集: {1, 2, 5, 6}

# 子集/超集判断
{1, 2}.issubset({1, 2, 3})      # True
{1, 2, 3}.issuperset({1, 2})    # True

# 集合推导式
evens = {x for x in range(20) if x % 2 == 0}

# frozenset：不可变集合，可作为字典的键
frozen = frozenset([1, 2, 3])
```

### 6.4 字典 (dict)

字典是**键值对**映射结构（Python 3.7+ 保持插入顺序）。

```python
# 创建
user = {"name": "Alice", "age": 28, "city": "Beijing"}
from_tuples = dict([("a", 1), ("b", 2)])
from_kwargs = dict(name="Bob", age=30)
# 字典推导式（见第15章）

# 访问
user["name"]             # "Alice"（键不存在则 KeyError）
user.get("email", "N/A") # "N/A"（键不存在返回默认值）
user.keys()              # dict_keys(['name', 'age', 'city'])
user.values()            # dict_values(['Alice', 28, 'Beijing'])
user.items()             # dict_items([('name', 'Alice'), ...])

# 修改
user["email"] = "alice@example.com"  # 添加/修改
user.update({"age": 29, "city": "Shanghai"})  # 批量更新
user.setdefault("role", "user")      # 如键不存在则设置默认值

# 删除
del user["city"]          # 删除键
email = user.pop("email") # 弹出并返回值
item = user.popitem()     # 弹出最后一项 (Python 3.7+)

# 合并字典（Python 3.5+ / 3.9+）
a = {"x": 1, "y": 2}
b = {"y": 3, "z": 4}
merged = {**a, **b}       # 3.5+: {'x': 1, 'y': 3, 'z': 4}
merged = a | b            # 3.9+: 同上

# 遍历
for key in user:
    print(key, user[key])

for key, value in user.items():
    print(f"{key}: {value}")
```

---

## 7. 运算符全解

### 7.1 算术运算符

```python
a, b = 10, 3

a + b      # 加: 13
a - b      # 减: 7
a * b      # 乘: 30
a / b      # 除: 3.3333...（始终返回 float）
a // b     # 地板除: 3（向下取整）
a % b      # 取模: 1
a ** b     # 幂: 1000
-a         # 取反: -10
```

### 7.2 比较运算符

```python
x, y = 5, 10

x == y    # 等于: False
x != y    # 不等于: True
x > y     # 大于: False
x < y     # 小于: True
x >= y    # 大于等于: False
x <= y    # 小于等于: True

# 链式比较（Python 特有）
age = 25
18 <= age <= 65   # True（等价于 18 <= age and age <= 65）

# is 与 == 的区别
a = [1, 2, 3]
b = [1, 2, 3]
a == b    # True（值相等）
a is b    # False（不是同一个对象）
```

### 7.3 逻辑运算符

```python
x, y = True, False

x and y    # False（短路：x 为假则返回 x）
x or y     # True（短路：x 为真则返回 x）
not x      # False

# 短路求值——常用于安全检查
name = None
safe_name = name and name.upper()  # None（不会因 None.upper() 报错）

# 赋值中的 or 技巧
config = user_input or "default_value"  # user_input 为空时用默认值
```

### 7.4 位运算符

```python
a, b = 0b1100, 0b1010  # 12, 10

a & b     # 按位与: 8   (0b1000)
a | b     # 按位或: 14  (0b1110)
a ^ b     # 按位异或: 6 (0b0110)
~a        # 按位取反: -13
a << 1    # 左移: 24
a >> 1    # 右移: 6
```

### 7.5 成员运算符

```python
fruits = ["apple", "banana", "cherry"]

"apple" in fruits       # True
"grape" not in fruits   # True

# 对字典检查的是键
user = {"name": "Alice"}
"name" in user  # True
"Alice" in user  # False（检查键，非值）
```

### 7.6 运算符优先级（从高到低）

```
**         幂
~ + -      按位取反、正负号
* / // %   乘除
+ -        加减
<< >>      移位
&          按位与
^          按位异或
|          按位或
== != > <  比较
not        逻辑非
and        逻辑与
or         逻辑或
=          赋值
```

---

## 8. 控制流

### 8.1 条件语句

```python
score = 85

if score >= 90:
    grade = "A"
elif score >= 80:
    grade = "B"
elif score >= 70:
    grade = "C"
elif score >= 60:
    grade = "D"
else:
    grade = "F"

print(f"Your grade is {grade}")

# 三元表达式（条件表达式）
age = 20
status = "Adult" if age >= 18 else "Minor"

# 多条件三元（可读性较差，不推荐嵌套）
x = 15
result = "大" if x > 10 else ("中" if x > 5 else "小")
```

### 8.2 match-case 语句（Python 3.10+）

```python
command = "quit"

match command:
    case "start":
        print("Starting...")
    case "stop":
        print("Stopping...")
    case "pause" | "resume":      # 多个值用 | 分隔
        print("Toggling state...")
    case str(s) if len(s) > 10:   # 支持守卫条件
        print(f"Long command: {s}")
    case _:                        # 默认匹配
        print("Unknown command")

# 结构匹配（解构复杂数据）
point = (0, 5)
match point:
    case (0, 0):
        print("原点")
    case (0, y):
        print(f"Y轴上的点: y={y}")
    case (x, 0):
        print(f"X轴上的点: x={x}")
    case (x, y):
        print(f"坐标: ({x}, {y})")

# 匹配字典结构
data = {"type": "user", "name": "Alice"}
match data:
    case {"type": "user", "name": name}:
        print(f"User: {name}")
    case {"type": "admin", "name": name}:
        print(f"Admin: {name}")
```

### 8.3 for 循环

```python
# 遍历列表
fruits = ["apple", "banana", "cherry"]
for fruit in fruits:
    print(fruit)

# range() 生成数字序列
for i in range(5):       # 0, 1, 2, 3, 4
    print(i)

for i in range(2, 10, 3):  # start=2, stop=10, step=3 → 2, 5, 8
    print(i)

# enumerate()：带索引遍历
for index, fruit in enumerate(fruits):
    print(f"{index}: {fruit}")

for index, fruit in enumerate(fruits, start=1):  # 从1开始计数
    print(f"{index}: {fruit}")

# zip()：并行遍历
names = ["Alice", "Bob", "Charlie"]
ages = [28, 35, 42]
for name, age in zip(names, ages):
    print(f"{name} is {age} years old")

# 遍历字典
user = {"name": "Alice", "age": 28}
for key in user:
    print(key, "→", user[key])

for key, value in user.items():
    print(f"{key}: {value}")

# break / continue / else
for n in range(10):
    if n == 3:
        continue    # 跳过本次迭代
    if n == 7:
        break       # 终止循环
    print(n)
else:
    # 仅当循环正常结束（未被 break 中断）时执行
    print("循环正常完成")
```

### 8.4 while 循环

```python
# 基本 while
count = 0
while count < 5:
    print(count)
    count += 1

# while True + break 模式（常见于交互式程序）
while True:
    user_input = input("输入 'quit' 退出: ")
    if user_input == "quit":
        break
    print(f"你输入了: {user_input}")

# while-else（与 for-else 类似）
n = 0
while n < 3:
    print(n)
    n += 1
else:
    print("循环正常完成")
```

---

## 9. 函数

### 9.1 函数定义与调用

```python
# 基本定义
def greet(name):
    """向指定的人问好（docstring）"""
    return f"Hello, {name}!"

# 调用
result = greet("Alice")
print(result)  # "Hello, Alice!"
```

### 9.2 参数类型

```python
# --- 位置参数 ---
def add(a, b):
    return a + b

add(3, 5)       # 8
add(b=5, a=3)   # 8（关键字参数也可）

# --- 默认参数 ---
def power(base, exponent=2):
    return base ** exponent

power(3)        # 9
power(3, 3)     # 27

# ⚠️ 默认参数陷阱：默认值在函数定义时只计算一次！
def append_to(element, target=[]):
    target.append(element)
    return target

print(append_to(1))  # [1]
print(append_to(2))  # [1, 2] ← 不是 [2]！

# ✅ 正确做法：
def append_to(element, target=None):
    if target is None:
        target = []
    target.append(element)
    return target

# --- 可变参数 *args（元组） ---
def sum_all(*numbers):
    """接收任意数量的位置参数"""
    return sum(numbers)

sum_all(1, 2, 3, 4, 5)  # 15

# --- 可变参数 **kwargs（字典） ---
def print_info(**details):
    """接收任意数量的关键字参数"""
    for key, value in details.items():
        print(f"{key}: {value}")

print_info(name="Alice", age=28, city="Beijing")

# --- 仅限关键字参数（*, /）---
def configure(host, port, *, timeout=30, ssl=False):
    """timeout 和 ssl 必须通过关键字传递"""
    return f"{host}:{port}, timeout={timeout}, ssl={ssl}"

configure("localhost", 8080, timeout=60)
# configure("localhost", 8080, 60)  # 报错！必须是关键字参数

# --- 仅限位置参数（Python 3.8+）---
def point(x, y, /):  # x, y 只能通过位置传递
    return (x, y)

point(3, 4)
# point(x=3, y=4)  # 报错！不能使用关键字
```

### 9.3 返回值

```python
# Python 函数默认返回 None
def no_return():
    pass

print(no_return())  # None

# 返回多个值（实际返回元组）
def min_max(numbers):
    return min(numbers), max(numbers)

result = min_max([3, 1, 7, 2, 9])
print(result)       # (1, 9)
a, b = min_max([3, 1, 7, 2, 9])  # 解包
```

### 9.4 lambda 匿名函数

```python
# 语法: lambda 参数: 表达式
square = lambda x: x ** 2
square(5)  # 25

add = lambda a, b: a + b
add(3, 7)  # 10

# 常用于 sorted()、map()、filter() 等
users = [{"name": "Bob", "age": 30}, {"name": "Alice", "age": 25}]
users.sort(key=lambda u: u["age"])  # 按年龄排序

# ⚠️ lambda 仅限于简单表达式，复杂逻辑请用 def
```

### 9.5 作用域与闭包

```python
# LEGB 规则：Local → Enclosing → Global → Built-in

x = "global"

def outer():
    x = "enclosing"

    def inner():
        x = "local"
        print(x)          # "local"（优先使用 Local）

    inner()
    print(x)              # "enclosing"

outer()
print(x)                  # "global"

# global 关键字
counter = 0

def increment():
    global counter
    counter += 1          # 不加 global 会报 UnboundLocalError

# nonlocal 关键字（修改外层非全局变量）
def make_counter():
    count = 0
    def counter():
        nonlocal count
        count += 1
        return count
    return counter

c = make_counter()
print(c())  # 1
print(c())  # 2
```

---

## 10. 模块与包

### 10.1 模块

模块是一个包含 Python 代码的 `.py` 文件。

```python
# mymath.py
def add(a, b):
    return a + b

PI = 3.14159
```

```python
# main.py
# 方式1：导入整个模块
import mymath
print(mymath.add(3, 5))
print(mymath.PI)

# 方式2：导入特定名称
from mymath import add, PI
print(add(3, 5))

# 方式3：导入所有名称（不推荐）
from mymath import *

# 方式4：使用别名
import mymath as mm
from mymath import add as addition

# __name__ 检测
if __name__ == "__main__":
    # 仅当文件作为主程序运行时执行
    print("这个脚本是直接运行的")
```

### 10.2 包

包是包含 `__init__.py` 的目录。

```
myapp/
    __init__.py      # 标识为包，可为空
    models/
        __init__.py
        user.py
        order.py
    utils/
        __init__.py
        security.py
        validation.py
    main.py
```

```python
# 导入包内模块
from myapp.models.user import User
from myapp.utils import security

# __init__.py 可以简化导入接口
# myapp/models/__init__.py:
from .user import User
from .order import Order

# 现在可以直接使用：
from myapp.models import User, Order
```

### 10.3 常用内置模块

```python
import sys
sys.argv          # 命令行参数列表
sys.path          # 模块搜索路径
sys.exit(0)       # 退出程序

import os
os.getcwd()          # 当前工作目录
os.listdir(".")      # 列出目录内容
os.path.join("a", "b", "c")  # 拼接路径
os.path.exists(path) # 路径是否存在
os.makedirs("a/b/c", exist_ok=True)  # 递归创建目录

import platform
platform.system()   # "Windows" / "Linux" / "Darwin"
```

---

## 11. 文件操作

### 11.1 文件读写基础

```python
# --- 读取文件 ---
# 一次性读取全部
with open("data.txt", "r", encoding="utf-8") as f:
    content = f.read()

# 逐行读取
with open("data.txt", "r", encoding="utf-8") as f:
    for line in f:
        print(line.strip())

# readlines() 返回行列表
with open("data.txt", "r", encoding="utf-8") as f:
    lines = f.readlines()

# --- 写入文件 ---
# 覆盖写入
with open("output.txt", "w", encoding="utf-8") as f:
    f.write("Hello\n")
    f.writelines(["Line 1\n", "Line 2\n"])

# 追加写入
with open("log.txt", "a", encoding="utf-8") as f:
    f.write("New log entry\n")
```

### 11.2 文件模式

| 模式 | 说明 |
|------|------|
| `r` | 只读（文件必须存在） |
| `w` | 覆盖写入（创建或清空） |
| `a` | 追加写入 |
| `x` | 排他创建（文件已存在则报错） |
| `r+` | 读写（文件必须存在） |
| `w+` | 读写（创建或清空） |
| `b` | 二进制模式（如 `rb`、`wb`） |
| `t` | 文本模式（默认） |

### 11.3 文件路径处理（pathlib，推荐）

```python
from pathlib import Path

# 创建路径对象
path = Path("data/files/example.txt")

# 路径信息
path.name        # "example.txt"
path.stem        # "example"（不含后缀）
path.suffix      # ".txt"
path.parent      # Path("data/files")

# 路径操作
new_path = path.with_suffix(".csv")  # 替换后缀
new_path = path.parent / "other.txt" # / 运算符拼接路径

# 文件操作
content = path.read_text(encoding="utf-8")   # 读取文本
path.write_text("Hello", encoding="utf-8")   # 写入文本
data = path.read_bytes()                     # 读取二进制
path.write_bytes(b"\x00\x01")                # 写入二进制

# 目录操作
Path("new_dir").mkdir(exist_ok=True)         # 创建目录
for f in Path(".").glob("*.py"):             # 匹配文件
    print(f)
for f in Path(".").rglob("*.py"):            # 递归匹配
    print(f)
```

### 11.4 CSV 与 JSON 操作

```python
import csv

# 读取 CSV
with open("data.csv", "r", encoding="utf-8") as f:
    reader = csv.DictReader(f)
    for row in reader:
        print(row["name"], row["age"])

# 写入 CSV
with open("output.csv", "w", newline="", encoding="utf-8") as f:
    writer = csv.DictWriter(f, fieldnames=["name", "age"])
    writer.writeheader()
    writer.writerow({"name": "Alice", "age": 28})

import json

# Python → JSON
data = {"name": "Alice", "age": 28, "hobbies": ["reading", "coding"]}
json_str = json.dumps(data, indent=2, ensure_ascii=False)
with open("data.json", "w") as f:
    json.dump(data, f, indent=2)

# JSON → Python
with open("data.json", "r") as f:
    loaded = json.load(f)
json_obj = json.loads(json_str)  # 从字符串加载
```

---

## 12. 异常处理

### 12.1 基本语法

```python
try:
    num = int(input("输入一个数字: "))
    result = 100 / num
    print(f"结果: {result}")

except ValueError:
    print("请输入有效的数字！")

except ZeroDivisionError:
    print("不能除以零！")

except (TypeError, KeyError) as e:
    print(f"发生错误: {e}")

except Exception as e:
    print(f"未知错误: {type(e).__name__}: {e}")

else:
    # try 块无异常时执行
    print("计算成功！")

finally:
    # 无论是否有异常都执行（常用于资源清理）
    print("处理完毕")
```

### 12.2 自定义异常与 raise

```python
# 自定义异常
class ValidationError(Exception):
    def __init__(self, message, field=None):
        super().__init__(message)
        self.field = field

# 抛出异常
def validate_age(age):
    if age < 0:
        raise ValidationError("年龄不能为负数", field="age")
    if age > 150:
        raise ValidationError("年龄超出合理范围", field="age")
    return age

# 使用
try:
    validate_age(-5)
except ValidationError as e:
    print(f"验证失败: {e}, 字段: {e.field}")

# raise from：异常链
def process_data(filename):
    try:
        with open(filename) as f:
            return f.read()
    except FileNotFoundError as e:
        raise RuntimeError(f"处理 {filename} 时出错") from e
```

---

## 13. 面向对象编程

### 13.1 类与对象

```python
class Dog:
    """犬类"""
    # 类变量（所有实例共享）
    species = "Canis familiaris"

    # 构造方法
    def __init__(self, name, age):
        # 实例变量（每个实例独有）
        self.name = name
        self.age = age
        self._tag = 0  # "私有" 约定（实际可访问）

    # 实例方法
    def bark(self):
        return f"{self.name} says Woof!"

    # __str__：面向用户的描述（print 调用）
    def __str__(self):
        return f"{self.name} ({self.age} years old)"

    # __repr__：面向开发者的描述（交互式解释器调用）
    def __repr__(self):
        return f"Dog(name='{self.name}', age={self.age})"

    # __eq__：定义相等性
    def __eq__(self, other):
        if not isinstance(other, Dog):
            return NotImplemented
        return self.name == other.name and self.age == other.age


# 创建实例
my_dog = Dog("Rex", 3)
print(my_dog.bark())    # "Rex says Woof!"
print(my_dog)           # "Rex (3 years old)"  ——调用 __str__
print(repr(my_dog))     # "Dog(name='Rex', age=3)" ——调用 __repr__
print(Dog.species)      # 类变量
```

### 13.2 封装（@property）

```python
class BankAccount:
    def __init__(self, balance=0):
        self._balance = balance  # 下划线表示"受保护"

    # getter
    @property
    def balance(self):
        """账户余额（只读）"""
        return self._balance

    # setter
    @balance.setter
    def balance(self, amount):
        if amount < 0:
            raise ValueError("余额不能为负")
        self._balance = amount

    # deleter
    @balance.deleter
    def balance(self):
        print("删除余额字段")
        del self._balance

    # 计算属性
    @property
    def formatted_balance(self):
        return f"¥{self._balance:,.2f}"


account = BankAccount(1000)
print(account.balance)             # 1000（调用 getter）
account.balance = 2000             # 调用 setter
# account.balance = -100           # ValueError!
print(account.formatted_balance)   # "¥2,000.00"
```

### 13.3 继承

```python
class Animal:
    def __init__(self, name):
        self.name = name

    def speak(self):
        raise NotImplementedError("子类必须实现此方法")

class Cat(Animal):
    def speak(self):
        return f"{self.name} says Meow!"

class Lion(Cat):
    def speak(self):
        parent_sound = super().speak()  # 调用父类方法
        return f"{parent_sound} *Roar!*"

# 多继承（MRO：方法解析顺序，C3 线性化）
class A:
    def method(self):
        print("A")

class B(A):
    def method(self):
        print("B")

class C(A):
    def method(self):
        print("C")

class D(B, C):   # MRO: D → B → C → A
    pass

print(D.mro())
# [D, B, C, A, object]
```

### 13.4 多态与鸭子类型

```python
# 鸭子类型："如果它走起来像鸭子，叫起来像鸭子，那它就是鸭子"
# Python 不强制类型检查，只看对象是否有对应方法

def make_sound(animal):
    """任何有 speak() 方法的对象都可以"""
    print(animal.speak())

class Dog:
    def speak(self):
        return "Woof!"

class Duck:
    def speak(self):
        return "Quack!"

class Car:
    def honk(self):
        return "Beep!"

make_sound(Dog())   # "Woof!"
make_sound(Duck())  # "Quack!"
# make_sound(Car()) # AttributeError: Car 没有 speak()
```

### 13.5 特殊方法（Magic Methods）

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):       # +
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):       # -
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar):      # *
        return Vector(self.x * scalar, self.y * scalar)

    def __rmul__(self, scalar):     # 右乘（scalar * v）
        return self.__mul__(scalar)

    def __len__(self):              # len()
        return int((self.x**2 + self.y**2) ** 0.5)

    def __getitem__(self, index):   # v[0], v[1]
        return (self.x, self.y)[index]

    def __iter__(self):             # for in
        yield self.x
        yield self.y

    def __bool__(self):             # bool()
        return self.x != 0 or self.y != 0

    def __call__(self):             # 将实例当作函数调用
        return f"Vector({self.x}, {self.y})"

v1 = Vector(3, 4)
v2 = Vector(1, 2)
print(v1 + v2)        # Vector(4, 6)
print(3 * v1)         # Vector(9, 12)
print(v1[0])          # 3
print(list(v1))       # [3, 4]
```

---

## 14. 常用标准库

### 14.1 datetime：日期时间

```python
from datetime import datetime, date, timedelta, timezone

# 当前时间
now = datetime.now()
today = date.today()

# 创建
dt = datetime(2024, 6, 15, 14, 30, 0)
d = date(2024, 6, 15)

# 格式化
print(now.strftime("%Y-%m-%d %H:%M:%S"))  # "2024-06-15 14:30:00"

# 解析字符串
parsed = datetime.strptime("2024-06-15", "%Y-%m-%d")

# 时间运算
tomorrow = today + timedelta(days=1)
one_week_ago = today - timedelta(weeks=1)
diff = datetime(2024, 6, 15) - datetime(2024, 6, 1)  # timedelta(days=14)

# 时区
utc_now = datetime.now(timezone.utc)
```

### 14.2 re：正则表达式

```python
import re

text = "Email: alice@example.com, Phone: 138-1234-5678"

# 常用函数
pattern = r"\b[\w.-]+@[\w.-]+\.\w+\b"

re.search(pattern, text)           # 返回第一个 Match 对象
re.findall(pattern, text)          # 返回所有匹配的字符串列表
re.match(pattern, text)            # 从开头匹配
re.sub(r"\d", "X", text)           # 替换
re.split(r"[,;]", "a,b;c")         # 按模式分割

# 编译模式（重复使用时提升性能）
email_pattern = re.compile(r"\b[\w.-]+@[\w.-]+\.\w+\b")
match = email_pattern.search(text)
if match:
    print(match.group())           # "alice@example.com"

# 分组
phone_pattern = re.compile(r"(\d{3})-(\d{4})-(\d{4})")
match = phone_pattern.search(text)
if match:
    print(match.group(0))          # 完整匹配
    print(match.group(1))          # 第一组
    print(match.groups())          # 所有组的元组

# 命名分组
p = re.compile(r"(?P<year>\d{4})-(?P<month>\d{2})-(?P<day>\d{2})")
m = p.search("2024-06-15")
print(m.group("year"))  # "2024"
```

### 14.3 random：随机数

```python
import random

random.random()             # [0.0, 1.0) 浮点数
random.randint(1, 10)       # [1, 10] 整数（包含10）
random.randrange(0, 10, 2)  # 从 [0, 2, 4, 6, 8] 中选取
random.choice(["a", "b", "c"])         # 随机选一个
random.choices(["a", "b", "c"], k=5)   # 随机选5个（可重复）
random.sample(["a", "b", "c"], k=2)    # 随机选2个（不重复）
random.shuffle(my_list)                # 原地打乱列表

# 可重复的随机（设置种子）
random.seed(42)
```

### 14.4 collections：高级容器

```python
from collections import Counter, defaultdict, OrderedDict, deque, ChainMap

# Counter：计数器
words = ["a", "b", "c", "a", "b", "a"]
counter = Counter(words)
print(counter)              # Counter({'a': 3, 'b': 2, 'c': 1})
print(counter.most_common(2))  # [('a', 3), ('b', 2)]

# defaultdict：带默认值的字典
dd = defaultdict(list)
dd["key"].append(1)         # 无需检查键是否存在
dd = defaultdict(lambda: 0) # 自定义默认值

# deque：双向队列（两端 O(1) 操作）
dq = deque([1, 2, 3])
dq.append(4)                # 右端添加
dq.appendleft(0)            # 左端添加
dq.pop()                    # 右端弹出
dq.popleft()                # 左端弹出

# ChainMap：链式查找多个字典
config = ChainMap({"debug": True}, {"host": "localhost"}, {})
```

### 14.5 itertools：迭代器工具

```python
from itertools import count, cycle, repeat, chain, combinations, product, islice

# 无限迭代器
count(10, 2)       # 10, 12, 14, 16, ...
cycle("ABC")       # A, B, C, A, B, C, ...
repeat("X", 3)     # X, X, X

# 组合迭代器
chain([1, 2], [3, 4])                    # 1, 2, 3, 4
combinations("ABC", 2)                   # AB, AC, BC
permutations("ABC", 2)                   # AB, AC, BA, BC, CA, CB
product("AB", "12")                      # A1, A2, B1, B2

# 切片迭代器
islice(range(100), 10, 20)               # 10..19
```

---

# 第二部分：进阶内容

## 15. 高级函数式编程

### 15.1 推导式详解

```python
# --- 列表推导式 ---
# 基本：[表达式 for 变量 in 可迭代对象 if 条件]
squares = [x**2 for x in range(10)]
evens = [x for x in range(20) if x % 2 == 0]

# 嵌套推导式：展开二维列表
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
flattened = [num for row in matrix for num in row]  # [1,2,3,4,5,6,7,8,9]

# if-else 在推导式中
labels = ["even" if x % 2 == 0 else "odd" for x in range(10)]

# --- 字典推导式 ---
squares_dict = {x: x**2 for x in range(5)}  # {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}

# 键值对换
original = {"a": 1, "b": 2, "c": 3}
flipped = {v: k for k, v in original.items()}  # {1: 'a', 2: 'b', 3: 'c'}

# --- 集合推导式 ---
unique_lengths = {len(word) for word in ["hello", "world", "python", "code"]}  # {4, 5, 6}

# --- 生成器表达式（惰性求值，节省内存）---
# 使用 () 而非 []
gen = (x**2 for x in range(10**9))  # 不会立即在内存中创建十亿个元素
print(next(gen))  # 0
print(next(gen))  # 1
```

### 15.2 map / filter / reduce

```python
from functools import reduce

nums = [1, 2, 3, 4, 5]

# map：对每个元素应用函数
doubled = list(map(lambda x: x * 2, nums))  # [2, 4, 6, 8, 10]
# 推导式更 Pythonic： [x * 2 for x in nums]

# filter：保留满足条件的元素
evens = list(filter(lambda x: x % 2 == 0, nums))  # [2, 4]
# 推导式更 Pythonic： [x for x in nums if x % 2 == 0]

# reduce：累积计算
total = reduce(lambda acc, x: acc + x, nums)      # 15
product = reduce(lambda acc, x: acc * x, nums)    # 120
```

### 15.3 functools 高阶函数

```python
from functools import lru_cache, partial, singledispatch, wraps

# lru_cache：函数结果缓存（Memoization）
@lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

print(fibonacci(100))  # 瞬间计算完毕

# partial：部分应用参数
def power(base, exponent):
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)
print(square(5))  # 25
print(cube(5))    # 125

# singledispatch：按类型分发（函数重载）
@singledispatch
def process(arg):
    """默认处理"""
    raise NotImplementedError(f"不支持的类型: {type(arg)}")

@process.register(int)
def _(arg):
    return f"整数: {arg}"

@process.register(str)
def _(arg):
    return f"字符串: {arg.upper()}"

@process.register(list)
def _(arg):
    return f"列表长度: {len(arg)}"

print(process(42))       # "整数: 42"
print(process("hello"))  # "字符串: HELLO"
```

---

## 16. 装饰器

### 16.1 装饰器基础

装饰器本质是**接受函数作为参数、返回新函数**的高阶函数。

```python
# 基本装饰器
def timer(func):
    import time

    @wraps(func)  # 保留原函数的元数据（__name__, __doc__ 等）
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f"{func.__name__} 执行耗时: {elapsed:.4f}s")
        return result

    return wrapper

@timer
def slow_function():
    import time
    time.sleep(0.5)
    return "done"

# slow_function = timer(slow_function)  ← @timer 的本质
print(slow_function())  # "done"（同时打印耗时）
```

### 16.2 带参数的装饰器

```python
import time

def retry(max_attempts=3, delay=1):
    """带参数的重试装饰器"""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(1, max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts:
                        raise  # 最后一次也失败了，抛出异常
                    print(f"第 {attempt} 次失败: {e}，{delay}s 后重试...")
                    time.sleep(delay)
            return None
        return wrapper
    return decorator

@retry(max_attempts=3, delay=0.5)
def unstable_api():
    import random
    if random.random() < 0.7:
        raise ConnectionError("网络错误")
    return "成功！"
```

### 16.3 类装饰器

```python
class Singleton:
    """单例模式装饰器"""
    def __init__(self, cls):
        self._cls = cls
        self._instance = None

    def __call__(self, *args, **kwargs):
        if self._instance is None:
            self._instance = self._cls(*args, **kwargs)
        return self._instance

@Singleton
class DatabaseConnection:
    def __init__(self):
        print("创建数据库连接（仅一次）")

db1 = DatabaseConnection()  # 打印一次
db2 = DatabaseConnection()  # 不打印
print(db1 is db2)           # True
```

### 16.4 实战：权限校验装饰器

```python
from functools import wraps

def require_permission(permission):
    """验证用户是否拥有指定权限"""
    def decorator(func):
        @wraps(func)
        def wrapper(current_user, *args, **kwargs):
            if permission not in current_user.get("permissions", []):
                raise PermissionError(f"需要 {permission} 权限")
            return func(current_user, *args, **kwargs)
        return wrapper
    return decorator

@require_permission("admin")
def delete_user(current_user, user_id):
    return f"删除用户 {user_id} 成功"

# 使用
user = {"name": "Alice", "permissions": ["admin", "write"]}
print(delete_user(user, 123))  # 成功

guest = {"name": "Guest", "permissions": ["read"]}
# delete_user(guest, 123)  # PermissionError!
```

---

## 17. 生成器与迭代器

### 17.1 迭代器协议

```python
# 任何实现了 __iter__() 和 __next__() 的对象都是迭代器
class CountDown:
    def __init__(self, start):
        self.current = start

    def __iter__(self):
        return self

    def __next__(self):
        if self.current <= 0:
            raise StopIteration
        self.current -= 1
        return self.current + 1

for n in CountDown(5):
    print(n)  # 5, 4, 3, 2, 1

# iter() 和 next() 底层原理
it = iter([1, 2, 3])  # 获取迭代器
print(next(it))       # 1
print(next(it))       # 2
print(next(it))       # 3
# next(it) 会抛出 StopIteration
```

### 17.2 生成器函数（yield）

生成器是创建迭代器的**最简洁方式**。

```python
# 生成器函数：使用 yield 而不是 return
def fibonacci_gen(n):
    """生成前 n 个斐波那契数"""
    a, b = 0, 1
    for _ in range(n):
        yield a
        a, b = b, a + b

# 使用
for num in fibonacci_gen(10):
    print(num, end=" ")  # 0 1 1 2 3 5 8 13 21 34

# yield from：委托给子生成器
def chain_generators(*gens):
    for gen in gens:
        yield from gen  # 等价于: for item in gen: yield item

# 生成器的 send() 方法（协程基础）
def echo_generator():
    while True:
        received = yield
        print(f"收到: {received}")

echo = echo_generator()
next(echo)              # 启动生成器（推进到第一个 yield）
echo.send("Hello")      # "收到: Hello"
echo.send("World")      # "收到: World"
echo.close()            # 关闭生成器
```

### 17.3 实战：流式处理大文件

```python
def read_large_file(file_path, chunk_size=8192):
    """逐块读取大文件，避免全部加载到内存"""
    with open(file_path, "r", encoding="utf-8") as f:
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            yield chunk

def process_logs(file_path, keyword):
    """逐行处理大型日志文件，查找关键字"""
    with open(file_path, "r", encoding="utf-8") as f:
        for line_num, line in enumerate(f, 1):
            if keyword in line:
                yield {"line": line_num, "content": line.strip()}

# 使用：10GB 日志文件也不会耗尽内存
for match in process_logs("huge_server.log", "ERROR"):
    print(f"第 {match['line']} 行: {match['content']}")
```

---

## 18. 上下文管理器

### 18.1 基于类的上下文管理器

```python
class DatabaseConnection:
    def __init__(self, db_url):
        self.db_url = db_url
        self.connection = None

    def __enter__(self):
        print(f"连接数据库: {self.db_url}")
        self.connection = {"connected": True}  # 模拟连接
        return self.connection

    def __exit__(self, exc_type, exc_val, exc_tb):
        print("关闭数据库连接")
        self.connection = None
        # 返回 True 会抑制异常（通常不这样做）
        return False

with DatabaseConnection("postgresql://localhost/mydb") as conn:
    print(f"执行查询... {conn}")
# 输出:
# 连接数据库: postgresql://localhost/mydb
# 执行查询... {'connected': True}
# 关闭数据库连接
```

### 18.2 基于生成器的上下文管理器

```python
from contextlib import contextmanager

@contextmanager
def timed_block(name="代码块"):
    """测量代码块执行耗时"""
    import time
    start = time.perf_counter()
    try:
        yield  # 在此处执行 with 块内的代码
    finally:
        elapsed = time.perf_counter() - start
        print(f"[{name}] 耗时: {elapsed:.4f}s")

with timed_block("数据处理"):
    import time
    time.sleep(0.3)
# [数据处理] 耗时: 0.3000s

@contextmanager
def temp_env_var(key, value):
    """临时设置环境变量，退出时恢复"""
    import os
    old = os.environ.get(key)
    os.environ[key] = value
    try:
        yield
    finally:
        if old is None:
            os.environ.pop(key, None)
        else:
            os.environ[key] = old
```

### 18.3 实用 contextlib 工具

```python
from contextlib import suppress, redirect_stdout, ExitStack
import io

# suppress：优雅忽略指定异常
with suppress(FileNotFoundError):
    os.remove("nonexistent_file.txt")
# 不会报错，等价于：
# try: os.remove(...)
# except FileNotFoundError: pass

# redirect_stdout：重定向标准输出
buffer = io.StringIO()
with redirect_stdout(buffer):
    print("这句话被重定向到 buffer 而非控制台")
output = buffer.getvalue()

# ExitStack：动态管理多个上下文管理器
with ExitStack() as stack:
    files = [stack.enter_context(open(f"file_{i}.txt", "w")) for i in range(5)]
    for i, f in enumerate(files):
        f.write(f"Content {i}")
# 所有文件自动关闭
```

---

## 19. 元类与描述符

### 19.1 描述符（Descriptor）

描述符协议：实现 `__get__`、`__set__`、`__delete__` 中任意一个方法。

```python
class Validator:
    """属性验证描述符"""
    def __init__(self, min_value=None, max_value=None):
        self.min_value = min_value
        self.max_value = max_value
        self.name = None  # 将在 __set_name__ 中设置

    def __set_name__(self, owner, name):
        """Python 3.6+：自动获取属性名"""
        self.name = name

    def __get__(self, instance, owner):
        if instance is None:
            return self
        return instance.__dict__.get(self.name)

    def __set__(self, instance, value):
        if not isinstance(value, (int, float)):
            raise TypeError(f"{self.name} 必须是数字")
        if self.min_value is not None and value < self.min_value:
            raise ValueError(f"{self.name} 不能小于 {self.min_value}")
        if self.max_value is not None and value > self.max_value:
            raise ValueError(f"{self.name} 不能大于 {self.max_value}")
        instance.__dict__[self.name] = value


class Person:
    age = Validator(min_value=0, max_value=150)
    score = Validator(min_value=0, max_value=100)

    def __init__(self, name, age, score):
        self.name = name
        self.age = age
        self.score = score

# 使用
p = Person("Alice", 28, 95)
p.age = 30     # 正常
# p.age = -5   # ValueError: age 不能小于 0
# p.age = "30" # TypeError: age 必须是数字
```

### 19.2 元类（Metaclass）

元类是**类的类**——控制类的创建过程。

```python
# 元类基本结构
class Meta(type):
    """自定义元类"""
    def __new__(mcs, name, bases, namespace, **kwargs):
        # 在类创建前修改
        print(f"创建类: {name}")

        # 自动为所有方法添加日志
        for key, value in namespace.items():
            if callable(value) and not key.startswith("__"):
                namespace[key] = mcs._add_logging(value, key)

        return super().__new__(mcs, name, bases, namespace)

    @staticmethod
    def _add_logging(func, func_name):
        @wraps(func)
        def wrapper(*args, **kwargs):
            print(f"[LOG] 调用 {func_name}")
            return func(*args, **kwargs)
        return wrapper


class MyService(metaclass=Meta):
    def process(self, data):
        return f"处理: {data}"

    def validate(self, data):
        return len(data) > 0


service = MyService()
service.process("hello")  # [LOG] 调用 process
service.validate("test")  # [LOG] 调用 validate
```

### 19.3 实战：ORM 模型基类

```python
class ModelMeta(type):
    """为模型类自动收集字段定义"""
    def __new__(mcs, name, bases, namespace):
        if name == "Model":
            return super().__new__(mcs, name, bases, namespace)

        fields = {}
        for key, value in namespace.items():
            if isinstance(value, Field):
                fields[key] = value
                value.name = key

        cls = super().__new__(mcs, name, bases, namespace)
        cls._fields = fields
        return cls


class Field:
    def __init__(self, field_type=str, required=False, default=None):
        self.field_type = field_type
        self.required = required
        self.default = default
        self.name = None


class Model(metaclass=ModelMeta):
    """基础模型类"""
    def __init__(self, **kwargs):
        for name, field in self._fields.items():
            value = kwargs.get(name, field.default)
            if field.required and value is None:
                raise ValueError(f"{name} 是必填字段")
            setattr(self, name, value)

    def to_dict(self):
        return {name: getattr(self, name) for name in self._fields}


# 使用
class User(Model):
    name = Field(str, required=True)
    age = Field(int, default=0)
    email = Field(str)

user = User(name="Alice", age=28)
print(user.to_dict())  # {'name': 'Alice', 'age': 28, 'email': None}
```

---

## 20. 并发与异步编程

### 20.1 多线程（threading）

适用场景：**I/O 密集型**任务（网络请求、文件读写）。

```python
import threading
import time
from concurrent.futures import ThreadPoolExecutor, as_completed

# --- 基本线程 ---
def download(url):
    print(f"开始下载: {url}")
    time.sleep(1)  # 模拟 I/O
    print(f"下载完成: {url}")
    return f"{url} 的内容"

# 创建线程
threads = []
for url in ["url1", "url2", "url3"]:
    t = threading.Thread(target=download, args=(url,))
    t.start()
    threads.append(t)

# 等待所有线程结束
for t in threads:
    t.join()

# --- 线程池（推荐） ---
urls = ["url1", "url2", "url3", "url4", "url5"]
with ThreadPoolExecutor(max_workers=3) as executor:
    # 方式1：批量提交
    futures = {executor.submit(download, url): url for url in urls}
    for future in as_completed(futures):
        url = futures[future]
        try:
            result = future.result(timeout=5)
            print(f"结果: {result}")
        except Exception as e:
            print(f"出错: {url} - {e}")

    # 方式2：map 模式
    results = executor.map(download, urls)
```

**⚠️ 线程安全：**

```python
# 共享数据需要锁保护
counter = 0
lock = threading.Lock()

def increment():
    global counter
    for _ in range(100_000):
        with lock:          # 上下文管理器自动获取/释放锁
            counter += 1

# 死锁预防：始终按相同顺序获取锁
lock_a = threading.Lock()
lock_b = threading.Lock()

# ✅ 安全方式
with lock_a:
    with lock_b:
        pass

# ❌ 危险（可能导致死锁）
# 线程1: lock_a → lock_b
# 线程2: lock_b → lock_a
```

### 20.2 多进程（multiprocessing）

适用场景：**CPU 密集型**任务（计算、数据处理）。

```python
from multiprocessing import Pool, Process, Queue
import os

# --- 进程池 ---
def cpu_intensive(n):
    """模拟 CPU 密集型任务"""
    return sum(i * i for i in range(n))

with Pool(processes=os.cpu_count()) as pool:
    # map：批量处理
    results = pool.map(cpu_intensive, [10**6, 10**7, 10**6])

    # apply_async：异步提交
    async_result = pool.apply_async(cpu_intensive, (10**7,))
    result = async_result.get(timeout=30)

# --- 进程间通信 ---
def producer(q):
    for i in range(5):
        q.put(f"消息 {i}")
    q.put(None)  # 终止信号

def consumer(q):
    while True:
        msg = q.get()
        if msg is None:
            break
        print(f"消费: {msg}")

q = Queue()
p1 = Process(target=producer, args=(q,))
p2 = Process(target=consumer, args=(q,))
p1.start(); p2.start()
p1.join(); p2.join()
```

### 20.3 异步编程（asyncio）

适用场景：**高并发 I/O** 任务。

```python
import asyncio

# --- 基本异步 ---
async def fetch_data(url):
    """异步获取数据"""
    print(f"开始获取: {url}")
    await asyncio.sleep(1)  # 模拟异步 I/O
    print(f"获取完成: {url}")
    return f"{url} 的数据"

async def main():
    # 顺序执行
    result1 = await fetch_data("url1")
    result2 = await fetch_data("url2")

    # 并发执行（推荐）
    results = await asyncio.gather(
        fetch_data("url1"),
        fetch_data("url2"),
        fetch_data("url3"),
    )
    # 或者：asyncio.as_completed
    for coro in asyncio.as_completed([
        fetch_data("url1"),
        fetch_data("url2"),
    ]):
        result = await coro

asyncio.run(main())

# --- 异步 HTTP 客户端 ---
import aiohttp  # pip install aiohttp

async def fetch_url(session, url):
    async with session.get(url) as response:
        return await response.text()

async def fetch_all(urls):
    async with aiohttp.ClientSession() as session:
        tasks = [fetch_url(session, url) for url in urls]
        return await asyncio.gather(*tasks, return_exceptions=True)

# --- 信号量与限流 ---
async def rate_limited_fetch(semaphore, url):
    async with semaphore:
        return await fetch_data(url)

async def bounded_concurrency(urls, max_concurrent=5):
    semaphore = asyncio.Semaphore(max_concurrent)
    tasks = [rate_limited_fetch(semaphore, url) for url in urls]
    return await asyncio.gather(*tasks)
```

---

## 21. 网络编程

### 21.1 Socket 编程

```python
import socket

# --- TCP 服务器 ---
def tcp_server(host="127.0.0.1", port=8888):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((host, port))
        s.listen(5)
        print(f"监听 {host}:{port}")

        while True:
            conn, addr = s.accept()
            with conn:
                data = conn.recv(1024)
                if data:
                    response = f"收到: {data.decode()}".encode()
                    conn.sendall(response)

# --- TCP 客户端 ---
def tcp_client(host="127.0.0.1", port=8888, message="Hello!"):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((host, port))
        s.sendall(message.encode())
        response = s.recv(1024)
        print(f"服务器回复: {response.decode()}")
```

### 21.2 HTTP 客户端（requests）

```python
import requests

# GET 请求
resp = requests.get("https://httpbin.org/get", params={"key": "value"})
print(resp.status_code)     # 200
print(resp.json())          # 解析 JSON 响应
print(resp.text)            # 原始文本

# POST 请求
resp = requests.post(
    "https://httpbin.org/post",
    json={"name": "Alice"},
    headers={"Authorization": "Bearer token123"},
    timeout=10,
)

# 文件上传
with open("file.txt", "rb") as f:
    files = {"file": ("filename.txt", f, "text/plain")}
    resp = requests.post("https://httpbin.org/post", files=files)

# Session：连接复用、Cookie 持久化
session = requests.Session()
session.headers.update({"User-Agent": "MyApp/1.0"})
resp1 = session.get("https://httpbin.org/cookies/set/name/value")
resp2 = session.get("https://httpbin.org/cookies")  # 自动携带 Cookie

# 高级：自定义适配器、重试策略
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

retry_strategy = Retry(
    total=3,
    backoff_factor=1,
    status_forcelist=[429, 500, 502, 503, 504],
)
adapter = HTTPAdapter(max_retries=retry_strategy)
session.mount("https://", adapter)
```

### 21.3 SSH 自动化

```python
# pip install paramiko
import paramiko

def execute_remote(host, username, password, command):
    """在远程服务器上执行命令"""
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    try:
        client.connect(host, username=username, password=password)
        stdin, stdout, stderr = client.exec_command(command)
        return stdout.read().decode(), stderr.read().decode()
    finally:
        client.close()

# 示例
# out, err = execute_remote("192.168.1.100", "admin", "pass", "uptime")
```

---

## 22. 数据库操作

### 22.1 SQLite 原生操作

```python
import sqlite3

# 连接（自动创建文件）
conn = sqlite3.connect("app.db")
conn.row_factory = sqlite3.Row  # 支持列名访问

# 创建表
conn.execute("""
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        age INTEGER,
        email TEXT UNIQUE
    )
""")

# 插入（使用参数化防止 SQL 注入）
conn.execute(
    "INSERT INTO users (name, age, email) VALUES (?, ?, ?)",
    ("Alice", 28, "alice@example.com")
)

# 批量插入
users_data = [("Bob", 35, "bob@example.com"), ("Charlie", 42, "charlie@example.com")]
conn.executemany("INSERT INTO users (name, age, email) VALUES (?, ?, ?)", users_data)

# 查询
cursor = conn.execute("SELECT * FROM users WHERE age > ?", (30,))
for row in cursor:
    print(f"{row['name']}: {row['age']} 岁")  # 支持列名访问

# 查询一条
row = conn.execute("SELECT * FROM users WHERE id = ?", (1,)).fetchone()

# 事务
try:
    conn.execute("INSERT INTO users (name) VALUES (?)", ("Dave",))
    conn.execute("INSERT INTO users (name) VALUES (?)", ("Eve",))
    conn.commit()
except Exception:
    conn.rollback()
    raise

conn.execute("PRAGMA journal_mode=WAL")  # 启用 WAL 模式提升并发性能
conn.close()
```

### 22.2 SQLAlchemy ORM

```python
from sqlalchemy import create_engine, Column, Integer, String, DateTime, func
from sqlalchemy.orm import DeclarativeBase, Session, relationship

# 引擎配置
engine = create_engine(
    "sqlite:///app.db",
    echo=False,  # 生产环境关闭 SQL 日志
    pool_size=5,
    max_overflow=10,
    pool_pre_ping=True,  # 连接健康检查
)

# 声明式基类
class Base(DeclarativeBase):
    pass

# 模型定义
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True)
    name = Column(String(50), nullable=False)
    email = Column(String(100), unique=True)
    created_at = Column(DateTime, server_default=func.now())

    orders = relationship("Order", back_populates="user")

class Order(Base):
    __tablename__ = "orders"

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    amount = Column(Integer, default=0)

    user = relationship("User", back_populates="orders")

# 创建表
Base.metadata.create_all(engine)

# 会话操作
with Session(engine) as session:
    # 新增
    user = User(name="Alice", email="alice@example.com")
    session.add(user)
    session.flush()  # 发送 SQL 但不提交

    # 查询
    users = session.query(User).filter(User.age > 18).all()
    user = session.query(User).filter_by(name="Alice").first()

    # 更新
    user.name = "Alice Updated"

    # 删除
    session.delete(user)

    session.commit()  # 提交事务

# 批量操作
with Session(engine) as session:
    session.execute(
        insert(User),
        [{"name": "User1"}, {"name": "User2"}],  # SQLAlchemy 2.0
    )
    session.commit()
```

### 22.3 Redis 操作

```python
import redis

pool = redis.ConnectionPool(
    host="localhost",
    port=6379,
    db=0,
    decode_responses=True,
    max_connections=10,
)
r = redis.Redis(connection_pool=pool)

# 字符串操作
r.set("key", "value", ex=3600)    # 带过期时间
r.setnx("lock", "1")             # 仅当不存在时设置
value = r.get("key")

# 列表
r.lpush("queue", "task1", "task2")
task = r.brpop("queue", timeout=5)

# 哈希
r.hset("user:1", mapping={"name": "Alice", "age": "28"})
all_data = r.hgetall("user:1")

# 发布订阅
pubsub = r.pubsub()
pubsub.subscribe("channel:updates")
for message in pubsub.listen():
    if message["type"] == "message":
        print(f"收到: {message['data']}")
```

---

## 23. Web 开发实战

### 23.1 FastAPI 入门

```python
# pip install fastapi uvicorn[standard]
from typing import Optional
from fastapi import FastAPI, HTTPException, Depends, Query
from pydantic import BaseModel, Field, EmailStr

app = FastAPI(title="用户管理系统", version="1.0.0")

# --- 数据模型 ---
class UserCreate(BaseModel):
    name: str = Field(..., min_length=2, max_length=50, description="用户名")
    email: EmailStr
    age: int = Field(..., ge=0, le=150)

class UserResponse(BaseModel):
    id: int
    name: str
    email: str

# 模拟数据库
users_db = {}
next_id = 1

# --- 路由 ---
@app.get("/")
async def root():
    return {"message": "欢迎使用用户管理系统"}

@app.post("/users", response_model=UserResponse, status_code=201)
async def create_user(user: UserCreate):
    """创建用户"""
    global next_id
    user_id = next_id
    next_id += 1
    users_db[user_id] = {**user.model_dump(), "id": user_id}
    return users_db[user_id]

@app.get("/users", response_model=list[UserResponse])
async def list_users(
    page: int = Query(1, ge=1),
    size: int = Query(10, ge=1, le=100),
):
    """分页查询用户"""
    start = (page - 1) * size
    return list(users_db.values())[start : start + size]

@app.get("/users/{user_id}", response_model=UserResponse)
async def get_user(user_id: int):
    """获取单个用户"""
    if user_id not in users_db:
        raise HTTPException(status_code=404, detail="用户不存在")
    return users_db[user_id]

# 启动: uvicorn main:app --reload
# 自动文档: http://localhost:8000/docs
```

### 23.2 中间件与依赖注入

```python
from fastapi import Request, Depends, HTTPException
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
import time
import jwt

# --- 自定义中间件 ---
@app.middleware("http")
async def add_process_time_header(request: Request, call_next):
    start = time.perf_counter()
    response = await call_next(request)
    elapsed = time.perf_counter() - start
    response.headers["X-Process-Time"] = str(elapsed)
    return response

# --- 依赖注入：认证 ---
security = HTTPBearer()
SECRET_KEY = "your-secret-key"

def verify_token(credentials: HTTPAuthorizationCredentials = Depends(security)):
    """验证 JWT Token"""
    try:
        payload = jwt.decode(credentials.credentials, SECRET_KEY, algorithms=["HS256"])
        return payload
    except jwt.InvalidTokenError:
        raise HTTPException(status_code=401, detail="无效的认证令牌")

@app.get("/protected")
async def protected_endpoint(user=Depends(verify_token)):
    """需要认证的接口"""
    return {"message": f"欢迎, {user['sub']}"}
```

---

## 24. 测试与质量保障

### 24.1 unittest 框架

```python
import unittest

def divide(a, b):
    if b == 0:
        raise ValueError("除数不能为零")
    return a / b

class TestDivide(unittest.TestCase):
    def setUp(self):
        """每个测试前执行"""
        self.a, self.b = 10, 2

    def tearDown(self):
        """每个测试后执行"""
        pass

    def test_normal_case(self):
        self.assertEqual(divide(10, 2), 5)
        self.assertAlmostEqual(divide(10, 3), 3.33333, places=4)

    def test_division_by_zero(self):
        with self.assertRaises(ValueError) as ctx:
            divide(10, 0)
        self.assertIn("除数不能为零", str(ctx.exception))

    def test_type_error(self):
        with self.assertRaises(TypeError):
            divide("10", 2)

if __name__ == "__main__":
    unittest.main()
```

### 24.2 pytest（推荐）

```python
# pip install pytest pytest-cov pytest-mock

import pytest

# --- 基本测试 ---
def add(a, b):
    return a + b

def test_add_positive():
    assert add(2, 3) == 5

def test_add_negative():
    assert add(-1, 1) == 0

# --- 参数化测试 ---
@pytest.mark.parametrize("a,b,expected", [
    (2, 3, 5),
    (0, 0, 0),
    (-1, -1, -2),
    (100, 200, 300),
])
def test_add_parametrized(a, b, expected):
    assert add(a, b) == expected

# --- 固件 (fixture) ---
@pytest.fixture
def sample_data():
    """准备测试数据"""
    return {"name": "Alice", "age": 28}

def test_with_fixture(sample_data):
    assert sample_data["name"] == "Alice"
    assert sample_data["age"] == 28

@pytest.fixture(scope="module")  # 模块级复用
def database():
    # setup
    db = {"connected": True}
    yield db
    # teardown
    db["connected"] = False

# --- Mock ---
def test_with_mock(mocker):
    mock_func = mocker.patch("module.external_api")
    mock_func.return_value = {"status": "ok"}
    # 测试逻辑...

# --- 异步测试 ---
@pytest.mark.asyncio
async def test_async_function():
    result = await some_async_func()
    assert result == "expected"
```

---

## 25. 性能优化

### 25.1 性能分析（Profiling）

```python
import cProfile
import pstats

# 命令行: python -m cProfile -s cumulative my_script.py

# 程序内分析
def profile_me():
    profiler = cProfile.Profile()
    profiler.enable()
    # 待分析的代码
    result = [x**2 for x in range(1_000_000)]
    profiler.disable()

    stats = pstats.Stats(profiler).sort_stats("cumulative")
    stats.print_stats(10)  # 打印前10条

# line_profiler（逐行分析，pip install line_profiler）
# @profile  # 在函数上添加装饰器
# 运行: kernprof -l -v script.py
```

### 25.2 性能优化技巧

```python
# 1. 使用 local variable 减少属性查找
def fast_loop(data):
    append = data.append  # 缓存方法引用
    for i in range(1000):
        append(i)

# 2. 生成器替代列表（节省内存）
# ❌ 耗内存
numbers = [i * 2 for i in range(10_000_000)]
# ✅ 省内存
numbers_gen = (i * 2 for i in range(10_000_000))

# 3. 集合替代列表做成员检查
# ❌ O(n) 成员检查
valid_items = [1, 2, 3, ...]
if item in valid_items: ...  # 慢
# ✅ O(1) 成员检查
valid_items = {1, 2, 3, ...}
if item in valid_items: ...  # 快

# 4. 字符串拼接：join 取代 +
# ❌ 每次创建新字符串，O(n²)
result = ""
for s in many_strings:
    result += s
# ✅ 一次分配，O(n)
result = "".join(many_strings)

# 5. __slots__ 减少内存占用
class Point:
    __slots__ = ("x", "y")  # 阻止 __dict__ 创建，节省内存
    def __init__(self, x, y):
        self.x = x
        self.y = y

# 6. 使用 dataclass（Python 3.7+）
from dataclasses import dataclass, field

@dataclass(slots=True)  # 3.10+ 支持 slots
class User:
    name: str
    age: int
    tags: list = field(default_factory=list)
```

### 25.3 Cython 与 C 扩展概述

```python
# Cython 示例（.pyx 文件）
# cython_example.pyx:
def cython_fibonacci(int n):
    cdef int i
    cdef long a = 0, b = 1
    for i in range(n):
        a, b = b, a + b
    return a

# 编译: python setup.py build_ext --inplace

# Numba JIT 编译（即插即用，推荐）
from numba import jit

@jit(nopython=True)
def numba_sum(arr):
    total = 0
    for x in arr:
        total += x
    return total
```

---

## 26. 安全编程

### 26.1 输入验证与清理

```python
import re
import html

# SQL 注入防护：始终使用参数化查询
# ❌ 危险：
# query = f"SELECT * FROM users WHERE name = '{user_input}'"
# ✅ 安全：
cursor.execute("SELECT * FROM users WHERE name = ?", (user_input,))

# XSS 防护：转义 HTML
from markupsafe import escape  # 或 html.escape
safe_output = escape(user_input)

# 命令注入防护：使用 subprocess 列表形式
import subprocess
# ❌ 危险：
# subprocess.run(f"ls {user_input}", shell=True)
# ✅ 安全：
subprocess.run(["ls", user_input], check=True)

# 路径遍历防护
from pathlib import Path
def safe_read(base_dir, filename):
    base = Path(base_dir).resolve()
    filepath = (base / filename).resolve()
    if not str(filepath).startswith(str(base)):
        raise ValueError("路径遍历攻击！")
    return filepath.read_text()
```

### 26.2 密码与加密

```python
import hashlib
import secrets
import bcrypt

# --- 密钥生成（使用 secrets 而非 random） ---
api_key = secrets.token_hex(32)       # 64字符十六进制
reset_token = secrets.token_urlsafe(32) # URL安全格式

# --- 密码哈希（始终使用 bcrypt 或 argon2） ---
# pip install bcrypt
password = "user_password".encode()

# 生成哈希
hashed = bcrypt.hashpw(password, bcrypt.gensalt(rounds=12))

# 验证
if bcrypt.checkpw(password, hashed):
    print("密码正确")

# --- HMAC 消息认证 ---
import hmac
def verify_signature(payload, signature, secret_key):
    expected = hmac.new(
        secret_key.encode(), payload.encode(), hashlib.sha256
    ).hexdigest()
    return hmac.compare_digest(expected, signature)  # 恒定时间比较

# --- 对称加密（Fernet） ---
from cryptography.fernet import Fernet
key = Fernet.generate_key()
cipher = Fernet(key)
encrypted = cipher.encrypt(b"敏感数据")
decrypted = cipher.decrypt(encrypted)
```

### 26.3 安全反序列化

```python
import json
import pickle
import yaml

# ❌ 绝对不要反序列化不可信来源的数据
# pickle.loads(untrusted_data)   # 可执行任意代码！
# yaml.load(untrusted_data)      # 旧版 PyYAML 可执行代码！

# ✅ 安全做法
# 使用 JSON（最安全）
data = json.loads(untrusted_string)  # 只支持基本数据类型

# 如需 YAML，使用 safe_load
data = yaml.safe_load(untrusted_yaml)

# 如确需 pickle，只用于完全可信的来源
# data = pickle.loads(internal_data)  # 仅限内部可信数据
```

---

## 27. 黑客级技巧

### 27.1 内省与反射

```python
# --- 运行时查看对象信息 ---
import inspect

def secret_function(x, y, *, mode="fast"):
    """这是一个秘密函数"""
    return x + y

# 查看源码
print(inspect.getsource(secret_function))

# 查看签名
sig = inspect.signature(secret_function)
print(sig.parameters)  # OrderedDict([('x', ...), ('y', ...), ('mode', ...)])

# 获取调用栈
def who_called_me():
    frame = inspect.currentframe()
    caller = inspect.getouterframes(frame, 2)
    return caller[1].function  # 返回调用者函数名

# --- 动态获取/修改属性 ---
obj = type("Dynamic", (), {"x": 1})()

getattr(obj, "x")          # 1
setattr(obj, "y", 2)       # 动态设置属性
hasattr(obj, "z")          # True/False
delattr(obj, "x")          # 删除属性

# vars() / dir() 查看对象属性字典
print(vars(obj))           # {'y': 2}
print(dir(obj))            # 所有属性和方法的列表
```

### 27.2 Monkey Patching（运行时修改）

```python
# 修改标准库行为
import json

# 保存原始函数
_original_dumps = json.dumps

def patched_dumps(obj, **kwargs):
    """为所有 JSON 输出添加时间戳"""
    if isinstance(obj, dict):
        obj = {"_timestamp": time.time(), **obj}
    return _original_dumps(obj, **kwargs)

# 应用补丁
json.dumps = patched_dumps

# 现在所有 json.dumps 调用都会带上时间戳
print(json.dumps({"user": "Alice"}))

# ⚠️ 谨慎使用！可能导致不可预测的行为
# 生产环境中建议使用依赖注入替代 Monkey Patching
```

### 27.3 动态代码执行

```python
# exec() — 执行任意 Python 代码（极度危险！仅用于受控环境）
namespace = {}
exec("""
def dynamic_function(x):
    return x ** 2
result = dynamic_function(5)
""", namespace)
print(namespace["result"])  # 25

# eval() — 计算单个表达式
result = eval("3 + 4 * 2")  # 11
# ⚠️ 绝不要 eval() 用户输入！

# compile() — 编译代码为字节码对象
code = compile("a + b", "<string>", "eval")
result = eval(code, {"a": 10, "b": 20})  # 30

# ⚠️  安全提示：这些函数可导致代码注入，仅在完全受控的环境中使用
```

### 27.4 字节码操作

```python
import dis

def mysterious_function(x):
    if x > 10:
        return x * 2
    return x + 1

# 反汇编：查看函数的字节码
dis.dis(mysterious_function)
# 输出示例：
#   2           0 LOAD_FAST                0 (x)
#               2 LOAD_CONST               1 (10)
#               4 COMPARE_OP               4 (>)
#               6 POP_JUMP_IF_FALSE       16
# ...

# 查看代码对象属性
code = mysterious_function.__code__
print(f"变量名: {code.co_varnames}")
print(f"常量: {code.co_consts}")
print(f"参数数量: {code.co_argcount}")
print(f"局部变量数: {code.co_nlocals}")
```

### 27.5 代码注入与 Hook

```python
import sys

# --- 模块导入钩子 ---
class ImportInterceptor:
    """拦截所有模块导入，记录日志"""
    def __init__(self):
        self._original_import = __builtins__.__import__
        self.imports = []

    def start(self):
        def patched_import(name, *args, **kwargs):
            self.imports.append(name)
            return self._original_import(name, *args, **kwargs)
        __builtins__.__import__ = patched_import

    def stop(self):
        __builtins__.__import__ = self._original_import

    def report(self):
        return self.imports

# 使用
interceptor = ImportInterceptor()
interceptor.start()
import json
import re
import socket
interceptor.stop()
print(interceptor.report())  # ['json', 're', 'socket']

# --- Hook 函数调用 ---
class HookManager:
    """管理函数钩子"""
    def __init__(self):
        self._hooks = {}

    def add_hook(self, func, hook):
        """为函数添加前置钩子"""
        if func not in self._hooks:
            self._hooks[func] = []
        self._hooks[func].append(hook)

    def patch(self, target, owner=None):
        original = getattr(owner, target) if owner else globals()[target]

        def wrapper(*args, **kwargs):
            for hook in self._hooks.get(original, []):
                hook(*args, **kwargs)
            return original(*args, **kwargs)

        if owner:
            setattr(owner, target, wrapper)
        else:
            globals()[target] = wrapper
```

### 27.6 网络嗅探与协议分析

```python
# pip install scapy
from scapy.all import sniff, IP, TCP

def packet_callback(packet):
    """处理捕获的数据包"""
    if IP in packet:
        src = packet[IP].src
        dst = packet[IP].dst
        proto = packet[IP].proto
        print(f"{src} → {dst} (协议: {proto})")

        if TCP in packet:
            sport = packet[TCP].sport
            dport = packet[TCP].dport
            flags = packet[TCP].flags
            print(f"  TCP {sport} → {dport} flags={flags}")

# 捕获50个数据包（需要管理员/root权限）
# sniff(prn=packet_callback, count=50)
```

### 27.7 内存操作与 CTF 技巧

```python
import struct
import sys

# --- 字节级操作 ---
# 整数与字节互转
num = 0xDEADBEEF
packed = struct.pack("<I", num)     # 小端序打包: b'\xef\xbe\xad\xde'
unpacked = struct.unpack("<I", packed)[0]  # 0xDEADBEEF

# 任意进制转换
int("DEADBEEF", 16)     # 十六进制 → 十进制
bin(42)                 # 十进制 → 二进制字符串 '0b101010'
hex(255)                # 十进制 → 十六进制字符串 '0xff'
oct(8)                  # 十进制 → 八进制字符串 '0o10'

# --- 绕过 Python 沙箱（仅供研究） ---
def sandbox_escape_demo():
    """展示常见的 Python 沙箱绕过技术"""
    # 1. 访问子类获取危险方法
    dangerous = ().__class__.__bases__[0].__subclasses__()

    # 2. 通过 __builtins__ 恢复被删除的 builtin
    frame = sys._getframe()

    # 3. 字符串拼接绕过黑名单
    func_name = "".join([chr(101), chr(118), chr(97), chr(108)])  # "eval"

    # ⚠️  这些技术仅用于安全研究和 CTF 比赛
    pass

# --- CTF 常用技巧 ---
# ord/chr 转换
chars = [ord(c) for c in "flag"]    # [102, 108, 97, 103]
string = "".join(chr(c) for c in [102, 108, 97, 103])  # "flag"

# XOR 编码
def xor_encrypt(data, key):
    return bytes([b ^ key for b in data])

encrypted = xor_encrypt(b"secret", 0x55)
decrypted = xor_encrypt(encrypted, 0x55)  # XOR 自逆

# Base64 变种处理
import base64
encoded = base64.b64encode(b"data")
base64.b85encode(b"data")  # Ascii85
```

---

## 28. 企业级部署与运维

### 28.1 项目结构

```
myproject/
├── src/
│   └── myapp/
│       ├── __init__.py
│       ├── main.py          # 应用入口
│       ├── api/             # API 路由
│       ├── models/          # 数据模型
│       ├── services/        # 业务逻辑
│       ├── utils/           # 工具函数
│       └── config.py        # 配置管理
├── tests/
│   ├── unit/
│   ├── integration/
│   └── conftest.py
├── migrations/              # 数据库迁移
├── scripts/                 # 运维脚本
├── Dockerfile
├── docker-compose.yml
├── pyproject.toml           # 项目配置（依赖、构建）
├── .env.example
├── .pre-commit-config.yaml
└── README.md
```

### 28.2 日志系统

```python
import logging
import logging.handlers
import json
from datetime import datetime

# --- 结构化 JSON 日志 ---
class JSONFormatter(logging.Formatter):
    def format(self, record):
        log_entry = {
            "timestamp": datetime.utcnow().isoformat(),
            "level": record.levelname,
            "logger": record.name,
            "message": record.getMessage(),
            "module": record.module,
            "function": record.funcName,
            "line": record.lineno,
        }
        if record.exc_info and record.exc_info[1]:
            log_entry["exception"] = str(record.exc_info[1])
        return json.dumps(log_entry)

# --- 配置 ---
def setup_logging(app_name="myapp", log_level=logging.INFO):
    logger = logging.getLogger(app_name)
    logger.setLevel(log_level)

    # 控制台输出（开发环境）
    console = logging.StreamHandler()
    console.setFormatter(logging.Formatter(
        "%(asctime)s [%(levelname)s] %(name)s: %(message)s"
    ))

    # 文件输出（JSON 格式，生产环境）
    file_handler = logging.handlers.RotatingFileHandler(
        f"{app_name}.json",
        maxBytes=10 * 1024 * 1024,  # 10MB
        backupCount=5,
    )
    file_handler.setFormatter(JSONFormatter())

    logger.addHandler(console)
    logger.addHandler(file_handler)
    return logger

logger = setup_logging()
logger.info("服务启动", extra={"port": 8080})
```

### 28.3 配置管理

```python
# pyproject.toml 或 .env 管理配置
import os
from dataclasses import dataclass
from functools import lru_cache

@dataclass
class Settings:
    """应用配置"""
    # 数据库
    database_url: str = "sqlite:///app.db"

    # Redis
    redis_url: str = "redis://localhost:6379/0"

    # 安全
    secret_key: str = "change-me-in-production"
    jwt_expire_minutes: int = 60

    # 日志
    log_level: str = "INFO"

    # CORS
    allowed_origins: list[str] = field(default_factory=lambda: ["*"])

    @classmethod
    def from_env(cls) -> "Settings":
        """从环境变量加载配置"""
        import json
        return cls(
            database_url=os.getenv("DATABASE_URL", "sqlite:///app.db"),
            redis_url=os.getenv("REDIS_URL", "redis://localhost:6379/0"),
            secret_key=os.getenv("SECRET_KEY", "change-me"),
            jwt_expire_minutes=int(os.getenv("JWT_EXPIRE_MINUTES", "60")),
            log_level=os.getenv("LOG_LEVEL", "INFO"),
            allowed_origins=json.loads(
                os.getenv("ALLOWED_ORIGINS", '["*"]')
            ),
        )

@lru_cache()
def get_settings() -> Settings:
    """单例获取配置（进程内缓存）"""
    return Settings.from_env()
```

### 28.4 Docker 部署

```dockerfile
# Dockerfile（多阶段构建）
FROM python:3.12-slim AS builder

# 安装依赖
COPY requirements.txt .
RUN pip install --user -r requirements.txt

# 运行阶段
FROM python:3.12-slim

# 创建非 root 用户
RUN useradd --create-home --shell /bin/bash app

# 复制构建产物
COPY --from=builder /root/.local /home/app/.local

# 复制应用代码
COPY --chown=app:app src/ /home/app/src/
WORKDIR /home/app/src

USER app
ENV PATH="/home/app/.local/bin:${PATH}"
ENV PYTHONUNBUFFERED=1

EXPOSE 8080
CMD ["uvicorn", "myapp.main:app", "--host", "0.0.0.0", "--port", "8080"]
```

```yaml
# docker-compose.yml
version: "3.8"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/mydb
      - REDIS_URL=redis://redis:6379/0
    depends_on:
      db:
        condition: service_healthy
      redis:
        condition: service_healthy
    restart: unless-stopped

  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
      POSTGRES_DB: mydb
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U user -d mydb"]
      interval: 5s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:
```

### 28.5 CI/CD（GitHub Actions 示例）

```yaml
# .github/workflows/ci.yml
name: CI

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        python-version: ["3.11", "3.12"]

    steps:
      - uses: actions/checkout@v4

      - name: Set up Python
        uses: actions/setup-python@v5
        with:
          python-version: ${{ matrix.python-version }}

      - name: Install dependencies
        run: |
          pip install -e ".[dev]"

      - name: Lint
        run: |
          ruff check .
          mypy src/

      - name: Test with coverage
        run: |
          pytest --cov=src --cov-report=xml

      - name: Upload coverage
        uses: codecov/codecov-action@v4
```

### 28.6 性能监控（Prometheus + Grafana）

```python
# pip install prometheus-client
from prometheus_client import Counter, Histogram, Gauge, generate_latest
from fastapi import FastAPI, Response

app = FastAPI()

# 定义指标
REQUEST_COUNT = Counter(
    "http_requests_total",
    "Total HTTP requests",
    ["method", "endpoint", "status"],
)
REQUEST_LATENCY = Histogram(
    "http_request_duration_seconds",
    "HTTP request latency",
    ["method", "endpoint"],
)
ACTIVE_CONNECTIONS = Gauge(
    "active_connections",
    "Active connections count",
)

# 暴露指标端点
@app.get("/metrics")
async def metrics():
    return Response(
        content=generate_latest(),
        media_type="text/plain",
    )

# 使用中间件记录指标
@app.middleware("http")
async def metrics_middleware(request: Request, call_next):
    method = request.method
    endpoint = request.url.path

    ACTIVE_CONNECTIONS.inc()
    with REQUEST_LATENCY.labels(method, endpoint).time():
        response = await call_next(request)
    ACTIVE_CONNECTIONS.dec()

    REQUEST_COUNT.labels(method, endpoint, response.status_code).inc()
    return response
```

### 28.7 健康检查与优雅关闭

```python
import asyncio
import signal

class Application:
    def __init__(self):
        self._running = False

    async def health_check(self):
        """健康检查"""
        return {
            "status": "healthy" if self._running else "degraded",
            "version": "1.0.0",
        }

    async def readiness_check(self):
        """就绪检查"""
        # 检查数据库连接
        # 检查 Redis 连接
        return {"status": "ready"}

    async def shutdown(self):
        """优雅关闭"""
        self._running = False
        # 停止接受新请求
        # 等待正在处理的请求完成
        # 关闭数据库连接池
        print("关闭中...")

    def setup_signal_handlers(self):
        """信号处理"""
        loop = asyncio.get_event_loop()
        for sig in (signal.SIGTERM, signal.SIGINT):
            loop.add_signal_handler(
                sig,
                lambda: asyncio.create_task(self.shutdown()),
            )
```

---

## 附录：学习路线建议

### 新手路径（0 → 入门）
1. 环境搭建 → 基础语法 → 数据类型
2. 控制流 → 函数 → 文件操作
3. 面向对象编程 → 标准库使用
4. 用 Flask/FastAPI 写第一个 Web 应用

### 进阶路径（入门 → 熟练）
1. 装饰器 → 生成器 → 上下文管理器
2. 并发编程 → 异步编程
3. 数据库 ORM → Web 框架深入
4. 测试 → 性能优化 → Docker 部署

### 专家路径（熟练 → 精通）
1. 元类 → 描述符 → C 扩展
2. 分布式系统 → 消息队列 → 微服务
3. 源码阅读（CPython 内部原理）
4. 安全审计 → CTF 实战

---

> **持续更新中** | 最后更新：2026-05-18
