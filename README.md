# spring-security-family
A demo &amp; share for how to use spring-security in micro-service system.

我们但凡做一个系统，这个系统不是在封闭环境中，不是只给一个人用，为了保证系统与数据安全，那么就会涉及到权限控制，权限控制这个东西可以说是很多系统的基础，因为我们不能让所有人对系统上的所有资源都进行同样的操作。

这篇分享与我们之前的分享一样，我们以一个框架与DEMO为切入口，会比较系统的讲到主题所指的内容，也会交叉一些其他知识点，其实技术这个东西也是熟能生巧的，我们如果不断把一些知识，一些细节融汇在一起反复提及，那么你慢慢就在潜移默化中掌握了。还是那句话，念念不忘必有回响。

喜欢先动手的小伙伴，或者急着需要使用的小伙伴，可以先取得对应代码，可以直接跑起来，代码、数据库脚本都有了，请戳这里->>>>>>[代码库地址](https://github.com/zerozhao13/spring-security-family)
![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly90aW1nc2EuYmFpZHUuY29tL3RpbWc_aW1hZ2UmcXVhbGl0eT04MCZzaXplPWI5OTk5XzEwMDAwJnNlYz0xNTk2OTk1NzMxNjE3JmRpPTRkNDkyNzc0MGFjODU0YTAwZmQ4ODU1ZGI4ZjliOWNkJmltZ3R5cGU9MCZzcmM9aHR0cDovL2JubXBwaWMuYm9va3V1LmNvbS9nb29kcy8xOC8xNy8wMi8yODczOTA2LWZtLmpwZw?x-oss-process=image/format,png)


在系统中，权限的控制大致可以分为三层：

 1. 展示层：就是这个东西要不要让用户看到，对于用来讲是最直观的，看不到或者不让操作，但是这一层通过一些技术手段是可能被攻破的，用户直接调用接口怎么办？

2. 控制层：这一层便是我们本次分享的主要内容，一个用户发起了接口调用请求，我们如何识别这个用户以及如何辨别这个用户能否进行该操作（这里就涉及到认证与鉴权，很多小伙伴容易将两者混为一谈）。这里的方式就会比较多样了，我们在这里先留下问题，在后面逐渐解答：1. 为什么控制层的权限控制比展示层可靠？2. 我们有哪些手段来进行控制？3. 分布式系统下如何进行控制？

3. 数据层：到了这里表示我们已经通过接口的鉴权了，那么还会有什么问题呢？我们举一个例子，我们可以在A网站修改自己的用户信息，但是不能修改别的用户的，这代表什么？我们每个用户都调用的同一个接口，代表我们都有访问该接口的权限。

最后我们对这三层控制做一个简单总结：
我能不能看到这个功能 -> 我能不能使用该功能 -> 我能不能操作该数据。

铺垫的内容就先到这里，便于大家从整体上对我们要做的事有个理解以及为什么要做这件事。

因为我们的分享是一个DEMO配合分享内容，所以在这里我们先大致讲讲要使用该DEMO可以做哪些前期准备，也帮助心急想吃热豆腐的小伙伴快速掌握DEMO，就像九阴真经不修练上卷直接练下卷，这当然是可以的。

# DEMO环境依赖

|环境/工具|备注|
| ------------ | ------------ |
| JDK 1.8  |   |
| Intellij IDEA 202001 社区版  |安装Spring Assistant插件|
| Docker Desktop Windows  | 数据库用的Docker镜像  |
| Mariadb 10.5.4| [文档地址](https://mariadb.org/) 如果有MySQL的小伙伴应该可以直接用替代，代码中客户端包替换为MySQL的 |
| HeidiSQL  |Mariadb 的桌面客户端工具  |
| [Springboot 2.3.1](https://spring.io/projects/spring-boot)  |   |
| [Spring Security 5](https://docs.spring.io/spring-security/site/docs/5.3.3.BUILD-SNAPSHOT/reference/html5/#samples)  |   |
|[JJWT](https://github.com/jwtk/jjwt)|JWT的java工具包|
|其它工具及框架|Lombok, Swagger2, Flyway|

# 基础知识
这里我们要再讲到一些基本知识点，不然到了后面的分享，我们讲到这些名词，大家的理解是不一样的，或者就不知道我们在讲什么那就尴尬了。

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly90aW1nc2EuYmFpZHUuY29tL3RpbWc_aW1hZ2UmcXVhbGl0eT04MCZzaXplPWI5OTk5XzEwMDAwJnNlYz0xNTk3MDM1NDA2NTExJmRpPWRkYmNkN2ZjNTQ2NGMwMmFhMGIxNjEzYTA4MTFjMjg2JmltZ3R5cGU9MCZzcmM9aHR0cDovL3d4MS5zaW5haW1nLmNuL2xhcmdlLzAwNm9PV2FoZ3kxZmdsc3ZieDJoOGozMGpnMGoxcTNlLmpwZw?x-oss-process=image/format,png)
## 认识权限名词两兄弟
这两兄弟，很多小伙伴容易搞混，或者觉得就是一个东西的两种说法，其实别人还是有区别的，下面我们就讲讲两者的区别。

### 认证 - authentication
举个简单例子，什么是认证呢？你登录系统，输入用户名密码证明你就是你，那么这就是认证。

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly90aW1nc2EuYmFpZHUuY29tL3RpbWc_aW1hZ2UmcXVhbGl0eT04MCZzaXplPWI5OTk5XzEwMDAwJnNlYz0xNTk3MDM3MjM3Mzk3JmRpPTVmMjdlZjNhNjUzNzM5MjhkOWVmZGU2YzBlYzRlZDBiJmltZ3R5cGU9MCZzcmM9aHR0cDovL2ltZzYuY2FjaGUubmV0ZWFzZS5jb20vbS8yMDE1LzYvMTgvMjAxNTA2MTgxODI2NDEzNDBhMy5wbmc?x-oss-process=image/format,png)

根据不同的安全级别，也有不同级别的认证方式，大致如下：

 - 单因素 身份验证 ：
   这是最简单的身份验证方法，通常依赖于简单的密码来授予用户对特定系统（如网站或网络）的访问权限。单因素身份验证的最常见示例是登录凭据，其仅需要针对用户名的密码。
 - 双因素身份验证 ：
它是一个两步验证过程，不仅需要用户名和密码，还需要用户知道的东西，以确保更高级别的安全性。使用用户名和密码以及额外的机密信息，冒充的代价就会更大了。
   
 - 多重身份验证：
   它使用来自独立身份验证类别的两个或更多级别的安全性来授予用户对系统的访问权限。所有因素应相互独立，以消除系统中的任何漏洞。现在常见的比如短信验证码、人脸识别、指纹识别等。

### 鉴权 - authorization
鉴权总在认证后，你登录了系统就能为所欲为了吗？如果这么认为那就是too young too simple，而控制你能干嘛不能干嘛的就是鉴权。

下面通过这个对比表格进一步帮助大家理解两者的区别：

|认证|授权|
|----|----|
|身份验证确认您的身份以授予对系统的访问权限。|授权确定您是否有权访问资源。|
|这是验证用户凭据以获得用户访问权限的过程。|这是验证是否允许访问的过程。|
|它决定用户是否是他声称的用户。|它确定用户可以访问和不访问的内容。|
|身份验证通常需要用户名和密码。|授权所需的身份验证因素可能有所不同，具体取决于安全级别。|
|身份验证是授权的第一步，因此始终是第一步。|授权在成功验证后完成。|
|例如，特定大学的学生在访问大学官方网站的学生链接之前需要进行身份验证。这称为身份验证。|例如，授权确定成功验证后学生有权在大学网站上访问哪些信息。|
## 客户端鉴权
这里所指的客户端是一个广义的客户端，意指使用Token并通过Token携带数据进行鉴权的系统，可能是APP，可能是H5，也可能是其他分散在世界各地的后端服务。

我们为什么要使用这样的鉴权呢？这样安全吗？

我们这样去想：
1. 用户信息及用户权限我们做了统一管理；
2. 我们有很多服务分散在各地，每个服务的权限控制可能各有自己的述求，我们把用户的角色提供了，资源要不要让用户访问操作，由各个服务自己决定；
3. 集中式进行鉴权带来的访问压力会比较大；
4. 如果用户的电脑、手机就能帮忙做一道拦截会大大减少可能的无效请求。

那我们要怎么做呢？基于session-cookie的体系就不行了，我们需要让请求是无状态的，那么我们就要用到Token。

### Token 
一个客户端鉴权的Token流程：
1. 客户端使用用户名跟密码请求登录；
2. 服务端收到请求，验证用户名与密码；
3. 验证成功后，服务端会签发一个 Token，Token包含了用户鉴权需要的基本信息，再把这个 Token 发送给客户端；
4. 客户端收到 Token 以后可以把它（或者角色信息）存储起来，比如放在 Cookie 里或者 Local Storage或者Local Session 里；
5. 客户端每次向服务端请求资源的时候先解析Token，看看用户的角色是否符合请求资源的要求，如果符合需要则带着 Token一并请求给服务提供端；
6. 服务端收到请求，然后去验证客户端请求里面带着的 Token及进行鉴权，如果验证成功，就向客户端返回请求的数据。

那么这个Token有没有比较标准化的实践呢？使用比较广泛的由[JWT - JSON Web Token](https://jwt.io/)，那么接下来我们就来简单分享一下JWT。
### JWT
JSON Web Token（JWT）是一个非常轻巧的规范。这个规范允许我们使用JWT在用户和服务器之间传递安全可靠的信息。

一个JWT就是一个字符串，它由三部分组成，头部、载荷与签名，每个部分之间用·符号分隔（注意左边真的由一个符号）。

#### 头部（Header）

头部用于描述关于该JWT的最基本的信息，例如其类型以及签名所用的算法等。这也可以被表示成一个JSON对象。

{"typ":"JWT","alg":"HS256"}

在头部指明了签名算法是HS256算法。 最后这个头部会被BASE64编码得到一个字符串。

#### 载荷（playload）

载荷是存放Token需要携带的有效信息的地方。这些有效信息包含三个部分：

（1）标准中注册的声明（建议但不强制使用）

```javascript
iss: jwt签发者
sub: jwt所面向的用户
aud: 接收jwt的一方
exp: jwt的过期时间，这个过期时间必须要大于签发时间
nbf: 定义在什么时间之前，该jwt都是不可用的.
iat: jwt的签发时间
jti: jwt的唯一身份标识，主要用来作为一次性token。
```

（2）公共的声明

公共的声明可以添加任何的信息，一般添加用户的相关信息或其他业务需要的必要信息.但不建议添加敏感信息，因为该部分在客户端可解密.

（3）私有的声明

私有声明是提供者和消费者所共同定义的声明，不建议存放敏感信息如密码等，因为载荷中的数据是需要能被解密的，意味着该部分信息可以归类为明文信息。

载荷部分自己定义的数据内容，需要让Token使用方知道如何解析如何使用，比较好的方式就是提供SDK给到使用方。

#### 签名（signature）

jwt的第三部分是一个签证信息，这个签证信息由三部分组成：

```javascript
header (base64后的)

payload (base64后的)

secret
```

这个部分需要base64加密后的header和base64加密后的payload使用.连接组成的字符串，然后通过header中声明的加密方式进行加盐secret组合加密，然后就构成了jwt的第三部分。

如果我们集中式鉴权，那么这个Token这样就没问题了，可是如果我们要做上面的客户端健全分布式鉴权，如何保证Token的安全可靠呢？这就需要加入加解密体系。

## 加解密体系
这是一个很庞大也很复杂的体系，我们就讲讲我们要在这次分享中使用到的部分。
### 非对称加密
非对称加密是相对于对称加密的，对称加密是指加解密都使用同一把密钥，就像我们锁门与开门，如果这把钥匙丢了，坏人就能打开门也能锁上门了。
而非对称加密则是包含两把密钥，一把加密一把解密，作为研发人员最熟悉的场景就是使用github或gitlab，我们会生成一个RSA的SSH密钥对，在本机保存私钥文件，在远端存放着公钥。
### RSA
#### 加解密方式

 - 公钥加密私钥解密
 - **私钥加密公钥解密**（这将是我们要使用的方式）
 - 私钥加密私钥解密

#### 密钥长度
RSA 是目前应用最广泛的数字加密和签名技术，比如国内的支付宝就是通过RSA算法来进行签名验证。它的安全程度取决于秘钥的长度，目前主流可选秘钥长度为 1024位、2048位、4096位等，理论上秘钥越长越难于破解，按照维基百科上的说法，小于等于256位的秘钥，在一台个人电脑上花几个小时就能被破解，512位的秘钥和768位的秘钥也分别在1999年和2009年被成功破解，虽然目前还没有公开资料证实有人能够成功破解1024位的秘钥，但显然距离这个节点也并不遥远，所以目前业界推荐使用 2048 位或以上的秘钥，不过目前看 2048 位的秘钥已经足够安全了，支付宝的官方文档上推荐也是2048位，当然更长的秘钥更安全，但也意味着会产生更大的性能开销。

### 国密
这里我们不做深入分享，只做一些科普。

国密即国家密码局认定的国产密码算法，即商用密码。

国密算法是国家密码局制定标准的一系列算法。其中包括了对称加密算法，椭圆曲线非对称加密算法，杂凑算法。具体包括SM1, SM2, SM3, SM4, SM7, SM9, 祖冲之密码算法等，其中：

 - SM1

对称加密算法，加密强度为128位，采用硬件实现，该算法不公开；

 - SM2 - 替代RSA

为国家密码管理局公布的公钥算法，其加密强度为256位，官方文档显示比RSA性能好，我们在DEMO中实测下来与官方有些差异。

 - SM3 - 替代MD5、SHA-1

密码杂凑算法，杂凑值长度为32字节，和SM2算法同期公布，参见《国家密码管理局公告（第 22 号）》；

 - SM4 - 替代DES、3DES、AES

对称加密算法，随WAPI标准一起公布，可使用软件实现，加密强度为128位。

# Spring Security
我们分享Spring Security依然希望大家在理解的基础上，未来可以选择Shiro也可以自己去写实现。

而且目前Spring Security在Springboot的加持下，基本使用已经很简单了，一个配置类几个注解，其实已经可以满足很多的应用了。

但是我们的目标是什么呢？就是让业务的灵活变动尽量少修改代码甚至不修改代码，能用工具给到业务部门自己弄的就不用配置文件，能用配置文件的就不用改代码。

带着这样的目标，我们就需要对其有基本的了解（[想要深入了解?](https://docs.spring.io/spring-security/site/docs/5.3.3.BUILD-SNAPSHOT/reference/html5/)）

## 简介
Spring Security 是一个可以帮助我们做用户认证、鉴权、以及预防一些基础攻击的框架。

因为我们在分享中使用的Spring Security 5，所以需要的JDK版本必须是8以上。

Spring Security的实现是基于其内部的一系列 Filter，我们可以在这些Filter基础上实现一些自己的Filter，来实现我们自定义的认证、鉴权及一些基础防御。

它支持基于Servlet、WebFlux的接口进行权限控制。

## 基本工作流程
我们先看一个用户在Spring Security的生命周期大概经历哪些过程：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810134646772.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

我们简单解释一下上图：
Spring Security的核心配置类是 WebSecurityConfigurerAdapter这个抽象类
这是权限管理启动的入口，我们在实现自己的应用时需要自己去实现这个类，里面根据我们自己的需求去做一些配置。

进入这个过程之后，我们的系统会经历一系列的Filter来层层过滤请求，保障我们的系统安全。

因此理解一些主要Filter对于理解后面的DEMO会有所帮助。

### SecurityFilterChain

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810142029920.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

### Security Filters

 - ChannelProcessingFilter
 - ConcurrentSessionFilter
   
   WebAsyncManagerIntegrationFilter
   
   SecurityContextPersistenceFilter
   
   HeaderWriterFilter
   
   CorsFilter
   
   CsrfFilter
   
   LogoutFilter
   
   OAuth2AuthorizationRequestRedirectFilter
   
   Saml2WebSsoAuthenticationRequestFilter
   
   X509AuthenticationFilter
   
   AbstractPreAuthenticatedProcessingFilter
   
   CasAuthenticationFilter
   
   OAuth2LoginAuthenticationFilter
   
   Saml2WebSsoAuthenticationFilter
   
   UsernamePasswordAuthenticationFilter
   
   ConcurrentSessionFilter
   
   OpenIDAuthenticationFilter
   
   DefaultLoginPageGeneratingFilter
   
   DefaultLogoutPageGeneratingFilter
   
   DigestAuthenticationFilter
   
   BearerTokenAuthenticationFilter
   
   BasicAuthenticationFilter
   
   RequestCacheAwareFilter
   
   SecurityContextHolderAwareRequestFilter
   
   JaasApiIntegrationFilter
   
   RememberMeAuthenticationFilter
   
   AnonymousAuthenticationFilter
   
   OAuth2AuthorizationCodeGrantFilter
   
   SessionManagementFilter
   
   ExceptionTranslationFilter
   
   FilterSecurityInterceptor
   
   SwitchUserFilter

### 处理异常

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810142925794.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

### 用户认证

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810143042168.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)


![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810143057877.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

### UserDetails

### UserDetailsService

### 基于用户名密码验证

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810144000404.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

上图的大致流程如下：

1. AbstractAuthenticationProcessingFilter#doFilter()
2. UsernamePasswordAuthenticationFilter#attemptAuthentication()
3. ProviderManager#authenticate()
4. AbstractUserDetailsAuthenticationProvider#authenticate()
5. DaoAuthenticationProvider#retrieveUser()
6. UserDetailsService#loadUserByUsername()

### AuthenticationProvider

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810143302863.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)

### 鉴权

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200810143631497.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjI4ODIxOQ==,size_16,color_FFFFFF,t_70)
