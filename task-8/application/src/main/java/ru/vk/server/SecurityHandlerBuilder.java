package ru.vk.server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityHandlerBuilder {
    private static final String ROLE_MANAGER = "Manager";
    private static final String ROLE_GUEST = "Guest";

    private final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

    public final ConstraintSecurityHandler build(LoginService loginService) {
        securityHandler.setLoginService(loginService);

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        constraintMappings.addAll(constraintPostMapping(
                buildConstraint(ROLE_MANAGER),
                new ArrayList<>(List.of(new String[]{"/products", "/products/*"}))));

        constraintMappings.addAll(constraintGetMapping(
                buildConstraint(ROLE_GUEST, ROLE_MANAGER),
                new ArrayList<>(List.of(new String[]{"/products", "/products/*"}))));

        securityHandler.setConstraintMappings(constraintMappings);
        securityHandler.setAuthenticator(new BasicAuthenticator());
        securityHandler.setDenyUncoveredHttpMethods(true);
        return securityHandler;
    }


    private static Constraint buildConstraint(String... userRoles) {
        final Constraint starterConstraint = new Constraint();
        starterConstraint.setName(Constraint.__BASIC_AUTH);
        starterConstraint.setRoles(userRoles);
        starterConstraint.setAuthenticate(true);
        return starterConstraint;
    }

    private static Collection<ConstraintMapping> constraintGetMapping(Constraint constraint,
                                                                      Collection<String> paths) {
        return constraintMapping(constraint, paths, "GET");
    }


    private static Collection<ConstraintMapping> constraintPostMapping(Constraint constraint,
                                                                       Collection<String> paths) {
        return constraintMapping(constraint, paths, "POST");
    }

    private static Collection<ConstraintMapping> constraintMapping(Constraint constraint,
                                                                   Collection<String> paths,
                                                                   String method) {
        return paths.stream()
                .map(path -> {
                            final ConstraintMapping mapping = new ConstraintMapping();
                            mapping.setConstraint(constraint);
                            mapping.setPathSpec(path);
                            mapping.setMethod(method);
                            return mapping;
                        }
                ).collect(Collectors.toList());
    }
}
