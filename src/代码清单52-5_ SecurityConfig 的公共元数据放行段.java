// 前略：package、import 区、@Order(1) 的 oauth 独立放行链与 filterChain 开头数行，见源文件
                authz
                    .requestMatchers("/api/v1/auth/**")
                    .permitAll()
                    // M04.F01.I06 — 公共 client 元数据（用于登录页应用选择）匿名可读。
                    // 严格匹配 /api/v1/clients/{clientId}，不豁免 /api/v1/admin/clients/{clientId}（admin 仍需
                    // auth）。
                    .requestMatchers("/api/v1/clients/*")
                    .permitAll()
                    // v0.1.12 起: 容器内 Docker HEALTHCHECK 与外部 deploy 脚本都直接
                    // wget /actuator/health; 不带 JWT 走不到 controller, 401 让
                    // healthcheck 失败, deploy 脚本 120 次都进不了 '200'. permitAll
                    // 让 health probe 路径免 auth, 不影响业务 endpoint.
                    .requestMatchers("/actuator/**")
                    .permitAll()