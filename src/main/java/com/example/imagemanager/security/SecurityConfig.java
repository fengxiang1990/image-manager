package com.example.imagemanager.security;

import com.example.imagemanager.Const;
import com.example.imagemanager.admin.model.LoginResult;
import com.example.imagemanager.admin.model.Ticket;
import com.example.imagemanager.admin.service.SysUserPermissionService;
import com.example.imagemanager.admin.service.SysUserService;
import com.example.imagemanager.cache.SysUserCache;
import com.example.imagemanager.model.BaseResponse;
import com.example.imagemanager.utils.JsonUtils;
import com.example.imagemanager.utils.SHAUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import liquibase.pro.packaged.E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.*;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.*;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    SecurityUserService userService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserPermissionService sysUserPermissionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private void saveToCache(String uid, Authentication authentication) {
        redisTemplate.opsForHash().put(Const.REDIS_SECURE_AUTHENTICATION_CACHE, uid, authentication);
    }

    @Bean
    JsonLoginFilter jsonLoginFilter() {
        JsonLoginFilter filter = new JsonLoginFilter();
        filter.setAuthenticationSuccessHandler((req, resp, auth) -> {
            System.out.println("JsonLoginFilter setAuthenticationSuccessHandler uid->" + req.getHeader("uid"));

            req.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            //获取当前登录成功的用户对象
            SecurityUserDetails user = (SecurityUserDetails) auth.getPrincipal();
            //登录成功应该用新登录的UID来存储 req.getHeader("uid") 取到的是前一个用户的
            saveToCache(user.getId(), auth);
            BaseResponse<LoginResult> respBean = new BaseResponse<>();
            LoginResult loginResult = new LoginResult();
            loginResult.setUid(user.getId());
            loginResult.setUsername(user.getUsername());
            //创建一个登录成功的票据
            //后面放入redis中
            Ticket ticket = new Ticket();
            long current = System.currentTimeMillis();
            //当前时间加过期目标时间
            ticket.setExpiry(current + Ticket.DEFAULT_EXPIRY_DURATION);
            ticket.setLoginTime(current);
            int random = new Random().nextInt(10000000);
            String str = current + user.getId() + user.getUsername() + random;
            String token = SHAUtils.getSHA256Hash(str);
            if (token == null || token.isEmpty()) {
                LOGGER.debug("login fail,token create failed");
                respBean.setRet(BaseResponse.FAILED);
                respBean.setErr(BaseResponse.ERR_LOGIN_TOKEN_CREATE_ERR);
                respBean.setMsg("login fail,token create failed");
                respBean.setErr(BaseResponse.ERR_LOGIN_USER_NOT_FOUND);
            }
            LOGGER.debug("login suc,token:" + token);
            ticket.setToken(token);
            loginResult.setTicket(ticket);
            loginResult.setSysRole(sysUserPermissionService.getSysRoleByUid(user.getId()));
            sysUserService.updateUserLastLoginTime(user.getUsername(), LocalDateTime.now());
            respBean.setRet(BaseResponse.SUCCESS);
            respBean.setData(loginResult);
            //登录信息放入redis中
            Object obj = null;
            try {
                obj = redisTemplate.opsForValue().get(user.getId());
            } catch (Exception e) {

            }
            SysUserCache sysUserCache;
            if (obj instanceof String) {
                sysUserCache = JsonUtils.fromJson((String) obj, SysUserCache.class);
                sysUserCache.setTicket(ticket);
            } else {
                sysUserCache = new SysUserCache();
                sysUserCache.setTicket(ticket);
                sysUserCache.setUid(user.getId());
            }
            redisTemplate.opsForValue().set(user.getId(), JsonUtils.toJson(sysUserCache));
            out.write(new ObjectMapper().writeValueAsString(respBean));
        });
        filter.setAuthenticationFailureHandler((req, resp, e) -> {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            BaseResponse<LoginResult> respBean = new BaseResponse<>();
            respBean.setRet(BaseResponse.FAILED);
            respBean.setErr(BaseResponse.ERR_LOGIN_ERR_SECURITY);
            if (e instanceof BadCredentialsException) {
                respBean.setMsg("用户名或者密码输入错误，登录失败");
            } else if (e instanceof DisabledException) {
                respBean.setMsg("账户被禁用，登录失败");
            } else if (e instanceof CredentialsExpiredException) {
                respBean.setMsg("密码过期，登录失败");
            } else if (e instanceof AccountExpiredException) {
                respBean.setMsg("账户过期，登录失败");
            } else if (e instanceof LockedException) {
                respBean.setMsg("账户被锁定，登录失败");
            }
            out.write(new ObjectMapper().writeValueAsString(respBean));
        });
        filter.setAuthenticationManager(authenticationManager());
        filter.setPasswordParameter("password");
        filter.setUsernameParameter("username");
        filter.setFilterProcessesUrl("/admin/login");
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/admin/**")
                        //这种方法也可以自定义权限判断逻辑
//                        .access(new AuthorizationManager<RequestAuthorizationContext>() {
//                            @Override
//                            public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
//                                Object obj = redisTemplate.opsForHash().get(Const.REDIS_SECURE_AUTHENTICATION_CACHE, object.getRequest().getHeader("uid"));
//                                Authentication currentAuthentication;
//                                boolean authSuc = false;
//                                if (obj instanceof Authentication) {
//                                    currentAuthentication = (Authentication) obj;
//                                    if (currentAuthentication.getAuthorities() != null) {
//                                        for (GrantedAuthority grantedAuthority : currentAuthentication.getAuthorities()) {
//                                            String authName = grantedAuthority.getAuthority();
//                                            if ("ROLE_ADMIN".equals(authName) || "ROLE_441".equals(authName)) {
//                                                authSuc = true;
//                                            }
//                                        }
//                                    }
//                                }
//                                return new AuthorizationDecision(authSuc);
//                            }
//                        })
                        .hasAnyAuthority("admin", "admin_normal", "admin_content_manager")
                        .anyRequest().authenticated()
        );
        //禁用匿名认证
        http.anonymous(AbstractHttpConfigurer::disable);
        http.formLogin((form) -> form
                .loginPage("/admin/login")
                .permitAll()
        );
        http.addFilter(jsonLoginFilter());
        http.addFilterBefore(myFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthorizationEventPublisher authorizationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new SpringAuthorizationEventPublisher(applicationEventPublisher);
    }

    @Component
    public static class AuthenticationEvents {

        @EventListener
        public void onFailure(AuthorizationDeniedEvent failure) {
            LOGGER.debug("AuthenticationEvents onFailure:" + failure);
        }
    }

    @Bean
    MyFilter myFilter() {
        MyFilter filter = new MyFilter();
        return filter;
    }


    /**
     * 这里为什么要这么做参考文章
     * https://blog.csdn.net/g759780748/article/details/122873069
     * FilterChainProxy  this.securityContextHolderStrategy.clearContext(); content 被清空了
     * 需要重新设置一下 否则http.authorizeHttpRequests.access 中无法获取认证token
     */
    @Component
    public class MyFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            System.out.println("MyFilter doFilterInternal uid->" + request.getHeader("uid"));
            if (request.getHeader("uid") == null) {
                filterChain.doFilter(request, response);
                return;
            }
            Object obj = redisTemplate.opsForHash().get(Const.REDIS_SECURE_AUTHENTICATION_CACHE, request.getHeader("uid"));
            if (obj instanceof Authentication) {
                SecurityContextHolder.getContext().setAuthentication((Authentication) obj);
            }
            filterChain.doFilter(request, response);
        }
    }

}
