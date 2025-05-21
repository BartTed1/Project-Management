package xyz.teodorowicz.pm.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SystemRole(vararg val roles: xyz.teodorowicz.pm.enumeration.user.SystemRole)
