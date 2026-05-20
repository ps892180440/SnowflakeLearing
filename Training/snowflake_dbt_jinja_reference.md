# Snowflake × dbt：Jinja 过滤器 / 内置函数 / 引用包 完整参考手册

## 目录

- [1. Jinja 过滤器](#1-jinja-过滤器)
  - [1.1 标准 Jinja 内置过滤器](#11-标准-jinja-内置过滤器)
    - [abs](#abs) · [attr](#attr) · [batch](#batch) · [capitalize](#capitalize) · [center](#center) · [default / d](#default--d) · [dictsort](#dictsort) · [escape / e](#escape--e) · [filesizeformat](#filesizeformat) · [first](#first) · [float](#float) · [forceescape](#forceescape) · [format](#format) · [groupby](#groupby) · [indent](#indent) · [int](#int) · [items](#items) · [join](#join) · [last](#last) · [length / count](#length--count) · [list](#list) · [lower](#lower) · [map](#map) · [max](#max) · [min](#min) · [pprint](#pprint) · [random](#random) · [reject](#reject) · [rejectattr](#rejectattr) · [replace](#replace) · [reverse](#reverse) · [round](#round) · [safe](#safe) · [select](#select) · [selectattr](#selectattr) · [slice](#slice) · [sort](#sort) · [string](#string) · [striptags](#striptags) · [sum](#sum) · [title](#title) · [tojson-jinja](#tojson-jinja) · [trim](#trim) · [truncate](#truncate) · [unique](#unique) · [upper](#upper) · [urlencode](#urlencode) · [urlize](#urlize) · [wordcount](#wordcount) · [wordwrap](#wordwrap) · [xmlattr](#xmlattr)
  - [1.2 dbt 专用过滤器](#12-dbt-专用过滤器)
    - [as_bool](#as_bool) · [as_native](#as_native) · [as_number](#as_number)
  - [1.3 Jinja 内置测试 (is tests)](#13-jinja-内置测试-is-tests)
    - [boolean](#boolean) · [callable](#callable) · [defined](#defined) · [divisibleby](#divisibleby) · [eq / equalto](#eq--equalto) · [escaped](#escaped) · [even](#even) · [false](#false) · [filter](#filter) · [float-test](#float-test) · [ge](#ge) · [gt / greaterthan](#gt--greaterthan) · [in](#in) · [integer](#integer) · [iterable](#iterable) · [le](#le) · [lower-test](#lower-test) · [lt / lessthan](#lt--lessthan) · [mapping](#mapping) · [ne](#ne) · [none](#none) · [number](#number) · [odd](#odd) · [sameas](#sameas) · [sequence](#sequence) · [string-test](#string-test) · [true](#true) · [undefined](#undefined) · [upper-test](#upper-test) · [test](#test)

- [2. dbt 内置 Jinja 函数](#2-dbt-内置-jinja-函数)
  - [2.1 模型与数据源引用](#21-模型与数据源引用)
    - [ref()](#ref) · [source()](#source) · [this](#this) · [model](#model) · [schema](#schema) · [schemas](#schemas)
  - [2.2 配置与环境](#22-配置与环境)
    - [config()](#config) · [var()](#var) · [env_var()](#env_var) · [target](#target) · [flags](#flags)
  - [2.3 序列化 / 反序列化](#23-序列化--反序列化)
    - [fromjson()](#fromjson) · [fromyaml()](#fromyaml) · [tojson()](#tojson-dbt) · [toyaml()](#toyaml)
  - [2.4 执行与运行上下文](#24-执行与运行上下文)
    - [execute](#execute) · [run_started_at](#run_started_at) · [invocation_id](#invocation_id) · [thread_id](#thread_id) · [selected_resources](#selected_resources) · [graph](#graph) · [on-run-end](#on-run-end)
  - [2.5 数据库操作](#25-数据库操作)
    - [run_query()](#run_query) · [adapter](#adapter) · [statement blocks](#statement-blocks)
  - [2.6 项目与模块](#26-项目与模块)
    - [dbt_version](#dbt_version) · [project_name](#project_name) · [modules](#modules) · [builtins](#builtins)
  - [2.7 工具与调试](#27-工具与调试)
    - [log()](#log) · [print()](#print-dbt) · [debug()](#debug) · [return()](#return) · [exceptions](#exceptions) · [local_md5()](#local_md5) · [set()](#set) · [zip()](#zip) · [doc()](#doc)
  - [2.8 宏与分发](#28-宏与分发)
    - [dispatch()](#dispatch)
  - [2.9 API 类 (Relation / Column / Result)](#29-api-类-relation--column--result)
    - [Relation](#relation) · [Column](#column) · [BigQueryColumn](#bigquerycolumn) · [Result](#result)

- [3. dbt 引用包](#3-dbt-引用包)
  - [3.1 安装语法](#31-安装语法)
  - [3.2 dbt Labs 官方包](#32-dbt-labs-官方包)
  - [3.3 Snowflake 专用包](#33-snowflake-专用包)
  - [3.4 测试与质量包](#34-测试与质量包)
  - [3.5 数据转换与工具包](#35-数据转换与工具包)
  - [3.6 Fivetran 数据源包](#36-fivetran-数据源包)
  - [3.7 机器学习与高级分析包](#37-机器学习与高级分析包)
  - [3.8 监控与元数据包](#38-监控与元数据包)
  - [3.9 其他社区包](#39-其他社区包)

---

## 1. Jinja 过滤器

> Jinja 过滤器使用管道语法 `|` 对变量进行转换处理，可链式调用。

### 1.1 标准 Jinja 内置过滤器

#### abs

返回参数的绝对值。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | number | 输入值 |

```jinja
{{ -3 | abs }}          {# 输出: 3 #}
{{ my_number | abs }}
```

#### attr

获取对象的属性，类似 `.` 操作但更安全（属性不存在时返回 undefined 而非回退到 `__getitem__`）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `obj` | Any | 对象 |
| `name` | str | 属性名 |

```jinja
{{ foo | attr("bar") }}       {# 等价于 foo.bar，但不存在时返回 undefined #}
{{ user | attr("email") }}
```

#### batch

将可迭代对象分批，返回嵌套列表。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 输入序列 |
| `linecount` | int | — | 每批元素数 |
| `fill_with` | Any | None | 不足时填充值 |

```jinja
{% for row in items | batch(3, '&nbsp;') %}
  <tr>
  {% for column in row %}
    <td>{{ column }}</td>
  {% endfor %}
  </tr>
{% endfor %}
```

#### capitalize

返回首字母大写、其余字母小写的字符串。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | str | 输入字符串 |

```jinja
{{ "hello world" | capitalize }}    {# 输出: Hello world #}
```

#### center

将值在指定宽度的字段中居中。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | str | — | 输入字符串 |
| `width` | int | 80 | 字段宽度 |

```jinja
{{ "dbt" | center(20) }}    {# 输出: '        dbt         ' #}
```

#### default / d

变量未定义或为假值时返回默认值。别名：`d`。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | Any | — | 输入值 |
| `default_value` | Any | `''` | 回退值 |
| `boolean` | bool | False | True 时把 falsy 值也视为未定义 |

```jinja
{{ my_variable | default('my_variable is not defined') }}
{{ '' | default('the string was empty', true) }}
{{ my_var | d("fallback") }}
```

#### dictsort

对字典按键或值排序，返回 (key, value) 元组列表。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | Mapping | — | 字典 |
| `case_sensitive` | bool | False | 区分大小写 |
| `by` | str | `'key'` | 按 `'key'` 或 `'value'` 排序 |
| `reverse` | bool | False | 逆序 |

```jinja
{% for key, value in mydict | dictsort %}
  {{ key }}: {{ value }}
{% endfor %}

{% for key, value in mydict | dictsort(reverse=true) %}
{% for key, value in mydict | dictsort(false, 'value') %}
```

#### escape / e

将字符串中的 `&` `<` `>` `'` `"` 替换为 HTML 安全序列。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | Any | 输入值 |

```jinja
{{ "<div>text</div>" | escape }}
{# 输出: &lt;div&gt;text&lt;/div&gt; #}
{{ user_input | e }}
```

#### filesizeformat

将数值格式化为人类可读的文件大小。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | str/float/int | — | 大小值 |
| `binary` | bool | False | True 时使用二进前缀 (Mebi, Gibi) |

```jinja
{{ 1024 | filesizeformat }}           {# 输出: 1.0 kB #}
{{ 1048576 | filesizeformat(true) }}  {# 输出: 1.0 MiB #}
```

#### first

返回序列的第一个元素。

| 参数 | 类型 | 说明 |
|------|------|------|
| `seq` | iterable | 序列 |

```jinja
{{ [1, 2, 3] | first }}    {# 输出: 1 #}
{{ users | first }}
```

#### float

将值转换为浮点数，转换失败返回默认值。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | Any | — | 输入值 |
| `default` | float | 0.0 | 失败时默认值 |

```jinja
{{ "3.14" | float }}        {# 输出: 3.14 #}
{{ "abc" | float(0.0) }}    {# 输出: 0.0 #}
```

#### forceescape

强制 HTML 转义（可能造成双重转义）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 输入字符串 |

```jinja
{{ already_escaped | forceescape }}
```

#### format

使用 printf 风格格式化字符串。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 格式字符串 |
| `*args` | Any | 位置参数 |
| `**kwargs` | Any | 关键字参数 |

```jinja
{{ "%s, %s!" | format(greeting, name) }}       {# 输出: Hello, World! #}
{{ "%(name)s is %(age)d" | format(name="John", age=30) }}
```

#### groupby

按指定属性对对象列表分组。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 对象列表 |
| `attribute` | str/int | — | 分组属性（支持点号访问） |
| `default` | Any | None | 属性缺失时的默认值 |
| `case_sensitive` | bool | False | 区分大小写 |

```jinja
{% for city, items in users | groupby("city") %}
  <li>{{ city }}
    <ul>{% for user in items %}
      <li>{{ user.name }}</li>
    {% endfor %}</ul>
  </li>
{% endfor %}
```

#### indent

将字符串每行缩进指定宽度。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `s` | str | — | 字符串 |
| `width` | int\|str | 4 | 缩进宽度（空格数或字符串） |
| `first` | bool | False | 首行也缩进 |
| `blank` | bool | False | 空行也缩进 |

```jinja
{{ "line1\nline2" | indent(4) }}
{# 输出:
    line1
    line2
#}
{{ "header\nbody" | indent(4, first=true) }}
```

#### int

将值转换为整数，支持 0b/0o/0x 前缀。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | Any | — | 输入值 |
| `default` | int | 0 | 失败时默认值 |
| `base` | int | 10 | 进制 |

```jinja
{{ "42" | int }}          {# 输出: 42 #}
{{ "0xff" | int(0, 16) }} {# 输出: 255 #}
```

#### items

返回字典的 (key, value) 迭代器（Jinja 3.1+）。未定义时返回空迭代器。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Mapping | 字典 |

```jinja
{% for key, value in my_dict | items %}
    <dt>{{ key }}
    <dd>{{ value }}
{% endfor %}
```

#### join

用分隔符连接可迭代对象的元素。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 可迭代对象 |
| `d` | str | `''` | 分隔符 |
| `attribute` | str/int | None | 连接指定属性 |

```jinja
{{ [1, 2, 3] | join('|') }}            {# 输出: 1|2|3 #}
{{ [1, 2, 3] | join }}                  {# 输出: 123 #}
{{ users | join(', ', attribute='username') }}
```

#### last

返回序列的最后一个元素（不支持生成器）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `seq` | iterable | 可逆序列 |

```jinja
{{ [1, 2, 3] | last }}    {# 输出: 3 #}
{{ data | selectattr('name', '==', 'Jinja') | list | last }}
```

#### length / count

返回容器中元素的数量。别名：`count`。

| 参数 | 类型 | 说明 |
|------|------|------|
| `obj` | container | 容器对象 |

```jinja
{{ [1, 2, 3] | length }}     {# 输出: 3 #}
{{ "hello" | length }}        {# 输出: 5 #}
{{ users | count }}
```

#### list

将值转换为列表（字符串变为字符列表）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |

```jinja
{{ range(3) | list }}         {# 输出: [0, 1, 2] #}
{{ "abc" | list }}            {# 输出: ['a', 'b', 'c'] #}
```

#### lower

转换为小写。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | str | 输入字符串 |

```jinja
{{ "HELLO" | lower }}    {# 输出: hello #}
{{ column_name | lower }}
```

#### map

对可迭代对象的每个元素应用属性查找或过滤器。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |
| `attribute` | kw | 获取此属性 |
| `default` | kw | 属性缺失时的回退 |

```jinja
{{ users | map(attribute='username') | join(', ') }}
{{ users | map(attribute="username", default="Anonymous") | join(", ") }}
{{ titles | map('lower') | join(', ') }}
```

#### max

返回可迭代对象中的最大值。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 可迭代对象 |
| `case_sensitive` | bool | False | 区分大小写 |
| `attribute` | str/int | None | 比较此属性 |

```jinja
{{ [1, 2, 3] | max }}                        {# 输出: 3 #}
{{ users | max(attribute='age') }}
```

#### min

返回可迭代对象中的最小值。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 同上 |
| `case_sensitive` | bool | False | 同上 |
| `attribute` | str/int | None | 同上 |

```jinja
{{ [1, 2, 3] | min }}    {# 输出: 1 #}
```

#### pprint

美化打印变量，常用于调试。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | 任意值 |

```jinja
{{ complex_object | pprint }}
```

#### random

从序列中随机返回一个元素。

| 参数 | 类型 | 说明 |
|------|------|------|
| `seq` | sequence | 序列 |

```jinja
{{ [1, 2, 3, 4, 5] | random }}
{{ ['dev', 'staging', 'prod'] | random }}
```

#### reject

过滤掉通过测试的元素（与 select 相反）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |
| `test` | str | 测试名，省略则按布尔值评估 |

```jinja
{{ numbers | reject("odd") }}        {# 过滤掉奇数 #}
{{ numbers | reject("divisibleby", 3) }}
```

#### rejectattr

过滤掉指定属性通过测试的对象。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |
| `attribute` | str | 属性名 |
| `test` | str | 测试名，省略则按属性布尔值 |

```jinja
{{ users | rejectattr("is_active") }}
{{ users | rejectattr("email", "none") }}
```

#### replace

字符串替换。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `s` | str | — | 输入字符串 |
| `old` | str | — | 被替换子串 |
| `new` | str | — | 替换子串 |
| `count` | int | None | 最大替换次数 |

```jinja
{{ "Hello World" | replace("Hello", "Goodbye") }}   {# 输出: Goodbye World #}
{{ "aaaaargh" | replace("a", "d'oh, ", 2) }}        {# 输出: d'oh, d'oh, aaargh #}
```

#### reverse

反转对象或返回反向迭代器。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str\|Iterable | 输入 |

```jinja
{{ "hello" | reverse }}         {# 输出: olleh #}
{{ [1, 2, 3] | reverse | list }} {# 输出: [3, 2, 1] #}
```

#### round

四舍五入到指定精度。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | float | — | 数值 |
| `precision` | int | 0 | 精度 |
| `method` | str | `'common'` | `'common'` / `'ceil'` / `'floor'` |

```jinja
{{ 42.55 | round }}              {# 输出: 43.0 #}
{{ 42.55 | round(1, 'floor') }}  {# 输出: 42.5 #}
{{ 42.55 | round | int }}        {# 输出: 43 #}
```

#### safe

标记值为安全，跳过自动转义。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 输入字符串 |

```jinja
{{ "<b>bold</b>" | safe }}    {# 渲染为 <b>bold</b>，不转义 #}
```

#### select

筛选通过测试的元素。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |
| `test` | str | 测试名，省略则按布尔值 |

```jinja
{{ numbers | select("odd") }}
{{ numbers | select("divisibleby", 3) }}
{{ numbers | select("lessthan", 42) }}
{{ strings | select("equalto", "mystring") }}
```

#### selectattr

筛选指定属性通过测试的对象。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | iterable | 可迭代对象 |
| `attribute` | str | 属性名 |
| `test` | str | 测试名，省略则按属性布尔值 |

```jinja
{{ users | selectattr("is_active") }}
{{ users | selectattr("email", "none") }}
{{ orders | selectattr("status", "in", ["completed", "shipped"]) }}
```

#### slice

将集合等分为指定数量的切片。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | collection | — | 输入集合 |
| `slices` | int | — | 切片数 |
| `fill_with` | Any | None | 填充值 |

```jinja
{% for column in items | slice(3) %}
    <ul class="column-{{ loop.index }}">
    {% for item in column %}
      <li>{{ item }}</li>
    {% endfor %}
    </ul>
{% endfor %}
```

#### sort

对可迭代对象排序。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 可迭代对象 |
| `reverse` | bool | False | 逆序 |
| `case_sensitive` | bool | False | 区分大小写 |
| `attribute` | str/int | None | 按此属性排序（支持逗号多列） |

```jinja
{% for user in users | sort(attribute="name") %}
  {{ user.name }}
{% endfor %}
{% for user in users | sort(attribute="age,name") %}
{# 多列排序：先 age，再 name #}
```

#### string

将对象转换为字符串。保留 Markup 标记。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | Any | 输入值 |

```jinja
{{ 42 | string }}              {# 输出: '42' #}
{{ my_object | string }}
```

#### striptags

去除 SGML/XML/HTML 标签。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 输入字符串 |

```jinja
{{ "<p>Hello <b>World</b></p>" | striptags }}  {# 输出: Hello World #}
```

#### sum

对可迭代对象的元素求和。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 数字列表 |
| `attribute` | str/int | None | 对此属性求和 |
| `start` | int | 0 | 起始值 |

```jinja
{{ [1, 2, 3] | sum }}                      {# 输出: 6 #}
{{ items | sum(attribute='price') }}        {# 对每个 item.price 求和 #}
```

#### title

将每个单词首字母大写。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | str | 输入字符串 |

```jinja
{{ "hello world" | title }}    {# 输出: Hello World #}
```

#### tojson (Jinja)

将对象序列化为 JSON 字符串，标记为 HTML 安全。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | Any | — | 要序列化的对象 |
| `indent` | int | None | 缩进空格数 |

```jinja
{{ {"key": "value", "num": 42} | tojson }}         {# 输出: {"key": "value", "num": 42} #}
{{ my_data | tojson(indent=2) }}
```

#### trim

去除首尾指定字符（默认空白字符）。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | str | — | 输入字符串 |
| `chars` | str | None | 要去除的字符集 |

```jinja
{{ "  hello  " | trim }}           {# 输出: hello #}
{{ "//path//" | trim("/") }}       {# 输出: path #}
```

#### truncate

截断字符串到指定长度。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `s` | str | — | 输入字符串 |
| `length` | int | 255 | 保留长度 |
| `killwords` | bool | False | True 时精确按长度截断 |
| `end` | str | `'...'` | 省略号字符串 |
| `leeway` | int | 5 | 容差范围 |

```jinja
{{ "foo bar baz qux" | truncate(9) }}                {# 输出: foo... #}
{{ "foo bar baz qux" | truncate(9, True) }}           {# 输出: foo ba... #}
{{ "foo bar baz qux" | truncate(11, False, '...', 0) }} {# 输出: foo bar... #}
```

#### unique

返回去重后的列表。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | iterable | — | 可迭代对象 |
| `case_sensitive` | bool | False | 区分大小写 |
| `attribute` | str/int | None | 按此属性去重 |

```jinja
{{ ['foo', 'bar', 'foobar', 'FooBar'] | unique | list }}  {# ['foo', 'bar', 'foobar'] #}
{{ users | unique(attribute='email') | list }}
```

#### upper

转换为大写。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | str | 输入字符串 |

```jinja
{{ "hello" | upper }}    {# 输出: HELLO #}
```

#### urlencode

对字符串或字典进行 URL 编码（UTF-8）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str\|dict\|iterable | URL 参数字符串、字典或 (key,value) 对 |

```jinja
{{ "hello world" | urlencode }}                  {# 输出: hello%20world #}
{{ {"q": "dbt jinja", "page": 1} | urlencode }}   {# 输出: q=dbt+jinja&page=1 #}
```

#### urlize

将文本中的 URL 转为可点击链接。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `value` | str | — | 文本 |
| `trim_url_limit` | int | None | 显示 URL 截断长度 |
| `nofollow` | bool | False | 添加 `rel=nofollow` |
| `target` | str | None | 添加 `target` 属性 |
| `rel` | str | None | 添加 `rel` 属性 |
| `extra_schemes` | iterable | None | 额外识别的 URL scheme |

```jinja
{{ "Visit https://docs.getdbt.com" | urlize }}
{{ "Contact admin@example.com" | urlize }}
```

#### wordcount

统计字符串中的单词数。

| 参数 | 类型 | 说明 |
|------|------|------|
| `s` | str | 输入字符串 |

```jinja
{{ "hello world from dbt" | wordcount }}    {# 输出: 4 #}
```

#### wordwrap

将字符串按指定宽度换行。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `s` | str | — | 输入字符串 |
| `width` | int | 79 | 换行宽度 |
| `break_long_words` | bool | True | 长单词也断行 |
| `break_on_hyphens` | bool | True | 连字符处可断行 |
| `wrapstring` | str | None | 换行符 |

```jinja
{{ long_text | wordwrap(40) }}
```

#### xmlattr

从字典创建 SGML/XML 属性字符串。值为 none/undefined 的属性自动省略。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `d` | Mapping | — | 属性名→值的映射 |
| `autospace` | bool | True | 输出前自动加空格 |

```jinja
<ul{{ {'class': 'my_list', 'missing': none,
        'id': 'list-' ~ variable } | xmlattr }}>
{# 输出: <ul class="my_list" id="list-42"> #}
```

---

### 1.2 dbt 专用过滤器

#### as_bool

将 Jinja 输出强制转换为布尔值。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | 输入值 |

```sql
{{ "true" | as_bool }}       -- True
{{ 1 | as_bool }}            -- True
{{ 0 | as_bool }}            -- False
{{ "false" | as_bool }}      -- False

-- 典型用法
{% set is_prod = (target.name == 'prod') | as_bool %}
```

#### as_native

将 Jinja 编译输出还原为 Python 原生类型。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | 字符串化的 Python 对象 |

```sql
{% set my_list = "[1, 2, 3]" | as_native %}
-- my_list 现在是 Python list: [1, 2, 3]

{% set my_dict = "{'key': 'value'}" | as_native %}
```

#### as_number

将 Jinja 输出转换为数值。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | 输入值 |

```sql
{{ "42" | as_number }}       -- 42
{{ "3.14" | as_number }}     -- 3.14
{{ true | as_number }}       -- 1
```

---

### 1.3 Jinja 内置测试 (is tests)

> 内置测试用于配合 `is` 关键字进行条件判断。

#### boolean

对象是否为布尔值（Jinja 2.11+）。

```jinja
{% if value is boolean %}true{% endif %}
```

#### callable

对象是否可调用。

```jinja
{% if obj is callable %}{{ obj() }}{% endif %}
```

#### defined

变量是否已定义。

```jinja
{% if variable is defined %}
    {{ variable }}
{% else %}
    variable is not defined
{% endif %}
```

#### divisibleby

变量是否能被指定数整除。

| 参数 | 类型 | 说明 |
|------|------|------|
| `num` | int | 除数 |

```jinja
{% if loop.index is divisibleby 3 %}
{% if loop.index is divisibleby(3) %}
```

#### eq / equalto

是否相等。别名：`==`、`equalto`。

```jinja
{% if a is eq b %}
{% if a is equalto b %}
```

#### escaped

值是否已被 HTML 转义。

```jinja
{% if value is escaped %}
```

#### even

变量是否为偶数。

```jinja
{% if number is even %}
```

#### false

对象是否为 `False`（Jinja 2.11+）。

```jinja
{% if value is false %}
```

#### filter

检查过滤器是否存在（Jinja 3.0+）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 过滤器名称 |

```jinja
{% if 'markdown' is filter %}
    {{ value | markdown }}
{% else %}
    {{ value }}
{% endif %}
```

#### float (test)

对象是否为浮点数（Jinja 2.11+）。

```jinja
{% if value is float %}
```

#### ge

是否大于等于。别名：`>=`。

```jinja
{% if a is ge b %}
```

#### gt / greaterthan

是否大于。别名：`>`、`greaterthan`。

```jinja
{% if a is gt b %}
{% if a is greaterthan b %}
```

#### in

值是否在容器中（Jinja 2.10+）。

```jinja
{% if 1 in [1, 2, 3] %}
{{ target.name in ['dev', 'prod'] }}
```

#### integer

对象是否为整数（Jinja 2.11+）。

```jinja
{% if value is integer %}
```

#### iterable

对象是否可迭代。

```jinja
{% if collection is iterable %}
```

#### le

是否小于等于。别名：`<=`。

```jinja
{% if a is le b %}
```

#### lower (test)

字符串是否全部小写。

```jinja
{% if text is lower %}
```

#### lt / lessthan

是否小于。别名：`<`、`lessthan`。

```jinja
{% if a is lt b %}
{% if a is lessthan b %}
```

#### mapping

对象是否为映射类型（dict 等，Jinja 2.6+）。

```jinja
{% if data is mapping %}
```

#### ne

是否不等。别名：`!=`。

```jinja
{% if a is ne b %}
```

#### none

变量是否为 `None`。

```jinja
{% if value is none %}
```

#### number

变量是否为数值。

```jinja
{% if value is number %}
```

#### odd

变量是否为奇数。

```jinja
{% if number is odd %}
```

#### sameas

两个对象是否指向同一内存地址。

```jinja
{% if foo.attribute is sameas false %}
    foo 的属性确实是 `False` 单例
{% endif %}
```

#### sequence

变量是否为序列类型。

```jinja
{% if items is sequence %}
```

#### string (test)

对象是否为字符串。

```jinja
{% if value is string %}
```

#### true

对象是否为 `True`（Jinja 2.11+）。

```jinja
{% if value is true %}
```

#### undefined

变量是否未定义（与 `defined` 相反）。

```jinja
{% if variable is undefined %}
```

#### upper (test)

字符串是否全部大写。

```jinja
{% if text is upper %}
```

#### test

检查测试是否存在（Jinja 3.0+）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | str | 测试名称 |

```jinja
{% if 'loud' is test %}
    {% if value is loud %}
        {{ value | upper }}
    {% else %}
        {{ value | lower }}
    {% endif %}
{% endif %}
```

---

## 2. dbt 内置 Jinja 函数

> dbt 在标准 Jinja 基础上扩展了大量数据库开发专用的上下文变量和函数。

### 2.1 模型与数据源引用

#### ref()

引用 dbt 项目中的另一个模型。**这是 dbt 中最重要的函数**——它构建依赖 DAG 并确保模型按正确顺序构建。

| 参数 | 类型 | 说明 |
|------|------|------|
| `model_name` | str | 模型名（文件名，不含扩展名） |
| `package_name` | str (可选) | 跨项目引用时指定包名 |
| `version` | int (可选) | 引用特定模型版本 |

```sql
-- 引用同一项目中的模型
SELECT * FROM {{ ref('stg_orders') }}

-- 引用其他包/项目中的模型
SELECT * FROM {{ ref('fivetran_salesforce', 'account') }}

-- 带版本号引用
SELECT * FROM {{ ref('my_model', v=2) }}
```

**最佳实践：** 所有模型间引用使用 `ref()`，不要硬编码表名或 schema 名。

#### source()

引用 dbt 项目中定义的源表（在 `.yml` 文件中声明的原始数据表）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `source_name` | str | 在 `sources.yml` 中定义的源名称 |
| `table_name` | str | 源中的表名 |

```sql
-- 引用源表
SELECT * FROM {{ source('snowplow', 'events') }}

-- 引用不同数据库的源表
SELECT * FROM {{ source('salesforce', 'opportunity') }}
```

**源定义示例 (sources.yml):**
```yaml
version: 2
sources:
  - name: snowplow
    database: raw
    schema: snowplow
    tables:
      - name: events
      - name: page_views
```

#### this

代表当前模型在数据库中的 Relation 对象。

| 属性 | 类型 | 说明 |
|------|------|------|
| `this.database` | str | 数据库名 |
| `this.schema` | str | Schema 名 |
| `this.identifier` | str | 表名/视图名 |
| `this.name` | str | 含 schema 的完整名 |
| `this.render()` | str | 完整限定名 |

```sql
-- 获取当前模型的完整路径
{{ this.database }}.{{ this.schema }}.{{ this.identifier }}

-- 日志中使用
{% do log("Building model: " ~ this.name, info=true) %}
```

#### model

当前模型的 dbt graph 对象（node），包含模型的全部元数据。

| 属性 | 说明 |
|------|------|
| `model.name` | 模型名 |
| `model.schema` | schema 名 |
| `model.database` | 数据库名 |
| `model.config` | 模型配置 |
| `model.depends_on` | 依赖节点列表 |
| `model.description` | 模型描述 |
| `model.tags` | 标签列表 |
| `model.meta` | 元数据字典 |
| `model.path` | 文件路径 |
| `model.original_file_path` | 原始文件路径 |
| `model.package_name` | 包名 |

```sql
{% if model.tags | select('equalto', 'nightly') | list | length > 0 %}
  -- 模型带有 nightly 标签的特殊逻辑
{% endif %}
```

#### schema

当前模型所配置的 schema 名称。

```sql
{{ schema }}    -- 直接输出当前 schema 名
```

#### schemas

当前运行期间 dbt 构建对象的所有 schema 列表。

```sql
{% for s in schemas %}
  Schema: {{ s }}
{% endfor %}
```

---

### 2.2 配置与环境

#### config()

在模型文件中设置模型/物化配置。

| 常用参数 | 类型 | 说明 |
|----------|------|------|
| `materialized` | str | `'table'` / `'view'` / `'incremental'` / `'ephemeral'` / `'materialized_view'` |
| `schema` | str | 目标 schema |
| `database` | str | 目标数据库 |
| `alias` | str | 表/视图别名 |
| `tags` | list[str] | 标签列表 |
| `pre_hook` / `post_hook` | list[str] | 前后 hook SQL |
| `unique_key` | str | 增量模型的唯一键 |
| `partition_by` | str | 分区字段 |
| `cluster_by` | str | 聚簇字段 |
| `snowflake_warehouse` | str | Snowflake 虚拟仓库 |
| `enabled` | bool | 是否启用 |
| `full_refresh` | bool | 全量刷新 |
| `contract` | dict | 模型契约 |

```sql
{{ config(
    materialized='incremental',
    unique_key='id',
    schema='analytics',
    tags=['hourly', 'critical'],
    snowflake_warehouse='transform_wh',
    post_hook=[
        "{{ dbt_utils.audit_helper_compare_relations(
            a_relation=this,
            b_relation=this ~ '__backup',
            primary_key='id'
        ) }}"
    ]
) }}

SELECT * FROM {{ ref('stg_events') }}
{% if is_incremental() %}
WHERE event_date > (SELECT MAX(event_date) FROM {{ this }})
{% endif %}
```

#### var()

获取 dbt_project.yml 中定义的变量或命令行传入的变量。

| 参数 | 类型 | 说明 |
|------|------|------|
| `name` | str | 变量名 |
| `default` | Any (可选) | 默认值 |

```sql
-- 从 dbt_project.yml 或命令行获取变量
{{ var('my_dynamic_date') }}

-- 带默认值
SELECT * FROM {{ ref('orders') }}
WHERE order_date >= '{{ var("start_date", "2024-01-01") }}'

-- 布尔变量控制逻辑
{% if var('enable_audit', true) %}
  -- 审计逻辑
{% endif %}
```

**命令行传入：**
```bash
dbt run --vars '{"start_date": "2024-06-01", "enable_audit": false}'
```

**dbt_project.yml 定义：**
```yaml
vars:
  start_date: '2024-01-01'
  enable_audit: true
```

#### env_var()

从运行环境获取环境变量。

| 参数 | 类型 | 说明 |
|------|------|------|
| `name` | str | 环境变量名 |
| `default` | str (可选) | 默认回退值 |

```sql
-- 获取环境变量
SELECT * FROM {{ ref('users') }}
WHERE environment = '{{ env_var("DBT_ENV", "dev") }}'

-- 敏感信息（不要硬编码）
{{ config(
    snowflake_warehouse=env_var('SNOWFLAKE_WAREHOUSE', 'dev_wh')
) }}

-- 无默认值时会报错，适合强制要求
{{ env_var('SNOWFLAKE_PASSWORD') }}   -- 未设置则报错
```

#### target

包含当前数据库连接信息。

| 属性 | 说明 |
|------|------|
| `target.name` | 连接配置名（如 dev / prod） |
| `target.type` | 适配器类型（如 snowflake） |
| `target.database` | 数据库名 |
| `target.schema` | 默认 schema |
| `target.threads` | 并行线程数 |
| `target.warehouse` | Snowflake 仓库 |
| `target.role` | Snowflake 角色 |
| `target.user` | 用户名 |

```sql
{% if target.name == 'prod' %}
  {{ config(snowflake_warehouse='prod_transform_wh') }}
{% elif target.name == 'dev' %}
  {{ config(snowflake_warehouse='dev_transform_wh') }}
{% endif %}
```

#### flags

包含 CLI 传入的标志值。

| 常用属性 | 说明 |
|----------|------|
| `flags.WHICH` | 当前命令（run / test / compile / generate 等） |
| `flags.FULL_REFRESH` | 是否全量刷新 |
| `flags.STORE_FAILURES` | 是否存储失败记录 |

```sql
{% if flags.WHICH == 'run' %}
  {% do log("Running models...", info=true) %}
{% endif %}

{% if flags.FULL_REFRESH %}
  -- 全量刷新逻辑
  {{ config(materialized='table') }}
{% endif %}
```

---

### 2.3 序列化 / 反序列化

#### fromjson()

将 JSON 字符串反序列化为 Python 对象。

| 参数 | 类型 | 说明 |
|------|------|------|
| `json_string` | str | JSON 格式字符串 |

```sql
{% set config = fromjson('{"threshold": 100, "tags": ["nightly", "critical"]}') %}
{{ config.threshold }}   -- 100
{{ config.tags[0] }}     -- 'nightly'

-- 典型用法：在 var 中传递结构化配置
{% set audit_config = fromjson(var('audit_rules', '{}')) %}
```

#### fromyaml()

将 YAML 字符串反序列化为 Python 对象。

| 参数 | 类型 | 说明 |
|------|------|------|
| `yaml_string` | str | YAML 格式字符串 |

```sql
{% set settings = fromyaml("
threshold: 100
tags:
  - nightly
  - critical
") %}
{{ settings.threshold }}      -- 100
{{ settings.tags[0] }}        -- 'nightly'
```

#### tojson() (dbt)

将 Python 对象序列化为 JSON 字符串。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | Python 原生对象 |

```sql
{% set my_dict = {"key": "value", "num": 42} %}
{{ tojson(my_dict) }}   -- '{"key": "value", "num": 42}'

-- 在 hook 中传递结构化上下文
{{ config(post_hook="INSERT INTO audit_log VALUES ('" ~ tojson(graph.nodes.values() | list) ~ "')") }}
```

#### toyaml()

将 Python 对象序列化为 YAML 字符串。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | Python 原生对象 |

```sql
{% set data = {"name": "model_a", "depends_on": ["model_b", "model_c"]} %}
{{ toyaml(data) }}
-- 输出:
-- name: model_a
-- depends_on:
--   - model_b
--   - model_c
```

---

### 2.4 执行与运行上下文

#### execute

返回 `True` 当 dbt 处于 "执行" 模式（而非 "解析" 模式）。

**关键概念：** dbt 编译时对 Jinja 模板执行两次——先在解析模式构建 DAG，再在执行模式运行 SQL。需要数据库查询的代码必须在 `execute` 为 True 时才执行。

```sql
{% if execute %}
  {% set results = run_query("SELECT COUNT(*) FROM " ~ ref('stg_orders')) %}
  {% set row_count = results.columns[0].values()[0] %}
  {% do log("stg_orders has " ~ row_count ~ " rows", info=true) %}
{% endif %}
```

#### run_started_at

本次 dbt 运行开始的时间戳（Python `datetime` 对象）。

```sql
-- 记录运行时间
{{ run_started_at }}

-- 作为默认日期
{% set run_date = run_started_at.strftime('%Y-%m-%d') %}
SELECT * FROM events WHERE event_date = '{{ run_date }}'
```

#### invocation_id

当前 dbt 命令的唯一 UUID。

```sql
-- 审计表中记录运行标识
INSERT INTO dbt_metadata (invocation_id, model_name)
VALUES ('{{ invocation_id }}', '{{ model.name }}')
```

#### thread_id

当前 Python 线程标识符，格式如 `'Thread-1'`。

```sql
{{ thread_id }}
```

#### selected_resources

当前 dbt 命令所选的全部节点 `unique_id` 列表。

```sql
{% if 'model.my_project.stg_orders' in selected_resources %}
  -- stg_orders 在本运行中被选中的特殊逻辑
{% endif %}
```

#### graph

包含 dbt 项目中所有节点信息的数据结构。

```sql
{% for node in graph.nodes.values() %}
  {% if node.resource_type == 'model' %}
    {{ node.name }}
  {% endif %}
{% endfor %}
```

#### on-run-end

`on-run-end` hook 中可用的上下文变量。

```sql
-- 在 dbt_project.yml 中
on-run-end:
  - "{{ grant_all_on_schemas(schemas, target.role) }}"
```

---

### 2.5 数据库操作

#### run_query()

在编译期间执行 SQL 查询并获取结果。返回 Agate Table 对象。

| 参数 | 类型 | 说明 |
|------|------|------|
| `sql` | str | 要执行的 SQL 查询 |

```sql
-- 获取最新日期
{% set results = run_query("SELECT MAX(order_date) FROM " ~ ref('stg_orders')) %}
{% set latest_date = results.columns[0].values()[0] %}

-- 动态构建 SELECT 列
{% set columns_query %}
  SELECT column_name FROM information_schema.columns
  WHERE table_name = '{{ ref("stg_orders").identifier }}'
{% endset %}
{% set results = run_query(columns_query) %}
{% set columns = results.columns[0].values() %}
SELECT
{% for col in columns %}
  {{ col }},
{% endfor %}
FROM {{ ref('stg_orders') }}
```

> **注意：** `run_query` 在解析和编译阶段都会执行。使用 `{% if execute %}` 包裹以避免解析阶段的副作用。

#### adapter

包装内部数据库适配器的 Jinja 对象，提供数据库操作函数。

| 常用方法 | 说明 |
|----------|------|
| `adapter.get_relation(database, schema, identifier)` | 获取 Relation 对象 |
| `adapter.get_columns_in_relation(relation)` | 获取表中列信息 |
| `adapter.create_schema(relation)` | 创建 schema |
| `adapter.drop_schema(relation)` | 删除 schema |
| `adapter.drop_relation(relation)` | 删除表/视图 |
| `adapter.rename_relation(from, to)` | 重命名表 |
| `adapter.get_missing_columns(from, to)` | 比较两表列差异 |

```sql
{% set relation = adapter.get_relation(
    database=target.database,
    schema=target.schema,
    identifier='stg_orders'
) %}
{% if relation is none %}
  -- 表不存在
{% endif %}

-- 获取列信息
{% set columns = adapter.get_columns_in_relation(ref('stg_orders')) %}
{% for col in columns %}
  {{ col.name }}: {{ col.data_type }}
{% endfor %}
```

#### statement blocks

执行 SQL 并将结果绑定到 Jinja 上下文的代码块。

```sql
{% call statement('my_query', fetch_result=True) %}
    SELECT status, COUNT(*) as cnt
    FROM {{ ref('orders') }}
    GROUP BY 1
{% endcall %}
{% set results = load_result('my_query') %}
{% set data = results['data'] %}
{% for row in data %}
  Status: {{ row[0] }}, Count: {{ row[1] }}
{% endfor %}
```

---

### 2.6 项目与模块

#### dbt_version

返回当前 dbt 版本字符串。

```sql
{{ dbt_version }}    -- 如: 1.9.0

{% if dbt_version >= '1.8.0' %}
  -- 使用 v1.8+ 特性
{% endif %}
```

#### project_name

返回当前 dbt 项目名称。

```sql
{{ project_name }}
```

#### modules

暴露 Python 模块供数据处理使用。

```sql
{% set datetime = modules.datetime %}
{% set pytz = modules.pytz %}
{% set re = modules.re %}

{% set today = datetime.date.today() %}
{% set one_month_ago = today - datetime.timedelta(days=30) %}

-- 正则匹配
{% set matches = modules.re.findall(r'[0-9]+', some_string) %}
```

#### builtins

提供内置 Jinja 变量的访问。

```sql
{{ builtins }}
```

---

### 2.7 工具与调试

#### log()

将消息写入 dbt 日志文件。

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `message` | str | — | 日志消息 |
| `info` | bool | True | True=INFO 级别, False=DEBUG 级别 |

```sql
{% do log("Starting model: " ~ model.name, info=true) %}
{% do log("This is a debug detail", info=false) %}

-- 条件日志
{% if execute %}
  {% set cnt = run_query("SELECT COUNT(*) FROM " ~ ref('orders')).columns[0][0] %}
  {% do log("Order count: " ~ cnt, info=true) %}
{% endif %}
```

#### print() (dbt)

将消息同时输出到日志文件和 stdout（CLI 终端）。

| 参数 | 类型 | 说明 |
|------|------|------|
| `message` | str | 输出消息 |

```sql
{{ print("Building: " ~ model.name) }}

-- 输出查询结果
{% set results = run_query("SELECT COUNT(*) FROM " ~ ref('stg_orders')) %}
{{ print("stg_orders row count: " ~ results[0][0]) }}
```

**`log()` vs `print()` 区别：** `log()` 只写日志文件，`print()` 同时写日志文件和终端输出。

#### debug()

打开 iPython 交互式调试器（在运行 dbt 的终端中）。

| 参数 | 类型 | 说明 |
|------|------|------|
| — | — | 无参数 |

```sql
{% set my_var = {"key": "value"} %}
{{ debug() }}   -- 在此处进入 iPython 调试器，可检查 my_var
```

> **注意：** 退出调试器输入 `q` 或 `Ctrl+D`。仅开发环境使用。

#### return()

从宏中提前返回一个值。

| 参数 | 类型 | 说明 |
|------|------|------|
| `value` | Any | 返回值 |

```sql
{% macro safe_divide(numerator, denominator) %}
  {% if denominator == 0 %}
    {{ return(0) }}
  {% endif %}
  {{ numerator / denominator }}
{% endmacro %}

-- 从宏返回结构化数据
{% macro get_table_config() %}
  {% return({
    "materialized": "incremental",
    "unique_key": "id",
    "tags": ["nightly"]
  }) %}
{% endmacro %}
```

#### exceptions

抛出编译错误或警告。

| 方法 | 说明 |
|------|------|
| `exceptions.raise_compiler_error(msg)` | 终止编译并显示错误 |
| `exceptions.raise_database_error(msg)` | 终止并显示数据库错误 |
| `exceptions.warn(msg)` | 显示警告但不终止 |

```sql
-- 强制终止
{% if target.name not in ['dev', 'staging', 'prod'] %}
  {{ exceptions.raise_compiler_error("Invalid target: " ~ target.name) }}
{% endif %}

-- 警告
{% if is_incremental() and not var('has_merge_logic', false) %}
  {{ exceptions.warn("Incremental run — ensure dedup logic is active.") }}
{% endif %}
```

#### local_md5()

对字符串计算 MD5 哈希。

| 参数 | 类型 | 说明 |
|------|------|------|
| `string` | str | 要哈希的字符串 |

```sql
{{ local_md5("hello") }}
-- '5d41402abc4b2a76b9719d911017c592'

-- 典型用法：为多列生成 surrogate key
SELECT
    {{ local_md5(
        coalesce(cast(id as varchar), '') ~ '|' ~
        coalesce(email, '') ~ '|' ~
        coalesce(cast(updated_at as varchar), '')
    ) }} AS row_hash,
    *
FROM {{ source('raw', 'users') }}
```

#### set()

将可迭代对象转换为去重元素序列。

| 参数 | 类型 | 说明 |
|------|------|------|
| `iterable` | iterable | 可迭代对象 |

```sql
{% set duplicates = [1, 2, 2, 3, 3, 3] %}
{{ set(duplicates) }}   -- {1, 2, 3}

-- 去重 schema 列表
{% set unique_schemas = set(schemas) %}
```

#### zip()

返回多个可迭代对象的元组迭代器。

| 参数 | 类型 | 说明 |
|------|------|------|
| `*iterables` | iterable(s) | 多个可迭代对象 |

```sql
{% set cols = ["id", "name", "date"] %}
{% set types = ["INT", "VARCHAR", "TIMESTAMP"] %}
{% for col, dtype in zip(cols, types) %}
  {{ col }} {{ dtype }}{{ "," if not loop.last }}
{% endfor %}
-- 输出: id INT, name VARCHAR, date TIMESTAMP
```

#### doc()

引用在 `.yml` 文件中定义的文档块。

| 参数 | 类型 | 说明 |
|------|------|------|
| `block_name` | str | 文档块名称 |

```yaml
# schema.yml
version: 2
models:
  - name: orders
    description: "{{ doc('orders_table_desc') }}"

docs:
  - name: orders_table_desc
    description: >
      This table contains all orders with their associated line items
      and customer details. Updated hourly via incremental merge.
```

```sql
-- 在 description 中引用
{{ doc('orders_table_desc') }}
```

---

### 2.8 宏与分发

#### dispatch()

跨数据平台分发宏调用，支持多平台兼容。

| 参数 | 类型 | 说明 |
|------|------|------|
| `macro_name` | str | 宏名称 |
| `packages` | list (可选) | 从指定包查找实现 |

```sql
-- 基本分发：自动查找适配平台实现
{{ dispatch('my_cross_db_macro') }}

-- 指定包
{{ dispatch('my_macro', packages=['dbt_utils']) }}
```

**工作原理：** 定义宏时在父宏中 `{{ dispatch(...) }}`，各平台用适配器前缀覆写（如 `snowflake__my_macro`）。

---

### 2.9 API 类 (Relation / Column / Result)

#### Relation

安全地处理 schema/表名插值，自动处理引号规则。

**创建：**
```sql
{% set relation = api.Relation.create(
    database='analytics',
    schema='snowplow',
    identifier='events'
) %}
```

**`ref()`、`source()`、`this` 自动返回 Relation 对象。**

| 属性/方法 | 类型 | 说明 |
|-----------|------|------|
| `relation.database` | str | 数据库名 |
| `relation.schema` | str | Schema 名（BigQuery 为 dataset） |
| `relation.identifier` | str | 表/视图名 |
| `relation.name` | str | 不含 database 的名称 |
| `relation.include(database=False)` | str | 不含 database 的限定名 |
| `relation.render()` | str | 完整限定名 |
| `relation.is_table` | bool | 是否 table |
| `relation.is_view` | bool | 是否 view |
| `relation.is_cte` | bool | 是否是 CTE |

```sql
{% set rel = ref('stg_orders') %}

-- 在不同上下文中使用
SELECT * FROM {{ rel }}
{{ rel.database }}
{{ rel.schema }}
{{ rel.identifier }}
{{ rel.include(database=false) }}
```

#### Column

封装表列信息：名称、数据类型、字符大小、数值精度等。

**构造函数：**
```python
api.Column(column_name, dtype, char_size=None, numeric_size=None)
```

| 属性 | 说明 |
|------|------|
| `col.column` / `col.name` | 列名 |
| `col.dtype` | 数据类型（不含尺寸） |
| `col.data_type` | 数据类型（含尺寸/精度/标度） |
| `col.char_size` | 字符串类型的最大宽度 |
| `col.numeric_precision` | 定点数值的精度 |
| `col.numeric_scale` | 定点数值的标度 |
| `col.quoted` | 加引号后的列名 |

| 实例方法 | 返回值 |
|----------|--------|
| `col.is_string()` | 是否 String 类型 |
| `col.is_numeric()` | 是否定点 Numeric 类型 |
| `col.is_number()` | 是否数值类（int/float/numeric） |
| `col.is_integer()` | 是否整数 |
| `col.is_float()` | 是否浮点数 |
| `col.string_size()` | 字符串宽度 |

| 静态方法 | 说明 |
|----------|------|
| `api.Column.string_type(255)` | 返回数据看适用的 string 类型表示 `character varying(255)` |
| `api.Column.numeric_type('numeric', 12, 4)` | 返回数据库适用的 numeric 类型表示 `numeric(12,4)` |

```sql
-- 检查列类型
{% set columns = adapter.get_columns_in_relation(ref('stg_orders')) %}
{% for col in columns %}
  {% if col.is_string() %}
    COALESCE(NULLIF({{ col.quoted }}, ''), 'N/A')
  {% elif col.is_numeric() %}
    COALESCE({{ col.quoted }}, 0)
  {% else %}
    {{ col.quoted }}
  {% endif %}
{% endfor %}
```

#### BigQueryColumn

`Column` 的 BigQuery 覆写，额外支持 STRUCT / REPEATED 嵌套字段。

| 额外属性/方法 | 说明 |
|---------------|------|
| `col.fields` | 返回子字段列表（STRUCT 列） |
| `col.mode` | 列模式（如 `REPEATED`） |
| `col.flatten()` | 展平嵌套字段为独立列 |

```sql
{% for col in adapter.get_columns_in_relation(source('ga4', 'events')) %}
  {% if col.mode == 'REPEATED' %}
    -- 处理重复字段
  {% endif %}
{% endfor %}
```

#### Result

dbt 资源执行结果对象，记录在 `run_results.json` 中。

| 属性 | 说明 |
|------|------|
| `result.node` | dbt 资源完整对象（含 unique_id） |
| `result.status` | 运行时状态 |
| `result.thread_id` | 执行线程 |
| `result.execution_time` | 执行耗时（秒） |
| `result.timing` | 耗时分解（compile + execute） |
| `result.message` | CLI 输出消息 |
| `result.adapter_response` | 数据库返回的元数据字典 |

```sql
-- 在 on-run-end hook 中
{% for result in results %}
  {% if result.status == 'error' %}
    {{ log("Error in " ~ result.node.name ~ ": " ~ result.message, info=true) }}
  {% endif %}
{% endfor %}
```

---

## 3. dbt 引用包

> dbt 包通过 `packages.yml` 文件引入，提供可复用的宏、模型和测试。

### 3.1 安装语法

**packages.yml 格式：**
```yaml
packages:
  # Hub 发布包
  - package: dbt-labs/dbt_utils
    version: [">=1.3.0", "<2.0.0"]

  # Git 仓库
  - git: "https://github.com/dbt-labs/dbt-utils.git"
    revision: 1.3.0

  # 本地包
  - local: /path/to/local/package
```

**安装命令：**
```bash
dbt deps
```

---

### 3.2 dbt Labs 官方包

#### dbt_utils

**包名：** `dbt-labs/dbt_utils`

最广泛使用的 dbt 工具包。提供跨数据库兼容的通用 SQL 宏和测试。

```yaml
packages:
  - package: dbt-labs/dbt_utils
    version: ["1.4.0"]
```

**常用宏：**

```sql
-- 生成 surrogate key
SELECT {{ dbt_utils.generate_surrogate_key(['id', 'email', 'updated_at']) }} AS sk

-- 数据透视 (pivot)
{{ dbt_utils.pivot(
    column='status',
    values=['completed', 'pending', 'cancelled'],
    agg='count',
    then_value='id',
    prefix='count_'
) }}

-- Union 多个表
{{ dbt_utils.union_relations(
    relations=[
        ref('orders_us'),
        ref('orders_eu'),
        ref('orders_apac')
    ],
    include=['id', 'amount', 'order_date'],
    source_column_name='region'
) }}

-- 星型日期 spine
{{ dbt_utils.date_spine(
    datepart="day",
    start_date="cast('2024-01-01' as date)",
    end_date="cast('2024-12-31' as date)"
) }}
```

**常用测试：**
```yaml
# schema.yml
models:
  - name: orders
    columns:
      - name: order_id
        tests:
          - dbt_utils.unique_combination_of_columns:
              combination_of_columns:
                - order_id
                - order_date
          - dbt_utils.not_null_proportion:
              at_least: 0.95
      - name: status
        tests:
          - dbt_utils.accepted_range:
              min_value: 0
              max_value: 100
```

#### audit_helper

**包名：** `dbt-labs/audit_helper`

表级数据审计——快速比较两个 relation 的行数据差异。

```yaml
packages:
  - package: dbt-labs/audit_helper
    version: ["0.12.0"]
```

```sql
-- 比较两个表的数据
{{ audit_helper.compare_relations(
    a_relation=ref('orders_new'),
    b_relation=ref('orders_old'),
    primary_key='order_id'
) }}

-- 比较 column 级别的差异
{{ audit_helper.compare_column_values(
    a_relation=ref('orders_new'),
    b_relation=ref('orders_old'),
    primary_key='order_id',
    column_to_compare='amount'
) }}
```

#### codegen

**包名：** `dbt-labs/codegen`

自动生成模型 YAML (schema.yml) 和 SQL 模板。

```yaml
packages:
  - package: dbt-labs/codegen
    version: ["0.13.0"]
```

```sql
-- 生成模型的 YAML 描述
{{ codegen.generate_model_yaml(
    model_names=['stg_orders', 'dim_customers']
) }}

-- 生成 source YAML
{{ codegen.generate_source(
    schema_name='raw',
    database_name=target.database,
    table_names=['orders', 'customers', 'products']
) }}

-- 生成 base model SQL
{{ codegen.generate_base_model(
    source_name='raw',
    table_name='orders'
) }}
```

#### dbt_external_tables

**包名：** `dbt-labs/dbt_external_tables`

管理 Snowflake 外部表/阶段、BigQuery 外部表、Redshift Spectrum 等。

```yaml
packages:
  - package: dbt-labs/dbt_external_tables
    version: ["0.11.0"]
```

```sql
-- 获取 Snowflake 外部表 DDL
{{ dbt_external_tables.snowflake_external_table_ddl(
    source_name='s3_data',
    table_name='events'
) }}
```

#### dbt_project_evaluator

**包名：** `dbt-labs/dbt_project_evaluator`

检查 dbt 项目最佳实践——建模规范、测试覆盖率、文档完整性等。

```yaml
packages:
  - package: dbt-labs/dbt_project_evaluator
    version: ["1.0.0"]
```

```bash
# 运行项目评估
dbt build --select package:dbt_project_evaluator
```

#### redshift

**包名：** `dbt-labs/redshift`

Redshift 专属工具（如 `admin` 操作、表维护）。

```yaml
packages:
  - package: dbt-labs/redshift
    version: ["0.8.0"]
```

#### spark_utils

**包名：** `dbt-labs/spark_utils`

Spark/DataBricks 专属工具宏。

```yaml
packages:
  - package: dbt-labs/spark_utils
    version: ["0.5.0"]
```

---

### 3.3 Snowflake 专用包

#### dbt_snow_mask

**包名：** `entechlog/dbt_snow_mask`

在 Snowflake 上管理动态数据脱敏 (Dynamic Data Masking) 策略。

```yaml
packages:
  - package: entechlog/dbt_snow_mask
    version: ["1.0.0"]
```

```sql
-- 创建脱敏策略
{{ dbt_snow_mask.create_masking_policy(
    name='mask_email',
    data_type='string',
    masking_expression='case when current_role() in (\'ADMIN\') then val else \'***MASKED***\' end'
) }}

-- 应用策略到列
{{ dbt_snow_mask.apply_masking_policy(
    policy='mask_email',
    table=ref('customers'),
    column='email'
) }}
```

#### dbt_constraints

**包名：** `Snowflake-Labs/dbt_constraints`

在 dbt 模型中定义和管理数据库约束（PK / FK / UNIQUE）。

```yaml
packages:
  - package: Snowflake-Labs/dbt_constraints
    version: ["1.0.0"]
```

```sql
{{ config(
    materialized='table',
    contract={'enforced': true},
    constraints={
        'primary_key': ['order_id'],
        'unique': ['order_key'],
        'foreign_key': {
            'fk_customer': {
                'columns': ['customer_id'],
                'ref_table': ref('dim_customers'),
                'ref_columns': ['customer_id']
            }
        }
    }
) }}
```

#### dbt_snowflake_monitoring

**包名：** `get-select/dbt_snowflake_monitoring`

Snowflake 费用监控——分析查询成本、仓库使用、存储消耗等。

```yaml
packages:
  - package: get-select/dbt_snowflake_monitoring
    version: ["1.0.0"]
```

#### dbt_query_tags

**包名：** `get-select/dbt_query_tags`

为 dbt 运行的 SQL 自动设置 Snowflake Query Tag。

```yaml
packages:
  - package: get-select/dbt_query_tags
    version: ["1.0.0"]
```

```sql
-- 在 dbt_project.yml 中配置
vars:
  dbt_query_tags:
    enabled: true
    tags:
      dbt_model: "{{ model.name }}"
      dbt_materialized: "{{ model.config.materialized }}"
      dbt_user: "{{ target.user }}"
```

#### snowflake_utils

**包名：** `Montreal-Analytics/snowflake_utils`

Snowflake 工具宏集合。

```yaml
packages:
  - package: Montreal-Analytics/snowflake_utils
    version: ["0.7.0"]
```

#### dbt_snow_utils

**包名：** `entechlog/dbt_snow_utils`

Snowflake 实用工具集。

```yaml
packages:
  - package: entechlog/dbt_snow_utils
    version: ["1.0.0"]
```

---

### 3.4 测试与质量包

#### dbt_expectations

**包名：** `metaplane/dbt_expectations`

受 Great Expectations 启发的声明式数据质量测试——提供 50+ 内置测试。

```yaml
packages:
  - package: metaplane/dbt_expectations
    version: ["0.10.0"]
```

```yaml
# schema.yml
models:
  - name: orders
    columns:
      - name: amount
        tests:
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: 0
              max_value: 10000
              row_condition: "order_date >= current_date - 30"
          - dbt_expectations.expect_column_values_to_not_be_null:
              row_condition: "status = 'completed'"
      - name: order_date
        tests:
          - dbt_expectations.expect_row_values_to_have_recent_data:
              datepart: day
              interval: 1
      - name: customer_id
        tests:
          - dbt_expectations.expect_column_pair_values_A_to_be_greater_than_B:
              column_B: order_date
```

#### dbt_unit_testing

**包名：** `EqualExperts/dbt_unit_testing`

为 dbt 模型创建单元测试（mock 输入数据，验证输出）。

```yaml
packages:
  - package: EqualExperts/dbt_unit_testing
    version: ["0.4.0"]
```

```sql
-- unit_tests.yml
unit_tests:
  - name: test_order_total_calculation
    model: fct_orders
    given:
      - input: ref('stg_orders')
        rows:
          - { order_id: 1, quantity: 2, unit_price: 10.00 }
      - input: ref('stg_discounts')
        rows:
          - { order_id: 1, discount_pct: 10 }
    expect:
      rows:
        - { order_id: 1, total: 18.00 }
```

#### dbt_dataquality

**包名：** `Divergent-Insights/dbt_dataquality`

全面的数据质量检查套件。

```yaml
packages:
  - package: Divergent-Insights/dbt_dataquality
    version: ["1.0.0"]
```

#### dq_tools

**包名：** `infinitelambda/dq_tools`

数据质量监控工具——自动生成质量仪表盘。

```yaml
packages:
  - package: infinitelambda/dq_tools
    version: ["1.0.0"]
```

#### dbt_assertions

**包名：** `AxelThevenot/dbt_assertions`

基于 SQL 表达式的声明式断言测试。

```yaml
packages:
  - package: AxelThevenot/dbt_assertions
    version: ["0.1.0"]
```

---

### 3.5 数据转换与工具包

#### dbt_date

**包名：** `godatadriven/dbt_date`

日期维度表和日期处理宏——包括财政年度、ISO 周、节假日等。

```yaml
packages:
  - package: godatadriven/dbt_date
    version: ["0.10.0"]
```

```sql
-- 生成日期维度表
{{ dbt_date.get_date_dimension(
    start_date='2020-01-01',
    end_date='2030-12-31'
) }}

-- 获取当前日期各种表示
{{ dbt_date.today() }}
{{ dbt_date.yesterday() }}
{{ dbt_date.n_months_ago(3) }}
{{ dbt_date.last_week_start() }}
{{ dbt_date.last_week_end() }}

-- 财政年度函数
{{ dbt_date.get_fiscal_year('2024-07-15', start_month=4) }}
{{ dbt_date.get_fiscal_quarter('2024-07-15') }}

-- 日期转换
{{ dbt_date.convert_timezone(
    column='created_at',
    target_tz='America/Chicago',
    source_tz='UTC'
) }}
```

#### dbt_ml

**包名：** `kristeligt-dagblad/dbt_ml`

在 dbt 中进行特征工程和机器学习预处理。

```yaml
packages:
  - package: kristeligt-dagblad/dbt_ml
    version: ["1.0.0"]
```

```sql
-- 标准化 (Standardization)
SELECT {{ dbt_ml.standardize('revenue') }} FROM {{ ref('orders') }}

-- 最小-最大归一化 (Min-Max Normalization)
SELECT {{ dbt_ml.min_max_normalize('revenue') }} FROM {{ ref('orders') }}

-- 独热编码 (One-Hot Encoding)
SELECT {{ dbt_ml.one_hot_encode('status', ['completed', 'pending', 'cancelled']) }}
FROM {{ ref('orders') }}

-- 分位数分箱
SELECT {{ dbt_ml.quantile_bin('age', 4) }} FROM {{ ref('customers') }}
```

#### dbt_activity_schema

**包名：** `tnightengale/dbt_activity_schema`

构建活动的 schema 以达到客户数据建模的高标准。

```yaml
packages:
  - package: tnightengale/dbt_activity_schema
    version: ["0.3.0"]
```

#### dbtplyr

**包名：** `emilyriederer/dbtplyr`

Analogous to `dplyr` 的数据选择/过滤——列选择和工作流辅助。

```yaml
packages:
  - package: emilyriederer/dbtplyr
    version: ["0.4.0"]
```

```sql
-- 选择以 'order_' 开头的所有列
{{ dbtplyr.starts_with('order_') }}

-- 选择包含 'amount' 的列
{{ dbtplyr.contains('amount') }}
```

#### dbt_tags

**包名：** `infinitelambda/dbt_tags`

dbt 标签管理系统——可按标签选择性运行模型。

```yaml
packages:
  - package: infinitelambda/dbt_tags
    version: ["1.0.0"]
```

#### dbt_translate

**包名：** `datnguye/dbt_translate`

在 dbt 中进行文本翻译和本地化。

```yaml
packages:
  - package: datnguye/dbt_translate
    version: ["0.2.0"]
```

#### dbt_privacy

**包名：** `pvcy/dbt_privacy`

数据隐私保护和匿名化工具。

```yaml
packages:
  - package: pvcy/dbt_privacy
    version: ["1.0.0"]
```

```sql
-- 数据脱敏
SELECT {{ dbt_privacy.hash('email') }} FROM {{ ref('users') }}
SELECT {{ dbt_privacy.mask('phone', n=3, char='*') }} FROM {{ ref('users') }}
```

#### dbt_datamocktool

**包名：** `mjirv/dbt_datamocktool`

为 dbt 模型创建模拟数据进行测试。

```yaml
packages:
  - package: mjirv/dbt_datamocktool
    version: ["0.4.0"]
```

#### automate_dv / datavault4dbt

**包名：** `Datavault-UK/automate_dv` / `ScalefreeCOM/datavault4dbt`

Data Vault 2.0 建模自动化——生成 Hub、Link、Satellite 表。

```yaml
packages:
  - package: Datavault-UK/automate_dv
    version: ["0.12.0"]
```

```sql
-- 自动生成 Hub 表
{{ automate_dv.hub(
    src_pk='customer_hk',
    src_nk='customer_id',
    src_ldts='load_datetime',
    src_source='source_system',
    source_model=ref('stg_customers')
) }}
```

---

### 3.6 Fivetran 数据源包

Fivetran 提供 59+ SaaS 数据源的 dbt 模型包，将原始 API 数据转化为结构化分析表。

**通用安装格式：**
```yaml
packages:
  - package: fivetran/salesforce
    version: ["1.0.0"]
  - package: fivetran/stripe
    version: ["1.0.0"]
  - package: fivetran/hubspot
    version: ["1.0.0"]
```

**主要产品列表：**

| 包名 | 数据源 | 说明 |
|------|--------|------|
| `fivetran/salesforce` | Salesforce | CRM 数据：机会、联系人、活动 |
| `fivetran/stripe` | Stripe | 支付数据：收费、退款、订阅 |
| `fivetran/hubspot` | HubSpot | 营销和 CRM 数据 |
| `fivetran/jira` | Jira | 项目管理：ISSUE、Sprint、Epic |
| `fivetran/shopify` | Shopify | 电商数据：订单、商品、客户 |
| `fivetran/zendesk` | Zendesk | 工单系统：Tickets、Users、SLA |
| `fivetran/facebook_ads` | Facebook Ads | 广告投放：Campaigns、Ads、Insights |
| `fivetran/google_ads` | Google Ads | 广告投放：Campaigns、Keywords |
| `fivetran/linkedin` | LinkedIn Ads | 广告投放 |
| `fivetran/github` | GitHub | 仓库：Commits、PRs、Issues |
| `fivetran/netsuite` | NetSuite | ERP 财务数据 |
| `fivetran/marketo` | Marketo | 营销自动化 |
| `fivetran/qualtrics` | Qualtrics | 调研数据 |
| `fivetran/zuora` | Zuora | 订阅计费 |
| `fivetran/xero` | Xero | 会计数据 |
| `fivetran/adwords` | Google Ads (旧称) | 通过 dbt-labs 维护 |
| `fivetran/fivetran_utils` | — | Fivetran 包的公共工具 |

**使用 Fivetran 包的典型模式：**
```sql
-- 源表在 Fivetran schema 中，直接使用 source
SELECT * FROM {{ source('salesforce', 'opportunity') }}

-- staging 模型中引用包提供的宏
{{ fivetran_utils.fill_staging_columns(
    source_columns=adapter.get_columns_in_relation(source('salesforce','opportunity')),
    staging_columns=get_opportunity_columns()
) }}
```

---

### 3.7 机器学习与高级分析包

| 包名 | 维护者 | 说明 |
|------|--------|------|
| `dbt_ml` | `kristeligt-dagblad` | 特征工程和 ML 预处理 |
| `dbt_ml_eval` | `Matts52` | ML 模型评估指标 (accuracy, precision, recall, F1) |
| `dbt_ml_preprocessing` | `omnata-labs` | ML 数据预处理 |
| `dbt_ml_inline_preprocessing` | `Matts52` | 内联 ML 预处理 |
| `dbt_linreg` | `dwreeves` | 线性回归分析 |
| `dbt_pca` | `dwreeves` | 主成分分析 |
| `dbt_stat_test` | `Matts52` | 统计测试 (t-test, chi-square, etc.) |
| `dbt_set_similarity` | `Matts52` | 集合相似度计算 |
| `dbt_graph_theory` | `jpmmcneill` | 图论算法 |
| `dbt_llm_evals` | `paradime-io` | LLM 评估 |
| `metalog` | `techindicium` | Metalog 分布建模 |
| `feature_store` | `fal-ai` | 特征商店 |

---

### 3.8 监控与元数据包

#### elementary

**包名：** `elementary-data/elementary`

数据可观测性平台——自带 UI 的数据质量监控和异常检测。

```yaml
packages:
  - package: elementary-data/elementary
    version: ["0.18.0"]
```

```bash
# 安装 Elementary
pip install elementary-data
dbt deps

# 生成监控报告 UI
edr report
```

#### dbt_artifacts

**包名：** `brooklyn-data/dbt_artifacts`

持久化 dbt 运行元数据（artifacts）到数据库，构建 dbt 运行历史数据分析。

```yaml
packages:
  - package: brooklyn-data/dbt_artifacts
    version: ["2.7.0"]
```

```sql
-- 自动记录每次运行的：
-- - models, tests, seeds, snapshots 的执行状态
-- - 模型执行时间趋势
-- - 测试失败历史
-- - 运行频率和覆盖率

-- 查询运行历史
SELECT * FROM {{ ref('dbt_artifacts__fct_models') }}
WHERE total_run_time_seconds > 300
```

#### dbt_test_results

**包名：** `xoniks/dbt_test_results`

测试结果聚合和分析。

```yaml
packages:
  - package: xoniks/dbt_test_results
    version: ["1.0.0"]
```

#### dbt_model_usage

**包名：** `rjh336/dbt_model_usage`

跟踪 dbt 模型使用情况。

```yaml
packages:
  - package: rjh336/dbt_model_usage
    version: ["1.0.0"]
```

#### dbt_meta_testing

**包名：** `tnightengale/dbt_meta_testing`

元测试——测试你的测试是否有效。

```yaml
packages:
  - package: tnightengale/dbt_meta_testing
    version: ["0.3.0"]
```

---

### 3.9 其他社区包

| 包名 | 维护者 | 说明 |
|------|--------|------|
| `re_data` | `re-data` | 数据可靠性框架 |
| `dbt_eda_tools` | `shankararul` | 探索性数据分析 (EDA) |
| `dbt_duckdb_utils` | `sdebruyn` | DuckDB 工具宏 |
| `postgres_utils` | `sgoley` | PostgreSQL 工具宏 |
| `tsql_utils` | `dbt-msft` | T-SQL / SQL Server 工具宏 |
| `teradata_utils` | `Teradata` | Teradata 工具宏 |
| `trino_utils` | `starburstdata` | Trino/Starburst 工具宏 |
| `athena_utils` | `lalalilo` | AWS Athena 工具宏 |
| `materialize_dbt_utils` | `MaterializeInc` | Materialize 流式数据库工具 |
| `iceberg_utils` | `teoria` | Apache Iceberg 工具 |
| `dbt_airflow_macros` | `yu-iskw` | Airflow 集成宏 |
| `dbt_ops` | `yu-iskw` | 运维辅助 (clone schema, 表管理等) |
| `dbt_unittest` | `yu-iskw` | 另类单元测试方案 |
| `dbt_orphan` | `Matts52` | 检测孤立模型 |
| `dbt_diving` | `data-diving` | 数据探索和分析 |
| `dbt_scd2_utils` | `henryupton` | Type 2 SCD (缓慢变化维度) 工具 |
| `dbt_anomaly_detector` | `tripleaceme` | 异常检测 |
| `dbt_doc_inherit` | `tripleaceme` | 文档继承 |
| `dbt_fullstory` | `fullstorydev` | FullStory 会话分析集成 |
| `flexor` | `flexor-ai` | Flexor AI 分析 |
| `the_tuva_project` | `tuva-health` | 医疗健康数据模型 |
| `natural_language` | `Delphi-Data` | 自然语言数据查询 |
| `logs` | `dbt-labs/logging` | dbt 日志记录增强 |
| `metrics` | `dbt-labs/metrics` | MetricFlow 集成 |
| `snowplow_web` | `snowplow` | Snowplow 网络分析 |
| `snowplow_mobile` | `snowplow` | Snowplow 移动分析 |
| `snowplow_ecommerce` | `snowplow` | Snowplow 电商分析 |
| `ga4` | `Velir` | Google Analytics 4 |

---

> **文档版本：** 基于 dbt Core 1.9+ / Jinja 3.1+ / dbt Hub 2025 年数据整理
>
> **参考资料:**
> - [dbt Jinja Functions Reference](https://docs.getdbt.com/reference/dbt-jinja-functions)
> - [Jinja Template Designer Documentation](https://jinja.palletsprojects.com/en/3.1.x/templates/)
> - [dbt Hub](https://hub.getdbt.com/)
